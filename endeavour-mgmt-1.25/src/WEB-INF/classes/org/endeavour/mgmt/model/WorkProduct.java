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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.email.EmailCourier;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.model.StatusListModel;

@SuppressWarnings("unchecked")
public class WorkProduct implements IPlanElement {

	private Integer id = null;
	private String name = null;
	private Project project = null;
	private Date startDate = null;
	private Date endDate = null;
	private String createdBy = null;
	private String description = null;
	private Integer progress = null;
	private Iteration iteration = null;
	private String status = null;
	private String priority = null;
	private String label = null;

	private Set<ProjectMember> projectMembers = null;
	private Set<Document> documents = null;
	private List<Attachment> attachments = null;
	private List<Comment> comments = null;

	public static final String NAME = "NAME";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String PROGRESS = "PROGRESS";
	public static final String PROJECT_MEMBERS = "PROJECT_MEMBERS";
	public static final String DOCUMENTS = "DOCUMENTS";
	public static final String COMMENTS = "COMMENTS";
	public static final String ATTACHMENTS = "ATTACHMENTS";
	public static final String CREATED_BY = "CREATED_BY";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String ITERATION = "ITERATION";
	public static final String STATUS = "STATUS";
	public static final String PRIORITY = "PRIORITY";
	public static final String LABEL = "All";
	public static final String CURRENT_ATTACHMENT_ID = "CURRENT_ATTACHMENT_ID";
	public static final String CURRENT_COMMENT_ID = "CURRENT_COMMENT_ID";

	public Integer getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public Project getProject() {
		return this.project;
	}

	public Set<ProjectMember> getProjectMembers() {
		if (this.projectMembers == null) {
			this.projectMembers = new HashSet<ProjectMember>();
		}
		return this.projectMembers;
	}

