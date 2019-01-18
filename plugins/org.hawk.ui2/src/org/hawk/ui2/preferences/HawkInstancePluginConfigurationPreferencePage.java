package org.hawk.ui2.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckboxTableViewer;
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
import org.hawk.core.IModelUpdater;
import org.hawk.core.graph.IGraphChangeListener;
import org.hawk.core.query.IQueryEngine;
import org.hawk.osgiserver.HManager;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.util.HUIManager;

public class HawkInstancePluginConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Combo combo;

	private Composite control;

	private CheckboxTableViewer metamodelTableViewer;
	private CheckboxTableViewer modelTableViewer;
	private CheckboxTableViewer modelUpdaterTableViewer;
	private CheckboxTableViewer graphChangeListenerTableViewer;
	private CheckboxTableViewer queryEnginesTableViewer;

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
		GridData gd = new GridData(GridData.FILL_BOTH);
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

		// METAMODEL PARSER
		
		Label label = new Label(control, SWT.NULL);
		label.setText("&Metamodel plugins:");

		metamodelTableViewer = newTableViewer();
		metamodelTableViewer.setLabelProvider(new TypedLabelProvider(IMetaModelResourceFactory.class));
		metamodelTableViewer.setContentProvider(new TypedContentProvider("getMetamodelParserInstances"));
		metamodelTableViewer.setInput(HManager.getInstance().getMetamodelParserInstances().values().toArray());
		metamodelTableViewer.setCheckStateProvider(new TypedCheckStateProvider(IMetaModelResourceFactory.class));
		
		// MODEL PARSER
		
		label = new Label(control, SWT.NULL);
		label.setText("&Model plugins:");

		modelTableViewer = newTableViewer();
		modelTableViewer.setLabelProvider(new TypedLabelProvider(IModelResourceFactory.class));
		modelTableViewer.setContentProvider(new TypedContentProvider("getModelParserInstances"));
		modelTableViewer.setInput(HManager.getInstance().getModelParserInstances().values().toArray());
		modelTableViewer.setCheckStateProvider(new TypedCheckStateProvider(IModelResourceFactory.class));
		
		// MODEL UPDATER

		label = new Label(control, SWT.NULL);
		label.setText("&Model updater plugins:");

		modelUpdaterTableViewer = newTableViewer();
		modelUpdaterTableViewer.setLabelProvider(new TypedLabelProvider(IModelUpdater.class));
		modelUpdaterTableViewer.setContentProvider(new TypedContentProvider("getModelUpdaterInstances"));
		modelUpdaterTableViewer.setInput(HManager.getInstance().getMetamodelParserInstances().values().toArray());
		modelUpdaterTableViewer.setCheckStateProvider(new TypedCheckStateProvider(IModelUpdater.class));
		
		// QUERY LANGUAGES
		
		label = new Label(control, SWT.NULL);
		label.setText("&Query language plugins:");

		queryEnginesTableViewer = newTableViewer();
		queryEnginesTableViewer.setLabelProvider(new TypedLabelProvider(IQueryEngine.class));
		queryEnginesTableViewer.setContentProvider(new TypedContentProvider("getQueryLanguageInstances"));
		queryEnginesTableViewer.setInput(HManager.getInstance().getModelParserInstances().values().toArray());
		queryEnginesTableViewer.setCheckStateProvider(new TypedCheckStateProvider(IQueryEngine.class));
		
		// GRAPH CHANGE LISTENERS

		label = new Label(control, SWT.NULL);
		label.setText("&Graph change listeners plugins:");

		graphChangeListenerTableViewer = newTableViewer();
		graphChangeListenerTableViewer.setLabelProvider(new TypedLabelProvider(IGraphChangeListener.class));
		graphChangeListenerTableViewer.setContentProvider(new TypedContentProvider("getGraphChangeListenerInstances"));
		graphChangeListenerTableViewer.setInput(HManager.getInstance().getModelParserInstances().values().toArray());
		graphChangeListenerTableViewer.setCheckStateProvider(new TypedCheckStateProvider(IGraphChangeListener.class));

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

	public HModel getSelectedHawkInstance() {
		return HUIManager.getInstance().getHawkByName(combo.getItem(combo.getSelectionIndex()));
	}

	class TypedCheckStateProvider implements ICheckStateProvider {
		
		private Class<?> clazz;
		
		public TypedCheckStateProvider(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		@Override
		public boolean isGrayed(Object element) {
			return false;
		}

		@Override
		public boolean isChecked(Object element) {
			try{
				String plugin = (String) clazz.getMethod("getType").invoke(clazz.cast(element));
				return getSelectedHawkInstance().getEnabledPlugins().contains(plugin);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	
	class TypedContentProvider implements IStructuredContentProvider {

		private String method;
		
		public TypedContentProvider(String method) {
			this.method = method;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Object[] getElements(Object input) {
			try{
				Map invoke = (Map) HManager.class.getMethod(method).invoke(HManager.getInstance());
				return invoke.values().toArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		@Override
		public void dispose() {}

	}

	
	class TypedLabelProvider extends LabelProvider implements IToolTipProvider {

		private Class<?> clazz;
		
		public TypedLabelProvider(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		@Override
		public String getText(Object element) {
			try{
				return (String) clazz.getMethod("getHumanReadableName").invoke(clazz.cast(element));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		@Override
		public String getToolTipText(Object element) {
			try{
				return (String) clazz.getMethod("getType").invoke(clazz.cast(element));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

	}

	public void update() {
		this.metamodelTableViewer.refresh();
		this.modelTableViewer.refresh();
		this.graphChangeListenerTableViewer.refresh();
		this.modelUpdaterTableViewer.refresh();
		this.queryEnginesTableViewer.refresh();
	}
	
	private CheckboxTableViewer newTableViewer(){
		CheckboxTableViewer modelTableViewer = CheckboxTableViewer.newCheckList(control, SWT.BORDER | SWT.V_SCROLL | SWT.FILL);
		modelTableViewer.setUseHashlookup(true);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		modelTableViewer.getTable().setLayoutData(gd);
		modelTableViewer.getTable().setHeaderVisible(false);
		return modelTableViewer;
	}
	
	@Override
	protected void performDefaults() {
		update();
	}
	
	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}
	

	@Override
	protected void performApply() {
		try {
			HModel hawk = getSelectedHawkInstance();
			hawk.addPlugins(getAdditionalPluginsFromChecked());
			hawk.removePlugins(getPluginsToRemoveFromUnchecked());
			HUIManager.getInstance().saveHawkToMetadata(hawk, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		update();
	}
	
	private List<String> getAdditionalPluginsFromChecked(){
		List<String> result = new ArrayList<>();
		List<String> enabledPlugins = getSelectedHawkInstance().getEnabledPlugins();
		getAllChecked().stream().forEach(type -> {
			if (!enabledPlugins.contains(type)) {
				result.add(type);
			}
		});
		return result;
	}
	
	private List<String> getPluginsToRemoveFromUnchecked(){
		List<String> enabled = new ArrayList<>(getSelectedHawkInstance().getEnabledPlugins());
		enabled.removeAll(getAllChecked());
		return enabled; 
	}
	
	private List<String> getAllChecked(){
		List<String> checked = new ArrayList<>();
		checked.addAll(getCheckedList(metamodelTableViewer, IMetaModelResourceFactory.class));
		checked.addAll(getCheckedList(modelTableViewer, IModelResourceFactory.class));
		checked.addAll(getCheckedList(modelUpdaterTableViewer, IModelUpdater.class));
		checked.addAll(getCheckedList(queryEnginesTableViewer, IQueryEngine.class));
		checked.addAll(getCheckedList(graphChangeListenerTableViewer, IGraphChangeListener.class));
		return checked;
	}
	
	private List<String> getCheckedList(CheckboxTableViewer viewer, Class<?> clazz){
		return Arrays.asList(
				viewer.getCheckedElements()).stream()
				.map(e -> {
					try{
						return (String) clazz.getMethod("getType").invoke(clazz.cast(e));
					} catch (Exception ex) {
						return "";
					}
				}).collect(Collectors.toList());
	}

}