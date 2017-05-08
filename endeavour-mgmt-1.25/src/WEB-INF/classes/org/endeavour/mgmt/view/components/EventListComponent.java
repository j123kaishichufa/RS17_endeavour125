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

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.EventMaintenance;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.model.EventsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.GridBox.Row;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class EventListComponent extends PanelComponent implements ActionListener, Observer {

	private GridBoxComponent eventsGrid = null;
	private EventMaintenance eventMaintenance = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private EventsListModel eventsModel = null;

	public static final String EVENTS = "EVENTS";

	public EventListComponent(EventMaintenance aEventMaintenance, String aText) {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 25, 0, 25 } }, 5, 5));

		this.initializeControllers(aEventMaintenance);

		Label theLabel = new Label();
		theLabel.setText(aText);
		this.add(theLabel.setLimit("0, 0"));

		this.initializeEventsGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100 }, { 0 } }, 0, 5));

		newButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		newButton.setImage(IViewConstants.NEW_BUTTON_ICON);
		newButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(newButton.setLimit("1, 0"));

		editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
		editButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(editButton.setLimit("2, 0"));

		deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		deleteButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(deleteButton.setLimit("3, 0"));

		super.add(theButtonsPanel.setLimit("0, 2"));
	}

	private void initializeEventsGrid() {

		this.eventsModel = new EventsListModel(this.eventMaintenance.getEvents());
		this.eventsGrid = new GridBoxComponent(this.eventsModel);
		this.eventsGrid.setVisibleHeader(false);
		this.eventsGrid.setColumnWidth(0, 30);
		this.eventsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.add(this.eventsGrid.setLimit("0, 1"));
	}

	private void initializeControllers(EventMaintenance aEventMaintenance) {
		this.eventMaintenance = aEventMaintenance;
		this.eventMaintenance.addObserver(this);
	}

	public void actionPerformed(ActionEvent aEvt) {

		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewEvent();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				this.eventMaintenance.clearSelectedEvent();
				new EventViewComponent(this.eventMaintenance);
			}

			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewEvent();
			}

			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				Row theSelectedRow = this.eventsGrid.getSelectedRow();
				if (theSelectedRow != null) {
					int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
					if (theResult == IViewConstants.YES) {
						this.eventMaintenance.deleteEvent(theSelectedRow.getIndex());
						this.viewEvents();
					}
				}
			}
		}
	}

	private void viewEvent() {
		Integer theEventId = (Integer) this.eventsGrid.getSelectedRowId();
		if (theEventId != null) {
			new EventViewComponent(theEventId, this.eventMaintenance);
		}
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewEvents();
	}

	private void viewEvents() {
		this.eventsModel.setData(this.eventMaintenance.getEvents());
	}

	public Object getData() {
		return this.eventMaintenance.getData();
	}

	public void setModel(Map<String, Object> aData) {
		this.eventMaintenance.setEvents((List) aData.get(EVENTS));
		this.viewEvents();
	}

	public void addActionListener(ActionListener aActionListener) {
		this.eventsGrid.addActionListener(GridBoxComponent.ACTION_CLICK, aActionListener);
	}

	public Integer getSelectedEventId() {
		return this.eventsGrid.getSelectedRowId();
	}

	public boolean hasEvents() {
		List<Row> theRows = this.eventsGrid.getRows();
		return theRows != null && theRows.size() > 0;
	}

	public void enableButtons(boolean aEnabled) {
		this.newButton.setEnabled(aEnabled);
		this.editButton.setEnabled(aEnabled);
		this.deleteButton.setEnabled(aEnabled);
	}
	public void setVisibleButtons(boolean isVisible) {
		this.newButton.setVisible(isVisible);
		this.editButton.setVisible(isVisible);
		this.deleteButton.setVisible(isVisible);
	}
}
