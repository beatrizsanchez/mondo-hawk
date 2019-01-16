package org.hawk.ui2.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hawk.ui2.Activator;

public class HawkPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private IPreferenceStore store;
	
    public HawkPreferencePage() {
        super("Default Enabled Plugins");
    	store = Activator.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }
    
    @Override
    public void init(IWorkbench workbench) {
    	//  new StringFieldEditor(HawkPreferenceConstants.X, "Server:", getFieldEditorParent()));

    }


	@Override	
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return parent;
	}
   


}
