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

import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.SecurityGroupMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.CheckBoxComponent;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.CheckBox;
import thinwire.ui.Component;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

@SuppressWarnings("unchecked")
public class SecurityGroupView extends DialogComponent implements ActionListener {

	private Button saveButton = null;
	private Button cancelButton = null;
	private CheckBoxComponent planningViewCB = null;
	private CheckBoxComponent planningEditCB = null;
	private CheckBoxComponent planningDeleteCB = null;
	private CheckBoxComponent requirementViewCB = null;
	private CheckBoxComponent requirementEditCB = null;
	private CheckBoxComponent requirementDeleteCB = null;
	private CheckBoxComponent defectViewCB = null;
	private CheckBoxComponent defectEditCB = null;
	private CheckBoxComponent defectDeleteCB = null;
	private CheckBoxComponent testViewCB = null;
	private CheckBoxComponent testEditCB = null;
	private CheckBoxComponent testDeleteCB = null;
	private CheckBoxComponent documentsViewCB = null;
	private CheckBoxComponent documentsEditCB = null;
	private CheckBoxComponent documentsDeleteCB = null;
	private CheckBoxComponent reportViewCB = null;
	private CheckBoxComponent securityViewCB = null;
	private CheckBoxComponent securityEditCB = null;
	private CheckBoxComponent securityDeleteCB = null;

	private PanelComponent privilegesPanel = null;
	private SecurityGroupMaintenance securityGroupMaintenance = null;
	private TextField nameTextField = null;

