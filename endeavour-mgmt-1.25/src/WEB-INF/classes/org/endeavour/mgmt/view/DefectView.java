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

import org.endeavour.mgmt.controller.DefectMaintenance;
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
import org.endeavour.mgmt.view.model.IterationsListModel;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class DefectView extends DialogComponent implements ActionListener, Observer {

	private Button saveButton = null;
	private Button cancelButton = null;
	private Button editButton = null;
	private BasicInfoPanel basicInfoPanel = null;
	private DefectMaintenance defectMaintenance = null;
	private ProjectMembersTabSheet projectMembersTabSheet = null;
	private URLListComponent documentsList = null;
	private DocumentAssignmentMaintenance documentAssignmentMaintenance = null;
	private AttachmentsTabSheet attachmentsTabSheet = null;
	private CommentsTabSheet commentsTabSheet = null;
	private TabSheetComponent documentsTab = null;
	private TextArea descriptionTextArea = null;
	private DropDownGridBoxComponent iterationDropDown = null;
	private AssignedDocumentsURLModel assignedDocumentsURLModel = null;

	public DefectView(DefectMaintenance aDefectMaintenance) {
		this.initializeControllers(aDefectMaintenance);

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("defect.lbl"));
		super.setSize(500, 500);
		super.centerDialog();

		TabFolderComponent theTabFolder = new TabFolderComponent();
		TabSheetComponent theDefectTab = new TabSheetComponent(IViewConstants.RB.getString("defect.lbl"));
		theDefectTab.setImage(IViewConstants.DEFECTS_ICON);
		theDefectTab.setLayout(new TableLayout(new double[][] { { 0 }, { 175, 0 } }, 5));

		this.basicInfoPanel = new BasicInfoPanel(aDefectMaintenance.getProjectStartDate(), aDefectMaintenance.getProjectEndDate());
		this.basicInfoPanel.displayStatus(true, false);
		this.basicInfoPanel.displayPriority();
		theDefectTab.add(this.basicInfoPanel.setLimit("0, 0"));

		PanelComponent theDetailsPanel = new PanelComponent();
		theDetailsPanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 0 } }, 5, 5));
		
		IterationMaintenance theIterationMaintenance = new IterationMaintenance(this.defectMaintenance.getProject());
		IterationsListModel theIterationsListModel = new IterationsListModel(theIterationMaintenance.getIterations());
		Label theIterationlabel = new Label(IViewConstants.RB.getString("iteration.lbl") + ":");
		theDetailsPanel.add(theIterationlabel.setLimit("0, 0"));
		this.iterationDropDown = new DropDownGridBoxComponent(theIterationsListModel, 0);
		theDetailsPanel.add(this.iterationDropDown.setLimit("1, 0"));

		Label theDescriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		theDetailsPanel.add(theDescriptionLabel.setLimit("0, 1"));
		
		this.descriptionTextArea = new TextArea();
		theDetailsPanel.add(this.descriptionTextArea.setLimit("1, 1"));

		theDefectTab.add(theDetailsPanel.setLimit("0, 1"));

		theTabFolder.add(theDefectTab);

		this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("stakeholders.lbl"));
		this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.defectMaintenance.getUnassignedProjectMembersDataForProject());
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

	public DefectView(Integer aDefectId, DefectMaintenance aDefectMaintenance) {
		this(aDefectMaintenance);
		this.viewDefect(aDefectId);
	}

	private void viewDefect(Integer aDefectId) {
		Map<String, Object> theData = this.defectMaintenance.getDefectDataBy(aDefectId);
		this.basicInfoPanel.setData(theData);
		this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(WorkProductMaintenance.PROJECT_MEMBERS));
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.defectMaintenance.getUnassignedProjectMembersDataForProject());
		this.documentAssignmentMaintenance.setAssignedDocuments((List) theData.get(DefectMaintenance.DOCUMENTS));
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.defectMaintenance.getUnassignedDocuments());
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.descriptionTextArea.setText((String) theData.get(DefectMaintenance.DESCRIPTION));
		this.iterationDropDown.setSelectedRowObject((Integer) theData.get(DefectMaintenance.ITERATION));
		this.attachmentsTabSheet.setData(theData);
		this.commentsTabSheet.setData(theData);

		super.setTitle("Defect - " + (String) theData.get(IBasicInfoMaintenance.NAME));
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveDefect();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
		if (theSource.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
			new DocumentAssignmentView(this.documentAssignmentMaintenance);
		}
	}

	private void initializeControllers(DefectMaintenance aDefectMaintenance) {
		this.defectMaintenance = aDefectMaintenance;
		this.defectMaintenance.startUnitOfWork();
		this.defectMaintenance.reset();

		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.documentAssignmentMaintenance = new DocumentAssignmentMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.defectMaintenance.getUnassignedDocuments());
		this.documentAssignmentMaintenance.addObserver(this);
	}

	private void saveDefect() {

		Map<String, Object> theData = new HashMap<String, Object>();
		this.basicInfoPanel.getData(theData);
		theData.put(DefectMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
		theData.put(DefectMaintenance.DOCUMENTS, this.documentAssignmentMaintenance.getAssignedDocuments());
		theData.put(DefectMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
		theData.put(DefectMaintenance.ITERATION, this.iterationDropDown.getSelectedRowId());
		this.attachmentsTabSheet.getData(theData);
		this.commentsTabSheet.getData(theData);

		List<String> theErrors = this.defectMaintenance.saveDefect(theData);
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
			this.defectMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.DEFECT_TRACKING_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
