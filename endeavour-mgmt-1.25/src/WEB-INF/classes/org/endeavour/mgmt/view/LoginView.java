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

import java.util.ArrayList;
import java.util.List;

import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Application;
import thinwire.ui.Button;
import thinwire.ui.Frame;
import thinwire.ui.Hyperlink;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class LoginView implements ActionListener {

	private TextField userId = null;
	private TextField password = null;
	private PanelComponent loginPanel = null;

	public static void main(String args[]) {
		new LoginView();
	}

	public LoginView() {

		Frame theMainFrame = Application.current().getFrame();
		theMainFrame.setTitle("Endeavour - " + IViewConstants.SUB_TITLE);

		PanelComponent theMainPanel = new PanelComponent();
		theMainPanel.setLayout(new TableLayout(new double[][] { { 0, 823, 0 }, { 159, 350, 0 } }));
		theMainPanel.setSize(theMainFrame.getWidth(), theMainFrame.getHeight());

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.ENDEAVOUR_HEADER);
		theMainPanel.add(theLogo.setLimit("1, 0"));

		PanelComponent theLayoutPanel = new PanelComponent();
		theLayoutPanel.setLayout(new TableLayout(new double[][] { { 0, 0, 0 }, { 100, 25, 55, 60, 0 } }));
		theMainPanel.add(theLayoutPanel.setLimit("1, 1"));

		Label theLabel = new Label("  " + IViewConstants.LOG_IN_LABEL);
		theLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theLabel.getStyle().setProperty(Font.PROPERTY_FONT_SIZE, new Double(12));
		theLayoutPanel.add(theLabel.setLimit("1, 1"));

		this.loginPanel = new PanelComponent();
		this.loginPanel.setLayout(new TableLayout(new double[][] { { 80, 180 }, { 20, 20 } }, 5, 5));

		Label theUserIdLabel = new Label(IViewConstants.RB.getString("username.lbl"));
		this.loginPanel.add(theUserIdLabel.setLimit("0, 0"));

		this.userId = new TextField();
		this.userId.setFocus(true);
		// this.userId.setText("Admin");
		this.loginPanel.add(this.userId.setLimit("1, 0"));

		Label thePasswordLabel = new Label(IViewConstants.RB.getString("password.lbl"));
		this.loginPanel.add(thePasswordLabel.setLimit("0, 1"));

		this.password = new TextField();
		this.password.setInputHidden(true);
		// this.password.setText("password");
		this.loginPanel.add(this.password.setLimit("1, 1"));

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 32, 10, 32, 10, 32, 54, 100 }, { 25, 0 } }));

		Button theOkButton = new Button(IViewConstants.LOG_IN_LABEL);
		theOkButton.addActionListener(Button.ACTION_CLICK, this);
		theOkButton.setImage(IViewConstants.OK_BUTTON_ICON);
		theButtonsPanel.add(theOkButton.setLimit("6, 0"));

		Hyperlink theFacebookLink = new Hyperlink();
		theFacebookLink.setLocation("http://www.facebook.com/pages/Endeavour-ALM/144917658873616");
		theFacebookLink.getStyle().getBackground().setImage(IViewConstants.FACEBOOK_ICON);
		theButtonsPanel.add(theFacebookLink.setLimit("0, 1"));

		Hyperlink theTwitterLink = new Hyperlink();
		theTwitterLink.setLocation("http://twitter.com/endeavouralm");
		theTwitterLink.getStyle().getBackground().setImage(IViewConstants.TWITTER_ICON);
		theButtonsPanel.add(theTwitterLink.setLimit("2, 1"));

		Hyperlink theEmailLink = new Hyperlink("");
		theEmailLink.setLocation("mailto:e-cuellar@users.sourceforge.net");
		theEmailLink.getStyle().getBackground().setImage(IViewConstants.EMAIL_ICON);
		theButtonsPanel.add(theEmailLink.setLimit("4, 1"));

		theLayoutPanel.add(theButtonsPanel.setLimit("1, 3"));
		theLayoutPanel.add(this.loginPanel.setLimit("1, 2"));

		Label theFooter = new Label();
		theFooter.getStyle().getBackground().setImage(IViewConstants.ENDEAVOUR_FOOTER);
		theMainPanel.add(theFooter.setLimit("1, 2"));

		theMainFrame.getChildren().add(theMainPanel);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theButton = (Button) aEvt.getSource();
		if (theButton.getText().equals(IViewConstants.LOG_IN_LABEL)) {
			login();
		}
	}

	private void login() {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		theSecurityMaintenance.startUnitOfWork();
		List<String> theErrors = new ArrayList<String>();
		boolean hasAccess = theSecurityMaintenance.verifyUser(userId.getText(), password.getText(), theErrors);
		if (hasAccess) {
			loginPanel.setVisible(false);
			new MainView();
		} else {
			MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, IViewConstants.RB.getString("bad_login.msg"));
		}
		theSecurityMaintenance.endUnitOfWork();
	}
}
