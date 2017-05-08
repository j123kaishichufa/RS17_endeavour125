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

import org.endeavour.mgmt.controller.DocumentAssignmentMaintenance;
import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.UseCaseMaintenance;
import org.endeavour.mgmt.controller.UseCaseTaskMaintenance;
import org.endeavour.mgmt.controller.WorkProductMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.components.URLListComponent;
import org.endeavour.mgmt.view.model.AssignedActorsListModel;
import org.endeavour.mgmt.view.model.AssignedDocumentsURLModel;
import org.endeavour.mgmt.view.model.IterationsListModel;
import org.endeavour.mgmt.view.model.UnassignedActorsListModel;
import org.endeavour.mgmt.view.model.UseCaseListModel;
import org.endeavour.mgmt.view.model.UseCaseTypeListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.TextField;
import thinwire.ui.WebBrowser;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.event.DropEvent;
import thinwire.ui.event.DropListener;
import thinwire.ui.event.PropertyChangeEvent;
import thinwire.ui.event.PropertyChangeListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

@SuppressWarnings("unchecked")
public class UseCaseView extends DialogComponent implements ActionListener, PropertyChangeListener, DropListener, Observer {

	private BasicInfoPanel basicInfoPanel = null;
	private UseCasePanel useCasePanel = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private Button editButton = null;
	private TabSheetComponent tasksTab = null;
	private TabSheetComponent actorsTab = null;
	private TabSheetComponent documentsTab = null;
	private GridBoxComponent assignedActorsGrid = null;
	private GridBoxComponent unassignedActorsGrid = null;
	private UseCaseMaintenance useCaseMaintenance = null;
	private UseCaseTaskMaintenance useCaseTaskMaintenance = null;
	private TasksListView taskListView = null;
	private ProjectMembersTabSheet projectMembersTabSheet = null;
	private URLListComponent documentsList = null;
	private DocumentAssignmentMaintenance documentAssignmentMaintenance = null;
	private AttachmentsTabSheet attachmentsTabSheet = null;
	private CommentsTabSheet commentsTabSheet = null;
	private TextArea descriptionTextArea = null;
	private DropDownGridBoxComponent typeDropDown = null;
	private DropDownGridBoxComponent includeDropDown = null;
	private DropDownGridBoxComponent extendDropDown = null;
	private DropDownGridBoxComponent iterationDropDown = null;
	private TextArea preconditionsTextArea = null;
	private TextArea postConditionsTextArea = null;
	private WebBrowser printPreviewWebBrowser = null;
	private AssignedActorsListModel assignedActorsModel = null;
	private UnassignedActorsListModel unassignedActorsModel = null;
	private AssignedDocumentsURLModel assignedDocumentsURLModel = null;
	private UseCaseTypeListModel useCaseTypeListModel = null;

