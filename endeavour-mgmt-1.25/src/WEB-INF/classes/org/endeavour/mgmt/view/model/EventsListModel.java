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


package org.endeavour.mgmt.view.model;

import java.util.List;

import org.endeavour.mgmt.model.Event;
import org.endeavour.mgmt.view.components.GridBoxModel;


public class EventsListModel extends GridBoxModel {

	public EventsListModel() {
		super();
	}

	public EventsListModel(List<Event> aEvents) {
		super(aEvents);
	}

	public void initializeColumns() {
		super.columns = new String[] { "", "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Event theEvent = (Event) this.rows.get(aRowIndex);
		Event theParentEvent = theEvent.getParentEvent();

		switch (aColumnIndex) {
		case 0:
			aRowIndex++;
			if (theParentEvent == null) {
				theEvent.setIndex(aRowIndex + ".");
			} else {
				theEvent.setIndex(theParentEvent.getIndex() + aRowIndex);
			}
			theValue = theEvent.getIndex();
			break;
		case 1:
			theValue = theEvent.getText();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Event theEvent = (Event) this.rows.get(aRowIndex);
		return theEvent.getId();
	}
}
