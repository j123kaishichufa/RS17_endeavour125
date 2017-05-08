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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class Actor implements Comparable<Actor> {

	private Integer id = null;
	private String name = null;
	private String description = null;
	private Set<UseCase> useCases = null;
	private Project project = null;

	public static final String NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";

	public Actor() {
	}

	public Actor(Project aProject) {
		this.project = aProject;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project aProject) {
		this.project = aProject;
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

	public Set<UseCase> getUseCases() {
		if (this.useCases == null) {
			this.useCases = new HashSet<UseCase>();
		}
		return this.useCases;
	}

	public void setUseCases(Set<UseCase> aUseCases) {
		this.useCases = aUseCases;
	}

	public void removeUseCase(UseCase aUseCase) {
		Set<UseCase> theUseCases = this.getUseCases();
		if (theUseCases.contains(aUseCase)) {
			theUseCases.remove(aUseCase);
		}
	}

	public void addUseCase(UseCase aUseCase) {
		Set<UseCase> theUseCases = this.getUseCases();
		if (!theUseCases.contains(aUseCase)) {
			theUseCases.add(aUseCase);
		}
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(actor.id) from " + Actor.class.getSimpleName() + " actor");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}

		String theName = (String) aData.get(NAME);
		if (theName != null) {
			this.setName(theName);
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription != null) {
			this.setDescription(theDescription);
		}

		this.project.addActor(this);
	}

	public void delete() {
		this.project.removeActor(this);

		for (Iteration theIteration : this.project.getIterations()) {
			if (theIteration != null) {
				for (WorkProduct theWorkProduct : theIteration.getWorkProducts()) {
					if (theWorkProduct != null && theWorkProduct instanceof UseCase) {
						UseCase theUseCase = (UseCase) theWorkProduct;
						theUseCase.removeActor(this);
					}
				}
			}
		}
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(DESCRIPTION, this.getDescription());
		return theData;
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

	public boolean isAssignedTo(UseCase aUseCase) {
		Set<UseCase> theUseCases = this.getUseCases();
		return theUseCases.contains(aUseCase);
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Actor) {
			Actor theActor = (Actor) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theActor.getId());
			}
		}
		return isEquals;
	}

	public int compareTo(Actor anActor) {
		int theResult = -1;
		if (anActor != null) {
			theResult = this.getName().compareTo(anActor.getName());
		}
		return theResult;
	}
}
