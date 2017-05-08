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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.Actor;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

@SuppressWarnings("unchecked")
public class UseCaseMaintenance extends WorkProductMaintenance {

	private List<String> errors = null;

	public static final String TASKS = UseCase.TASKS;
	public static final String EVENTS = UseCase.EVENTS;
	public static final String EXTENSIONS = UseCase.EXTENSIONS;
	public static final String ACTORS = "ACTORS";
	public static final String ASSIGNED_ACTORS = "ASSIGNED_ACTORS";
	public static final String DESCRIPTION = UseCase.DESCRIPTION;
	public static final String TYPE = UseCase.TYPE;
	public static final String POSTCONDITIONS = UseCase.POSTCONDITIONS;
	public static final String PRECONDITIONS = UseCase.PRECONDITIONS;
	public static final String INCLUDE = UseCase.INCLUDE;
	public static final String EXTEND = UseCase.EXTEND;
	public static final String LABEL = UseCase.LABEL;

	public UseCaseMaintenance(Project aProject) {
		super.setProject(aProject);
	}

	public List<UseCase> getUseCasesBy(String aPriority, String aStatus, String aName, String aNumber) {
		Integer theProjectId = super.getProject().getId();
		String theQuery = "select useCase from " + UseCase.class.getSimpleName() + " useCase where ";

		List<Object> theParameters = new ArrayList<Object>();

		if (!aPriority.equals("All")) {
			theParameters.add(aPriority);
			theQuery = theQuery + "useCase.priority = ? and ";
		}
		if (!aStatus.equals("All")) {
			theParameters.add(aStatus);
			theQuery = theQuery + "useCase.status = ? and ";
		}

		theParameters.add("%" + aName + "%");
		theQuery = theQuery + "useCase.name like ? and useCase.project.id = " + theProjectId;

		try {
			theParameters.add(new Integer(aNumber));
			theQuery = theQuery + " and useCase.id = ?";
		} catch (NumberFormatException e) {

		}

		List<UseCase> theUseCases = PersistenceManager.getInstance().findAllBy(theQuery, theParameters);
		return theUseCases;
	}

	public List<UseCase> getUseCases() {
		List<UseCase> theUseCases = super.getProject().retrieveWorkProducts(UseCase.class);
		return theUseCases;
	}

	public List<UseCase> getUseCasesBy(Iteration aIteration) {

		List<UseCase> theResultUseCases = new ArrayList<UseCase>();
		List<UseCase> theUseCases = super.getProject().retrieveWorkProducts(UseCase.class);
		Iteration theIteration = null;
		for (UseCase theUseCase : theUseCases) {
			theIteration = theUseCase.getIteration();
			if ((theIteration == null && aIteration == null) || (theIteration != null && theIteration.equals(aIteration))) {
				theResultUseCases.add(theUseCase);
			}
		}
		return theResultUseCases;
	}

	public Map<String, Object> getUseCaseDataBy(Integer aUseCaseId) {
		UseCase theUseCase = super.getProject().retrieveWorkProduct(UseCase.class, aUseCaseId);
		super.setWorkProduct(theUseCase);
		Map<String, Object> theData = super.getWorkProduct().getData();

		ActorMaintenance theActorMaintenance = new ActorMaintenance(super.getProject());
		List<Actor> theAssignedActors = theActorMaintenance.getActorsBy(this.getUseCase(), true);
		theData.put(ASSIGNED_ACTORS, theAssignedActors);

		return theData;
	}

	public List<Actor> getUnassignedActors() {
		ActorMaintenance theActorMaintenance = new ActorMaintenance(super.getProject());
		return theActorMaintenance.getActorsBy(this.getUseCase(), false);
	}

	public List<String> saveUseCase(Map<String, Object> aData) {

		if (super.getWorkProduct() == null) {
			super.setWorkProduct(new UseCase(super.getProject()));
		}
		super.processIterationId(aData);
		super.processProjectMemberIds(aData);
		this.processActorsIds(aData);
		this.processUseCaseIds(aData);
		if (this.isValid(aData)) {
			super.getWorkProduct().save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	private void processUseCaseIds(Map<String, Object> aData) {
		Integer theInclude = (Integer) aData.get(INCLUDE);
		Integer theExtend = (Integer) aData.get(EXTEND);
		UseCase theIncludeUseCase = super.getProject().retrieveWorkProduct(UseCase.class, theInclude);
		UseCase theExtendUseCase = super.getProject().retrieveWorkProduct(UseCase.class, theExtend);
		aData.put(INCLUDE, theIncludeUseCase);
		aData.put(EXTEND, theExtendUseCase);
	}

	private void processActorsIds(Map<String, Object> aData) {
		List<Actor> theActors = new ArrayList<Actor>();
		List<Integer> theActorIds = (List) aData.get(ACTORS);
		Actor theActor = null;
		for (Integer theActorId : theActorIds) {
			theActor = super.getProject().retrieveActorBy(theActorId);
			theActors.add(theActor);
		}
		aData.put(ACTORS, theActors);
	}

	public void deleteUseCase(Integer aUseCaseId) {

		UseCase theUseCase = super.getProject().retrieveWorkProduct(UseCase.class, aUseCaseId);
		super.setWorkProduct(theUseCase);
		super.getWorkProduct().delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = super.getWorkProduct().validate(aData);
		return this.errors.isEmpty();
	}

	public UseCase getUseCase() {
		return (UseCase) super.getWorkProduct();
	}

	public String createPrintPreviewLocation() {
		String theLocation = null;
		UseCase theUseCase = (UseCase) this.getWorkProduct();
		if (theUseCase != null) {
			Map<String, String> theReportParameters = new HashMap<String, String>();
			theReportParameters.put("use_case_id_parameter", theUseCase.getId() + ":Integer");
			theLocation = super.createReportURL("use_case.pdf", "use_case.prpt", theReportParameters);
		}
		return theLocation;
	}

	public void removeUnsavedTaskComments() {
		UseCase theUseCase = this.getUseCase();
		if (theUseCase != null) {
			theUseCase.removeUnsavedTaskComments();
		}
	}
}
