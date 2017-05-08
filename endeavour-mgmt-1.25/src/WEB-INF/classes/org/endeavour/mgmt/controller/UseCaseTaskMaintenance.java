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
import org.endeavour.mgmt.model.Document;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class UseCaseTaskMaintenance extends WorkProductMaintenance implements ITaskMaintenance {

	private List<Task> tasks = null;
	private List<String> errors = null;
	private WorkProduct parent = null;
	private Task task = null;

	public UseCaseTaskMaintenance(Project aProject) {
		this.tasks = new ArrayList<Task>();
		super.setProject(aProject);
	}

	public void deleteTask(Integer aTaskId) {
		for (Task theTask : this.tasks) {
			this.task = theTask;
			if (this.task.getId().equals(aTaskId)) {
				break;
			}
		}

		for (Task theCurrentTask : this.tasks) {
			if (theCurrentTask != null) {
				for (Dependency theDependency : theCurrentTask.getDependencies()) {
					if (theDependency != null && this.task.equals(theDependency.getPredecessor())) {
						int theIndex = theCurrentTask.getDependencies().indexOf(theDependency);
						theCurrentTask.getDependencies().set(theIndex, null);
						theDependency.delete();
					}
				}
			}
		}
		
		this.tasks.remove(this.task);
		
		setChanged();
		notifyObservers();
	}

	public List<ProjectMember> getUnassignedProjectMembersDataForProject() {
		return ProjectMember.getUnassignedProjectMembersFor(this.task, super.getProject());
	}

	public List<Document> getUnassignedDocuments() {
		return Document.getUnassignedDocumentsDataFor(this.task, this.getProject());
	}

	public Map<String, Object> getTaskDataBy(Integer aTaskId) {

		for (Task theTask : this.tasks) {
			this.task = theTask;
			if (this.task != null && this.task.getId().equals(aTaskId)) {
				break;
			}
		}
		Map<String, Object> theData = this.task.getData();
		return theData;
	}

	public void reset() {
		this.task = null;
	}

	public List<String> saveTask(Map<String, Object> aData) {
		if (this.task == null) {
			this.task = new Task(this.parent);
		}
		super.processIterationId(aData);
		super.processProjectMemberIds(aData);
		
		// Set the project only to validate against its start and end date.
		this.task.setProject(super.getProject());
		if(this.parent != null) {
			aData.put(ITERATION, this.parent.getIteration());
		}
		if (this.isValid(aData)) {
			Integer theId = this.task.getId();
			if (this.parent == null) {
				// Once validation passed the project has to be removed
				this.task.setProject(null);
			}
			this.task.updateUCTask(aData);
			this.task.setId(theId == null ? this.produceTransientTaskId() : theId);
			this.addTask((Task) this.task);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public List<Task> retrievePeerTasksFor(Integer aTaskId) {
		Task theTask = (Task) PersistenceManager.getInstance().findById(Task.class, aTaskId);
		UseCase theUseCase = (UseCase) theTask.getWorkProduct();
		return theUseCase.getTasks();
	}

	private Integer produceTransientTaskId() {
		Integer theTransientId = 0;
		for (Task theTask : this.tasks) {
			int theId = theTask.getId();
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

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.task.validate(aData);
		return this.errors.isEmpty();
	}

	private void addTask(Task aTask) {
		if (!this.tasks.contains(aTask)) {
			this.tasks.add(aTask);
		}
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public boolean hasTasks() {
		return this.tasks.size() > 0;
	}

	public void setTasks(List<Task> aTasks) {
		this.tasks = new ArrayList<Task>();
		for (Task theTask : aTasks) {
			if (theTask != null) {
				this.tasks.add(theTask);
			}
		}
	}

	public void setTaskParent(WorkProduct anWorkProduct) {
		this.parent = anWorkProduct;
		if (super.getProject() == null) {
			super.setProject(this.parent.getProject());
		}
	}

	public Date getParentStartDate() {
		Date theStartDate = null;
		if (this.parent != null) {
			theStartDate = this.parent.getStartDate();
		} else {
			theStartDate = super.getProjectStartDate();
		}
		return theStartDate;
	}

	public Date getParentEndDate() {
		Date theEndDate = null;
		if (this.parent != null) {
			theEndDate = this.parent.getEndDate();
		} else {
			theEndDate = super.getProjectEndDate();
		}
		return theEndDate;
	}

	public List<Task> getTasksBy(String aPriority, String aStatus, String aName, String aNumber) {
		return null;
	}

	public void startUnitOfWork() {
	}

	public void endUnitOfWork() {
	}

	public Integer getSelectedWorkProductId() {
		Integer theId = null;
		if (this.task != null) {
			theId = this.task.getId();
		}
		return theId;
	}
	
	public boolean isIterationDerived() {
		return true;
	}
}
