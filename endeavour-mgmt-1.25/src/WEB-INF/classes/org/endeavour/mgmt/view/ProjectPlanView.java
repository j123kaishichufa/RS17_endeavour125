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

import org.endeavour.mgmt.controller.ChangeRequestMaintenance;
import org.endeavour.mgmt.controller.DefectMaintenance;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.ProjectPlanMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
import org.endeavour.mgmt.controller.UseCaseMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.ProjectPlanModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.WebBrowser;
import thinwire.ui.GridBox.Row;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ProjectPlanView extends PanelComponent implements ActionListener, Observer {

	private GridBoxComponent planGrid = null;
	private Button editButton = null;
	private Button expandButton = null;
	private Button collapseButton = null;
	private Button deleteButton = null;
	private Button collapseAllButton = null;
	private Button expandAllButton = null;
	private List<String> expandedRows = null;
	private Map<Integer, Integer> idToGridMappings = null;
	private ProjectPlanModel projectPlanModel = null;
	private TabSheetComponent projectPlanTab = null;
	private WebBrowser printPreviewWebBrowser = null;
	private WebBrowser projectScheduleWebBrowser = null;
	private ProjectPlanMaintenance projectPlanMaintenance = null;

	public static final String TYPE = IViewConstants.RB.getString("type.lbl");
	private static final String PROJECT_SCHEDULE = IViewConstants.RB.getString("project_schedule.lbl");

	public ProjectPlanView() {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 5, 5));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.PROJECT_PLAN_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("project_plan.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		this.projectPlanTab = new TabSheetComponent(IViewConstants.RB.getString("project_plan.lbl"));
		this.projectPlanTab.setImage(IViewConstants.PROJECT_PLAN_ICON);

		this.projectPlanTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.idToGridMappings = new HashMap<Integer, Integer>();
		this.expandedRows = new ArrayList<String>();
		this.initializeControllers();

		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		this.initializeProjectPlanGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 100, 100, 100, 100, 0, 100, 100, 100 }, { 0 } }, 0, 5));

		this.expandAllButton = new Button(IViewConstants.EXPAND_ALL_BUTTON_LABEL);
		this.expandAllButton.addActionListener(Button.ACTION_CLICK, this);
		this.expandAllButton.setEnabled(isEnabled);
		this.expandAllButton.setImage(IViewConstants.EXPAND_BUTTON_ICON);
		theButtonsPanel.add(this.expandAllButton.setLimit("0, 0"));

		this.expandButton = new Button(IViewConstants.EXPAND_BUTTON_LABEL);
		this.expandButton.addActionListener(Button.ACTION_CLICK, this);
		this.expandButton.setEnabled(isEnabled);
		this.expandButton.setImage(IViewConstants.EXPAND_BUTTON_ICON);
		theButtonsPanel.add(this.expandButton.setLimit("1, 0"));

		this.collapseButton = new Button(IViewConstants.COLLAPSE_BUTTON_LABEL);
		this.collapseButton.addActionListener(Button.ACTION_CLICK, this);
		this.collapseButton.setEnabled(isEnabled);
		this.collapseButton.setImage(IViewConstants.COLLAPSE_BUTTON_ICON);
		theButtonsPanel.add(this.collapseButton.setLimit("2, 0"));

		this.collapseAllButton = new Button(IViewConstants.COLLAPSE_ALL_BUTTON_LABEL);
		this.collapseAllButton.addActionListener(Button.ACTION_CLICK, this);
		this.collapseAllButton.setEnabled(isEnabled);
		this.collapseAllButton.setImage(IViewConstants.COLLAPSE_BUTTON_ICON);
		theButtonsPanel.add(this.collapseAllButton.setLimit("3, 0"));

		this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		this.editButton.addActionListener(Button.ACTION_CLICK, this);
		this.editButton.setEnabled(isEnabled);
		this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
		theButtonsPanel.add(this.editButton.setLimit("7, 0"));

		this.deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteButton.addActionListener(Button.ACTION_CLICK, this);
		this.deleteButton.setEnabled(false);
		this.deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		// theButtonsPanel.add(this.deleteButton.setLimit("7, 0"));

		if (isEnabled) {
			setButtonsStatus();
		}

		this.projectPlanTab.add(theButtonsPanel.setLimit("0, 1"));

		theTabFolder.add(this.projectPlanTab);

		TabSheetComponent theProjectScheduleTab = new TabSheetComponent(PROJECT_SCHEDULE);
		theProjectScheduleTab.setImage(IViewConstants.PROJECT_SCHEDULE_ICON);
		theProjectScheduleTab.addActionListener(TabSheetComponent.ACTION_CLICK, this);
		theProjectScheduleTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.projectScheduleWebBrowser = new WebBrowser();
		theProjectScheduleTab.add(this.projectScheduleWebBrowser.setLimit("0, 0"));
		theTabFolder.add(theProjectScheduleTab);

		TabSheetComponent thePrintPreviewTab = new TabSheetComponent(IViewConstants.PRINT_PREVIEW);
		thePrintPreviewTab.setImage(IViewConstants.PRINT_PREVIEW_ICON);
		thePrintPreviewTab.addActionListener(TabSheetComponent.ACTION_CLICK, this);
		thePrintPreviewTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.printPreviewWebBrowser = new WebBrowser();
		thePrintPreviewTab.add(this.printPreviewWebBrowser.setLimit("0, 0"));
		theTabFolder.add(thePrintPreviewTab);

		super.add(theTabFolder.setLimit("0, 1"));
	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.projectPlanMaintenance = new ProjectPlanMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.projectPlanMaintenance.addObserver(this);
	}

	private void initializeProjectPlanGrid() {
		this.projectPlanModel = new ProjectPlanModel(this.projectPlanMaintenance.getProjectPlanData(this.idToGridMappings, this.expandedRows));
		this.planGrid = new GridBoxComponent(this.projectPlanModel);
		this.planGrid.setColumnWidth(0, 400);
		this.planGrid.setColumnWidth(1, 40);
		this.planGrid.setSortAllowed(false);
		this.planGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.planGrid.addActionListener(GridBox.ACTION_CLICK, this);
		this.projectPlanTab.add(this.planGrid.setLimit("0, 0"));
	}

	private void displayPlan() {
		this.initializeControllers();
		int theRowIndex = this.planGrid.getSelectedRow().getIndex();
		this.projectPlanModel.setData(this.projectPlanMaintenance.getProjectPlanData(this.idToGridMappings, this.expandedRows));
		this.planGrid.setSelectedRowByIndex(theRowIndex);
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

		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewSelection();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteSelection();
			}
			if (theButton.getText().equals(IViewConstants.EXPAND_BUTTON_LABEL)) {
				this.expandSelection();
			}
			if (theButton.getText().equals(IViewConstants.COLLAPSE_BUTTON_LABEL)) {
				this.collapseSelection();
			}
			if (theButton.getText().equals(IViewConstants.COLLAPSE_ALL_BUTTON_LABEL)) {
				this.collapseAll();
			}
			if (theButton.getText().equals(IViewConstants.EXPAND_ALL_BUTTON_LABEL)) {
				this.expandAll();
			}
		}
		if (theSource instanceof TabSheetComponent) {
			TabSheetComponent theTabSheetComponent = (TabSheetComponent) theSource;
			if (theTabSheetComponent.getText().equals(PROJECT_SCHEDULE)) {
				this.projectPlanMaintenance.startUnitOfWork();
				this.initializeControllers();
				this.projectScheduleWebBrowser.setContent(this.projectPlanMaintenance.createProjectSchedule());
				this.projectPlanMaintenance.endUnitOfWork();
			}
			if (theTabSheetComponent.getText().equals(IViewConstants.PRINT_PREVIEW)) {
				if (MainView.getProjectDropDown().getSelectedRowId() != null) {
					this.printPreviewWebBrowser.setLocation(this.projectPlanMaintenance.createProjectPlanURL());
				}
			}
		}
	}

	private void setButtonsStatus() {
		Integer theSelectedRowIndex = this.planGrid.getSelectedRow().getIndex();
		String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
		Integer theSelectedElementId = this.idToGridMappings.get(theSelectedRowIndex);
		if (theType.equals(ProjectPlanMaintenance.ITERATION)) {
			boolean isExpanded = this.expandedRows.contains(theType + theSelectedElementId);
			this.expandButton.setEnabled(!isExpanded);
			this.collapseButton.setEnabled(isExpanded);
		} else if (theType.equals(ProjectPlanMaintenance.PROJECT)) {
			this.expandButton.setEnabled(false);
			this.collapseButton.setEnabled(false);
		} else {
			this.expandButton.setEnabled(false);
			this.collapseButton.setEnabled(false);
		}
	}

	private void expandSelection() {
		Integer theSelectedRowIndex = this.planGrid.getSelectedRow().getIndex();

		String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
		Integer theElementId = this.idToGridMappings.get(theSelectedRowIndex);

		this.expandedRows.add(theType + theElementId);
		this.projectPlanMaintenance.startUnitOfWork();
		this.displayPlan();
		this.projectPlanMaintenance.endUnitOfWork();
		this.setButtonsStatus();
	}

	private void collapseSelection() {
		Integer theSelectedRowIndex = this.planGrid.getSelectedRow().getIndex();

		String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
		Integer theElementId = this.idToGridMappings.get(theSelectedRowIndex);

		this.expandedRows.remove(theType + theElementId);
		this.projectPlanMaintenance.startUnitOfWork();
		this.displayPlan();
		this.projectPlanMaintenance.endUnitOfWork();
		this.setButtonsStatus();
	}

	private void collapseAll() {
		List<Row> theRows = this.planGrid.getRows();
		for (Row theRow : theRows) {
			this.planGrid.setSelectedRowByIndex(theRow.getIndex());
			String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
			Integer theElementId = this.idToGridMappings.get(theRow.getIndex());
			this.expandedRows.remove(theType + theElementId);
		}
		this.projectPlanMaintenance.startUnitOfWork();
		this.displayPlan();
		this.projectPlanMaintenance.endUnitOfWork();
		this.planGrid.setSelectedRowByIndex(0);
		this.setButtonsStatus();
	}

	private void expandAll() {
		List<Row> theRows = this.planGrid.getRows();
		for (Row theRow : theRows) {
			this.planGrid.setSelectedRowByIndex(theRow.getIndex());
			String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
			Integer theElementId = this.idToGridMappings.get(theRow.getIndex());
			this.expandedRows.add(theType + theElementId);
		}
		this.projectPlanMaintenance.startUnitOfWork();
		this.displayPlan();
		this.projectPlanMaintenance.endUnitOfWork();
		this.planGrid.setSelectedRowByIndex(0);
		this.setButtonsStatus();

	}

	private void viewSelection() {
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		theProjectMaintenance.addObserver(this);
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		Integer theSelectedRowIndex = this.planGrid.getSelectedRow().getIndex();
		Integer theId = this.idToGridMappings.get(theSelectedRowIndex);
		if (theId != null) {
			this.planGrid.setEnabled(false);
			String theType = (String) this.planGrid.getSelectedRow().get(TYPE);
			if (theType.equals(ProjectPlanMaintenance.PROJECT)) {
				new ProjectView(theId, theProjectMaintenance);
			}
			if (theType.equals(ProjectPlanMaintenance.USE_CASE)) {
				theProjectMaintenance.startUnitOfWork();
				UseCaseMaintenance theUseCaseMaintenance = new UseCaseMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
				theProjectMaintenance.endUnitOfWork();
				theUseCaseMaintenance.addObserver(this);
				new UseCaseView(theId, theUseCaseMaintenance);
			}
			if (theType.equals(ProjectPlanMaintenance.ITERATION)) {
				theProjectMaintenance.startUnitOfWork();
				IterationMaintenance theIterationMaintenance = new IterationMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
				theProjectMaintenance.endUnitOfWork();
				theIterationMaintenance.addObserver(this);
				new IterationView(theId, theIterationMaintenance);
			}
			if (theType.equals(ProjectPlanMaintenance.DEFECT)) {
				theProjectMaintenance.startUnitOfWork();
				DefectMaintenance theDefectMaintenance = new DefectMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
				theProjectMaintenance.endUnitOfWork();
				theDefectMaintenance.addObserver(this);
				new DefectView(theId, theDefectMaintenance);
			}
			if (theType.equals(ProjectPlanMaintenance.CHANGE_REQUEST)) {
				theProjectMaintenance.startUnitOfWork();
				ChangeRequestMaintenance theChangeRequestMaintenance = new ChangeRequestMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
				theProjectMaintenance.endUnitOfWork();
				theChangeRequestMaintenance.addObserver(this);
				new ChangeRequestView(theId, theChangeRequestMaintenance);
			}
			if (theType.equals(ProjectPlanMaintenance.TASK)) {
				theProjectMaintenance.startUnitOfWork();
				TaskMaintenance theTaskMaintenance = new TaskMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
				theProjectMaintenance.endUnitOfWork();
				theTaskMaintenance.addObserver(this);
				new TaskView(theId, theTaskMaintenance);
			}
			this.planGrid.setEnabled(true);
		}
	}

	private void deleteSelection() {
		Integer theProjectId = this.planGrid.getSelectedRowId();
		if (theProjectId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				// this.planMaintenance.deleteProject(theProjectId);
				// this.displayPlan();
			}
		}
	}

	public void update(Observable aObservable, Object aObject) {
		this.displayPlan();
	}
}
