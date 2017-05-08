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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import thinwire.ui.GridBox;

public class GridBoxComponent extends GridBox implements Observer {

	private GridBoxModel model = null;

	public GridBoxComponent(GridBoxModel aModel) {
		super.setVisibleHeader(true);
		this.model = aModel;
		this.model.addObserver(this);

		this.initializeColumns();
		this.initializeRows();
	}

	public void update(Observable aObservable, Object anObject) {
		this.initializeRows();
	}

	private void initializeColumns() {

		super.getColumns().clear();

		int theColumnCount = model.getColumnCount();
		GridBox.Column theColumn = null;
		for (int i = 0; i < theColumnCount; i++) {
			theColumn = new GridBox.Column();
			theColumn.setName(model.getColumnName(i));
			super.getColumns().add(theColumn);
		}
	}

	private void initializeRows() {
		Integer theSelectedRowId = this.getSelectedRow() != null ? (Integer) this.getSelectedRow().getUserObject() : null;
		super.getRows().clear();

		int theRowCount = this.model.getRowCount();
		int theColumnCount = this.model.getColumnCount();
		GridBox.Row theRow = null;
		Object[] theRowData = null;

		for (int theRi = 0; theRi < theRowCount; theRi++) {
			if (!this.model.isRowNull(theRi)) {
				theRowData = new Object[theColumnCount];
				for (int theCi = 0; theCi < theColumnCount; theCi++) {
					theRowData[theCi] = this.model.getValueAt(theRi, theCi);
				}
				theRow = new GridBox.Row(theRowData);
				theRow.setUserObject(this.model.getRowId(theRi));
				super.getRows().add(theRow);
			}
		}
		if(theSelectedRowId != null) {
			this.setSelectedRowById(theSelectedRowId);
		} else {
			this.setSelectedRowByIndex(0);
		}
	}

	public void setSelectedRowByIndex(Integer aRowIndex) {
		if (aRowIndex < this.getRows().size()) {
			Row theRow = (Row) this.getRows().get(aRowIndex);
			theRow.setSelected(true);
		}
	}

	public void setSelectedRowById(Integer aRowId) {
		for (Row theRow : this.getRows()) {
			if (theRow.getUserObject().equals(aRowId)) {
				theRow.setSelected(true);
				break;
			}
		}
	}

	public Integer getSelectedRowId() {
		Integer theSelectedRow = null;
		if(super.getSelectedRow() != null) {
			theSelectedRow = (Integer) super.getSelectedRow().getUserObject();	
		}
		return theSelectedRow;
	}

	public List<Integer> getAllRowIds() {
		List<Integer> theRowIds = new ArrayList<Integer>();
		for (GridBox.Row theRow : super.getRows()) {
			theRowIds.add((Integer) theRow.getUserObject());
		}
		return theRowIds;
	}

	public void setColumnWidth(int aColumnIndex, int aWidth) {
		GridBox.Column theColumn = super.getColumns().get(aColumnIndex);
		theColumn.setWidth(aWidth);
	}

	public void add(GridBox.Row aRow, Object anObject) {
		this.model.add(anObject);
		super.getRows().add(aRow);
	}

	public Object getSelectedRowObject() {
		return this.model.getRowObjectByIndex(super.getSelectedRow().getIndex());
	}

	public void remove(GridBox.Row aRow, Object anObject) {
		this.model.remove(anObject);
		super.getRows().remove(aRow);
	}
}
