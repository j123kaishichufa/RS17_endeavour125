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
import org.endeavour.mgmt.controller.SecurityGroupMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.SecurityGroupsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class SecurityGroupsListView extends PanelComponent implements ActionListener, Observer {

	private SecurityGroupMaintenance securityGroupMaintenance = null;
	private GridBoxComponent securityGroupsGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private SecurityGroupsListModel securityGroupsModel = null;

	public SecurityGroupsListView() {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.SECURITY_GROUPS_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("security_groups.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeSecurityGroupsGrid();

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
		boolean hasSecurityGroups = !this.securityGroupsGrid.getRows().isEmpty();
		this.editButton.setEnabled(hasSecurityGroups);

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.SECURITY_DELETE);
		this.deleteButton.setEnabled(hasSecurityGroups && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.SECURITY_EDIT);
		this.newButton.setEnabled(hasPrivilege);
	}

	private void initializeControllers() {
		this.securityGroupMaintenance = new SecurityGroupMaintenance();
		this.securityGroupMaintenance.addObserver(this);
	}

	private void initializeSecurityGroupsGrid() {
		this.securityGroupsModel = new SecurityGroupsListModel(this.securityGroupMaintenance.getSecurityGroups());
		this.securityGroupsGrid = new GridBoxComponent(this.securityGroupsModel);
		this.securityGroupsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.securityGroupsGrid.setLimit("0, 1"));
	}

	private void viewSecurityGroups() {
		this.securityGroupsModel.setData(this.securityGroupMaintenance.getSecurityGroups());
		Integer theId = this.securityGroupMaintenance.getSelectedSecurityGroupId();
		if (theId != null) {
			this.securityGroupsGrid.setSelectedRowById(theId);
		}
		setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewSecurityGroup();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new SecurityGroupView(this.securityGroupMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewSecurityGroup();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteSecurityGroup();
			}
		}
	}

	private void viewSecurityGroup() {
		Integer theSecurityGroupId = this.securityGroupsGrid.getSelectedRowId();
		if (theSecurityGroupId != null) {
			this.securityGroupsGrid.setEnabled(false);
			new SecurityGroupView(theSecurityGroupId, this.securityGroupMaintenance);
			this.securityGroupsGrid.setEnabled(true);
		}
	}

	private void deleteSecurityGroup() {
		this.securityGroupMaintenance.startUnitOfWork();
		Integer theSecurityGroupId = this.securityGroupsGrid.getSelectedRowId();
		if (theSecurityGroupId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.securityGroupMaintenance.reset();
				this.securityGroupMaintenance.deleteSecurityGroup(theSecurityGroupId);
				this.viewSecurityGroups();
			}
		}
		this.securityGroupMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewSecurityGroups();
	}
}