	public UseCaseView(UseCaseMaintenance aUseCaseMaintenance) {
		this.initializeControllers(aUseCaseMaintenance);

		super.setTitle(IViewConstants.RB.getString("use_case.lbl"));
		super.setSize(820, 700);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		TabSheetComponent theUseCaseTab = new TabSheetComponent(IViewConstants.RB.getString("use_case.lbl"));
		theUseCaseTab.setImage(IViewConstants.USE_CASES_ICON);
		theUseCaseTab.setLayout(new TableLayout(new double[][] { { 0 }, { 175, 500 } }, 5));

		this.basicInfoPanel = new BasicInfoPanel(aUseCaseMaintenance.getProjectStartDate(), aUseCaseMaintenance.getProjectEndDate(), 85);
		this.basicInfoPanel.displayStatus(false, false);
		this.basicInfoPanel.displayPriority();
		this.basicInfoPanel.getNameTextField().addPropertyChangeListener(TextField.PROPERTY_TEXT, this);
		theUseCaseTab.add(this.basicInfoPanel.setLimit("0, 0"));

		PanelComponent theDetailsPanel = new PanelComponent();
		theDetailsPanel.setLayout(new TableLayout(new double[][] { { 85, 0 }, { 80, 20, 20, 20, 20, 80, 80 } }, 5, 5));

		Label theDescriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		theDetailsPanel.add(theDescriptionLabel.setLimit("0, 0"));

		this.descriptionTextArea = new TextArea();
		theDetailsPanel.add(this.descriptionTextArea.setLimit("1, 0"));

		this.useCaseTypeListModel = new UseCaseTypeListModel();
		Label theTypeLabel = new Label(IViewConstants.RB.getString("type.lbl") + ":");
		theDetailsPanel.add(theTypeLabel.setLimit("0, 1"));
		this.typeDropDown = new DropDownGridBoxComponent(useCaseTypeListModel, 0);
		this.typeDropDown.selectFirstElement();
		theDetailsPanel.add(typeDropDown.setLimit("1, 1"));

		UseCaseListModel theUseCaseListModel = new UseCaseListModel(aUseCaseMaintenance.getUseCases());
		Label theIncludeLabel = new Label(IViewConstants.RB.getString("include.lbl"));
		theDetailsPanel.add(theIncludeLabel.setLimit("0, 2"));
		this.includeDropDown = new DropDownGridBoxComponent(theUseCaseListModel, 0);
		this.includeDropDown.selectFirstElement();
		theDetailsPanel.add(includeDropDown.setLimit("1, 2"));

		Label theExtendLabel = new Label(IViewConstants.RB.getString("extend.lbl"));
		theDetailsPanel.add(theExtendLabel.setLimit("0, 3"));
		this.extendDropDown = new DropDownGridBoxComponent(theUseCaseListModel, 0);
		this.extendDropDown.selectFirstElement();
		theDetailsPanel.add(extendDropDown.setLimit("1, 3"));
		
		IterationMaintenance theIterationMaintenance = new IterationMaintenance(this.useCaseMaintenance.getProject());
		IterationsListModel theIterationsListModel = new IterationsListModel(theIterationMaintenance.getIterations());
		Label theIterationlabel = new Label(IViewConstants.RB.getString("iteration.lbl") + ":");
		theDetailsPanel.add(theIterationlabel.setLimit("0, 4"));
		this.iterationDropDown = new DropDownGridBoxComponent(theIterationsListModel, 0);
		theDetailsPanel.add(this.iterationDropDown.setLimit("1, 4"));

		Label thePreconditionsLabel = new Label(IViewConstants.RB.getString("preconditions.lbl"));
		theDetailsPanel.add(thePreconditionsLabel.setLimit("0, 5"));
		this.preconditionsTextArea = new TextArea();
		theDetailsPanel.add(preconditionsTextArea.setLimit("1, 5"));

		Label thePostconditionsLabel = new Label(IViewConstants.RB.getString("postconditions.lbl"));
		theDetailsPanel.add(thePostconditionsLabel.setLimit("0, 6"));
		this.postConditionsTextArea = new TextArea();
		theDetailsPanel.add(postConditionsTextArea.setLimit("1, 6"));

		theUseCaseTab.add(theDetailsPanel.setLimit("0, 1"));

		theTabFolder.add(theUseCaseTab);

		TabSheetComponent theRequirementsTab = new TabSheetComponent(IViewConstants.RB.getString("events.lbl"));
		theRequirementsTab.setImage(IViewConstants.EVENTS_ICON);
		theRequirementsTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5, 5));
		this.useCasePanel = new UseCasePanel(this.basicInfoPanel);
		theRequirementsTab.add(this.useCasePanel.setLimit("0, 0"));
		theTabFolder.add(theRequirementsTab);

		this.actorsTab = new TabSheetComponent(IViewConstants.RB.getString("actors.lbl"));
		this.actorsTab.setImage(IViewConstants.ACTORS_ICON);
		this.actorsTab.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));
		Label theAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.actorsTab.add(theAssignmentLabel.setLimit("0, 0"));
		this.initializeUnassignedActorsGrid();
		this.initializeAssignedActorsGrid();
		this.assignedActorsGrid.addDropListener(this.unassignedActorsGrid, this);
		this.unassignedActorsGrid.addDropListener(this.assignedActorsGrid, this);
		theTabFolder.add(this.actorsTab);

		this.tasksTab = new TabSheetComponent(IViewConstants.RB.getString("tasks.lbl"));
		this.tasksTab.setImage(IViewConstants.TASKS_ICON);
		this.tasksTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.taskListView = new TasksListView(this.useCaseTaskMaintenance, false);
		this.tasksTab.add(this.taskListView.setLimit("0, 0"));

		theTabFolder.add(this.tasksTab);

		this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("stakeholders.lbl"));
		this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.useCaseMaintenance.getUnassignedProjectMembersDataForProject());
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

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public UseCaseView(Integer aUseCaseId, UseCaseMaintenance aUseCaseMaintenance) {
		this(aUseCaseMaintenance);
		this.viewUseCase(aUseCaseId);
	}

	private void initializeControllers(UseCaseMaintenance aUseCaseMaintenance) {
		this.useCaseMaintenance = aUseCaseMaintenance;
		this.useCaseMaintenance.startUnitOfWork();
		this.useCaseMaintenance.reset();

		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		this.useCaseTaskMaintenance = new UseCaseTaskMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.useCaseTaskMaintenance.addObserver(this);
		
		this.documentAssignmentMaintenance = new DocumentAssignmentMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.useCaseMaintenance.getUnassignedDocuments());
		this.documentAssignmentMaintenance.addObserver(this);
	}

	private void viewUseCase(Integer aUseCaseId) {
		Map<String, Object> theData = this.useCaseMaintenance.getUseCaseDataBy(aUseCaseId);
		this.basicInfoPanel.setData(theData);
		this.useCasePanel.setData(theData);
		this.useCaseTaskMaintenance.setTaskParent(this.useCaseMaintenance.getUseCase());
		this.useCaseTaskMaintenance.setTasks((List) theData.get(UseCaseMaintenance.TASKS));

		String theUseCaseType = (String) theData.get(UseCaseMaintenance.TYPE);
		this.typeDropDown.setText(this.useCaseTypeListModel.getDescriptionByType(theUseCaseType) );
		this.includeDropDown.setSelectedRowObject((Integer) theData.get(UseCaseMaintenance.INCLUDE));
		this.extendDropDown.setSelectedRowObject((Integer) theData.get(UseCaseMaintenance.EXTEND));
		this.iterationDropDown.setSelectedRowObject((Integer) theData.get(UseCaseMaintenance.ITERATION));
		this.descriptionTextArea.setText((String) theData.get(UseCaseMaintenance.DESCRIPTION));
		this.preconditionsTextArea.setText((String) theData.get(UseCaseMaintenance.PRECONDITIONS));
		this.postConditionsTextArea.setText((String) theData.get(UseCaseMaintenance.POSTCONDITIONS));

		this.taskListView.update(null, null);

		this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(WorkProductMaintenance.PROJECT_MEMBERS));
		this.projectMembersTabSheet.setUnassignedProjectMembers(this.useCaseMaintenance.getUnassignedProjectMembersDataForProject());

		this.documentAssignmentMaintenance.setAssignedDocuments((List) theData.get(UseCaseMaintenance.DOCUMENTS));
		this.documentAssignmentMaintenance.setUnAssignedDocuments(this.useCaseMaintenance.getUnassignedDocuments());
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());

		this.assignedActorsModel.setData((List) theData.get(UseCaseMaintenance.ASSIGNED_ACTORS));
		this.unassignedActorsModel.setData(this.useCaseMaintenance.getUnassignedActors());

		this.attachmentsTabSheet.setData(theData);
		this.commentsTabSheet.setData(theData);
		
		super.setTitle(IViewConstants.RB.getString("use_case.lbl") + " - " + (String) theData.get(IBasicInfoMaintenance.NAME));
		
		this.tasksTab.setCount(this.taskListView.getRowCount());
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
		this.actorsTab.setCount(this.assignedActorsModel.getRowCount());
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
				int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
				if (theResult == IViewConstants.YES) {
					this.saveUseCase();
				}
			}
			if (theButton.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
				this.setVisible(false);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				new DocumentAssignmentView(this.documentAssignmentMaintenance);
			}
		}

		if (theSource instanceof TabSheetComponent) {
			this.printPreviewWebBrowser.setLocation(this.useCaseMaintenance.createPrintPreviewLocation());
		}
	}

	public void propertyChange(PropertyChangeEvent aEvent) {
		String theName = this.basicInfoPanel.getNameTextField().getText();
		this.useCasePanel.populateName(theName);
	}

	private void saveUseCase() {
		Map<String, Object> theData = new HashMap<String, Object>();
		this.basicInfoPanel.getData(theData);
		this.useCasePanel.getData(theData);
		theData.put(UseCaseMaintenance.TASKS, this.useCaseTaskMaintenance.getTasks());
		theData.put(UseCaseMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
		theData.put(UseCaseMaintenance.DOCUMENTS, this.documentAssignmentMaintenance.getAssignedDocuments());
		theData.put(UseCaseMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
		theData.put(UseCaseMaintenance.INCLUDE, this.includeDropDown.getSelectedRowId());
		theData.put(UseCaseMaintenance.EXTEND, this.extendDropDown.getSelectedRowId());
		theData.put(UseCaseMaintenance.TYPE, this.useCaseTypeListModel.getTypeByDescription(this.typeDropDown.getText()));
		theData.put(UseCaseMaintenance.PRECONDITIONS, this.preconditionsTextArea.getText());
		theData.put(UseCaseMaintenance.POSTCONDITIONS, this.postConditionsTextArea.getText());
		theData.put(UseCaseMaintenance.ITERATION, this.iterationDropDown.getSelectedRowId());
		
		this.attachmentsTabSheet.getData(theData);
		this.commentsTabSheet.getData(theData);

		List<Integer> theActorsIds = new ArrayList<Integer>();
		theActorsIds.addAll(this.assignedActorsGrid.getAllRowIds());
		theData.put(UseCaseMaintenance.ACTORS, theActorsIds);

		List<String> theErrors = this.useCaseMaintenance.saveUseCase(theData);
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

	public void dropPerformed(DropEvent aEvent) {
		GridBoxComponent theDestinationGrid = (GridBoxComponent) aEvent.getSourceComponent();
		GridBoxComponent theSourceGrid = (GridBoxComponent) aEvent.getDragComponent();

		GridBox.Row theDragRow = ((GridBox.Range) aEvent.getDragObject()).getRow();
		theDragRow.setSelected(true);

		Object theDragObject = theSourceGrid.getSelectedRowObject();
		theSourceGrid.remove(theDragRow, theDragObject);
		theDestinationGrid.add(theDragRow, theDragObject);
		
		this.actorsTab.setCount(this.assignedActorsModel.getRowCount());
	}

	public void update(Observable aObservable, Object aObject) {
		this.assignedDocumentsURLModel.setData(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.tasksTab.setCount(this.taskListView.getRowCount());
		this.documentsTab.setCount(this.assignedDocumentsURLModel.getRowCount());
	}

	private void initializeUnassignedActorsGrid() {
		this.unassignedActorsModel = new UnassignedActorsListModel();
		this.unassignedActorsModel.setData(this.useCaseMaintenance.getUnassignedActors());
		this.unassignedActorsGrid = new GridBoxComponent(this.unassignedActorsModel);
		this.actorsTab.add(this.unassignedActorsGrid.setLimit("0, 1"));
	}

	private void initializeAssignedActorsGrid() {
		this.assignedActorsModel = new AssignedActorsListModel();
		this.assignedActorsGrid = new GridBoxComponent(this.assignedActorsModel);
		this.actorsTab.add(this.assignedActorsGrid.setLimit("1, 1"));
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			this.useCaseMaintenance.removeUnsavedTaskComments();
			this.useCaseMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
