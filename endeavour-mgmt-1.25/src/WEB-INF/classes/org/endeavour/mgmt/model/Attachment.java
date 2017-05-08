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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

import thinwire.render.web.WebApplication;

public class Attachment implements Comparable<Attachment> {

	private Integer id = null;
	private String name = null;
	private byte[] file = null;
	private WorkProduct workProduct = null;

	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String FILE = "FILE";
	public static final String FILE_NAME = "FILE_NAME";
	public static final String DOWNLOAD_INFO = "DOWNLOAD_INFO";

	public Attachment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String aDescription) {
		this.name = aDescription;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] aFile) {
		this.file = aFile;
	}

	public WorkProduct getWorkProduct() {
		return this.workProduct;
	}

	public void setWorkProduct(WorkProduct anWorkProduct) {
		this.workProduct = anWorkProduct;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		byte[] theFile = (byte[]) aData.get(FILE);
		if (theFile == null) {
			theErrors.add(IViewConstants.RB.getString("file_not_empty.msg"));
		}
		return theErrors;
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(attachment.id) from " + Attachment.class.getSimpleName() + " attachment");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}
		byte[] theFile = (byte[]) aData.get(FILE);
		if (theFile != null) {
			String theFileName = (String) aData.get(FILE_NAME);
			this.setName(theFileName);

			if (theFile != null) {
				this.setFile(theFile);
			}
		}
	}

	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());

		String theLocalHost = ((WebApplication) WebApplication.current()).clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");

		String theKey = this.getName();
		String theURL = theLocalHost + "downloadAttachment?" + ID + "=" + this.getId() + "&" + NAME + "=" + this.getName();

		Map<String, Object> theAttachmentMap = new HashMap<String, Object>();
		theAttachmentMap.put(theKey, theURL);
		theData.put(DOWNLOAD_INFO, theAttachmentMap);

		return theData;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Attachment) {
			Attachment theAttachment = (Attachment) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theAttachment.getId());
			}
		}
		return isEquals;
	}

	public int compareTo(Attachment anAttachment) {
		int theResult = -1;
		if (anAttachment != null) {
			theResult = this.getName().compareTo(anAttachment.getName());
		}
		return theResult;
	}
}
