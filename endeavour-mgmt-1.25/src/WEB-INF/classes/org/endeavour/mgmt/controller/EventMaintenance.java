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

import org.endeavour.mgmt.model.Event;

public class EventMaintenance extends ApplicationController {

	protected Event event = null;
	protected List<Event> events = null;
	protected List<String> errors = null;

	public static final String TEXT = Event.TEXT;

	public EventMaintenance() {
		this.events = new ArrayList<Event>();
	}

	public void deleteEvent(Integer aEventId) {
		// This logic works in combination with the
		// ExtensionsMaintenance.update()
		int theEventId = aEventId.intValue();
		Object deleteValue = this.events.size() - 1 != theEventId ? aEventId : null;

		this.events.remove(theEventId);
		setChanged();
		notifyObservers(deleteValue);
	}

	public List<String> saveEvent(Map<String, Object> aData) {

		if (this.event == null) {
			this.event = new Event();
		}
		if (this.isValid(aData)) {
			Integer theId = this.event.getId();
			this.event.save(aData);
			this.event.setId(theId == null ? this.produceTransientEventId() : theId);
			this.addEvent(this.event);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	protected boolean isValid(Map<String, Object> aData) {
		this.errors = this.event.validate(aData);
		return this.errors.isEmpty();
	}

	protected Integer produceTransientEventId() {
		Integer theTransientId = 0;
		int theId = this.events.size() + 1;
		theTransientId = theId - (theId * 2);
		return theTransientId;
	}

	private void addEvent(Event aEvent) {
		if (!this.events.contains(aEvent)) {
			this.events.add(aEvent);
		}
	}

	public String viewEvent(Integer aEventId) {
		String theEventText = null;
		for (Event theEvent : this.events) {
			if (theEvent != null && theEvent.getId().equals(aEventId)) {
				this.event = theEvent;
				theEventText = this.event.getText();
				break;
			}
		}
		return theEventText;
	}

	public void setEvents(List<Event> aEvents) {
		this.events = new ArrayList<Event>();
		for (Event theEvent : aEvents) {
			if (theEvent != null) {
				this.events.add(theEvent);
			}
		}
	}

	public Object getData() {
		return this.getEvents();
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void clearSelectedEvent() {
		this.event = null;
	}

	public Event getEventForId(Integer aEventId) {
		Event theEvent = null;
		for (Event theCurrentEvent : this.events) {
			if (theCurrentEvent.getId().equals(aEventId)) {
				theEvent = theCurrentEvent;
				break;
			}
		}
		return theEvent;
	}
}
