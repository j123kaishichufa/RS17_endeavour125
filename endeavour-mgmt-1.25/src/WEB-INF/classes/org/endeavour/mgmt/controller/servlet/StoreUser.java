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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StoreUser extends SecureServlet {

	public static final long serialVersionUID = -11248723459993412L;

	public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {

		HttpSession theSession = aRequest.getSession();
		String theUserIdValue = aRequest.getParameter(USER_ID);
		String theUserPasswordValue = aRequest.getParameter(PASSWORD);
		String isLogOff = aRequest.getParameter(LOG_OFF);
		if (isLogOff == null) {
			super.authenticateCredentials(theUserIdValue, theUserPasswordValue, aRequest);
		} else {
			theSession.removeAttribute(PROJECT_MEMBER);
		}
	}
}
