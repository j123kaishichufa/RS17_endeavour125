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

import org.endeavour.mgmt.view.IViewConstants;

public class TestFolder implements ITestPlanElement {

	private Integer id = null;
	private String name = null;
	private List<TestRun> testRuns = null;
	private TestPlan testPlan = null;

	public static final String NAME = "NAME";
	public static final String LABEL = "Folder";

	public TestFolder(TestPlan aTestPlan) {
		this.testPlan = aTestPlan;
	}

	public TestFolder(Integer anId, String aName, TestPlan aTestPlan) {
		this.id = anId;
		this.name = aName;
		this.testPlan = aTestPlan;
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

	public List<TestRun> getTestRuns() {
		if (this.testRuns == null) {
			this.testRuns = new ArrayList<TestRun>();
		}
		return this.testRuns;
	}

	public void save(Map<String, Object> aData) {
		String theName = (String) aData.get(NAME);
		this.setName(theName);
		this.updateTestRuns();
		this.testPlan.addTestFolder(this);
	}

	private void updateTestRuns() {
		for (TestRun theTestRun : this.getTestRuns()) {
			theTestRun.setFolder(this.getName());
		}
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		return theErrors;
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		return theData;
	}

	public void addTestRun(TestRun aTestRun) {
		List<TestRun> theTestRuns = getTestRuns();
		int theId = -1;
		if (!theTestRuns.contains(aTestRun)) {
			if (aTestRun.getId() == null) {
				theId = theTestRuns.size() > 0 ? theTestRuns.size() + 1 : 1;
				theId = new Integer(this.getId().toString() + theId);
				theId = theId - (theId * 2);
				aTestRun.setId(theId);
			}
			aTestRun.setFolder(this.getName());
			theTestRuns.add(aTestRun);
		}
	}

	public String getStatus() {
		return "";
	}

	public String getElementType() {
		return IViewConstants.RB.getString("folder.lbl");
	}
}
