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
package org.endeavour.mgmt.controller.ci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.endeavour.mgmt.controller.ApplicationController;

public class CIMaintenance extends ApplicationController {

	private String ciLocation = null;

	public CIMaintenance() {
		this.ciLocation = super.getSubsystemLocation("ci.location");
	}

	protected void createProject(String aProjectName, String aProjectDescription) {
		try {
			aProjectName = aProjectName.replaceAll(" ", "_");
			FileInputStream theInputStream = new FileInputStream(new File(getClass().getClassLoader().getResource("config.xml").toURI()));
			HttpURLConnection theOpenConnection = this.createConnection("/createItem?name=" + aProjectName);
			theOpenConnection.setRequestProperty("Content-Type", "text/xml");
			BufferedWriter theBufferedWriter = new BufferedWriter(new OutputStreamWriter(theOpenConnection.getOutputStream()));
			BufferedReader theBuffReader = new BufferedReader(new InputStreamReader(theInputStream));

			String theLine = null;
			while ((theLine = theBuffReader.readLine()) != null) {
				if (theLine.trim().equals("<description></description>")) {
					theLine = "<description>" + aProjectDescription + "</description>";
				}
				theBufferedWriter.write(theLine);
				theBufferedWriter.newLine();
			}

			theBuffReader.close();
			theBufferedWriter.flush();
			theBufferedWriter.close();
			theOpenConnection.connect();
			theOpenConnection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void deleteProject(String aProjectName) {
		try {
			aProjectName = aProjectName.replaceAll(" ", "_");
			HttpURLConnection theOpenConnection = this.createConnection("/job/" + aProjectName + "/doDelete");
			theOpenConnection.connect();
			theOpenConnection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void changeProjectDescription(String aProjectName, String aProjectDescription) {
		try {
			aProjectName = aProjectName.replaceAll(" ", "_");
			HttpURLConnection theOpenConnection = this.createConnection("/job/" + aProjectName + "/description");

			String theUrlParameters = "description=" + URLEncoder.encode(aProjectDescription, "UTF-8");
			theOpenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			theOpenConnection.setRequestProperty("Content-Length", "" + Integer.toString(theUrlParameters.getBytes().length));

			DataOutputStream theDataOutputStream = new DataOutputStream(theOpenConnection.getOutputStream());
			theDataOutputStream.writeBytes(theUrlParameters);
			theDataOutputStream.flush();
			theDataOutputStream.close();
			theOpenConnection.connect();
			theOpenConnection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void renameProject(String aOldProjectName, String aProjectNewName) {
		try {
			aOldProjectName = aOldProjectName.replaceAll(" ", "_");
			aProjectNewName = aProjectNewName.replaceAll(" ", "_");
			HttpURLConnection theOpenConnection = this.createConnection("/job/" + aOldProjectName + "/doRename");

			String theUrlParameters = "newName=" + URLEncoder.encode(aProjectNewName, "UTF-8");
			theOpenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			theOpenConnection.setRequestProperty("Content-Length", "" + Integer.toString(theUrlParameters.getBytes().length));

			DataOutputStream theDataOutputStream = new DataOutputStream(theOpenConnection.getOutputStream());
			theDataOutputStream.writeBytes(theUrlParameters);
			theDataOutputStream.flush();
			theDataOutputStream.close();
			theOpenConnection.connect();
			theOpenConnection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpURLConnection createConnection(String aCommand) throws Exception {
		URL theUrl = new URL(this.ciLocation + aCommand);
		HttpURLConnection theOpenConnection = (HttpURLConnection) theUrl.openConnection();
		theOpenConnection.setRequestMethod("POST");
		theOpenConnection.setDoOutput(true);
		theOpenConnection.setDoInput(true);
		theOpenConnection.setUseCaches(false);
		return theOpenConnection;
	}

	protected void createCIProject(String aProjectName, String aProjectDescription) {
		CreateProject theProjectCreator = new CreateProject(aProjectName, aProjectDescription);
		Thread theThread = new Thread(theProjectCreator);
		theThread.start();
	}

	protected void deleteCIProject(String aProjectName) {
		DeleteProject theProjectDeleter = new DeleteProject(aProjectName);
		Thread theThread = new Thread(theProjectDeleter);
		theThread.start();
	}

	protected void renameCIProject(String aOldProjectName, String aProjectNewName) {
		RenameProject theProjectRenamer = new RenameProject(aOldProjectName, aProjectNewName);
		Thread theThread = new Thread(theProjectRenamer);
		theThread.start();
	}

	protected void changeCIProjectDescription(String aProjectName, String aProjectDescription) {
		ChangeProjectDescription theDescriptionChanger = new ChangeProjectDescription(aProjectName, aProjectDescription);
		Thread theThread = new Thread(theDescriptionChanger);
		theThread.start();
	}
}
