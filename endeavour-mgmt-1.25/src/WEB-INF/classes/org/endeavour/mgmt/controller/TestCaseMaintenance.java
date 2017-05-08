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

import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.TestCase;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class TestCaseMaintenance extends ApplicationController implements ITestMaintenance {

	private TestCase testCase = null;
	private List<String> errors = null;
	private Project project = null;

	public TestCaseMaintenance(Project aProject) {
		this.project = aProject;
	}

	public List<TestCase> getTestCases() {
		return this.project.getTestCases();
	}

	public List<TestCase> getTestCasesBy(String aNumber, String aName) {
		Integer theProjectId = this.project.getId();
		String theQuery = "select testCase from " + TestCase.class.getSimpleName() + " testCase where ";

		List<Object> theParameters = new ArrayList<Object>();

		theParameters.add("%" + aName + "%");
		theQuery = theQuery + "testCase.name like ? and testCase.project.id = " + theProjectId;

		try {
			theParameters.add(new Integer(aNumber));
			theQuery = theQuery + " and testCase.id = ?";
		} catch (NumberFormatException e) {

		}

		List<TestCase> theTestCases = PersistenceManager.getInstance().findAllBy(theQuery, theParameters);
		return theTestCases;
	}

	public Map<String, Object> getTestCaseDataBy(Integer aTestCaseId) {
		this.testCase = this.project.retrieveTestCaseBy(aTestCaseId);
		return this.testCase.getData();
	}

	public List<String> saveTestCase(Map<String, Object> aData) {

		if (this.testCase == null) {
			this.testCase = new TestCase(this.project);
		}
		if (this.isValid(aData)) {
			this.testCase.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void resetTestCase() {
		this.testCase = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteTestCase(Integer aTestCaseId) {

		this.testCase = this.project.retrieveTestCaseBy(aTestCaseId);
		this.testCase.delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.testCase.validate(aData);
		return this.errors.isEmpty();
	}

	public List<ProjectMember> getUnassignedProjectMembers() {
		return null;
	}

	public Integer getSelectedTestCaseId() {
		Integer theId = null;
		if (this.testCase != null) {
			theId = this.testCase.getId();
		}
		return theId;
	}
}
