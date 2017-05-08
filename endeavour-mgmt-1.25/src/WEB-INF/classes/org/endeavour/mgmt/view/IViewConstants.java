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

import java.util.ResourceBundle;

import thinwire.ui.Application;

public interface IViewConstants {
	public static final ResourceBundle RB = ResourceBundle.getBundle("endeavour");
	public static final String SUB_TITLE = "Agile ALM " + IViewConstants.RB.getString("version.lbl");
	public static final String DATE_MASK = RB.getString("date_mask");
	public static final String APPLICATION_BASE_FOLDER = Application.current().getBaseFolder();
	public static final String OK_BUTTON_LABEL = RB.getString("ok.lbl");
	public static final String OK_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/ok.png";
	public static final String SAVE_BUTTON_LABEL = IViewConstants.RB.getString("save.lbl");
	public static final String SAVE_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/save.png";
	public static final String EDIT_BUTTON_LABEL = RB.getString("edit.lbl");
	public static final String EDIT_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/edit.png";
	public static final String CANCEL_BUTTON_LABEL = IViewConstants.RB.getString("cancel.lbl");
	public static final String CANCEL_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/cancel.png";
	public static final String DELETE_BUTTON_LABEL = RB.getString("delete.lbl");
	public static final String DELETE_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/delete.png";
	public static final String NEW_BUTTON_LABEL = RB.getString("new.lbl");
	public static final String NEW_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/new.png";
	public static final String PRINT_BUTTON_LABEL = RB.getString("print.lbl");
	public static final String PRINT_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/print.png";
	public static final String DEPENDENCIES_ICON = APPLICATION_BASE_FOLDER + "/images/dependencies.png";
	public static final String ADD_BUTTON_LABEL = RB.getString("add.lbl");
	public static final String COLLAPSE_BUTTON_LABEL = IViewConstants.RB.getString("collapse.lbl");
	public static final String COLLAPSE_ALL_BUTTON_LABEL = IViewConstants.RB.getString("collapse_all.lbl");
	public static final String COLLAPSE_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/minus.png";
	public static final String SAVE_DIALOG_MESSAGE = IViewConstants.RB.getString("save.msg");
	public static final String DELETE_DIALOG_MESSAGE = RB.getString("delete.msg");
	public static final String LOG_OUT_MESSAGE = RB.getString("logout.msg");
	public static final String ACCESS_DENIED_MESSAGE = RB.getString("access_denied.msg");
	public static final String DRAG_AND_DROP_MESSAGE = RB.getString("drag_and_drop.msg");
	public static final String WARNING_DIALOG_TITLE = RB.getString("warning.lbl");
	public static final String ERROR_DIALOG_TITLE = RB.getString("error.lbl");
	public static final String WARNING_DIALOG_BUTTONS = RB.getString("yes_no_buttons.lbl");
	public static final String EXPAND_BUTTON_LABEL = IViewConstants.RB.getString("expand.lbl");
	public static final String EXPAND_ALL_BUTTON_LABEL = IViewConstants.RB.getString("expand_all.lbl");
	public static final String EXPAND_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/plus.png";
	public static final String DOWNLOAD_BUTTON_LABEL = IViewConstants.RB.getString("download.lbl");
	public static final String VIEW_BUTTON_LABEL = IViewConstants.RB.getString("view.lbl");
	public static final String LOG_IN_LABEL = RB.getString("login.lbl");
	public static final String PRINT_PREVIEW = RB.getString("pring_preview.lbl");
	public static final String UPDATE_LABEL = RB.getString("update.lbl");
	public static final String DOWNLOAD_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/download.png";
	public static final String VIEW_BUTTON_ICON = APPLICATION_BASE_FOLDER + "/images/view.png";
	public static final String ACTORS_ICON = APPLICATION_BASE_FOLDER + "/images/actors.gif";
	public static final String CHANGE_REQUESTS_ICON = APPLICATION_BASE_FOLDER + "/images/change_requests.gif";
	public static final String DEFECTS_ICON = APPLICATION_BASE_FOLDER + "/images/defects.gif";
	public static final String DOCUMENTS_ICON = APPLICATION_BASE_FOLDER + "/images/documents.gif";
	public static final String GLOSSARY_ICON = APPLICATION_BASE_FOLDER + "/images/glossary.gif";
	public static final String ITERATIONS_ICON = APPLICATION_BASE_FOLDER + "/images/iterations.gif";
	public static final String PROJECT_PLAN_ICON = APPLICATION_BASE_FOLDER + "/images/project_plan.gif";
	public static final String REPORT_ICON = APPLICATION_BASE_FOLDER + "/images/report.gif";
	public static final String CI_ICON = APPLICATION_BASE_FOLDER + "/images/ci.png";
	public static final String SVN_ICON = APPLICATION_BASE_FOLDER + "/images/svn.png";
	public static final String FORUMS_ICON = APPLICATION_BASE_FOLDER + "/images/forums.gif";
	public static final String SECURITY_GROUPS_ICON = APPLICATION_BASE_FOLDER + "/images/security_groups.gif";
	public static final String LOGOUT_ICON = APPLICATION_BASE_FOLDER + "/images/logout.gif";
	public static final String TEST_CASES_ICON = APPLICATION_BASE_FOLDER + "/images/test_cases.gif";
	public static final String TEST_PLANS_ICON = APPLICATION_BASE_FOLDER + "/images/test_plan.gif";
	public static final String FOLDER_ICON = APPLICATION_BASE_FOLDER + "/images/folder.gif";
	public static final String USE_CASES_ICON = APPLICATION_BASE_FOLDER + "/images/use_cases.gif";
	public static final String USERS_ICON = APPLICATION_BASE_FOLDER + "/images/users.gif";
	public static final String TASKS_ICON = APPLICATION_BASE_FOLDER + "/images/tasks.gif";
	public static final String PROJECTS_ICON = APPLICATION_BASE_FOLDER + "/images/projects.gif";
	public static final String WIKI_ICON = APPLICATION_BASE_FOLDER + "/images/wiki.gif";
	public static final String HELP_ICON = APPLICATION_BASE_FOLDER + "/images/help.gif";
	public static final String ENDEAVOUR_LOGO = APPLICATION_BASE_FOLDER + "/images/logo.png";
	public static final String ENDEAVOUR_HEADER = APPLICATION_BASE_FOLDER + "/images/main-header.png";
	public static final String ENDEAVOUR_FOOTER = APPLICATION_BASE_FOLDER + "/images/main-footer.png";
	public static final String FACEBOOK_ICON = APPLICATION_BASE_FOLDER + "/images/facebook.png";
	public static final String TWITTER_ICON = APPLICATION_BASE_FOLDER + "/images/twitter.png";
	public static final String EMAIL_ICON = APPLICATION_BASE_FOLDER + "/images/email.png";
	public static final String COMMENTS_ICON = APPLICATION_BASE_FOLDER + "/images/comments.gif";
	public static final String ATTACHMENTS_ICON = APPLICATION_BASE_FOLDER + "/images/attachments.gif";
	public static final String PRINT_PREVIEW_ICON = APPLICATION_BASE_FOLDER + "/images/print_preview.gif";
	public static final String EVENTS_ICON = APPLICATION_BASE_FOLDER + "/images/events.gif";
	public static final String TIMESHEET_ICON = APPLICATION_BASE_FOLDER + "/images/time_sheet.gif";
	public static final String PROJECT_SCHEDULE_ICON = APPLICATION_BASE_FOLDER + "/images/project_schedule.gif";
	public static final String DOCUMENT_VERSION_ICON = APPLICATION_BASE_FOLDER + "/images/document_version.gif";
	public static final String PRIORITY_HIGH_ICON = APPLICATION_BASE_FOLDER + "/images/priority_1_high.gif";
	public static final String PRIORITY_MEDIUM_ICON = APPLICATION_BASE_FOLDER + "/images/priority_2_medium.gif";
	public static final String PRIORITY_LOW_ICON = APPLICATION_BASE_FOLDER + "/images/priority_3_low.gif";
	public static final String HOME_ICON = APPLICATION_BASE_FOLDER + "/images/home.png";
	public static final String PASS_ICON = APPLICATION_BASE_FOLDER + "/images/pass.gif";
	public static final String FAIL_ICON = APPLICATION_BASE_FOLDER + "/images/fail.gif";
	public static final String HEADER = APPLICATION_BASE_FOLDER + "/images/header.png";

	public static final int YES = 1;
	public static final int NO = 2;
}
