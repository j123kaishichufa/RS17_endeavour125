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

import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class DefectsListModel extends GridBoxModel {

	private StatusListModel statusListModel = null;

	public DefectsListModel() {
		super();
		this.statusListModel = new StatusListModel(true, false);
	}

	public void initializeColumns() {

		String theNumber = IViewConstants.RB.getString("number.lbl");
		String thePriority = IViewConstants.RB.getString("priority.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");
		String theDefectName = IViewConstants.RB.getString("defect_name.lbl");
		String theDescription = IViewConstants.RB.getString("description.lbl");

		super.columns = new String[] { theNumber, thePriority, theStatus, theDefectName, theDescription };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Defect theDefect = (Defect) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = IViewConstants.RB.getString("defect_initial.lbl") + theDefect.getId().toString();
			break;
		case 1:
			theValue = theDefect.getPriority();
			if (theValue.equals(PriorityListModel.HIGH)) {
				theValue = IViewConstants.PRIORITY_HIGH_ICON;
			} else if (theValue.equals(PriorityListModel.MEDIUM)) {
				theValue = IViewConstants.PRIORITY_MEDIUM_ICON;
			} else if (theValue.equals(PriorityListModel.LOW)) {
				theValue = IViewConstants.PRIORITY_LOW_ICON;
			}
			theValue = "<img src=\"" + theValue + "\"/>";
			break;
		case 2:
			theValue = this.statusListModel.getDescriptionByValue(theDefect.getStatus());
			break;
		case 3:
			theValue = theDefect.getName();
			break;
		case 4:
			theValue = theDefect.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Defect theDefect = (Defect) this.rows.get(aRowIndex);
		return theDefect.getId();
	}
}
