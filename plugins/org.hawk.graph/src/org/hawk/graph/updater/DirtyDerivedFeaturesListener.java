/*******************************************************************************
 * Copyright (c) 2011-2018 The University of York, Aston University.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0
 *
 * Contributors:
 *     Konstantinos Barmpis - original idea and implementation
 *     Antonio Garcia-Dominguez - rearrange as graph change listener,
 *       generalise to derived features
 ******************************************************************************/
package org.hawk.graph.updater;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.hawk.core.IModelIndexer;
import org.hawk.core.VcsCommitItem;
import org.hawk.core.graph.IGraphChangeListener;
import org.hawk.core.graph.IGraphDatabase;
import org.hawk.core.graph.IGraphDatabase.Mode;
import org.hawk.core.graph.IGraphEdge;
import org.hawk.core.graph.IGraphIterable;
import org.hawk.core.graph.IGraphNode;
import org.hawk.core.graph.IGraphNodeIndex;
import org.hawk.core.graph.IGraphTransaction;
import org.hawk.core.model.IHawkClass;
import org.hawk.core.model.IHawkObject;
import org.hawk.core.model.IHawkPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graph change listener that collects the graph nodes whose derived features
 * should be updated, and marks them as dirty on the fly with special properties.
 */
public class DirtyDerivedFeaturesListener implements IGraphChangeListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirtyDerivedFeaturesListener.class);

	public static final String NOT_YET_DERIVED_PREFIX = "_NYD##";

	private final class PendingEntry implements Entry<String, String> {
		String key;
		String value;

		public PendingEntry(String k, String v) {
			key = k;
			value = v;
		}

		@Override
		public String setValue(String value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public int hashCode() {
			final int prime = 29;
			int result = Integer.MIN_VALUE;
			result += key.hashCode() * prime ^ 1;
			result += value.hashCode() * prime ^ 2;
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry<?, ?>)
				return ((Entry<?, ?>) o).getKey().equals(key)
						&& ((Entry<?, ?>) o).getValue().equals(value);
			else
				return false;
		}

		@Override
		public String toString() {
			return "PendingEntry [key=" + key + ", value=" + value + "]";
		}
	}

	private final Set<IGraphNode> nodesToBeUpdated = new HashSet<>();
	private IGraphDatabase db;
	private Set<IGraphNode> markedForRemoval = new HashSet<>();
	private Set<Entry<String, String>> pending = new HashSet<>();

	public DirtyDerivedFeaturesListener(IGraphDatabase graph) {
		this.db = graph;
	}

	public Set<IGraphNode> getNodesToBeUpdated() {
		resolvePending();
		nodesToBeUpdated.removeAll(markedForRemoval);
		return nodesToBeUpdated;
	}

	private void resolvePending() {
		Set<IGraphNode> toBeUpdated = new HashSet<>();

		try {
			IGraphTransaction t = null;
			if (db.currentMode().equals(Mode.TX_MODE)) {
				t = db.beginTransaction();
			}

			final IGraphNodeIndex idx = db.getOrCreateNodeIndex(GraphModelInserter.DERIVED_ACCESS_IDXNAME);

			// Do a quick check first if there are *any* derived attributes:
			// repeated checks are very expensive in Neo4j due to Lucene query
			// parsing.
			IGraphIterable<? extends IGraphNode> anyResults = idx.query("*", "*");
			if (anyResults.iterator().hasNext()) {
				for (Entry<String, String> e : pending) {
					for (IGraphNode n : idx.query(e.getKey(), e.getValue())) {
						toBeUpdated.add(n);
					}
				}
			}

			if (toBeUpdated.size() > 0) {

				if (t == null)
					t = db.beginTransaction();

				for (IGraphNode n : toBeUpdated)
					markDependentToBeUpdated(n);

			}

			if (t != null) {
				t.success();
				t.close();
			}

		} catch (Exception e) {
			LOGGER.error("Marking of derived attributes needing update failed", e);
		}

		pending.clear();
	}

	@Override
	public String getHumanReadableName() {
		return "Internal listener for finding dirty attributes";
	}

	@Override
	public void synchroniseStart() {
		// nothing to do
	}

	@Override
	public void synchroniseEnd() {
		// nothing to do
	}

	@Override
	public void changeStart() {
		// nothing to do
	}

	@Override
	public void changeSuccess() {
		// nothing to do
	}

	@Override
	public void changeFailure() {
		nodesToBeUpdated.clear();
		pending.clear();
	}

	@Override
	public void metamodelAddition(IHawkPackage pkg, IGraphNode pkgNode) {
		// nothing to do
	}

	@Override
	public void classAddition(IHawkClass cls, IGraphNode clsNode) {
		// nothing to do
	}

	@Override
	public void fileAddition(VcsCommitItem s, IGraphNode fileNode) {
		// nothing to do
	}

	@Override
	public void fileRemoval(VcsCommitItem s, IGraphNode fileNode) {
		// nothing to do
	}

	@Override
	public void modelElementAddition(VcsCommitItem s, IHawkObject element,
			IGraphNode elementNode, boolean isTransient) {
		if (!isTransient) {
			markDependentToBeUpdated(elementNode.getId().toString());
		}
	}

	@Override
	public void modelElementRemoval(VcsCommitItem s, IGraphNode elementNode,
			boolean isTransient) {

		if (!isTransient) {
			markDependentToBeUpdated(elementNode.getId().toString());
		} else {
			markForRemoval(elementNode);
		}
	}

	private void markForRemoval(IGraphNode elementNode) {
		markedForRemoval.add(elementNode);

	}

	private Entry<String, String> createEntry(String k, String v) {
		Entry<String, String> ret = new PendingEntry(k, v);
		return ret;
	}

	@Override
	public void modelElementAttributeUpdate(VcsCommitItem s,
			IHawkObject eObject, String attrName, Object oldValue,
			Object newValue, IGraphNode elementNode, boolean isTransient) {
		if (!isTransient) {
			pending.add(createEntry(elementNode.getId().toString(), attrName));
		}
	}

	@Override
	public void modelElementAttributeRemoval(VcsCommitItem s,
			IHawkObject eObject, String attrName, IGraphNode node,
			boolean isTransient) {
		if (!isTransient) {
			pending.add(createEntry(node.getId().toString(), attrName));
		}
	}

	@Override
	public void referenceAddition(VcsCommitItem s, IGraphNode source,
			IGraphNode destination, String edgelabel, boolean isTransient) {
		if (!isTransient) {
			pending.add(createEntry(source.getId().toString(), edgelabel));
		}
	}

	@Override
	public void referenceRemoval(VcsCommitItem s, IGraphNode source,
			IGraphNode destination, String edgelabel, boolean isTransient) {
		if (!isTransient) {
			pending.add(createEntry(source.getId().toString(), edgelabel));
		}
	}

	private void markDependentToBeUpdated(final String key) {
		pending.add(createEntry(key, "*"));
	}

	private void markDependentToBeUpdated(IGraphNode node) {
		final Iterable<IGraphEdge> incoming = node.getIncoming();
		final IGraphEdge firstIncoming = incoming.iterator().next();
		final String derivedPropertyName = firstIncoming.getType();

		node.setProperty(derivedPropertyName,
			NOT_YET_DERIVED_PREFIX + node.getProperty("derivationlogic"));
		nodesToBeUpdated.add(node);
	}

	@Override
	public void setModelIndexer(IModelIndexer m) {
		// not used
	}

}
