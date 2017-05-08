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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;

@SuppressWarnings("unchecked")
public class IterationMaintenance extends ApplicationController {
	private Iteration iteration = null;
	private List<String> errors = null;
	private Project project = null;

	public static final String WORK_PRODUCTS = Iteration.WORK_PRODUCTS;
	public static final String START_DATE = Iteration.START_DATE;
	public static final String END_DATE = Iteration.END_DATE;
	public static final String PROGRESS = Iteration.PROGRESS;
	public static final String ASSIGNED_USE_CASES = "ASSIGNED_USE_CASES";
	public static final String ASSIGNED_DEFECTS = "ASSIGNED_DEFECTS";
	public static final String ASSIGNED_CHANGE_REQUESTS = "ASSIGNED_CHANGE_REQUESTS";
	public static final String ASSIGNED_TASKS = "ASSIGNED_TASKS";

	public IterationMaintenance(Project aProject) {
		this.project = aProject;
	}

	public List<Iteration> getIterations() {
		return this.project.getIterations();
	}

	public Map<String, Object> getIterationDataBy(Integer aIterationId) {
		this.iteration = this.project.retrieveIterationBy(aIterationId);
		Map<String, Object> theData = this.iteration.getData();

		UseCaseMaintenance theUseCaseMaintenance = new UseCaseMaintenance(this.project);
		List<UseCase> theAssignedUseCases = theUseCaseMaintenance.getUseCasesBy(this.iteration);
		theData.put(ASSIGNED_USE_CASES, theAssignedUseCases);

		DefectMaintenance theDefectMaintenance = new DefectMaintenance(this.project);
		List<Defect> theAssignedDefects = theDefectMaintenance.getDefectsBy(this.iteration);
		theData.put(ASSIGNED_DEFECTS, theAssignedDefects);

		ChangeRequestMaintenance theChangeRequestMaintenance = new ChangeRequestMaintenance(this.project);
		List<ChangeRequest> theAssignedChangeRequests = theChangeRequestMaintenance.getChangeRequestsBy(this.iteration);
		theData.put(ASSIGNED_CHANGE_REQUESTS, theAssignedChangeRequests);

		TaskMaintenance theTaskMaintenance = new TaskMaintenance(this.project);
		List<Task> theAssignedTasks = theTaskMaintenance.getTasksBy(this.iteration);
		theData.put(ASSIGNED_TASKS, theAssignedTasks);

		return theData;
	}

	public List<UseCase> getUnassignedUseCases() {
		UseCaseMaintenance theUseCaseMaintenance = new UseCaseMaintenance(this.project);
		return theUseCaseMaintenance.getUseCasesBy(null);
	}

	public List<Defect> getUnassignedDefects() {
		DefectMaintenance theDefectMaintenance = new DefectMaintenance(this.project);
		return theDefectMaintenance.getDefectsBy(null);
	}

	public List<Task> getUnassignedTasks() {
		TaskMaintenance theTaskMaintenance = new TaskMaintenance(this.project);
		return theTaskMaintenance.getTasksBy(null);
	}

	public List<ChangeRequest> getUnassignedChangeRequests() {
		ChangeRequestMaintenance theChangeRequestMaintenance = new ChangeRequestMaintenance(this.project);
		return theChangeRequestMaintenance.getChangeRequestsBy(null);
	}

	public List<String> saveIteration(Map<String, Object> aData) {

		if (this.iteration == null) {
			this.iteration = new Iteration(this.project);
		}
		this.processWorkProductIds(aData);
		if (this.isValid(aData)) {
			this.iteration.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	private void processWorkProductIds(Map<String, Object> aData) {
		List<WorkProduct> theWorkProducts = new ArrayList<WorkProduct>();
		List<Integer> theWorkProductIds = (List) aData.get(WORK_PRODUCTS);
		for (Integer theWorkProductId : theWorkProductIds) {
			WorkProduct theWorkProduct = this.project.retrieveWorkProduct(WorkProduct.class, theWorkProductId);
			theWorkProducts.add(theWorkProduct);
		}
		aData.put(WORK_PRODUCTS, theWorkProducts);
	}

	public void reset() {
		this.iteration = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteIteration(Integer aIterationId) {
		this.iteration = this.project.retrieveIterationBy(aIterationId);
		this.iteration.delete();
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.iteration.validate(aData);
		return this.errors.isEmpty();
	}

	public Date getProjectStartDate() {
		return this.project.getStartDate();
	}

	public Date getProjectEndDate() {
		return this.project.getEndDate();
	}

	public Integer getSelectedIterationId() {
		Integer theId = null;
		if (this.iteration != null) {
			theId = this.iteration.getId();
		}
		return theId;
	}
}
