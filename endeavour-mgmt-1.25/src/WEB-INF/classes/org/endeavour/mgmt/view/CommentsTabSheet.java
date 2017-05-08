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

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.endeavour.mgmt.controller.CommentMaintenance;
import org.endeavour.mgmt.controller.WorkProductMaintenance;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.CommentsListModel;

import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.MessageBox;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class CommentsTabSheet extends TabSheetComponent implements ActionListener, Observer {

	private GridBoxComponent commentsGrid = null;
	private CommentMaintenance commentMaintenance = null;
	private Button newCommentButton = null;
	private Button viewCommentButton = null;
	private Button deleteCommentButton = null;
	private CommentsListModel commentsModel = null;

	public CommentsTabSheet(String aTitle) {

		super(aTitle);
		super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));

		this.initializeControllers();
		this.initializeCommentsGrid();

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100 }, { 0 } }, 0, 5));

		this.newCommentButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
		this.newCommentButton.setImage(IViewConstants.NEW_BUTTON_ICON);
		this.newCommentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.newCommentButton.setLimit("1, 0"));

		this.viewCommentButton = new Button(IViewConstants.VIEW_BUTTON_LABEL);
		this.viewCommentButton.setImage(IViewConstants.VIEW_BUTTON_ICON);
		this.viewCommentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.viewCommentButton.setLimit("2, 0"));

		this.deleteCommentButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
		this.deleteCommentButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
		this.deleteCommentButton.addActionListener(Button.ACTION_CLICK, this);
		theButtonsPanel.add(this.deleteCommentButton.setLimit("3, 0"));

		this.setButtonsStatus();
		super.add(theButtonsPanel.setLimit("0, 1"));
	}

	public void initializeCommentsGrid() {
		this.commentsModel = new CommentsListModel();
		this.commentsGrid = new GridBoxComponent(this.commentsModel);
		this.commentsGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
		this.commentsGrid.addActionListener(GridBox.ACTION_CLICK, this);
		super.add(this.commentsGrid.setLimit("0, 0"));
	}

	public void setData(Map<String, Object> aData) {
		this.commentsModel.setData((List) aData.get(WorkProductMaintenance.COMMENTS));
		this.commentMaintenance.setComments((List) aData.get(WorkProductMaintenance.COMMENTS));
		this.setSelectedComment();
		super.setCount(this.commentsModel.getRowCount());
	}

	public void getData(Map<String, Object> aData) {
		aData.put(WorkProductMaintenance.COMMENTS, this.commentMaintenance.getComments());
	}

	private void setSelectedComment() {
		Integer theCommentId = this.commentsGrid.getSelectedRowId();
		this.commentMaintenance.getCommentDataBy(theCommentId);
		this.setButtonsStatus();
	}

	public void actionPerformed(ActionEvent aEvent) {
		Object theSource = aEvent.getSource();
		if (theSource instanceof GridBox.Range) {
			String theAction = aEvent.getAction();
			if (theAction.equals(GridBox.ACTION_DOUBLE_CLICK)) {
				this.viewComment();
			} else if (theAction.equals(GridBox.ACTION_CLICK)) {
				this.setSelectedComment();
			}
		}
		if (theSource instanceof Button) {
			Button theButton = (Button) theSource;
			if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
				new CommentView(this.commentMaintenance);
			}
			if (theButton.getText().equals(IViewConstants.VIEW_BUTTON_LABEL)) {
				this.viewComment();
			}
			if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
				this.deleteComment();
			}
		}
	}

	private void viewComment() {
		Integer theCommentId = this.commentsGrid.getSelectedRowId();
		if (theCommentId != null) {
			this.commentsGrid.setEnabled(false);
			new CommentView(theCommentId, this.commentMaintenance);
			this.commentsGrid.setEnabled(true);
		}
	}

	private void initializeControllers() {
		this.commentMaintenance = new CommentMaintenance();
		this.commentMaintenance.addObserver(this);
	}

	public void update(Observable aObservable, Object aObject) {
		this.viewComments();
	}

	private void viewComments() {
		this.commentsModel.setData(this.commentMaintenance.getComments());
		Integer theId = this.commentMaintenance.getSelectedCommentId();
		if (theId != null) {
			this.commentsGrid.setSelectedRowById(theId);
		}
		this.setButtonsStatus();
		super.setCount(this.commentsModel.getRowCount());
	}

	private void setButtonsStatus() {
		boolean hasComments = !this.commentsGrid.getRows().isEmpty();
		this.viewCommentButton.setEnabled(hasComments);
		this.deleteCommentButton.setEnabled(hasComments && this.commentMaintenance.isCommentOwnedByUser());
	}

	private void deleteComment() {
		Integer theCommentId = this.commentsGrid.getSelectedRowId();
		if (theCommentId != null) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.commentMaintenance.reset();
				this.commentMaintenance.deleteComment(theCommentId);
				this.viewComments();
			}
		}
	}
}
