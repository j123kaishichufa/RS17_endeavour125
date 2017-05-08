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

import org.endeavour.mgmt.controller.DocumentAssignmentMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.AssignedDocumentsListModel;
import org.endeavour.mgmt.view.model.UnassignedDocumentsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.event.DropEvent;
import thinwire.ui.event.DropListener;
import thinwire.ui.layout.TableLayout;

public class DocumentAssignmentView extends DialogComponent implements DropListener, ActionListener {

	private GridBoxComponent assignedDocumentsGrid = null;
	private GridBoxComponent unassignedDocumentsGrid = null;
	private Button saveButton = null;
	private Button closeButton = null;
	private DocumentAssignmentMaintenance documentAssignmentMaintenance = null;
	private AssignedDocumentsListModel assignedDocumentsModel = null;
	private UnassignedDocumentsListModel unassignedDocumentsModel = null;
	private PanelComponent documentsPanelComponent = null;

	public DocumentAssignmentView(DocumentAssignmentMaintenance aDocumentAssignmentMaintenance) {
		this.documentAssignmentMaintenance = aDocumentAssignmentMaintenance;

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("documents.lbl"));
		super.setSize(365, 380);
		super.centerDialog();

		this.documentsPanelComponent = new PanelComponent();
		this.documentsPanelComponent.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 0 } }, 5, 5));

		this.initializeUnassignedDocumentsGrid();
		this.initializeAssignedDocumentsGrid();
		this.assignedDocumentsGrid.addDropListener(this.unassignedDocumentsGrid, this);
		this.unassignedDocumentsGrid.addDropListener(this.assignedDocumentsGrid, this);

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
		this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		this.saveButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.saveButton.setLimit("1, 0"));

		this.closeButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.closeButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		this.closeButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.closeButton.setLimit("2, 0"));

		super.add(this.documentsPanelComponent.setLimit("0, 0"));
		super.add(theButtonsPanel.setLimit("0, 1"));
		super.setVisible(true);
	}

	private void initializeAssignedDocumentsGrid() {
		this.assignedDocumentsModel = new AssignedDocumentsListModel(this.documentAssignmentMaintenance.getAssignedDocuments());
		this.assignedDocumentsGrid = new GridBoxComponent(this.assignedDocumentsModel);
		this.assignedDocumentsGrid.setColumnWidth(0, 165);
		this.documentsPanelComponent.add(this.assignedDocumentsGrid.setLimit("1, 0"));
	}

	private void initializeUnassignedDocumentsGrid() {
		this.unassignedDocumentsModel = new UnassignedDocumentsListModel(this.documentAssignmentMaintenance.getUnassignedDocuments());
		this.unassignedDocumentsGrid = new GridBoxComponent(this.unassignedDocumentsModel);
		this.unassignedDocumentsGrid.setColumnWidth(0, 165);
		this.documentsPanelComponent.add(this.unassignedDocumentsGrid.setLimit("0, 0"));
	}

	public void dropPerformed(DropEvent aEvent) {
		GridBoxComponent theDestinationGrid = (GridBoxComponent) aEvent.getSourceComponent();
		GridBoxComponent theSourceGrid = (GridBoxComponent) aEvent.getDragComponent();

		GridBox.Row theDragRow = ((GridBox.Range) aEvent.getDragObject()).getRow();
		theDragRow.setSelected(true);

		Object theDragObject = theSourceGrid.getSelectedRowObject();
		theSourceGrid.remove(theDragRow, theDragObject);
		theDestinationGrid.add(theDragRow, theDragObject);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			this.documentAssignmentMaintenance.updateDocuments(this.assignedDocumentsGrid.getAllRowIds(), this.unassignedDocumentsGrid.getAllRowIds());
			this.setVisible(false);
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}
}
