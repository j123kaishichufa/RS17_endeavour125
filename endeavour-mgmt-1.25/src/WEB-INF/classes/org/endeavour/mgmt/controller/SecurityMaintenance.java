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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.endeavour.mgmt.controller.servlet.SecureServlet;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.model.RolesListModel;

import thinwire.render.web.WebApplication;
import thinwire.ui.Application;

public class SecurityMaintenance extends ApplicationController {

	private static Application.Local<SecurityMaintenance> instance = new Application.Local<SecurityMaintenance>();
	private ProjectMember currentUser = null;

	public SecurityMaintenance() {
		this(true);
	}

	public SecurityMaintenance(boolean initialize) {
		if (initialize) {
			instance.set(this);
		}
		this.initializeLibraries();
	}

	public static SecurityMaintenance getInstance() {
		if (instance.get() == null) {
			new SecurityMaintenance();
		}
		return instance.get();
	}

	public boolean verifyUser(String aUserName, String aPassword, List<String> aErrors) {
		ProjectMember theProjectMember = null;
		try {
			Properties theProperties = new Properties();
			theProperties.load(new FileInputStream(new File(getClass().getClassLoader().getResource("endeavour.settings").toURI())));
			boolean isLdapAuthentication = Boolean.parseBoolean(theProperties.getProperty("ldap.autentication.enabled"));
			if (isLdapAuthentication) {
				theProjectMember = authenticateLdapUser(aUserName, aPassword, theProperties, aErrors);
			} else {
				theProjectMember = authenticateStandardUser(aUserName, aPassword, aErrors, null);
			}
			if (theProjectMember != null) {
				this.storeUserInSession(theProjectMember);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theProjectMember != null;
	}

	private ProjectMember authenticateLdapUser(String aUserName, String aPassword, Properties aProperties, List<String> aErrors) {

		String theLdapServer = (String) aProperties.get("ldap.location");
		String theLdapOrganizationalUnit = (String) aProperties.get("ldap.ou");

		try {
			Hashtable<String, String> theEnvironmentProperties = new Hashtable<String, String>();
			theEnvironmentProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			theEnvironmentProperties.put(Context.PROVIDER_URL, theLdapServer);
			theEnvironmentProperties.put(Context.SECURITY_AUTHENTICATION, "simple");
			theEnvironmentProperties.put(Context.SECURITY_PRINCIPAL, "uid=" + aUserName + "," + theLdapOrganizationalUnit);
			theEnvironmentProperties.put(Context.SECURITY_CREDENTIALS, aPassword);
			DirContext ctx = new InitialDirContext(theEnvironmentProperties);

			SearchControls theConstraints = new SearchControls();
			theConstraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> theSearchResults = ctx.search(theLdapOrganizationalUnit, "uid=" + aUserName, theConstraints);

			if (theSearchResults.hasMore()) {
				SearchResult theSearchResult = theSearchResults.next();
				Attributes theAttributes = theSearchResult.getAttributes();

				SecurityGroupMaintenance theSecurityGroupMaintenance = new SecurityGroupMaintenance();
				Integer theSecurityGroupId = theSecurityGroupMaintenance.getFirstSecurityGroupId();
				if (theSecurityGroupId != null) {

					String theFirstName = (theAttributes.get("givenName") != null) ? (String) theAttributes.get("givenName").get() : null;
					String theLastName = (theAttributes.get("sn") != null) ? (String) theAttributes.get("sn").get() : null;
					String theEmail = (theAttributes.get("mail") != null) ? (String) theAttributes.get("mail").get() : null;

					List<Object> theParameters = new ArrayList<Object>();
					theParameters.add(aUserName);
					ProjectMember theUser = (ProjectMember) PersistenceManager.getInstance().findBy("select user from " + ProjectMember.class.getSimpleName() + " user where user.userId = ?", theParameters);
					if (theUser == null) {
						theUser = this.saveProjectMember(theUser, theFirstName, theLastName, theEmail, aUserName, aPassword, theSecurityGroupId);
					} else {
						Map<String, Object> theUserData = theUser.getData();
						String theUserFirstName = (String) theUserData.get(ProjectMember.FIRST_NAME);
						String theUserLastName = (String) theUserData.get(ProjectMember.LAST_NAME);
						String theUserId = (String) theUserData.get(ProjectMember.USER_ID);
						String theUserEmail = (String) theUserData.get(ProjectMember.EMAIL);
						String theUserPassword = (String) theUserData.get(ProjectMember.PASSWORD);
						if (!theUserFirstName.equals(theFirstName) || !theUserLastName.equals(theLastName) || !theUserId.equals(aUserName) || !theUserEmail.equals(theEmail) || !theUserPassword.equals(aPassword)) {
							theUser = this.saveProjectMember(theUser, theFirstName, theLastName, theEmail, aUserName, aPassword, theSecurityGroupId);
						}
					}
					this.currentUser = theUser;
				} else {
					aErrors.add(IViewConstants.RB.getString("no_security_groups.msg"));
				}
			} else {
				aErrors.add(IViewConstants.RB.getString("incorrect_login_credentials.msg"));
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return this.currentUser;
	}

	private void storeUserInSession(ProjectMember aUser) {
		try {
			WebApplication theWebApplication = (WebApplication) WebApplication.current();
			String theLocalHost = theWebApplication.clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");

			String theParameters = null;
			if (aUser != null) {
				theParameters = SecureServlet.USER_ID + "=" + aUser.getUserId() + "&" + SecureServlet.PASSWORD + "=" + ProjectMember.unencrypt(aUser.getPassword());
			} else {
				theParameters = SecureServlet.LOG_OFF + "=" + SecureServlet.LOG_OFF;
			}

			URL theURL = new URL(theLocalHost + "storeUser?" + theParameters);
			URLConnection theUrlConnection = (URLConnection) theURL.openConnection();
			theUrlConnection.setRequestProperty("Cookie", "JSESSIONID=" + theWebApplication.getSessionId());
			BufferedReader theBufferedReader = new BufferedReader(new InputStreamReader(theUrlConnection.getInputStream()));
			theBufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ProjectMember saveProjectMember(ProjectMember aProjectMember, String aFirstName, String aLastName, String aEmail, String aUserName, String aPassword, Integer aSecurityGroupId) {
		ProjectMemberMaintenance theProjectMemberMaintenance = new ProjectMemberMaintenance();
		theProjectMemberMaintenance.setProjectMember(aProjectMember);

		Map<String, Object> theData = new HashMap<String, Object>();
		theData.put(ProjectMember.FIRST_NAME, aFirstName);
		theData.put(ProjectMember.LAST_NAME, aLastName);
		theData.put(ProjectMember.EMAIL, aEmail);
		theData.put(ProjectMember.USER_ID, aUserName);
		theData.put(ProjectMember.PASSWORD, aPassword);
		theData.put(ProjectMember.PASSWORD2, aPassword);
		theData.put(ProjectMember.ACCEPT_NOTIFICATIONS, Boolean.FALSE);
		theData.put(ProjectMember.SECURITY_GROUP, aSecurityGroupId);
		theData.put(ProjectMember.ROLE, RolesListModel.STAKEHOLDER);
		theProjectMemberMaintenance.saveProjectMember(theData);
		return theProjectMemberMaintenance.getProjectMember();
	}

	public ProjectMember authenticateStandardUser(String aUserName, String aPassword, List<String> aErrors, PersistenceManager aPersistenceManager) {
		List<Object> theParameters = new ArrayList<Object>();
		theParameters.add(aUserName);
		if (aPersistenceManager == null) {
			aPersistenceManager = PersistenceManager.getInstance();
		}

		ProjectMember theUser = (ProjectMember) aPersistenceManager.findBy("select user from " + ProjectMember.class.getSimpleName() + " user where user.userId = ?", theParameters);
		if (theUser != null) {
			if (!theUser.isPasswordCorrect(aPassword)) {
				if (aErrors != null) {
					aErrors.add(IViewConstants.RB.getString("incorrect_password.msg"));
				}
			} else {
				this.currentUser = theUser;
			}
		} else {
			if (aErrors != null) {
				aErrors.add(IViewConstants.RB.getString("user_not_found.msg"));
			}
		}
		return this.currentUser;
	}

	public String getLoggedUserId() {
		return this.getCurrentUser().getUserId();
	}

	public ProjectMember getLoggedUser() {
		return this.getCurrentUser();
	}

	public void resetUser() {
		this.currentUser = null;
		this.storeUserInSession(null);
	}

	public boolean hasPrivilege(String aPrivilege) {
		return this.getCurrentUser().hasPrivilege(aPrivilege);
	}

	private ProjectMember getCurrentUser() {
		PersistenceManager.getInstance().refresh(this.currentUser);
		PersistenceManager.getInstance().refresh(this.currentUser.getSecurityGroup());
		return this.currentUser;
	}

	public boolean isLoggedUserAdmin() {
		return this.currentUser.isAdministrator();
	}

	public boolean isUserEqualToLoggedUser(Integer aCurrentUserId) {
		return this.currentUser.getId().equals(aCurrentUserId);
	}
}
