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

import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.EventMaintenance;
import org.endeavour.mgmt.controller.ExtensionsMaintenance;
import org.endeavour.mgmt.controller.UseCaseMaintenance;
import org.endeavour.mgmt.view.components.EventListComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Label;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class UseCasePanel extends PanelComponent implements ActionListener {

	private TextField nameTextField = null;
	private EventListComponent mainSucessScenarioEventList = null;
	private EventListComponent extensionsEventList = null;
	private EventMaintenance eventMaintenance = null;
	private ExtensionsMaintenance extensionsMaintenance = null;

	public UseCasePanel(BasicInfoPanel aBasicInfoPanel) {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 25, 0, 0 } }));

		PanelComponent theNamePanel = new PanelComponent();
		theNamePanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20 } }, 5, 5));

		Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		theNamePanel.add(theNameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		this.nameTextField.setText(aBasicInfoPanel.getNameTextField().getText());
		this.nameTextField.setEnabled(false);
		theNamePanel.add(this.nameTextField.setLimit("1, 0"));

		super.add(theNamePanel.setLimit("0, 0"));

		this.initializeSuccessScenarioGrid();
		this.initializeExtensionsGrid();
	}

	private void initializeSuccessScenarioGrid() {
		this.eventMaintenance = new EventMaintenance();
		this.mainSucessScenarioEventList = new EventListComponent(this.eventMaintenance, IViewConstants.RB.getString("main_sucess_scenario.lbl"));
		this.mainSucessScenarioEventList.addActionListener(this);
		this.add(this.mainSucessScenarioEventList.setLimit("0, 1"));
	}

	private void initializeExtensionsGrid() {
		this.extensionsMaintenance = new ExtensionsMaintenance(this.eventMaintenance);
		this.extensionsEventList = new EventListComponent(this.extensionsMaintenance, IViewConstants.RB.getString("extensions.lbl"));
		this.add(this.extensionsEventList.setLimit("0, 2"));
	}

	public void populateName(String aName) {
		this.nameTextField.setText(aName);
	}

	public void getData(Map<String, Object> aData) {
		aData.put(UseCaseMaintenance.EVENTS, (List) this.mainSucessScenarioEventList.getData());
		aData.put(UseCaseMaintenance.EXTENSIONS, (Map) this.extensionsEventList.getData());
	}

	public void setData(Map<String, Object> aData) {
		this.mainSucessScenarioEventList.setModel(aData);

		boolean hasEvents = this.mainSucessScenarioEventList.hasEvents();
		this.extensionsEventList.enableButtons(hasEvents);
		if (hasEvents) {
			Integer theSelectedEventId = this.mainSucessScenarioEventList.getSelectedEventId();
			this.extensionsMaintenance.setSelectedEventId(theSelectedEventId);
		}
	}

	public void actionPerformed(ActionEvent aEvent) {
		Integer theSelectedId = this.mainSucessScenarioEventList.getSelectedEventId();
		this.extensionsMaintenance.setSelectedEventId(theSelectedId);
		this.extensionsEventList.enableButtons(this.mainSucessScenarioEventList.hasEvents());
	}
}
