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
package org.endeavour.mgmt.controller.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.pentaho.reporting.designer.core.model.ModelUtility;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfOutputProcessor;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

@SuppressWarnings("unchecked")
public class ExecuteReport extends SecureServlet {

	public static final long serialVersionUID = -16712390453473412L;

	public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {
		try {
			this.authenticateCredentials(aRequest);
			String theNameParam = aRequest.getParameter("REPORT_NAME");
			if (super.hasAccess(aRequest, IPrivileges.REPORTS_VIEW)) {
				aResponse.setHeader("Content-Disposition", "filename=" + theNameParam);
				aResponse.setContentType("application/pdf");
				OutputStream theOutputStream = aResponse.getOutputStream();

				ResourceManager theResourceManager = new ResourceManager();
				theResourceManager.registerDefaults();

				String theReportFileParam = aRequest.getParameter("REPORT_FILE");
				String theRealPath = getServletContext().getRealPath("/");

				File theReportFile = new File(theRealPath + "reports/" + theReportFileParam);
				Resource theResource = theResourceManager.createDirectly(theReportFile, MasterReport.class);
				MasterReport theReport = (MasterReport) theResource.getResource();

				PersistenceManager thePersistenceManager = new PersistenceManager();
				thePersistenceManager.beginTransaction();
				Connection theConnection = thePersistenceManager.openConnection();

				ReportDataFactory theDataFactory = new ReportDataFactory(theConnection, theReport);
				SubReport[] theSubReports = ModelUtility.findSubReports(theReport);
				for (int i = 0; i < theSubReports.length; i++) {
					theDataFactory = new ReportDataFactory(theConnection, theSubReports[i]);
					theSubReports[i].setDataFactory(theDataFactory);
				}

				Enumeration theParameterNames = aRequest.getParameterNames();
				String theKey = null;
				Object theValue = null;
				while (theParameterNames.hasMoreElements()) {
					theKey = (String) theParameterNames.nextElement();
					if (!theKey.equals("REPORT_NAME") && !theKey.trim().equals("REPORT_FILE") && !theKey.trim().equals(ProjectMember.USER_ID) && !theKey.trim().equals(ProjectMember.PASSWORD)) {
						theValue = this.processParameterValue(aRequest.getParameter(theKey));
						theReport.getParameterValues().put(theKey, theValue);
					}
				}

				BufferedOutputStream theOutputBuffer = new BufferedOutputStream(theOutputStream);
				PdfOutputProcessor theOutputProcessor = new PdfOutputProcessor(theReport.getConfiguration(), theOutputBuffer, theReport.getResourceManager());
				PageableReportProcessor theReportProcessor = new PageableReportProcessor(theReport, theOutputProcessor);

				theReportProcessor.processReport();
				theReportProcessor.close();
				theOutputBuffer.flush();
				thePersistenceManager.endTransaction();
				thePersistenceManager.closeConnection();
			} else {
				aResponse.addHeader("WWW-Authenticate", "BASIC realm=\"Endeavour Agile ALM\"");
				aResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}

		} catch (Exception aExc) {
			aExc.printStackTrace();
		}
	}

	private void authenticateCredentials(HttpServletRequest aRequest) {

		String theAuthorization = aRequest.getHeader("authorization");
		if (theAuthorization != null) {
			String theCredentials = ProjectMember.unencrypt(theAuthorization.substring(theAuthorization.indexOf(" ") + 1, theAuthorization.length()));
			StringTokenizer theTokenizer = new StringTokenizer(theCredentials, ":");
			if (theTokenizer.countTokens() == 2) {
				String theUserId = theTokenizer.nextToken();
				String thePassword = theTokenizer.nextToken();
				super.authenticateCredentials(theUserId, thePassword, aRequest);
			}
		}
	}

	private Object processParameterValue(String aValue) {

		Object theProcessedValue = null;
		String theValueString = aValue.toString();
		if (theValueString.startsWith("[") && theValueString.endsWith("]")) {
			theValueString = theValueString.substring(theValueString.indexOf("[") + 1, theValueString.indexOf("]"));
			StringTokenizer theSingleSpec = new StringTokenizer(theValueString, ",");
			ArrayList theParameters = new ArrayList();
			while (theSingleSpec.hasMoreTokens()) {

				Object theValue = this.processValue(theSingleSpec.nextToken());
				theParameters.add(theValue);
			}
			theProcessedValue = theParameters.toArray();
		} else {
			theProcessedValue = this.processValue(aValue);
		}
		return theProcessedValue;
	}

	private Object processValue(String aValue) {

		StringTokenizer theSpecElements = new StringTokenizer(aValue, ":");

		Object theValue = theSpecElements.nextElement();
		String theType = theSpecElements.nextToken();
		if (theType.equals("Integer")) {
			theValue = new Integer(theValue.toString());
		}
		if (theType.equals("String")) {
			theValue = theValue.toString();
		}
		return theValue;
	}

	class ReportDataFactory extends SQLReportDataFactory {

		public static final long serialVersionUID = -11232394278123412L;

		public ReportDataFactory(Connection aConnection, AbstractReportDefinition aReport) throws ReportDataFactoryException {
			super(aConnection);
			CompoundDataFactory theCompoundDataFactory = (CompoundDataFactory) aReport.getDataFactory();
			SQLReportDataFactory theSQLReportDataFactory = (SQLReportDataFactory) theCompoundDataFactory.get(0);
			String[] theQueryNames = theSQLReportDataFactory.getQueryNames();
			String theQueryName = null;
			String theQuery = null;
			DataFactory theDataFactory = null;
			for (int i = 0; i < theQueryNames.length; i++) {
				theQueryName = theQueryNames[i];
				theQuery = theSQLReportDataFactory.getQuery(theQueryName);
				theDataFactory = theCompoundDataFactory.getDataFactoryForQuery(theQueryName);
				if (theDataFactory instanceof SQLReportDataFactory) {
					this.setQuery(theQueryName, theQuery);
				}
			}
			theCompoundDataFactory.remove(0);
			theCompoundDataFactory.add(0, this);
		}

		public synchronized void close() {
		}
	}
}
