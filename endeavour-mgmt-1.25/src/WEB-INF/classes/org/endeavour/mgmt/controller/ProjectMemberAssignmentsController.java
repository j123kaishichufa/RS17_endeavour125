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
package org.endeavour.mgmt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.view.MainView;
import org.endeavour.mgmt.view.model.PriorityListModel;
import org.endeavour.mgmt.view.model.StatusListModel;

public class ProjectMemberAssignmentsController extends ApplicationController implements IReportConstants {

	public List<ProjectMember> getProjectMembers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		Project theProject = theProjectMaintenance.retrieveProjectBy(theProjectId);
		return new ArrayList<ProjectMember>(theProject.getProjectMembers());
	}

	private String generateProjectMembersParameter() {
		List<ProjectMember> theProjectMembers = this.getProjectMembers();
		String theProjectMembersList = "";
		ProjectMember theProjectMember = null;
		int theSize = theProjectMembers.size();
		for (int i = 0; i < theSize; i++) {
			theProjectMember = theProjectMembers.get(i);
			theProjectMembersList = theProjectMembersList + theProjectMember.getId() + ":Integer";
			if (i != theSize - 1) {
				theProjectMembersList = theProjectMembersList + ",";
			}
		}
		return "[" + theProjectMembersList + "]";
	}

	public String createReportLocation(Integer aProjectMemberId, String aType, String aStatus, String aPriority, Integer aProject) {
		String theReportFile = null;

		if (aType.equals(UseCase.LABEL)) {
			theReportFile = PROJECT_MEMBER_USE_CASES_REPORT;
		} else if (aType.equals(ChangeRequest.LABEL)) {
			theReportFile = PROJECT_MEMBER_CHANGE_REQUESTS_REPORT;
		} else if (aType.equals(Defect.LABEL)) {
			theReportFile = PROJECT_MEMBER_DEFECTS_REPORT;
		} else if (aType.equals(Task.LABEL)) {
			theReportFile = PROJECT_MEMBER_TASKS_REPORT;
		} else if (aType.equals(WorkProduct.LABEL)) {
			theReportFile = PROJECT_MEMBER_WORK_PRODUCTS_REPORT;
		}

		if (aStatus.equals(StatusListModel.ALL)) {
			StatusListModel theStatusModel = new StatusListModel(true, false);
			aStatus = theStatusModel.toString();
		} else {
			aStatus = "[" + aStatus + ":String" + "]";
		}

		if (aPriority.equals(PriorityListModel.ALL)) {
			PriorityListModel thePriorityModel = new PriorityListModel(false);
			aPriority = thePriorityModel.toString();
		} else {
			aPriority = "[" + aPriority + ":String" + "]";
		}

		String theProjectMemberId = null;
		if (aProjectMemberId == -1) {
			theProjectMemberId = this.generateProjectMembersParameter();
		} else {
			theProjectMemberId = "[" + aProjectMemberId + ":Integer" + "]";
		}

		Map<String, String> theReportParameters = new HashMap<String, String>();
		theReportParameters.put("project_member_id_parameter", theProjectMemberId);
		theReportParameters.put("status_parameter", aStatus);
		theReportParameters.put("priority_parameter", aPriority);
		theReportParameters.put("project_id_parameter", aProject + ":Integer");
		return super.createReportURL("project_member_assignments.pdf", theReportFile, theReportParameters);
	}
}
