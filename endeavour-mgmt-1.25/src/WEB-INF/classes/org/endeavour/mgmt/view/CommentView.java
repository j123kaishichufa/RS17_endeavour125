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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavour.mgmt.controller.CommentMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.PanelComponent;

import thinwire.ui.Button;
import thinwire.ui.DropDownDateBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

public class CommentView extends DialogComponent implements ActionListener {

	private TextArea commentTextArea = null;
	private Button saveButton = null;
	private Button cancelButton = null;
	private CommentMaintenance commentMaintenance = null;
	private Label authorLabel = null;
	private TextField authorTextField = null;
	private Label dateLabel = null;
	private DropDownDateBox dateDropDown = null;

	public CommentView(Integer aCommentId, CommentMaintenance aCommentMaintenance) {
		this(aCommentMaintenance);
		this.viewComment(aCommentId);
	}

	public CommentView(CommentMaintenance aCommentMaintenance) {

		super.setLayout(new TableLayout(new double[][] { { 0 }, { 50, 0, 25 } }, 5, 5));

		this.commentMaintenance = aCommentMaintenance;
		this.commentMaintenance.reset();

		super.setTitle(IViewConstants.RB.getString("comment.lbl"));
		super.setSize(500, 505);
		super.centerDialog();

		PanelComponent theAuthorPanel = new PanelComponent();
		theAuthorPanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 20 } }, 5, 5));

		this.authorLabel = new Label(IViewConstants.RB.getString("author.lbl") + ":");
		theAuthorPanel.add(this.authorLabel.setLimit("0, 0"));

		SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
		this.authorTextField = new TextField();
		this.authorTextField.setEnabled(false);
		this.authorTextField.setText(theSecurityMaintenance.getLoggedUserId());
		theAuthorPanel.add(this.authorTextField.setLimit("1, 0"));

		this.dateLabel = new Label("Date:");
		theAuthorPanel.add(this.dateLabel.setLimit("0, 1"));

		this.dateDropDown = new DropDownDateBox(new SimpleDateFormat(IViewConstants.DATE_MASK).format(new Date()));
		this.dateDropDown.setEditMask(IViewConstants.DATE_MASK);
		this.dateDropDown.setEnabled(false);
		theAuthorPanel.add(this.dateDropDown.setLimit("1, 1"));

		super.add(theAuthorPanel.setLimit("0, 0"));

		PanelComponent theCommentPanel = new PanelComponent();
		theCommentPanel.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 5, 5));

		Label theCommentLabel = new Label(IViewConstants.RB.getString("comment.lbl") + ":");
		theCommentPanel.add(theCommentLabel.setLimit("0, 0"));

		this.commentTextArea = new TextArea();
		theCommentPanel.add(this.commentTextArea.setLimit("0, 1"));

		super.add(theCommentPanel.setLimit("0, 1"));

		PanelComponent theButtonsPanel = new PanelComponent();
		theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));

		this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
		this.saveButton.addActionListener(Button.ACTION_CLICK, this);
		this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
		theButtonsPanel.add(this.saveButton.setLimit("1, 0"));

		this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
		this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
		this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
		theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));

		super.add(theButtonsPanel.setLimit("0, 2"));

		super.setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvt) {
		Button theSource = (Button) aEvt.getSource();
		if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
			int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
			if (theResult == IViewConstants.YES) {
				this.saveComment();
			}
		}
		if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
			super.setVisible(false);
		}
	}

	private void saveComment() {
		try {
			Map<String, Object> theData = new HashMap<String, Object>();
			theData.put(CommentMaintenance.DATE, new SimpleDateFormat(IViewConstants.DATE_MASK).parse(this.dateDropDown.getText()));
			theData.put(CommentMaintenance.AUTHOR, this.authorTextField.getText());
			theData.put(CommentMaintenance.TEXT, this.commentTextArea.getText());
			List<String> theErrors = this.commentMaintenance.saveComment(theData);
			if (theErrors.isEmpty()) {
				super.setVisible(false);
			} else {
				this.viewErrors(theErrors);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void viewErrors(List<String> aErrors) {
		StringBuffer theErrorMessages = new StringBuffer();
		for (String theError : aErrors) {
			theErrorMessages.append(theError);
			theErrorMessages.append("\n");
		}
		MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, theErrorMessages.toString());
	}

	private void viewComment(Integer aCommentId) {
		Map<String, Object> theData = this.commentMaintenance.getCommentDataBy(aCommentId);

		String theAuthor = (String) theData.get(CommentMaintenance.AUTHOR);
		this.authorTextField.setText(theAuthor);

		Date theDate = (Date) theData.get(CommentMaintenance.DATE);
		this.dateDropDown.setText(new SimpleDateFormat(IViewConstants.DATE_MASK).format(theDate));

		String theText = (String) theData.get(CommentMaintenance.TEXT);
		this.commentTextArea.setText(theText);

		this.setButtonStatus();
	}

	private void setButtonStatus() {
		this.saveButton.setEnabled(this.commentMaintenance.isCommentOwnedByUser());
	}
}
