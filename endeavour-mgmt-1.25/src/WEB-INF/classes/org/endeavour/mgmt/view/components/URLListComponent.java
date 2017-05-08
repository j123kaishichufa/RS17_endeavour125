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

import thinwire.ui.Hyperlink;
import thinwire.ui.layout.TableLayout;


public class URLListComponent extends PanelComponent implements Observer {

	private TableLayout tableLayout = null;
	private GridBoxModel model = null;

	public URLListComponent(GridBoxModel aModel) {

		super.setScrollType(PanelComponent.ScrollType.ALWAYS);
		this.tableLayout = new TableLayout();
		this.tableLayout.setMargin(1);
		this.tableLayout.setSpacing(1);
		super.setLayout(this.tableLayout);
		List<TableLayout.Column> theColumns = this.tableLayout.getColumns();
		theColumns.add(new TableLayout.Column(0));
		this.model = aModel;
		this.model.addObserver(this);
		this.initializeRows();
	}

	public void update(Observable aObservable, Object anObject) {
		this.initializeRows();
	}

	private void initializeRows() {

		int theRowCount = this.model.getRowCount();
		List<TableLayout.Row> theRows = this.tableLayout.getRows();
		theRows.clear();

		TableLayout.Row theRow = null;
		for (int i = 0; i < theRowCount; i++) {
			String theURL = (String) this.model.getValueAt(i, 0);
			String theKey = (String) this.model.getValueAt(i, 1);
			if (theURL != null && theKey != null) {
				theRow = new TableLayout.Row(18);
				theRows.add(theRow);
				Hyperlink theHyperlink = new Hyperlink(theKey);
				theHyperlink.setLocation(theURL);
				theRow.set(0, theHyperlink);
			}
		}
	}
}
