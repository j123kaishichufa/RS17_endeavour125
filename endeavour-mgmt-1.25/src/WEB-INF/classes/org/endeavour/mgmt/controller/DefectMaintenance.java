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

import org.endeavour.mgmt.model.Defect;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class DefectMaintenance extends WorkProductMaintenance {

	private List<String> errors = null;

	public DefectMaintenance(Project aProject) {
		super.setProject(aProject);
	}

	public List<Defect> getDefectsBy(String aPriority, String aStatus, String aName, String aNumber) {

		Integer theProjectId = super.getProject().getId();
		String theQuery = "select defect from " + Defect.class.getSimpleName() + " defect where ";

		List<Object> theParameters = new ArrayList<Object>();

		if (!aPriority.equals("All")) {
			theParameters.add(aPriority);
			theQuery = theQuery + "defect.priority = ? and ";
		}
		if (!aStatus.equals("All")) {
			theParameters.add(aStatus);
			theQuery = theQuery + "defect.status = ? and ";
		}

		theParameters.add("%" + aName + "%");
		theQuery = theQuery + "defect.name like ? and defect.project.id = " + theProjectId;

		try {
			theParameters.add(new Integer(aNumber));
			theQuery = theQuery + " and defect.id = ?";
		} catch (NumberFormatException e) {

		}

		List<Defect> theDefects = PersistenceManager.getInstance().findAllBy(theQuery, theParameters);
		return theDefects;
	}

	public List<Defect> getDefectsBy(Iteration aIteration) {

		List<Defect> theResultDefects = new ArrayList<Defect>();
		List<Defect> theDefects = super.getProject().retrieveWorkProducts(Defect.class);
		Iteration theIteration = null;
		for (Defect theDefect : theDefects) {
			theIteration = theDefect.getIteration();
			if ((theIteration == null && aIteration == null) || (theIteration != null && theIteration.equals(aIteration))) {
				theResultDefects.add(theDefect);
			}
		}
		return theResultDefects;
	}

	public Map<String, Object> getDefectDataBy(Integer aDefectId) {
		Defect theDefect = (Defect) super.getProject().retrieveWorkProduct(Defect.class, aDefectId);
		super.setWorkProduct(theDefect);
		Map<String, Object> theData = super.getWorkProduct().getData();
		return theData;
	}

	public List<String> saveDefect(Map<String, Object> aData) {

		if (super.getWorkProduct() == null) {
			super.setWorkProduct(new Defect(super.getProject()));
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

	public void deleteDefect(Integer aDefectId) {

		Defect theDefect = (Defect) super.getProject().retrieveWorkProduct(Defect.class, aDefectId);
		super.setWorkProduct(theDefect);
		super.getWorkProduct().delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = super.getWorkProduct().validate(aData);
		return this.errors.isEmpty();
	}
}
