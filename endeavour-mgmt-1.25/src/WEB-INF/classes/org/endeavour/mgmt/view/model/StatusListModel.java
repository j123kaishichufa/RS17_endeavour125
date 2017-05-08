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

package org.endeavour.mgmt.view.model;

import java.util.ArrayList;
import java.util.List;

import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

@SuppressWarnings("unchecked")
public class StatusListModel extends GridBoxModel {

	public static final String PENDING = "Pending";
	public static final String IN_PROGRESS = "In Progress";
	public static final String COMPLETED = "Completed";
	public static final String REJECTED = "Rejected";
	public static final String CLOSED = "Closed";
	public static final String REOPENED = "Reopened";
	public static final String ALL = "All";

	public StatusListModel(boolean aFullList, boolean aIncludeAll) {
		super();
		super.rows = this.initializeStatuses(aFullList, aIncludeAll);
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Status theStatus = (Status) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theStatus.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Status theStatus = (Status) this.rows.get(aRowIndex);
		return theStatus.getId();
	}

	private List<Status> initializeStatuses(boolean aFullList, boolean aIncludeAll) {

		List<Status> theStatuses = new ArrayList<Status>();
		theStatuses.add(new Status(0, IViewConstants.RB.getString("pending.lbl"), PENDING));
		theStatuses.add(new Status(1, IViewConstants.RB.getString("in_progress.lbl"), IN_PROGRESS));
		theStatuses.add(new Status(2, IViewConstants.RB.getString("completed.lbl"), COMPLETED));
		if (aFullList) {
			theStatuses.add(new Status(3, IViewConstants.RB.getString("rejected.lbl"), REJECTED));
			theStatuses.add(new Status(4, IViewConstants.RB.getString("closed.lbl"), CLOSED));
			theStatuses.add(new Status(5, IViewConstants.RB.getString("reopened.lbl"), REOPENED));
		}
		if (aIncludeAll) {
			theStatuses.add(new Status(aFullList ? 6 : 3, IViewConstants.RB.getString("all.lbl"), ALL));
		}
		return theStatuses;
	}

	public String getValueById(Integer anId) {
		Status theStatus = (Status) super.getRowObjectByIndex(anId);
		return theStatus.getValue();
	}

	public String getValueByDescription(String aDescription) {
		String theValue = null;
		for (Status theStatus : (List<Status>) super.rows) {
			if (theStatus.getDescription().equals(aDescription)) {
				theValue = theStatus.getValue();
			}
		}
		return theValue;
	}

	public String getDescriptionByValue(String aValue) {
		String theDescription = null;
		for (Status theStatus : (List<Status>) super.rows) {
			if (theStatus.getValue().equals(aValue)) {
				theDescription = theStatus.getDescription();
			}
		}
		return theDescription;
	}

	class Status {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public Status(Integer anId, String aDescription, String aValue) {
			this.id = anId;
			this.description = aDescription;
			this.value = aValue;
		}

		public Integer getId() {
			return this.id;
		}

		public String getDescription() {
			return this.description;
		}

		public String getValue() {
			return this.value;
		}
	}

	public String toString() {
		String theStatusList = "";
		Status theStatus = null;
		int theSize = super.rows.size();
		for (int i = 0; i < theSize; i++) {
			theStatus = (Status) super.rows.get(i);
			theStatusList = theStatusList + theStatus.getValue() + ":String";
			if (i != theSize - 1) {
				theStatusList = theStatusList + ",";
			}
		}
		return "[" + theStatusList + "]";
	}
}
