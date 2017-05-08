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
package org.endeavour.mgmt.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class SecurityGroup {

	private Integer id = null;
	private String name = null;
	private List<Privilege> privileges = null;
	private List<ProjectMember> projectMembers = null;

	public static final String NAME = "NAME";
	public static final String PRIVILEGES = "PRIVILEGES";

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public List<Privilege> getPrivileges() {
		if (this.privileges == null) {
			this.privileges = new ArrayList<Privilege>();
		}
		return this.privileges;
	}

	public void setPrivileges(List<Privilege> aPrivileges) {
		this.privileges = aPrivileges;
	}

	public List<ProjectMember> getProjectMembers() {
		if (this.projectMembers == null) {
			this.projectMembers = new ArrayList<ProjectMember>();
		}
		return this.projectMembers;
	}

	public void setProjectMembers(List<ProjectMember> aProjectMembers) {
		this.projectMembers = aProjectMembers;
	}

	public void delete() {
		for (ProjectMember theProjectMember : this.getProjectMembers()) {
			if(theProjectMember != null) {
				theProjectMember.setSecurityGroup(null);
			}
		}
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		List<String> thePrivilegeValues = new ArrayList<String>();
		for (Privilege thePrivilege : this.getPrivileges()) {
			if (thePrivilege != null) {
				thePrivilegeValues.add(thePrivilege.getValue());
			}
		}
		theData.put(PRIVILEGES, thePrivilegeValues);
		theData.put(NAME, this.getName());
		return theData;
	}

	public void save(Map<String, Object> aData) {

		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(securityGroup.id) from " + SecurityGroup.class.getSimpleName() + " securityGroup");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}
		String theName = (String) aData.get(NAME);
		if (theName != null) {
			this.setName(theName);
		}

		List<String> thePrivileges = (List) aData.get(PRIVILEGES);
		this.updatePrivileges(thePrivileges);

		PersistenceManager.getInstance().save(this);
	}

	private void updatePrivileges(List<String> aPrivileges) {

		List<Privilege> thePrivileges = this.getPrivileges();
		for (Privilege thePrivilege : thePrivileges) {
			if (thePrivilege != null) {
				thePrivilege.setSecurityGroup(null);
				thePrivileges.set(thePrivileges.indexOf(thePrivilege), null);
				thePrivilege.delete();
			}
		}

		thePrivileges.clear();

		Integer thePrivilegeId = (Integer) PersistenceManager.getInstance().findBy("select max(privilege.id) from " + Privilege.class.getSimpleName() + " privilege");
		thePrivilegeId = thePrivilegeId == null ? 0 : thePrivilegeId;

		Privilege theNewPrivilege = null;
		for (String thePrivilege : aPrivileges) {
			thePrivilegeId++;
			theNewPrivilege = new Privilege(thePrivilegeId, thePrivilege, this);
			thePrivileges.add(theNewPrivilege);
		}
	}

	public List<String> validate(Map<String, Object> aData) {

		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		List<Privilege> thePrivileges = (List) aData.get(PRIVILEGES);
		if (thePrivileges.size() == 0) {
			theErrors.add(IViewConstants.RB.getString("group_privileges.msg"));
		}

		return theErrors;
	}

	public boolean hasAllPrivileges() {

		boolean hasAllPrivileges = true;
		Field[] thePrivileges = IPrivileges.class.getFields();
		for (Field thePrivilege : thePrivileges) {
			hasAllPrivileges = this.hasPrivilege(thePrivilege.getName());
			if (!hasAllPrivileges) {
				break;
			}
		}
		return hasAllPrivileges;
	}

	public boolean hasPrivilege(String aPrivilege) {
		boolean hasPrivilege = false;
		for (Privilege thePrivilege : this.getPrivileges()) {
			if (thePrivilege != null) {
				if (thePrivilege.getValue().equals(aPrivilege)) {
					hasPrivilege = true;
					break;
				}
			}
		}
		return hasPrivilege;
	}
}
