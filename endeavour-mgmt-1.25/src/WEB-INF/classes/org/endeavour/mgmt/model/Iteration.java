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
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

@SuppressWarnings("unchecked")
public class Iteration implements IPlanElement {

	private Integer id = null;
	private String name = null;
	private String createdBy = null;
	private Project project = null;
	private Date startDate = null;
	private Date endDate = null;
	private List<WorkProduct> workProducts = null;

	public static final String NAME = "NAME";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String PROGRESS = "PROGRESS";
	public static final String WORK_PRODUCTS = "WORK_PRODUCTS";
	public static final String CREATED_BY = "CREATED_BY";

	public Iteration() {
	}

	public Iteration(Project aProject) {
		this.project = aProject;
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

	public String getName() {
		return this.name;
	}

	public void setName(String aName) {
		this.name = aName;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer aId) {
		this.id = aId;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
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

	public void setCreatedBy(String aCreatedBy) {
		this.createdBy = aCreatedBy;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	private void updateWorkProducts(List<WorkProduct> aWorkProducts) {

		List<WorkProduct> theWorkProducts = this.getWorkProducts();
		for (WorkProduct theWorkProduct : theWorkProducts) {
			if (theWorkProduct != null && !aWorkProducts.contains(theWorkProduct)) {
				theWorkProduct.setIteration(null);
			}
		}
		theWorkProducts.clear();
		for (WorkProduct theWorkProduct : aWorkProducts) {
			this.addWorkProduct(theWorkProduct);
		}
	}

	private void addWorkProduct(WorkProduct aWorkProduct) {
		List<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (!theWorkProducts.contains(aWorkProduct)) {
			aWorkProduct.setIteration(this);

			if (aWorkProduct.getStartDate().compareTo(this.getStartDate()) < 0) {
				aWorkProduct.setStartDate(this.getStartDate());
			}

			if (aWorkProduct.getEndDate().compareTo(this.getEndDate()) > 0) {
				aWorkProduct.setEndDate(this.getEndDate());
			}

			theWorkProducts.add(aWorkProduct);
		}
	}

	public void removeWorkProduct(WorkProduct aWorkProduct) {
		List<WorkProduct> theWorkProducts = this.getWorkProducts();
		if (theWorkProducts.contains(aWorkProduct)) {
			theWorkProducts.remove(aWorkProduct);
		}
	}

	public List<String> validate(Map<String, Object> aData) {
		List<String> theErrors = new ArrayList<String>();

		String theName = (String) aData.get(NAME);
		if (theName == null || theName.trim().length() == 0) {
			theErrors.add(IViewConstants.RB.getString("name_not_empty.msg"));
		}

		Date theStartDate = (Date) aData.get(START_DATE);
		if (theStartDate != null) {
			if (theStartDate.compareTo(this.getProject().getStartDate()) < 0) {
				theErrors.add(IViewConstants.RB.getString("start_date_range.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(this.getProject().getStartDate()) + ".");
			}
		}

		Date theEndDate = (Date) aData.get(END_DATE);
		if (theEndDate != null) {
			if (theEndDate.compareTo(this.getProject().getEndDate()) > 0) {
				theErrors.add(IViewConstants.RB.getString("end_date_range.msg") + " " + new SimpleDateFormat(IViewConstants.DATE_MASK).format(this.getProject().getEndDate()) + ".");
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

	public Map<String, Object> getData() {
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(NAME, this.getName());
		theData.put(CREATED_BY, this.getCreatedBy());
		theData.put(START_DATE, this.getStartDate());
		theData.put(END_DATE, this.getEndDate());
		theData.put(PROGRESS, this.getProgress() + "%");
		return theData;
	}

	public void delete() {
		this.updateWorkProducts(new ArrayList<WorkProduct>());
		this.getProject().removeIteration(this);
		PersistenceManager.getInstance().delete(this);
	}

	public void save(Map<String, Object> aData) {

		if (this.getId() == null) {
			Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(iteration.id) from " + Iteration.class.getSimpleName() + " iteration");
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

		Date theStartDate = (Date) aData.get(START_DATE);
		if (theStartDate != null) {
			this.setStartDate(theStartDate);
		}

		Date theEndDate = (Date) aData.get(END_DATE);
		if (theEndDate != null) {
			this.setEndDate(theEndDate);
		}

		List<WorkProduct> theWorkProducts = (List) aData.get(WORK_PRODUCTS);
		if (theWorkProducts != null) {
			this.updateWorkProducts(theWorkProducts);
		}

		this.getProject().addIteration(this);
	}

	public Integer getProgress() {

		Integer theTotalElements = 0;
		Integer theProgress = 0;
		for (WorkProduct theWorkProduct : this.getWorkProducts()) {
			if (theWorkProduct != null) {
				theProgress = theProgress + theWorkProduct.getProgress();
				theTotalElements++;
			}
		}
		return theTotalElements == 0 ? theProgress : (theProgress / theTotalElements) + (theProgress % theTotalElements);
	}

	public String getElementType() {
		return IViewConstants.RB.getString("iteration.lbl");
	}

	public boolean equals(Object anObj) {
		boolean isEquals = false;
		if (anObj != null && anObj instanceof Iteration) {
			Iteration theIteration = (Iteration) anObj;
			if (this.getId() != null) {
				isEquals = this.getId().equals(theIteration.getId());
			}
		}
		return isEquals;
	}
}
