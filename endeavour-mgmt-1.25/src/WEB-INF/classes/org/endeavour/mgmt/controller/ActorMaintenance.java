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

import org.endeavour.mgmt.model.Actor;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.UseCase;

public class ActorMaintenance extends ApplicationController {

	private Actor actor = null;
	private Project project = null;
	private List<String> errors = null;

	public static final String NAME = Actor.NAME;
	public static final String DESCRIPTION = Actor.DESCRIPTION;

	public ActorMaintenance(Project aProject) {
		this.project = aProject;
	}

	public List<Actor> getActors() {
		return this.project.getActors();
	}

	public Map<String, Object> getActorDataBy(Integer aActorId) {
		this.actor = this.project.retrieveActorBy(aActorId);
		return this.actor.getData();
	}

	public List<Actor> getActorsBy(UseCase aUseCase, boolean returnAssigned) {

		List<Actor> theResultActors = new ArrayList<Actor>();
		for (Actor theActor : this.project.getActors()) {
			if (theActor != null) {
				if (returnAssigned ? theActor.isAssignedTo(aUseCase) : !theActor.isAssignedTo(aUseCase)) {
					theResultActors.add(theActor);
				}
			}
		}
		return theResultActors;
	}

	public List<String> saveActor(Map<String, Object> aData) {

		if (this.actor == null) {
			this.actor = new Actor(this.project);
		}
		if (this.isValid(aData)) {
			this.actor.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void reset() {
		this.actor = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteActor(Integer aActorId) {

		this.actor = this.project.retrieveActorBy(aActorId);
		this.actor.delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.actor.validate(aData);
		return this.errors.isEmpty();
	}

	public Integer getSelectedActorId() {
		Integer theId = null;
		if (this.actor != null) {
			theId = this.actor.getId();
		}
		return theId;
	}
}
