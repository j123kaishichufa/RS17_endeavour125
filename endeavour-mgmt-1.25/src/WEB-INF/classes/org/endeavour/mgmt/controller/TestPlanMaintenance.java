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

import org.endeavour.mgmt.model.ITestPlanElement;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.TestCase;
import org.endeavour.mgmt.model.TestFolder;
import org.endeavour.mgmt.model.TestPlan;
import org.endeavour.mgmt.model.TestRun;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class TestPlanMaintenance extends ApplicationController implements ITestMaintenance {

	private List<String> errors = null;
	private Project project = null;
	private TestFolder testFolder = null;
	private TestPlan testPlan = null;
	private TestRun testRun = null;

	public static final String FOLDER = IViewConstants.RB.getString("folder.lbl");
	public static final String TEST_RUN = IViewConstants.RB.getString("test_case.lbl");
	public static final String NAME = TestPlan.NAME;
	public static final String CREATED_BY = TestPlan.CREATED_BY;
	public static final String TEST_PLAN_ELEMENTS = TestPlan.TEST_PLAN_ELEMENTS;

	public TestPlanMaintenance(Project aProject) {
		this.project = aProject;
		this.testPlan = new TestPlan(this.project);
	}

	// -- Test Plan Maintenance.

	public List<TestPlan> getTestPlans() {
		return this.project.getTestPlans();
	}

	public List<String> saveTestPlan(Map<String, Object> aData) {
		if (this.testPlan == null) {
			this.testPlan = new TestPlan(this.project);
		}
		if (this.isValid(aData)) {
			this.testPlan.save(aData);
			
			PersistenceManager.getInstance().attachToSession(this.project);
			PersistenceManager.getInstance().refresh(this.project);

			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void resetTestPlan() {
		this.testPlan = new TestPlan(this.project);
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteTestPlan(Integer aTestPlanId) {
		this.testPlan = this.project.retrieveTestPlanBy(aTestPlanId);
		this.testPlan.delete();
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.testPlan.validate(aData);
		return this.errors.isEmpty();
	}

	public Map<String, Object> getTestPlanDataBy(Integer aTestCaseId) {
		this.testPlan = this.project.retrieveTestPlanBy(aTestCaseId);
		return this.testPlan.getData();
	}

	public List<ITestPlanElement> getTestPlanData(Map<Integer, Integer> anIdToGridMappings, List<String> aExpandedRows) {
		Integer theRowId = 0;
		anIdToGridMappings.clear();
		List<ITestPlanElement> theTestPlanElements = new ArrayList<ITestPlanElement>();
		if (this.testPlan != null) {

			for (TestFolder theTestFolder : this.testPlan.getTestFolders()) {
				theTestPlanElements.add(theTestFolder);
				anIdToGridMappings.put(theRowId, theTestFolder.getId());
				theRowId++;

				if (aExpandedRows.contains(FOLDER + theTestFolder.getId())) {
					for (TestRun theTestRun : theTestFolder.getTestRuns()) {
						theTestPlanElements.add(theTestRun);
						anIdToGridMappings.put(theRowId, theTestRun.getId());
						theRowId++;
					}
				}
			}
		}
		return theTestPlanElements;
	}

	// -- Test Folder Maintenance.

	public void resetTestFolder() {
		this.testFolder = null;
	}

	public List<String> saveTestFolder(Map<String, Object> aData) {
		if (this.testFolder == null) {
			this.testFolder = new TestFolder(this.testPlan);
		}
		this.errors = this.testFolder.validate(aData);
		if (this.errors.isEmpty()) {
			this.testFolder.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void deleteTestFolder(Integer aTestFolderId) {
		this.testPlan.removeTestFolder(aTestFolderId);
	}

	public Map<String, Object> getTestFolderDataBy(Integer aTestFolderId) {
		this.testFolder = this.testPlan.retrieveTestFolderBy(aTestFolderId);
		return this.testFolder.getData();
	}

	public void resetTestFolders() {
		this.testPlan.resetTestFolders();
	}

	// -- Test Case Maintenance.

	public List<TestCase> getTestCases() {
		return null;
	}

	public Map<String, Object> getTestCaseDataBy(Integer aTestCaseId) {
		Map<String, Object> theData = null;
		this.testRun = this.testPlan.retrieveTestRunBy(aTestCaseId);
		TestCase theTestCase = this.testRun.getTestCase();
		if (theTestCase != null) {
			theData = theTestCase.getData();
			Map<String, Object> theTestRunData = this.testRun.getData();
			theData.put(TestRun.COMMENTS, theTestRunData.get(TestRun.COMMENTS));
			theData.put(TestRun.PROJECT_MEMBERS, theTestRunData.get(TestRun.PROJECT_MEMBERS));
			theData.put(TestRun.STATUS, theTestRunData.get(TestRun.STATUS));
		}
		return theData;
	}

	public List<String> saveTestCase(Map<String, Object> aData) {
		this.processProjectMemberIds(aData);
		if (this.isTestCaseValid(aData)) {
			this.testRun.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public boolean isTestCaseValid(Map<String, Object> aData) {
		this.errors = this.testRun.validate(aData);
		return this.errors.isEmpty();
	}

	public void deleteTestCase(Integer aTestRunId) {
		this.testPlan.removeTestRun(aTestRunId);
	}

	public void resetTestCase() {
	}

	public List<ProjectMember> getUnassignedProjectMembers() {
		return ProjectMember.getUnassignedProjectMembersFor(this.testRun, this.project);
	}

	public void addTestCase(Integer aTestCaseCaseId) {
		TestCase theTestCase = this.project.retrieveTestCaseBy(aTestCaseCaseId);
		TestRun theTestRun = new TestRun();
		theTestRun.setTestCase(theTestCase);
		this.testFolder.addTestRun(theTestRun);
		setChanged();
		notifyObservers();
	}

	public void removeUnsavedTestRunComments() {
		this.testPlan.removeUnsavedTestRunComments();
	}

	protected void processProjectMemberIds(Map<String, Object> aData) {
		List<ProjectMember> theProjectMembers = new ArrayList<ProjectMember>();
		List<Integer> theProjectMemberIds = (List) aData.get(PROJECT_MEMBERS);
		for (Integer theProjectMemberId : theProjectMemberIds) {
			ProjectMember theProjectMember = this.project.retrieveProjectMemberBy(theProjectMemberId);
			theProjectMembers.add(theProjectMember);
		}
		aData.put(PROJECT_MEMBERS, theProjectMembers);
	}

	public List<TestCase> filterUnassignedTestCases(List<TestCase> aTestCases) {
		List<TestCase> theCurrentTestCases = new ArrayList<TestCase>();

		for (TestFolder theTestFolder : this.testPlan.getTestFolders()) {
			for (TestRun theTestRun : theTestFolder.getTestRuns()) {
				theCurrentTestCases.add(theTestRun.getTestCase());
			}
		}

		List<TestCase> theAvailableTestCases = new ArrayList<TestCase>();
		for (TestCase theTestCase : aTestCases) {
			if (!theCurrentTestCases.contains(theTestCase)) {
				theAvailableTestCases.add(theTestCase);
			}
		}
		return theAvailableTestCases;
	}

	public String createPrintPreviewLocation() {
		String theLocation = null;
		Map<String, String> theReportParameters = new HashMap<String, String>();
		if (this.testPlan.getId() != null && this.project.getId() != null) {
			theReportParameters.put("test_plan_id_parameter", this.testPlan.getId() + ":Integer");
			theReportParameters.put("project_id_parameter", this.project.getId() + ":Integer");
			theLocation = super.createReportURL("test_plan.pdf", "test_plan.prpt", theReportParameters);
		}
		return theLocation;
	}

	public String createTestPlanExecutionReportLocation(Integer aTestPlan) {
		Map<String, String> theReportParameters = new HashMap<String, String>();
		theReportParameters.put("test_plan_id_parameter", aTestPlan + ":Integer");
		theReportParameters.put("project_id_parameter", this.project.getId() + ":Integer");
		return super.createReportURL("test_plan_execution.pdf", TEST_PLAN_EXECUTION_REPORT, theReportParameters);
	}

	public Integer getSelectedTestPlanId() {
		Integer theId = null;
		if (this.testPlan != null) {
			theId = this.testPlan.getId();
		}
		return theId;
	}
}
