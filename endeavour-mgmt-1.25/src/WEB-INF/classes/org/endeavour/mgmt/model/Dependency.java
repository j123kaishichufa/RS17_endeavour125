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

public class Dependency {

	private Integer id = null;
	private String type = null;
	private Task sucessor = null;
	private Task predecessor = null;

	public static final String ID = "ID";
	public static final String TYPE = "TYPE";
	public static final String PREDECESSOR = "PREDECESSOR";

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String aType) {
		this.type = aType;
	}

	public Task getSucessor() {
		return this.sucessor;
	}

	public void setSucessor(Task aSucessor) {
		this.sucessor = aSucessor;
	}

	public void save(Map<String, Object> aData) {

		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(dependency.id) from " + Dependency.class.getSimpleName() + " dependency");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		String theType = (String) aData.get(TYPE);
		if (theType != null) {
			this.setType(theType);
		}

		Task thePredecessor = (Task) aData.get(PREDECESSOR);
		if (thePredecessor != null) {
			this.setPredecessor(thePredecessor);
		}
	}

	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(TYPE, this.getType());
		theData.put(PREDECESSOR, this.getPredecessor() != null ? this.getPredecessor().getId() : -1);
		return theData;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		Task thePredecessor = (Task) aData.get(PREDECESSOR);
		if (thePredecessor == null) {
			theErrors.add(IViewConstants.RB.getString("predecessor_must_be_selected.msg"));
		}
		return theErrors;
	}

	public Task getPredecessor() {
		return this.predecessor;
	}

	public void setPredecessor(Task aTask) {
		this.predecessor = aTask;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Dependency) {
			Dependency theDependency = (Dependency) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theDependency.getId());
			}
		}
		return isEquals;
	}
}
