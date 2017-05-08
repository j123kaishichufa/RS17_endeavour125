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

import org.endeavour.mgmt.model.Document;
import org.endeavour.mgmt.model.Project;

public class DocumentAssignmentMaintenance extends ApplicationController {
	private List<Document> assignedDocuments = null;
	private List<Document> unassignedDocuments = null;
	private Project project = null;

	public DocumentAssignmentMaintenance(Project aProject) {
		this.assignedDocuments = new ArrayList<Document>();
		this.unassignedDocuments = new ArrayList<Document>();
		this.project = aProject;
	}

	public void setAssignedDocuments(List<Document> anAssignedDocuments) {
		this.assignedDocuments = anAssignedDocuments;
	}

	public void setUnAssignedDocuments(List<Document> aUnassignedDocuments) {
		this.unassignedDocuments = aUnassignedDocuments;
	}

	public List<Document> getUnassignedDocuments() {
		return this.unassignedDocuments;
	}

	public List<Document> getAssignedDocuments() {
		return this.assignedDocuments;
	}

	public void updateDocuments(List<Integer> aSelectedDocumentIds, List<Integer> aUnselectedDocumentIds) {
		this.assignedDocuments = new ArrayList<Document>();

		Document theSelectedDocument = null;
		for (Integer theSelectedDocumentId : aSelectedDocumentIds) {
			theSelectedDocument = this.project.retrieveDocumentBy(theSelectedDocumentId);
			this.assignedDocuments.add(theSelectedDocument);

		}

		List<Document> theUnselectedDocuments = new ArrayList<Document>();
		Document theUnselectedDocument = null;
		for (Integer theUnselectedDocumentId : aUnselectedDocumentIds) {
			theUnselectedDocument = this.project.retrieveDocumentBy(theUnselectedDocumentId);
			theUnselectedDocuments.add(theUnselectedDocument);
		}
		this.unassignedDocuments = theUnselectedDocuments;

		setChanged();
		notifyObservers();
	}

}
