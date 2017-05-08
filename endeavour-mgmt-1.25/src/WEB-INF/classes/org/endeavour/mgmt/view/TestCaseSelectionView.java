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
import org.endeavour.mgmt.controller.TestCaseMaintenance;
import org.endeavour.mgmt.controller.TestPlanMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.TestCasesListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class TestCaseSelectionView extends DialogComponent implements ActionListener {

	private Button okButton = null;
	private Button cancelButton = null;
	private GridBoxComponent testCasesGrid = null;
	private TestPlanMaintenance testPlanMaintenance = null;
	private TestCaseMaintenance testCaseMaintenance = null;
	private TestCasesListModel testCasesModel = null;

	public TestCaseSelectionView(TestPlanMaintenance aTestPlanMaintenance) {

		this.testPlanMaintenance = aTestPlanMaintenance;
		this.initializeControllers();

		super.setTitle(IViewConstants.RB.getString("test_cases.lbl"));
		super.setSize(800, 400);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 8, 5));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.TEST_CASES_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("test_cases.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		this.initializeTestCasesGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
		theButtonsPanel.add(this.okButton.setLimit("1, 0"));

		this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
		this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));

		super.add(theButtonsPanel.setLimit("0, 2"));

		super.setVisible(true);

	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.testCaseMaintenance = new TestCaseMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
	}

	private void initializeTestCasesGrid() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		if (theProjectId != null) {
			this.testCasesModel = new TestCasesListModel(this.testPlanMaintenance.filterUnassignedTestCases(this.testCaseMaintenance.getTestCases()));
		} else {
			this.testCasesModel = new TestCasesListModel();
		}
		this.testCasesGrid = new GridBoxComponent(this.testCasesModel);
		this.testCasesGrid.setColumnWidth(0, 50);
		this.testCasesGrid.setColumnWidth(1, 250);
		this.testCasesGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		super.add(this.testCasesGrid.setLimit("0, 1"));
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.addTestCase();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.OK_BUTTON_LABEL)) {
				this.addTestCase();
			}
			if (theButton.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
				this.setVisible(false);
			}
		}
	}

	private void addTestCase() {
		Integer theSelectedTestCaseId = this.testCasesGrid.getSelectedRowId();
		this.testPlanMaintenance.addTestCase(theSelectedTestCaseId);
		this.setVisible(false);
	}
}
