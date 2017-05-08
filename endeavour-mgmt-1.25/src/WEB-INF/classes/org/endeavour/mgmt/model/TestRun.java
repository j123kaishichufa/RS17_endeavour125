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

@SuppressWarnings("unchecked")
public class TestRun implements ITestPlanElement {

	private Integer id = null;
	private String status = null;
	private Date executionDate = null;
	private String folder = null;
	private TestPlan testPlan = null;
	private TestCase testCase = null;
	private List<Comment> comments = null;
	private Set<ProjectMember> projectMembers = null;

	public static final String STATUS = "STATUS";
	public static final String COMMENTS = "COMMENTS";
	public static final String PROJECT_MEMBERS = "PROJECT_MEMBERS";
	public static final String LABEL = "Test Case";

	public TestRun() {
	}

	public String getName() {
		return this.testCase.getName();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String aStatus) {
		this.status = aStatus;
	}

	public Date getExecutionDate() {
		return this.executionDate;
	}

	public void setExecutionDate(Date aExecutionDate) {
		this.executionDate = aExecutionDate;
	}

	public String getFolder() {
		return this.folder;
	}

	public void setFolder(String aFolder) {
		this.folder = aFolder;
	}

	public TestPlan getTestPlan() {
		return this.testPlan;
	}

	public void setTestPlan(TestPlan aTestPlan) {
		this.testPlan = aTestPlan;
	}

	public TestCase getTestCase() {
		return this.testCase;
	}

	public void setTestCase(TestCase aTestCase) {
		this.testCase = aTestCase;
	}

	public Set<ProjectMember> getProjectMembers() {
		if (this.projectMembers == null) {
			this.projectMembers = new HashSet<ProjectMember>();
		}
		return this.projectMembers;
	}

	public void setProjectMembers(Set<ProjectMember> aProjectMebmers) {
		this.projectMembers = aProjectMebmers;
	}

	public List<Comment> getComments() {
		if (this.comments == null) {
			this.comments = new ArrayList<Comment>();
		}
		return this.comments;
	}

	public void setComments(List<Comment> aComments) {
		this.comments = aComments;
	}

	public List<String> validate(Map<String, Object> aData) {
		return new ArrayList<String>();
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(testRun.id) from " + TestRun.class.getSimpleName() + " testRun");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		List<ProjectMember> theProjectMembers = (List) aData.get(PROJECT_MEMBERS);
		if (theProjectMembers != null) {
			this.updateProjectMembers(theProjectMembers);
		}

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
			this.setExecutionDate(new Date());
		}

		List<Comment> theComments = (List) aData.get(COMMENTS);
		if (theComments != null) {
			this.updateComments(theComments);
		}
	}

	public void removeUnsavedComments() {
		List<Comment> theComments = this.getComments();
		for (Comment theComment : theComments) {
			if (theComment != null && theComment.getId() < 0) {
				theComments.set(theComments.indexOf(theComment), null);
			}
		}
	}

	protected void addComment(Comment aComment) {
		List<Comment> theComments = this.getComments();
		if (!theComments.contains(aComment)) {
			aComment.setTestRun(this);
			theComments.add(aComment);
		}
	}

	private void updateComments(List<Comment> aComments) {
		List<Comment> theComments = this.getComments();
		for (Comment theComment : theComments) {
			if (theComment != null && !aComments.contains(theComment)) {
				theComments.set(theComments.indexOf(theComment), null);
				theComment.delete();
			}
		}
		theComments.clear();
		for (Comment theComment : aComments) {
			if (theComment != null) {
				this.addComment(theComment);
			}
		}
	}

	private void updateProjectMembers(List<ProjectMember> aProjectMembers) {

		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		for (ProjectMember theProjectMember : this.getProjectMembers()) {
			if (theProjectMember != null && !aProjectMembers.contains(theProjectMember)) {
				theProjectMember.removeTestRun(this);
			}
		}
		theProjectMembers.clear();
		for (ProjectMember theProjectMember : aProjectMembers) {
			if (theProjectMember != null) {
				this.addProjectMember(theProjectMember);
			}
		}
	}

	private void addProjectMember(ProjectMember aProjectMember) {
		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		if (!theProjectMembers.contains(aProjectMember)) {
			theProjectMembers.add(aProjectMember);
			aProjectMember.addTestRun(this);
		}
	}

	public void delete() {
		getTestCase().removeTestRun(this);
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(STATUS, this.getStatus());
		theData.put(COMMENTS, this.getComments());
		List theProjectMembers = new ArrayList<ProjectMember>();
		theProjectMembers.addAll(this.getProjectMembers());
		theData.put(PROJECT_MEMBERS, theProjectMembers);
		return theData;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof TestRun) {
			TestRun theTestRun = (TestRun) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theTestRun.getId());
			}
		}
		return isEquals;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("test_case.lbl");
	}
}