	public Iteration getIteration() {
		return this.iteration;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public void setProject(Project aProject) {
		this.project = aProject;
	}

	public void setProjectMembers(Set<ProjectMember> aProjectMebmers) {
		this.projectMembers = aProjectMebmers;
	}

	public void setIteration(Iteration aIteration) {
		this.iteration = aIteration;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date aStartDate) {
		this.startDate = aStartDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date aEndDate) {
		this.endDate = aEndDate;
	}

	public Integer getProgress() {
		return this.progress;
	}

	public void setProgress(Integer aProgress) {
		this.progress = aProgress;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String aStatus) {
		this.status = aStatus;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String aPriority) {
		this.priority = aPriority;
	}

	public Set<Document> getDocuments() {
		if (this.documents == null) {
			this.documents = new HashSet<Document>();
		}
		return this.documents;
	}

	public void setDocuments(Set<Document> aDocuments) {
		this.documents = aDocuments;
	}

	public void setLabel(String aLabel) {
		this.label = aLabel;
	}

	public String getLabel() {
		return this.label;
	}

	public List<Attachment> getAttachments() {
		if (this.attachments == null) {
			this.attachments = new ArrayList<Attachment>();
		}
		return this.attachments;
	}

	public void setAttachments(List<Attachment> aAttachments) {
		this.attachments = aAttachments;
	}

	public List<Comment> getComments() {
		if (this.comments == null) {
			this.comments = new ArrayList<Comment>();
		}
		return this.comments;
	}

	public void setComments(List<Comment> aComments) {
		this.comments = aComments;
	}

	public void setCreatedBy(String aCreatedBy) {
		this.createdBy = aCreatedBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public String getElementType() {
		return "";
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(workProduct.id) from " + WorkProduct.class.getSimpleName() + " workProduct");
			if (theId == null) {
				theId = 0;
			} else {
				theId++;
			}
			this.setId(theId);
		}
		String theName = (String) aData.get(NAME);
		if (theName != null) {
			this.setName(theName);
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription != null) {
			this.setDescription(theDescription);
		}

		Iteration theIteration = (Iteration) aData.get(ITERATION);
		this.setIteration(theIteration);

		String theCreatedBy = (String) aData.get(CREATED_BY);
		if (theCreatedBy != null) {
			this.setCreatedBy(theCreatedBy);
		}

		Date theStartDate = (Date) aData.get(START_DATE);
		if (theStartDate != null) {
			this.setStartDate(theStartDate);
		}

		Date theEndDate = (Date) aData.get(END_DATE);
		if (theEndDate != null) {
			this.setEndDate(theEndDate);
		}

		Integer theProgress = (Integer) aData.get(PROGRESS);
		if (theProgress != null) {
			this.setProgress(theProgress * 5);
		}

		String thePriority = (String) aData.get(PRIORITY);
		if (thePriority != null) {
			this.setPriority(thePriority);
		}

		List<ProjectMember> theProjectMembers = (List) aData.get(PROJECT_MEMBERS);
		if (theProjectMembers != null) {
			this.updateProjectMembers(theProjectMembers);
		}

		List<Document> theDocuments = (List) aData.get(DOCUMENTS);
		if (theDocuments != null) {
			this.updateDocuments(theDocuments);
		}

		List<Attachment> theAttachments = (List) aData.get(ATTACHMENTS);
		if (theAttachments != null) {
			Integer theCurrentAttachmentId = this.updateAttachments(theAttachments);
			aData.put(CURRENT_ATTACHMENT_ID, theCurrentAttachmentId);
		}

		List<Comment> theComments = (List) aData.get(COMMENTS);
		if (theComments != null) {
			Integer theCurrentCommentId = this.updateComments(theComments);
			aData.put(CURRENT_COMMENT_ID, theCurrentCommentId);
		}
	}

	private Integer updateAttachments(List<Attachment> aAttachments) {

		List<Attachment> theAttachments = this.getAttachments();
		for (Attachment theAttachment : theAttachments) {
			if (theAttachment != null && !aAttachments.contains(theAttachment)) {
				theAttachments.set(theAttachments.indexOf(theAttachment), null);
				theAttachment.delete();
			}
		}
		theAttachments.clear();
		Integer theAttachmentId = (Integer) PersistenceManager.getInstance().findBy("select max(attachment.id) from " + Attachment.class.getSimpleName() + " attachment");
		theAttachmentId = theAttachmentId == null ? 0 : theAttachmentId;
		for (Attachment theAttachment : aAttachments) {
			if (theAttachment != null) {
				if (theAttachment.getId() < 0) {
					theAttachmentId++;
					theAttachment.setId(theAttachmentId);
				}
				this.addAttachment(theAttachment);
			}
		}
		return theAttachmentId;
	}

	private Integer updateComments(List<Comment> aComments) {

		List<Comment> theComments = this.getComments();
		for (Comment theComment : theComments) {
			if (theComment != null && !aComments.contains(theComment)) {
				theComments.set(theComments.indexOf(theComment), null);
				theComment.delete();
			}
		}
		theComments.clear();

		Integer theCommentId = (Integer) PersistenceManager.getInstance().findBy("select max(comment.id) from " + Comment.class.getSimpleName() + " comment");
		theCommentId = theCommentId == null ? 0 : theCommentId;
		for (Comment theComment : aComments) {
			if (theComment != null) {
				if (theComment.getId() < 0) {
					theCommentId++;
					theComment.setId(theCommentId);
				}
				this.addComment(theComment);
			}
		}
		return theCommentId;
	}

	protected void addAttachment(Attachment aAttachment) {
		List<Attachment> theAttachments = this.getAttachments();
		if (!theAttachments.contains(aAttachment)) {
			aAttachment.setWorkProduct(this);
			theAttachments.add(aAttachment);
		}
	}

	protected void addComment(Comment aComment) {
		List<Comment> theComments = this.getComments();
		if (!theComments.contains(aComment)) {
			aComment.setWorkProduct(this);
			theComments.add(aComment);
		}
	}

	private void updateProjectMembers(List<ProjectMember> aNewProjectMembers) {

		Set<ProjectMember> theCurrentProjectMembers = this.getProjectMembers();
		if (!(theCurrentProjectMembers.containsAll(aNewProjectMembers) && theCurrentProjectMembers.size() == aNewProjectMembers.size())) {
			EmailCourier theCourier = new EmailCourier(aNewProjectMembers, new HashSet<ProjectMember>(theCurrentProjectMembers), this.getLabel(), this.getId(), this.getDescription(), this.getName());
			theCourier.start();
			for (ProjectMember theProjectMember : theCurrentProjectMembers) {
				if (theProjectMember != null && !aNewProjectMembers.contains(theProjectMember)) {
					theProjectMember.removeWorkProduct(this);
				}
			}
			theCurrentProjectMembers.clear();
			for (ProjectMember theProjectMember : aNewProjectMembers) {
				if (theProjectMember != null) {
					this.addProjectMember(theProjectMember);
				}
			}
		}
	}

	private void updateDocuments(List<Document> aDocuments) {

		Set<Document> theDocuments = this.getDocuments();
		for (Document theDocument : theDocuments) {
			if (theDocument != null && !aDocuments.contains(theDocument)) {
				theDocument.removeWorkProduct(this);
			}
		}
		theDocuments.clear();
		for (Document theDocument : aDocuments) {
			if (theDocument != null) {
				this.addDocument(theDocument);
			}
		}
	}

	private void addProjectMember(ProjectMember aProjectMember) {
		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		if (!theProjectMembers.contains(aProjectMember)) {
			theProjectMembers.add(aProjectMember);
			aProjectMember.addWorkProduct(this);
		}
	}

	public void removeProjectMember(ProjectMember aProjectMember) {
		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		if (theProjectMembers.contains(aProjectMember)) {
			theProjectMembers.remove(aProjectMember);
		}
	}

	private void addDocument(Document aDocument) {
		Set<Document> theDocuments = this.getDocuments();
		if (!theDocuments.contains(aDocument)) {
			theDocuments.add(aDocument);
			aDocument.addWorkProduct(this);
		}
	}

	public void removeDocument(Document aDocument) {
		Set<Document> theDocuments = this.getDocuments();
		if (theDocuments.contains(aDocument)) {
			theDocuments.remove(aDocument);
		}
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(DESCRIPTION, this.getDescription());
		theData.put(START_DATE, this.getStartDate());
		theData.put(END_DATE, this.getEndDate());
		theData.put(PROGRESS, this.getProgress() + "%");
		theData.put(COMMENTS, this.getComments());
		theData.put(CREATED_BY, this.getCreatedBy());
		theData.put(STATUS, this.getStatus());
		theData.put(PRIORITY, this.getPriority());
		theData.put(ITERATION, this.getIteration()!= null ? this.getIteration().getId() : -1);

		List theProjectMembers = new ArrayList<ProjectMember>();
		theProjectMembers.addAll(this.getProjectMembers());
		theData.put(PROJECT_MEMBERS, theProjectMembers);

		List theDocuments = new ArrayList<Document>();
		theDocuments.addAll(this.getDocuments());
		theData.put(DOCUMENTS, theDocuments);

		List<Attachment> theAttachments = new ArrayList<Attachment>();
		for (Attachment theAttachment : this.getAttachments()) {
			if (theAttachment != null) {
				theAttachments.add(theAttachment);
			}
		}
		theData.put(ATTACHMENTS, theAttachments);

		return theData;
	}

	protected Date getParentStartDate(Map<String, Object> aData) {
		Iteration theIteration = (Iteration) aData.get(ITERATION);
		Date theParentStartDate = theIteration != null ? theIteration.getStartDate() : this.getProject().getStartDate();
		return theParentStartDate;
	}

	protected Date getParentEndDate(Map<String, Object> aData) {
		Iteration theIteration = (Iteration) aData.get(ITERATION);
		Date theParentEndDate = theIteration != null ? theIteration.getEndDate() : this.getProject().getEndDate();
		return theParentEndDate;
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription == null || theDescription.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("description_not_empty.msg"));
		}

