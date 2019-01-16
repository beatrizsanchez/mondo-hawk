package org.hawk.ui2.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.hawk.osgiserver.HModel;
import org.hawk.ui2.Activator;
import org.hawk.ui2.dialog.HConfigDialog;
import org.hawk.ui2.dialog.HImportDialog;
import org.hawk.ui2.util.HUIManager;
import org.hawk.ui2.wizard.HWizard;
import org.osgi.service.prefs.BackingStoreException;

public class HawkInstanceBlock {

	private Button createButton;
	private Button importButton;
	private Button removeButton;
	private Button editButton;

	private Composite control;

	private List<HModel> indexes = new ArrayList<>();
	private TableViewer instanceListTableViewer;
	private ISelection prevSelection = new StructuredSelection();
	private Table fTable;
	private static String fgLastUsedID;

	class HawkIndexContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object input) {
			return (Object[]) indexes.toArray();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }

		@Override
		public void dispose() { }

	}

	/** FROM VIEW */
	class HawkIndexLabelProvider extends LabelProvider implements ITableLabelProvider {

		protected Image runningImage = null;
		protected Image stoppedImage = null;

		public String getColumnText(Object obj, int index) {
			HModel hModel = (HModel) obj;
			if (index == 0)
				return hModel.getName();
			else if (index == 1)
				return hModel.getFolder();
			else if (index == 2)
				return hModel.getStatus().toString();
			else
				return hModel.getInfo();
		}

		public Image getColumnImage(Object obj, int index) {
			if (index == 0)
				return getImage(obj);
			else
				return null;
		}

		public Image getImage(Object obj) {

			if (runningImage == null) {
				runningImage = Activator.getImageDescriptor("icons/hawk-running.png").createImage();
				stoppedImage = Activator.getImageDescriptor("icons/hawk-stopped.png").createImage();
			}

			HModel hModel = (HModel) obj;

			if (hModel.isRunning()) {
				return runningImage;
			} else {
				return stoppedImage;
			}
		}

	}
	
	public Control getControl() {
		return control;
	}
	
	protected Shell getShell() {
		return getControl().getShell();
	}
	
	public void createControl(Composite ancestor) {

		Font font = ancestor.getFont();
	
		Composite parent = new Composite(ancestor, GridData.FILL_BOTH);
		parent.setFont(font);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 1;
		parent.setLayout(layout);
		
		control = parent;

		/*Label label = new Label(parent, 2);
		label.setText("Instanced &Indexes:");*/

		fTable= new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
		gd.widthHint = 350;
		fTable.setLayoutData(gd);
		fTable.setFont(font);
		fTable.setHeaderVisible(true);
		fTable.setLinesVisible(true);

		TableColumn column = new TableColumn(fTable, SWT.NULL);
		column.setText("Name");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//sortByName(); FIXME
				instanceListTableViewer.refresh(true);
			}
		});
		int defaultwidth = 350/4 +1;
		column.setWidth(defaultwidth);

		column = new TableColumn(fTable, SWT.NULL);
		column.setText("Location");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// sortByLocation(); FIXME
				instanceListTableViewer.refresh(true);
			}
		});
		column.setWidth(defaultwidth);

		column = new TableColumn(fTable, SWT.NULL);
		column.setText("Status");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// sortByType(); FIXME
				instanceListTableViewer.refresh(true);
			}
		});
		column.setWidth(defaultwidth);
		
		column = new TableColumn(fTable, SWT.NULL);
		column.setText("Info");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// sortByType(); FIXME
				instanceListTableViewer.refresh(true);
			}
		});
		column.setWidth(defaultwidth);

		instanceListTableViewer = new TableViewer(fTable);
		instanceListTableViewer.setLabelProvider(new HawkIndexLabelProvider());
		instanceListTableViewer.setContentProvider(new HawkIndexContentProvider());
		//instanceListTableViewer.setContentProvider(HUIManager.getInstance());
		instanceListTableViewer.setUseHashlookup(true);
		// by default, sort by name
		// sortByName(); // FIXME

		instanceListTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent evt) {
				enableButtons();
			}
		});

		instanceListTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent e) {
				if (!instanceListTableViewer.getSelection().isEmpty()) {
					HModel selection = (HModel) instanceListTableViewer.getSelection();
					if (selection.isRunning()) {						
						editIndex();
					} else {
						selection.start(selection.getManager());
					}
				}
			}
		});
		fTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.character == SWT.DEL && event.stateMask == 0) {
					if (removeButton.isEnabled()){
						removeIndex();
					}
				}
			}
		});

		Composite buttons = new Composite(parent, GridData.VERTICAL_ALIGN_BEGINNING);
		buttons.setFont(font);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttons.setLayout(layout);
		
		createButton = new Button(buttons, SWT.PUSH);
		createButton.setText("&Create...");
		createButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				createIndex();
			}
		});

		editButton = new Button(buttons, SWT.PUSH);
		editButton.setText("&Configure...");
		editButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				editIndex();
			}

		});

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText("&Remove");
		removeButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				removeIndex();
			}
		});

	    /*Label separator = new Label(buttons, SWT.SEPARATOR | SWT.VERTICAL);
	    separator.setOrientation(1);*/

		importButton = new Button(buttons, SWT.PUSH);
		importButton.setText("&Import...");
		importButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				importIndex();
			}
		});

		fillWithWorkspaceIndexes();
		enableButtons();
		createButton.setEnabled(true);//JavaRuntime.getVMInstallTypes().length > 0); FIXME
		
		instanceListTableViewer.refresh();
	}
	
	public HModel[] getIndexes() {
		return indexes.toArray(new HModel[indexes.size()]);
	}
	
	protected void setIndexes(HModel[] idx){
		indexes.clear();
		indexes.addAll(Arrays.asList(idx));
		instanceListTableViewer.setInput(idx);
		instanceListTableViewer.refresh();
	}
	
	protected void fillWithWorkspaceIndexes() {
		// fill with JREs
		Set<HModel> hawks = HUIManager.getInstance().getHawks();
		setIndexes(hawks.toArray(new HModel[hawks.size()]));
	}
	
	/*public void setCheckedJRE(IVMInstall vm) {
		if (vm == null) {
			setSelection(new StructuredSelection());
		} else {
			setSelection(new StructuredSelection(vm));
		}
	}*/
	
	private void enableButtons() {
		createButton.setEnabled(true);
		removeButton.setEnabled(true);
		editButton.setEnabled(true);
		importButton.setEnabled(true);
	}
	
	private void createIndex() {
		HWizard wizard = new HWizard();
		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		if (dialog.open() == Window.OK) {
		HModel result = wizard.getResult();
			if (result != null) {
				indexes.add(result);
				//refresh from model
				instanceListTableViewer.refresh();
				instanceListTableViewer.setSelection(new StructuredSelection(result));
				//ensure labels are updated
				instanceListTableViewer.refresh(true);
			}
		}
	}
	
	private void editIndex() {
		// TODO Auto-generated method stub
		IStructuredSelection selection= (IStructuredSelection)instanceListTableViewer.getSelection();
		HModel firstElement = (HModel) selection.getFirstElement();
		if (firstElement == null) {
			return;
		}
		HConfigDialog dialog = new HConfigDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),
				firstElement);
		dialog.open();
		
	}
	
	private void removeIndex() {
		IStructuredSelection selected = (IStructuredSelection) instanceListTableViewer.getSelection();
		if (selected.size() == 1) {
			HModel hawkmodel = (HModel) selected.getFirstElement();
			try {
				HUIManager.getInstance().delete(hawkmodel, hawkmodel.exists());
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			instanceListTableViewer.refresh();
		}
	}

	private void importIndex() {
		final HImportDialog dialog = new HImportDialog(getShell());
		dialog.open();
	}
}
