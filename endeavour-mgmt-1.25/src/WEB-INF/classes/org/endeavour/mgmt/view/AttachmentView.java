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

import org.endeavour.mgmt.controller.AttachmentMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.FileChooser;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.FileChooser.FileInfo;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class AttachmentView extends DialogComponent implements ActionListener {

	private FileChooser fileChooser = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private AttachmentMaintenance attachmentMaintenance = null;

	public AttachmentView(AttachmentMaintenance aAttachmentMaintenance) {
		this.attachmentMaintenance = aAttachmentMaintenance;
		this.attachmentMaintenance.reset();

		super.setLayout(new TableLayout(new double[][] { { 25, 0 }, { 20, 20, 25 } }, 5, 5));

		super.setTitle(IViewConstants.RB.getString("attachment.lbl"));
		super.setSize(575, 105);
		super.centerDialog();

		Label theFileLabel = new Label(IViewConstants.RB.getString("file.lbl"));
		super.add(theFileLabel.setLimit("0, 0"));

		this.fileChooser = new FileChooser();
		super.add(this.fileChooser.setLimit("1, 0"));
		
		Label theUploadLabel = new Label(IViewConstants.RB.getString("file_max_size.msg"));
		theUploadLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		super.add(theUploadLabel.setLimit("1, 1"));

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

		super.add(theButtonsPanel.setLimit("1, 2"));

		super.setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveAttachment();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}

	private void saveAttachment() {
		FileInfo theFile = this.fileChooser.getFileInfo();

		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(AttachmentMaintenance.FILE, this.getFileBytes(theFile));
		if (theFile != null) {
			theData.put(AttachmentMaintenance.FILE_NAME, theFile.getName());
		}
		List<String> theErrors = this.attachmentMaintenance.saveAttachment(theData);
		if (theErrors.isEmpty()) {
			super.setVisible(false);
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
}
