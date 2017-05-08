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

package org.endeavour.mgmt.view;

import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.IReportConstants;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.ProjectsListModel;

import thinwire.ui.Application;
import thinwire.ui.Frame;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.Menu;
import thinwire.ui.MessageBox;
import thinwire.ui.Tree;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.SplitLayout;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Color;
import thinwire.ui.style.Font;

public class MainView implements ActionListener {

	private final String PROJECT_PLANNING_MENU_LABEL = IViewConstants.RB.getString("planning.lbl");
	private final String PROJECTS_MENU_LABEL = IViewConstants.RB.getString("projects.lbl");
	private final String PLAN_MENU_LABEL = IViewConstants.RB.getString("project_plan.lbl");
	private final String REQUIREMENTS_MENU_LABEL = IViewConstants.RB.getString("requirements.lbl");
	private final String USE_CASES_MENU_LABEL = IViewConstants.RB.getString("use_cases.lbl");
	private final String DEFECT_TRACKING_MENU_LABEL = IViewConstants.RB.getString("defect_tracking.lbl");
	private final String DEFECTS_MENU_LABEL = IViewConstants.RB.getString("defects.lbl");
	private final String HELP_MENU_LABEL = IViewConstants.RB.getString("help.lbl");
	private final String ABOUT_MENU_LABEL = IViewConstants.RB.getString("about.lbl");
	private final String TASKS_MENU_LABEL = IViewConstants.RB.getString("tasks.lbl");
	private final String ITERATIONS_MENU_LABEL = IViewConstants.RB.getString("iterations.lbl");
	private final String SECURITY_MENU_LABEL = IViewConstants.RB.getString("security.lbl");
	private final String USERS_MENU_LABEL = IViewConstants.RB.getString("users.lbl");
	private final String REPORTS_MENU_LABEL = IViewConstants.RB.getString("reports.lbl");
	private final String TEST_MANAGEMENT_MENU_LABEL = IViewConstants.RB.getString("test_management.lbl");
	private final String TEST_CASES_MENU_LABEL = IViewConstants.RB.getString("test_cases.lbl");
	private final String TEST_PLAN_MENU_LABEL = IViewConstants.RB.getString("test_plan.lbl");
	private final String CHANGE_REQUESTS_MENU_LABEL = IViewConstants.RB.getString("change_requests.lbl");
	private final String DOCUMENT_MANAGEMENT_MENU_LABEL = IViewConstants.RB.getString("document_management.lbl");
	private final String PROJECT_DOCUMENTS_MENU_LABEL = IViewConstants.RB.getString("project_documents.lbl");
	private final String PROJECT_MEMBER_ASSIGNMENTS = IViewConstants.RB.getString("project_member_assignments.lbl");
	private final String DEFECTS_REPORT_LABEL = IViewConstants.RB.getString("defects_report.lbl");
	private final String ITERATION_CUMULATIVE_FLOW_REPORT_LABEL = IViewConstants.RB.getString("iteration_cumulative_flow.lbl");
	private final String ITERATION_DEFECTS_BY_PRIORITY_LABEL = IViewConstants.RB.getString("iteration_defects_by_priority.lbl");
	private final String ITERATION_DEFECTS_BY_STATUS_LABEL = IViewConstants.RB.getString("iteration_defects_by_status.lbl");
	private final String TEST_PLAN_EXECUTION_LABEL = IViewConstants.RB.getString("test_plan_execution.lbl");
	private final String ACTORS_MENU_LABEL = IViewConstants.RB.getString("actors.lbl");
	private final String PROJECT_GLOSSARY_MENU_LABEL = IViewConstants.RB.getString("project_glossary.lbl");
	private final String WIKI_MENU_LABEL = IViewConstants.RB.getString("project_wiki.lbl");
	private final String UTILITIES_MENU_LABEL = IViewConstants.RB.getString("utilities.lbl");
	private final String CI_MENU_LABEL = IViewConstants.RB.getString("continuous_integration.lbl");
	private final String SVN_MENU_LABEL = IViewConstants.RB.getString("subversion_browser.lbl");
	private final String PERSONAL_SPACE_LABEL = IViewConstants.RB.getString("personal_space.lbl");
	private final String HOME_LABEL = IViewConstants.RB.getString("home.lbl");
	private final String SECURITY_GROUPS_MENU_LABEL = IViewConstants.RB.getString("security_groups.lbl");
	private final String LOG_OUT_MENU_LABEL = IViewConstants.RB.getString("logout.lbl");
	private final String FORUMS_MENU_LABEL = IViewConstants.RB.getString("forums.lbl");
	// private final String TIME_TRACKING_MENU_LABEL = "Time Tracking";
	// private final String TIMESHEETS_MENU_LABEL = "Timesheets";

