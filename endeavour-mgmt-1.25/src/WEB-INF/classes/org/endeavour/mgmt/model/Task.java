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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.model.DependencyTypesListModel;
import org.endeavour.mgmt.view.model.StatusListModel;

@SuppressWarnings("unchecked")
public class Task extends WorkProduct {

	private WorkProduct workProduct = null;
	private List<Dependency> dependencies = null;
	public static final String LABEL = "Task";
	public static final String DEPENDENCIES = "Dependencies";
	public static final String CURRENT_DEPENDENCY_ID = "CURRENT_DEPENDENCY_ID";

	public Task() {
		super.setLabel(LABEL);
	}

	public Task(WorkProduct aWorkProduct) {
		this();
		this.workProduct = aWorkProduct;
	}

	public Task(Project aProject) {
		this();
		super.setProject(aProject);
	}

	public WorkProduct getWorkProduct() {
		return this.workProduct;
	}

	public void setWorkProduct(WorkProduct aWorkProduct) {
		this.workProduct = aWorkProduct;
	}

	public List<Dependency> getDependencies() {
		if (this.dependencies == null) {
			this.dependencies = new ArrayList<Dependency>();
		}
		return this.dependencies;
	}

	public void setDependencies(List<Dependency> aDependencies) {
		this.dependencies = aDependencies;
	}

	public void save(Map<String, Object> aData) {
		super.save(aData);

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
		}

		List<Dependency> theDependencies = (List) aData.get(DEPENDENCIES);
		if (theDependencies != null) {
			this.updateDependencies(theDependencies);
		}

