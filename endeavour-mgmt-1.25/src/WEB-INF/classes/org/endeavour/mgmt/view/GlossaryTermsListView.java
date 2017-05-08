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

import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.GlossaryTermMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.GlossaryTermsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.WebBrowser;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class GlossaryTermsListView extends PanelComponent implements ActionListener, Observer {

	private GlossaryTermMaintenance glossaryTermMaintenance = null;
	private GridBoxComponent glossaryTermsGrid = null;
	private TabSheetComponent glossaryTermsTab = null;
	private WebBrowser printPreviewWebBrowser = null;
	private Button newButton = null;
	private Button editButton = null;
	private Button deleteButton = null;
	private GlossaryTermsListModel glossaryTermsModel = null;

	public GlossaryTermsListView() {
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 5, 5));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.GLOSSARY_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label(IViewConstants.RB.getString("project_glossary.lbl"));
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		TabFolderComponent theTabFolder = new TabFolderComponent();

		this.glossaryTermsTab = new TabSheetComponent(IViewConstants.RB.getString("glossary.lbl"));
		this.glossaryTermsTab.setImage(IViewConstants.GLOSSARY_ICON);
		this.glossaryTermsTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));
		this.initializeControllers();

		this.initializeGlossaryTermsGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100, 100 }, { 0 } }, 0, 5));

		this.newButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		this.newButton.addActionListener(Button.ACTION_CLICK, this);
		this.newButton.setImage(IViewConstants.NEW_BUTTON_ICON);
		theButtonsPanel.add(this.newButton.setLimit("2, 0"));

		this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
		this.editButton.addActionListener(Button.ACTION_CLICK, this);
		this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
		theButtonsPanel.add(this.editButton.setLimit("3, 0"));

		this.deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteButton.addActionListener(Button.ACTION_CLICK, this);
		this.deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		theButtonsPanel.add(this.deleteButton.setLimit("4, 0"));

		this.setButtonsStatus();

		this.glossaryTermsTab.add(theButtonsPanel.setLimit("0, 1"));

		theTabFolder.add(this.glossaryTermsTab);

		TabSheetComponent thePrintPreviewTab = new TabSheetComponent(IViewConstants.PRINT_PREVIEW);
		thePrintPreviewTab.setImage(IViewConstants.PRINT_PREVIEW_ICON);
		thePrintPreviewTab.addActionListener(TabSheetComponent.ACTION_CLICK, this);
		thePrintPreviewTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.printPreviewWebBrowser = new WebBrowser();
		thePrintPreviewTab.add(this.printPreviewWebBrowser.setLimit("0, 0"));
		theTabFolder.add(thePrintPreviewTab);

		super.add(theTabFolder.setLimit("0, 1"));

	}

	private void setButtonsStatus() {
		boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
		boolean hasGlossaryTerms = !this.glossaryTermsGrid.getRows().isEmpty();
		this.editButton.setEnabled(isEnabled && hasGlossaryTerms);

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_DELETE);
		this.deleteButton.setEnabled(isEnabled && hasGlossaryTerms && hasPrivilege);

		hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
		this.newButton.setEnabled(isEnabled && hasPrivilege);
	}

	private void initializeControllers() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		this.glossaryTermMaintenance = new GlossaryTermMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
		this.glossaryTermMaintenance.addObserver(this);
	}

	private void initializeGlossaryTermsGrid() {
		Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
		if (theProjectId != null) {
			this.glossaryTermsModel = new GlossaryTermsListModel(this.glossaryTermMaintenance.getGlossaryTerms());
		} else {
			this.glossaryTermsModel = new GlossaryTermsListModel();
		}
		this.glossaryTermsGrid = new GridBoxComponent(this.glossaryTermsModel);
		this.glossaryTermsGrid.setColumnWidth(0, 250);
		this.glossaryTermsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.glossaryTermsTab.add(this.glossaryTermsGrid.setLimit("0, 0"));
	}

	private void viewGlossaryTerms() {
		this.glossaryTermsModel.setData(this.glossaryTermMaintenance.getGlossaryTerms());
		Integer theId = this.glossaryTermMaintenance.getSelectedGlossaryTermId();
		if (theId != null) {
			this.glossaryTermsGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvt) {
		Object theSource = aEvt.getSource();
		if (theSource instanceof GridBox.Range) {
			this.viewGlossaryTerm();
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new GlossaryTermView(this.glossaryTermMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
				this.viewGlossaryTerm();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteGlossaryTerm();
			}
		}

		if (theSource instanceof TabSheetComponent) {
			SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
			theSecurityMaintenance.startUnitOfWork();
			if (MainView.getProjectDropDown().getSelectedRowId() != null) {
				this.printPreviewWebBrowser.setLocation(this.glossaryTermMaintenance.createPrintPreviewLocation());
			}
			theSecurityMaintenance.endUnitOfWork();
		}
	}

	private void viewGlossaryTerm() {
		Integer theGlossaryTermId = this.glossaryTermsGrid.getSelectedRowId();
		if (theGlossaryTermId != null) {
			this.glossaryTermsGrid.setEnabled(false);
			new GlossaryTermView(theGlossaryTermId, this.glossaryTermMaintenance);
			this.glossaryTermsGrid.setEnabled(true);
		}
	}

	private void deleteGlossaryTerm() {
		this.glossaryTermMaintenance.startUnitOfWork();
		Integer theGlossaryTermId = this.glossaryTermsGrid.getSelectedRowId();
		if (theGlossaryTermId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.glossaryTermMaintenance.reset();
				this.glossaryTermMaintenance.deleteGlossaryTerm(theGlossaryTermId);
				this.viewGlossaryTerms();
			}
		}
		this.glossaryTermMaintenance.endUnitOfWork();
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewGlossaryTerms();
	}
}
