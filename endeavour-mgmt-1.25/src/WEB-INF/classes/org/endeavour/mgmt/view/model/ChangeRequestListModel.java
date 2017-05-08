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

import java.text.SimpleDateFormat;

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class ChangeRequestListModel extends GridBoxModel {

	private StatusListModel statusListModel = null;
	private PriorityListModel priorityModel = null;

	public ChangeRequestListModel() {
		super();
		this.statusListModel = new StatusListModel(true, false);
		this.priorityModel = new PriorityListModel(true);
	}

	public void initializeColumns() {
		String theNumber = IViewConstants.RB.getString("number.lbl");
		String theChangeRequestName = IViewConstants.RB.getString("change_request_name.lbl");
		String thePriority = IViewConstants.RB.getString("priority.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");
		String theStartDate = IViewConstants.RB.getString("start_date.lbl");
		String theEndDate = IViewConstants.RB.getString("end_date.lbl");
		String thePercentComplete = IViewConstants.RB.getString("percent_complete.lbl");

		super.columns = new String[] { theNumber, theChangeRequestName, thePriority, theStatus, theStartDate, theEndDate, thePercentComplete };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		ChangeRequest theChangeRequest = (ChangeRequest) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = IViewConstants.RB.getString("change_request_initials.lbl") + theChangeRequest.getId().toString();
			break;
		case 1:
			theValue = theChangeRequest.getName();
			break;
		case 2:
			theValue = this.priorityModel.getDescriptionByValue(theChangeRequest.getPriority());
			break;
		case 3:
			theValue = this.statusListModel.getDescriptionByValue(theChangeRequest.getStatus());
			break;
		case 4:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theChangeRequest.getStartDate());
			break;
		case 5:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theChangeRequest.getEndDate());
			break;
		case 6:
			theValue = theChangeRequest.getProgress() + IViewConstants.RB.getString("percent_sign.lbl");
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		ChangeRequest theChangeRequest = (ChangeRequest) this.rows.get(aRowIndex);
		return theChangeRequest.getId();
	}
}
