package org.hawk.graph.updater;

import java.util.HashSet;

import org.hawk.core.graph.IGraphDatabase;
import org.hawk.core.graph.IGraphEdge;
import org.hawk.core.graph.IGraphNode;
import org.hawk.core.graph.IGraphNodeIndex;
import org.hawk.core.graph.IGraphTransaction;

public class DeletionUtils {

	private IGraphDatabase graph;
	private IGraphNodeIndex filedictionary;
	private IGraphNodeIndex proxydictionary;

	public DeletionUtils(IGraphDatabase graph) {
		this.graph = graph;
		try (IGraphTransaction t = graph.beginTransaction()) {
			filedictionary = graph.getFileIndex();
			proxydictionary = graph.getOrCreateNodeIndex("proxydictionary");
			t.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void delete(IGraphNode modelElement) {

		try {
			// FIXME maintain indexes (create api to remove a node from all
			// indexes???)
			modelElement.delete();
		} catch (Exception e) {
			System.err.println("DELETE NODE EXCEPTION:");
			e.printStackTrace();
		}

	}

	protected void deleteAll(String filepath) throws Exception {

		long start = System.currentTimeMillis();

		try (IGraphTransaction transaction = graph.beginTransaction()) {

			IGraphNode file = graph.getFileIndex().get("id", filepath)
					.iterator().next();

			System.out.println("deleting nodes from file: "
					+ file.getProperty("id"));

			HashSet<IGraphNode> modelElements = new HashSet<IGraphNode>();

			for (IGraphEdge rel : file.getIncomingWithType("file")) {
				modelElements.add(rel.getStartNode());
				rel.delete();
			}

			for (IGraphNode node : modelElements) {
				dereference(node);
			}

			for (IGraphNode node : modelElements) {
				makeProxyRefs(node, file);
			}

			for (IGraphNode node : modelElements) {
				delete(node);
			}

			modelElements = null;

			filedictionary = graph.getFileIndex();

			filedictionary.remove(file);
			delete(file);

			transaction.success();
		}

		System.out.println("deleted all, took: "
				+ (System.currentTimeMillis() - start) / 1000 + "s"
				+ (System.currentTimeMillis() - start) / 1000 + "ms");

	}

	protected String getRelativeURI(String uri) {
		// TODO volatile syntax: need to change if all files not in temp
		// directory -- find alternative? -- or stick to pre-set structure

		int sub = uri.toString().indexOf("/temp/");

		return uri.substring((sub > -1) ? sub + 6 : 0);
		// return uri;
	}

	protected String[] add(String[] proxies, String relativeURI,
			String edgelabel) {

		if (proxies != null) {

			String[] ret = new String[proxies.length + 2];

			for (int i = 0; i < proxies.length; i = i + 2) {

				ret[i] = proxies[i];
				ret[i + 1] = proxies[i + 1];

			}

			ret[proxies.length] = relativeURI;
			ret[proxies.length + 1] = edgelabel;

			proxies = null;

			return ret;

		} else {
			String[] ret = new String[2];
			ret[0] = relativeURI;
			ret[1] = edgelabel;
			return ret;
		}
	}

	protected void makeProxyRefs(IGraphNode modelElement, IGraphNode fileNode) {

		// handle any incoming references (after dereference, aka other file
		// ones)
		// FIXMEdone 5-11-13 check changes work
		for (IGraphEdge rel : modelElement.getIncoming()) {

			IGraphNode referencingNode = rel.getStartNode();
			String referencingNodeFileID = (String) referencingNode
					.getOutgoingWithType("file").iterator().next().getEndNode()
					.getProperty("id");
			String fileID = (String) fileNode.getProperty("id");

			// System.out.println(referencingNodeFileID+" ::: "+fileID);

			if (!referencingNodeFileID.equals(fileID)) {

				// System.err.println("relationship is one to an object in file: "
				// + rel.getStartNode()
				// .getRelationships(
				// Direction.OUTGOING,
				// new RelationshipUtil()
				// .getNewRelationshipType("file"))
				// .iterator().next().getEndNode().getProperty("id"));

				String relativeFileURI = new DeletionUtils(graph)
						.getRelativeURI(fileID.toString());

				String relativeElementURI = relativeFileURI + "#"
						+ modelElement.getProperty("id").toString();

				Object proxies = null;

				if (referencingNode.getProperty("_proxyRef:" + relativeFileURI) != null) {
					proxies = referencingNode.getProperty("_proxyRef:"
							+ relativeFileURI);
				}
				proxies = new DeletionUtils(graph).add((String[]) proxies,
						relativeElementURI, rel.getType().toString());

				referencingNode.setProperty("_proxyRef:" + relativeFileURI,
						proxies);

				proxydictionary = graph.getOrCreateNodeIndex("proxydictionary");
				proxydictionary.add(referencingNode, "_proxyRef",
						relativeFileURI);

				rel.delete();

			} else {
				// same file so just delete
				rel.delete();
			}

		}

	}

	protected void dereference(IGraphNode modelElement) {

		for (IGraphEdge rel : modelElement.getOutgoing()) {

			// delete derived attributes stored as nodes
			if (rel.getProperty("isDerived") != null)
				rel.getEndNode().delete();

			rel.delete();

		}

	}

	public void delete(IGraphEdge rel) {

		try {
			rel.delete();
		} catch (Exception e) {
			System.err.println("DELETE NODE EXCEPTION:");
			e.printStackTrace();
		}

	}

}
