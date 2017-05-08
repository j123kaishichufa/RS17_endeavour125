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

import java.util.Map;

import org.endeavour.mgmt.controller.AttachmentMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;

import thinwire.ui.Button;
import thinwire.ui.Hyperlink;
import thinwire.ui.Label;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;


@SuppressWarnings("unchecked")
public class DownloadAttachmentView extends DialogComponent implements ActionListener {

	private Button okButton = null;
	private Hyperlink url = null;

	public DownloadAttachmentView(Integer aAttachmentId, AttachmentMaintenance aAttachmentMaintenance) {
		Map<String, Object> theData = aAttachmentMaintenance.getAttachmentDataBy(aAttachmentId);

		super.setTitle(IViewConstants.RB.getString("download_attachment.lbl"));
		
		super.setLayout(new TableLayout(new double[][] { { 0, 0, 0 }, { 20, 25 } }, 5, 5));
		
		super.setSize(365, 85);
		super.centerDialog();

		Label theNameLabel = new Label(IViewConstants.RB.getString("attachment.lbl") + ":"); 
		super.add(theNameLabel.setLimit("0, 0"));

		String theName = (String) theData.get(AttachmentMaintenance.NAME);

		Map<String, Object> theDownloadInfo = (Map) theData.get(AttachmentMaintenance.DOWNLOAD_INFO);
		String theUrl = (String) theDownloadInfo.get(theName);

		this.url = new Hyperlink(theName);
		this.url.setLocation(theUrl);
		super.add(this.url.setLimit("1, 0"));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
		super.add(this.okButton.setLimit("2, 1"));
		
		super.setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.OK_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}
}
