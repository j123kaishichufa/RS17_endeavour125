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

public class GlossaryTerm {

	private Integer id = null;
	private String name = null;
	private String description = null;
	private String createdBy = null;
	private Project project = null;

	public static final String NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String CREATED_BY = "CREATED_BY";

	public GlossaryTerm() {
	}

	public GlossaryTerm(Project aProject) {
		this.setProject(aProject);
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project aProject) {
		this.project = aProject;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String aCreatedBy) {
		this.createdBy = aCreatedBy;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription == null || theDescription.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("description_not_empty.msg"));
		}
		return theErrors;
	}

	public void save(Map<String, Object> aData) {

		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(glossaryTerm.id) from " + GlossaryTerm.class.getSimpleName() + " glossaryTerm");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		this.getProject().addGlossaryTerm(this);

		String theName = (String) aData.get(NAME);
		if (theName != null) {
			this.setName(theName);
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription != null) {
			this.setDescription(theDescription);
		}

		String theCreatedBy = (String) aData.get(CREATED_BY);
		if (theCreatedBy != null) {
			this.setCreatedBy(theCreatedBy);
		}
	}

	public void delete() {
		this.getProject().removeGlossaryTerm(this);
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(DESCRIPTION, this.getDescription());
		theData.put(CREATED_BY, this.getCreatedBy());
		return theData;
	}
}
