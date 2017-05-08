/***************************************************************************************
 *Endeavour Agile ALM
 *Copyright (C) 2009  Ezequiel Cuellar
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ***************************************************************************************/

package org.endeavour.mgmt.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.ITestMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TestPlanMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.TestPlanModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.WebBrowser;
import thinwire.ui.GridBox.Row;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class TestPlanView extends DialogComponent implements ActionListener, Observer {

	private TestPlanMaintenance testPlanMaintenance = null;
	private GridBoxComponent testPlanGrid = null;
	private TextField nameTextField = null;
	private TextField createdByTextField = null;
	private Button expandButton = null;
	private Button collapseButton = null;
	private Button collapseAllButton = null;
	private Button expandAllButton = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private Button folderButton = null;
	private Button testRunButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private WebBrowser printPreviewWebBrowser = null;

	private List<String> expandedRows = null;
	private Map<Integer, Integer> idToGridMappings = null;
	private TestPlanModel testPlanModel = null;
	private PanelComponent testPlanPanel = null;

	public static final String TYPE = IViewConstants.RB.getString("type.lbl");

	public TestPlanView(Integer aTestPlanMaintenanceId, TestPlanMaintenance aTestPlanMaintenance) {
		this(aTestPlanMaintenance);
		this.viewTestPlan(aTestPlanMaintenanceId);
		this.displayPlan();
		this.setButtonsStatus();
	}

	public TestPlanView(TestPlanMaintenance aTestPlanMaintenance) {

		this.testPlanMaintenance = aTestPlanMaintenance;
		this.initializeControllers();

		super.setTitle(IViewConstants.RB.getString("test_plan.lbl"));
		super.setSize(850, 600);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		TabSheetComponent theTestPlanTab = new TabSheetComponent(IViewConstants.RB.getString("test_plan.lbl"));
		theTestPlanTab.setImage(IViewConstants.TEST_PLANS_ICON);

		theTestPlanTab.setLayout(new TableLayout(new double[][] { { 0 }, { 22, 50, 0 } }, 5, 5));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 0, 70, 70, 70, 70, 5 }, { 22 } }, 0, 5));

		PanelComponent theIconsPanel = new PanelComponent();
		theIconsPanel.setLayout(new TableLayout(new double[][] { { 27, 20, 25, 20 }, { 20 } }, 5, 0));

		Label thePassLabel = new Label(IViewConstants.RB.getString("pass.lbl"));
		theIconsPanel.add(thePassLabel.setLimit("0, 0"));

		Label thePassIconLabel = new Label();
		thePassIconLabel.getStyle().getBackground().setImage(IViewConstants.PASS_ICON);
		theIconsPanel.add(thePassIconLabel.setLimit("1, 0"));

		Label theFailLabel = new Label(IViewConstants.RB.getString("fail.lbl"));
		theIconsPanel.add(theFailLabel.setLimit("2, 0"));

		Label theFailIconLabel = new Label();
		theFailIconLabel.getStyle().getBackground().setImage(IViewConstants.FAIL_ICON);
		theIconsPanel.add(theFailIconLabel.setLimit("3, 0"));

		theHeaderPanel.add(theIconsPanel.setLimit("0, 0"));

		this.folderButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		this.folderButton.addActionListener(Button.ACTION_CLICK, this);
		this.folderButton.setImage(IViewConstants.FOLDER_ICON);
		theHeaderPanel.add(this.folderButton.setLimit("1, 0"));

		this.testRunButton = new Button(IViewConstants.ADD_BUTTON_LABEL);
		this.testRunButton.addActionListener(Button.ACTION_CLICK, this);
		this.testRunButton.setImage(IViewConstants.TEST_CASES_ICON);
		this.testRunButton.setEnabled(false);
		theHeaderPanel.add(this.testRunButton.setLimit("2, 0"));

		this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		this.editButton.addActionListener(Button.ACTION_CLICK, this);
		this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
		this.editButton.setEnabled(false);
		theHeaderPanel.add(this.editButton.setLimit("3, 0"));

		this.deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteButton.addActionListener(Button.ACTION_CLICK, this);
		this.deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		this.deleteButton.setEnabled(false);
		theHeaderPanel.add(this.deleteButton.setLimit("4, 0"));

		theTestPlanTab.add(theHeaderPanel.setLimit("0, 0"));

		PanelComponent theNamePanel = new PanelComponent();
		theNamePanel.setLayout(new TableLayout(new double[][] { { 65, 0 }, { 20, 20 } }, 5, 5));

		Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		theNamePanel.add(theNameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		theNamePanel.add(this.nameTextField.setLimit("1, 0"));

		Label theCreatedByLabel = new Label(IViewConstants.RB.getString("created_by.lbl"));
		theNamePanel.add(theCreatedByLabel.setLimit("0, 1"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		this.createdByTextField = new TextField();
		this.createdByTextField.setText(theSecurityMaintenance.getLoggedUserId());
		this.createdByTextField.setEnabled(false);
		theNamePanel.add(this.createdByTextField.setLimit("1, 1"));

		theTestPlanTab.add(theNamePanel.setLimit("0, 1"));

		this.testPlanPanel = new PanelComponent();
		this.testPlanPanel.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.idToGridMappings = new HashMap<Integer, Integer>();
		this.expandedRows = new ArrayList<String>();

		this.initializeTestPlanGrid();

		PanelComponent theControlButtonsPanel = new PanelComponent();
		theControlButtonsPanel.setLayout(new TableLayout(new double[][] { { 100, 100, 100, 100, 0 }, { 0 } }, 0, 5));

		this.expandAllButton = new Button(IViewConstants.EXPAND_ALL_BUTTON_LABEL);
		this.expandAllButton.addActionListener(Button.ACTION_CLICK, this);
		this.expandAllButton.setImage(IViewConstants.EXPAND_BUTTON_ICON);
		theControlButtonsPanel.add(this.expandAllButton.setLimit("0, 0"));

		this.expandButton = new Button(IViewConstants.EXPAND_BUTTON_LABEL);
		this.expandButton.addActionListener(Button.ACTION_CLICK, this);
		this.expandButton.setImage(IViewConstants.EXPAND_BUTTON_ICON);
		theControlButtonsPanel.add(this.expandButton.setLimit("1, 0"));

		this.collapseButton = new Button(IViewConstants.COLLAPSE_BUTTON_LABEL);
		this.collapseButton.addActionListener(Button.ACTION_CLICK, this);
		this.collapseButton.setImage(IViewConstants.COLLAPSE_BUTTON_ICON);
		theControlButtonsPanel.add(this.collapseButton.setLimit("2, 0"));

		this.collapseAllButton = new Button(IViewConstants.COLLAPSE_ALL_BUTTON_LABEL);
		this.collapseAllButton.addActionListener(Button.ACTION_CLICK, this);
		this.collapseAllButton.setImage(IViewConstants.COLLAPSE_BUTTON_ICON);
		theControlButtonsPanel.add(this.collapseAllButton.setLimit("3, 0"));

		setButtonsStatus();

		this.testPlanPanel.add(theControlButtonsPanel.setLimit("0, 1"));
		theTestPlanTab.add(this.testPlanPanel.setLimit("0, 2"));

		theTabFolder.add(theTestPlanTab);

		TabSheetComponent thePrintPreviewTab = new TabSheetComponent(IViewConstants.PRINT_PREVIEW);
		thePrintPreviewTab.setImage(IViewConstants.PRINT_PREVIEW_ICON);
		thePrintPreviewTab.addActionListener(TabSheetComponent.ACTION_CLICK, this);
		thePrintPreviewTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.printPreviewWebBrowser = new WebBrowser();
		thePrintPreviewTab.add(this.printPreviewWebBrowser.setLimit("0, 0"));
		theTabFolder.add(thePrintPreviewTab);

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
		this.saveButton.addActionListener(Button.ACTION_CLICK, this);
		this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		theButtonsPanel.add(this.saveButton.setLimit("1, 0"));

		this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
		this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));

		super.add(theTabFolder.setLimit("0, 0"));
		super.add(theButtonsPanel.setLimit("0, 1"));

		this.setVisible(true);
	}

	private void initializeControllers() {
		this.testPlanMaintenance.startUnitOfWork();
		this.testPlanMaintenance.resetTestPlan();
		this.testPlanMaintenance.addObserver(this);
	}

	private void initializeTestPlanGrid() {
		this.testPlanModel = new TestPlanModel(this.testPlanMaintenance.getTestPlanData(this.idToGridMappings, this.expandedRows));
		this.testPlanGrid = new GridBoxComponent(this.testPlanModel);
		this.testPlanGrid.setColumnWidth(0, 45);
		this.testPlanGrid.setColumnWidth(1, 230);
		this.testPlanGrid.setColumnWidth(2, 45);
		this.testPlanGrid.setColumnWidth(3, 125);
		this.testPlanGrid.setColumnWidth(4, 200);
		this.testPlanGrid.setSortAllowed(false);
		this.testPlanGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.testPlanGrid.addActionListener(GridBox.ACTION_CLICK, this);
		this.testPlanPanel.add(this.testPlanGrid.setLimit("0, 0"));
	}

	private void displayPlan() {
		int theRowIndex = this.testPlanGrid.getSelectedRow() != null ? this.testPlanGrid.getSelectedRow().getIndex() : 0;
		this.testPlanModel.setData(this.testPlanMaintenance.getTestPlanData(this.idToGridMappings, this.expandedRows));
		this.testPlanGrid.setSelectedRowByIndex(theRowIndex);
	}

	private void viewTestPlan(Integer aTestPlanId) {
		Map<String, Object> theData = this.testPlanMaintenance.getTestPlanDataBy(aTestPlanId);
		String theName = (String) theData.get(ITestMaintenance.NAME);
		String theCreatedBy = (String) theData.get(ITestMaintenance.CREATED_BY);
		this.nameTextField.setText(theName);
		this.createdByTextField.setText(theCreatedBy);
		super.setTitle("Test Plan - " + theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		String theAction = aEvt.getAction();

		if (theSource instanceof GridBox.Range && theAction.equals(GridBox.ACTION_DOUBLE_CLICK)) {
			this.viewSelection();
		}

		if (theSource instanceof GridBox.Range && theAction.equals(GridBox.ACTION_CLICK)) {
			this.setButtonsStatus();
		}

		if (theSource instanceof TabSheetComponent) {
			this.printPreviewWebBrowser.setLocation(this.testPlanMaintenance.createPrintPreviewLocation());
		}

		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			String theText = theButton.getText();
			if (theText.equals(IViewConstants.EXPAND_BUTTON_LABEL)) {
				this.expandSelection();
			}
			if (theText.equals(IViewConstants.COLLAPSE_BUTTON_LABEL)) {
				this.collapseSelection();
			}
			if (theText.equals(IViewConstants.COLLAPSE_ALL_BUTTON_LABEL)) {
				this.collapseAll();
			}
			if (theText.equals(IViewConstants.EXPAND_ALL_BUTTON_LABEL)) {
				this.expandAll();
			}
			if (theText.equals(IViewConstants.SAVE_BUTTON_LABEL)) {
				int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
				if (theResult == IViewConstants.YES) {
					this.saveTestPlan();
				}
			}
			if (theText.equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
				this.testPlanMaintenance.resetTestFolders();
				this.setVisible(false);
			}
			if (theText.equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new FolderView(this.testPlanMaintenance);
			}
			if (theText.equals(IViewConstants.ADD_BUTTON_LABEL)) {
				Integer theSelectedRowIndex = this.testPlanGrid.getSelectedRow().getIndex();
				Integer theFolderId = this.idToGridMappings.get(theSelectedRowIndex);
				if (theFolderId != null) {
					this.testPlanMaintenance.getTestFolderDataBy(theFolderId);
					new TestCaseSelectionView(this.testPlanMaintenance);
				}
			}
			if (theText.equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewSelection();
			}
			if (theText.equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteSelection();
			}
		}
	}

	private void setButtonsStatus() {
		if (this.testPlanGrid.getSelectedRow() != null) {
			Integer theSelectedRowIndex = this.testPlanGrid.getSelectedRow().getIndex();
			String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
			Integer theSelectedElementId = this.idToGridMappings.get(theSelectedRowIndex);
			boolean isExpanded = this.expandedRows.contains(theType + theSelectedElementId);
			if (theType.equals(TestPlanMaintenance.FOLDER)) {
				this.expandButton.setEnabled(!isExpanded);
				this.collapseButton.setEnabled(isExpanded);
				this.testRunButton.setEnabled(true);
			} else {
				this.expandButton.setEnabled(false);
				this.collapseButton.setEnabled(false);
				this.testRunButton.setEnabled(false);
			}
			this.editButton.setEnabled(true);
			this.deleteButton.setEnabled(true);
		} else {
			this.testRunButton.setEnabled(false);
			this.editButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
		}
	}

	private void expandSelection() {
		if (this.testPlanGrid.getSelectedRow() != null) {
			Integer theSelectedRowIndex = this.testPlanGrid.getSelectedRow().getIndex();
			String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
			Integer theElementId = this.idToGridMappings.get(theSelectedRowIndex);
			this.expandedRows.add(theType + theElementId);
			this.displayPlan();
			this.setButtonsStatus();
		}
	}

	private void collapseSelection() {
		Integer theSelectedRowIndex = this.testPlanGrid.getSelectedRow().getIndex();
		String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
		Integer theElementId = this.idToGridMappings.get(theSelectedRowIndex);
		this.expandedRows.remove(theType + theElementId);
		this.displayPlan();
		this.setButtonsStatus();
	}

	private void collapseAll() {
		for (Row theRow : this.testPlanGrid.getRows()) {
			this.testPlanGrid.setSelectedRowByIndex(theRow.getIndex());
			String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
			Integer theElementId = this.idToGridMappings.get(theRow.getIndex());
			this.expandedRows.remove(theType + theElementId);
		}
		this.displayPlan();
		this.testPlanGrid.setSelectedRowByIndex(0);
		this.setButtonsStatus();
	}

	private void expandAll() {
		for (Row theRow : this.testPlanGrid.getRows()) {
			this.testPlanGrid.setSelectedRowByIndex(theRow.getIndex());
			String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
			Integer theElementId = this.idToGridMappings.get(theRow.getIndex());
			this.expandedRows.add(theType + theElementId);
		}
		this.displayPlan();
		this.testPlanGrid.setSelectedRowByIndex(0);
		this.setButtonsStatus();
	}

	private void viewSelection() {
		Integer theSelectedRowIndex = this.testPlanGrid.getSelectedRow().getIndex();
		Integer theId = this.idToGridMappings.get(theSelectedRowIndex);
		if (theId != null) {
			this.testPlanGrid.setEnabled(false);
			String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
			if (theType.equals(TestPlanMaintenance.FOLDER)) {
				new FolderView(theId, this.testPlanMaintenance);
			}
			if (theType.equals(TestPlanMaintenance.TEST_RUN)) {

				new TestCaseView(theId, this.testPlanMaintenance, false);
			}
			this.testPlanGrid.setEnabled(true);
		}
	}

	private void deleteSelection() {
		Integer theSelectedElementId = this.testPlanGrid.getSelectedRowId();
		if (theSelectedElementId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				String theType = (String) this.testPlanGrid.getSelectedRow().get(TYPE);
				if (theType.equals(TestPlanMaintenance.FOLDER)) {
					this.testPlanMaintenance.deleteTestFolder(theSelectedElementId);
				} else if (theType.equals(TestPlanMaintenance.TEST_RUN)) {
					this.testPlanMaintenance.deleteTestCase(theSelectedElementId);
				}
				this.displayPlan();
				this.setButtonsStatus();
			}
		}
	}

	public void update(Observable aObservable, Object aObject) {
		this.expandSelection();
		this.displayPlan();
		this.setButtonsStatus();
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			this.testPlanMaintenance.removeUnsavedTestRunComments();
			this.testPlanMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void saveTestPlan() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(TestPlanMaintenance.NAME, this.nameTextField.getText());
		theData.put(TestPlanMaintenance.TEST_PLAN_ELEMENTS, this.testPlanModel.getRows());
		theData.put(ITestMaintenance.CREATED_BY, this.createdByTextField.getText());

		List<String> theErrors = this.testPlanMaintenance.saveTestPlan(theData);
		if (theErrors.isEmpty()) {
			this.setVisible(false);
		} else {
			this.viewErrors(theErrors);
		}
	}

	private void viewErrors(List<String> aErrors) {
		StringBuffer theErrorMessages = new StringBuffer();
		for (String theError : aErrors) {
			theErrorMessages.append(theError);
			theErrorMessages.append("\n");
		}
		MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, theErrorMessages.toString());
	}
}
