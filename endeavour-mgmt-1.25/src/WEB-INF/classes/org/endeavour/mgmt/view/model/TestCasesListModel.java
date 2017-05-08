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

package org.endeavour.mgmt.view.model;

import java.util.List;

import org.endeavour.mgmt.model.TestCase;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class TestCasesListModel extends GridBoxModel {

	public TestCasesListModel() {
		super();
	}

	public TestCasesListModel(List<TestCase> aTestCases) {
		super(aTestCases);
	}

	public void initializeColumns() {
		String theNumber = IViewConstants.RB.getString("number.lbl");
		String theTestCaseName = IViewConstants.RB.getString("test_case_name.lbl");
		String thePurpose = IViewConstants.RB.getString("purpose.lbl");

		super.columns = new String[] { theNumber, theTestCaseName, thePurpose };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		TestCase theTestCase = (TestCase) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = IViewConstants.RB.getString("test_case_initials.lbl") + theTestCase.getId().toString();
			break;
		case 1:
			theValue = theTestCase.getName();
			break;
		case 2:
			theValue = theTestCase.getPurpose();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		TestCase theTestCase = (TestCase) this.rows.get(aRowIndex);
		return theTestCase.getId();
	}
}