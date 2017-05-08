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
import java.util.List;

import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class IterationsListModel extends GridBoxModel {

	public IterationsListModel() {
		super();
	}

	public IterationsListModel(List<Iteration> aIterations) {
		super(aIterations);
	}

	public void initializeColumns() {

		String theIterationName = IViewConstants.RB.getString("iteration_name.lbl");
		String theStartDate = IViewConstants.RB.getString("start_date.lbl");
		String theEndDate = IViewConstants.RB.getString("end_date.lbl");
		String thePercentComplete = IViewConstants.RB.getString("percent_complete.lbl");

		super.columns = new String[] { theIterationName, theStartDate, theEndDate, thePercentComplete };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Iteration theIteration = (Iteration) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theIteration.getName();
			break;
		case 1:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theIteration.getStartDate());
			break;
		case 2:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theIteration.getEndDate());
			break;
		case 3:
			theValue = theIteration.getProgress() + IViewConstants.RB.getString("percent_sign.lbl");
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Iteration theIteration = (Iteration) this.rows.get(aRowIndex);
		return theIteration.getId();
	}
}