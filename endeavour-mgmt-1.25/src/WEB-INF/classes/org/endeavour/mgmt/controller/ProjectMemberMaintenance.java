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
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class ProjectMemberMaintenance extends ApplicationController {

	private List<String> errors = null;
	private ProjectMember projectMember = null;

	public static final String USER_ID = ProjectMember.USER_ID;
	public static final String PASSWORD = ProjectMember.PASSWORD;
	public static final String PASSWORD2 = ProjectMember.PASSWORD2;
	public static final String FIRST_NAME = ProjectMember.FIRST_NAME;
	public static final String LAST_NAME = ProjectMember.LAST_NAME;
	public static final String FULL_NAME = ProjectMember.FULL_NAME;
	public static final String STATUS = ProjectMember.STATUS;
	public static final String ROLE = ProjectMember.ROLE;
	public static final String SECURITY_GROUP = ProjectMember.SECURITY_GROUP;
	public static final String EMAIL = ProjectMember.EMAIL;
	public static final String ACCEPT_NOTIFICATIONS = ProjectMember.ACCEPT_NOTIFICATIONS;

	public void reset() {
		this.projectMember = null;
	}

	public void deleteProjectMember(Integer aProjectMemberId) {

		this.projectMember = this.retrieveProjectMemberBy(aProjectMemberId);
		this.projectMember.delete();
	}

	public Map<String, Object> getProjectMemberDataBy(Integer aProjectMemberId) {
		this.projectMember = this.retrieveProjectMemberBy(aProjectMemberId);
		return this.projectMember.getData();
	}

	public List<ProjectMember> getProjectMembers() {
		List<ProjectMember> theProjectMembers = PersistenceManager.getInstance().findAllBy("select projectMember from " + ProjectMember.class.getSimpleName() + " projectMember order by projectMember.userId");
		return theProjectMembers;
	}

	public List<ProjectMember> getProjectMembersByPrivilege() {
		List<ProjectMember> theProjectMembers = new ArrayList<ProjectMember>();
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		ProjectMember theProjectMember = theSecurityMaintenance.getLoggedUser();
		if (theProjectMember.isAdministrator()) {
			theProjectMembers = this.getProjectMembers();
		} else {
			theProjectMembers.add(theProjectMember);
		}
		return theProjectMembers;
	}

	public List<String> saveProjectMember(Map<String, Object> aData) {

		if (this.projectMember == null) {
			this.projectMember = new ProjectMember();
		}
		if (this.isValid(aData)) {
			this.projectMember.save(aData);
			setChanged();
		}
		notifyObservers();
		return this.errors;
	}

	public void setProjectMember(ProjectMember aProjectMember) {
		this.projectMember = aProjectMember;
	}

	public ProjectMember getProjectMember() {
		return this.projectMember;
	}

	private ProjectMember retrieveProjectMemberBy(Integer aProjectMemberId) {
		ProjectMember theProjectMember = (ProjectMember) PersistenceManager.getInstance().findById(ProjectMember.class, aProjectMemberId);
		return theProjectMember;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.projectMember.validate(aData);
		return this.errors.isEmpty();
	}

	public Integer getSelectedProjectMemberid() {
		Integer theId = null;
		if (this.projectMember != null) {
			theId = this.projectMember.getId();
		}
		return theId;
	}
}
