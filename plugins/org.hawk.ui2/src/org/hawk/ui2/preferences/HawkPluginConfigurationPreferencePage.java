package org.hawk.ui2.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class HawkPluginConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{

	public HawkPluginConfigurationPreferencePage(){
		super("Configuration");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		return parent;
	}
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

}
