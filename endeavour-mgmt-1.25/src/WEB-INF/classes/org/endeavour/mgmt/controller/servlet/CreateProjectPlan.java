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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.endeavour.mgmt.controller.ProjectPlanMaintenance;
import org.endeavour.mgmt.model.IPlanElement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

public class CreateProjectPlan extends HttpServlet {

	public static final long serialVersionUID = -15216780234563412L;
	private String TIMELINE = "Timeline";
	private String SCHEDULED = "Scheduled";
	private String ARTIFACTS = "Artifacts";

	public void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws IOException {
		try {

			Document theDocument = new Document(PageSize.A4.rotate());
			aResponse.setContentType("application/pdf");
			PdfWriter.getInstance(theDocument, aResponse.getOutputStream());
			theDocument.open();

			String theProjectIdValue = aRequest.getParameter(ProjectPlanMaintenance.PROJECT_ID);
			StringTokenizer theId = new StringTokenizer(theProjectIdValue, ":");
			Integer theProjectId = new Integer(theId.nextToken());
			ProjectPlanMaintenance thePlanMaintenance = new ProjectPlanMaintenance(null);
			List<IPlanElement> thePlanElements = thePlanMaintenance.getProjectPlanReportingData(theProjectId);

			Task theTask = null;
			IPlanElement thePlanElement = null;
			TaskSeries theSeries = new TaskSeries(SCHEDULED);
			int theDataSetIndex = 0;

			Date theStartDate = null;
			Date theEndDate = null;
			String theDescription = null;

			for (int i = 0; i < thePlanElements.size(); i++) {
				thePlanElement = thePlanElements.get(i);

				theTask = new Task(thePlanElement.getElementType() + " : " + thePlanElement.getName(), thePlanElement.getStartDate(), thePlanElement.getEndDate());
				theTask.setPercentComplete(thePlanElement.getProgress() * 0.01);
				theSeries.add(theTask);
				
				if (i == 0) {
					theStartDate = thePlanElement.getStartDate();
					theEndDate = thePlanElement.getEndDate();
					theDescription = thePlanElement.getElementType() + " : " + thePlanElement.getName();
				}

				theDataSetIndex++;
				// Each page displays up to 25 tasks before creating a new one.
				if (theDataSetIndex == 25) {
					theDataSetIndex = 0;
					this.createReportPage(theDocument, theSeries, theStartDate, theEndDate, theDescription);
					theSeries = new TaskSeries(SCHEDULED);
				}
			}
			this.createReportPage(theDocument, theSeries, theStartDate, theEndDate, theDescription);

			theDocument.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createReportPage(Document aDocument, TaskSeries aTaskSeries, Date aStartDate, Date aEndDate, String aDescription) throws Exception {

		TaskSeriesCollection theDataset = new TaskSeriesCollection();
		theDataset.add(aTaskSeries);

		DateAxis theDateRange = new DateAxis(TIMELINE);
		theDateRange.setMinimumDate(aStartDate);
		theDateRange.setMaximumDate(aEndDate);

		JFreeChart theChart = ChartFactory.createGanttChart(aDescription, ARTIFACTS, null, theDataset, true, false, false);
		CategoryPlot theCategoryPlot = theChart.getCategoryPlot();
		theCategoryPlot.setRangeAxis(theDateRange);

		BufferedImage theChartImage = theChart.createBufferedImage(780, 520);
		ByteArrayOutputStream theOutputStream = new ByteArrayOutputStream();
		ImageIO.write(theChartImage, "png", theOutputStream);
		Image theDocumentImage = Image.getInstance(theOutputStream.toByteArray());
		aDocument.add(theDocumentImage);
	}
}
