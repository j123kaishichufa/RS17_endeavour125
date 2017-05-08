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

import org.endeavour.mgmt.controller.ChangeRequestMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ChangeRequestListModel;
import org.endeavour.mgmt.view.model.PriorityListModel;
import org.endeavour.mgmt.view.model.StatusListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ChangeRequestsListView extends PanelComponent implements ActionListener, Observer {

	private ChangeRequestMaintenance changeRequestMaintenance = null;
	private GridBoxComponent changeRequestsGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private ChangeRequestListModel changeRequestModel = null;
	private TextField nameTextField = null;
	private TextField numberTextField = null;
	private DropDownGridBoxComponent statusDropDown = null;
	private DropDownGridBoxComponent priorityDropDown = null;
	private Button okButton = null;
	private PanelComponent fieldsPanel = null;
	private StatusListModel statusModel = null;
	private PriorityListModel priorityModel = null;

	public ChangeRequestsListView() {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 30, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.CHANGE_REQUESTS_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("change_requests.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		PanelComponent theParametersPanel = new PanelComponent();
		theParametersPanel.setLayout(new TableLayout(new double[][] { { 820, 0 }, { 50 } }, 0, 5));

		this.fieldsPanel = new PanelComponent();
		this.fieldsPanel.setLayout(new TableLayout(new double[][] { { 45, 150, 40, 150, 45, 250, 55, 50 }, { 20 } }, 0, 5));

		Label thePriorityLabel = new Label(IViewConstants.RB.getString("priority.lbl") + ":");
		this.fieldsPanel.add(thePriorityLabel.setLimit("0, 0"));

		this.priorityModel = new PriorityListModel(true);
		this.priorityDropDown = new DropDownGridBoxComponent(this.priorityModel, 0);
		this.priorityDropDown.setSelectedRowObject(3);
		this.fieldsPanel.add(this.priorityDropDown.setLimit("1, 0"));

		Label theStatusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
		this.fieldsPanel.add(theStatusLabel.setLimit("2, 0"));

		this.statusModel = new StatusListModel(true, true);
		this.statusDropDown = new DropDownGridBoxComponent(this.statusModel, 0);
		this.statusDropDown.setSelectedRowObject(6);
		this.fieldsPanel.add(this.statusDropDown.setLimit("3, 0"));

		Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		this.fieldsPanel.add(theNameLabel.setLimit("4, 0"));

		this.nameTextField = new TextField();
		this.fieldsPanel.add(nameTextField.setLimit("5, 0"));

		Label theNumberLabel = new Label(IViewConstants.RB.getString("change_request_number.lbl"));
		this.fieldsPanel.add(theNumberLabel.setLimit("6, 0"));

		this.numberTextField = new TextField();
		this.numberTextField.setEditMask("#########");
		this.fieldsPanel.add(this.numberTextField.setLimit("7, 0"));

		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();

		PanelComponent theButtonPanel = new PanelComponent();
		theButtonPanel.setLayout(new TableLayout(new double[][] { { 100, 0 }, { 25 } }, 0, 5));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
		this.okButton.setEnabled(theProjectId != null);
		theButtonPanel.add(okButton.setLimit("0, 0"));

		theParametersPanel.add(this.fieldsPanel.setLimit("0, 0"));
		theParametersPanel.add(theButtonPanel.setLimit("1, 0"));

		super.add(theParametersPanel.setLimit("0, 1"));

		this.initializeChangeRequestsGrid();

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

		super.add(theButtonsPanel.setLimit("0, 3"));
	}

	private void setButtonsStatus() {
		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		this.editButton.setEnabled(isEnabled && !this.changeRequestsGrid.getRows().isEmpty());

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_DELETE);
		this.deleteButton.setEnabled(isEnabled && !this.changeRequestsGrid.getRows().isEmpty() && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.newButton.setEnabled(isEnabled && hasPrivilege);
	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.changeRequestMaintenance = new ChangeRequestMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.changeRequestMaintenance.addObserver(this);
	}

	private void initializeChangeRequestsGrid() {
		this.changeRequestModel = new ChangeRequestListModel();
		this.changeRequestsGrid = new GridBoxComponent(this.changeRequestModel);
		this.changeRequestsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.changeRequestsGrid.setColumnWidth(0, 50);
		this.changeRequestsGrid.setColumnWidth(1, 170);
		super.add(this.changeRequestsGrid.setLimit("0, 2"));
	}

	private void viewChangeRequests() {
		String thePriority = this.priorityModel.getValueByDescription(this.priorityDropDown.getText());
		String theStatus = this.statusModel.getValueByDescription(this.statusDropDown.getText());
		String theName = this.nameTextField.getText();
		String theNumber = this.numberTextField.getText();

		this.changeRequestModel.setData(this.changeRequestMaintenance.getChangeRequestsBy(thePriority, theStatus, theName, theNumber));
		Integer theId = this.changeRequestMaintenance.getSelectedWorkProductId();
		if (theId != null) {
			this.changeRequestsGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewChangeRequest();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new ChangeRequestView(this.changeRequestMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewChangeRequest();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteChangeRequest();
			}
			if (theButton.getText().equals(IViewConstants.OK_BUTTON_LABEL)) {
				this.changeRequestMaintenance.startUnitOfWork();
				this.changeRequestMaintenance.reset();
				this.viewChangeRequests();
				this.changeRequestMaintenance.endUnitOfWork();
			}
		}
	}

	private void viewChangeRequest() {
		Integer theChangeRequestId = this.changeRequestsGrid.getSelectedRowId();
		if (theChangeRequestId != null) {
			this.changeRequestsGrid.setEnabled(false);
			new ChangeRequestView(theChangeRequestId, this.changeRequestMaintenance);
			this.changeRequestsGrid.setEnabled(true);
		}
	}

	private void deleteChangeRequest() {
		this.changeRequestMaintenance.startUnitOfWork();
		Integer theChangeRequestId = this.changeRequestsGrid.getSelectedRowId();
		if (theChangeRequestId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.changeRequestMaintenance.reset();
				this.changeRequestMaintenance.deleteChangeRequest(theChangeRequestId);
				this.viewChangeRequests();
			}
		}
		this.changeRequestMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewChangeRequests();
	}
}
