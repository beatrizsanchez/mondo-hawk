/*******************************************************************************
 * Copyright (c) 2017-2018 Aston University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Antonio Garcia-Dominguez - initial API and implementation
 ******************************************************************************/
package org.hawk.integration.tests.uml;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.hawk.backend.tests.BackendTestSuite;
import org.hawk.backend.tests.factories.IGraphDatabaseFactory;
import org.hawk.integration.tests.ModelIndexingTest;
import org.hawk.uml.vcs.PredefinedUMLLibraries;
import org.junit.runners.Parameterized.Parameters;

/**
 * Base class for UML indexing tests.
 */
public class AbstractUMLIndexingTest extends ModelIndexingTest {

	protected static final File BASE_DIRECTORY = new File("resources/models/uml");

	@Parameters(name = "{0}")
	public static Iterable<Object[]> params() {
		return BackendTestSuite.caseParams();
	}

	public AbstractUMLIndexingTest(IGraphDatabaseFactory dbFactory) {
		super(dbFactory, new UMLModelSupportFactory());
	}

	@Override
	public void setup() throws Throwable {
		UMLResourcesUtil.initGlobalRegistries();
		super.setup();
		addUMLComponents();
	}

	protected void copyResource(final URI destURI, final File oldFile) throws IOException {
		ResourceSetImpl rs = new ResourceSetImpl();
		UMLResourcesUtil.init(rs);
		final Resource oldResource = rs.createResource(URI.createFileURI(oldFile.toString()));
		oldResource.load(null);
		oldResource.setURI(destURI);
		oldResource.save(null);
		oldResource.unload();
	}

	protected void addUMLComponents() throws Throwable {
		final PredefinedUMLLibraries vcs = new PredefinedUMLLibraries();
		vcs.init(null, indexer);
		vcs.run();
		indexer.addVCSManager(vcs, true);
		waitForSync();
	}

}