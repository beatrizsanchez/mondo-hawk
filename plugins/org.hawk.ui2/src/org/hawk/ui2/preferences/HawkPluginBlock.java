package org.hawk.ui2.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IToolTipProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.hawk.core.IMetaModelResourceFactory;
import org.hawk.osgiserver.HManager;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.util.HUIManager;

public class HawkPluginBlock {

	private Composite control;

	private List<HModel> indexes = new ArrayList<>();
	private CheckboxTableViewer checkedTableViewer;
	private Table fTable;
	private HModel hawk;

	class HawkIndexContentProvider implements IStructuredContentProvider{

		@Override
		public Object[] getElements(Object input) {
			return (Object[]) HManager.getInstance().getMetamodelParserInstances().values().toArray();
			//return (Object[]) hawk.getMetamodelParsers().toArray();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }

		@Override
		public void dispose() { }

	}

	class HawkIndexLabelProvider extends LabelProvider implements IToolTipProvider {

		@Override
			public String getText(Object element) {
			return ((IMetaModelResourceFactory) element).getHumanReadableName();
		}

		@Override
		public String getToolTipText(Object element) {
			return ((IMetaModelResourceFactory) element).getType();
		}

	}
	
	public Control getControl() {
		return control;
	}
	
	protected Shell getShell() {
		return getControl().getShell();
	}
	
	public void createControl(Composite ancestor, HModel hawk) {

		this.hawk = hawk;
		Font font = ancestor.getFont();
	
		control = new Composite(ancestor, SWT.NONE);
		control.setFont(font);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		layout.horizontalSpacing = 1;
		control.setLayout(layout);
		
		checkedTableViewer = CheckboxTableViewer.newCheckList(control,
				SWT.BORDER | SWT.V_SCROLL | SWT.FILL);
		checkedTableViewer.setLabelProvider(new HawkIndexLabelProvider());
		checkedTableViewer.setContentProvider(new HawkIndexContentProvider());
		checkedTableViewer.setUseHashlookup(true);
		
		checkedTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				// TODO update()
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		checkedTableViewer.getTable().setLayoutData(gd);
		checkedTableViewer.getTable().setHeaderVisible(false);
		
		fillWithWorkspaceIndexes();
		
		checkedTableViewer.refresh();
		checkedTableViewer.setCheckStateProvider(new ICheckStateProvider() {
			
			@Override
			public boolean isGrayed(Object element) {
				return false;
			}
			
			@Override
			public boolean isChecked(Object element) {
				return hawk.getEnabledPlugins().contains(((IMetaModelResourceFactory) element).getType());
				
			}
		});
	}
	
	public HModel[] getIndexes() {
		return indexes.toArray(new HModel[indexes.size()]);
	}
	
	protected void setIndexes(HModel[] idx){
		indexes.clear();
		indexes.addAll(Arrays.asList(idx));
		checkedTableViewer.setInput(idx);
		checkedTableViewer.refresh();
	}
	
	protected void fillWithWorkspaceIndexes() {
		Set<HModel> hawks = HUIManager.getInstance().getHawks();
		setIndexes(hawks.toArray(new HModel[hawks.size()]));
	}

	public void update(HModel hawk) {
		this.hawk = hawk;
		this.checkedTableViewer.refresh();
	}

}
