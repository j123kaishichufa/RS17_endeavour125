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

import java.util.List;

import org.endeavour.mgmt.model.Dependency;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class DependenciesListModel extends GridBoxModel {

	public DependenciesListModel() {
		super();
	}

	public DependenciesListModel(List<Dependency> aDependencies) {
		super(aDependencies);
	}

	public void initializeColumns() {
		super.columns = new String[] { IViewConstants.RB.getString("task_predecessor.lbl"), IViewConstants.RB.getString("type.lbl") };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Dependency theDependency = (Dependency) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theDependency.getPredecessor().getName();
			break;
		case 1:
			theValue = theDependency.getType();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Dependency theDependency = (Dependency) this.rows.get(aRowIndex);
		return theDependency.getId();
	}
}