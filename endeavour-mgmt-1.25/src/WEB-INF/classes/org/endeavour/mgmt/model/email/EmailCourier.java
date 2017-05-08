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
package org.endeavour.mgmt.model.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.view.IViewConstants;

public class EmailCourier extends Thread {

	private Integer id = null;
	private String label = null;
	private String description = null;
	private String name = null;
	private List<ProjectMember> newProjectMembers = null;
	private Set<ProjectMember> currentProjectMembers = null;

	public EmailCourier(List<ProjectMember> aNewProjectMembers, Set<ProjectMember> aCurrentProjectMembers, String aLabel, Integer anId, String aDescription, String aName) {
		this.newProjectMembers = aNewProjectMembers;
		this.currentProjectMembers = aCurrentProjectMembers;
		this.id = anId;
		this.description = aDescription;
		this.name = aName;
		this.label = aLabel;
	}

	public void run() {
		Email theEmail = Email.getInstance();
		if (theEmail.isSetupCorrect()) {
			try {
				String theAssignment = IViewConstants.RB.getString("assignment_notifiaction.msg");
				String theNameAndNumber = IViewConstants.RB.getString("name_and_number.msg");
				String theUnassignment = IViewConstants.RB.getString("unassigment_notification.msg");
				String theDescription = IViewConstants.RB.getString("description.lbl");
				String theProject = IViewConstants.RB.getString("project.lbl");

				List<ProjectMember> theAssignedRecipients = new ArrayList<ProjectMember>();
				for (ProjectMember theProjectMember : this.newProjectMembers) {
					if (theProjectMember != null) {
						if (!this.currentProjectMembers.contains(theProjectMember)) {
							if (theProjectMember.isAcceptNotifications()) {
								theAssignedRecipients.add(theProjectMember);
							}
						}
					}
				}
				if (!theAssignedRecipients.isEmpty()) {

					String theSubject = theAssignment + ": " + this.label + " " + this.id;
					StringBuffer theMessage = new StringBuffer();
					theMessage.append(theAssignment);
					theMessage.append("\n\n");
					theMessage.append(theNameAndNumber + ": " + this.label + " ");
					theMessage.append(this.id);
					theMessage.append("\n");
					theMessage.append(theProject + ": ");
					theMessage.append(this.name);
					theMessage.append("\n");
					theMessage.append(theDescription + ": ");
					theMessage.append(this.description);

					theEmail.send(theAssignedRecipients, theSubject, theMessage.toString());
				}
				List<ProjectMember> theUnassignedRecipients = new ArrayList<ProjectMember>();
				for (ProjectMember theProjectMember : this.currentProjectMembers) {
					if (theProjectMember != null) {
						if (!this.newProjectMembers.contains(theProjectMember)) {
							if (theProjectMember.isAcceptNotifications()) {
								theUnassignedRecipients.add(theProjectMember);
							}
						}
					}
				}
				if (!theUnassignedRecipients.isEmpty()) {

					String theSubject = theUnassignment + ": " + this.label + " " + this.id;
					StringBuffer theMessage = new StringBuffer();
					theMessage.append(theUnassignment);
					theMessage.append("\n\n");
					theMessage.append(theNameAndNumber + ": " + this.label + " ");
					theMessage.append(this.id);
					theMessage.append("\n");
					theMessage.append(theProject + ": ");
					theMessage.append(this.name);
					theMessage.append("\n");
					theMessage.append(theDescription + ": ");
					theMessage.append(this.description);

					theEmail.send(theUnassignedRecipients, theSubject, theMessage.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			theEmail.endSession();
		}
	}
}