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

package org.endeavour.mgmt.model;

import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class Privilege {

	private Integer id = null;
	private String value = null;
	private SecurityGroup securityGroup = null;

	public Privilege() {
	}

	public Privilege(Integer anId, String aValue, SecurityGroup aGroup) {
		this.setId(anId);
		this.setValue(aValue);
		this.setSecurityGroup(aGroup);
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String aValue) {
		this.value = aValue;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public SecurityGroup getSecurityGroup() {
		return this.securityGroup;
	}

	public void setSecurityGroup(SecurityGroup aGroup) {
		this.securityGroup = aGroup;
	}
	
	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}
}
