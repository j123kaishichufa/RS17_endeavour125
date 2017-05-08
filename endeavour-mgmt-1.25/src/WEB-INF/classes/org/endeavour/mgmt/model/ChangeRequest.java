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

import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class ChangeRequest extends WorkProduct {

	private String type = null;

	public static final String TYPE = "TYPE";
	public static final String LABEL = "Change Request";

	public ChangeRequest() {
		super.setLabel(LABEL);
	}

	public ChangeRequest(Project aProject) {
		this();
		super.setProject(aProject);
	}

	public void delete() {
		super.delete();
	}

	public void save(Map<String, Object> aData) {
		super.save(aData);

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
		}

		String theType = (String) aData.get(TYPE);
		if (theType != null) {
			this.setType(theType);
		}

		super.getProject().addWorkProduct(this);
		PersistenceManager.getInstance().save(this);
	}

	public Map<String, Object> getData() {

		Map<String, Object> theData = super.getData();
		theData.put(TYPE, this.getType());
		return theData;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = super.validate(aData);
		return theErrors;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String aType) {
		this.type = aType;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("change_request.lbl");
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof ChangeRequest) {
			ChangeRequest theChangeRequest = (ChangeRequest) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theChangeRequest.getId());
			}
		}
		return isEquals;
	}
}
