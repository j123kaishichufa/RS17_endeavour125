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

import org.endeavour.mgmt.model.Dependency;
import org.endeavour.mgmt.model.Task;

public class DependencyMaintenance extends ApplicationController {

	private Dependency dependency = null;
	private List<Dependency> dependencies = null;
	private List<String> errors = null;
	private ITaskMaintenance taskMaintenance = null;

	public static final String TYPE = Dependency.TYPE;
	public static final String PREDECESSOR = Dependency.PREDECESSOR;

	public DependencyMaintenance(ITaskMaintenance aTaskMaintenance) {
		this.taskMaintenance = aTaskMaintenance;
		this.dependencies = new ArrayList<Dependency>();
	}

	public List<Dependency> getDependencies() {
		return this.dependencies;
	}

	public void setDependencies(List<Dependency> aDependencies) {
		this.dependencies = new ArrayList<Dependency>();
		for (Dependency theDependency : aDependencies) {
			if (theDependency != null) {
				this.dependencies.add(theDependency);
			}
		}
	}

	public List<Task> getAvailablePredecessors(boolean isEditing) {

		/*
		 * Filters out Tasks that are the same as the current sucessor or that
		 * have been used by the current sucessor as predecessor already. The
		 * Tasks to be filtered might come from the Project or from the UseCase
		 * but not from both at the same time.
		 */

		List<Task> theResults = new ArrayList<Task>();
		for (Task theTask : this.taskMaintenance.getTasks()) {
			if (theTask != null) {
				if (!theTask.getId().equals(this.taskMaintenance.getSelectedWorkProductId())) {
					theResults.add(theTask);
				}

				if (!isEditing) {
					for (Dependency theDependency : this.dependencies) {
						if (theDependency != null && theDependency.getPredecessor().getId().equals(theTask.getId())) {
							theResults.remove(theTask);
						}
					}
				}
			}
		}
		return theResults;
	}

	public void deleteDependency(Integer aDependencyId) {
		Dependency theDependency = null;
		for (Dependency theCurrentDependency : this.dependencies) {
			if (theCurrentDependency.getId().equals(aDependencyId)) {
				theDependency = theCurrentDependency;
				break;
			}
		}
		if (theDependency != null) {
			this.dependencies.remove(theDependency);
			setChanged();
			notifyObservers();
		}
	}

	public Integer getSelectedDependencyId() {
		Integer theId = null;
		if (this.dependency != null) {
			theId = this.dependency.getId();
		}
		return theId;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.dependency.validate(aData);
		return this.errors.isEmpty();
	}

	public List<String> saveDependency(Map<String, Object> aData) {
		if (this.dependency == null) {
			this.dependency = new Dependency();
		}
		if (this.isValid(aData)) {
			Integer theId = this.dependency.getId();
			this.dependency.save(aData);
			this.dependency.setId(theId == null ? this.produceTransientDependencyId() : theId);
			this.addDependency(this.dependency);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	private void addDependency(Dependency aDependency) {
		if (!this.dependencies.contains(aDependency)) {
			this.dependencies.add(aDependency);
		}
	}

	public Map<String, Object> getDependencyDataBy(Integer aDependencyId) {
		Map<String, Object> theData = null;
		for (Dependency theDependency : this.getDependencies()) {
			if (theDependency != null) {
				if (theDependency.getId().equals(aDependencyId)) {
					this.dependency = theDependency;
					theData = theDependency.getData();
					break;
				}
			}
		}

		for (Dependency theDependency : this.getDependencies()) {
			if (theDependency != null) {
				if (theDependency.getId().equals(aDependencyId)) {
					this.dependency = theDependency;
					theData = theDependency.getData();
					break;
				}
			}
		}
		return theData;
	}

	private Integer produceTransientDependencyId() {
		Integer theTransientId = 0;
		for (Dependency theDependency : this.dependencies) {
			int theId = theDependency.getId();
			if (theId < 0) {
				theId = theId - (theId * 2);
			}
			if (theId > theTransientId) {
				theTransientId = theId;
			}
		}
		theTransientId++;
		theTransientId = theTransientId - (theTransientId * 2);
		return theTransientId;
	}

	public void reset() {
		this.dependency = null;
	}
}
