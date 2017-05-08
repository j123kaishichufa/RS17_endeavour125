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

import org.endeavour.mgmt.controller.ActorMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ActorsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ActorsListView extends PanelComponent implements ActionListener, Observer {

	private ActorMaintenance actorMaintenance = null;
	private GridBoxComponent actorsGrid = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private ActorsListModel actorsModel = null;

	public ActorsListView() {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 5, 5));

		this.initializeControllers();

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.ACTORS_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("actors.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeActorsGrid();

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

		super.add(theButtonsPanel.setLimit("0, 2"));
	}

	private void setButtonsStatus() {
		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		this.editButton.setEnabled(isEnabled && !this.actorsGrid.getRows().isEmpty());

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_DELETE);
		this.deleteButton.setEnabled(isEnabled && !this.actorsGrid.getRows().isEmpty() && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.newButton.setEnabled(isEnabled && hasPrivilege);
	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.actorMaintenance = new ActorMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.actorMaintenance.addObserver(this);
	}

	private void initializeActorsGrid() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		if (theProjectId != null) {
			this.actorsModel = new ActorsListModel(this.actorMaintenance.getActors());
		} else {
			this.actorsModel = new ActorsListModel();
		}
		this.actorsGrid = new GridBoxComponent(this.actorsModel);
		this.actorsGrid.setColumnWidth(0, 250);
		this.actorsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.actorsGrid.setLimit("0, 1"));
	}

	private void viewActors() {
		this.actorsModel.setData(this.actorMaintenance.getActors());
		Integer theId = this.actorMaintenance.getSelectedActorId();
		if (theId != null) {
			this.actorsGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewActor();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new ActorView(this.actorMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewActor();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteActor();
			}
		}
	}

	private void viewActor() {
		Integer theActorId = this.actorsGrid.getSelectedRowId();
		if (theActorId != null) {
			this.actorsGrid.setEnabled(false);
			new ActorView(theActorId, this.actorMaintenance);
			this.actorsGrid.setEnabled(true);
		}
	}

	private void deleteActor() {
		this.actorMaintenance.startUnitOfWork();
		Integer theActorId = this.actorsGrid.getSelectedRowId();
		if (theActorId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.actorMaintenance.reset();
				this.actorMaintenance.deleteActor(theActorId);
				this.viewActors();
			}
		}
		this.actorMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewActors();
	}
}
