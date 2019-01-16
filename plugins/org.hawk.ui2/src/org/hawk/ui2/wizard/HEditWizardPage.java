package org.hawk.ui2.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.hawk.osgiserver.HModel;

public class HEditWizardPage extends WizardPage {

	HModel model;
	
	protected HEditWizardPage(HModel model) {
		super("Edit "  + model.getName());
		this.model = model;
	}

	@Override
	public void createControl(Composite parent) {
		
	}

}
