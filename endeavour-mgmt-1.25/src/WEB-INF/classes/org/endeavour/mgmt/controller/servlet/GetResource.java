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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetResource extends HttpServlet {

	public static final long serialVersionUID = -8201632985098882L;

	public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {

		String theRealPath = getServletContext().getRealPath("/");
		String theFileName = aRequest.getParameter("FILE_NAME");
		File theFile = new File(theRealPath + theFileName);
		FileInputStream theInputStream = new FileInputStream(theFile);
		byte[] theFileBytes = new byte[theInputStream.available()];
		theInputStream.read(theFileBytes);
		aResponse.getOutputStream().write(theFileBytes);
	}
}
