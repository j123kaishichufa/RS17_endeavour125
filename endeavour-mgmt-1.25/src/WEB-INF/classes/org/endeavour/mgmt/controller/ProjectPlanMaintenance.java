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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.model.Dependency;
import org.endeavour.mgmt.model.IPlanElement;
import org.endeavour.mgmt.model.Iteration;
import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.model.Task;
import org.endeavour.mgmt.model.WorkProduct;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

import thinwire.render.web.WebApplication;

public class ProjectPlanMaintenance extends ApplicationController {

	public static final String PROJECT = IViewConstants.RB.getString("project.lbl");
	public static final String ITERATION = IViewConstants.RB.getString("iteration.lbl");
	public static final String TASK = IViewConstants.RB.getString("task.lbl");
	public static final String DEFECT = IViewConstants.RB.getString("defect.lbl");
	public static final String USE_CASE = IViewConstants.RB.getString("use_case.lbl");
	public static final String CHANGE_REQUEST = IViewConstants.RB.getString("change_request.lbl");
	public static final String PROJECT_ID = "PROJECT_ID";

	private Project project = null;

	public ProjectPlanMaintenance(Project aProject) {
		this.project = aProject;
	}

	public List<IPlanElement> getProjectPlanData(Map<Integer, Integer> anIdToGridMappings, List<String> aExpandedRows) {
		Integer theRowId = 0;
		anIdToGridMappings.clear();
		List<IPlanElement> thePlanElements = new ArrayList<IPlanElement>();
		if (this.project != null) {
			thePlanElements.add(this.project);
			anIdToGridMappings.put(theRowId, this.project.getId());
			theRowId++;
			for (Iteration theIteration : this.project.getIterations()) {
				if (theIteration != null) {
					thePlanElements.add(theIteration);
					anIdToGridMappings.put(theRowId, theIteration.getId());
					theRowId++;
					if (aExpandedRows.contains(ITERATION + theIteration.getId())) {
						for (WorkProduct theWorkProduct : theIteration.getWorkProducts()) {
							if (theWorkProduct != null) {
								// Use Case Tasks are not displayed.
								if (theWorkProduct instanceof Task && !((Task) theWorkProduct).isOrphan()) {
									continue;
								}
								thePlanElements.add(theWorkProduct);
								anIdToGridMappings.put(theRowId, theWorkProduct.getId());
								theRowId++;
							}
						}
					}
				}
			}
			List<Task> theTasks = this.project.retrieveWorkProducts(Task.class);
			for (Task theTask : theTasks) {
				if (theTask.isOrphan() && theTask.isUnscheduled()) {
					thePlanElements.add(theTask);
					anIdToGridMappings.put(theRowId, theTask.getId());
					theRowId++;
				}
			}
		}
		return thePlanElements;
	}

	public String createProjectPlanURL() {
		String theLocalHost = ((WebApplication) WebApplication.current()).clientSideMethodCallWaitForReturn("tw_APP_URL", "valueOf");
		return theLocalHost + "createProjectPlan?" + PROJECT_ID + "=" + this.project.getId() + ":Integer";
	}

	public String createProjectSchedule() {

		Integer GROUP_PARENT = 1;
		Integer NOT_GROUP_PARENT = 0;
		StringBuffer theOut = new StringBuffer();

		SimpleDateFormat theDf = new SimpleDateFormat("MM/dd/yyyy");
		String theDateMask = IViewConstants.DATE_MASK.trim().toLowerCase();
		if (theDateMask.equals("mm/dd/yyyy") || theDateMask.equals("dd/mm/yyyy") || theDateMask.equals("yyyy-mm-dd")) {
			theDf = new SimpleDateFormat(IViewConstants.DATE_MASK);
		} else {
			theDateMask = "mm/dd/yyyy";
		}

		theOut.append("<html>\n");
		theOut.append("<head>\n");
		theOut.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"./getResource?FILE_NAME=/jsgantt/jsgantt.css\" />\n");
		theOut.append("<script language=\"javascript\" src=\"./getResource?FILE_NAME=/jsgantt/jsgantt.js\"></script>\n");
		theOut.append("<body>\n");
		theOut.append("<div style=\"position:relative\" class=\"gantt\" id=\"GanttChartDIV\"></div>\n");
		theOut.append("<script>\n");
		theOut.append("var g = new JSGantt.GanttChart('g',document.getElementById('GanttChartDIV'), 'month');\n");
		theOut.append("g.setShowRes(1);\n");
		theOut.append("g.setShowDur(1);\n");
		theOut.append("g.setShowComp(1);\n");
		theOut.append("g.setCaptionType('Resource');\n");
		theOut.append("g.setDateInputFormat('" + theDateMask + "');\n");
		theOut.append("g.setDateDisplayFormat('" + theDateMask + "');\n");
		theOut.append("if( g ) {\n");

