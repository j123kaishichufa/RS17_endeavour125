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

import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.TextArea;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class AboutView extends DialogComponent implements ActionListener {

	private Button okButton = null;
	private StringBuffer license = null;
	private TextArea textArea = null;

	public AboutView() {
		super.setTitle(IViewConstants.RB.getString("about.lbl") + " Endeavour - " + IViewConstants.SUB_TITLE);
		super.setSize(410, 250);
		super.centerDialog();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 26, 25, 130, 0 } }, 5, 5));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.ENDEAVOUR_LOGO);
		this.add(theLogo.setLimit("0, 0"));

		Label theLabel = new Label("  " + IViewConstants.SUB_TITLE);
		theLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.add(theLabel.setLimit("0, 1"));

		this.textArea = new TextArea();
		this.textArea.setEnabled(false);
		this.textArea.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.add(this.textArea.setLimit("0, 2"));

		this.license = new StringBuffer();

		this.license.append("This program is free software: you can redistribute it and/or modify\n");
		this.license.append("it under the terms of the GNU General Public License as published by\n");
		this.license.append("the Free Software Foundation, either version 3 of the License, or\n");
		this.license.append("(at your option) any later version.\n\n");

		this.license.append("This program is distributed in the hope that it will be useful,\n");
		this.license.append("but WITHOUT ANY WARRANTY; without even the implied warranty of\n");
		this.license.append("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n");
		this.license.append("GNU General Public License for more details.");

		this.textArea.setText(this.license.toString());

		PanelComponent theOkButtonPanel = new PanelComponent();
		theOkButtonPanel.setLayout(new TableLayout(new double[][] { { 0, 100 }, { 0 } }));

		this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
		this.okButton.addActionListener(Button.ACTION_CLICK, this);
		this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);

		theOkButtonPanel.add(this.okButton.setLimit("1, 0"));

		this.add(theOkButtonPanel.setLimit("0, 3"));

		super.setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvt) {
		super.setVisible(false);
	}
}