		Date theParentStartDate = this.getParentStartDate(aData);
		Date theParentEndDate = this.getParentEndDate(aData);

		Date theStartDate = (Date) aData.get(START_DATE);
		Date theEndDate = (Date) aData.get(END_DATE);

		if (theStartDate != null) {
			if (theStartDate.compareTo(theParentStartDate) < 0 || theStartDate.compareTo(theParentEndDate) > 0) {
				theErrors.add(IViewConstants.RB.getString("start_date_between.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(theParentStartDate) + " " + IViewConstants.RB.getString("and.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(theParentEndDate));
			}
		}

		if (theEndDate != null) {
			if (theEndDate.compareTo(theParentEndDate) > 0 || theEndDate.compareTo(theParentStartDate) < 0) {
				theErrors.add(IViewConstants.RB.getString("end_date_between.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(theParentStartDate) + " " + IViewConstants.RB.getString("and.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(theParentEndDate));
			}
			if (theStartDate != null) {
				if (theEndDate.compareTo(theStartDate) < 0) {
					theErrors.add(IViewConstants.RB.getString("end_date_not_before_start_date.msg"));
				}
			}
		}

		if (theStartDate == null) {
			theErrors.add(IViewConstants.RB.getString("start_date_not_empty.msg"));
		}

		if (theEndDate == null) {
			theErrors.add(IViewConstants.RB.getString("end_date_not_empty.msg"));
		}

		return theErrors;
	}

	public void delete() {
		this.getProject().removeWorkProduct(this);
		Iteration theIteration = this.getIteration();
		if (theIteration != null) {
			theIteration.removeWorkProduct(this);
		}

		updateProjectMembers(new ArrayList<ProjectMember>());
		updateDocuments(new ArrayList<Document>());

		PersistenceManager.getInstance().delete(this);
	}

	public Attachment retrieveAttachmentBy(Integer aAttachmentId) {
		Attachment theAttachment = null;
		for (Attachment theCurrentAttachment : this.getAttachments()) {
			if (theCurrentAttachment != null) {
				if (theCurrentAttachment.getId().equals(aAttachmentId)) {
					theAttachment = theCurrentAttachment;
					break;
				}
			}
		}
		return theAttachment;
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof WorkProduct) {
			WorkProduct theWorkProduct = (WorkProduct) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theWorkProduct.getId());
			}
		}
		return isEquals;
	}

	public String getFirstStakeHolder() {
		String theStakeholder = "-";
		for (ProjectMember theProjectMember : this.getProjectMembers()) {
			if (theProjectMember != null) {
				theStakeholder = theProjectMember.getUserId();
				break;
			}
		}
		return theStakeholder;
	}

	public boolean isActive() {
		String theStatus = this.getStatus();
		return theStatus.equals(StatusListModel.PENDING) || theStatus.equals(StatusListModel.IN_PROGRESS) || theStatus.equals(StatusListModel.REOPENED);
	}
}
