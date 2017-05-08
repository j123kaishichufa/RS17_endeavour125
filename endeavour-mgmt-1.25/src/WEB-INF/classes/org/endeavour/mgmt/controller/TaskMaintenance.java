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

import org.endeavour.mgmt.model.Dependency;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class TaskMaintenance extends WorkProductMaintenance implements ITaskMaintenance {

	private List<String> errors = null;
	public static final String LABEL = Task.LABEL;
	public static final String DEPENDENCIES = Task.DEPENDENCIES;

	public TaskMaintenance(Project aProject) {
		super.setProject(aProject);
	}

	public List<Task> getTasks() {
		List<Task> theResults = new ArrayList<Task>();
		List<Task> theTasks = super.getProject().retrieveWorkProducts(Task.class);
		for (Task theTask : theTasks) {
			if (theTask.isOrphan()) {
				theResults.add(theTask);
			}
		}
		return theResults;
	}

	public List<Task> getTasksBy(Iteration aIteration) {

		List<Task> theResultTasks = new ArrayList<Task>();
		List<Task> theTasks = super.getProject().retrieveWorkProducts(Task.class);
		for (Task theTask : theTasks) {
			Iteration theIteration = null;
			theIteration = theTask.getIteration();
			if (theTask.isOrphan()) {
				if ((theIteration == null && aIteration == null) || (theIteration != null && theIteration.equals(aIteration))) {
					theResultTasks.add(theTask);
				}
			}
		}
		return theResultTasks;
	}

	public Map<String, Object> getTaskDataBy(Integer aTaskId) {
		Task theTask = super.getProject().retrieveWorkProduct(Task.class, aTaskId);
		super.setWorkProduct(theTask);
		Map<String, Object> theData = super.getWorkProduct().getData();
		return theData;
	}

	public List<String> saveTask(Map<String, Object> aData) {

		if (super.getWorkProduct() == null) {
			super.setWorkProduct(new Task(super.getProject()));
		}
		super.processIterationId(aData);
		super.processProjectMemberIds(aData);
		if (this.isValid(aData)) {
			super.getWorkProduct().save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void deleteTask(Integer aTaskId) {
		Task theTask = super.getProject().retrieveWorkProduct(Task.class, aTaskId);

		for (Task theCurrentTask : this.getTasks()) {
			if (theCurrentTask != null) {
				for (Dependency theDependency : theCurrentTask.getDependencies()) {
					if (theDependency != null && theTask.equals(theDependency.getPredecessor())) {
						int theIndex = theCurrentTask.getDependencies().indexOf(theDependency);
						theCurrentTask.getDependencies().set(theIndex, null);
						theDependency.delete();
					}
				}
			}
		}

		super.setWorkProduct(theTask);
		super.getWorkProduct().delete();
		
		setChanged();
		notifyObservers();
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = super.getWorkProduct().validate(aData);
		return this.errors.isEmpty();
	}

	public Date getParentStartDate() {
		return super.getProject().getStartDate();
	}

	public Date getParentEndDate() {
		return super.getProject().getEndDate();
	}

	public List<Task> getTasksBy(String aPriority, String aStatus, String aName, String aNumber) {

		Integer theProjectId = super.getProject().getId();
		String theQuery = "select task from " + Task.class.getSimpleName() + " task where ";

		List<Object> theParameters = new ArrayList<Object>();

		if (!aPriority.equals("All")) {
			theParameters.add(aPriority);
			theQuery = theQuery + "task.priority = ? and ";
		}
		if (!aStatus.equals("All")) {
			theParameters.add(aStatus);
			theQuery = theQuery + "task.status = ? and ";
		}

		theParameters.add("%" + aName + "%");
		theQuery = theQuery + "task.name like ? and task.workProduct = null and task.project.id = " + theProjectId;

		try {
			theParameters.add(new Integer(aNumber));
			theQuery = theQuery + " and task.id = ?";
		} catch (NumberFormatException e) {

		}

		List<Task> theTasks = PersistenceManager.getInstance().findAllBy(theQuery, theParameters);
		return theTasks;
	}
	
	public boolean isIterationDerived() {
		boolean isIterationDerived = false;
		Task theTask = (Task) this.getWorkProduct();
		if(theTask != null) {
			isIterationDerived = theTask.getWorkProduct() != null;
		}
		return isIterationDerived;
	}
}
