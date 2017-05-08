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

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.IPlanElement;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class ProjectPlanModel extends GridBoxModel {

	public ProjectPlanModel() {
		super();
	}

	public ProjectPlanModel(List<IPlanElement> aPlanElements) {
		super(aPlanElements);
	}

	public void initializeColumns() {

		String theArtifactName = IViewConstants.RB.getString("artifact_name.lbl");
		String theIcon = IViewConstants.RB.getString("icon.lbl");
		String theType = IViewConstants.RB.getString("type.lbl");
		String theDuration = IViewConstants.RB.getString("duration.lbl");
		String theStartDate = IViewConstants.RB.getString("start_date.lbl");
		String theEndDate = IViewConstants.RB.getString("end_date.lbl");
		String thePercentComplete = IViewConstants.RB.getString("percent_complete.lbl");

		super.columns = new String[] { theArtifactName, theIcon, theType, theDuration, theStartDate, theEndDate, thePercentComplete };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		IPlanElement thePlanElement = (IPlanElement) super.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			String theSpacer = null;
			if (thePlanElement instanceof Project) {
				theSpacer = "";
			}
			if (thePlanElement instanceof Iteration) {
				theSpacer = "    ";
			} else if (thePlanElement instanceof Task) {
				Task theTask = (Task) thePlanElement;
				if (theTask.isOrphan() && theTask.isUnscheduled()) {
					// Not assigned to a UseCase and Iteration only to Project.
					theSpacer = "    ";
				} else if (!theTask.isUnscheduled() && theTask.isOrphan()) {
					// Assigned to an Iteration.
					theSpacer = "        ";
				} else if (!theTask.isOrphan()) {
					// Assigned to a UseCase.
					theSpacer = "            ";
				}
			} else if (thePlanElement instanceof WorkProduct) {
				theSpacer = "        ";
			}
			theValue = theSpacer + thePlanElement.getName();
			break;
		case 1:
			if (thePlanElement instanceof Project) {
				theValue = IViewConstants.PROJECTS_ICON;
			} else if (thePlanElement instanceof Iteration) {
				theValue = IViewConstants.ITERATIONS_ICON;
			} else if (thePlanElement instanceof UseCase) {
				theValue = IViewConstants.USE_CASES_ICON;
			} else if (thePlanElement instanceof Task) {
				theValue = IViewConstants.TASKS_ICON;
			} else if (thePlanElement instanceof Defect) {
				theValue = IViewConstants.DEFECTS_ICON;
			} else if (thePlanElement instanceof ChangeRequest) {
				theValue = IViewConstants.CHANGE_REQUESTS_ICON;
			}
			theValue = "<img src=\"" + theValue + "\"/>";
			break;
		case 2:
			theValue = thePlanElement.getElementType();
			break;
		case 3:
			theValue = (thePlanElement.getEndDate().getTime() - thePlanElement.getStartDate().getTime()) / (24 * 60 * 60 * 1000) + 1 + " " + IViewConstants.RB.getString("days.lbl");
			break;
		case 4:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(thePlanElement.getStartDate());
			break;
		case 5:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(thePlanElement.getEndDate());
			break;
		case 6:
			theValue = thePlanElement.getProgress() + IViewConstants.RB.getString("percent_sign.lbl");
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		IPlanElement thePlanElement = (IPlanElement) super.rows.get(aRowIndex);
		return thePlanElement.getId();
	}
}
