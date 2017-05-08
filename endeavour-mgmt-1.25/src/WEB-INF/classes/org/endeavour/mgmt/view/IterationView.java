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

import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.AssignedChangeRequestsListModel;
import org.endeavour.mgmt.view.model.AssignedDefectsListModel;
import org.endeavour.mgmt.view.model.AssignedTasksListModel;
import org.endeavour.mgmt.view.model.AssignedUseCasesListModel;
import org.endeavour.mgmt.view.model.UnassignedChangeRequestsListModel;
import org.endeavour.mgmt.view.model.UnassignedDefectsListModel;
import org.endeavour.mgmt.view.model.UnassignedTasksListModel;
import org.endeavour.mgmt.view.model.UnassignedUseCasesListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.event.DropEvent;
import thinwire.ui.event.DropListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

@SuppressWarnings("unchecked")
public class IterationView extends DialogComponent implements ActionListener, DropListener {

	private Button saveButton = null;
	private Button cancelButton = null;
	private BasicInfoPanel basicInfoPanel = null;
	private IterationMaintenance iterationMaintenance = null;
	private TabSheetComponent useCasesTab = null;
	private GridBoxComponent assignedUseCasesGrid = null;
	private GridBoxComponent unassignedUseCasesGrid = null;
	private TabSheetComponent defectsTab = null;
	private GridBoxComponent assignedDefectsGrid = null;
	private GridBoxComponent unassignedDefectsGrid = null;
	private TabSheetComponent changeRequestsTab = null;
	private GridBoxComponent assignedChangeRequestsGrid = null;
	private GridBoxComponent unassignedChangeRequestsGrid = null;
	private TabSheetComponent tasksTab = null;
	private GridBoxComponent assignedTasksGrid = null;
	private GridBoxComponent unassignedTasksGrid = null;

	private AssignedUseCasesListModel assignedUseCasesModel = null;
	private UnassignedUseCasesListModel unassignedUseCasesModel = null;

	private AssignedDefectsListModel assignedDefectsModel = null;
	private UnassignedDefectsListModel unassignedDefectsModel = null;

	private AssignedChangeRequestsListModel assignedChangeRequestsModel = null;
	private UnassignedChangeRequestsListModel unassignedChangeRequestsModel = null;

	private AssignedTasksListModel assignedTasksModel = null;
	private UnassignedTasksListModel unassignedTasksModel = null;

