/*******************************************************************************
 * Copyright (c) 2011-2016 The University of York.
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
 *     Antonio Garcia-Dominguez - initial API and implementation
 ******************************************************************************/
package org.hawk.graph.sampleListener;

import java.util.Arrays;

import org.hawk.core.IModelIndexer;
import org.hawk.core.VcsCommitItem;
import org.hawk.core.graph.IGraphChangeListener;
import org.hawk.core.graph.IGraphNode;
import org.hawk.core.model.IHawkClass;
import org.hawk.core.model.IHawkObject;
import org.hawk.core.model.IHawkPackage;

public class ExampleListener implements IGraphChangeListener {

	@Override
	public void synchroniseStart() {
		// System.out.println("synchronize started");
	}

	@Override
	public void synchroniseEnd() {
		// System.out.println("synchronize ended");
	}

	@Override
	public void changeStart() {
		// System.out.println("change started");
	}

	@Override
	public void changeSuccess() {
		// System.out.println("change succeeded");
	}

	@Override
	public void changeFailure() {
		// System.out.println("change failed");
	}

	@Override
	public void metamodelAddition(IHawkPackage pkg, IGraphNode pkgNode) {
		// System.out.println(String.format("metamodel %s added as node %s",
		// pkg.getNsURI(), pkgNode.getId()));
	}

	@Override
	public void classAddition(IHawkClass cls, IGraphNode clsNode) {
		// System.out.println(String.format("class %s added as node %s",
		// cls.getName(), clsNode.getId()));
	}

	@Override
	public void fileAddition(VcsCommitItem s, IGraphNode fileNode) {
		// System.out.println(String.format("rev %s: file %s added as node %s",
		// s.getCommit().getRevision(), s.getPath(), fileNode.getId()));
	}

	@Override
	public void fileRemoval(VcsCommitItem s, IGraphNode fileNode) {
		// System.out.println(String.format("rev %s: file %s removed as node %s",
		// s.getCommit().getRevision(), s.getPath(), fileNode.getId()));
	}

	@Override
	public void modelElementAddition(VcsCommitItem s, IHawkObject element,
			IGraphNode elementNode, boolean isTransient) {
		// System.out.println(String.format("rev %s: instance of %s in %s added as node %s",
		// s.getCommit().getRevision(), element.getType().getName(),
		// s.getPath(), elementNode.getId()));
	}

	@Override
	public void modelElementRemoval(VcsCommitItem s, IGraphNode elementNode,
			boolean isTransient) {
		// System.out.println(String.format("rev %s: model element in node %s removed",
		// s.getCommit().getRevision(), elementNode.getId()));
	}

	@Override
	public void modelElementAttributeUpdate(VcsCommitItem s,
			IHawkObject eObject, String attrName, Object oldValue,
			Object newValue, IGraphNode elementNode, boolean isTransient) {

		//String oldv = convertToString(oldValue);
		//String newv = convertToString(newValue);

		// if (oldv.equals("1098.0") || oldv.equals("1097.0") ||
		// newv.equals("1098.0") || newv.equals("1097.0"))

		// System.out.println(String.format("rev %s: attribute %s in node %s changed from '%s' to '%s'",
		// s
		// .getCommit().getRevision(), attrName, elementNode.getId(), oldv,
		// newv)
		// + " [id:"+
		// elementNode.getProperty(IModelIndexer.IDENTIFIER_PROPERTY)+ "]");
	}

	protected String convertToString(Object value) {

		String ret = value + "";

		if (value != null) {

			Class<?> c = value.getClass().getComponentType();

			if (c != null) {
				if (value instanceof Object[])
					ret = Arrays.toString((Object[]) value);
				else if (value instanceof byte[])
					ret = Arrays.toString((byte[]) value);
				else
					System.err
							.println("convertToString failed, value is of type: "
									+ value.getClass());
			}
		}

		return ret;

	}

	@Override
	public void modelElementAttributeRemoval(VcsCommitItem s,
			IHawkObject eObject, String attrName, IGraphNode node,
			boolean isTransient) {
		// System.out.println(String.format("rev %s: attribute %s in node %s unset",
		// s.getCommit().getRevision(), attrName, node.getId()));
	}

	@Override
	public void referenceAddition(VcsCommitItem s, IGraphNode source,
			IGraphNode destination, String edgelabel, boolean isTransient) {
		// System.out.println(String.format("rev %s: reference '%s' from node %s to %s added",
		// s.getCommit().getRevision(), edgelabel, source.getId(),
		// destination.getId()));
	}

	@Override
	public void referenceRemoval(VcsCommitItem s, IGraphNode source,
			IGraphNode destination, String edgelabel, boolean isTransient) {
		// System.out.println(String.format("rev %s: reference '%s' from node %s to %s removed",
		// s.getCommit().getRevision(), edgelabel, source.getId(),
		// destination.getId()));
	}

	@Override
	public String getHumanReadableName() {
		return "ExampleListener";
	}

	@Override
	public void setModelIndexer(IModelIndexer m) {
		// not used

	}

}
