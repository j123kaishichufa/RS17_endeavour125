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
package org.endeavour.mgmt.controller;

import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.SecurityGroup;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class SecurityGroupMaintenance extends ApplicationController {

	public static final String NAME = SecurityGroup.NAME;
	public static final String PRIVILEGES = SecurityGroup.PRIVILEGES;

	private List<String> errors = null;
	private SecurityGroup securityGroup = null;

	public void reset() {
		this.securityGroup = null;
	}

	public void deleteSecurityGroup(Integer aSecurityGroupId) {
		this.securityGroup = this.retrieveSecurityGroupBy(aSecurityGroupId);
		this.securityGroup.delete();
	}

	public Map<String, Object> getSecurityGroupDataBy(Integer aSecurityGroupId) {
		this.securityGroup = this.retrieveSecurityGroupBy(aSecurityGroupId);
		return this.securityGroup.getData();
	}

	public List<SecurityGroup> getSecurityGroups() {
		List<SecurityGroup> theSecurityGroups = PersistenceManager.getInstance().findAllBy("select securityGroup from " + SecurityGroup.class.getSimpleName() + " securityGroup order by securityGroup.name");
		return theSecurityGroups;
	}

	public List<String> saveSecurityGroup(Map<String, Object> aData) {

		if (this.securityGroup == null) {
			this.securityGroup = new SecurityGroup();
		}
		if (this.isValid(aData)) {
			this.securityGroup.save(aData);
			setChanged();
		}
		notifyObservers();
		return this.errors;
	}

	private SecurityGroup retrieveSecurityGroupBy(Integer aSecurityGroupId) {
		SecurityGroup theSecurityGroup = (SecurityGroup) PersistenceManager.getInstance().findById(SecurityGroup.class, aSecurityGroupId);
		return theSecurityGroup;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.securityGroup.validate(aData);
		return this.errors.isEmpty();
	}

	public Integer getSelectedSecurityGroupId() {
		Integer theId = null;
		if (this.securityGroup != null) {
			theId = this.securityGroup.getId();
		}
		return theId;
	}

	public Integer getFirstSecurityGroupId() {
		Integer theSecurityGroupId = null;
		List<SecurityGroup> theSecurityGroups = this.getSecurityGroups();
		for (SecurityGroup theCurrentSecuritGroup : theSecurityGroups) {
			if (theCurrentSecuritGroup != null) {
				theSecurityGroupId = theCurrentSecuritGroup.getId();
				break;
			}
		}
		return theSecurityGroupId;
	}
}
