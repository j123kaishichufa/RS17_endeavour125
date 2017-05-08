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

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

@SuppressWarnings("unchecked")
public class TypeListModel extends GridBoxModel {

	public TypeListModel() {
		super();
		super.rows = this.initializeTypes();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Type theType = (Type) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theType.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Type theStatus = (Type) this.rows.get(aRowIndex);
		return theStatus.getId();
	}

	private List<Type> initializeTypes() {
		List<Type> theTypes = new ArrayList<Type>();
		theTypes.add(new Type(0, IViewConstants.RB.getString("use_case.lbl"), UseCase.LABEL));
		theTypes.add(new Type(1, IViewConstants.RB.getString("change_request.lbl"), ChangeRequest.LABEL));
		theTypes.add(new Type(2, IViewConstants.RB.getString("defect.lbl"), Defect.LABEL));
		theTypes.add(new Type(3, IViewConstants.RB.getString("task.lbl"), Task.LABEL));
		theTypes.add(new Type(4, IViewConstants.RB.getString("all.lbl"), WorkProduct.LABEL));
		return theTypes;
	}

	public String getValueByDescription(String aDescription) {
		String theValue = null;
		for (Type theType : (List<Type>) super.rows) {
			if (theType.getDescription().equals(aDescription)) {
				theValue = theType.getValue();
			}
		}
		return theValue;
	}

	class Type {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public Type(Integer anId, String aDescription, String aValue) {
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
