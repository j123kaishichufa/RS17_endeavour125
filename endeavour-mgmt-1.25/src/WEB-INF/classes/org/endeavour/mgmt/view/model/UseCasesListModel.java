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

import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class UseCasesListModel extends GridBoxModel {

	private StatusListModel statusListModel = null;
	private PriorityListModel priorityModel = null;

	public UseCasesListModel() {
		super();
		this.statusListModel = new StatusListModel(false, false);
		this.priorityModel = new PriorityListModel(true);
	}

	public void initializeColumns() {

		String theNumber = IViewConstants.RB.getString("number.lbl");
		String theUseCaseName = IViewConstants.RB.getString("use_case_name.lbl");
		String thePriority = IViewConstants.RB.getString("priority.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");
		String theStartDate = IViewConstants.RB.getString("start_date.lbl");
		String theEndDate = IViewConstants.RB.getString("end_date.lbl");
		String thePercentComplete = IViewConstants.RB.getString("percent_complete.lbl");

		super.columns = new String[] { theNumber, theUseCaseName, thePriority, theStatus, theStartDate, theEndDate, thePercentComplete };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		UseCase theUseCase = (UseCase) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = IViewConstants.RB.getString("use_case_initials.lbl") + theUseCase.getId().toString();
			break;
		case 1:
			theValue = theUseCase.getName();
			break;
		case 2:
			theValue = this.priorityModel.getDescriptionByValue(theUseCase.getPriority());
			break;
		case 3:
			theValue = this.statusListModel.getDescriptionByValue(theUseCase.getStatus());
			break;
		case 4:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theUseCase.getStartDate());
			break;
		case 5:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theUseCase.getEndDate());
			break;
		case 6:
			theValue = theUseCase.getProgress() + "%";
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		UseCase theUseCase = (UseCase) this.rows.get(aRowIndex);
		return theUseCase.getId();
	}
}