		if (this.project != null) {
			Integer theId = 1;
			Integer theProjectId = theId;
			this.addTask(theOut, theProjectId, this.project.getName(), theDf.format(this.project.getStartDate()), theDf.format(this.project.getEndDate()), "-", this.project.getProgress(), GROUP_PARENT, 0, "");

			Map<Task, Integer> theTaskIds = this.getTaskIds();

			for (Iteration theIteration : this.project.getIterations()) {
				if (theIteration != null) {
					theId++;
					this.addTask(theOut, theId, theIteration.getName(), theDf.format(theIteration.getStartDate()), theDf.format(theIteration.getEndDate()), "-", theIteration.getProgress(), GROUP_PARENT, theProjectId, "");

					Integer theIterationId = theId;
					for (WorkProduct theWp : theIteration.getWorkProducts()) {
						if (theWp != null) {

							// Use Case Tasks are not displayed.
							if (theWp instanceof Task && !((Task) theWp).isOrphan()) {
								continue;
							}

							theId++;

							String theDependencies = "";
							if (theWp instanceof Task) {
								theDependencies = this.getDependencies((Task) theWp, theTaskIds);
							}

							this.addTask(theOut, theId, theWp.getName(), theDf.format(theWp.getStartDate()), theDf.format(theWp.getEndDate()), theWp.getFirstStakeHolder(), theWp.getProgress(), NOT_GROUP_PARENT, theIterationId, theDependencies);
						}
					}
				}
			}
			List<Task> theTasks = this.project.retrieveWorkProducts(Task.class);
			for (Task theTask : theTasks) {
				if (theTask.isOrphan() && theTask.isUnscheduled()) {
					Integer theTaskId = theTaskIds.get(theTask);
					String theDependencies = this.getDependencies(theTask, theTaskIds);
					this.addTask(theOut, theTaskId, theTask.getName(), theDf.format(theTask.getStartDate()), theDf.format(theTask.getEndDate()), theTask.getFirstStakeHolder(), theTask.getProgress(), NOT_GROUP_PARENT, theProjectId, theDependencies);
				}
			}
		}
		theOut.append("g.Draw();\n");
		theOut.append("g.DrawDependencies();\n");
		theOut.append("}\n");
		theOut.append("else\n");
		theOut.append("{\n");
		theOut.append("alert(\"not defined\");\n");
		theOut.append("}\n");
		theOut.append("</script>\n");
		theOut.append("</body>\n");
		theOut.append("</head>\n");
		theOut.append("</html>\n");
		return theOut.toString();
	}

	private Map<Task, Integer> getTaskIds() {
		Map<Task, Integer> theTaskIds = new HashMap<Task, Integer>();
		if (this.project != null) {
			Integer theId = 1;
			for (Iteration theIteration : this.project.getIterations()) {
				if (theIteration != null) {
					theId++;
					for (WorkProduct theWp : theIteration.getWorkProducts()) {
						if (theWp != null) {
							if (theWp instanceof Task && !((Task) theWp).isOrphan()) {
								continue;
							}
							theId++;
							if (theWp instanceof Task) {
								theTaskIds.put((Task) theWp, theId);
							}
						}
					}
				}
			}
			List<Task> theTasks = this.project.retrieveWorkProducts(Task.class);
			for (Task theTask : theTasks) {
				if (theTask.isOrphan() && theTask.isUnscheduled()) {
					theId++;
					theTaskIds.put(theTask, theId);
				}
			}
		}
		return theTaskIds;
	}

	private String getDependencies(Task aTask, Map<Task, Integer> aTaskIds) {

		StringBuffer theDependencyIds = new StringBuffer();
		List<Dependency> theDependencies = aTask.getDependencies();
		for (Dependency theDependency : theDependencies) {
			if (theDependency != null) {
				Task thePredecessor = theDependency.getPredecessor();
				Integer theDependencyId = aTaskIds.get(thePredecessor);
				theDependencyIds.append(theDependencyId);
				theDependencyIds.append(",");
			}
		}
		if (theDependencyIds.length() > 0) {
			theDependencyIds.replace(theDependencyIds.length() - 1, theDependencyIds.length(), "");
		}
		return theDependencyIds.toString();
	}

	private void addTask(StringBuffer aWriter, Integer anId, String aName, String aStartDate, String aEndDate, String aStakeholder, Integer aCompletionPercent, Integer isGroupParent, Integer aParentId, String aDependencies) {
		aWriter.append("g.AddTaskItem(new JSGantt.TaskItem(" + anId + ",'" + aName.replace("'","&#39;") + "','" + aStartDate + "','" + aEndDate + "','ff00ff', '', 0, '" + aStakeholder + "'," + aCompletionPercent + "," + isGroupParent + "," + aParentId + ", 1, '" + aDependencies + "', ''));\n");
	}

	public List<IPlanElement> getProjectPlanReportingData(Integer aProjectId) {

		PersistenceManager thePersistenceManager = new PersistenceManager();
		thePersistenceManager.beginTransaction();
		this.project = (Project) thePersistenceManager.findById(Project.class, aProjectId);

		List<IPlanElement> thePlanElements = new ArrayList<IPlanElement>();
		if (this.project != null) {
			thePlanElements.add(this.project);

			for (Iteration theIteration : this.project.getIterations()) {
				if (theIteration != null) {
					thePlanElements.add(theIteration);
					for (WorkProduct theWorkProduct : theIteration.getWorkProducts()) {
						if (theWorkProduct != null) {
							// Use Case Tasks are not displayed.
							if (theWorkProduct instanceof Task && !((Task) theWorkProduct).isOrphan()) {
								continue;
							}
							thePlanElements.add(theWorkProduct);
						}
					}
				}
			}
			List<Task> theTasks = this.project.retrieveWorkProducts(Task.class);
			for (Task theTask : theTasks) {
				if (theTask.isOrphan() && theTask.isUnscheduled()) {
					thePlanElements.add(theTask);
				}
			}
		}

		thePersistenceManager.endTransaction();
		return thePlanElements;
	}
}
