/*******************************************************************************
 * Copyright (c) 2011-2015 The University of York.
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
package org.hawk.core.util;

import org.hawk.core.IModelIndexer;
import org.hawk.core.VcsCommitItem;
import org.hawk.core.graph.IGraphChangeListener;
import org.hawk.core.graph.IGraphNode;
import org.hawk.core.model.IHawkClass;
import org.hawk.core.model.IHawkObject;
import org.hawk.core.model.IHawkPackage;

/**
 * Default empty implementation of a {@link IGraphChangeListener}, to be used through subclassing.
 */
public class GraphChangeAdapter implements IGraphChangeListener {

	@Override
	public String getHumanReadableName() {
		return null;
	}

	@Override
	public void setModelIndexer(IModelIndexer m) {
		// nothing
	}

	@Override
	public void synchroniseStart() {
		// nothing
	}

	@Override
	public void synchroniseEnd() {
		// nothing
	}

	@Override
	public void changeStart() {
		// nothing
	}

	@Override
	public void changeSuccess() {
		// nothing
	}

	@Override
	public void changeFailure() {
		// nothing
	}

	@Override
	public void metamodelAddition(IHawkPackage pkg, IGraphNode pkgNode) {
		// nothing
	}

	@Override
	public void classAddition(IHawkClass cls, IGraphNode clsNode) {
		// nothing
	}

	@Override
	public void fileAddition(VcsCommitItem s, IGraphNode fileNode) {
		// nothing
	}

	@Override
	public void fileRemoval(VcsCommitItem s, IGraphNode fileNode) {
		// nothing
	}

	@Override
	public void modelElementAddition(VcsCommitItem s, IHawkObject element, IGraphNode elementNode,
			boolean isTransient) {
		// nothing
	}

	@Override
	public void modelElementRemoval(VcsCommitItem s, IGraphNode elementNode, boolean isTransient) {
		// nothing
	}

	@Override
	public void modelElementAttributeUpdate(VcsCommitItem s, IHawkObject eObject, String attrName, Object oldValue,
			Object newValue, IGraphNode elementNode, boolean isTransient) {
		// nothing
	}

	@Override
	public void modelElementAttributeRemoval(VcsCommitItem s, IHawkObject eObject, String attrName,
			IGraphNode elementNode, boolean isTransient) {
		// nothing
	}

	@Override
	public void referenceAddition(VcsCommitItem s, IGraphNode source, IGraphNode destination, String edgelabel,
			boolean isTransient) {
		// nothing
	}

	@Override
	public void referenceRemoval(VcsCommitItem s, IGraphNode source, IGraphNode destination, String edgelabel,
			boolean isTransient) {
		// nothing
	}

}
