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

import org.endeavour.mgmt.controller.StandardReportController;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Label;
import thinwire.ui.WebBrowser;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class StandardReportView extends PanelComponent {

	public StandardReportView(String aReport) {

		StandardReportController theReportController = new StandardReportController();

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 5, 5));

		PanelComponent theHeaderPanel = new PanelComponent();
		theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.REPORT_ICON);
		theHeaderPanel.add(theLogo.setLimit("0, 0"));

		Label theHeaderLabel = new Label();
		theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));

		super.add(theHeaderPanel.setLimit("0, 0"));

		WebBrowser theWebBrowser = new WebBrowser();
		this.add(theWebBrowser.setLimit("0, 1"));

		if (theReportController.isProjectSelected()) {
			String theLocation = theReportController.createReportLocation(aReport);
			theWebBrowser.setLocation(theLocation);
		}

		theHeaderLabel.setText(theReportController.getReportTitle(aReport));
	}
}
