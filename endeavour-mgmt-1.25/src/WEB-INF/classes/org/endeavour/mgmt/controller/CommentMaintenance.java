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

import org.endeavour.mgmt.model.Comment;
import org.endeavour.mgmt.model.ProjectMember;

public class CommentMaintenance extends ApplicationController {

	public static final String TEXT = Comment.TEXT;
	public static final String AUTHOR = Comment.AUTHOR;
	public static final String DATE = Comment.DATE;

	private List<String> errors = null;
	private Comment comment = null;
	private List<Comment> comments = null;

	public CommentMaintenance() {
		this.comments = new ArrayList<Comment>();
	}

	public List<String> saveComment(Map<String, Object> aData) {
		if (this.comment == null) {
			this.comment = new Comment();
		}
		if (this.isValid(aData)) {
			Integer theId = this.comment.getId();
			this.comment.save(aData);
			this.comment.setId(theId == null ? this.produceTransientCommentId() : theId);
			this.addComment(this.comment);
			setChanged();
			notifyObservers();
		}
		return this.errors;
	}

	public boolean isCommentOwnedByUser() {
		boolean isCommentOwnedByUser = false;
		if (this.comment != null) {
			SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
			ProjectMember theLoggedUser = theSecurityMaintenance.getLoggedUser();
			if (theLoggedUser.isAdministrator()) {
				isCommentOwnedByUser = true;
			} else {
				isCommentOwnedByUser = this.comment.getAuthor().equals(theLoggedUser.getUserId());
			}
		}
		return isCommentOwnedByUser;
	}

	private void addComment(Comment aComment) {
		if (!this.comments.contains(aComment)) {
			this.comments.add(aComment);
		}
	}

	private Integer produceTransientCommentId() {
		Integer theTransientId = 0;
		for (Comment theComment : this.comments) {
			int theId = theComment.getId();
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

	public Map<String, Object> getCommentDataBy(Integer aCommentId) {
		Map<String, Object> theData = null;
		for (Comment theComment : this.getComments()) {
			if (theComment != null) {
				if (theComment.getId().equals(aCommentId)) {
					this.comment = theComment;
					theData = theComment.getData();
					break;
				}
			}
		}
		return theData;
	}

	private boolean isValid(Map<String, Object> aData) {
		this.errors = this.comment.validate(aData);
		return this.errors.isEmpty();
	}

	public void reset() {
		this.comment = null;
	}

	public void setComments(List<Comment> aComments) {
		this.comments = new ArrayList<Comment>();
		for (Comment theComment : aComments) {
			if (theComment != null) {
				this.comments.add(theComment);
			}
		}
	}

	public Integer getSelectedCommentId() {
		Integer theId = null;
		if (this.comment != null) {
			theId = this.comment.getId();
		}
		return theId;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void deleteComment(Integer aCommentId) {
		Comment theComment = null;
		for (Comment theCurrentComment : this.comments) {
			if (theCurrentComment.getId().equals(aCommentId)) {
				theComment = theCurrentComment;
				break;
			}
		}
		if (theComment != null) {
			this.comments.remove(theComment);
			setChanged();
			notifyObservers();
		}
	}
}
