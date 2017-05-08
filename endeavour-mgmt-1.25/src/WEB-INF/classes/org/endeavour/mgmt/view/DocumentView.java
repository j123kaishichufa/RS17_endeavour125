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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.DocumentMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.components.URLListComponent;
import org.endeavour.mgmt.view.model.DocumentVersionsURLModel;

import thinwire.ui.Button;
import thinwire.ui.FileChooser;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.FileChooser.FileInfo;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

@SuppressWarnings("unchecked")
public class DocumentView extends DialogComponent implements ActionListener {

	private Label descriptionLabel = null;
	private TextField descriptionTextField = null;
	private FileChooser fileChooser = null;
	private URLListComponent versionsList = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private DocumentMaintenance documentMaintenance = null;
	private DocumentVersionsURLModel documentVersionsModel = null;

	public DocumentView(DocumentMaintenance aDocumentMaintenance) {
		this.documentMaintenance = aDocumentMaintenance;
		this.documentMaintenance.startUnitOfWork();
		this.documentMaintenance.reset();

		super.setTitle(IViewConstants.RB.getString("document.lbl"));
		super.setSize(620, 320);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		TabFolderComponent theTabFolder = new TabFolderComponent();
		TabSheetComponent theDocumentTab = new TabSheetComponent(IViewConstants.RB.getString("document.lbl"));
		theDocumentTab.setImage(IViewConstants.DOCUMENTS_ICON);
		theDocumentTab.setLayout(new TableLayout(new double[][] { { 60, 0 }, { 20, 20, 20 } }, 5, 5));

		this.descriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
		theDocumentTab.add(this.descriptionLabel.setLimit("0, 0"));

		this.descriptionTextField = new TextField();
		theDocumentTab.add(this.descriptionTextField.setLimit("1, 0"));

		Label theFileLabel = new Label(IViewConstants.RB.getString("file.lbl"));
		theDocumentTab.add(theFileLabel.setLimit("0, 1"));

		this.fileChooser = new FileChooser();
		theDocumentTab.add(this.fileChooser.setLimit("1, 1"));
		
		Label theUploadLabel = new Label(IViewConstants.RB.getString("file_max_size.msg"));
		theUploadLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theDocumentTab.add(theUploadLabel.setLimit("1, 2"));

		theTabFolder.add(theDocumentTab);

		TabSheetComponent theVersionsTab = new TabSheetComponent(IViewConstants.RB.getString("versions.lbl"));
		theVersionsTab.setImage(IViewConstants.DOCUMENT_VERSION_ICON);
		theVersionsTab.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5, 5));

		this.documentVersionsModel = new DocumentVersionsURLModel();
		this.versionsList = new URLListComponent(this.documentVersionsModel);
		theVersionsTab.add(this.versionsList.setLimit("0, 0"));

		theTabFolder.add(theVersionsTab);

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
		this.saveButton.addActionListener(Button.ACTION_CLICK, this);
		this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		theButtonsPanel.add(this.saveButton.setLimit("1, 0"));

		this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
		this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));

		super.add(theTabFolder.setLimit("0, 0"));
		super.add(theButtonsPanel.setLimit("0, 1"));

		this.setButtonsStatus();
		this.setVisible(true);
	}

	public DocumentView(Integer aDocumentId, DocumentMaintenance aDocumentMaintenance) {
		this(aDocumentMaintenance);
		this.viewDocument(aDocumentId);
	}

	private void viewDocument(Integer aDocumentId) {
		Map<String, Object> theData = this.documentMaintenance.getDocumentDataBy(aDocumentId);
		String theDescription = (String) theData.get(DocumentMaintenance.DESCRIPTION);
		this.descriptionTextField.setText(theDescription);
		this.documentVersionsModel.setData((List) theData.get(DocumentMaintenance.VERSIONS));
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveDocument();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			this.setVisible(false);
		}
	}

	private void saveDocument() {
		FileInfo theFile = this.fileChooser.getFileInfo();
		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(DocumentMaintenance.DESCRIPTION, this.descriptionTextField.getText());
		theData.put(DocumentMaintenance.FILE, this.getFileBytes(theFile));
		if (theFile != null) {
			theData.put(DocumentMaintenance.FILE_NAME, theFile.getName());
		}
		List<String> theErrors = this.documentMaintenance.saveDocument(theData);
		if (theErrors.isEmpty()) {
			this.setVisible(false);
		} else {
			this.viewErrors(theErrors);
		}
	}

	private byte[] getFileBytes(FileInfo aFile) {
		byte[] theFileBytes = null;
		try {
			if (aFile != null) {
				InputStream theInputStream = aFile.getInputStream();
				ByteArrayOutputStream theOutputStream = new ByteArrayOutputStream();
				int theCurrentByte;
				while ((theCurrentByte = theInputStream.read()) != -1) {
					theOutputStream.write((char) theCurrentByte);
				}
				theFileBytes = theOutputStream.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return theFileBytes;
	}

	private void viewErrors(List<String> aErrors) {
		StringBuffer theErrorMessages = new StringBuffer();
		for (String theError : aErrors) {
			theErrorMessages.append(theError);
			theErrorMessages.append("\n");
		}
		MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, theErrorMessages.toString());
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			this.documentMaintenance.endUnitOfWork();
		}
		super.setVisible(aVisible);
	}

	private void setButtonsStatus() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.DOCUMENT_MANAGEMENT_EDIT);
		this.saveButton.setEnabled(hasPrivilege);
	}
}
