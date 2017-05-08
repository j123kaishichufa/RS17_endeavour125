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
import java.util.Map;

import org.endeavour.mgmt.model.Version;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class DocumentVersionsURLModel extends GridBoxModel {

	public DocumentVersionsURLModel() {
		super();
	}

	public DocumentVersionsURLModel(List<Version> aVersions) {
		super(aVersions);
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Version theVersion = (Version) this.rows.get(aRowIndex);
		if (theVersion != null) {
			Map<String, Object> theVersionData = theVersion.getData();
			switch (aColumnIndex) {
			case 0:
				theValue = (String) theVersionData.get(Version.URL);
				break;
			case 1:
				theValue = (String) theVersionData.get(Version.KEY);
				break;
			default:
				theValue = "";
			}
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Version theVersion = (Version) this.rows.get(aRowIndex);
		return theVersion.getId();
	}
}
