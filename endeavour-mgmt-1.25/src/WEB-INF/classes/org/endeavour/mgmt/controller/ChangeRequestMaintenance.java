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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.ChangeRequest;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class ChangeRequestMaintenance extends WorkProductMaintenance {

	public static final String TYPE = ChangeRequest.TYPE;
	public static final String STATUS = ChangeRequest.STATUS;

	private List<String> errors = null;

	public ChangeRequestMaintenance(Project aProject) {
		super.setProject(aProject);
	}

	public List<ChangeRequest> getChangeRequestsBy(String aPriority, String aStatus, String aName, String aNumber) {

		Integer theProjectId = super.getProject().getId();
		String theQuery = "select changeRequest from " + ChangeRequest.class.getSimpleName() + " changeRequest where ";

		List<Object> theParameters = new ArrayList<Object>();

		if (!aPriority.equals("All")) {
			theParameters.add(aPriority);
			theQuery = theQuery + "changeRequest.priority = ? and ";
		}
		if (!aStatus.equals("All")) {
			theParameters.add(aStatus);
			theQuery = theQuery + "changeRequest.status = ? and ";
		}

		theParameters.add("%" + aName + "%");
		theQuery = theQuery + "changeRequest.name like ? and changeRequest.project.id = " + theProjectId;

		try {
			theParameters.add(new Integer(aNumber));
			theQuery = theQuery + " and changeRequest.id = ?";
		} catch (NumberFormatException e) {

		}

		List<ChangeRequest> theChangeRequests = PersistenceManager.getInstance().findAllBy(theQuery, theParameters);
		return theChangeRequests;
	}

	public List<ChangeRequest> getChangeRequestsBy(Iteration aIteration) {
		List<ChangeRequest> theResultChangeRequests = new ArrayList<ChangeRequest>();
		List<ChangeRequest> theChangeRequests = super.getProject().retrieveWorkProducts(ChangeRequest.class);
		Iteration theIteration = null;
		for (ChangeRequest theChangeRequest : theChangeRequests) {
			theIteration = theChangeRequest.getIteration();
			if ((theIteration == null && aIteration == null) || (theIteration != null && theIteration.equals(aIteration))) {
				theResultChangeRequests.add(theChangeRequest);
			}
		}
		return theResultChangeRequests;
	}

	public Map<String, Object> getChangeRequestDataBy(Integer aChangeRequestId) {
		ChangeRequest theChangeRequest = super.getProject().retrieveWorkProduct(ChangeRequest.class, aChangeRequestId);
		super.setWorkProduct(theChangeRequest);
		Map<String, Object> theData = super.getWorkProduct().getData();
		return theData;
	}

	public List<String> saveChangeRequest(Map<String, Object> aData) {

		if (super.getWorkProduct() == null) {
			super.setWorkProduct(new ChangeRequest(super.getProject()));
		}
		super.processIterationId(aData);
		super.processProjectMemberIds(aData);
		if (this.isValid(aData)) {
			super.getWorkProduct().save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void deleteChangeRequest(Integer aChangeRequestId) {

		ChangeRequest theChangeRequest = super.getProject().retrieveWorkProduct(ChangeRequest.class, aChangeRequestId);
		super.setWorkProduct(theChangeRequest);
		super.getWorkProduct().delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = super.getWorkProduct().validate(aData);
		return this.errors.isEmpty();
	}
}
