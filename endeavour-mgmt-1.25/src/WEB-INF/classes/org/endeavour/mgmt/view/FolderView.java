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

import org.endeavour.mgmt.controller.ITestMaintenance;
import org.endeavour.mgmt.controller.TestPlanMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class FolderView extends DialogComponent implements ActionListener {

	private Button saveButton = null;
	private Button cancelButton = null;
	private TestPlanMaintenance testPlanMaintenance = null;
	private TextField nameTextField = null;

	public FolderView(TestPlanMaintenance aTestPlanMaintenance) {

		this.testPlanMaintenance = aTestPlanMaintenance;
		this.testPlanMaintenance.resetTestFolder();

		super.setTitle(IViewConstants.RB.getString("folder.lbl"));
		super.setSize(755, 90);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 40, 0 }, { 25, 25 } }, 5, 5));

		Label theFolderNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		super.add(theFolderNameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		super.add(this.nameTextField.setLimit("1, 0"));

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

	public FolderView(Integer aTestFolder, TestPlanMaintenance aTestPlanMaintenance) {
		this(aTestPlanMaintenance);
		this.viewTestFolder(aTestFolder);
	}

	private void viewTestFolder(Integer aTestFolderId) {
		Map<String, Object> theData = this.testPlanMaintenance.getTestFolderDataBy(aTestFolderId);
		String theName = (String) theData.get(ITestMaintenance.NAME);
		this.nameTextField.setText(theName);
		super.setTitle(IViewConstants.RB.getString("folder.lbl") + " - " + theName);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveTestFolder();
				super.setVisible(false);
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}

	private void saveTestFolder() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(ITestMaintenance.NAME, this.nameTextField.getText());
		List<String> theErrors = this.testPlanMaintenance.saveTestFolder(theData);
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