	private Frame mainFrame = null;
	private PanelComponent mainPanel = null;
	private PanelComponent layoutPanel = null;
	private PanelComponent header = null;
	private Tree tree = null;
	private PanelComponent currentModule = null;
	private static Application.Local<DropDownGridBoxComponent> projectDropDown = new Application.Local<DropDownGridBoxComponent>();

	public MainView() {
		this.mainFrame = Application.current().getFrame();
		this.mainFrame.setTitle("Endeavour - " + IViewConstants.SUB_TITLE);

		addTopMenu();
		addHeader();
		addProjectDropDown();
		addLayoutPanel();
		addLeftMenu();
		addMainPanel();
	}

	private void addHeader() {
		this.mainFrame.setLayout(new TableLayout(new double[][] { { 0 }, { 60, 0 } }));
		this.header = new PanelComponent();
		this.header.setLayout(new TableLayout(new double[][] { { 10, 210, 62, 0, 0, 350 }, { 5, 26, 25 } }));
		this.header.getStyle().getBackground().setImage(IViewConstants.HEADER);

		Label theLogo = new Label();
		theLogo.getStyle().getBackground().setImage(IViewConstants.ENDEAVOUR_LOGO);
		this.header.add(theLogo.setLimit("1, 1"));

		Label theLabel = new Label("  " + IViewConstants.SUB_TITLE);
		theLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.header.add(theLabel.setLimit("1, 2"));

		Label theUserLabel = new Label(IViewConstants.RB.getString("user.lbl"));
		theUserLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.header.add(theUserLabel.setLimit("2, 2"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		Label theLoggedUserLabel = new Label(theSecurityMaintenance.getLoggedUser().getFullName());
		this.header.add(theLoggedUserLabel.setLimit("3, 2"));

		this.mainFrame.getChildren().add(this.header.setLimit("0, 0"));
	}

	private void addMainPanel() {
		this.mainPanel = new PanelComponent();
		this.mainPanel.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 5));
		this.layoutPanel.add(this.mainPanel);

		this.currentModule = new HomeView();
		this.mainPanel.add(this.currentModule.setLimit("0, 0"));
	}

	private void addLayoutPanel() {
		this.layoutPanel = new PanelComponent();
		this.mainFrame.getChildren().add(this.layoutPanel.setLimit("0, 1"));
		this.layoutPanel.getStyle().getBackground().setColor(Color.DIMGRAY);
		this.layoutPanel.setLayout(new SplitLayout(.16, true, 5));
	}

	private void addProjectDropDown() {
		PanelComponent theProjectPanel = new PanelComponent();
		theProjectPanel.setLayout(new TableLayout(new double[][] { { 60, 290 }, { 25 } }));
		Label theProjectLabel = new Label(IViewConstants.RB.getString("project.lbl") + ":");
		theProjectLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		theProjectLabel.setWrapText(true);
		theProjectPanel.add(theProjectLabel.setLimit("0, 0"));

		ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
		ProjectsListModel theProjectsModel = new ProjectsListModel(theProjectMaintenance.getProjects());
		DropDownGridBoxComponent theDropDown = new DropDownGridBoxComponent(theProjectsModel, 0);
		theDropDown.getComponent().addActionListener(GridBox.ACTION_CLICK, this);

		theProjectPanel.add(theDropDown.setLimit("1, 0"));
		this.header.add(theProjectPanel.setLimit("5, 2"));

		projectDropDown.set(theDropDown);
	}

