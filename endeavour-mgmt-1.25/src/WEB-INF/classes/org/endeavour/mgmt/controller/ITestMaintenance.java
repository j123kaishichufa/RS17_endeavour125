package org.endeavour.mgmt.controller;

import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.TestCase;
import org.endeavour.mgmt.model.TestRun;

public interface ITestMaintenance {

	public static final String NAME = TestCase.NAME;
	public static final String CREATED_BY = TestCase.CREATED_BY;
	public static final String STEPS = TestCase.STEPS;
	public static final String PREREQUISITES = TestCase.PREREQUISITES;
	public static final String TEST_DATA = TestCase.TEST_DATA;
	public static final String PURPOSE = TestCase.PURPOSE;
	public static final String PROJECT_MEMBERS = TestRun.PROJECT_MEMBERS;
	public static final String STATUS = TestRun.STATUS;

	public List<TestCase> getTestCases();

	public Map<String, Object> getTestCaseDataBy(Integer aTestCaseId);

	public List<String> saveTestCase(Map<String, Object> aData);

	public void resetTestCase();

	public void deleteTestCase(Integer aTestCaseId);

	public List<ProjectMember> getUnassignedProjectMembers();

	public void startUnitOfWork();

	public void endUnitOfWork();
}