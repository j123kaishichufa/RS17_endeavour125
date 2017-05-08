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
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class Comment {

	public static final String TEXT = "TEXT";
	public static final String AUTHOR = "AUTHOR";
	public static final String DATE = "DATE";

	private Integer id = null;
	private String text = null;
	private String author = null;
	private Date date = null;
	private WorkProduct workProduct = null;
	private TestCase testCase = null;
	private TestRun testRun = null;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String aText) {
		this.text = aText;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String anAuthor) {
		this.author = anAuthor;
	}

	public WorkProduct getWorkProduct() {
		return this.workProduct;
	}

	public void setWorkProduct(WorkProduct anWorkProduct) {
		this.workProduct = anWorkProduct;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date aDate) {
		this.date = aDate;
	}

	public TestCase getTestCase() {
		return this.testCase;
	}

	public void setTestCase(TestCase aTestCase) {
		this.testCase = aTestCase;
	}

	public TestRun getTestRun() {
		return this.testRun;
	}

	public void setTestRun(TestRun aTestRun) {
		this.testRun = aTestRun;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theText = (String) aData.get(TEXT);
		if (theText == null) {
			theErrors.add(IViewConstants.RB.getString("text_not_empty.msg"));
		}
		return theErrors;
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(comment.id) from " + Comment.class.getSimpleName() + " comment");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		String theText = (String) aData.get(TEXT);
		if (theText != null) {
			this.setText(theText);
		}

		String theAuthor = (String) aData.get(AUTHOR);
		if (theAuthor != null) {
			this.setAuthor(theAuthor);
		}

		Date theDate = (Date) aData.get(DATE);
		if (theDate != null) {
			this.setDate(theDate);
		}
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(TEXT, this.getText());
		theData.put(AUTHOR, this.getAuthor());
		theData.put(DATE, this.getDate());
		return theData;
	}

	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Comment) {
			Comment theComment = (Comment) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theComment.getId());
			}
		}
		return isEquals;
	}
}
