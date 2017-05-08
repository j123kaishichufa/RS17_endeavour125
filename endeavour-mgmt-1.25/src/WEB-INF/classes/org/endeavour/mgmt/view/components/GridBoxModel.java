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
import java.util.Collections;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("unchecked")
public abstract class GridBoxModel extends Observable {

	protected String[] columns = null;
	protected List rows = null;

	public GridBoxModel() {
		this.initializeColumns();
		this.rows = new ArrayList();
	}

	public GridBoxModel(List aListData) {
		this();
		this.initializeRows(aListData);
	}

	private void initializeRows(List aListData) {
		boolean isSortable = false;
		this.rows = new ArrayList();
		for (Object theObject : aListData) {
			if (theObject != null) {
				isSortable = theObject instanceof Comparable;
				this.rows.add(theObject);
			}
		}

		if (isSortable) {
			Collections.sort(this.rows);
		}
	}

	public void setData(List aData) {
		this.initializeRows(aData);
		super.setChanged();
		super.notifyObservers();
	}

	public int getRowCount() {
		return this.rows == null ? 0 : this.rows.size();
	}

	public int getColumnCount() {
		return this.columns.length;
	}

	public String getColumnName(int aColumnIndex) {
		return this.columns[aColumnIndex];
	}

	public boolean isRowNull(int aRowIndex) {
		return this.rows.get(aRowIndex) == null;
	}

	public void add(Object anObject) {
		this.rows.add(anObject);
	}

	public void remove(Object anObject) {
		this.rows.remove(anObject);
	}

	public Object getRowObjectByIndex(int aIndex) {
		return this.rows.get(aIndex);
	}

	public List getRows() {
		return this.rows;
	}

	public abstract Object getValueAt(int aRowIndex, int aColumnIndex);

	public abstract int getRowId(int aRowIndex);

	public abstract void initializeColumns();
}
