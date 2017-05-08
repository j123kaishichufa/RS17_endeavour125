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

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.ChangeRequestMaintenance;
import org.endeavour.mgmt.controller.DefectMaintenance;
import org.endeavour.mgmt.controller.HomeController;
import org.endeavour.mgmt.controller.IterationMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.ProjectMemberMaintenance;
import org.endeavour.mgmt.controller.ProjectPlanMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
import org.endeavour.mgmt.controller.UseCaseMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ProjectMemberStatusListModel;
import org.endeavour.mgmt.view.model.ProjectsListModel;
import org.endeavour.mgmt.view.model.WorkProductsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class HomeView extends PanelComponent implements Observer {

	private ProjectsListModel projectsModel = null;
	private GridBoxComponent projectsGrid = null;
	private WorkProductsListModel workProductsModel = null;
	private GridBoxComponent workProductsGrid = null;
	private HomeController homeController = null;
	private GridBoxComponent statusGrid = null;
	private ProjectMemberStatusListModel statusModel = null;
	private Button statusButton = null;
	private TextField statusTextField = null;

	public static final String TYPE = IViewConstants.RB.getString("type.lbl");

	public HomeView() {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 25, 0, 20, 0, 20, 0 } }, 5, 5));

		this.homeController = new HomeController();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.HOME_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("home.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeProjectsGrid();
		this.initializeWorkProductsGrid();
		this.initializeStatusGrid();
	}

	private void initializeProjectsGrid() {
		Label theProjectsLabel = new Label(IViewConstants.RB.getString("my_projects.lbl"));
		theProjectsLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		super.add(theProjectsLabel.setLimit("0, 5"));

		this.projectsModel = new ProjectsListModel(this.homeController.getProjects());
		this.projectsGrid = new GridBoxComponent(this.projectsModel);
		this.projectsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, new ProjectsGridListener());
		super.add(this.projectsGrid.setLimit("0, 6"));
	}

	private void initializeWorkProductsGrid() {
		Label theAssignmentsLabel = new Label(IViewConstants.RB.getString("my_assignments.lbl"));
		theAssignmentsLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		super.add(theAssignmentsLabel.setLimit("0, 3"));

		this.workProductsModel = new WorkProductsListModel(this.homeController.getWorkProducts());
		this.workProductsGrid = new GridBoxComponent(this.workProductsModel);
		this.workProductsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, new WorkProductsGridListener());
		this.workProductsGrid.setSortAllowed(false);
		this.workProductsGrid.setColumnWidth(0, 70);
		this.workProductsGrid.setColumnWidth(1, 200);
		this.workProductsGrid.setColumnWidth(2, 40);
		super.add(this.workProductsGrid.setLimit("0, 4"));
	}

	private void initializeStatusGrid() {

		PanelComponent theStatusPanel = new PanelComponent();
		theStatusPanel.setLayout(new TableLayout(new double[][] { { 70, 500, 100 }, { 25 } }, 0, 5));

		Label theProjectsLabel = new Label(IViewConstants.RB.getString("my_status.lbl"));
		theProjectsLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theStatusPanel.add(theProjectsLabel.setLimit("0, 0"));

		this.statusTextField = new TextField();
		theStatusPanel.add(this.statusTextField.setLimit("1, 0"));

		this.statusButton = new Button(IViewConstants.UPDATE_LABEL);
		this.statusButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		this.statusButton.addActionListener(Button.ACTION_CLICK, new StatusUpdateListener());
		theStatusPanel.add(this.statusButton.setLimit("2, 0"));

		super.add(theStatusPanel.setLimit("0, 1"));

		this.statusModel = new ProjectMemberStatusListModel(HomeView.this.homeController.getProjectMembers());
		this.statusGrid = new GridBoxComponent(this.statusModel);
		this.statusGrid.setColumnWidth(0, 150);
		super.add(this.statusGrid.setLimit("0, 2"));
	}

	class ProjectsGridListener implements ActionListener {

		public void actionPerformed(ActionEvent aEvt) {
			ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
			theProjectMaintenance.addObserver(HomeView.this);
			Integer theProjectId = HomeView.this.projectsGrid.getSelectedRowId();
			if (theProjectId != null) {
				HomeView.this.projectsGrid.setEnabled(false);
				new ProjectView(theProjectId, theProjectMaintenance);
				HomeView.this.projectsGrid.setEnabled(false);
			}
		}
	}

	class StatusUpdateListener implements ActionListener {

		public void actionPerformed(ActionEvent aEvt) {

			String theStatus = HomeView.this.statusTextField.getText();
			if (theStatus.trim().length() > 0) {
				HomeView.this.homeController.startUnitOfWork();
				ProjectMemberMaintenance theProjectMemberMaintenance = new ProjectMemberMaintenance();
				Map<String, Object> theProjectMemberData = HomeView.this.homeController.getProjectMemberData(theProjectMemberMaintenance);
				theProjectMemberData.put(ProjectMemberMaintenance.STATUS, HomeView.this.statusTextField.getText());
				theProjectMemberMaintenance.addObserver(HomeView.this);
				theProjectMemberMaintenance.saveProjectMember(theProjectMemberData);
				HomeView.this.statusTextField.setText("");
				HomeView.this.homeController.endUnitOfWork();
			} else {
				MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, IViewConstants.RB.getString("status.msg"));
			}
		}
	}

	class WorkProductsGridListener implements ActionListener {

		public void actionPerformed(ActionEvent aEvt) {
			ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
			Integer theWorkProductId = HomeView.this.workProductsGrid.getSelectedRowId();
			HomeView.this.homeController.startUnitOfWork();
			Integer theProjectId = HomeView.this.homeController.getProjectIdBy(theWorkProductId);
			HomeView.this.homeController.endUnitOfWork();

			if (theWorkProductId != null) {
				HomeView.this.workProductsGrid.setEnabled(false);
				String theType = (String) HomeView.this.workProductsGrid.getSelectedRow().get(TYPE);
				if (theType.equals(ProjectPlanMaintenance.PROJECT)) {
					new ProjectView(theWorkProductId, theProjectMaintenance);
				}
				if (theType.equals(ProjectPlanMaintenance.USE_CASE)) {
					HomeView.this.homeController.startUnitOfWork();
					UseCaseMaintenance theUseCaseMaintenance = new UseCaseMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
					HomeView.this.homeController.endUnitOfWork();
					theUseCaseMaintenance.addObserver(HomeView.this);
					new UseCaseView(theWorkProductId, theUseCaseMaintenance);
				}
				if (theType.equals(ProjectPlanMaintenance.ITERATION)) {
					HomeView.this.homeController.startUnitOfWork();
					IterationMaintenance theIterationMaintenance = new IterationMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
					HomeView.this.homeController.endUnitOfWork();
					theIterationMaintenance.addObserver(HomeView.this);
					new IterationView(theWorkProductId, theIterationMaintenance);
				}
				if (theType.equals(ProjectPlanMaintenance.DEFECT)) {
					HomeView.this.homeController.startUnitOfWork();
					DefectMaintenance theDefectMaintenance = new DefectMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
					HomeView.this.homeController.endUnitOfWork();
					theDefectMaintenance.addObserver(HomeView.this);
					new DefectView(theWorkProductId, theDefectMaintenance);
				}
				if (theType.equals(ProjectPlanMaintenance.CHANGE_REQUEST)) {
					HomeView.this.homeController.startUnitOfWork();
					ChangeRequestMaintenance theChangeRequestMaintenance = new ChangeRequestMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
					HomeView.this.homeController.endUnitOfWork();
					theChangeRequestMaintenance.addObserver(HomeView.this);
					new ChangeRequestView(theWorkProductId, theChangeRequestMaintenance);
				}
				if (theType.equals(ProjectPlanMaintenance.TASK)) {
					HomeView.this.homeController.startUnitOfWork();
					TaskMaintenance theTaskMaintenance = new TaskMaintenance(theProjectMaintenance.retrieveProjectBy(theProjectId));
					HomeView.this.homeController.endUnitOfWork();
					theTaskMaintenance.addObserver(HomeView.this);
					new TaskView(theWorkProductId, theTaskMaintenance);
				}
				HomeView.this.workProductsGrid.setEnabled(true);
			}
		}
	}

	public void update(Observable aObservable, Object aObject) {
		if (aObservable instanceof ProjectMaintenance) {
			this.projectsModel.setData(this.homeController.getProjects());
		} else if (aObservable instanceof ProjectMemberMaintenance) {
			this.statusModel.setData(HomeView.this.homeController.getProjectMembers());
		} else {
			this.workProductsModel.setData(this.homeController.getWorkProducts());
		}
	}
}
