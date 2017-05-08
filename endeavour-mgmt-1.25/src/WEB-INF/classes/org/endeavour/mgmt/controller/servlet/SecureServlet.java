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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.persistence.PersistenceManager;

public class SecureServlet extends HttpServlet {

	public static final long serialVersionUID = -11232390009993412L;
	public static final String PROJECT_MEMBER = "PROJECT_MEMBER";
	public static final String USER_ID = "userId";
	public static final String PASSWORD = "password";
	public static final String LOG_OFF = "log_off";

	protected boolean hasAccess(HttpServletRequest aRequest, String aPrivilege) {
		boolean hasAccess = false;
		HttpSession theSession = aRequest.getSession();
		ProjectMember theProjectMember = (ProjectMember) theSession.getAttribute(PROJECT_MEMBER);
		if (theProjectMember != null) {
			PersistenceManager thePersistenceManager = new PersistenceManager();
			thePersistenceManager.beginTransaction();
			thePersistenceManager.attachToSession(theProjectMember);
			hasAccess = aPrivilege == null ? true : theProjectMember.hasPrivilege(aPrivilege);
			thePersistenceManager.endTransaction();
		}
		return hasAccess;
	}

	protected void authenticateCredentials(String aUserId, String aPassword, HttpServletRequest theRequest) {
		PersistenceManager thePersistenceManager = new PersistenceManager();
		thePersistenceManager.beginTransaction();
		SecurityMaintenance theSecurityMaintenance = new SecurityMaintenance(false);
		ProjectMember theProjectMember = theSecurityMaintenance.authenticateStandardUser(aUserId, aPassword, null, thePersistenceManager);
		thePersistenceManager.endTransaction();

		if (theProjectMember != null) {
			HttpSession theSession = theRequest.getSession();
			theSession.setAttribute(PROJECT_MEMBER, theProjectMember);
		}
	}
}
