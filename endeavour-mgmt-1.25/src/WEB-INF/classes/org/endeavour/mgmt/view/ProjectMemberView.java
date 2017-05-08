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

import org.endeavour.mgmt.controller.ProjectMemberMaintenance;
import org.endeavour.mgmt.controller.SecurityGroupMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.RolesListModel;
import org.endeavour.mgmt.view.model.SecurityGroupsListModel;

import thinwire.ui.Button;
import thinwire.ui.CheckBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class ProjectMemberView extends DialogComponent implements ActionListener {

	private Label userId = null;
	private TextField userIdTextField = null;
	private Label password1 = null;
	private Label password2 = null;
	private TextField passwordTextField1 = null;
	private TextField passwordTextField2 = null;
	private Label firstNameLabel = null;
	private TextField firstNameTextField = null;
	private Label lastNameLabel = null;
	private TextField lastNameTextField = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private ProjectMemberMaintenance projectMemberMaintenance = null;
	private DropDownGridBoxComponent rolesDropDown = null;
	private Label roleLabel = null;
	private DropDownGridBoxComponent securityGroupsDropDown = null;
	private Label securityGroupLabel = null;
	private Label emailLabel = null;
	private TextField emailTextField = null;
	private Label acceptNotificationsLabel = null;
	private CheckBox acceptNotificationsCheckBox = null;
	private RolesListModel rolesListModel = null;

	public ProjectMemberView(ProjectMemberMaintenance aProjectMemberMaintenance) {
		this.projectMemberMaintenance = aProjectMemberMaintenance;
		this.projectMemberMaintenance.startUnitOfWork();
		this.projectMemberMaintenance.reset();

		super.setLayout(new TableLayout(new double[][] { { 108, 0 }, { 20, 20, 20, 20, 20, 20, 20, 20, 20, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("project_member.lbl"));
		super.setSize(365, 280);
		super.centerDialog();

		this.firstNameLabel = new Label(IViewConstants.RB.getString("first_name.lbl"));
		super.add(this.firstNameLabel.setLimit("0, 0"));

		this.firstNameTextField = new TextField();
		super.add(this.firstNameTextField.setLimit("1, 0"));

		this.lastNameLabel = new Label(IViewConstants.RB.getString("last_name.lbl"));
		super.add(this.lastNameLabel.setLimit("0, 1"));

		this.lastNameTextField = new TextField();
		super.add(this.lastNameTextField.setLimit("1, 1"));

		this.userId = new Label(IViewConstants.RB.getString("user_id.lbl") + ":");
		super.add(this.userId.setLimit("0, 2"));

		this.userIdTextField = new TextField();
		super.add(this.userIdTextField.setLimit("1, 2"));

		this.password1 = new Label(IViewConstants.RB.getString("password.lbl"));
		super.add(this.password1.setLimit("0, 3"));

		this.passwordTextField1 = new TextField();
		this.passwordTextField1.setInputHidden(true);
		super.add(this.passwordTextField1.setLimit("1, 3"));

		this.password2 = new Label(IViewConstants.RB.getString("repassword.lbl"));
		super.add(this.password2.setLimit("0, 4"));

		this.passwordTextField2 = new TextField();
		this.passwordTextField2.setInputHidden(true);
		super.add(this.passwordTextField2.setLimit("1, 4"));

		this.roleLabel = new Label(IViewConstants.RB.getString("role.lbl") + ":");
		super.add(this.roleLabel.setLimit("0, 5"));

		this.rolesListModel = new RolesListModel();
		this.rolesDropDown = new DropDownGridBoxComponent(this.rolesListModel, 0);
		this.rolesDropDown.selectFirstElement();
		super.add(this.rolesDropDown.setLimit("1, 5"));

		this.securityGroupLabel = new Label(IViewConstants.RB.getString("security_group.lbl") + ":");
		super.add(this.securityGroupLabel.setLimit("0, 6"));

		SecurityGroupsListModel theSecurityGroupsListModel = new SecurityGroupsListModel();
		SecurityGroupMaintenance theSecurityGroupMaintenance = new SecurityGroupMaintenance();
		theSecurityGroupsListModel.setData(theSecurityGroupMaintenance.getSecurityGroups());
		this.securityGroupsDropDown = new DropDownGridBoxComponent(theSecurityGroupsListModel, 0);
		this.securityGroupsDropDown.selectFirstElement();
		super.add(this.securityGroupsDropDown.setLimit("1, 6"));

		this.emailLabel = new Label(IViewConstants.RB.getString("email.lbl"));
		super.add(this.emailLabel.setLimit("0, 7"));

		this.emailTextField = new TextField();
		super.add(this.emailTextField.setLimit("1, 7"));

		this.acceptNotificationsLabel = new Label(IViewConstants.RB.getString("email_assignments.lbl"));
		super.add(this.acceptNotificationsLabel.setLimit("0, 8"));

		this.acceptNotificationsCheckBox = new CheckBox();
		super.add(this.acceptNotificationsCheckBox.setLimit("1, 8"));

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

		super.add(theButtonsPanel.setLimit("1, 9"));

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public ProjectMemberView(Integer aProjectMemberId, ProjectMemberMaintenance aProjectMemberMaintenance) {
		this(aProjectMemberMaintenance);
		this.viewProjectMember(aProjectMemberId);
	}

	private void viewProjectMember(Integer aProjectMemberId) {
		Map<String, Object> theData = this.projectMemberMaintenance.getProjectMemberDataBy(aProjectMemberId);
		String theUserId = (String) theData.get(ProjectMemberMaintenance.USER_ID);
		String thePassword = (String) theData.get(ProjectMemberMaintenance.PASSWORD);
		String theFirstName = (String) theData.get(ProjectMemberMaintenance.FIRST_NAME);
		String theLastName = (String) theData.get(ProjectMemberMaintenance.LAST_NAME);
		String theFullName = (String) theData.get(ProjectMemberMaintenance.FULL_NAME);
		String theRole = (String) theData.get(ProjectMemberMaintenance.ROLE);
		Integer theSecurityGroup = (Integer) theData.get(ProjectMemberMaintenance.SECURITY_GROUP);
		String theEmail = (String) theData.get(ProjectMemberMaintenance.EMAIL);
		Boolean isAcceptNotifications = (Boolean) theData.get(ProjectMemberMaintenance.ACCEPT_NOTIFICATIONS);

		this.userIdTextField.setText(theUserId);
		this.passwordTextField1.setText(thePassword);
		this.passwordTextField2.setText(thePassword);
		this.firstNameTextField.setText(theFirstName);
		this.lastNameTextField.setText(theLastName);
		this.rolesDropDown.setText(this.rolesListModel.getDescriptionByValue(theRole));
		this.emailTextField.setText(theEmail);
		this.acceptNotificationsCheckBox.setChecked(isAcceptNotifications);
		if (theSecurityGroup != null) {
			this.securityGroupsDropDown.setSelectedRowObject(theSecurityGroup);
		}

		super.setTitle(IViewConstants.RB.getString("project_member.lbl") + " - " + theFullName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveProjectMember();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void saveProjectMember() {

		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(ProjectMemberMaintenance.USER_ID, this.userIdTextField.getText());
		theData.put(ProjectMemberMaintenance.PASSWORD, this.passwordTextField1.getText());
		theData.put(ProjectMemberMaintenance.PASSWORD2, this.passwordTextField2.getText());
		theData.put(ProjectMemberMaintenance.FIRST_NAME, this.firstNameTextField.getText());
		theData.put(ProjectMemberMaintenance.LAST_NAME, this.lastNameTextField.getText());
		theData.put(ProjectMemberMaintenance.ROLE, this.rolesListModel.getValueByDescription(this.rolesDropDown.getText()));
		theData.put(ProjectMemberMaintenance.SECURITY_GROUP, this.securityGroupsDropDown.getSelectedRowId());
		theData.put(ProjectMemberMaintenance.EMAIL, this.emailTextField.getText());
		theData.put(ProjectMemberMaintenance.ACCEPT_NOTIFICATIONS, this.acceptNotificationsCheckBox.isChecked());

		List<String> theErrors = this.projectMemberMaintenance.saveProjectMember(theData);
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
			this.projectMemberMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.getLoggedUser().isAdministrator();
		this.securityGroupsDropDown.setEnabled(hasPrivilege);
	}
}
