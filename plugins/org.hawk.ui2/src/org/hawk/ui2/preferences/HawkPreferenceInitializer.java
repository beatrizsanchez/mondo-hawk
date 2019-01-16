package org.hawk.ui2.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.hawk.ui2.Activator;

public class HawkPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(HawkPreferenceConstants.X, "http://localhost:8080/thrift/hawk/tuple");
	}

}
