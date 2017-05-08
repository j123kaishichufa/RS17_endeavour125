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

import org.endeavour.mgmt.controller.DependencyMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.DependencyTypesListModel;
import org.endeavour.mgmt.view.model.TasksListModel;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class DependencyView extends DialogComponent implements ActionListener {

	private DropDownGridBoxComponent typesDropDown = null;
	private DropDownGridBoxComponent predecessorsDropDown = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private DependencyMaintenance dependencyMaintenance = null;
	private DependencyTypesListModel dependencyTypes = null;
	private TasksListModel predecessorsListModel = null;

	public DependencyView(Integer aDependencyId, DependencyMaintenance aDependencyMaintenance) {
		this(aDependencyMaintenance, true);
		this.viewDependency(aDependencyId);
	}

	public DependencyView(DependencyMaintenance aDependencyMaintenance, boolean isEditing) {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.dependencyMaintenance = aDependencyMaintenance;
		this.dependencyMaintenance.reset();

		super.setTitle(IViewConstants.RB.getString("dependency.lbl"));
		super.setSize(400, 110);
		super.centerDialog();

		PanelComponent theDependencyPanel = new PanelComponent();
		theDependencyPanel.setLayout(new TableLayout(new double[][] { { 65, 0 }, { 20, 20 } }, 5, 5));

		Label thePredecessorLabel = new Label(IViewConstants.RB.getString("predecessor.lbl") + ":");
		theDependencyPanel.add(thePredecessorLabel.setLimit("0, 0"));

		this.predecessorsListModel = new TasksListModel(aDependencyMaintenance.getAvailablePredecessors(isEditing));
		this.predecessorsDropDown = new DropDownGridBoxComponent(this.predecessorsListModel, 1);
		theDependencyPanel.add(this.predecessorsDropDown.setLimit("1, 0"));

		Label theTypesLabel = new Label(IViewConstants.RB.getString("type.lbl") + ":");
		theDependencyPanel.add(theTypesLabel.setLimit("0, 1"));

		this.dependencyTypes = new DependencyTypesListModel();
		this.typesDropDown = new DropDownGridBoxComponent(this.dependencyTypes, 0);
		this.typesDropDown.selectFirstElement();
		theDependencyPanel.add(this.typesDropDown.setLimit("1, 1"));

		super.add(theDependencyPanel.setLimit("0, 0"));

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

		super.add(theButtonsPanel.setLimit("0, 1"));

		super.setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveDependency();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}

	private void saveDependency() {

		Map<String, Object> theData = new HashMap<String, Object>();
		
		Object theSelectedValue = null;
		if(this.predecessorsDropDown.getComponent().getSelectedRow() != null) {
			theSelectedValue = this.predecessorsListModel.getRowObjectByIndex(this.predecessorsDropDown.getComponent().getSelectedRow().getIndex());
		}
		
		theData.put(DependencyMaintenance.PREDECESSOR, theSelectedValue);
		theData.put(DependencyMaintenance.TYPE, this.dependencyTypes.getValueById(this.typesDropDown.getSelectedRowId()));
		List<String> theErrors = this.dependencyMaintenance.saveDependency(theData);
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

	private void viewDependency(Integer aDependencyId) {
		Map<String, Object> theData = this.dependencyMaintenance.getDependencyDataBy(aDependencyId);
		this.typesDropDown.setText(this.dependencyTypes.getDescriptionByValue((String) theData.get(DependencyMaintenance.TYPE)));
		this.predecessorsDropDown.setSelectedRowObject((Integer) theData.get(DependencyMaintenance.PREDECESSOR));
	}
}
