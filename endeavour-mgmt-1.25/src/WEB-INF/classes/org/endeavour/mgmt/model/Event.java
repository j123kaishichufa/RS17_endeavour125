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
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class Event {

	private Integer id = null;
	private String index = null;
	private String text = null;
	private List<Event> extensions = null;
	private Event parentEvent = null;
	private WorkProduct workProduct = null;
	private TestCase testCase = null;

	public static final String TEXT = "TEXT";

	public Event() {
	}

	public List<Event> getExtensions() {
		if (this.extensions == null) {
			this.extensions = new ArrayList<Event>();
		}
		return this.extensions;
	}

	public void setExtensions(List<Event> aExtensions) {
		this.extensions = aExtensions;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String aDescription) {
		this.text = aDescription;
	}

	public void addExtension(Event anExtension) {
		if (anExtension != null && !this.extensions.contains(anExtension)) {
			anExtension.setParentEvent(this);
			this.extensions.add(anExtension);
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public String getIndex() {
		return this.index;
	}

	public void setIndex(String aIndex) {
		this.index = aIndex;
	}

	public Event getParentEvent() {
		return this.parentEvent;
	}

	public void setParentEvent(Event aParentEvent) {
		this.parentEvent = aParentEvent;
	}

	public WorkProduct getWorkProduct() {
		return this.workProduct;
	}

	public void setWorkProduct(WorkProduct anWorkProduct) {
		this.workProduct = anWorkProduct;
	}

	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theText = (String) aData.get(TEXT);
		if (theText == null || theText.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("text_not_empty.msg"));
		}

		return theErrors;
	}

	public TestCase getTestCase() {
		return this.testCase;
	}

	public void setTestCase(TestCase aTestCase) {
		this.testCase = aTestCase;
	}

	public void save(Map<String, Object> aData) {
		Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(event.id) from " + Event.class.getSimpleName() + " event");
		if (theId == null) {
			theId = 0;
		} else {
			theId++;
		}
		this.setId(theId);

		String theText = (String) aData.get(TEXT);
		if (theText != null) {
			this.setText(theText);
		}
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Event) {
			Event theEvent = (Event) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theEvent.getId());
			}
		}
		return isEquals;
	}
}
