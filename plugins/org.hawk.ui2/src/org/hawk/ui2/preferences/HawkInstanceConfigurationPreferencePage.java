package org.hawk.ui2.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.util.HUIManager;

public class HawkInstanceConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{


	public HawkInstanceConfigurationPreferencePage() {
		super("Instance Configuration");
	}
	
	@Override
	public void init(IWorkbench workbench) { }

	@Override
	protected Control createContents(Composite parent) {
		/** Super preferences */
		initializeDialogUnits(parent);
		
		/** ----- */
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		combo.setLayoutData(gd);
		for (HModel db : HUIManager.getInstance().getHawks()) {
			combo.add(db.getName());
		}
		combo.select(0);
		return parent;
	}
	
	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
}