	private void addLeftMenu() {
		this.tree = new Tree();
		this.tree.addActionListener(Tree.ACTION_CLICK, this);

		Tree.Item theRoot = this.tree.getRootItem();
		this.tree.setRootItemVisible(false);

		Tree.Item thePersonalSpaceMenuItem = new Tree.Item(this.PERSONAL_SPACE_LABEL);
		thePersonalSpaceMenuItem.setExpanded(true);
		theRoot.getChildren().add(thePersonalSpaceMenuItem);

		Tree.Item theHomeMenuItem = new Tree.Item(this.HOME_LABEL);
		theHomeMenuItem.setImage(IViewConstants.HOME_ICON);
		thePersonalSpaceMenuItem.getChildren().add(theHomeMenuItem);

		Tree.Item theProjectPlanningMenuItem = new Tree.Item(this.PROJECT_PLANNING_MENU_LABEL);
		theProjectPlanningMenuItem.setExpanded(true);
		theRoot.getChildren().add(theProjectPlanningMenuItem);

		Tree.Item theProjectsMenuItem = new Tree.Item(this.PROJECTS_MENU_LABEL);
		theProjectsMenuItem.setImage(IViewConstants.PROJECTS_ICON);
		theProjectPlanningMenuItem.getChildren().add(theProjectsMenuItem);

		Tree.Item theIterationsMenuItem = new Tree.Item(this.ITERATIONS_MENU_LABEL);
		theIterationsMenuItem.setImage(IViewConstants.ITERATIONS_ICON);
		theProjectPlanningMenuItem.getChildren().add(theIterationsMenuItem);

		Tree.Item thePlanMenuItem = new Tree.Item(this.PLAN_MENU_LABEL);
		thePlanMenuItem.setImage(IViewConstants.PROJECT_PLAN_ICON);
		theProjectPlanningMenuItem.getChildren().add(thePlanMenuItem);

		Tree.Item theRequirementsMenuItem = new Tree.Item(this.REQUIREMENTS_MENU_LABEL);
		theRequirementsMenuItem.setExpanded(true);
		theRoot.getChildren().add(theRequirementsMenuItem);

		Tree.Item theActorsMenuItem = new Tree.Item(this.ACTORS_MENU_LABEL);
		theActorsMenuItem.setImage(IViewConstants.ACTORS_ICON);
		theRequirementsMenuItem.getChildren().add(theActorsMenuItem);

		Tree.Item theUseCasesMenuItem = new Tree.Item(this.USE_CASES_MENU_LABEL);
		theUseCasesMenuItem.setImage(IViewConstants.USE_CASES_ICON);
		theRequirementsMenuItem.getChildren().add(theUseCasesMenuItem);

		Tree.Item theRequestsMenuItem = new Tree.Item(this.CHANGE_REQUESTS_MENU_LABEL);
		theRequestsMenuItem.setImage(IViewConstants.CHANGE_REQUESTS_ICON);
		theRequirementsMenuItem.getChildren().add(theRequestsMenuItem);

		Tree.Item theTasksMenuItem = new Tree.Item(this.TASKS_MENU_LABEL);
		theTasksMenuItem.setImage(IViewConstants.TASKS_ICON);
		theRequirementsMenuItem.getChildren().add(theTasksMenuItem);

		Tree.Item theGlosaryMenuItem = new Tree.Item(this.PROJECT_GLOSSARY_MENU_LABEL);
		theGlosaryMenuItem.setImage(IViewConstants.GLOSSARY_ICON);
		theRequirementsMenuItem.getChildren().add(theGlosaryMenuItem);

		Tree.Item theDefectTrackingMenuItem = new Tree.Item(this.DEFECT_TRACKING_MENU_LABEL);
		theDefectTrackingMenuItem.setExpanded(true);
		theRoot.getChildren().add(theDefectTrackingMenuItem);

		Tree.Item theDefectsMenuItem = new Tree.Item(this.DEFECTS_MENU_LABEL);
		theDefectsMenuItem.setImage(IViewConstants.DEFECTS_ICON);
		theDefectTrackingMenuItem.getChildren().add(theDefectsMenuItem);

		Tree.Item theTestManagementMenuItem = new Tree.Item(this.TEST_MANAGEMENT_MENU_LABEL);
		theTestManagementMenuItem.setExpanded(true);
		theRoot.getChildren().add(theTestManagementMenuItem);

		Tree.Item theTestCasesMenuItem = new Tree.Item(this.TEST_CASES_MENU_LABEL);
		theTestCasesMenuItem.setImage(IViewConstants.TEST_CASES_ICON);
		theTestManagementMenuItem.getChildren().add(theTestCasesMenuItem);

		Tree.Item theTestPlanMenuItem = new Tree.Item(this.TEST_PLAN_MENU_LABEL);
		theTestPlanMenuItem.setImage(IViewConstants.TEST_PLANS_ICON);
		theTestManagementMenuItem.getChildren().add(theTestPlanMenuItem);

		Tree.Item theDocumentManagementMenuItem = new Tree.Item(this.DOCUMENT_MANAGEMENT_MENU_LABEL);
		theDocumentManagementMenuItem.setExpanded(true);
		theRoot.getChildren().add(theDocumentManagementMenuItem);

		Tree.Item theDocumentsMenuItem = new Tree.Item(this.PROJECT_DOCUMENTS_MENU_LABEL);
		theDocumentsMenuItem.setImage(IViewConstants.DOCUMENTS_ICON);
		theDocumentManagementMenuItem.getChildren().add(theDocumentsMenuItem);

		Tree.Item theWikiMenuItem = new Tree.Item(this.WIKI_MENU_LABEL);
		theWikiMenuItem.setImage(IViewConstants.WIKI_ICON);
		theDocumentManagementMenuItem.getChildren().add(theWikiMenuItem);

		Tree.Item theCiAndSvnMenuItem = new Tree.Item(this.UTILITIES_MENU_LABEL);
		theCiAndSvnMenuItem.setExpanded(true);
		theRoot.getChildren().add(theCiAndSvnMenuItem);

		Tree.Item theCIMenuItem = new Tree.Item(this.CI_MENU_LABEL);
		theCIMenuItem.setImage(IViewConstants.CI_ICON);
		theCiAndSvnMenuItem.getChildren().add(theCIMenuItem);

		Tree.Item theSvnMenuItem = new Tree.Item(this.SVN_MENU_LABEL);
		theSvnMenuItem.setImage(IViewConstants.SVN_ICON);
		theCiAndSvnMenuItem.getChildren().add(theSvnMenuItem);
		
		Tree.Item theFourumsMenuItem = new Tree.Item(this.FORUMS_MENU_LABEL);
		theFourumsMenuItem.setImage(IViewConstants.FORUMS_ICON);
		theCiAndSvnMenuItem.getChildren().add(theFourumsMenuItem);
		
		// Tree.Item theTimeTrackingMenuItem = new
		// Tree.Item(this.TIME_TRACKING_MENU_LABEL);
		// theTimeTrackingMenuItem.setExpanded(true);
		// theRoot.getChildren().add(theTimeTrackingMenuItem);

		// Tree.Item theTimesheetsMenuItem = new
		// Tree.Item(this.TIMESHEETS_MENU_LABEL);
		// theTimeTrackingMenuItem.getChildren().add(theTimesheetsMenuItem);

		Tree.Item theReportsMenuItem = new Tree.Item(this.REPORTS_MENU_LABEL);
		theReportsMenuItem.setExpanded(true);
		theRoot.getChildren().add(theReportsMenuItem);

		Tree.Item theProjectMemberAssignemntsReportMenuItem = new Tree.Item(this.PROJECT_MEMBER_ASSIGNMENTS);
		theProjectMemberAssignemntsReportMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theProjectMemberAssignemntsReportMenuItem);

