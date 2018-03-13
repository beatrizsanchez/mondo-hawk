/*******************************************************************************
 * Copyright (c) 2011-2018 The University of York, Aston University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Konstantinos Barmpis - initial API and implementation
 *     Antonio Garcia-Dominguez - use SLF4J
 ******************************************************************************/
package org.hawk.emf.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterMeta {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMeta.class);

	private static int registered = 0;

	public void clean() {
		Object[] packages = EPackage.Registry.INSTANCE.keySet().toArray();
		LOGGER.info("Cleaning packages: {}", packages);

		for (Object s : packages)
			if ((!((String) s).contains("Ecore") && !((String) s)
					.contains("XMLType")))
				EPackage.Registry.INSTANCE.remove(s);

	}

	// registers metamodel
	/**
	 * Iterates through the descendants of a package and registers all the
	 * subpackages (as well as the root)
	 * 
	 * @param root
	 */
	public static int registerPackages(EPackage root) {
		if (root.getNsURI() != null && !root.getNsURI().equals(EcorePackage.eNS_URI)
				&& !root.getNsURI().equals(XMLTypePackage.eNS_URI)) {
			if (EPackage.Registry.INSTANCE.get(root.getNsURI()) == null) {
				if (EPackage.Registry.INSTANCE.put(root.getNsURI(), root) == null) {
					LOGGER.info("Registering package: {} ({}) [{}]",
						root.getName(), root.getNsURI(), root.eResource().getURI()
					);
					registered++;
				}
				for (EPackage pkg : root.getESubpackages()) {
					registerPackages(pkg);
				}
			}
		}
		return registered;
	}

	public static void registerPackages(Resource r) {

		for (EObject e : r.getContents())
			if (e instanceof EPackage)
				registerPackages((EPackage) e);

	}

}
