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
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.ChangeRequestMaintenance;
import org.endeavour.mgmt.controller.DocumentAssignmentMaintenance;
import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.WorkProductMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.components.URLListComponent;
import org.endeavour.mgmt.view.model.AssignedDocumentsURLModel;
import org.endeavour.mgmt.view.model.ChangeTypeListModel;
import org.endeavour.mgmt.view.model.IterationsListModel;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class ChangeRequestView extends DialogComponent implements ActionListener, Observer {

	private Button saveButton = null;
	private Button cancelButton = null;
	private Button editButton = null;
	private BasicInfoPanel basicInfoPanel = null;
	private ChangeRequestMaintenance changeRequestMaintenance = null;
	private ProjectMembersTabSheet projectMembersTabSheet = null;
	private URLListComponent documentsList = null;
	private DocumentAssignmentMaintenance documentAssignmentMaintenance = null;
	private AttachmentsTabSheet attachmentsTabSheet = null;
	private CommentsTabSheet commentsTabSheet = null;
	private TabSheetComponent documentsTab = null;
	private TextArea descriptionTextArea = null;
	private DropDownGridBoxComponent changeTypeDropDown = null;
	private DropDownGridBoxComponent iterationDropDown = null;	
	private ChangeTypeListModel changeTypeListModel = null;

	private AssignedDocumentsURLModel assignedDocumentsURLModel = null;

	public ChangeRequestView(ChangeRequestMaintenance aChangeRequestMaintenance) {
		this.initializeControllers(aChangeRequestMaintenance);

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("change_request.lbl"));
		super.setSize(520, 500);
		super.centerDialog();

		TabFolderComponent theTabFolder = new TabFolderComponent();
		TabSheetComponent theChangeRequestTab = new TabSheetComponent(IViewConstants.RB.getString("change_request.lbl"));
		theChangeRequestTab.setImage(IViewConstants.CHANGE_REQUESTS_ICON);
		theChangeRequestTab.setLayout(new TableLayout(new double[][] { { 0 }, { 175, 0 } }, 5));

		this.basicInfoPanel = new BasicInfoPanel(aChangeRequestMaintenance.getProjectStartDate(), aChangeRequestMaintenance.getProjectEndDate());
		this.basicInfoPanel.displayStatus(true, false);
		this.basicInfoPanel.displayPriority();
		theChangeRequestTab.add(this.basicInfoPanel.setLimit("0, 0"));

		theTabFolder.add(theChangeRequestTab);

		PanelComponent theDetailsPanel = new PanelComponent();
		theDetailsPanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 20, 0 } }, 5, 5));

		Label theTypeLabel = new Label(IViewConstants.RB.getString("type.lbl") + ":");
		theDetailsPanel.add(theTypeLabel.setLimit("0, 0"));

		this.changeTypeListModel = new ChangeTypeListModel();
		this.changeTypeDropDown = new DropDownGridBoxComponent(this.changeTypeListModel, 0);
		this.changeTypeDropDown.selectFirstElement();

		theDetailsPanel.add(this.changeTypeDropDown.setLimit("1, 0"));
		
		IterationMaintenance theIterationMaintenance = new IterationMaintenance(this.changeRequestMaintenance.getProject());
		IterationsListModel theIterationsListModel = new IterationsListModel(theIterationMaintenance.getIterations());
		Label theIterationlabel = new Label(IViewConstants.RB.getString("iteration.lbl") + ":");
		theDetailsPanel.add(theIterationlabel.setLimit("0, 1"));
		this.iterationDropDown = new DropDownGridBoxComponent(theIterationsListModel, 0);
		theDetailsPanel.add(this.iterationDropDown.setLimit("1, 1"));
		
		Label theDescriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		theDetailsPanel.add(theDescriptionLabel.setLimit("0, 2"));

		this.descriptionTextArea = new TextArea();
		theDetailsPanel.add(this.descriptionTextArea.setLimit("1, 2"));

		theChangeRequestTab.add(theDetailsPanel.setLimit("0, 1"));

		this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("stakeholders.lbl"));
		this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.changeRequestMaintenance.getUnassignedProjectMembersDataForProject());
		theTabFolder.add(this.projectMembersTabSheet);

		documentsTab = new TabSheetComponent(IViewConstants.RB.getString("documents.lbl"));
		documentsTab.setImage(IViewConstants.DOCUMENTS_ICON);
		documentsTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.assignedDocumentsURLModel = new AssignedDocumentsURLModel();
		this.documentsList = new URLListComponent(this.assignedDocumentsURLModel);
		documentsTab.add(this.documentsList.setLimit("0, 0"));

		PanelComponent theEditButtonPanel = new PanelComponent();
		theEditButtonPanel.setLayout(new TableLayout(new double[][] { { 0, 100 }, { 0 } }));

		this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		this.editButton.addActionListener(Button.ACTION_CLICK, this);
		this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);

		theEditButtonPanel.add(this.editButton.setLimit("1, 0"));
		documentsTab.add(theEditButtonPanel.setLimit("0, 1"));

		theTabFolder.add(documentsTab);

		this.attachmentsTabSheet = new AttachmentsTabSheet(IViewConstants.RB.getString("attachments.lbl"));
		this.attachmentsTabSheet.setImage(IViewConstants.ATTACHMENTS_ICON);
		theTabFolder.add(this.attachmentsTabSheet);

		this.commentsTabSheet = new CommentsTabSheet(IViewConstants.RB.getString("comments.lbl"));
		this.commentsTabSheet.setImage(IViewConstants.COMMENTS_ICON);
		theTabFolder.add(this.commentsTabSheet);

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

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public ChangeRequestView(Integer aChangeRequestId, ChangeRequestMaintenance aChangeRequestMaintenance) {
		this(aChangeRequestMaintenance);
		this.viewChangeRequest(aChangeRequestId);
	}

	private void viewChangeRequest(Integer aChangeRequestId) {
		Map<String, Object> theData = this.changeRequestMaintenance.getChangeRequestDataBy(aChangeRequestId);
		this.basicInfoPanel.setData(theData);

		this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(WorkProductMaintenance.PROJECT_MEMBERS));
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.changeRequestMaintenance.getUnassignedProjectMembersDataForProject());
		this.documentAssignmentMaintenance.setAssignedDocuments((List) theData.get(ChangeRequestMaintenance.DOCUMENTS));
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.changeRequestMaintenance.getUnassignedDocuments());
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.attachmentsTabSheet.setData(theData);
		this.commentsTabSheet.setData(theData);
		this.descriptionTextArea.setText((String) theData.get(ChangeRequestMaintenance.DESCRIPTION));
		this.iterationDropDown.setSelectedRowObject((Integer) theData.get(ChangeRequestMaintenance.ITERATION));
		
		String theChangeType = (String) theData.get(ChangeRequestMaintenance.TYPE);
		this.changeTypeDropDown.setText(this.changeTypeListModel.getDescriptionByValue(theChangeType));

		super.setTitle(IViewConstants.RB.getString("change_request.lbl") + " - " + (String) theData.get(IBasicInfoMaintenance.NAME));
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveChangeRequest();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
		if (theSource.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
			new DocumentAssignmentView(this.documentAssignmentMaintenance);
		}
	}

	private void initializeControllers(ChangeRequestMaintenance aChangeRequestMaintenance) {
		this.changeRequestMaintenance = aChangeRequestMaintenance;
		this.changeRequestMaintenance.startUnitOfWork();
		this.changeRequestMaintenance.reset();

		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.documentAssignmentMaintenance = new DocumentAssignmentMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.changeRequestMaintenance.getUnassignedDocuments());
		this.documentAssignmentMaintenance.addObserver(this);
	}

	private void saveChangeRequest() {
		Map<String, Object> theData = new HashMap<String, Object>();
		this.basicInfoPanel.getData(theData);
		theData.put(ChangeRequestMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
		theData.put(ChangeRequestMaintenance.DOCUMENTS, this.documentAssignmentMaintenance.getAssignedDocuments());
		theData.put(ChangeRequestMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
		theData.put(ChangeRequestMaintenance.TYPE, this.changeTypeListModel.getValueById(this.changeTypeDropDown.getSelectedRowId()));
		theData.put(ChangeRequestMaintenance.ITERATION, this.iterationDropDown.getSelectedRowId());		

		this.attachmentsTabSheet.getData(theData);
		this.commentsTabSheet.getData(theData);

		List<String> theErrors = this.changeRequestMaintenance.saveChangeRequest(theData);
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

	public void update(Observable aObservable, Object aObject) {
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			this.changeRequestMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
