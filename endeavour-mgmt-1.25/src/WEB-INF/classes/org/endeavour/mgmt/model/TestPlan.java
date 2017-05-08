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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class TestPlan implements ITestPlanElement {

	private Integer id = null;
	private String name = null;
	private String folders = null;
	private String createdBy = null;
	private List<TestRun> testRuns = null;
	private Project project = null;
	private List<TestFolder> testFolders = null;

	public static final String NAME = "NAME";
	public static final String CREATED_BY = "CREATED_BY";
	public static final String LABEL = "Test Plan";
	public static final String TEST_PLAN_ELEMENTS = "TEST_PLAN_ELEMENTS";

	public TestPlan() {
		this.folders = "";
	}

	public TestPlan(Project aProject) {
		this.setProject(aProject);
	}

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

	public String getFolders() {
		return this.folders;
	}

	public void setFolders(String aFolders) {
		this.folders = aFolders;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<TestRun> getTestRuns() {
		if (this.testRuns == null) {
			this.testRuns = new ArrayList<TestRun>();
		}
		return this.testRuns;
	}

	public void setTestRuns(List<TestRun> testRuns) {
		this.testRuns = testRuns;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();
		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}
		return theErrors;
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(testPlan.id) from " + TestPlan.class.getSimpleName() + " testPlan");
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

		String theCreatedBy = (String) aData.get(CREATED_BY);
		if (theCreatedBy != null) {
			this.setCreatedBy(theCreatedBy);
		}

		List<ITestPlanElement> theTestPlanElements = (List) aData.get(TEST_PLAN_ELEMENTS);
		this.updateTestRuns(theTestPlanElements);
		this.updateTestFolders(theTestPlanElements);
		this.updateTestRunComments(this.getTestRuns());

		this.getProject().addTestPlan(this);
		PersistenceManager.getInstance().save(this);
	}

	public void removeUnsavedTestRunComments() {
		for (TestRun theTestRun : this.getTestRuns()) {
			if (theTestRun != null) {
				theTestRun.removeUnsavedComments();
			}
		}
	}

	private void updateTestRunComments(List<TestRun> aTestRuns) {

		Integer theCommentId = (Integer) PersistenceManager.getInstance().findBy("select max(comment.id) from " + Comment.class.getSimpleName() + " comment");
		theCommentId = theCommentId == null ? 0 : theCommentId;

		for (TestRun theTestRun : aTestRuns) {
			if (theTestRun != null) {
				for (Comment theComment : theTestRun.getComments()) {
					if (theComment != null && theComment.getId() < 0) {
						theCommentId++;
						theComment.setId(theCommentId);
					}
				}
			}
		}

	}

	private void updateTestRuns(List<ITestPlanElement> aTestPlanElements) {

		List<TestRun> theNewTestRuns = new ArrayList<TestRun>();
		for (ITestPlanElement theTestPlanElement : aTestPlanElements) {
			if (theTestPlanElement != null && theTestPlanElement instanceof TestFolder) {
				TestFolder theTestFolder = (TestFolder) theTestPlanElement;
				theNewTestRuns.addAll(theTestFolder.getTestRuns());
			}
		}

		Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(testRun.id) from " + TestRun.class.getSimpleName() + " testRun");
		theId = theId == null ? 0 : theId;

		List<TestRun> theCurrentTestRuns = this.getTestRuns();
		for (TestRun theCurrentTestRun : theCurrentTestRuns) {
			if (theCurrentTestRun != null && !theNewTestRuns.contains(theCurrentTestRun)) {
				theCurrentTestRuns.set(theCurrentTestRuns.indexOf(theCurrentTestRun), null);
				theCurrentTestRun.delete();
			}
		}
		theCurrentTestRuns.clear();
		for (TestRun theNewTestRun : theNewTestRuns) {
			this.addTestRun(theNewTestRun);
			if (theNewTestRun.getId() < 0) {
				theId++;
				theNewTestRun.setId(theId);
			}
		}
	}

	private void updateTestFolders(List<ITestPlanElement> aTestPlanElements) {
		StringBuffer theFolders = new StringBuffer();
		for (ITestPlanElement theTestPlanElement : aTestPlanElements) {
			if (theTestPlanElement != null && theTestPlanElement instanceof TestFolder) {
				theFolders.append(theTestPlanElement.getName());
				theFolders.append("|");
			}
		}
		this.setFolders(theFolders.toString());
	}

	public void delete() {
		this.getProject().removeTestPlan(this);
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(CREATED_BY, this.getCreatedBy());
		return theData;
	}

	public List<TestFolder> getTestFolders() {
		if (this.testFolders == null) {
			this.testFolders = this.createTestFolders();
		}
		return this.testFolders;
	}

	private List<TestFolder> createTestFolders() {
		List<TestFolder> theTestFolders = new ArrayList<TestFolder>();
		String theFolders = this.getFolders();
		int theId = 0;
		if (theFolders != null && theFolders.trim().length() > 0) {
			StringTokenizer theTokens = new StringTokenizer(theFolders, "|");
			while (theTokens.hasMoreTokens()) {
				String theFolder = (String) theTokens.nextElement();
				TestFolder theTestFolder = new TestFolder(theId, theFolder, this);

				for (TestRun theTestRun : this.getTestRuns()) {
					if (theTestRun != null && theFolder.equals(theTestRun.getFolder())) {
						theTestFolder.addTestRun(theTestRun);
					}
				}

				theTestFolders.add(theTestFolder);
				theId++;
			}
		}
		return theTestFolders;
	}

	public void removeTestFolder(Integer aTestFolderId) {
		TestFolder theTestFolder = this.retrieveTestFolderBy(aTestFolderId);
		this.testFolders.remove(theTestFolder);
	}

	public void removeTestRun(Integer aTestRunId) {
		TestRun theTestRun = null;
		for (TestFolder theTestFolder : this.getTestFolders()) {
			List<TestRun> theTestRuns = theTestFolder.getTestRuns();
			for (TestRun theCurrentTestRun : theTestRuns) {
				if (theCurrentTestRun != null) {
					if (theCurrentTestRun.getId().equals(aTestRunId)) {
						theTestRun = theCurrentTestRun;
						break;
					}
				}
			}
			if (theTestRun != null) {
				if (theTestRuns.contains(theTestRun)) {
					theTestRuns.remove(theTestRun);
				}
				break;
			}
		}
	}

	public TestRun retrieveTestRunBy(Integer aTestRunId) {
		TestRun theTestRun = null;
		main: for (TestFolder theTestFolder : this.getTestFolders()) {
			for (TestRun theCurrentTestRun : theTestFolder.getTestRuns()) {
				if (theCurrentTestRun.getId().equals(aTestRunId)) {
					theTestRun = theCurrentTestRun;
					break main;
				}
			}
		}
		return theTestRun;
	}

	public void addTestFolder(TestFolder aTestFolder) {
		List<TestFolder> theTestFolders = this.getTestFolders();
		if (!theTestFolders.contains(aTestFolder)) {
			aTestFolder.setId(theTestFolders.size() > 0 ? theTestFolders.size() + 1 : 0);
			theTestFolders.add(aTestFolder);
		}
	}

	public void resetTestFolders() {
		this.testFolders = null;
	}

	public void addTestRun(TestRun aTestRun) {
		List<TestRun> theTestRuns = this.getTestRuns();
		if (!theTestRuns.contains(aTestRun)) {
			theTestRuns.add(aTestRun);
			aTestRun.setTestPlan(this);
		}
	}

	public TestFolder retrieveTestFolderBy(Integer aTestFolderId) {
		TestFolder theTestFolder = null;
		for (TestFolder theCurrentTestFolder : this.getTestFolders()) {
			if (theCurrentTestFolder.getId().equals(aTestFolderId)) {
				theTestFolder = theCurrentTestFolder;
				break;
			}
		}
		return theTestFolder;
	}

	public String getStatus() {
		return null;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("test_plan.lbl");
	}
}
