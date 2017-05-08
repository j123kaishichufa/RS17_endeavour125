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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.Document;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.WorkProduct;

@SuppressWarnings("unchecked")
public class WorkProductMaintenance extends ApplicationController {

	public static final String PROJECT_MEMBERS = WorkProduct.PROJECT_MEMBERS;
	public static final String DOCUMENTS = WorkProduct.DOCUMENTS;
	public static final String ATTACHMENTS = WorkProduct.ATTACHMENTS;
	public static final String COMMENTS = WorkProduct.COMMENTS;
	public static final String DESCRIPTION = WorkProduct.DESCRIPTION;
	public static final String ITERATION = WorkProduct.ITERATION;
	public static final String UNASSIGNED_PROJECT_MEMBERS = "UNASSIGNED_PROJECT_MEMBERS";

	private Project project = null;
	private WorkProduct workProduct = null;

	protected void processProjectMemberIds(Map<String, Object> aData) {
		List<ProjectMember> theProjectMembers = new ArrayList<ProjectMember>();
		List<Integer> theProjectMemberIds = (List) aData.get(PROJECT_MEMBERS);

		for (Integer theProjectMemberId : theProjectMemberIds) {
			ProjectMember theProjectMember = this.getProject().retrieveProjectMemberBy(theProjectMemberId);
			theProjectMembers.add(theProjectMember);
		}
		aData.put(PROJECT_MEMBERS, theProjectMembers);
	}

	public List<ProjectMember> getUnassignedProjectMembersDataForProject() {
		return ProjectMember.getUnassignedProjectMembersFor(this.workProduct, this.getProject());
	}

	public void reset() {
		this.workProduct = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public List<Document> getUnassignedDocuments() {
		return Document.getUnassignedDocumentsDataFor(this.workProduct, this.getProject());
	}

	public Date getProjectStartDate() {
		return this.project.getStartDate();
	}

	public Date getProjectEndDate() {
		return this.project.getEndDate();
	}

	public Project getProject() {
		return this.project;
	}

	protected void setProject(Project aProject) {
		this.project = aProject;
	}

	protected WorkProduct getWorkProduct() {
		return this.workProduct;
	}

	protected void setWorkProduct(WorkProduct anWorkProduct) {
		this.workProduct = anWorkProduct;
	}

	public Integer getSelectedWorkProductId() {
		Integer theId = null;
		if (this.workProduct != null) {
			theId = this.workProduct.getId();
		}
		return theId;
	}

	protected void processIterationId(Map<String, Object> aData) {
		Integer theIterationId = (Integer) aData.get(ITERATION);
		Iteration theIteration = this.getProject().retrieveIterationBy(theIterationId);
		aData.put(ITERATION, theIteration);
	}
}
