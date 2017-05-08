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

import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TestCaseMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.TestCasesListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class TestCasesListView extends PanelComponent implements ActionListener, Observer {

	private TestCaseMaintenance testCaseMaintenance = null;
	private GridBoxComponent testCasesGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private TestCasesListModel testCasesModel = null;
	private PanelComponent fieldsPanel = null;
	private Button okButton = null;
	private TextField nameTextField = null;
	private TextField numberTextField = null;

	public TestCasesListView() {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 30, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.TEST_CASES_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("test_cases.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		PanelComponent theParametersPanel = new PanelComponent();
		theParametersPanel.setLayout(new TableLayout(new double[][] { { 410, 0 }, { 50 } }, 0, 5));

		this.fieldsPanel = new PanelComponent();
		this.fieldsPanel.setLayout(new TableLayout(new double[][] { { 55, 50, 40, 250 }, { 20 } }, 0, 5));

		Label theNumberLabel = new Label(IViewConstants.RB.getString("test_case_number.lbl"));
		this.fieldsPanel.add(theNumberLabel.setLimit("0, 0"));

		this.numberTextField = new TextField();
		this.numberTextField.setEditMask("#########");
		this.fieldsPanel.add(this.numberTextField.setLimit("1, 0"));

		Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		this.fieldsPanel.add(theNameLabel.setLimit("2, 0"));

		this.nameTextField = new TextField();
		this.fieldsPanel.add(nameTextField.setLimit("3, 0"));

		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();

		PanelComponent theButtonPanel = new PanelComponent();
		theButtonPanel.setLayout(new TableLayout(new double[][] { { 100, 0 }, { 25 } }, 0, 5));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
		this.okButton.setEnabled(theProjectId != null);
		theButtonPanel.add(okButton.setLimit("0, 0"));

		theParametersPanel.add(this.fieldsPanel.setLimit("0, 0"));
		theParametersPanel.add(theButtonPanel.setLimit("1, 0"));

		super.add(theParametersPanel.setLimit("0, 1"));

		this.initializeTestCasesGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100 }, { 0 } }, 0, 5));

		this.newButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		this.newButton.addActionListener(Button.ACTION_CLICK, this);
		this.newButton.setImage(IViewConstants.NEW_BUTTON_ICON);
		theButtonsPanel.add(this.newButton.setLimit("1, 0"));

		this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		this.editButton.addActionListener(Button.ACTION_CLICK, this);
		this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
		theButtonsPanel.add(this.editButton.setLimit("2, 0"));

		this.deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteButton.addActionListener(Button.ACTION_CLICK, this);
		this.deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		theButtonsPanel.add(this.deleteButton.setLimit("3, 0"));

		this.setButtonsStatus();

		super.add(theButtonsPanel.setLimit("0, 3"));
	}

	private void setButtonsStatus() {
		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		this.editButton.setEnabled(isEnabled && !this.testCasesGrid.getRows().isEmpty());

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_DELETE);
		this.deleteButton.setEnabled(isEnabled && !this.testCasesGrid.getRows().isEmpty() && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_EDIT);
		this.newButton.setEnabled(isEnabled && hasPrivilege);
	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.testCaseMaintenance = new TestCaseMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.testCaseMaintenance.addObserver(this);
	}

	private void initializeTestCasesGrid() {
		this.testCasesModel = new TestCasesListModel();
		this.testCasesGrid = new GridBoxComponent(this.testCasesModel);
		this.testCasesGrid.setColumnWidth(0, 50);
		this.testCasesGrid.setColumnWidth(1, 250);
		this.testCasesGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.testCasesGrid.setLimit("0, 2"));
	}

	private void viewTestCases() {

		String theNumber = this.numberTextField.getText();
		String theName = this.nameTextField.getText();

		this.testCasesModel.setData(this.testCaseMaintenance.getTestCasesBy(theNumber, theName));
		Integer theId = this.testCaseMaintenance.getSelectedTestCaseId();
		if (theId != null) {
			this.testCasesGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewTestCase();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new TestCaseView(this.testCaseMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewTestCase();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteTestCase();
			}
			if (theButton.getText().equals(IViewConstants.OK_BUTTON_LABEL)) {
				this.testCaseMaintenance.startUnitOfWork();
				this.testCaseMaintenance.resetTestCase();
				this.viewTestCases();
				this.testCaseMaintenance.endUnitOfWork();
			}
		}
	}

	private void viewTestCase() {
		Integer theTestCaseId = this.testCasesGrid.getSelectedRowId();
		if (theTestCaseId != null) {
			this.testCasesGrid.setEnabled(false);
			new TestCaseView(theTestCaseId, this.testCaseMaintenance);
			this.testCasesGrid.setEnabled(true);
		}
	}

	private void deleteTestCase() {
		this.testCaseMaintenance.startUnitOfWork();
		Integer theTestCaseId = this.testCasesGrid.getSelectedRowId();
		if (theTestCaseId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.testCaseMaintenance.resetTestCase();
				this.testCaseMaintenance.deleteTestCase(theTestCaseId);
				this.viewTestCases();
			}
		}
		this.testCaseMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		// TODO after saving a TestCase the query does not retrieve the records
		// including the new one like when creating new WorkProducts.
		// this.viewTestCases();

		this.testCasesModel.setData(this.testCaseMaintenance.getTestCases());
		Integer theId = this.testCaseMaintenance.getSelectedTestCaseId();
		if (theId != null) {
			this.testCasesGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}
}
