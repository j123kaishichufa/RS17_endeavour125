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

import java.util.HashMap;
import java.util.Map;

import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.MainView;

public class StandardReportController extends ApplicationController {

	private Map<String, Report> registry = null;
	private Integer projectId = null;

	public StandardReportController() {
		this.projectId = MainView.getProjectDropDown().getSelectedRowId();
		this.initializeRegistry();
	}

	public String createReportLocation(String aReport) {
		Report theReport = this.registry.get(aReport);
		return super.createReportURL(theReport.getReportOutput(), aReport, theReport.getReportParameters());
	}

	public String getReportTitle(String aReport) {
		Report theReport = this.registry.get(aReport);
		return theReport.getReportTitle();
	}

	private void initializeRegistry() {
		this.registry = new HashMap<String, Report>();

		Report theDefectsReport = new Report();
		theDefectsReport.setReportOutput("defects.pdf");
		theDefectsReport.setReportTitle(IViewConstants.RB.getString("defects_report.lbl"));
		Map<String, String> theDefectReportParameters = new HashMap<String, String>();
		theDefectReportParameters.put("project_id_parameter", this.projectId + ":Integer");
		theDefectsReport.setReportParameters(theDefectReportParameters);
		this.registry.put(IReportConstants.DEFECTS_REPORT, theDefectsReport);

		Report theIterationCumulativeReport = new Report();
		theIterationCumulativeReport.setReportOutput("iteration_cumulative_flow.pdf");
		theIterationCumulativeReport.setReportTitle(IViewConstants.RB.getString("iteration_cumulative_flow.lbl"));
		Map<String, String> theCumulativeReportParameters = new HashMap<String, String>();
		theCumulativeReportParameters.put("project_id_parameter", this.projectId + ":Integer");
		theIterationCumulativeReport.setReportParameters(theCumulativeReportParameters);
		this.registry.put(IReportConstants.ITERATION_CUMULATIVE_FLOW_REPORT, theIterationCumulativeReport);

		Report theIterationDefectsByPriority = new Report();
		theIterationDefectsByPriority.setReportOutput("iteration_defects_by_priority.pdf");
		theIterationDefectsByPriority.setReportTitle(IViewConstants.RB.getString("iteration_defects_by_priority.lbl"));
		Map<String, String> theIterationDefectsByPriorityParameters = new HashMap<String, String>();
		theIterationDefectsByPriorityParameters.put("project_id_parameter", this.projectId + ":Integer");
		theIterationDefectsByPriority.setReportParameters(theIterationDefectsByPriorityParameters);
		this.registry.put(IReportConstants.ITERATION_DEFECTS_BY_PRIORITY_REPORT, theIterationDefectsByPriority);

		Report theIterationDefectsByStatus = new Report();
		theIterationDefectsByStatus.setReportOutput("iteration_defects_by_status.pdf");
		theIterationDefectsByStatus.setReportTitle(IViewConstants.RB.getString("iteration_defects_by_status.lbl"));
		Map<String, String> theIterationDefectsByStatusParameters = new HashMap<String, String>();
		theIterationDefectsByStatusParameters.put("project_id_parameter", this.projectId + ":Integer");
		theIterationDefectsByStatus.setReportParameters(theIterationDefectsByStatusParameters);
		this.registry.put(IReportConstants.ITERATION_DEFECTS_BY_STATUS_REPORT, theIterationDefectsByStatus);
	}

	public boolean isProjectSelected() {
		return this.projectId != null;
	}

	class Report {

		private String reportOutput = null;
		private String reportTitle = null;
		private Map<String, String> reportParameters = null;

		public String getReportOutput() {
			return this.reportOutput;
		}

		public void setReportOutput(String aReportOutput) {
			this.reportOutput = aReportOutput;
		}

		public String getReportTitle() {
			return this.reportTitle;
		}

		public void setReportTitle(String aReportTitle) {
			this.reportTitle = aReportTitle;
		}

		public Map<String, String> getReportParameters() {
			return this.reportParameters;
		}

		public void setReportParameters(Map<String, String> aReportParameters) {
			this.reportParameters = aReportParameters;
		}
	}
}
