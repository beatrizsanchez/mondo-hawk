package org.hawk.ui2.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.hawk.osgiserver.HManager;
import org.hawk.ui2.Activator;

public class HawkPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		HManager instance = HManager.getInstance();
		store.setDefault(HawkPreferenceConstants.MODEL_PLUGINS, String.join(",", instance.getModelTypes()));
		store.setDefault(HawkPreferenceConstants.METAMODEL_PLUGINS, String.join(",", instance.getMetaModelTypes()));
		
	}

}
