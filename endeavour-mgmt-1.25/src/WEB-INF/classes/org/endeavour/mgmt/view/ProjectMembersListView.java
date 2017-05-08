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

import org.endeavour.mgmt.controller.ProjectMemberMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ProjectMembersListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ProjectMembersListView extends PanelComponent implements ActionListener, Observer {

	private ProjectMemberMaintenance projectMemberMaintenance = null;
	private GridBoxComponent projectMembersGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private ProjectMembersListModel projectMembersModel = null;

	public ProjectMembersListView() {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.USERS_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("users.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeProjectMembersGrid();

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
		boolean hasProjects = !this.projectMembersGrid.getRows().isEmpty();
		this.editButton.setEnabled(hasProjects);

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.isLoggedUserAdmin();
		this.deleteButton.setEnabled(hasProjects && hasPrivilege);
		this.newButton.setEnabled(hasPrivilege);

		boolean isAutodeleteAttempt = theSecurityMaintenance.isUserEqualToLoggedUser(this.projectMembersGrid.getSelectedRowId());
		this.deleteButton.setEnabled(!isAutodeleteAttempt);
	}

	private void initializeControllers() {
		this.projectMemberMaintenance = new ProjectMemberMaintenance();
		this.projectMemberMaintenance.addObserver(this);
	}

	private void initializeProjectMembersGrid() {
		this.projectMembersModel = new ProjectMembersListModel(this.projectMemberMaintenance.getProjectMembersByPrivilege(), false);
		this.projectMembersGrid = new GridBoxComponent(this.projectMembersModel);
		this.projectMembersGrid.setColumnWidth(0, 250);
		this.projectMembersGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.projectMembersGrid.addActionListener(GridBox.ACTION_CLICK, this);
		super.add(this.projectMembersGrid.setLimit("0, 1"));
	}

	private void viewProjectMembers() {
		this.projectMembersModel.setData(this.projectMemberMaintenance.getProjectMembersByPrivilege());
		Integer theId = this.projectMemberMaintenance.getSelectedProjectMemberid();
		if (theId != null) {
			this.projectMembersGrid.setSelectedRowById(theId);
		}
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			if (aEvt.getAction().equals(GridBox.ACTION_DOUBLE_CLICK)) {
				this.viewProjectMember();
			} else {
				this.setButtonsStatus();
			}
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new ProjectMemberView(this.projectMemberMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewProjectMember();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteProjectMember();
			}
		}
	}

	private void viewProjectMember() {
		Integer theGroupId = this.projectMembersGrid.getSelectedRowId();
		if (theGroupId != null) {
			this.projectMembersGrid.setEnabled(false);
			new ProjectMemberView(theGroupId, this.projectMemberMaintenance);
			this.projectMembersGrid.setEnabled(true);
		}
	}

	private void deleteProjectMember() {
		this.projectMemberMaintenance.startUnitOfWork();
		Integer theGroupId = this.projectMembersGrid.getSelectedRowId();
		if (theGroupId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.projectMemberMaintenance.reset();
				this.projectMemberMaintenance.deleteProjectMember(theGroupId);
				this.viewProjectMembers();
			}
		}
		this.projectMemberMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewProjectMembers();
	}
}
