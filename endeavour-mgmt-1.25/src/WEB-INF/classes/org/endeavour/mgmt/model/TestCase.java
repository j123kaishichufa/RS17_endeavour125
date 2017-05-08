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

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class TestCase {

	private Integer id = null;
	private String name = null;
	private String purpose = null;
	private String prerequisites = null;
	private String testData = null;
	private String createdBy = null;
	private Project project = null;
	private List<Comment> comments = null;
	private List<Event> steps = null;
	private List<TestRun> testRuns = null;

	public static final String NAME = "NAME";
	public static final String CREATED_BY = "CREATED_BY";
	public static final String PURPOSE = "PURPOSE";
	public static final String PREREQUISITES = "PREREQUISITES";
	public static final String TEST_DATA = "TEST_DATA";
	public static final String COMMENTS = "COMMENTS";
	public static final String STEPS = "EVENTS";

	public TestCase() {
	}

	public TestCase(Project aProject) {
		this.setProject(aProject);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public void setCreatedBy(String aCreatedBy) {
		this.createdBy = aCreatedBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setProject(Project aProject) {
		this.project = aProject;
	}

	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String aPurpose) {
		this.purpose = aPurpose;
	}

	public String getPrerequisites() {
		return this.prerequisites;
	}

	public void setPrerequisites(String aPrerequisites) {
		this.prerequisites = aPrerequisites;
	}

	public String getTestData() {
		return this.testData;
	}

	public void setTestData(String aTestData) {
		this.testData = aTestData;
	}

	public List<Comment> getComments() {
		if (this.comments == null) {
			this.comments = new ArrayList<Comment>();
		}
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Event> getSteps() {
		if (this.steps == null) {
			this.steps = new ArrayList<Event>();
		}
		return steps;
	}

	public void setSteps(List<Event> aSteps) {
		this.steps = aSteps;
	}

	public List<TestRun> getTestRuns() {
		if (this.testRuns == null) {
			this.testRuns = new ArrayList<TestRun>();
		}
		return this.testRuns;
	}

	public void setTestRuns(List<TestRun> aTestRuns) {
		this.testRuns = aTestRuns;
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(COMMENTS, this.getComments());
		theData.put(PREREQUISITES, this.getPrerequisites());
		theData.put(PURPOSE, this.getPurpose());
		theData.put(TEST_DATA, this.getTestData());
		theData.put(STEPS, this.getSteps());
		theData.put(CREATED_BY, this.getCreatedBy());
		return theData;
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(testCase.id) from " + TestCase.class.getSimpleName() + " testCase");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		this.getProject().addTestCase(this);

		String theName = (String) aData.get(NAME);
		if (theName != null) {
			this.setName(theName);
		}

		String theCreatedBy = (String) aData.get(CREATED_BY);
		if (theCreatedBy != null) {
			this.setCreatedBy(theCreatedBy);
		}

		String thePurpose = (String) aData.get(PURPOSE);
		if (thePurpose != null) {
			this.setPurpose(thePurpose);
		}

		String thePrerequisites = (String) aData.get(PREREQUISITES);
		if (thePrerequisites != null) {
			this.setPrerequisites(thePrerequisites);
		}

		String theTestData = (String) aData.get(TEST_DATA);
		if (theTestData != null) {
			this.setTestData(theTestData);
		}

		List<Comment> theComments = (List) aData.get(COMMENTS);
		if (theComments != null) {
			this.updateComments(theComments);
		}

		List<Event> theSteps = (List) aData.get(STEPS);
		if (theSteps != null) {
			this.updateSteps(theSteps);
		}
	}

	private void updateSteps(List<Event> aSteps) {

		List<Event> theSteps = this.getSteps();
		for (Event theStep : theSteps) {
			if (theStep != null && !aSteps.contains(theStep)) {
				theSteps.set(theSteps.indexOf(theStep), null);
				theStep.delete();
			}
		}
		theSteps.clear();

		Integer theEventId = (Integer) PersistenceManager.getInstance().findBy("select max(event.id) from " + Event.class.getSimpleName() + " event");
		theEventId = theEventId == null ? 0 : theEventId;
		for (Event theEvent : aSteps) {
			if (theEvent.getId() < 0) {
				theEventId++;
				theEvent.setId(theEventId);
			}
			this.addStep(theEvent);
		}
	}

	private void addStep(Event aStep) {
		List<Event> theSteps = this.getSteps();
		if (!theSteps.contains(aStep)) {
			aStep.setTestCase(this);
			theSteps.add(aStep);
		}
	}

	private void updateComments(List<Comment> aComments) {

		List<Comment> theComments = this.getComments();
		for (Comment theComment : this.getComments()) {
			if (theComment != null && !aComments.contains(theComment)) {
				theComments.set(theComments.indexOf(theComment), null);
				theComment.delete();
			}
		}
		theComments.clear();
		for (Comment theComment : aComments) {
			if (theComment != null) {
				this.updateTransientCommentId(theComment);
				this.addComment(theComment);
			}
		}
	}

	private void addComment(Comment aComment) {
		List<Comment> theComments = this.getComments();
		if (!theComments.contains(aComment)) {
			aComment.setTestCase(this);
			theComments.add(aComment);
		}
	}

	private void updateTransientCommentId(Comment aComment) {
		if (aComment.getId() < 0) {
			aComment.setId(null);
			aComment.save(new HashMap<String, Object>());
		}
	}

	public void delete() {
		this.getProject().removeTestCase(this);
		PersistenceManager.getInstance().delete(this);
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		String thePurpose = (String) aData.get(PURPOSE);
		if (thePurpose == null || thePurpose.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("purpose_not_empty.msg"));
		}

		List<Event> theSteps = (List) aData.get(STEPS);
		if (theSteps == null || theSteps.size() == 0) {
			theErrors.add(IViewConstants.RB.getString("steps_not_empty.msg"));
		}

		return theErrors;
	}

	public Project getProject() {
		return this.project;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof TestCase) {
			TestCase theTestCase = (TestCase) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theTestCase.getId());
			}
		}
		return isEquals;
	}

	public void removeTestRun(TestRun aTestRun) {
		List<TestRun> theTestRuns = this.getTestRuns();
		if (theTestRuns.contains(aTestRun)) {
			theTestRuns.remove(aTestRun);
		}
	}
}
