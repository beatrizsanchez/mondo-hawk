package org.hawk.uml.model;

import java.util.Collections;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.hawk.core.model.IHawkClassifier;
import org.hawk.core.model.IHawkReference;
import org.hawk.core.model.IHawkStructuralFeature;
import org.hawk.emf.EMFObject;
import org.hawk.emf.EMFWrapperFactory;

/**
 * Overrides a regular UML package so profile applications are treated as
 * <code>ofType</code> edges, instead of metamodel references that would be left
 * dangling.
 */
public class UMLProfiledPackage extends EMFObject {

	private static final EReference IGNORED_FEATURE = UMLPackage.eINSTANCE.getPackage_ProfileApplication();

	public UMLProfiledPackage(Package o, EMFWrapperFactory wf) {
		super(o, wf);
	}

	@Override
	public Package getEObject() {
		return (Package) super.getEObject();
	}

	@Override
	public IHawkClassifier getType() {
		return new UMLProfiledPackageType(getEObject(), wf);
	}

	@Override
	public boolean isSet(IHawkStructuralFeature hsf) {
		if (IGNORED_FEATURE.getName().equals(hsf.getName())) {
			return false;
		}
		return super.isSet(hsf);
	}

	@Override
	public Object get(IHawkReference ref, boolean resolve) {
		if (IGNORED_FEATURE.getName().equals(ref.getName())) {
			return Collections.emptyList();
		}
		return super.get(ref, resolve);
	}

}
