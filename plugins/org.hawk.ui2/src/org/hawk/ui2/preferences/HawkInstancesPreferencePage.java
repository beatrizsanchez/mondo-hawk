package org.hawk.ui2.preferences;

import java.util.Set;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.util.HUIManager;

public class HawkInstancesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{

	private HawkInstanceBlock hawkBlock;

	public HawkInstancesPreferencePage() {
		super("Index Instances");
	}
	
	@Override
	public void init(IWorkbench workbench) { }

	@Override
	protected Control createContents(Composite parent) {
		/** Super preferences */
		initializeDialogUnits(parent);
		noDefaultButton();
		
		/** ----- */
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		
		Label DescriptionLabel = new Label(parent, SWT.WRAP);
		DescriptionLabel.setText("Add, remove or edit Hawk instances.");
		//label.setAlignment(1);
		//SWTFactory.createWrapLabel(ancestor, JREMessages.JREsPreferencePage_2, 1, 300);
		//new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);	
		
		hawkBlock = new HawkInstanceBlock();
		hawkBlock.createControl(parent);
		Control control = hawkBlock.getControl();
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		control.setLayoutData(data);
		
		return parent;
	}
	
	@Override
	public boolean performOk() {
		// TODO 
		return super.performOk();
	}
	
}
