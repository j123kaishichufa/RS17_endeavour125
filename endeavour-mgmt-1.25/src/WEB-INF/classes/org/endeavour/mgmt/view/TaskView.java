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

import org.endeavour.mgmt.controller.DependencyMaintenance;
import org.endeavour.mgmt.controller.DocumentAssignmentMaintenance;
import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ITaskMaintenance;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
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
public class TaskView extends DialogComponent implements ActionListener, Observer {

	private Button saveButton = null;
	private Button cancelButton = null;
	private Button editButton = null;
	private BasicInfoPanel basicInfoPanel = null;
	private ITaskMaintenance taskMaintenance = null;
	private ProjectMembersTabSheet projectMembersTabSheet = null;
	private URLListComponent documentsList = null;
	private DocumentAssignmentMaintenance documentAssignmentMaintenance = null;
	private AttachmentsTabSheet attachmentsTabSheet = null;
	private CommentsTabSheet commentsTabSheet = null;
	private TextArea descriptionTextArea = null;
	private TabSheetComponent dependenciesTab = null;
	private TabSheetComponent documentsTab = null;
	private DependenciesListView dependenciesListView = null;
	private DependencyMaintenance dependencyMaintenance = null;
	private DropDownGridBoxComponent iterationDropDown = null;
	private AssignedDocumentsURLModel assignedDocumentsURLModel = null;

	public TaskView(ITaskMaintenance aTaskMaintenance) {
		this.initializeControllers(aTaskMaintenance);

		super.setTitle(IViewConstants.RB.getString("task.lbl"));
		super.setSize(550, 500);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		TabSheetComponent theTaskTab = new TabSheetComponent(IViewConstants.RB.getString("task.lbl"));
		theTaskTab.setImage(IViewConstants.TASKS_ICON);
		theTaskTab.setLayout(new TableLayout(new double[][] { { 0 }, { 175, 0 } }, 5));

		this.basicInfoPanel = new BasicInfoPanel(aTaskMaintenance.getParentStartDate(), aTaskMaintenance.getParentEndDate());
		this.basicInfoPanel.displayStatus(false, false);
		this.basicInfoPanel.displayPriority();
		theTaskTab.add(this.basicInfoPanel.setLimit("0, 0"));

		PanelComponent theDetailsPanel = new PanelComponent();
		theDetailsPanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 0 } }, 5, 5));
		
		IterationMaintenance theIterationMaintenance = new IterationMaintenance(this.taskMaintenance.getProject());
		IterationsListModel theIterationsListModel = new IterationsListModel(theIterationMaintenance.getIterations());
		Label theIterationlabel = new Label(IViewConstants.RB.getString("iteration.lbl") + ":");
		theDetailsPanel.add(theIterationlabel.setLimit("0, 0"));
		this.iterationDropDown = new DropDownGridBoxComponent(theIterationsListModel, 0);
		theDetailsPanel.add(this.iterationDropDown.setLimit("1, 0"));
		
		Label theDescriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		theDetailsPanel.add(theDescriptionLabel.setLimit("0, 1"));

		this.descriptionTextArea = new TextArea();
		theDetailsPanel.add(this.descriptionTextArea.setLimit("1, 1"));

		theTaskTab.add(theDetailsPanel.setLimit("0, 1"));

		theTabFolder.add(theTaskTab);

		this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("stakeholders.lbl"));
		this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.taskMaintenance.getUnassignedProjectMembersDataForProject());
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

		this.dependenciesTab = new TabSheetComponent(IViewConstants.RB.getString("dependencies.lbl"));
		this.dependenciesTab.setImage(IViewConstants.DEPENDENCIES_ICON);
		this.dependenciesTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 0));
		this.dependenciesListView = new DependenciesListView(this.dependencyMaintenance);
		this.dependenciesTab.add(this.dependenciesListView.setLimit("0, 0"));
		theTabFolder.add(this.dependenciesTab);

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

	public TaskView(Integer aTaskId, ITaskMaintenance aTaskMaintenance) {
		this(aTaskMaintenance);
		this.viewTask(aTaskId);
	}

	private void viewTask(Integer aTaskId) {
		Map<String, Object> theData = this.taskMaintenance.getTaskDataBy(aTaskId);
		this.basicInfoPanel.setData(theData);

		this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(WorkProductMaintenance.PROJECT_MEMBERS));
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.taskMaintenance.getUnassignedProjectMembersDataForProject());
		this.documentAssignmentMaintenance.setAssignedDocuments((List) theData.get(TaskMaintenance.DOCUMENTS));
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.taskMaintenance.getUnassignedDocuments());
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.attachmentsTabSheet.setData(theData);
		this.commentsTabSheet.setData(theData);
		this.dependenciesListView.setData(theData);
		this.descriptionTextArea.setText((String) theData.get(TaskMaintenance.DESCRIPTION));
		this.iterationDropDown.setSelectedRowObject((Integer) theData.get(TaskMaintenance.ITERATION));

		super.setTitle("Task - " + (String) theData.get(IBasicInfoMaintenance.NAME));
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
		this.dependenciesTab.setCount(this.dependenciesListView.getRowCount());
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveTask();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
		if (theSource.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
			new DocumentAssignmentView(this.documentAssignmentMaintenance);
		}
	}

	private void initializeControllers(ITaskMaintenance aTaskMaintenance) {
		this.taskMaintenance = aTaskMaintenance;
		this.taskMaintenance.startUnitOfWork();
		this.taskMaintenance.reset();

		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.documentAssignmentMaintenance = new DocumentAssignmentMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.taskMaintenance.getUnassignedDocuments());
		this.documentAssignmentMaintenance.addObserver(this);

		this.dependencyMaintenance = new DependencyMaintenance(aTaskMaintenance);
		this.dependencyMaintenance.addObserver(this);
	}

	private void saveTask() {
		Map<String, Object> theData = new HashMap<String, Object>();
		this.basicInfoPanel.getData(theData);
		theData.put(TaskMaintenance.DEPENDENCIES, this.dependencyMaintenance.getDependencies());
		theData.put(TaskMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
		theData.put(TaskMaintenance.DOCUMENTS, this.documentAssignmentMaintenance.getAssignedDocuments());
		theData.put(TaskMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
		theData.put(TaskMaintenance.ITERATION, this.iterationDropDown.getSelectedRowId());
		this.attachmentsTabSheet.getData(theData);
		this.commentsTabSheet.getData(theData);

		List<String> theErrors = this.taskMaintenance.saveTask(theData);
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
		this.dependenciesTab.setCount(this.dependenciesListView.getRowCount());
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			this.taskMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
		
		this.iterationDropDown.setEnabled(!this.taskMaintenance.isIterationDerived());
	}
}
