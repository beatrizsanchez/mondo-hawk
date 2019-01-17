package org.hawk.ui2.preferences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IToolTipProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hawk.core.IMetaModelResourceFactory;
import org.hawk.core.IModelResourceFactory;
import org.hawk.osgiserver.HManager;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.util.HUIManager;

public class HawkInstancePluginConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Combo combo;

	private Composite control;

	private CheckboxTableViewer metamodelTableViewer;
	private CheckboxTableViewer modelTableViewer;

	public HawkInstancePluginConfigurationPreferencePage() {
		super("Instance Configuration");
	}

	@Override
	public void init(IWorkbench workbench) {}

	@Override
	protected Control createContents(Composite parent) {
		/** Super preferences */
		initializeDialogUnits(parent);

		/** ----- */
		combo = new Combo(parent, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		combo.setLayoutData(gd);
		Set<HModel> hawks = HUIManager.getInstance().getHawks();
		for (HModel db : hawks) {
			combo.add(db.getName());
		}
		combo.select(0);

		Font font = parent.getFont();

		control = new Composite(parent, SWT.NONE);
		control.setFont(font);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		layout.horizontalSpacing = 1;
		control.setLayout(layout);

		Label label = new Label(control, SWT.NULL);
		label.setText("&Metamodel plugins:");

		metamodelTableViewer = newTableViewer();
		metamodelTableViewer.setLabelProvider(new MetamodelLabelProvider());
		metamodelTableViewer.setContentProvider(new MetamodelContentProvider());
		metamodelTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					List<String> plugins = Arrays.asList(((IMetaModelResourceFactory) event.getElement()).getType());
					try {
						getSelectedHawkInstance().addPlugins(plugins);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				update();
			}
		});
		metamodelTableViewer.setCheckStateProvider(new ICheckStateProvider() {

			@Override
			public boolean isGrayed(Object element) {
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				return getSelectedHawkInstance().getEnabledPlugins().contains(((IMetaModelResourceFactory) element).getType());

			}
		});
		metamodelTableViewer.setInput(HManager.getInstance().getMetamodelParserInstances().values().toArray());
		metamodelTableViewer.refresh();
		
		label = new Label(control, SWT.NULL);
		label.setText("&Model plugins:");

		modelTableViewer = newTableViewer();
		modelTableViewer.setLabelProvider(new ModelLabelProvider());
		modelTableViewer.setContentProvider(new ModelContentProvider());
		modelTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					List<String> plugins = Arrays.asList(((IModelResourceFactory) event.getElement()).getType());
					try {
						getSelectedHawkInstance().addPlugins(plugins);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				update();
			}
		});
		modelTableViewer.setCheckStateProvider(new ICheckStateProvider() {

			@Override
			public boolean isGrayed(Object element) {
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				return getSelectedHawkInstance().getEnabledPlugins().contains(((IModelResourceFactory) element).getType());

			}
		});
		modelTableViewer.setInput(HManager.getInstance().getModelParserInstances().values().toArray());
		modelTableViewer.refresh();

		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				update();
			}
		});
		
		update();
		getSelectedHawkInstance();
		
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		control.setLayoutData(data);

		return parent;
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}

	public HModel getSelectedHawkInstance() {
		return HUIManager.getInstance().getHawkByName(combo.getItem(combo.getSelectionIndex()));
	}

	class MetamodelContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object input) {
			return (Object[]) HManager.getInstance().getMetamodelParserInstances().values().toArray();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		@Override
		public void dispose() {}

	}

	class ModelContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object input) {
			return (Object[]) HManager.getInstance().getModelParserInstances().values().toArray();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		@Override
		public void dispose() {}

	}

	class MetamodelLabelProvider extends LabelProvider implements IToolTipProvider {

		@Override
		public String getText(Object element) {
			return ((IMetaModelResourceFactory) element).getHumanReadableName();
		}

		@Override
		public String getToolTipText(Object element) {
			return ((IMetaModelResourceFactory) element).getType();
		}

	}
	
	class ModelLabelProvider extends LabelProvider implements IToolTipProvider {

		@Override
		public String getText(Object element) {
			return ((IModelResourceFactory) element).getHumanReadableName();
		}

		@Override
		public String getToolTipText(Object element) {
			return ((IModelResourceFactory) element).getType();
		}

	}

	public void update() {

		this.metamodelTableViewer.refresh();
		this.modelTableViewer.refresh();
	}
	
	private CheckboxTableViewer newTableViewer(){
		CheckboxTableViewer modelTableViewer = CheckboxTableViewer.newCheckList(control, SWT.BORDER | SWT.V_SCROLL | SWT.FILL);
		modelTableViewer.setUseHashlookup(true);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		modelTableViewer.getTable().setLayoutData(gd);
		modelTableViewer.getTable().setHeaderVisible(false);
		return modelTableViewer;
	}

}