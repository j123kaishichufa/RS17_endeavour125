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

import org.endeavour.mgmt.model.Attachment;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class AttachmentMaintenance extends ApplicationController {

	public static final String NAME = Attachment.NAME;
	public static final String DOWNLOAD_INFO = Attachment.DOWNLOAD_INFO;
	public static final String FILE = Attachment.FILE;
	public static final String FILE_NAME = Attachment.FILE_NAME;

	private List<String> errors = null;
	private Attachment attachment = null;
	private List<Attachment> attachments = null;

	public AttachmentMaintenance() {
		this.attachments = new ArrayList<Attachment>();
	}

	public List<String> saveAttachment(Map<String, Object> aData) {
		if (this.attachment == null) {
			this.attachment = new Attachment();
		}
		if (this.isValid(aData)) {
			Integer theId = this.attachment.getId();
			this.attachment.save(aData);
			this.attachment.setId(theId == null ? this.produceTransientAttachmentId() : theId);
			this.addAttachment(this.attachment);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	private void addAttachment(Attachment aAttachment) {
		if (!this.attachments.contains(aAttachment)) {
			this.attachments.add(aAttachment);
		}
	}

	private Integer produceTransientAttachmentId() {
		Integer theTransientId = 0;
		for (Attachment theAttachment : this.attachments) {
			int theId = theAttachment.getId();
			if (theId < 0) {
				theId = theId - (theId * 2);
			}
			if (theId > theTransientId) {
				theTransientId = theId;
			}
		}
		theTransientId++;
		theTransientId = theTransientId - (theTransientId * 2);
		return theTransientId;
	}

	public Map<String, Object> getAttachmentDataBy(Integer aAttachmentId) {
		Map<String, Object> theData = null;
		for (Attachment theAttachment : this.getAttachments()) {
			if (theAttachment != null) {
				if (theAttachment.getId().equals(aAttachmentId)) {
					theData = theAttachment.getData();
					break;
				}
			}
		}
		return theData;
	}

	public byte[] getAttachmentBy(Integer aAttachmentId) {
		PersistenceManager thePersistenceManager = new PersistenceManager();
		thePersistenceManager.beginTransaction();
		Attachment theAttachment = (Attachment) thePersistenceManager.findById(Attachment.class, aAttachmentId);
		byte[] theFile = theAttachment.getFile();
		thePersistenceManager.endTransaction();
		return theFile;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.attachment.validate(aData);
		return this.errors.isEmpty();
	}

	public void reset() {
		this.attachment = null;
	}

	public void setAttachments(List<Attachment> aAttachments) {
		this.attachments = new ArrayList<Attachment>();
		for (Attachment theAttachment : aAttachments) {
			if (theAttachment != null) {
				this.attachments.add(theAttachment);
			}
		}
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void deleteAttachment(Integer aAttachment) {
		this.attachments.remove(aAttachment.intValue());
		setChanged();
		notifyObservers();
	}
}