		Tree.Item theDefectsReportMenuItem = new Tree.Item(this.DEFECTS_REPORT_LABEL);
		theDefectsReportMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theDefectsReportMenuItem);

		Tree.Item theIterationCumulativeFlowReportMenuItem = new Tree.Item(this.ITERATION_CUMULATIVE_FLOW_REPORT_LABEL);
		theIterationCumulativeFlowReportMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theIterationCumulativeFlowReportMenuItem);

		Tree.Item theIterationDefectsByPriorityMenuItem = new Tree.Item(this.ITERATION_DEFECTS_BY_PRIORITY_LABEL);
		theIterationDefectsByPriorityMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theIterationDefectsByPriorityMenuItem);

		Tree.Item theIterationDefectsByStatusMenuItem = new Tree.Item(this.ITERATION_DEFECTS_BY_STATUS_LABEL);
		theIterationDefectsByStatusMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theIterationDefectsByStatusMenuItem);

		Tree.Item theTestPlanExecutionMenuItem = new Tree.Item(this.TEST_PLAN_EXECUTION_LABEL);
		theTestPlanExecutionMenuItem.setImage(IViewConstants.REPORT_ICON);
		theReportsMenuItem.getChildren().add(theTestPlanExecutionMenuItem);

		Tree.Item theSecurityMenuItem = new Tree.Item(this.SECURITY_MENU_LABEL);
		theSecurityMenuItem.setExpanded(true);
		theRoot.getChildren().add(theSecurityMenuItem);

		Tree.Item theUsersMenuItem = new Tree.Item(this.USERS_MENU_LABEL);
		theUsersMenuItem.setImage(IViewConstants.USERS_ICON);
		theSecurityMenuItem.getChildren().add(theUsersMenuItem);

		Tree.Item theSecurityGroupsMenuItem = new Tree.Item(this.SECURITY_GROUPS_MENU_LABEL);
		theSecurityGroupsMenuItem.setImage(IViewConstants.SECURITY_GROUPS_ICON);
		theSecurityMenuItem.getChildren().add(theSecurityGroupsMenuItem);

		Tree.Item theLogoutMenuItem = new Tree.Item(this.LOG_OUT_MENU_LABEL);
		theLogoutMenuItem.setImage(IViewConstants.LOGOUT_ICON);
		theSecurityMenuItem.getChildren().add(theLogoutMenuItem);

		this.layoutPanel.add(this.tree);
		theHomeMenuItem.setSelected(true);
	}

	private void addTopMenu() {

		Menu theRootMenu = new Menu();
		theRootMenu.addActionListener(Menu.ACTION_CLICK, this);
		Menu.Item theRootItem = theRootMenu.getRootItem();

		Menu.Item theHelpMenuItem = new Menu.Item(this.HELP_MENU_LABEL);
		theHelpMenuItem.setImage(IViewConstants.HELP_ICON);
		theRootItem.getChildren().add(theHelpMenuItem);

		Menu.Item theAboutMenuItem = new Menu.Item(this.ABOUT_MENU_LABEL);
		theHelpMenuItem.getChildren().add(theAboutMenuItem);

		mainFrame.setMenu(theRootMenu);
	}

	public void actionPerformed(ActionEvent aEvt) {
		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		theSecurityMaintenance.startUnitOfWork();
		try {
			Object theSource = aEvt.getSource();
			if (theSource instanceof Tree.Item) {
				Tree.Item theTreeItem = ((Tree.Item) aEvt.getSource());
				boolean hasAuthorization = false;
				this.mainPanel.remove(this.currentModule);
				this.currentModule = null;
				if (theTreeItem.getText().equals(this.HOME_LABEL)) {
					this.currentModule = new HomeView();
					hasAuthorization = true;
				}
				if (theTreeItem.getText().equals(this.PROJECTS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_VIEW)) {
						this.currentModule = new ProjectsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.USE_CASES_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_VIEW)) {
						this.currentModule = new UseCasesListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.ITERATIONS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_VIEW)) {
						this.currentModule = new IterationsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.TASKS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_VIEW)) {
						this.currentModule = new TasksListView(null, true);
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.DEFECTS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.DEFECT_TRACKING_VIEW)) {
						this.currentModule = new DefectsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.CHANGE_REQUESTS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_VIEW)) {
						this.currentModule = new ChangeRequestsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.TEST_CASES_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_VIEW)) {
						this.currentModule = new TestCasesListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.TEST_PLAN_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.TEST_MANAGEMENT_VIEW)) {
						this.currentModule = new TestPlansListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.PLAN_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_VIEW)) {
						this.currentModule = new ProjectPlanView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.USERS_MENU_LABEL)) {
					this.currentModule = new ProjectMembersListView();
					hasAuthorization = true;
				}
				if (theTreeItem.getText().equals(this.PROJECT_DOCUMENTS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.DOCUMENT_MANAGEMENT_VIEW)) {
						this.currentModule = new DocumentsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.ACTORS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_VIEW)) {
						this.currentModule = new ActorsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.SECURITY_GROUPS_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.SECURITY_VIEW)) {
						this.currentModule = new SecurityGroupsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.LOG_OUT_MENU_LABEL)) {
					hasAuthorization = true;
					if (this.logout()) {
						theSecurityMaintenance.resetUser();
					}
				}
				if (theTreeItem.getText().equals(this.PROJECT_GLOSSARY_MENU_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_VIEW)) {
						this.currentModule = new GlossaryTermsListView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.PROJECT_MEMBER_ASSIGNMENTS)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new ProjectMemberAssignmentsReportView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.DEFECTS_REPORT_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new StandardReportView(IReportConstants.DEFECTS_REPORT);
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.ITERATION_CUMULATIVE_FLOW_REPORT_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new StandardReportView(IReportConstants.ITERATION_CUMULATIVE_FLOW_REPORT);
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.ITERATION_DEFECTS_BY_PRIORITY_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new StandardReportView(IReportConstants.ITERATION_DEFECTS_BY_PRIORITY_REPORT);
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.ITERATION_DEFECTS_BY_STATUS_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new StandardReportView(IReportConstants.ITERATION_DEFECTS_BY_STATUS_REPORT);
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.TEST_PLAN_EXECUTION_LABEL)) {
					if (theSecurityMaintenance.hasPrivilege(IPrivileges.REPORTS_VIEW)) {
						this.currentModule = new TestPlanExecutionReportView();
						hasAuthorization = true;
					}
				}
				if (theTreeItem.getText().equals(this.WIKI_MENU_LABEL)) {
					String theLocation = theSecurityMaintenance.getSubsystemLocation("wiki.location");
					this.currentModule = new SubsystemIntegrationView(this.WIKI_MENU_LABEL, IViewConstants.WIKI_ICON, theLocation, "/en/" + MainView.getProjectDropDown().getText());
					hasAuthorization = true;
				}
				if (theTreeItem.getText().equals(this.CI_MENU_LABEL)) {
					String theLocation = theSecurityMaintenance.getSubsystemLocation("ci.location");
					this.currentModule = new SubsystemIntegrationView(this.CI_MENU_LABEL, IViewConstants.CI_ICON, theLocation, "");
					hasAuthorization = true;
				}
				if (theTreeItem.getText().equals(this.SVN_MENU_LABEL)) {
					String theLocation = theSecurityMaintenance.getSubsystemLocation("svn.location");
					this.currentModule = new SubsystemIntegrationView(this.SVN_MENU_LABEL, IViewConstants.SVN_ICON, theLocation, "");
					hasAuthorization = true;
				}
				if (theTreeItem.getText().equals(this.FORUMS_MENU_LABEL)) {
					String theLocation = theSecurityMaintenance.getSubsystemLocation("forums.location");
					this.currentModule = new SubsystemIntegrationView(this.FORUMS_MENU_LABEL, IViewConstants.FORUMS_ICON, theLocation, "");
					hasAuthorization = true;
				}
				if (this.currentModule != null) {
					this.mainPanel.add(this.currentModule.setLimit("0, 0"));
				}
				if (!hasAuthorization && !theTreeItem.hasChildren()) {
					MessageBox.show(IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.ACCESS_DENIED_MESSAGE);
				}
			}
			if (theSource instanceof Menu.Item) {
				Menu.Item theMenuItem = ((Menu.Item) aEvt.getSource());
				if (theMenuItem.getText().equals(this.ABOUT_MENU_LABEL)) {
					this.tree.getRootItem().setSelected(true);
					this.mainPanel.remove(this.currentModule);
					new AboutView();
				}
			}
			if (theSource instanceof GridBox.Range) {
				addLeftMenu();
				addMainPanel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		theSecurityMaintenance.endUnitOfWork();
	}

	private boolean logout() {
		boolean isLogout = false;
		int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.LOG_OUT_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
		if (theResult == IViewConstants.YES) {
			this.mainFrame.getChildren().clear();
			this.mainFrame.setMenu(null);
			this.mainFrame.setLayout(null);
			isLogout = true;
			new LoginView();
		}
		return isLogout;
	}

	public static DropDownGridBoxComponent getProjectDropDown() {
		return projectDropDown.get();
	}
}
