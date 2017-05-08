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
package org.endeavour.mgmt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;

import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.fonts.LibFontBoot;
import org.pentaho.reporting.libraries.resourceloader.LibLoaderBoot;

import thinwire.render.web.WebApplication;

public class ApplicationController extends Observable implements IReportConstants {

	public ApplicationController() {
	}

	protected void initializeLibraries() {
		LibLoaderBoot.getInstance().start();
		LibFontBoot.getInstance().start();
		ClassicEngineBoot.getInstance().start();
	}

	public void startUnitOfWork() {
		PersistenceManager.getInstance().beginTransaction();
	}

	public void endUnitOfWork() {
		PersistenceManager.getInstance().endTransaction();
	}

	protected String createReportURL(String aReportName, String aReportFile, Map<String, String> aReportParameters) {

		String theLocalHost = ((WebApplication) WebApplication.current()).clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");
		StringBuffer theURLBuffer = new StringBuffer();
		theURLBuffer.append(theLocalHost);
		theURLBuffer.append(REPORT_CONTROLLER);
		theURLBuffer.append("?");
		theURLBuffer.append(REPORT_NAME);
		theURLBuffer.append("=");
		theURLBuffer.append(aReportName);
		theURLBuffer.append("&");
		theURLBuffer.append(REPORT_FILE);
		theURLBuffer.append("=");
		theURLBuffer.append(aReportFile);

		if (aReportParameters != null) {
			for (String theParameterName : aReportParameters.keySet()) {
				theURLBuffer.append("&");
				theURLBuffer.append(theParameterName);
				theURLBuffer.append("=");
				theURLBuffer.append(aReportParameters.get(theParameterName));
			}
		}

		return theURLBuffer.toString();
	}

	public String getSubsystemLocation(String aSubsystem) {

		String theLocation = null;
		try {
			Properties theProperties = new Properties();
			theProperties.load(new FileInputStream(new File(getClass().getClassLoader().getResource("endeavour.settings").toURI())));
			theLocation = (String) theProperties.get(aSubsystem);
			if (theLocation.startsWith("http://localhost")) {
				String theRealLocation = ((WebApplication) WebApplication.current()).clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");
				theLocation = theLocation.replaceFirst("http://localhost", theRealLocation.substring(0, theRealLocation.lastIndexOf("/endeavour")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theLocation;
	}
}
