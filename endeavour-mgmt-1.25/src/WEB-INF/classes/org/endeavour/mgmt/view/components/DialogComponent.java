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

package org.endeavour.mgmt.view.components;

import thinwire.render.web.WebApplication;
import thinwire.ui.Application;
import thinwire.ui.Component;
import thinwire.ui.Dialog;
import thinwire.ui.Frame;

public class DialogComponent extends Dialog {

	protected void centerDialog() {
		Frame theMainFrame = Application.current().getFrame();
		int theWidth = theMainFrame.getInnerWidth();
		int theHeight = theMainFrame.getInnerHeight();
		if (theWidth < 0) {
			theWidth = 560;
		}
		if (theHeight < 0) {
			theHeight = 400;
		}
		int theX = (theWidth / 2) - (super.getWidth() / 2);
		int theY = (theHeight / 2) - (super.getHeight() / 2);
		if (theX < 0) {
			theX = 0;
		}
		if (theY < 0) {
			theY = 0;
		}
		super.setX(theX);
		super.setY(theY);
	}

	public void add(Component aComponent) {
		super.getChildren().add(aComponent);
	}

	public void setVisible(boolean aVisible) {
		if (!aVisible) {
			((WebApplication) WebApplication.current()).clearRenderStateListeners();
		}
		super.setVisible(aVisible);
	}
}
