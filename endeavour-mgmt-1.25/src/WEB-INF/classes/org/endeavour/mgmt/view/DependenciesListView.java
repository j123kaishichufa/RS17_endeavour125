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
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.DependencyMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.DependenciesListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class DependenciesListView extends PanelComponent implements ActionListener, Observer {

	private DependencyMaintenance dependencyMaintenance = null;
	private GridBoxComponent dependenciesGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private DependenciesListModel dependenciesModel = null;

	public DependenciesListView(DependencyMaintenance aDependencyMaintenance) {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.initializeControllers(aDependencyMaintenance);
		this.initializeDependenciesGrid();

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

		super.add(theButtonsPanel.setLimit("0, 1"));
	}

	private void setButtonsStatus() {
		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		this.editButton.setEnabled(isEnabled && !this.dependenciesGrid.getRows().isEmpty());

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_DELETE);
		this.deleteButton.setEnabled(isEnabled && !this.dependenciesGrid.getRows().isEmpty() && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_EDIT);
		this.newButton.setEnabled(isEnabled && hasPrivilege);
	}

	private void initializeControllers(DependencyMaintenance aDependencyMaintenance) {
		this.dependencyMaintenance = aDependencyMaintenance;
		this.dependencyMaintenance.addObserver(this);
	}

	private void initializeDependenciesGrid() {
		this.dependenciesModel = new DependenciesListModel();
		this.dependenciesGrid = new GridBoxComponent(this.dependenciesModel);
		this.dependenciesGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.dependenciesGrid.setLimit("0, 0"));
	}

	private void viewDependencies() {
		this.dependenciesModel.setData(this.dependencyMaintenance.getDependencies());
		Integer theId = this.dependencyMaintenance.getSelectedDependencyId();
		if (theId != null) {
			this.dependenciesGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}
	
	public void setData(Map<String, Object> aData) {
		this.dependenciesModel.setData((List) aData.get(TaskMaintenance.DEPENDENCIES));
		this.dependencyMaintenance.setDependencies((List) aData.get(TaskMaintenance.DEPENDENCIES));
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewDependency();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new DependencyView(this.dependencyMaintenance, false);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewDependency();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteDependency();
			}
		}
	}

	private void viewDependency() {
		Integer theDependencyId = this.dependenciesGrid.getSelectedRowId();
		if (theDependencyId != null) {
			this.dependenciesGrid.setEnabled(false);
			new DependencyView(theDependencyId, this.dependencyMaintenance);
			this.dependenciesGrid.setEnabled(true);
		}
	}

	private void deleteDependency() {
		Integer theDependencyId = this.dependenciesGrid.getSelectedRowId();
		if (theDependencyId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.dependencyMaintenance.deleteDependency(theDependencyId);
				this.viewDependencies();
			}
		}
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewDependencies();
	}
	
	public int getRowCount() {
		return this.dependenciesModel.getRowCount();
	}
}
