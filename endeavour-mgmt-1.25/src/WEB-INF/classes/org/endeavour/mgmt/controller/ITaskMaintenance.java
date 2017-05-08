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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import org.endeavour.mgmt.model.Document;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.model.Task;

public interface ITaskMaintenance extends IBasicInfoMaintenance {

	public Map<String, Object> getTaskDataBy(Integer aTaskId);

	public List<String> saveTask(Map<String, Object> aData);

	public void reset();

	public void deleteTask(Integer aTaskId);

	public void addObserver(Observer aObserver);

	public List<ProjectMember> getUnassignedProjectMembersDataForProject();

	public List<Document> getUnassignedDocuments();

	public List<Task> getTasks();

	public List<Task> getTasksBy(String aPriority, String aStatus, String aName, String aNumber);

	public Date getParentStartDate();

	public Date getParentEndDate();

	public void startUnitOfWork();

	public void endUnitOfWork();

	public Integer getSelectedWorkProductId();
	
	public Project getProject();
	
	public boolean isIterationDerived();
}