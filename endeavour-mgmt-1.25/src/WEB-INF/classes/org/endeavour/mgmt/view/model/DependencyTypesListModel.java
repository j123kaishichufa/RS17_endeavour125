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
public class DependencyTypesListModel extends GridBoxModel {

	public static final String FINISH_TO_START = "Finish-to-Start";
	public static final String START_TO_START = "Start-to-Start";
	public static final String FINISH_TO_FINISH = "Finish-to-Finish";
	public static final String START_TO_FINISH = "Start-to-Finish";

	public DependencyTypesListModel() {
		super();
		super.rows = this.initializeDependencyTypes();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		DependencyType theDependency = (DependencyType) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theDependency.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		DependencyType theDependency = (DependencyType) this.rows.get(aRowIndex);
		return theDependency.getId();
	}

	private List<DependencyType> initializeDependencyTypes() {

		List<DependencyType> theDependencies = new ArrayList<DependencyType>();
		theDependencies.add(new DependencyType(0, IViewConstants.RB.getString("finish_to_start.lbl"), FINISH_TO_START));
		theDependencies.add(new DependencyType(1, IViewConstants.RB.getString("start_to_start.lbl"), START_TO_START));
		theDependencies.add(new DependencyType(2, IViewConstants.RB.getString("finish_to_finish.lbl"), FINISH_TO_FINISH));
		theDependencies.add(new DependencyType(3, IViewConstants.RB.getString("start_to_finish.lbl"), START_TO_FINISH));
		return theDependencies;
	}

	public String getValueById(Integer anId) {
		DependencyType theDependency = (DependencyType) super.getRowObjectByIndex(anId);
		return theDependency.getValue();
	}

	public String getValueByDescription(String aDescription) {
		String theValue = null;
		for (DependencyType theDependency : (List<DependencyType>) super.rows) {
			if (theDependency.getDescription().equals(aDescription)) {
				theValue = theDependency.getValue();
			}
		}
		return theValue;
	}

	public String getDescriptionByValue(String aValue) {
		String theDescription = null;
		for (DependencyType theDependency : (List<DependencyType>) super.rows) {
			if (theDependency.getValue().equals(aValue)) {
				theDescription = theDependency.getDescription();
			}
		}
		return theDescription;
	}

	class DependencyType {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public DependencyType(Integer anId, String aDescription, String aValue) {
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
}