	public IterationView(IterationMaintenance aIterationMaintenance) {

		this.iterationMaintenance = aIterationMaintenance;
		this.iterationMaintenance.startUnitOfWork();
		this.iterationMaintenance.reset();

		super.setTitle(IViewConstants.RB.getString("iteration.lbl"));
		super.setSize(520, 500);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		TabSheetComponent theIterationTab = new TabSheetComponent(IViewConstants.RB.getString("iteration.lbl"));
		theIterationTab.setImage(IViewConstants.ITERATIONS_ICON);
		theIterationTab.setLayout(new TableLayout(new double[][] { { 0 }, { 175, 0 } }, 5));

		this.basicInfoPanel = new BasicInfoPanel(aIterationMaintenance.getProjectStartDate(), aIterationMaintenance.getProjectEndDate());
		this.basicInfoPanel.setProgressDropDownStatus(false);
		theIterationTab.add(this.basicInfoPanel.setLimit("0, 0"));

		theTabFolder.add(theIterationTab);

		this.useCasesTab = new TabSheetComponent(IViewConstants.RB.getString("use_cases.lbl"));
		this.useCasesTab.setImage(IViewConstants.USE_CASES_ICON);
		this.useCasesTab.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));
		Label theUseCasesAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theUseCasesAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.useCasesTab.add(theUseCasesAssignmentLabel.setLimit("0, 0"));
		this.initializeUnassignedUseCasesGrid();
		this.initializeAssignedUseCasesGrid();
		this.assignedUseCasesGrid.addDropListener(this.unassignedUseCasesGrid, this);
		this.unassignedUseCasesGrid.addDropListener(this.assignedUseCasesGrid, this);
		theTabFolder.add(useCasesTab);

		this.defectsTab = new TabSheetComponent(IViewConstants.RB.getString("defects.lbl"));
		this.defectsTab.setImage(IViewConstants.DEFECTS_ICON);
		this.defectsTab.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));
		Label theDefectsAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theDefectsAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.defectsTab.add(theDefectsAssignmentLabel.setLimit("0, 0"));
		this.initializeUnassignedDefectsGrid();
		this.initializeAssignedDefectsGrid();
		this.assignedDefectsGrid.addDropListener(this.unassignedDefectsGrid, this);
		this.unassignedDefectsGrid.addDropListener(this.assignedDefectsGrid, this);
		theTabFolder.add(defectsTab);

		this.changeRequestsTab = new TabSheetComponent(IViewConstants.RB.getString("change_requests.lbl"));
		this.changeRequestsTab.setImage(IViewConstants.CHANGE_REQUESTS_ICON);
		this.changeRequestsTab.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));
		Label theChangeRequestAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theChangeRequestAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.changeRequestsTab.add(theChangeRequestAssignmentLabel.setLimit("0, 0"));
		this.initializeUnassignedChangeRequestsGrid();
		this.initializeAssignedChangeRequestsGrid();
		this.assignedChangeRequestsGrid.addDropListener(this.unassignedChangeRequestsGrid, this);
		this.unassignedChangeRequestsGrid.addDropListener(this.assignedChangeRequestsGrid, this);
		theTabFolder.add(changeRequestsTab);

		this.tasksTab = new TabSheetComponent(IViewConstants.RB.getString("tasks.lbl"));
		this.tasksTab.setImage(IViewConstants.TASKS_ICON);
		this.tasksTab.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));
		Label theTasksAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theTasksAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.tasksTab.add(theTasksAssignmentLabel.setLimit("0, 0"));
		this.initializeUnassignedTasksGrid();
		this.initializeAssignedTasksGrid();
		this.assignedTasksGrid.addDropListener(this.unassignedTasksGrid, this);
		this.unassignedTasksGrid.addDropListener(this.assignedTasksGrid, this);
		theTabFolder.add(tasksTab);

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

	public IterationView(Integer aIterationId, IterationMaintenance aIterationMaintenance) {
		this(aIterationMaintenance);
		this.viewIteration(aIterationId);
	}

	private void initializeUnassignedUseCasesGrid() {
		this.unassignedUseCasesModel = new UnassignedUseCasesListModel(this.iterationMaintenance.getUnassignedUseCases());
		this.unassignedUseCasesGrid = new GridBoxComponent(this.unassignedUseCasesModel);
		this.useCasesTab.add(this.unassignedUseCasesGrid.setLimit("0, 1"));
	}

	private void initializeAssignedUseCasesGrid() {
		this.assignedUseCasesModel = new AssignedUseCasesListModel();
		this.assignedUseCasesGrid = new GridBoxComponent(this.assignedUseCasesModel);
		this.useCasesTab.add(this.assignedUseCasesGrid.setLimit("1, 1"));
	}

	private void initializeUnassignedDefectsGrid() {
		this.unassignedDefectsModel = new UnassignedDefectsListModel(this.iterationMaintenance.getUnassignedDefects());
		this.unassignedDefectsGrid = new GridBoxComponent(this.unassignedDefectsModel);
		this.defectsTab.add(this.unassignedDefectsGrid.setLimit("0, 1"));
	}

	private void initializeAssignedDefectsGrid() {
		this.assignedDefectsModel = new AssignedDefectsListModel();
		this.assignedDefectsGrid = new GridBoxComponent(this.assignedDefectsModel);
		this.defectsTab.add(this.assignedDefectsGrid.setLimit("1, 1"));
	}

	private void initializeUnassignedChangeRequestsGrid() {
		this.unassignedChangeRequestsModel = new UnassignedChangeRequestsListModel(this.iterationMaintenance.getUnassignedChangeRequests());
		this.unassignedChangeRequestsGrid = new GridBoxComponent(this.unassignedChangeRequestsModel);
		this.changeRequestsTab.add(this.unassignedChangeRequestsGrid.setLimit("0, 1"));
	}

	private void initializeAssignedChangeRequestsGrid() {
		this.assignedChangeRequestsModel = new AssignedChangeRequestsListModel();
		this.assignedChangeRequestsGrid = new GridBoxComponent(this.assignedChangeRequestsModel);
		this.changeRequestsTab.add(this.assignedChangeRequestsGrid.setLimit("1, 1"));
	}

	private void initializeUnassignedTasksGrid() {
		this.unassignedTasksModel = new UnassignedTasksListModel(this.iterationMaintenance.getUnassignedTasks());
		this.unassignedTasksGrid = new GridBoxComponent(this.unassignedTasksModel);
		this.tasksTab.add(this.unassignedTasksGrid.setLimit("0, 1"));
	}

	private void initializeAssignedTasksGrid() {
		this.assignedTasksModel = new AssignedTasksListModel();
		this.assignedTasksGrid = new GridBoxComponent(this.assignedTasksModel);
		this.tasksTab.add(this.assignedTasksGrid.setLimit("1, 1"));
	}

	public void dropPerformed(DropEvent aEvent) {
		GridBoxComponent theDestinationGrid = (GridBoxComponent) aEvent.getSourceComponent();
		GridBoxComponent theSourceGrid = (GridBoxComponent) aEvent.getDragComponent();

		GridBox.Row theDragRow = ((GridBox.Range) aEvent.getDragObject()).getRow();
		theDragRow.setSelected(true);

		Object theDragObject = theSourceGrid.getSelectedRowObject();
		theSourceGrid.remove(theDragRow, theDragObject);
		theDestinationGrid.add(theDragRow, theDragObject);
		
		this.useCasesTab.setCount(this.assignedUseCasesModel.getRowCount());
		this.defectsTab.setCount(this.assignedDefectsModel.getRowCount());
		this.changeRequestsTab.setCount(this.assignedChangeRequestsModel.getRowCount());
		this.tasksTab.setCount(this.assignedTasksModel.getRowCount());
	}

	private void viewIteration(Integer aIterationId) {
		Map<String, Object> theData = this.iterationMaintenance.getIterationDataBy(aIterationId);
		this.basicInfoPanel.setData(theData);
		this.assignedUseCasesModel.setData((List) theData.get(IterationMaintenance.ASSIGNED_USE_CASES));
		this.assignedDefectsModel.setData((List) theData.get(IterationMaintenance.ASSIGNED_DEFECTS));
		this.assignedChangeRequestsModel.setData((List) theData.get(IterationMaintenance.ASSIGNED_CHANGE_REQUESTS));
		this.assignedTasksModel.setData((List) theData.get(IterationMaintenance.ASSIGNED_TASKS));

		super.setTitle("Iteration - " + (String) theData.get(IBasicInfoMaintenance.NAME));
		this.useCasesTab.setCount(this.assignedUseCasesModel.getRowCount());
		this.defectsTab.setCount(this.assignedDefectsModel.getRowCount());
		this.changeRequestsTab.setCount(this.assignedChangeRequestsModel.getRowCount());
		this.tasksTab.setCount(this.assignedTasksModel.getRowCount());
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveIteration();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void saveIteration() {
		Map<String, Object> theData = new HashMap<String, Object>();
		this.basicInfoPanel.getData(theData);
		List<Integer> theWorkProductIds = new ArrayList<Integer>();
		theWorkProductIds.addAll(this.assignedUseCasesGrid.getAllRowIds());
		theWorkProductIds.addAll(this.assignedDefectsGrid.getAllRowIds());
		theWorkProductIds.addAll(this.assignedChangeRequestsGrid.getAllRowIds());
		theWorkProductIds.addAll(this.assignedTasksGrid.getAllRowIds());
		theData.put(IterationMaintenance.WORK_PRODUCTS, theWorkProductIds);
		List<String> theErrors = this.iterationMaintenance.saveIteration(theData);
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
			this.iterationMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
