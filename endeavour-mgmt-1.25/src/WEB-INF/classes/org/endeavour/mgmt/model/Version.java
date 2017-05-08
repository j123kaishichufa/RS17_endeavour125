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

import java.util.HashMap;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;

import thinwire.render.web.WebApplication;

public class Version {

	private Integer id = null;
	private Integer number = null;
	private byte[] file = null;
	private Document document = null;

	public static final String ID = "ID";
	public static final String FILE_NAME = "FILE_NAME";
	public static final String VERSION = "VERSION";
	public static final String KEY = "KEY";
	public static final String URL = "URL";

	public Version() {
	}

	public Version(Document aDocument) {
		this.document = aDocument;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] aFile) {
		this.file = aFile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer aNumber) {
		this.number = aNumber;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document aDocument) {
		this.document = aDocument;
	}

	public void update(byte[] aFile) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(version.id) from " + Version.class.getSimpleName() + " version");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);

			int theVersionNumber = 0;
			for (Version theVersion : this.document.getVersions()) {
				if (theVersion != null) {
					theVersionNumber++;
				}
			}
			this.setNumber(theVersionNumber);
		}

		if (aFile != null) {
			this.setFile(aFile);
		}
	}

	public Map<String, Object> getData() {
		String theLocalHost = ((WebApplication) WebApplication.current()).clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");
		Map<String, Object> theData = new HashMap<String, Object>();
		String theKey = "[Ver " + this.getNumber() + "] " + this.document.getFileName();
		String theURL = theLocalHost + "downloadDocument?" + ID + "=" + this.document.getId() + "&" + FILE_NAME + "=" + this.document.getFileName() + "&" + VERSION + "=" + this.getNumber();
		theData.put(KEY, theKey);
		theData.put(URL, theURL);
		return theData;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Version) {
			Version theVersion = (Version) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theVersion.getId());
			}
		}
		return isEquals;
	}
}