		Project theProject = super.getProject();
		if (theProject != null) {
			theProject.addWorkProduct(this);
		}
		PersistenceManager.getInstance().save(this);
	}

	private void updateDependencies(List<Dependency> aDependencies) {

		List<Dependency> theComments = this.getDependencies();
		for (Dependency theComment : theComments) {
			if (theComment != null && !aDependencies.contains(theComment)) {
				theComments.set(theComments.indexOf(theComment), null);
				theComment.delete();
			}
		}
		theComments.clear();

		Integer theDependencyId = (Integer) PersistenceManager.getInstance().findBy("select max(dependency.id) from " + Dependency.class.getSimpleName() + " dependency");
		theDependencyId = theDependencyId == null ? 0 : theDependencyId;
		for (Dependency theDependency : aDependencies) {
			if (theDependency != null) {
				if (theDependency.getId() < 0) {
					theDependencyId++;
					theDependency.setId(theDependencyId);
				}
				this.addDependency(theDependency);
			}
		}
	}

	protected void addDependency(Dependency aDependency) {
		List<Dependency> theDependencies = this.getDependencies();
		if (!theDependencies.contains(aDependency)) {
			aDependency.setSucessor(this);
			theDependencies.add(aDependency);
		}
	}

	public void updateUCTask(Map<String, Object> aData) {
		List<Attachment> theAttachments = (List) aData.get(ATTACHMENTS);
		List<Comment> theComments = (List) aData.get(COMMENTS);
		List<Dependency> theDependencies = (List) aData.get(DEPENDENCIES);

		aData.put(ATTACHMENTS, null);
		aData.put(COMMENTS, null);

		super.save(aData);

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
		}

		this.updateUCTaskAttachments(theAttachments);
		this.updateUCTaskComments(theComments);
		this.updateDependencies(theDependencies);
	}

	private void updateUCTaskAttachments(List<Attachment> aAttachments) {

		List<Attachment> theAttachments = this.getAttachments();
		for (Attachment theAttachment : theAttachments) {
			if (theAttachment != null && !aAttachments.contains(theAttachment)) {
				theAttachments.set(theAttachments.indexOf(theAttachment), null);
				theAttachment.delete();
			}
		}
		theAttachments.clear();
		for (Attachment theAttachment : aAttachments) {
			if (theAttachment != null) {
				super.addAttachment(theAttachment);
			}
		}
	}

	private void updateUCTaskComments(List<Comment> aComments) {

		List<Comment> theComments = this.getComments();
		for (Comment theComment : theComments) {
			if (theComment != null && !aComments.contains(theComment)) {
				theComments.set(theComments.indexOf(theComment), null);
				theComment.delete();
			}
		}
		theComments.clear();
		for (Comment theComment : aComments) {
			if (theComment != null) {
				super.addComment(theComment);
			}
		}
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData = super.getData();
		theData.put(DEPENDENCIES, this.getDependencies());
		return theData;
	}

	protected Date getParentStartDate() {
		Date theParentStartDate = this.getWorkProduct() != null ? this.getWorkProduct().getStartDate() : this.getIteration() != null ? this.getIteration().getStartDate() : this.getProject().getStartDate();
		return theParentStartDate;
	}

	protected Date getParentEndDate() {
		Date theParentEndDate = this.getWorkProduct() != null ? this.getWorkProduct().getEndDate() : this.getIteration() != null ? this.getIteration().getEndDate() : this.getProject().getEndDate();
		return theParentEndDate;
	}

	public List<String> validate(Map<String, Object> aData) {
		/*
		 * Finish-to-Start: The Successor cannot start until its Predecessor has
		 * finished.
		 * 
		 * Start-to-Start: The Successor cannot start until its Predecessor has
		 * started.
		 * 
		 * Finish-to-Finish: The Successor cannot finish until its Predecessor
		 * has finished.
		 * 
		 * Start-to-Finish: The Successor cannot finish until its Predecessor
		 * has started
		 * 
		 * Relevant Task Statuses: In Progress, Completed
		 */

		List<String> theErrors = super.validate(aData);
		String theSucessorStatus = (String) aData.get(STATUS);

		for (Dependency theDependency : this.getDependencies()) {
			if (theDependency != null) {
				Task thePredecessor = theDependency.getPredecessor();
				String thePredecessorStatus = thePredecessor.getStatus();
				String theDependencyType = theDependency.getType();

				if (theSucessorStatus.equals(StatusListModel.IN_PROGRESS)) {

					if (theDependencyType.equals(DependencyTypesListModel.FINISH_TO_START)) {
						/*
						 * Finish-to-Start: The Successor cannot start until its
						 * Predecessor has finished.
						 */
						if (!thePredecessorStatus.equals(StatusListModel.COMPLETED)) {
							theErrors.add(IViewConstants.RB.getString("finish_to_start.msg") + " \"" + IViewConstants.RB.getString("task_initial.lbl") + thePredecessor.getId() + "\".");
						}
					}

					if (theDependencyType.equals(DependencyTypesListModel.START_TO_START)) {
						/*
						 * Start-to-Start: The Successor cannot start until its
						 * Predecessor has started.
						 */
						if (!thePredecessorStatus.equals(StatusListModel.IN_PROGRESS)) {
							theErrors.add(IViewConstants.RB.getString("start_to_start.msg") + " \"" + IViewConstants.RB.getString("task_initial.lbl") + thePredecessor.getId() + "\".");
						}
					}
				}
				if (theSucessorStatus.equals(StatusListModel.COMPLETED)) {

					if (theDependencyType.equals(DependencyTypesListModel.FINISH_TO_FINISH)) {
						/*
						 * Finish-to-Finish: The Successor cannot finish until
						 * its Predecessor has finished.
						 */
						if (!thePredecessorStatus.equals(StatusListModel.COMPLETED)) {
							theErrors.add(IViewConstants.RB.getString("finish_to_finish.msg") + " \"" + IViewConstants.RB.getString("task_initial.lbl") + thePredecessor.getId() + "\".");
						}

					}

					if (theDependencyType.equals(DependencyTypesListModel.START_TO_FINISH)) {
						/*
						 * Start-to-Finish: The Successor cannot finish until
						 * its Predecessor has started
						 */
						if (!thePredecessorStatus.equals(StatusListModel.IN_PROGRESS)) {
							theErrors.add(IViewConstants.RB.getString("start_to_finish.msg") + " \"" + IViewConstants.RB.getString("task_initial.lbl") + thePredecessor.getId() + "\".");
						}
					}
				}
			}
		}

		return theErrors;
	}

	public void delete() {
		super.delete();
	}

	public boolean isOrphan() {
		return this.getWorkProduct() == null;
	}

	public boolean isUnscheduled() {
		return this.getIteration() == null;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("task.lbl");
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Task) {
			Task theTask = (Task) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theTask.getId());
			}
		}
		return isEquals;
	}

	public void removeUnsavedComments() {
		List<Comment> theComments = this.getComments();
		for (Comment theComment : theComments) {
			if (theComment != null && theComment.getId() < 0) {
				theComments.set(theComments.indexOf(theComment), null);
			}
		}
	}
}
