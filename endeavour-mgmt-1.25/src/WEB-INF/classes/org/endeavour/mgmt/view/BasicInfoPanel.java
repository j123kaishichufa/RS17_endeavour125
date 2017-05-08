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

package org.endeavour.mgmt.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.PercentStatusListModel;
import org.endeavour.mgmt.view.model.PriorityListModel;
import org.endeavour.mgmt.view.model.StatusListModel;

import thinwire.ui.DropDownDateBox;
import thinwire.ui.Label;
import thinwire.ui.TextField;
import thinwire.ui.layout.TableLayout;

public class BasicInfoPanel extends PanelComponent {

	private Label nameLabel = null;
	private TextField nameTextField = null;
	private Label startDateLabel = null;
	private DropDownDateBox startDateDropDown = null;
	private Label endDateLabel = null;
	private DropDownDateBox endDateDropDown = null;
	private Label progressLabel = null;
	private DropDownGridBoxComponent progressDropDown = null;
	private Label createdByLabel = null;
	private TextField createdByTextField = null;
	private DropDownGridBoxComponent prioritiesDropDown = null;
	private DropDownGridBoxComponent statusDropDown = null;
	private Label statusLabel = null;
	private Label priorityLabel = null;
	private StatusListModel statusModel = null;
	private PriorityListModel prioritiesModel = null;

	public BasicInfoPanel(Date aStartDate, Date aEndDate, double aWidth) {
		super.setLayout(new TableLayout(new double[][] { { aWidth, 0 }, { 20, 20, 20, 20, 20, 20, 20 } }, 5, 5));

		this.nameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
		super.add(this.nameLabel.setLimit("0, 0"));

		this.nameTextField = new TextField();
		super.add(this.nameTextField.setLimit("1, 0"));

		this.startDateLabel = new Label(IViewConstants.RB.getString("start_date.lbl") + ":");
		super.add(this.startDateLabel.setLimit("0, 1"));

		this.startDateDropDown = new DropDownDateBox(new SimpleDateFormat(IViewConstants.DATE_MASK).format(aStartDate));
		this.startDateDropDown.setEditMask(IViewConstants.DATE_MASK);
		super.add(this.startDateDropDown.setLimit("1, 1"));

		this.endDateLabel = new Label(IViewConstants.RB.getString("end_date.lbl") + ":");
		super.add(this.endDateLabel.setLimit("0, 2"));

		this.endDateDropDown = new DropDownDateBox(new SimpleDateFormat(IViewConstants.DATE_MASK).format(aEndDate));
		this.endDateDropDown.setEditMask(IViewConstants.DATE_MASK);
		super.add(this.endDateDropDown.setLimit("1, 2"));

		this.progressLabel = new Label(IViewConstants.RB.getString("progress.lbl"));
		super.add(this.progressLabel.setLimit("0, 3"));

		PercentStatusListModel theProgressModel = new PercentStatusListModel();
		this.progressDropDown = new DropDownGridBoxComponent(theProgressModel, 0);
		this.progressDropDown.selectFirstElement();
		super.add(this.progressDropDown.setLimit("1, 3"));

		this.createdByLabel = new Label(IViewConstants.RB.getString("created_by.lbl"));
		super.add(this.createdByLabel.setLimit("0, 4"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		this.createdByTextField = new TextField();
		this.createdByTextField.setText(theSecurityMaintenance.getLoggedUserId());
		this.createdByTextField.setEnabled(false);
		super.add(this.createdByTextField.setLimit("1, 4"));
	}

	public BasicInfoPanel(Date aStartDate, Date aEndDate) {
		this(aStartDate, aEndDate, 75);
	}

	public void displayPriority() {
		this.priorityLabel = new Label(IViewConstants.RB.getString("priority.lbl") + ":");
		super.add(this.priorityLabel.setLimit("0, 5"));

		this.prioritiesModel = new PriorityListModel(false);
		this.prioritiesDropDown = new DropDownGridBoxComponent(this.prioritiesModel, 0);
		this.prioritiesDropDown.selectFirstElement();

		super.add(this.prioritiesDropDown.setLimit("1, 5"));
	}

	public void displayStatus(boolean aFullList, boolean aIncludeAll) {

		this.statusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
		super.add(this.statusLabel.setLimit("0, 6"));

		this.statusModel = new StatusListModel(aFullList, aIncludeAll);
		this.statusDropDown = new DropDownGridBoxComponent(this.statusModel, 0);
		this.statusDropDown.selectFirstElement();

		super.add(this.statusDropDown.setLimit("1, 6"));
	}

	public void getData(Map<String, Object> aData) {
		try {
			aData.put(IBasicInfoMaintenance.NAME, this.nameTextField.getText());
			aData.put(IBasicInfoMaintenance.START_DATE, new SimpleDateFormat(IViewConstants.DATE_MASK).parse(this.startDateDropDown.getText()));
			aData.put(IBasicInfoMaintenance.END_DATE, new SimpleDateFormat(IViewConstants.DATE_MASK).parse(this.endDateDropDown.getText()));
			aData.put(IBasicInfoMaintenance.PROGRESS, this.progressDropDown.getSelectedRowId());
			aData.put(IBasicInfoMaintenance.CREATED_BY, this.createdByTextField.getText());
			if (this.statusDropDown != null) {
				aData.put(IBasicInfoMaintenance.STATUS, this.statusModel.getValueById(this.statusDropDown.getSelectedRowId()));
			}
			if (this.prioritiesDropDown != null) {
				aData.put(IBasicInfoMaintenance.PRIORITY, this.prioritiesModel.getValueById(this.prioritiesDropDown.getSelectedRowId()));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setData(Map<String, Object> aData) {

		String theName = (String) aData.get(IBasicInfoMaintenance.NAME);
		Date theStartDate = (Date) aData.get(IBasicInfoMaintenance.START_DATE);
		Date theEndDate = (Date) aData.get(IBasicInfoMaintenance.END_DATE);
		String theProgress = (String) aData.get(IBasicInfoMaintenance.PROGRESS);
		String theCreatedBy = (String) aData.get(IBasicInfoMaintenance.CREATED_BY);
		String theStatus = (String) aData.get(IBasicInfoMaintenance.STATUS);
		String thePriority = (String) aData.get(IBasicInfoMaintenance.PRIORITY);

		this.nameTextField.setText(theName);
		this.startDateDropDown.setText(new SimpleDateFormat(IViewConstants.DATE_MASK).format(theStartDate));
		this.endDateDropDown.setText(new SimpleDateFormat(IViewConstants.DATE_MASK).format(theEndDate));
		this.progressDropDown.setText(theProgress);
		this.createdByTextField.setText(theCreatedBy);
		if (this.statusDropDown != null) {
			this.statusDropDown.setText(this.statusModel.getDescriptionByValue(theStatus));
		}
		if (this.prioritiesDropDown != null) {
			this.prioritiesDropDown.setText(this.prioritiesModel.getDescriptionByValue(thePriority));
		}
	}

	public void setProgressDropDownStatus(boolean aStatus) {
		this.progressDropDown.setEnabled(aStatus);
	}

	public TextField getNameTextField() {
		return this.nameTextField;
	}
}
