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

package org.endeavour.mgmt.view.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.EventMaintenance;
import org.endeavour.mgmt.view.IViewConstants;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class EventViewComponent extends DialogComponent implements ActionListener {

	private Button saveButton = null;
	private Button cancelButton = null;
	private EventMaintenance eventMaintenance = null;
	private TextField eventTextField = null;

	public EventViewComponent(EventMaintenance aEventMaintenance) {

		this.eventMaintenance = aEventMaintenance;

		super.setTitle(IViewConstants.RB.getString("event.lbl"));
		super.setSize(755, 90);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 25, 25 } }, 5, 5));

		Label theEventLabel = new Label(IViewConstants.RB.getString("event.lbl") + ":");
		super.add(theEventLabel.setLimit("0, 0"));

		this.eventTextField = new TextField();
		super.add(this.eventTextField.setLimit("1, 0"));

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

		super.add(theButtonsPanel.setLimit("1, 1"));

		super.setVisible(true);
	}

	public EventViewComponent(Integer aEvent, EventMaintenance aEventMaintenance) {
		this(aEventMaintenance);
		this.viewEvent(aEvent);
	}

	private void viewEvent(Integer aEvent) {
		String theEvent = this.eventMaintenance.viewEvent(aEvent);
		this.eventTextField.setText(theEvent);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveEvent();
				super.setVisible(false);
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}

	private void saveEvent() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(EventMaintenance.TEXT, this.eventTextField.getText());
		List<String> theErrors = this.eventMaintenance.saveEvent(theData);
		if (theErrors.isEmpty()) {
			super.setVisible(false);
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
}
