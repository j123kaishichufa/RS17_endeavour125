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
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.endeavour.mgmt.model.ITestPlanElement;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.TestFolder;
import org.endeavour.mgmt.model.TestRun;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class TestPlanModel extends GridBoxModel {

	public TestPlanModel() {
		super();
	}

	public TestPlanModel(List<ITestPlanElement> aPlanElements) {
		super(aPlanElements);
	}

	public void initializeColumns() {
		String theIcon = IViewConstants.RB.getString("icon.lbl");
		String theName = IViewConstants.RB.getString("name.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");
		String theExecutionDate = IViewConstants.RB.getString("execution_date.lbl");
		String theAssigned = IViewConstants.RB.getString("assigned.lbl");
		String theType = IViewConstants.RB.getString("type.lbl");

		super.columns = new String[] { theIcon, theName, theStatus, theExecutionDate, theAssigned, theType };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = "";
		String theSpacer = null;
		ITestPlanElement theTestPlanElement = (ITestPlanElement) super.rows.get(aRowIndex);
		if (theTestPlanElement instanceof TestFolder) {
			theSpacer = "";
		} else if (theTestPlanElement instanceof TestRun) {
			theSpacer = "    ";
		}
		switch (aColumnIndex) {
		case 0:
			if (theTestPlanElement instanceof TestFolder) {
				theValue = theSpacer + "<img src=\"" + IViewConstants.FOLDER_ICON + "\"/>";
			} else if (theTestPlanElement instanceof TestRun) {
				theValue = theSpacer + "<img src=\"" + IViewConstants.TEST_CASES_ICON + "\"/>";
			}
			break;
		case 1:
			theValue = theSpacer + theTestPlanElement.getName();
			break;
		case 2:
			theValue = theTestPlanElement.getStatus();
			if (theValue != null) {
				if (theValue.equals(TestCaseStatusListModel.PASS)) {
					theValue = "<img src=\"" + IViewConstants.PASS_ICON + "\"/>";
				} else if (theValue.equals(TestCaseStatusListModel.FAIL)) {
					theValue = "<img src=\"" + IViewConstants.FAIL_ICON + "\"/>";
				}
			}
			break;
		case 3:
			if (theTestPlanElement instanceof TestRun) {
				Date theExecutionDate = ((TestRun) theTestPlanElement).getExecutionDate();
				if (theExecutionDate != null) {
					theValue = new SimpleDateFormat(IViewConstants.DATE_MASK + " hh:mm aaa").format(theExecutionDate);
				}
			}
			break;
		case 4:
			if (theTestPlanElement instanceof TestRun) {
				Set<ProjectMember> theProjectMembers = ((TestRun) theTestPlanElement).getProjectMembers();
				for (ProjectMember theProjectMember : theProjectMembers) {
					if (theProjectMember != null) {
						theValue = theValue + theProjectMember.getUserId() + ",";
					}
				}
				if (theValue.endsWith(",")) {
					theValue = theValue.substring(0, theValue.lastIndexOf(","));
				}
			}
			break;
		case 5:
			theValue = theTestPlanElement.getElementType();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		ITestPlanElement theTestPlaneElement = (ITestPlanElement) super.rows.get(aRowIndex);
		return theTestPlaneElement.getId();
	}
}
