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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.EventMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ITestMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.EventListComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.TestCaseStatusListModel;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class TestCaseView extends DialogComponent implements ActionListener {

	private Label nameLabel = null;
	private TextField nameTextField = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private Label createdByLabel = null;
	private TextField createdByTextField = null;
	private Label purposeLabel = null;
	private TextArea purposeTextArea = null;
	private Label prerequisitesLabel = null;
	private TextArea prerequisitesTextArea = null;
	private Label testDataLabel = null;
	private TextArea testDataTextArea = null;
	private ITestMaintenance testCaseMaintenance = null;
	private CommentsTabSheet commentsTabSheet = null;
	private ProjectMembersTabSheet projectMembersTabSheet = null;
	private EventMaintenance eventMaintenance = null;
	private EventListComponent stepsList = null;
	private DropDownGridBoxComponent statusDropDown = null;
	private boolean isTestCase = false;
	private TestCaseStatusListModel statusListModel = null;

	public TestCaseView(ITestMaintenance aTestCaseMaintenance) {
		this.initialize(aTestCaseMaintenance, true);
	}

	private void initialize(ITestMaintenance aTestCaseMaintenance, boolean isTestCase) {
		this.isTestCase = isTestCase;
		this.testCaseMaintenance = aTestCaseMaintenance;

		if (this.isTestCase) {
			this.testCaseMaintenance.startUnitOfWork();
			this.testCaseMaintenance.resetTestCase();
		}

		super.setTitle(IViewConstants.RB.getString("test_case.lbl"));
		super.setSize(800, 550);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		TabSheetComponent theTestCaseTabSheet = new TabSheetComponent(IViewConstants.RB.getString("test_case.lbl"));
		theTestCaseTabSheet.setImage(IViewConstants.TEST_CASES_ICON);
		theTestCaseTabSheet.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 20, 70, 150, 150, 20 } }, 5, 5));

		this.nameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		theTestCaseTabSheet.add(this.nameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		theTestCaseTabSheet.add(this.nameTextField.setLimit("1, 0"));

		this.createdByLabel = new Label(IViewConstants.RB.getString("created_by.lbl"));
		theTestCaseTabSheet.add(this.createdByLabel.setLimit("0, 1"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		this.createdByTextField = new TextField();
		this.createdByTextField.setText(theSecurityMaintenance.getLoggedUserId());
		this.createdByTextField.setEnabled(false);
		theTestCaseTabSheet.add(this.createdByTextField.setLimit("1, 1"));

		this.purposeLabel = new Label(IViewConstants.RB.getString("purpose.lbl") + ":");
		theTestCaseTabSheet.add(this.purposeLabel.setLimit("0, 2"));

		this.purposeTextArea = new TextArea();
		theTestCaseTabSheet.add(this.purposeTextArea.setLimit("1, 2"));

		this.prerequisitesLabel = new Label(IViewConstants.RB.getString("prerequisites.lbl"));
		theTestCaseTabSheet.add(this.prerequisitesLabel.setLimit("0, 3"));

		this.prerequisitesTextArea = new TextArea();
		theTestCaseTabSheet.add(this.prerequisitesTextArea.setLimit("1, 3"));

		this.testDataLabel = new Label(IViewConstants.RB.getString("test_data.lbl"));
		theTestCaseTabSheet.add(this.testDataLabel.setLimit("0, 4"));

		this.testDataTextArea = new TextArea();
		theTestCaseTabSheet.add(this.testDataTextArea.setLimit("1, 4"));

		if (!this.isTestCase) {
			Label theStatusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
			theTestCaseTabSheet.add(theStatusLabel.setLimit("0, 5"));

			this.statusListModel = new TestCaseStatusListModel();
			this.statusDropDown = new DropDownGridBoxComponent(this.statusListModel, 0);
			theTestCaseTabSheet.add(this.statusDropDown.setLimit("1, 5"));
		}

		theTabFolder.add(theTestCaseTabSheet);

		TabSheetComponent theSteps = new TabSheetComponent(IViewConstants.RB.getString("steps.lbl"));
		theSteps.setImage(IViewConstants.EVENTS_ICON);
		theSteps.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5, 5));
		this.eventMaintenance = new EventMaintenance();
		this.stepsList = new EventListComponent(this.eventMaintenance, IViewConstants.RB.getString("steps.lbl"));
		this.stepsList.setVisibleButtons(this.isTestCase);
		theSteps.add(this.stepsList.setLimit("0, 0"));
		theTabFolder.add(theSteps);

		if (!this.isTestCase) {
			this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("stakeholders.lbl"));
			this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
			this.projectMembersTabSheet.setUnassignedProjectMembers(this.testCaseMaintenance.getUnassignedProjectMembers());
			theTabFolder.add(this.projectMembersTabSheet);
		}

		this.commentsTabSheet = new CommentsTabSheet(IViewConstants.RB.getString("comments.lbl")); 
		this.commentsTabSheet.setImage(IViewConstants.COMMENTS_ICON);
		theTabFolder.add(commentsTabSheet);

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

		if (!this.isTestCase) {
			this.nameTextField.setEnabled(false);
			this.purposeTextArea.setEnabled(false);
			this.prerequisitesTextArea.setEnabled(false);
			this.testDataTextArea.setEnabled(false);
		}

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public TestCaseView(Integer aTestCaseId, ITestMaintenance aTestCaseMaintenance) {
		this(aTestCaseMaintenance);
		this.viewTestCase(aTestCaseId);
	}

	public TestCaseView(Integer aTestCaseId, ITestMaintenance aTestCaseMaintenance, boolean isTestCase) {
		this.initialize(aTestCaseMaintenance, isTestCase);
		this.viewTestCase(aTestCaseId);
	}

	private void viewTestCase(Integer aTestCaseId) {
		Map<String, Object> theData = this.testCaseMaintenance.getTestCaseDataBy(aTestCaseId);
		String theName = (String) theData.get(ITestMaintenance.NAME);
		String theCreatedBy = (String) theData.get(ITestMaintenance.CREATED_BY);
		String thePrerequisites = (String) theData.get(ITestMaintenance.PREREQUISITES);
		String theTestData = (String) theData.get(ITestMaintenance.TEST_DATA);
		String thePurpose = (String) theData.get(ITestMaintenance.PURPOSE);
		String theStatus = (String) theData.get(ITestMaintenance.STATUS);

		this.nameTextField.setText(theName);
		this.purposeTextArea.setText(thePurpose);
		this.testDataTextArea.setText(theTestData);
		this.prerequisitesTextArea.setText(thePrerequisites);
		this.createdByTextField.setText(theCreatedBy);
		this.commentsTabSheet.setData(theData);
		this.stepsList.setModel(theData);
		if (!this.isTestCase) {
			this.statusDropDown.setText(this.statusListModel.getDescriptionByValue(theStatus));
		}

		if (!this.isTestCase) {
			this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(ITestMaintenance.PROJECT_MEMBERS));
			this.projectMembersTabSheet.setUnassignedProjectMembers(this.testCaseMaintenance.getUnassignedProjectMembers());
		}

		super.setTitle(IViewConstants.RB.getString("test_case.lbl") + " - " + theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveTestCase();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void saveTestCase() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(ITestMaintenance.NAME, this.nameTextField.getText());
		theData.put(ITestMaintenance.CREATED_BY, this.createdByTextField.getText());
		theData.put(ITestMaintenance.PURPOSE, this.purposeTextArea.getText());
		theData.put(ITestMaintenance.PREREQUISITES, this.prerequisitesTextArea.getText());
		theData.put(ITestMaintenance.TEST_DATA, this.testDataTextArea.getText());

		if (!this.isTestCase) {
			theData.put(ITestMaintenance.STATUS, this.statusListModel.getValueById(this.statusDropDown.getSelectedRowId()));
		}

		this.commentsTabSheet.getData(theData);
		theData.put(ITestMaintenance.STEPS, (List) this.stepsList.getData());

		if (!this.isTestCase) {
			theData.put(ITestMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
		}

		List<String> theErrors = this.testCaseMaintenance.saveTestCase(theData);
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

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			if (this.isTestCase) {
				this.testCaseMaintenance.endUnitOfWork();
			}
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
