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

import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TestPlanMaintenance;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.TestPlansListModel;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.WebBrowser;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class TestPlanExecutionReportView extends PanelComponent implements ActionListener {

	private DropDownGridBoxComponent testPlansDropDown = null;
	private TestPlanMaintenance testPlanMaintenance = null;
	private Button okButton = null;
	private PanelComponent fieldsPanel = null;
	private WebBrowser webBrowser = null;

	public TestPlanExecutionReportView() {

		TestPlansListModel theTestPlansListModel = new TestPlansListModel();
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		if (theProjectId != null) {
			ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
			Project theProject = theProjectMaintenance.retrieveProjectBy(theProjectId);
			this.testPlanMaintenance = new TestPlanMaintenance(theProject);
			theTestPlansListModel.setData(this.testPlanMaintenance.getTestPlans());
		}

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 30, 0 } }, 5, 5));

		PanelComponent theParametersPanel = new PanelComponent();
		theParametersPanel.setLayout(new TableLayout(new double[][] { { 290, 0 }, { 50 } }, 0, 5));

		this.fieldsPanel = new PanelComponent();
		this.fieldsPanel.setLayout(new TableLayout(new double[][] { { 80, 200 }, { 20 } }, 0, 5));

		Label theTypeLabel = new Label(IViewConstants.RB.getString("test_plan.lbl") + ":");
		this.fieldsPanel.add(theTypeLabel.setLimit("0, 0"));

		this.testPlansDropDown = new DropDownGridBoxComponent(theTestPlansListModel, 0);

		this.testPlansDropDown.setSelectedRowObject(4);
		this.fieldsPanel.add(testPlansDropDown.setLimit("1, 0"));

		PanelComponent theButtonPanel = new PanelComponent();
		theButtonPanel.setLayout(new TableLayout(new double[][] { { 100, 0 }, { 25 } }, 0, 5));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
		this.okButton.setEnabled(theProjectId != null);
		theButtonPanel.add(okButton.setLimit("0, 0"));

		theParametersPanel.add(this.fieldsPanel.setLimit("0, 0"));
		theParametersPanel.add(theButtonPanel.setLimit("1, 0"));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.REPORT_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("test_plan_execution.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));
		super.add(theParametersPanel.setLimit("0, 1"));

		this.webBrowser = new WebBrowser();
		super.add(this.webBrowser.setLimit("0, 2"));
	}

	public void actionPerformed(ActionEvent aEvt) {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		theSecurityMaintenance.startUnitOfWork();
		Integer theTestPlan = this.testPlansDropDown.getSelectedRowId();
		if (theTestPlan != null) {
			this.webBrowser.setLocation(null);
			this.webBrowser.setLocation(this.testPlanMaintenance.createTestPlanExecutionReportLocation(theTestPlan));
		}
		theSecurityMaintenance.endUnitOfWork();
	}
}
