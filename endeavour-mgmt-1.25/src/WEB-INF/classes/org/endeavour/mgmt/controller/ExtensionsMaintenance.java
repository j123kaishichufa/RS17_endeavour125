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
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.model.Event;

public class ExtensionsMaintenance extends EventMaintenance implements Observer {

	private Event parentEvent = null;
	private EventMaintenance parentEventMaintenance = null;
	private Map<Event, List<Event>> extensions = null;

	public ExtensionsMaintenance(EventMaintenance aParentEventMaintenance) {
		this.extensions = new HashMap<Event, List<Event>>();
		this.parentEventMaintenance = aParentEventMaintenance;
		aParentEventMaintenance.addObserver(this);
	}

	public List<String> saveEvent(Map<String, Object> aData) {
		if (super.event == null) {
			super.event = new Event();
		}
		if (super.isValid(aData)) {
			Integer theId = this.event.getId();
			super.event.save(aData);
			super.event.setId(theId == null ? super.produceTransientEventId() : theId);
			super.event.setParentEvent(this.parentEvent);
			this.addExtension(super.event);
			setChanged();
		}
		notifyObservers();
		return super.errors;
	}

	private void addExtension(Event anExtension) {
		if (!super.events.contains(anExtension)) {
			super.events.add(anExtension);
		}
	}

	public Object getData() {
		return this.extensions;
	}

	public void setSelectedEventId(Integer aSelectedEventId) {
		this.parentEvent = this.parentEventMaintenance.getEventForId(aSelectedEventId);
		super.events = this.extensions.get(this.parentEvent);
		if (super.events == null) {
			super.events = new ArrayList<Event>();
			if (this.parentEvent != null) {
				for (Event theExtension : this.parentEvent.getExtensions()) {
					if (theExtension != null) {
						super.events.add(theExtension);
					}
				}
				this.extensions.put(this.parentEvent, super.events);
			}
		}
		setChanged();
		notifyObservers();
	}

	public void update(Observable aObservable, Object aObject) {
		// This logic works in combination with the
		// EventMaintenance.deleteEvent()
		if (aObject != null) {
			// If not deleting the last one then reselect the current index.
			List<Event> theEvents = this.parentEventMaintenance.getEvents();
			Event theEvent = theEvents.get((Integer) aObject);
			this.setSelectedEventId(theEvent.getId());
		} else {
			// If deleting the last one OR performing save then select the first
			// event
			EventMaintenance theEventMaintenance = (EventMaintenance) aObservable;
			List<Event> theEvents = theEventMaintenance.getEvents();
			if (theEvents != null && theEvents.size() > 0) {
				Event theEvent = theEvents.get(0);
				this.setSelectedEventId(theEvent.getId());
			} else {
				// If deleting the last and only element then clear lists.
				this.setSelectedEventId(-1);
			}
		}
	}
}
