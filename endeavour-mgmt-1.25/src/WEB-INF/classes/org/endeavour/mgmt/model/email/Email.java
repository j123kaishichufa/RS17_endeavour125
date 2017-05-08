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
package org.endeavour.mgmt.model.email;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.endeavour.mgmt.model.ProjectMember;

public class Email {

	private String fromAddress = null;
	private String protocol = "smtp";
	private boolean hasConnection = false;
	private boolean setupIsCorrect = true;
	private Session mailSession = null;
	private Transport transport = null;

	private static Email email = null;

	public static Email getInstance() {
		if (email == null) {
			email = new Email();
		}
		return email;
	}

	private boolean startSession() {
		try {
			if (this.hasConnection == false) {
				Properties theEmailConfiguration = new Properties();
				theEmailConfiguration.load(new FileInputStream(new File(getClass().getClassLoader().getResource("endeavour.settings").toURI())));
				this.fromAddress = (String) theEmailConfiguration.get("mail.fromAddress");
				String theHostName = (String) theEmailConfiguration.get("mail.host");
				String theHostPort = (String) theEmailConfiguration.get("mail.port");
				String theAuthUser = (String) theEmailConfiguration.get("mail.userid");
				String theAuthPassword = (String) theEmailConfiguration.get("mail.password");
				boolean theUseSSL = Boolean.parseBoolean((String) theEmailConfiguration.get("mail.useSSL"));
				boolean theUseTLS = Boolean.parseBoolean((String) theEmailConfiguration.get("mail.useTLS"));
				boolean theUseAuth = true;

				if (theHostPort == null || theHostPort.trim().equals("")) {
					theHostPort = "25";
				}
				if (theAuthUser == null || theAuthUser.trim().equals("")) {
					theAuthUser = "";
					theUseAuth = false;
				}
				if (theAuthPassword == null || theAuthPassword.trim().equals("")) {
					theAuthPassword = "";
				}
				if (theUseSSL) {
					this.protocol = "smtps";
				}

				Properties theEmailProperties = new Properties();
				theEmailProperties.put("mail.host", theHostName.trim());
				theEmailProperties.put("mail.transport.protocol", this.protocol);
				if (theUseTLS) {
					theEmailProperties.put("mail.smtp.starttls.enable", "true");
					theEmailProperties.put("mail.smtp.ssl.protocols", "SSLv3 TLSv1");
				}
				theEmailProperties.put("mail." + this.protocol + ".port", theHostPort);

				this.mailSession = Session.getInstance(theEmailProperties, null);
				this.mailSession.setDebug(true);
				this.transport = this.mailSession.getTransport(this.protocol);
				if (theUseAuth) {
					this.transport.connect(theAuthUser.trim(), theAuthPassword.trim());
				} else {
					this.transport.connect();
				}
				this.hasConnection = true;

			}
		} catch (Exception e) {
			this.setupIsCorrect = false;
			e.printStackTrace();
		}
		return this.hasConnection;
	}

	public void endSession() {
		try {
			if (this.transport != null) {
				this.transport.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.hasConnection = false;
	}

	public void send(List<ProjectMember> aProjectMembers, String aSubject, String aMessage) throws Exception {

		if (this.startSession()) {
			MimeMessage theMessage = new MimeMessage(this.mailSession);
			theMessage.setFrom(new InternetAddress(this.fromAddress.trim()));
			theMessage.setSubject(aSubject);
			theMessage.setContent(aMessage, "text/plain");
			for (ProjectMember theProjectMember : aProjectMembers) {
				theMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(theProjectMember.getEmail()));
			}
			this.transport.sendMessage(theMessage, theMessage.getRecipients(Message.RecipientType.TO));
		}
	}

	public boolean isSetupCorrect() {
		return this.setupIsCorrect;
	}
}
