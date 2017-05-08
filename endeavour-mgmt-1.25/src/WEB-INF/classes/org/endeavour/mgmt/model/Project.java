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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class Project implements IPlanElement {

	private Integer id = null;
	private String name = null;
	private String description = null;
	private Date startDate = null;
	private Date endDate = null;
	private String status = null;
	private String createdBy = null;
	private List<Iteration> iterations = null;
	private List<WorkProduct> workProducts = null;
	private List<TestCase> testCases = null;
	private List<Document> documents = null;
	private List<Actor> actors = null;
	private List<GlossaryTerm> glossaryTerms = null;
	private List<TestPlan> testPlans = null;
	private Set<ProjectMember> projectMembers = null;

	public static final String NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String STATUS = "STATUS";
	public static final String PROGRESS = "PROGRESS";
	public static final String PROJECT_MEMBERS = "PROJECT_MEMBERS";
	public static final String GLOSSARY_TERMS = "GLOSSARY_TERMS";
	public static final String CREATED_BY = "CREATED_BY";

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer anId) {
		this.id = anId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String aDescription) {
		this.description = aDescription;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date aStartDate) {
		this.startDate = aStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String aStatus) {
		this.status = aStatus;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date aEndDate) {
		this.endDate = aEndDate;
	}

	public void setCreatedBy(String aCreatedBy) {
		this.createdBy = aCreatedBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public List<WorkProduct> getWorkProducts() {
		if (this.workProducts == null) {
			this.workProducts = new ArrayList<WorkProduct>();
		}
		return this.workProducts;
	}

	public void setWorkProducts(List<WorkProduct> aWorkProducts) {
		this.workProducts = aWorkProducts;
	}

	public List<GlossaryTerm> getGlossaryTerms() {
		if (this.glossaryTerms == null) {
			this.glossaryTerms = new ArrayList<GlossaryTerm>();
		}
		return this.glossaryTerms;
	}

	public void setGlossaryTerms(List<GlossaryTerm> aGlossaryTerms) {
		this.glossaryTerms = aGlossaryTerms;
	}

	public List<Iteration> getIterations() {
		if (this.iterations == null) {
			this.iterations = new ArrayList<Iteration>();
		}
		Collections.sort(this.iterations, new IterationsComparator());
		return this.iterations;
	}

	public void setIterations(List<Iteration> aIterations) {
		this.iterations = aIterations;
	}

	public Set<ProjectMember> getProjectMembers() {
		if (this.projectMembers == null) {
			this.projectMembers = new HashSet<ProjectMember>();
		}
		return this.projectMembers;
	}

	public void setProjectMembers(Set<ProjectMember> aProjectMembers) {
		this.projectMembers = aProjectMembers;
	}

	public List<TestCase> getTestCases() {
		if (this.testCases == null) {
			this.testCases = new ArrayList<TestCase>();
		}
		return this.testCases;
	}

	public void setTestCases(List<TestCase> aTestCases) {
		this.testCases = aTestCases;
	}

	public List<Document> getDocuments() {
		if (this.documents == null) {
			this.documents = new ArrayList<Document>();
		}
		return this.documents;
	}

	public void setDocuments(List<Document> aDocuments) {
		this.documents = aDocuments;
	}

	public List<Actor> getActors() {
		if (this.actors == null) {
			this.actors = new ArrayList<Actor>();
		}
		return this.actors;
	}

	public void setActors(List<Actor> aActors) {
		this.actors = aActors;
	}

	public List<TestPlan> getTestPlans() {
		return this.testPlans;
	}

	public void setTestPlans(List<TestPlan> aTestPlans) {
		this.testPlans = aTestPlans;
	}

	public void save(Map<String, Object> aData) {
		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(project.id) from " + Project.class.getSimpleName() + " project");
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

		String theCreatedBy = (String) aData.get(CREATED_BY);
		if (theCreatedBy != null) {
			this.setCreatedBy(theCreatedBy);
		}

		String theDescription = (String) aData.get(DESCRIPTION);
		if (theDescription != null) {
			this.setDescription(theDescription);
		}

		Date theStartDate = (Date) aData.get(START_DATE);
		if (theStartDate != null) {
			this.setStartDate(theStartDate);
		}

		Date theEndDate = (Date) aData.get(END_DATE);
		if (theEndDate != null) {
			this.setEndDate(theEndDate);
		}

		String theStatus = (String) aData.get(STATUS);
		if (theStatus != null) {
			this.setStatus(theStatus);
		}

		List<ProjectMember> theProjectMembers = (List) aData.get(PROJECT_MEMBERS);
		if (theProjectMembers != null) {
			this.updateProjectMembers(theProjectMembers);
		}

		this.adjustIterationDates();
		this.adjustWorkProductDates();

		PersistenceManager.getInstance().save(this);
	}

	private void adjustWorkProductDates() {

		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct != null) {
				if (theWorkProduct.getStartDate().compareTo(this.getStartDate()) < 0) {
					theWorkProduct.setStartDate(this.getStartDate());
				}
				if (theWorkProduct.getEndDate().compareTo(this.getEndDate()) > 0) {
					theWorkProduct.setEndDate(this.getEndDate());
				}
			}
		}
	}

	private void adjustIterationDates() {

		for (Iteration theIteration : this.getIterations()) {
			if (theIteration != null) {
				if (theIteration.getStartDate().compareTo(this.getStartDate()) < 0) {
					theIteration.setStartDate(this.getStartDate());
				}
				if (theIteration.getEndDate().compareTo(this.getEndDate()) > 0) {
					theIteration.setEndDate(this.getEndDate());
				}
			}
		}
	}

	public void delete() {
		PersistenceManager.getInstance().delete(this);
	}

	private void updateProjectMembers(List<ProjectMember> aProjectMembers) {

		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		for (ProjectMember theProjectMember : theProjectMembers) {
			if (theProjectMember != null && !aProjectMembers.contains(theProjectMember)) {
				theProjectMember.removeProject(this);
			}
		}
		theProjectMembers.clear();
		for (ProjectMember theProjectMember : aProjectMembers) {
			if (theProjectMember != null) {
				this.addProjectMember(theProjectMember);
			}
		}
	}

	private void addProjectMember(ProjectMember aProjectMember) {
		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		if (!theProjectMembers.contains(aProjectMember)) {
			theProjectMembers.add(aProjectMember);
			aProjectMember.addProject(this);
		}
	}

	public void removeProjectMember(ProjectMember aProjectMember) {
		Set<ProjectMember> theProjectMembers = this.getProjectMembers();
		if (theProjectMembers.contains(aProjectMember)) {
			theProjectMembers.remove(aProjectMember);
		}
	}

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(CREATED_BY, this.getCreatedBy());
		theData.put(DESCRIPTION, this.getDescription());
		theData.put(STATUS, this.getStatus());
		theData.put(START_DATE, this.getStartDate());
		theData.put(END_DATE, this.getEndDate());
		theData.put(PROGRESS, this.getProgress() + "%");

		List theProjectMembers = new ArrayList<ProjectMember>();
		theProjectMembers.addAll(this.getProjectMembers());
		theData.put(PROJECT_MEMBERS, theProjectMembers);

		return theData;
	}

	public Integer getProgress() {

		Integer theTotalElements = 0;
		Integer theProgress = 0;
		for (Iteration theIteration : this.getIterations()) {
			if (theIteration != null) {
				theProgress = theProgress + theIteration.getProgress();
				theTotalElements++;
			}
		}

		List<Task> theTasks = this.retrieveWorkProducts(Task.class);
		for (Task theTask : theTasks) {
			if (theTask != null) {
				if (theTask.isOrphan()) {
					theProgress = theProgress + theTask.getProgress();
					theTotalElements++;
				}
			}
		}

		return theTotalElements == 0 ? theProgress : (theProgress / theTotalElements) + (theProgress % theTotalElements);
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

		Date theStartDate = (Date) aData.get(START_DATE);
		if (theStartDate == null) {
			theErrors.add(IViewConstants.RB.getString("start_date_not_empty.msg"));
		}

		Date theEndDate = (Date) aData.get(END_DATE);
		if (theEndDate == null) {
			theErrors.add(IViewConstants.RB.getString("end_date_not_empty.msg"));
		}

		if (theStartDate != null && theEndDate != null) {
			if (theEndDate.compareTo(theStartDate) < 0) {
				theErrors.add(IViewConstants.RB.getString("end_date_not_before_start_date.msg"));
			}
		}
		return theErrors;
	}

	public void addIteration(Iteration aIteration) {
		List<Iteration> theIterations = this.getIterations();
		if (!theIterations.contains(aIteration)) {
			aIteration.setProject(this);
			theIterations.add(aIteration);
		}
	}

	public void addWorkProduct(WorkProduct aWorkProduct) {
		List<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (!theWorkProducts.contains(aWorkProduct)) {
			aWorkProduct.setProject(this);
			theWorkProducts.add(aWorkProduct);
		}
	}

	public void removeWorkProduct(WorkProduct aWorkProduct) {
		List<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (theWorkProducts.contains(aWorkProduct)) {
			theWorkProducts.remove(aWorkProduct);
		}
	}

	public void addTestCase(TestCase aTestCase) {
		List<TestCase> theTestCases = this.getTestCases();
		if (!theTestCases.contains(aTestCase)) {
			aTestCase.setProject(this);
			theTestCases.add(aTestCase);
		}
	}

	public void addTestPlan(TestPlan aTestPlan) {
		List<TestPlan> theTestPlans = this.getTestPlans();
		if (!theTestPlans.contains(aTestPlan)) {
			aTestPlan.setProject(this);
			theTestPlans.add(aTestPlan);
		}
	}

	public void removeTestCase(TestCase aTestCase) {
		List<TestCase> theTestCases = this.getTestCases();
		if (theTestCases.contains(aTestCase)) {
			theTestCases.remove(aTestCase);
		}
	}

	public void removeTestPlan(TestPlan aTestPlan) {
		List<TestPlan> theTestPlans = this.getTestPlans();
		if (theTestPlans.contains(aTestPlan)) {
			theTestPlans.remove(aTestPlan);
		}
	}

	public void addGlossaryTerm(GlossaryTerm aGlossaryTerm) {
		List<GlossaryTerm> theGlossaryTerms = this.getGlossaryTerms();
		if (!theGlossaryTerms.contains(aGlossaryTerm)) {
			aGlossaryTerm.setProject(this);
			theGlossaryTerms.add(aGlossaryTerm);
		}
	}

	public void removeGlossaryTerm(GlossaryTerm aGlossaryTerm) {
		List<GlossaryTerm> theGlossaryTerms = this.getGlossaryTerms();
		if (theGlossaryTerms.contains(aGlossaryTerm)) {
			theGlossaryTerms.remove(aGlossaryTerm);
		}
	}

	public void addDocument(Document aDocument) {
		List<Document> theDocuments = this.getDocuments();
		if (!theDocuments.contains(aDocument)) {
			aDocument.setProject(this);
			theDocuments.add(aDocument);
		}
	}

	public void addActor(Actor aActor) {
		List<Actor> theActors = this.getActors();
		if (!theActors.contains(aActor)) {
			aActor.setProject(this);
			theActors.add(aActor);
		}
	}

	public void removeDocument(Document aDocument) {
		List<Document> theDocuments = this.getDocuments();
		if (theDocuments.contains(aDocument)) {
			theDocuments.remove(aDocument);
		}
	}

	public void removeActor(Actor aActor) {
		List<Actor> theActors = this.getActors();
		if (theActors.contains(aActor)) {
			theActors.remove(aActor);
		}
	}

	public void removeIteration(Iteration aIteration) {
		List<Iteration> theIterations = this.getIterations();
		if (theIterations.contains(aIteration)) {
			theIterations.remove(aIteration);
		}
	}

	public Iteration retrieveIterationBy(Integer aIterationId) {
		Iteration theIteration = null;
		for (Iteration theCurrentIteration : this.getIterations()) {
			if (theCurrentIteration != null) {
				if (theCurrentIteration.getId().equals(aIterationId)) {
					theIteration = theCurrentIteration;
					break;
				}
			}
		}
		return theIteration;
	}

	public TestCase retrieveTestCaseBy(Integer aTestCaseId) {
		TestCase theTestCase = null;
		for (TestCase theCurrentTestCase : this.getTestCases()) {
			if (theCurrentTestCase != null) {
				if (theCurrentTestCase.getId().equals(aTestCaseId)) {
					theTestCase = theCurrentTestCase;
					break;
				}
			}
		}
		return theTestCase;
	}

	public TestPlan retrieveTestPlanBy(Integer aTestPlanId) {
		TestPlan theTestPlan = null;
		for (TestPlan theCurrentTestPlan : this.getTestPlans()) {
			if (theCurrentTestPlan != null) {
				if (theCurrentTestPlan.getId().equals(aTestPlanId)) {
					theTestPlan = theCurrentTestPlan;
					break;
				}
			}
		}
		return theTestPlan;
	}

	public GlossaryTerm retrieveGlossaryTermBy(Integer aGlossaryId) {
		GlossaryTerm theGlossaryTerm = null;
		for (GlossaryTerm theCurrentGlossaryTerm : this.getGlossaryTerms()) {
			if (theCurrentGlossaryTerm != null) {
				if (theCurrentGlossaryTerm.getId().equals(aGlossaryId)) {
					theGlossaryTerm = theCurrentGlossaryTerm;
					break;
				}
			}
		}
		return theGlossaryTerm;
	}

	public Actor retrieveActorBy(Integer aActorId) {
		Actor theActor = null;
		for (Actor theCurrentActor : this.getActors()) {
			if (theCurrentActor != null) {
				if (theCurrentActor.getId().equals(aActorId)) {
					theActor = theCurrentActor;
					break;
				}
			}
		}
		return theActor;
	}

	public Document retrieveDocumentBy(Integer aDocumentId) {
		Document theDocument = null;
		for (Document theCurrentDocument : this.getDocuments()) {
			if (theCurrentDocument != null) {
				if (theCurrentDocument.getId().equals(aDocumentId)) {
					theDocument = theCurrentDocument;
					break;
				}
			}
		}
		return theDocument;
	}

	public ProjectMember retrieveProjectMemberBy(Integer aProjectMember) {
		ProjectMember theProjectMember = null;
		for (ProjectMember theCurrentProjectMember : this.getProjectMembers()) {
			if (theCurrentProjectMember != null) {
				if (theCurrentProjectMember.getId().equals(aProjectMember)) {
					theProjectMember = theCurrentProjectMember;
					break;
				}
			}
		}
		return theProjectMember;
	}

	public <E> E retrieveWorkProduct(Class<E> aClass, Integer aWorkProductId) {
		E theResult = null;
		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct != null) {
				if (theWorkProduct.getId().equals(aWorkProductId)) {
					theResult = (E) theWorkProduct;
					break;
				}
			}
		}
		return theResult;
	}

	public <E> List<E> retrieveWorkProducts(Class<E> aClass) {
		List<E> theResults = new ArrayList<E>();
		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct != null && aClass.isInstance(theWorkProduct)) {
				theResults.add((E) theWorkProduct);
			}
		}
		return theResults;
	}

	public String getElementType() {
		return IViewConstants.RB.getString("project.lbl");
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Project) {
			Project theProject = (Project) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theProject.getId());
			}
		}
		return isEquals;
	}

	class IterationsComparator implements Comparator<Iteration> {

		public int compare(Iteration aIteration1, Iteration aIteration2) {
			int theResult = -1;
			if (aIteration1 != null && aIteration2 != null) {
				theResult = aIteration1.getStartDate().compareTo(aIteration2.getStartDate());
			}
			return theResult;
		}
	}
}
