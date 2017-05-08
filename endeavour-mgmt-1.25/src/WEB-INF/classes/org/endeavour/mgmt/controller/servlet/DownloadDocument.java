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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.endeavour.mgmt.controller.DocumentMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.model.Document;

public class DownloadDocument extends SecureServlet {

	public static final long serialVersionUID = -11232390009993412L;

	public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {

		Integer theId = Integer.parseInt(aRequest.getParameter(Document.ID));
		Integer theVersion = Integer.parseInt(aRequest.getParameter(Document.VERSION));
		String theFileName = aRequest.getParameter(Document.FILE_NAME);

		if (super.hasAccess(aRequest, IPrivileges.DOCUMENT_MANAGEMENT_VIEW)) {
			aResponse.setHeader("Content-Disposition", "filename=" + theFileName);
			aResponse.setHeader("Content-Type", "text/plain");

			DocumentMaintenance theDocumentMaintenance = new DocumentMaintenance();
			byte[] theFile = theDocumentMaintenance.getDocumentVersionBy(theId, theVersion);
			OutputStream theOutputStream = aResponse.getOutputStream();
			theOutputStream.write(theFile);
		} else {
			aResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