	public SecurityGroupView(SecurityGroupMaintenance aSecurityGroup) {
		this.securityGroupMaintenance = aSecurityGroup;
		this.securityGroupMaintenance.startUnitOfWork();
		this.securityGroupMaintenance.reset();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 25, 25, 0, 25 } }, 5, 10));

		super.setTitle(IViewConstants.RB.getString("security_group.lbl"));
		super.setSize(480, 350);
		super.centerDialog();

		PanelComponent theNamePanel = new PanelComponent();
		theNamePanel.setLayout(new TableLayout(new double[][] { { 45, 0 }, { 20, 20 } }, 5, 5));

		Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		theNamePanel.add(theNameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		theNamePanel.add(this.nameTextField.setLimit("1, 0"));

		super.add(theNamePanel.setLimit("0, 0"));

		PanelComponent theWarningPanel = new PanelComponent();
		theWarningPanel.setLayout(new TableLayout(new double[][] { { 0 }, { 20 } }, 5, 5));

		Label theWarningLabel = new Label(IViewConstants.RB.getString("security_groups.msg"));
		theWarningLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theWarningPanel.add(theWarningLabel.setLimit("0, 0"));

		super.add(theWarningPanel.setLimit("0, 1"));

		this.privilegesPanel = new PanelComponent();
		this.privilegesPanel.setLayout(new TableLayout(new double[][] { { 180, 75, 75, 75 }, { 20, 20, 20, 20, 20, 20, 20, 25 } }, 5, 5));

		Label theViewLabel = new Label(IViewConstants.RB.getString("view.lbl"));
		this.privilegesPanel.add(theViewLabel.setLimit("1, 0"));

		Label theEditLabel = new Label(IViewConstants.RB.getString("save.lbl"));
		this.privilegesPanel.add(theEditLabel.setLimit("2, 0"));

		Label theDeleteLabel = new Label(IViewConstants.RB.getString("delete.lbl"));
		this.privilegesPanel.add(theDeleteLabel.setLimit("3, 0"));

		// PLANNING
		Label thePlanningLabel = new Label(IViewConstants.RB.getString("planning.lbl"));
		this.privilegesPanel.add(thePlanningLabel.setLimit("0, 1"));

		this.planningViewCB = new CheckBoxComponent(IPrivileges.PLANNING_VIEW);
		this.privilegesPanel.add(this.planningViewCB.setLimit("1, 1"));
		this.planningViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!planningViewCB.isChecked()) {
					planningEditCB.setChecked(false);
					planningDeleteCB.setChecked(false);
				}
			}
		});

		this.planningEditCB = new CheckBoxComponent(IPrivileges.PLANNING_EDIT);
		this.privilegesPanel.add(this.planningEditCB.setLimit("2, 1"));
		this.planningEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (planningEditCB.isChecked()) {
					planningViewCB.setChecked(true);
				}
			}
		});

		this.planningDeleteCB = new CheckBoxComponent(IPrivileges.PLANNING_DELETE);
		this.privilegesPanel.add(this.planningDeleteCB.setLimit("3, 1"));
		this.planningDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (planningDeleteCB.isChecked()) {
					planningViewCB.setChecked(true);
				}
			}
		});

		// REQUIREMENTS
		Label theRequirementsLabel = new Label(IViewConstants.RB.getString("requirements.lbl"));
		this.privilegesPanel.add(theRequirementsLabel.setLimit("0, 2"));

		this.requirementViewCB = new CheckBoxComponent(IPrivileges.REQUIREMENTS_VIEW);
		this.privilegesPanel.add(this.requirementViewCB.setLimit("1, 2"));
		this.requirementViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!requirementViewCB.isChecked()) {
					requirementEditCB.setChecked(false);
					requirementDeleteCB.setChecked(false);
				}
			}
		});

		this.requirementEditCB = new CheckBoxComponent(IPrivileges.REQUIREMENTS_EDIT);
		this.privilegesPanel.add(this.requirementEditCB.setLimit("2, 2"));
		this.requirementEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (requirementEditCB.isChecked()) {
					requirementViewCB.setChecked(true);
				}
			}
		});

		this.requirementDeleteCB = new CheckBoxComponent(IPrivileges.REQUIREMENTS_DELETE);
		this.privilegesPanel.add(this.requirementDeleteCB.setLimit("3, 2"));
		this.requirementDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (requirementDeleteCB.isChecked()) {
					requirementViewCB.setChecked(true);
				}
			}
		});

		// DEFECTS
		Label theDefectsLabel = new Label(IViewConstants.RB.getString("defect_tracking.lbl"));
		this.privilegesPanel.add(theDefectsLabel.setLimit("0, 3"));

		this.defectViewCB = new CheckBoxComponent(IPrivileges.DEFECT_TRACKING_VIEW);
		this.privilegesPanel.add(this.defectViewCB.setLimit("1, 3"));
		this.defectViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!defectViewCB.isChecked()) {
					defectEditCB.setChecked(false);
					defectDeleteCB.setChecked(false);
				}
			}
		});

		this.defectEditCB = new CheckBoxComponent(IPrivileges.DEFECT_TRACKING_EDIT);
		this.privilegesPanel.add(this.defectEditCB.setLimit("2, 3"));
		this.defectEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (defectEditCB.isChecked()) {
					defectViewCB.setChecked(true);
				}
			}
		});

		this.defectDeleteCB = new CheckBoxComponent(IPrivileges.DEFECT_TRACKING_DELETE);
		this.privilegesPanel.add(this.defectDeleteCB.setLimit("3, 3"));
		this.defectDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (defectDeleteCB.isChecked()) {
					defectViewCB.setChecked(true);
				}
			}
		});

		// TEST
		Label theTestLabel = new Label(IViewConstants.RB.getString("test_management.lbl"));
		this.privilegesPanel.add(theTestLabel.setLimit("0, 4"));

		this.testViewCB = new CheckBoxComponent(IPrivileges.TEST_MANAGEMENT_VIEW);
		this.privilegesPanel.add(this.testViewCB.setLimit("1, 4"));
		this.testViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!testViewCB.isChecked()) {
					testEditCB.setChecked(false);
					testDeleteCB.setChecked(false);
				}
			}
		});

		this.testEditCB = new CheckBoxComponent(IPrivileges.TEST_MANAGEMENT_EDIT);
		this.privilegesPanel.add(this.testEditCB.setLimit("2, 4"));
		this.testEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (testEditCB.isChecked()) {
					testViewCB.setChecked(true);
				}
			}
		});

		this.testDeleteCB = new CheckBoxComponent(IPrivileges.TEST_MANAGEMENT_DELETE);
		this.privilegesPanel.add(this.testDeleteCB.setLimit("3, 4"));
		this.testDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (testDeleteCB.isChecked()) {
					testViewCB.setChecked(true);
				}
			}
		});

		// DOCUMENTS
		Label theDocumentsLabel = new Label(IViewConstants.RB.getString("document_management.lbl"));
		this.privilegesPanel.add(theDocumentsLabel.setLimit("0, 5"));

		this.documentsViewCB = new CheckBoxComponent(IPrivileges.DOCUMENT_MANAGEMENT_VIEW);
		this.privilegesPanel.add(this.documentsViewCB.setLimit("1, 5"));
		this.documentsViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!documentsViewCB.isChecked()) {
					documentsEditCB.setChecked(false);
					documentsDeleteCB.setChecked(false);
				}
			}
		});

		this.documentsEditCB = new CheckBoxComponent(IPrivileges.DOCUMENT_MANAGEMENT_EDIT);
		this.privilegesPanel.add(this.documentsEditCB.setLimit("2, 5"));
		this.documentsEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (documentsEditCB.isChecked()) {
					documentsViewCB.setChecked(true);
				}
			}
		});

		this.documentsDeleteCB = new CheckBoxComponent(IPrivileges.DOCUMENT_MANAGEMENT_DELETE);
		this.privilegesPanel.add(this.documentsDeleteCB.setLimit("3, 5"));
		this.documentsDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (documentsDeleteCB.isChecked()) {
					documentsViewCB.setChecked(true);
				}
			}
		});

		// REPORTS
		Label theReportsLabel = new Label(IViewConstants.RB.getString("reports.lbl"));
		this.privilegesPanel.add(theReportsLabel.setLimit("0, 6"));

		this.reportViewCB = new CheckBoxComponent(IPrivileges.REPORTS_VIEW);
		this.privilegesPanel.add(this.reportViewCB.setLimit("1, 6"));

		// SECURITY
		Label theSecurityLabel = new Label(IViewConstants.RB.getString("security_groups.lbl"));
		this.privilegesPanel.add(theSecurityLabel.setLimit("0, 7"));

		this.securityViewCB = new CheckBoxComponent(IPrivileges.SECURITY_VIEW);
		this.privilegesPanel.add(this.securityViewCB.setLimit("1, 7"));
		this.securityViewCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!securityViewCB.isChecked()) {
					securityEditCB.setChecked(false);
					securityDeleteCB.setChecked(false);
				}
			}
		});

		this.securityEditCB = new CheckBoxComponent(IPrivileges.SECURITY_EDIT);
		this.privilegesPanel.add(this.securityEditCB.setLimit("2, 7"));
		this.securityEditCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (securityEditCB.isChecked()) {
					securityViewCB.setChecked(true);
				}
			}
		});

		this.securityDeleteCB = new CheckBoxComponent(IPrivileges.SECURITY_DELETE);
		this.privilegesPanel.add(this.securityDeleteCB.setLimit("3, 7"));
		this.securityDeleteCB.addActionListener(CheckBox.ACTION_CLICK, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (securityDeleteCB.isChecked()) {
					securityViewCB.setChecked(true);
				}
			}
		});

		super.add(this.privilegesPanel.setLimit("0, 2"));

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		CheckBoxComponent theSelectAllCB = new CheckBoxComponent();
		theSelectAllCB.setText(IViewConstants.RB.getString("select_all.lbl"));
		theSelectAllCB.addActionListener(CheckBoxComponent.ACTION_CLICK, this);
		theButtonsPanel.add(theSelectAllCB.setLimit("0, 0"));

		this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
		this.saveButton.addActionListener(Button.ACTION_CLICK, this);
		this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		theButtonsPanel.add(this.saveButton.setLimit("1, 0"));

		this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
		this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));

		super.add(theButtonsPanel.setLimit("0, 3"));

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public SecurityGroupView(Integer aGroupId, SecurityGroupMaintenance aProjectMemberMaintenance) {
		this(aProjectMemberMaintenance);
		this.viewSecurityGroup(aGroupId);
	}

	private void viewSecurityGroup(Integer aGroupId) {

		CheckBoxComponent theCheckBox = null;
		Map<String, Object> theData = this.securityGroupMaintenance.getSecurityGroupDataBy(aGroupId);

		List<String> thePrivilegesList = (List) theData.get(SecurityGroupMaintenance.PRIVILEGES);
		for (String thePrivilege : thePrivilegesList) {
			List<Component> theComponents = this.privilegesPanel.getChildren();
			for (Component theComponent : theComponents) {
				if (theComponent instanceof CheckBoxComponent) {
					theCheckBox = (CheckBoxComponent) theComponent;
					if (theCheckBox.getValue() != null && !theCheckBox.isChecked()) {
						theCheckBox.setChecked(theCheckBox.getValue().equals(thePrivilege));
					}
				}
			}
		}

		String theName = (String) theData.get(SecurityGroupMaintenance.NAME);
		super.setTitle(IViewConstants.RB.getString("security_group.lbl") + " - " + theName);
		this.nameTextField.setText(theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
				int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
				if (theResult == IViewConstants.YES) {
					this.saveSecurityGroup();
				}
			}
			if (theButton.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
				this.setVisible(false);
			}
		}
		if (theSource instanceof CheckBoxComponent) {
			CheckBoxComponent theCheckBox = (CheckBoxComponent) theSource;
			boolean isChecked = theCheckBox.isChecked();
			for (Component theComponent : this.privilegesPanel.getChildren()) {
				if (theComponent instanceof CheckBoxComponent) {
					theCheckBox = (CheckBoxComponent) theComponent;
					if (theCheckBox.getValue() != null) {
						theCheckBox.setChecked(isChecked);
					}
				}
			}
		}
	}

	private void saveSecurityGroup() {

		Map<String, Object> theData = new HashMap<String, Object>();

		theData.put(SecurityGroupMaintenance.NAME, this.nameTextField.getText());

		List<String> thePrivileges = new ArrayList<String>();
		CheckBoxComponent theCheckBox = null;
		for (Component theComponent : this.privilegesPanel.getChildren()) {
			if (theComponent instanceof CheckBoxComponent) {
				theCheckBox = (CheckBoxComponent) theComponent;
				if (theCheckBox.getValue() != null && theCheckBox.isChecked()) {
					thePrivileges.add(theCheckBox.getValue());
				}
			}
		}

		theData.put(SecurityGroupMaintenance.PRIVILEGES, thePrivileges);

		List<String> theErrors = this.securityGroupMaintenance.saveSecurityGroup(theData);
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
			this.securityGroupMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.SECURITY_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
