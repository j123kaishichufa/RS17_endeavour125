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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class UseCase extends WorkProduct {

	private String type = null;
	private String preconditions = null;
	private String postconditions = null;
	private List<Event> events = null;
	private List<Task> tasks = null;
	private Set<Actor> actors = null;
	private UseCase include = null;
	private UseCase extend = null;

	public static final String TASKS = "TASKS";
	public static final String EVENTS = "EVENTS";
	public static final String EXTENSIONS = "EXTENSIONS";
	public static final String ACTORS = "ACTORS";
	public static final String INCLUDE = "INCLUDE";
	public static final String EXTEND = "EXTEND";
	public static final String PRECONDITIONS = "PRECONDITIONS";
	public static final String POSTCONDITIONS = "POSTCONDITIONS";
	public static final String TYPE = "TYPE";
	public static final String LABEL = "Use Case";

	public UseCase() {
		super.setLabel(LABEL);
	}

	public List<Event> getEvents() {
		if (this.events == null) {
			this.events = new ArrayList<Event>();
		}
		return this.events;
	}

	public void setEvents(List<Event> aEvents) {
		this.events = aEvents;
	}

	public UseCase(Project aProject) {
		this();
		super.setProject(aProject);
	}

	public List<Task> getTasks() {
		if (this.tasks == null) {
			this.tasks = new ArrayList<Task>();
		}
		return this.tasks;
	}

	public void setTasks(List<Task> aTasks) {
		this.tasks = aTasks;
	}

	public Set<Actor> getActors() {
		if (this.actors == null) {
			this.actors = new HashSet<Actor>();
		}
		return this.actors;
	}

	public void setActors(Set<Actor> aActors) {
		this.actors = aActors;
	}

	public UseCase getInclude() {
		return this.include;
	}

	public void setInclude(UseCase aInclude) {
		this.include = aInclude;
	}

	public UseCase getExtend() {
		return this.extend;
	}

	public void setExtend(UseCase aExtend) {
		this.extend = aExtend;
	}

	public String getPreconditions() {
		return this.preconditions;
	}

	public void setPreconditions(String aPreconditions) {
		this.preconditions = aPreconditions;
	}

	public String getPostconditions() {
		return this.postconditions;
	}

	public void setPostconditions(String aPostconditions) {
		this.postconditions = aPostconditions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private void updateTasks(List<Task> aTasks) {

		List<Task> theTasks = this.getTasks();
		for (Task theTask : this.getTasks()) {
			if (theTask != null && !aTasks.contains(theTask)) {
				theTasks.set(theTasks.indexOf(theTask), null);
				theTask.delete();
			}
		}
		theTasks.clear();

		Integer theId = null;
		boolean isPersistent = PersistenceManager.getInstance().contains(this);
		if (isPersistent) {
			theId = (Integer) PersistenceManager.getInstance().findBy("select max(workProduct.id) from " + WorkProduct.class.getSimpleName() + " workProduct");
		} else {
			theId = this.getId();
		}

		for (Task theTask : aTasks) {
			if (theTask.getId() < 0) {
				theId++;
				theTask.setId(theId);
			}
			this.addTask(theTask);
		}
	}

	private void updateTaskAttachments(List<Task> aTasks, Map<String, Object> aData) {

		Integer theAttachmentId = (Integer) aData.get(CURRENT_ATTACHMENT_ID);
		for (Task theTask : aTasks) {
			if (theTask != null) {
				for (Attachment theAttachment : theTask.getAttachments()) {
					if (theAttachment != null && theAttachment.getId() < 0) {
						theAttachmentId++;
						theAttachment.setId(theAttachmentId);
					}
				}
			}
		}
	}

	private void updateTaskComments(List<Task> aTasks, Map<String, Object> aData) {

		Integer theCommentId = (Integer) aData.get(CURRENT_COMMENT_ID);
		for (Task theTask : aTasks) {
			if (theTask != null) {
				for (Comment theComment : theTask.getComments()) {
					if (theComment != null && theComment.getId() < 0) {
						theCommentId++;
						theComment.setId(theCommentId);
					}
				}
			}
		}
	}

	private Integer updateEvents(List<Event> aEvents) {

		List<Event> theEvents = this.getEvents();
		for (Event theEvent : this.getEvents()) {
			if (theEvent != null && !aEvents.contains(theEvent)) {
				theEvents.set(theEvents.indexOf(theEvent), null);
				theEvent.delete();
			}
		}
		theEvents.clear();

		Integer theEventId = (Integer) PersistenceManager.getInstance().findBy("select max(event.id) from " + Event.class.getSimpleName() + " event");
		theEventId = theEventId == null ? 0 : theEventId;
		for (Event theEvent : aEvents) {
			if (theEvent.getId() < 0) {
				theEventId++;
				theEvent.setId(theEventId);
			}
			this.addEvent(theEvent);
		}
		return theEventId;
	}

	private void updateExtensions(Map<Event, List<Event>> aExtensions, Integer aExtensionId) {

		for (Event theEvent : aExtensions.keySet()) {
			List<Event> theNewExtensions = aExtensions.get(theEvent);
			List<Event> theCurrentExtensions = theEvent.getExtensions();
			for (Event theCurrentExtension : theCurrentExtensions) {
				if (theCurrentExtension != null && !theNewExtensions.contains(theCurrentExtension)) {
					theCurrentExtensions.set(theCurrentExtensions.indexOf(theCurrentExtension), null);
					theCurrentExtension.delete();
				}
			}
			theCurrentExtensions.clear();
			for (Event theNewExtension : theNewExtensions) {
				if (theNewExtension != null && theNewExtension.getId() < 0) {
					aExtensionId++;
					theNewExtension.setId(aExtensionId);
				}
				theEvent.addExtension(theNewExtension);
			}
		}
	}

	private void addEvent(Event aEvent) {
		List<Event> theEvents = this.getEvents();
		if (!theEvents.contains(aEvent)) {
			aEvent.setWorkProduct(this);
			theEvents.add(aEvent);
		}
	}

	private void addTask(Task aTask) {
		List<Task> theTasks = this.getTasks();
		if (!theTasks.contains(aTask)) {
			aTask.setWorkProduct(this);
			aTask.setProject(super.getProject());
			aTask.setStartDate(this.getStartDate());
			aTask.setEndDate(this.getEndDate());
			aTask.setIteration(this.getIteration());
			theTasks.add(aTask);
		}
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = super.getData();
		theData.put(TASKS, this.getTasks());
		theData.put(EVENTS, this.getEvents());
		theData.put(ACTORS, this.getActors());
		theData.put(PRECONDITIONS, this.getPreconditions());
		theData.put(POSTCONDITIONS, this.getPostconditions());
		theData.put(TYPE, this.getType());
		theData.put(INCLUDE, this.getInclude() != null ? this.getInclude().getId() : -1);
		theData.put(EXTEND, this.getExtend() != null ? this.getExtend().getId() : -1);
		return theData;
	}

	public void delete() {
		List<Task> theTasks = this.getTasks();
		for (Task theTask : theTasks) {
			if (theTask != null) {
				theTasks.set(theTasks.indexOf(theTask), null);
				theTask.delete();
			}
		}
		super.delete();
	}

	public void save(Map<String, Object> aData) {
		super.save(aData);
		super.getProject().addWorkProduct(this);

		String theType = (String) aData.get(TYPE);
		this.setType(theType);

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
		}

		List<Task> theTasks = (List) aData.get(TASKS);
		if (theTasks != null) {
			this.updateTasks(theTasks);
		}

		this.updateTaskAttachments(theTasks, aData);
		this.updateTaskComments(theTasks, aData);

		List<Actor> theActors = (List) aData.get(ACTORS);
		if (theActors != null) {
			this.updateActors(theActors);
		}

		UseCase theExtend = (UseCase) aData.get(EXTEND);
		this.setExtend(theExtend);

		UseCase theInclude = (UseCase) aData.get(INCLUDE);
		this.setInclude(theInclude);

		String thePreconditions = (String) aData.get(PRECONDITIONS);
		this.setPreconditions(thePreconditions);

		String thePostconditions = (String) aData.get(POSTCONDITIONS);
		this.setPostconditions(thePostconditions);

		List<Event> theEvents = (List) aData.get(EVENTS);
		if (theEvents != null) {
			Integer theEventsId = this.updateEvents(theEvents);
			Map<Event, List<Event>> theExtensions = (Map) aData.get(EXTENSIONS);
			if (theExtensions != null) {
				this.updateExtensions(theExtensions, theEventsId);
			}
		}
		PersistenceManager.getInstance().save(this);
	}

	private void updateActors(List<Actor> aActors) {
		Set<Actor> theActors = this.getActors();
		for (Actor theActor : theActors) {
			if (theActor != null && !aActors.contains(theActor)) {
				theActor.removeUseCase(this);
			}
		}
		theActors.clear();
		for (Actor theActor : aActors) {
			this.addActor(theActor);
		}
	}

	private void addActor(Actor aActor) {
		Set<Actor> theActors = this.getActors();
		if (!theActors.contains(aActor)) {
			theActors.add(aActor);
			aActor.addUseCase(this);
		}
	}

	public void removeActor(Actor aActor) {
		Set<Actor> theActors = this.getActors();
		if (theActors.contains(aActor)) {
			theActors.remove(aActor);
		}
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = super.validate(aData);
		return theErrors;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("use_case.lbl");
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof UseCase) {
			UseCase theUseCase = (UseCase) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theUseCase.getId());
			}
		}
		return isEquals;
	}

	public void removeUnsavedTaskComments() {
		for (Task theTask : this.getTasks()) {
			if (theTask != null) {
				theTask.removeUnsavedComments();
			}
		}
	}
}
