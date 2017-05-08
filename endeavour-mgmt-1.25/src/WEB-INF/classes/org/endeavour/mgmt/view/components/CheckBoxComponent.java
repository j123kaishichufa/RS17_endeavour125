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

import thinwire.ui.CheckBox;

public class CheckBoxComponent extends CheckBox {

	private String value = null;
	
	public CheckBoxComponent() {
	}

	public CheckBoxComponent(String aValue) {
		this.value = aValue;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String aValue) {
		this.value = aValue;
	}
}
