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

package org.endeavour.mgmt.view.components;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import thinwire.ui.DropDownGridBox;
import thinwire.ui.GridBox;
import thinwire.ui.GridBox.Row;

public class DropDownGridBoxComponent extends DropDownGridBox implements Observer {

	private GridBoxModel model = null;
	private Integer displayingIndex = null;

	public DropDownGridBoxComponent(GridBoxModel aModel, Integer aDisplayingIndex) {
		this.model = aModel;
		this.displayingIndex = aDisplayingIndex;
		super.setEditAllowed(false);
		this.model.addObserver(this);
		this.initializeRows();
	}

	public void update(Observable aObservable, Object anObject) {
		this.initializeRows();
	}

	private void initializeRows() {

		int theRowCount = this.model.getRowCount();
		GridBox theGridBox = super.getComponent();
		theGridBox.getRows().clear();

		Object[] theRowData = null;
		for (int i = 0; i < theRowCount; i++) {
			theRowData = new Object[1];
			theRowData[0] = this.model.getValueAt(i, this.displayingIndex);
			GridBox.Row theRow = new GridBox.Row(theRowData);
			theRow.setUserObject(this.model.getRowId(i));
			theGridBox.getRows().add(theRow);
		}
	}

	public void setSelectedRowObject(Integer anId) {
		int theRowCount = this.model.getRowCount();
		for (int i = 0; i < theRowCount; i++) {
			if (this.model.getRowId(i) == anId) {
				Row theRow = super.getComponent().getRows().get(i);
				Object[] theRowData = theRow.toArray();
				String theValue = theRowData[0].toString();
				super.setText(theValue);
				break;
			}
		}
	}

	public void selectFirstElement() {
		List<Row> theRows = super.getComponent().getRows();
		if (theRows.size() > 0) {
			Row theRow = theRows.get(0);
			Object[] theRowData = theRow.toArray();
			String theValue = theRowData[0].toString();
			super.setText(theValue);
		} else {
			super.setText(null);
		}
	}

	public Integer getSelectedRowId() {
		Integer theSelectedRow = null;
		if (!super.getText().equals("")) {
			theSelectedRow = (Integer) super.getComponent().getSelectedRow().getUserObject();
		}
		return theSelectedRow;
	}

	public GridBoxModel getModel() {
		return this.model;
	}
}
