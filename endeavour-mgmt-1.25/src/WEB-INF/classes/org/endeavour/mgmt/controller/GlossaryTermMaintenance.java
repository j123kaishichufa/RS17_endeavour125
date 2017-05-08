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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.GlossaryTerm;
import org.endeavour.mgmt.model.Project;

public class GlossaryTermMaintenance extends ApplicationController {

	private List<String> errors = null;
	private GlossaryTerm glossaryTerm = null;
	private Project project = null;

	public static final String NAME = GlossaryTerm.NAME;
	public static final String DESCRIPTION = GlossaryTerm.DESCRIPTION;
	public static final String CREATED_BY = GlossaryTerm.CREATED_BY;

	public GlossaryTermMaintenance(Project aProject) {
		this.project = aProject;
	}

	public void reset() {
		this.glossaryTerm = null;
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.project = theProjectMaintenance.retrieveProjectBy(this.project.getId());
	}

	public void deleteGlossaryTerm(Integer aGlossaryTermId) {
		this.glossaryTerm = this.project.retrieveGlossaryTermBy(aGlossaryTermId);
		this.glossaryTerm.delete();
	}

	public Map<String, Object> getGlossaryTermDataBy(Integer aGlossaryTermId) {
		this.glossaryTerm = this.project.retrieveGlossaryTermBy(aGlossaryTermId);
		return this.glossaryTerm.getData();
	}

	public List<GlossaryTerm> getGlossaryTerms() {
		return this.project.getGlossaryTerms();
	}

	public List<String> saveGlossaryTerm(Map<String, Object> aData) {

		if (this.glossaryTerm == null) {
			this.glossaryTerm = new GlossaryTerm(this.project);
		}
		if (this.isValid(aData)) {
			this.glossaryTerm.save(aData);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.glossaryTerm.validate(aData);
		return this.errors.isEmpty();
	}

	public String createPrintPreviewLocation() {
		Map<String, String> theReportParameters = new HashMap<String, String>();
		theReportParameters.put("project_id_parameter", this.project.getId() + ":Integer");
		return super.createReportURL("glossary.pdf", "glossary.prpt", theReportParameters);
	}

	public Integer getSelectedGlossaryTermId() {
		Integer theId = null;
		if (this.glossaryTerm != null) {
			theId = this.glossaryTerm.getId();
		}
		return theId;
	}
}
