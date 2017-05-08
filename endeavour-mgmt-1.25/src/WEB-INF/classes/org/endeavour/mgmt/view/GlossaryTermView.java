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

import org.endeavour.mgmt.controller.GlossaryTermMaintenance;
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

public class GlossaryTermView extends DialogComponent implements ActionListener {

	private Label nameLabel = null;
	private TextField nameTextField = null;
	private Label createdByLabel = null;
	private TextField createdByTextField = null;
	private Label descriptionLabel = null;
	private TextArea descriptionTextArea = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private GlossaryTermMaintenance glossaryTermMaintenance = null;

	public GlossaryTermView(GlossaryTermMaintenance aGlossaryTermMaintenance) {
		this.glossaryTermMaintenance = aGlossaryTermMaintenance;
		this.glossaryTermMaintenance.startUnitOfWork();
		this.glossaryTermMaintenance.reset();

		super.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 20, 135, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("glossary_term.lbl"));
		super.setSize(400, 250);
		super.centerDialog();

		this.nameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		super.add(this.nameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		super.add(this.nameTextField.setLimit("1, 0"));

		this.createdByLabel = new Label(IViewConstants.RB.getString("created_by.lbl"));
		super.add(this.createdByLabel.setLimit("0, 1"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		this.createdByTextField = new TextField();
		this.createdByTextField.setText(theSecurityMaintenance.getLoggedUserId());
		this.createdByTextField.setEnabled(false);
		super.add(this.createdByTextField.setLimit("1, 1"));

		this.descriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		super.add(this.descriptionLabel.setLimit("0, 2"));

		this.descriptionTextArea = new TextArea();
		super.add(this.descriptionTextArea.setLimit("1, 2"));

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

		super.add(theButtonsPanel.setLimit("1, 3"));

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public GlossaryTermView(Integer aGlossaryTermId, GlossaryTermMaintenance aGlossaryTermMaintenance) {
		this(aGlossaryTermMaintenance);
		this.viewGlossaryTerm(aGlossaryTermId);
	}

	private void viewGlossaryTerm(Integer aGlossaryTermId) {
		Map<String, Object> theData = this.glossaryTermMaintenance.getGlossaryTermDataBy(aGlossaryTermId);
		String theName = (String) theData.get(GlossaryTermMaintenance.NAME);
		String theDescription = (String) theData.get(GlossaryTermMaintenance.DESCRIPTION);
		String theCreatedBy = (String) theData.get(GlossaryTermMaintenance.CREATED_BY);

		this.nameTextField.setText(theName);
		this.descriptionTextArea.setText(theDescription);
		this.createdByTextField.setText(theCreatedBy);

		super.setTitle(IViewConstants.RB.getString("glossary_term.lbl") + " - " + theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveGlossaryTerm();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void saveGlossaryTerm() {

		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(GlossaryTermMaintenance.NAME, this.nameTextField.getText());
		theData.put(GlossaryTermMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
		theData.put(GlossaryTermMaintenance.CREATED_BY, this.createdByTextField.getText());

		List<String> theErrors = this.glossaryTermMaintenance.saveGlossaryTerm(theData);
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
			this.glossaryTermMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
