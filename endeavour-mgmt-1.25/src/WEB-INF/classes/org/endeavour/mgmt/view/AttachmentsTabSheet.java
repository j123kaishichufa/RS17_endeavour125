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

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.AttachmentMaintenance;
import org.endeavour.mgmt.controller.WorkProductMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.AttachmentsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.GridBox.Row;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class AttachmentsTabSheet extends TabSheetComponent implements ActionListener, Observer {

	private GridBoxComponent attachmentsGrid = null;
	private AttachmentMaintenance attachmentMaintenance = null;
	private Button newAttachmentButton = null;
	private Button downloadAttachmentButton = null;
	private Button deleteAttachmentButton = null;
	private AttachmentsListModel attachmentsModel = null;

	public AttachmentsTabSheet(String aTitle) {

		super(aTitle);

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.initializeControllers();
		this.initializeAttachmentsGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100 }, { 0 } }, 0, 5));

		this.newAttachmentButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		this.newAttachmentButton.setImage(IViewConstants.NEW_BUTTON_ICON);
		this.newAttachmentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.newAttachmentButton.setLimit("1, 0"));

		this.downloadAttachmentButton = new Button(IViewConstants.DOWNLOAD_BUTTON_LABEL);
		this.downloadAttachmentButton.setImage(IViewConstants.DOWNLOAD_BUTTON_ICON);
		this.downloadAttachmentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.downloadAttachmentButton.setLimit("2, 0"));

		this.deleteAttachmentButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteAttachmentButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		this.deleteAttachmentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.deleteAttachmentButton.setLimit("3, 0"));

		this.setButtonsStatus();

		super.add(theButtonsPanel.setLimit("0, 1"));
	}

	public void initializeAttachmentsGrid() {
		this.attachmentsModel = new AttachmentsListModel();
		this.attachmentsGrid = new GridBoxComponent(this.attachmentsModel);
		this.attachmentsGrid.addActionListener(GridBoxComponent.ACTION_CLICK, this);
		super.add(this.attachmentsGrid.setLimit("0, 0"));
	}

	public void setData(Map<String, Object> aData) {
		this.attachmentsModel.setData((List) aData.get(WorkProductMaintenance.ATTACHMENTS));
		this.attachmentMaintenance.setAttachments((List) aData.get(WorkProductMaintenance.ATTACHMENTS));
		this.setButtonsStatus();
		super.setCount(this.attachmentsModel.getRowCount());
	}

	public void getData(Map<String, Object> aData) {
		aData.put(WorkProductMaintenance.ATTACHMENTS, this.attachmentMaintenance.getAttachments());
	}

	public void actionPerformed(ActionEvent aEvent) {

		if (aEvent.getSource() instanceof Button) {

			Button theButton = (Button) aEvent.getSource();
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new AttachmentView(this.attachmentMaintenance);
			}

			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				Row theSelectedRow = this.attachmentsGrid.getSelectedRow();
				if (theSelectedRow != null) {
					int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
					if (theResult == IViewConstants.YES) {
						this.attachmentMaintenance.deleteAttachment(theSelectedRow.getIndex());
						this.viewAttachments();
					}
				}
			}

			if (theButton.getText().equals(IViewConstants.DOWNLOAD_BUTTON_LABEL)) {
				Integer theAttachmentGridId = this.attachmentsGrid.getSelectedRowId();
				new DownloadAttachmentView(theAttachmentGridId, this.attachmentMaintenance);
			}
		}

		if (aEvent.getSource() instanceof GridBox.Range) {
			this.setButtonsStatus();
		}
	}

	private void initializeControllers() {
		this.attachmentMaintenance = new AttachmentMaintenance();
		this.attachmentMaintenance.addObserver(this);
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewAttachments();
	}

	private void viewAttachments() {
		this.attachmentsModel.setData(this.attachmentMaintenance.getAttachments());
		this.setButtonsStatus();
		super.setCount(this.attachmentsModel.getRowCount());
	}

	private void setButtonsStatus() {
		Integer theSElectedRowId = this.attachmentsGrid.getSelectedRowId() != null ? this.attachmentsGrid.getSelectedRowId() : -1;
		this.downloadAttachmentButton.setEnabled(theSElectedRowId >= 0);
		this.deleteAttachmentButton.setEnabled(!this.attachmentsGrid.getRows().isEmpty());
	}
}
