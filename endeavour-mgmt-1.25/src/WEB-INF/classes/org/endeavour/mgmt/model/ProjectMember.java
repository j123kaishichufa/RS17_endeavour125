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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ProjectMember implements Comparable<ProjectMember> {

	private Integer id = null;
	private String userId = null;
	private String password = null;
	private String firstName = null;
	private String lastName = null;
	private String status = null;
	private String role = null;
	private String email = null;
	private Boolean acceptNotifications = null;
	private Date statusDate = null;
	private SecurityGroup securityGroup = null;
	private Set<WorkProduct> workProducts = null;
	private Set<Project> projects = null;
	private Set<TestRun> testRuns = null;

	public static final String USER_ID = "USER_ID";
	public static final String PASSWORD = "PASSWORD";
	public static final String PASSWORD2 = "PASSWORD2";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String FULL_NAME = "FULL_NAME";
	public static final String ROLE = "ROLE";
	public static final String STATUS = "STATUS";
	public static final String SECURITY_GROUP = "SECURITY_GROUP";
	public static final String EMAIL = "EMAIL";
	public static final String ACCEPT_NOTIFICATIONS = "ACCEPT_NOTIFICATIONS";

	public ProjectMember() {
		this.setId(null);
		this.setUserId("");
		this.setPassword("");
		this.setFirstName("");
		this.setLastName("");
		this.setStatus("");
		this.setRole("");
		this.setEmail("");
		this.setAcceptNotifications(false);
	}

	public boolean isPasswordCorrect(String aPassword) {
		return this.getPassword().equals(encrypt(aPassword));
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String aUserId) {
		this.userId = aUserId;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String aRole) {
		this.role = aRole;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String aPassword) {
		this.password = aPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public SecurityGroup getSecurityGroup() {
		return this.securityGroup;
	}

	public void setSecurityGroup(SecurityGroup aRole) {
		this.securityGroup = aRole;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String aFirstName) {
		this.firstName = aFirstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String aLastName) {
		this.lastName = aLastName;
	}

	public Set<WorkProduct> getWorkProducts() {
		if (this.workProducts == null) {
			this.workProducts = new HashSet<WorkProduct>();
		}
		return this.workProducts;
	}

	public void setWorkProducts(Set<WorkProduct> aWorkProducts) {
		this.workProducts = aWorkProducts;
	}

	public Set<Project> getProjects() {
		if (this.projects == null) {
			this.projects = new HashSet<Project>();
		}
		return this.projects;
	}

	public void setTestRuns(Set<TestRun> aTestRuns) {
		this.testRuns = aTestRuns;
	}

	public Set<TestRun> getTestRuns() {
		if (this.testRuns == null) {
			this.testRuns = new HashSet<TestRun>();
		}
		return this.testRuns;
	}

	public void setProjects(Set<Project> aProjects) {
		this.projects = aProjects;
	}

	public String getFullName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String aStatus) {
		this.status = aStatus;
	}

	public Date getStatusDate() {
		return this.statusDate;
	}

	public void setStatusDate(Date aStatusDate) {
		this.statusDate = aStatusDate;
	}

	public void setEmail(String aEmail) {
		this.email = aEmail;
	}

	public String getEmail() {
		return this.email;
	}

	public void setAcceptNotifications(Boolean aNotifications) {
		this.acceptNotifications = aNotifications;
	}

	public Boolean isAcceptNotifications() {
		return this.acceptNotifications;
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(USER_ID, this.getUserId());
		theData.put(PASSWORD, unencrypt(this.getPassword()));
		theData.put(FIRST_NAME, this.getFirstName());
		theData.put(LAST_NAME, this.getLastName());
		theData.put(FULL_NAME, this.getFullName());
		theData.put(ROLE, this.getRole());
		theData.put(STATUS, this.getStatus());
		theData.put(ACCEPT_NOTIFICATIONS, this.isAcceptNotifications());
		theData.put(EMAIL, this.getEmail());

		SecurityGroup theSecurityGroup = this.getSecurityGroup();
		theData.put(SECURITY_GROUP, theSecurityGroup != null ? theSecurityGroup.getId() : null);

		return theData;
	}

	public void save(Map<String, Object> aData) {

		String theUserId = (String) aData.get(USER_ID);
		if (theUserId != null) {
			this.setUserId(theUserId);
		}

		String thePassword = (String) aData.get(PASSWORD);
		if (thePassword != null) {
			this.setPassword(encrypt(thePassword));
		}

		String theFirstName = (String) aData.get(FIRST_NAME);
		if (theFirstName != null) {
			this.setFirstName(theFirstName);
		}

		String theLastName = (String) aData.get(LAST_NAME);
		if (theLastName != null) {
			this.setLastName(theLastName);
		}

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
			this.setStatusDate(new Date());
		}

		String theRole = (String) aData.get(ROLE);
		if (theRole != null) {
			this.setRole(theRole);
		}

		String theEmail = (String) aData.get(EMAIL);
		if (theEmail != null) {
			this.setEmail(theEmail);
		}

		Boolean theAcceptNotifications = (Boolean) aData.get(ACCEPT_NOTIFICATIONS);
		this.setAcceptNotifications(theAcceptNotifications);

		Integer theSecurityGroupId = (Integer) aData.get(SECURITY_GROUP);
		SecurityGroup theSecurityGroup = null;
		if (theSecurityGroupId != null) {
			theSecurityGroup = (SecurityGroup) PersistenceManager.getInstance().findById(SecurityGroup.class, theSecurityGroupId);
		}
		this.setSecurityGroup(theSecurityGroup);

		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(projectMember.id) from " + ProjectMember.class.getSimpleName() + " projectMember");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}
		PersistenceManager.getInstance().save(this);
	}

	public void delete() {

		for (Project theProject : this.getProjects()) {
			theProject.removeProjectMember(this);
		}

		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			theWorkProduct.removeProjectMember(this);
		}
		PersistenceManager.getInstance().delete(this);
	}

	public boolean isAdministrator() {
		return this.getSecurityGroup().hasAllPrivileges();
	}

	public boolean isAssigned(Project aProject) {
		boolean isAssigned = false;
		for (Project theProject : this.getProjects()) {
			if (theProject.equals(aProject)) {
				isAssigned = true;
				break;
			}
		}
		return isAssigned;
	}

	public boolean isAssigned(TestRun aTestRun) {
		boolean isAssigned = false;
		for (TestRun theTestRun : this.getTestRuns()) {
			if (theTestRun.equals(aTestRun)) {
				isAssigned = true;
				break;
			}
		}
		return isAssigned;
	}

	public boolean isAssigned(WorkProduct anWorkProduct) {
		boolean isAssigned = false;
		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct.equals(anWorkProduct)) {
				isAssigned = true;
				break;
			}
		}
		return isAssigned;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theUserId = (String) aData.get(USER_ID);
		if (theUserId == null || theUserId.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("user_id_not_empty.msg"));
		}

		String theFirstName = (String) aData.get(FIRST_NAME);
		if (theFirstName == null || theFirstName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("first_name_not_empty.msg"));
		}

		String theLastName = (String) aData.get(LAST_NAME);
		if (theLastName == null || theLastName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("last_name_not_empty.msg"));
		}

		String thePassword1 = (String) aData.get(PASSWORD);
		String thePassword2 = (String) aData.get(PASSWORD2);
		if (thePassword2 != null) {
			if (!thePassword1.equals(thePassword2)) {
				theErrors.add(IViewConstants.RB.getString("password_dont_match.msg"));
			} else if (thePassword1 == null || thePassword1.trim().length() == 0) {
				theErrors.add(IViewConstants.RB.getString("password_not_empty.msg"));
			}
		}

		String theEmail = (String) aData.get(EMAIL);
		if (theEmail == null || theEmail.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("email_not_empty.msg"));
		}

		Integer theSecurityGroupId = (Integer) aData.get(SECURITY_GROUP);
		if (theSecurityGroupId == null) {
			theErrors.add(IViewConstants.RB.getString("provide_security_group.msg"));
		}

		return theErrors;
	}

	public void addProject(Project aProject) {
		Set<Project> theProjects = this.getProjects();
		if (!theProjects.contains(aProject)) {
			theProjects.add(aProject);
		}
	}

	public void removeProject(Project aProject) {
		Set<Project> theProjects = this.getProjects();
		if (theProjects.contains(aProject)) {
			theProjects.remove(aProject);
		}
	}

	public void addWorkProduct(WorkProduct aWorkProduct) {
		Set<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (!theWorkProducts.contains(aWorkProduct)) {
			theWorkProducts.add(aWorkProduct);
		}
	}

	public void addTestRun(TestRun aTestRun) {
		Set<TestRun> theTestRuns = this.getTestRuns();
		if (!theTestRuns.contains(aTestRun)) {
			theTestRuns.add(aTestRun);
		}
	}

	public void removeWorkProduct(WorkProduct aWorkProduct) {
		Set<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (theWorkProducts.contains(aWorkProduct)) {
			theWorkProducts.remove(aWorkProduct);
		}
	}

	public void removeTestRun(TestRun aTestRun) {
		Set<TestRun> theTestRuns = this.getTestRuns();
		if (theTestRuns.contains(aTestRun)) {
			theTestRuns.remove(aTestRun);
		}
	}

	public static List<ProjectMember> getUnassignedProjectMembersFor(Object aProjectMemberAssignee, Project aProject) {

		List<ProjectMember> theProjectMembers = new ArrayList<ProjectMember>();
		if (aProject != null) {
			theProjectMembers.addAll(aProject.getProjectMembers());
		} else {
			theProjectMembers = PersistenceManager.getInstance().findAllBy("select projectMember from " + ProjectMember.class.getSimpleName() + " projectMember order by projectMember.userId");
		}

		List<ProjectMember> theUnAssignedProjectMembers = new ArrayList<ProjectMember>();
		for (ProjectMember theProjectMember : theProjectMembers) {
			boolean isAssigned = false;
			if (aProjectMemberAssignee instanceof Project) {
				isAssigned = theProjectMember.isAssigned((Project) aProjectMemberAssignee);
			} else if (aProjectMemberAssignee instanceof WorkProduct) {
				isAssigned = theProjectMember.isAssigned((WorkProduct) aProjectMemberAssignee);
			} else if (aProjectMemberAssignee instanceof TestRun) {
				isAssigned = theProjectMember.isAssigned((TestRun) aProjectMemberAssignee);
			}
			if (!isAssigned) {
				theUnAssignedProjectMembers.add(theProjectMember);
			}
		}

		return theUnAssignedProjectMembers;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof ProjectMember) {
			ProjectMember theProjectMember = (ProjectMember) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theProjectMember.getId());
			}
		}
		return isEquals;
	}

	public boolean hasPrivilege(String aPrivilege) {
		boolean hasPrivilege = false;
		SecurityGroup theSecurityGroup = this.getSecurityGroup();
		if (theSecurityGroup != null) {
			hasPrivilege = theSecurityGroup.hasPrivilege(aPrivilege);
		}
		return hasPrivilege;
	}

	public static String encrypt(String aPassword) {
		String theHash = new BASE64Encoder().encode(aPassword.getBytes());
		return theHash;
	}

	public static String unencrypt(String aPassword) {
		String thePassword = null;
		try {
			byte[] theRaw = new BASE64Decoder().decodeBuffer(aPassword);
			thePassword = new String(theRaw, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thePassword;
	}

	public int compareTo(ProjectMember aProjectMember) {
		int theResult = -1;
		if (aProjectMember != null) {
			theResult = this.getUserId().compareTo(aProjectMember.getUserId());
		}
		return theResult;
	}
}
