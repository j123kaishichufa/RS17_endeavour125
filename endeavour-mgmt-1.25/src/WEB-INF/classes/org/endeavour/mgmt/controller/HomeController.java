package org.endeavour.mgmt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.IPlanElement;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

@SuppressWarnings("unchecked")
public class HomeController extends ApplicationController {

	public List<WorkProduct> getWorkProducts() {
		SecurityMaintenance theSecuritymMaintenance = SecurityMaintenance.getInstance();
		ProjectMember theProjectMember = theSecuritymMaintenance.getLoggedUser();

		// TODO Is there a better way to refresh the the workproducts after
		// saving without retrieving the project member?
		theProjectMember = (ProjectMember) PersistenceManager.getInstance().findById(ProjectMember.class, theProjectMember.getId());

		List theWorkProductsList = new ArrayList();
		for (WorkProduct theWorkProduct : theProjectMember.getWorkProducts()) {
			if (theWorkProduct != null && theWorkProduct.isActive()) {
				theWorkProductsList.add(theWorkProduct);
			}
		}

		Collections.sort(theWorkProductsList, new NameSorter());
		return theWorkProductsList;
	}

	public List<Project> getProjects() {
		SecurityMaintenance theSecuritymMaintenance = SecurityMaintenance.getInstance();
		ProjectMember theProjectMember = theSecuritymMaintenance.getLoggedUser();

		Set<Project> theProjects = theProjectMember.getProjects();
		List theProjectsList = new ArrayList();
		theProjectsList.addAll(theProjects);
		Collections.sort(theProjectsList, new NameSorter());
		return theProjectsList;
	}

	public List<ProjectMember> getProjectMembers() {
		List<ProjectMember> theProjectMembersResults = new ArrayList<ProjectMember>();
		List<ProjectMember> theProjectMembers = PersistenceManager.getInstance().findAllBy("select projectMember from " + ProjectMember.class.getSimpleName() + " projectMember order by projectMember.userId");
		for (ProjectMember theProjectMember : theProjectMembers) {
			if (theProjectMember != null) {
				String theStatus = theProjectMember.getStatus();
				if (theStatus != null && theStatus.trim().length() != 0) {
					theProjectMembersResults.add(theProjectMember);
				}
			}
		}

		return theProjectMembersResults;
	}

	public Integer getProjectIdBy(Integer aWorkProductId) {
		Integer theProjectId = null;
		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct != null) {
				if (aWorkProductId.equals(theWorkProduct.getId())) {
					theProjectId = theWorkProduct.getProject().getId();
					break;
				}
			}

		}
		return theProjectId;
	}

	public Map<String, Object> getProjectMemberData(ProjectMemberMaintenance aProjectMemberMaintenance) {
		SecurityMaintenance theSecuritymMaintenance = SecurityMaintenance.getInstance();
		ProjectMember theProjectMember = theSecuritymMaintenance.getLoggedUser();
		aProjectMemberMaintenance.setProjectMember(theProjectMember);
		return theProjectMember.getData();
	}

	class NameSorter implements Comparator<IPlanElement> {
		public int compare(IPlanElement aPlanElement1, IPlanElement aPlanElement2) {
			int theResult = -1;
			if (aPlanElement1 != null && aPlanElement2 != null) {
				theResult = aPlanElement1.getName().compareTo(aPlanElement2.getName());
			}
			return theResult;
		}
	}
}
