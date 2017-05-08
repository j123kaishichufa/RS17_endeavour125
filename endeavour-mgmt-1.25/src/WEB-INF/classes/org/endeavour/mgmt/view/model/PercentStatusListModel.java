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

import org.endeavour.mgmt.view.components.GridBoxModel;


public class PercentStatusListModel extends GridBoxModel {

	public PercentStatusListModel() {
		super();
		super.rows = this.initializeStatuses();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Status theStatus = (Status) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theStatus.getPercentage();
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

	private List<Status> initializeStatuses() {
		List<Status> theStatuses = new ArrayList<Status>();
		theStatuses.add(new Status(0, "0%"));
		theStatuses.add(new Status(1, "5%"));
		theStatuses.add(new Status(2, "10%"));
		theStatuses.add(new Status(3, "15%"));
		theStatuses.add(new Status(4, "20%"));
		theStatuses.add(new Status(5, "25%"));
		theStatuses.add(new Status(6, "30%"));
		theStatuses.add(new Status(7, "35%"));
		theStatuses.add(new Status(8, "40%"));
		theStatuses.add(new Status(9, "45%"));
		theStatuses.add(new Status(10, "50%"));
		theStatuses.add(new Status(11, "55%"));
		theStatuses.add(new Status(12, "60%"));
		theStatuses.add(new Status(13, "65%"));
		theStatuses.add(new Status(14, "70%"));
		theStatuses.add(new Status(15, "75%"));
		theStatuses.add(new Status(16, "80%"));
		theStatuses.add(new Status(17, "85%"));
		theStatuses.add(new Status(18, "90%"));
		theStatuses.add(new Status(19, "95%"));
		theStatuses.add(new Status(20, "100%"));
		return theStatuses;
	}

	class Status {
		private Integer id = null;
		private String percentage = null;

		public Status(Integer anId, String aPercentage) {
			this.id = anId;
			this.percentage = aPercentage;
		}

		public Integer getId() {
			return this.id;
		}

		public String getPercentage() {
			return this.percentage;
		}
	}
}
