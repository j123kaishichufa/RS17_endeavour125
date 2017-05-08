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

import org.endeavour.mgmt.controller.ActorMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class ActorView extends DialogComponent implements ActionListener {

	private Label nameLabel = null;
	private Label descriptionLabel = null;
	private TextField nameTextField = null;
	private TextArea descriptionTextArea = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private ActorMaintenance actorMaintenance = null;

	public ActorView(ActorMaintenance aActorMaintenance) {

		super.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 0, 25 } }, 5, 5));

		this.initializeControllers(aActorMaintenance);

		super.setTitle(IViewConstants.RB.getString("actor.lbl"));
		super.setSize(365, 205);
		super.centerDialog();

		this.nameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		super.add(this.nameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		super.add(this.nameTextField.setLimit("1, 0"));

		this.descriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		super.add(this.descriptionLabel.setLimit("0, 1"));

		this.descriptionTextArea = new TextArea();
		super.add(this.descriptionTextArea.setLimit("1, 1"));

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

		super.add(theButtonsPanel.setLimit("1, 2"));

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public ActorView(Integer aActorId, ActorMaintenance aActorMaintenance) {
		this(aActorMaintenance);
		this.viewActor(aActorId);
	}

	private void viewActor(Integer aActorId) {
		Map<String, Object> theActorData = this.actorMaintenance.getActorDataBy(aActorId);

		String theName = (String) theActorData.get(ActorMaintenance.NAME);
		String theDescription = (String) theActorData.get(ActorMaintenance.DESCRIPTION);

		this.nameTextField.setText(theName);
		this.descriptionTextArea.setText(theDescription);

		super.setTitle("Actor - " + theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveActor();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void initializeControllers(ActorMaintenance aActorMaintenance) {
		this.actorMaintenance = aActorMaintenance;
		this.actorMaintenance.startUnitOfWork();
		this.actorMaintenance.reset();
	}

	private void saveActor() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(ActorMaintenance.NAME, this.nameTextField.getText());
		theData.put(ActorMaintenance.DESCRIPTION, this.descriptionTextArea.getText());

		List<String> theErrors = this.actorMaintenance.saveActor(theData);
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
			this.actorMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
