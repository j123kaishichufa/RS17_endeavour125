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

import org.endeavour.mgmt.model.Document;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.Version;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class DocumentMaintenance extends ApplicationController {

	private Document document = null;
	private List<String> errors = null;
	private Project project = null;

	public static final String DESCRIPTION = Document.DESCRIPTION;
	public static final String FILE = Document.FILE;
	public static final String FILE_NAME = Document.FILE_NAME;
	public static final String VERSIONS = Document.VERSIONS;

	public DocumentMaintenance() {
	}

	public DocumentMaintenance(Project aProject) {
		this.project = aProject;
	}

	public List<Document> getDocuments() {
		return this.project.getDocuments();
	}

	public Map<String, Object> getDocumentDataBy(Integer aDocumentId) {
		this.document = this.project.retrieveDocumentBy(aDocumentId);
		return this.document.getData();
	}

	public List<String> saveDocument(Map<String, Object> aData) {

		if (this.document == null) {
			this.document = new Document(this.project);
		}
		if (this.isValid(aData)) {
			this.document.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public void reset() {
		this.document = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteDocument(Integer aDocumentId) {

		this.document = this.project.retrieveDocumentBy(aDocumentId);
		this.document.delete();

	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.document.validate(aData);
		return this.errors.isEmpty();
	}

	public byte[] getDocumentVersionBy(Integer aDocumentId, Integer aDocumentVersion) {
		PersistenceManager thePersistenceManager = new PersistenceManager();
		thePersistenceManager.beginTransaction();
		Document theDocument = (Document) thePersistenceManager.findById(Document.class, aDocumentId);
		Version theVersion = theDocument.retrieveVersionBy(aDocumentVersion);
		byte[] theFile = theVersion.getFile();
		thePersistenceManager.endTransaction();
		return theFile;
	}

	public Integer getSelectedDocumentId() {
		Integer theId = null;
		if (this.document != null) {
			theId = this.document.getId();
		}
		return theId;
	}
}
