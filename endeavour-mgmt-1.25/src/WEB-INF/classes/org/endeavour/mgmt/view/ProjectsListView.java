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
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ProjectsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ProjectsListView extends PanelComponent implements ActionListener, Observer {

	private ProjectMaintenance projectMaintenance = null;
	private GridBoxComponent projectsGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private ProjectsListModel projectsModel = null;

	public ProjectsListView() {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.PROJECTS_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("projects.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeProjectsGrid();

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

		super.add(theButtonsPanel.setLimit("0, 2"));
	}

	private void setButtonsStatus() {
		boolean hasProjects = !this.projectsGrid.getRows().isEmpty();
		this.editButton.setEnabled(hasProjects);

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_DELETE);
		this.deleteButton.setEnabled(hasProjects && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_EDIT);
		this.newButton.setEnabled(hasPrivilege);
	}

	private void initializeControllers() {
		this.projectMaintenance = new ProjectMaintenance();
		this.projectMaintenance.addObserver(this);
	}

	private void initializeProjectsGrid() {
		this.projectsModel = (ProjectsListModel) MainView.getProjectDropDown().getModel();
		this.projectsGrid = new GridBoxComponent(this.projectsModel);
		this.projectsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.projectsGrid.setLimit("0, 1"));		
		
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		this.projectsGrid.setSelectedRowById(theProjectId);
	}

	private void viewProjects() {
		this.projectsModel.setData(this.projectMaintenance.getProjects());
		Integer theProjectId = this.projectMaintenance.getSelectedProjectId();
		if (theProjectId != null) {
			MainView.getProjectDropDown().setSelectedRowObject(theProjectId);
			this.projectsGrid.setSelectedRowById(theProjectId);
		} else {
			MainView.getProjectDropDown().selectFirstElement();
		}
		setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewProject();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new ProjectView(this.projectMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewProject();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteProject();
			}
		}
	}

	private void viewProject() {
		Integer theProjectId = this.projectsGrid.getSelectedRowId();
		if (theProjectId != null) {
			this.projectsGrid.setEnabled(false);
			new ProjectView(theProjectId, this.projectMaintenance);
			this.projectsGrid.setEnabled(true);
		}
	}

	private void deleteProject() {
		this.projectMaintenance.startUnitOfWork();
		Integer theProjectId = this.projectsGrid.getSelectedRowId();
		if (theProjectId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.projectMaintenance.deleteProject(theProjectId);
				this.viewProjects();
			}
		}
		this.projectMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewProjects();
	}
}
