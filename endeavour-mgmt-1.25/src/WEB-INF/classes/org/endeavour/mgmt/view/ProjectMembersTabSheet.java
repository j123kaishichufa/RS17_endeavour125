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

import java.util.ArrayList;
import java.util.List;

import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.AssignedProjectMembersListModel;
import org.endeavour.mgmt.view.model.UnassignedProjectMemebersListModel;

import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.event.DropEvent;
import thinwire.ui.event.DropListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

@SuppressWarnings("unchecked")
public class ProjectMembersTabSheet extends TabSheetComponent implements DropListener {

	private GridBoxComponent assignedProjectMembersGrid = null;
	private GridBoxComponent unassignedProjectMembersGrid = null;
	private AssignedProjectMembersListModel assignedProjectMembersModel = null;
	private UnassignedProjectMemebersListModel unassignedProjectMembersModel = null;

	public ProjectMembersTabSheet(String aTitle) {
		super(aTitle);

		super.setLayout(new TableLayout(new double[][] { { 0, 0 }, { 25, 0 } }, 5, 5));

		Label theProjectMembersAssignmentLabel = new Label(IViewConstants.DRAG_AND_DROP_MESSAGE);
		theProjectMembersAssignmentLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
		this.add(theProjectMembersAssignmentLabel.setLimit("0, 0"));

		this.initializeUnassignedProjectMembersGrid();
		this.initializeAssignedProjectMembersGrid();
		this.assignedProjectMembersGrid.addDropListener(this.unassignedProjectMembersGrid, this);
		this.unassignedProjectMembersGrid.addDropListener(this.assignedProjectMembersGrid, this);

	}

	private void initializeAssignedProjectMembersGrid() {
		this.assignedProjectMembersModel = new AssignedProjectMembersListModel();
		this.assignedProjectMembersGrid = new GridBoxComponent(this.assignedProjectMembersModel);
		this.add(this.assignedProjectMembersGrid.setLimit("1, 1"));
	}

	private void initializeUnassignedProjectMembersGrid() {
		this.unassignedProjectMembersModel = new UnassignedProjectMemebersListModel();
		this.unassignedProjectMembersGrid = new GridBoxComponent(this.unassignedProjectMembersModel);
		this.add(this.unassignedProjectMembersGrid.setLimit("0, 1"));
	}

	public void dropPerformed(DropEvent aEvent) {
		GridBoxComponent theDestinationGrid = (GridBoxComponent) aEvent.getSourceComponent();
		GridBoxComponent theSourceGrid = (GridBoxComponent) aEvent.getDragComponent();

		GridBox.Row theDragRow = ((GridBox.Range) aEvent.getDragObject()).getRow();
		theDragRow.setSelected(true);

		Object theDragObject = theSourceGrid.getSelectedRowObject();
		theSourceGrid.remove(theDragRow, theDragObject);
		theDestinationGrid.add(theDragRow, theDragObject);
		
		super.setCount(this.assignedProjectMembersModel.getRowCount());
	}

	public void setAssignedProjectMembers(List aProjectMembers) {
		this.assignedProjectMembersModel.setData(aProjectMembers);
		super.setCount(this.assignedProjectMembersModel.getRowCount());
	}

	public void setUnassignedProjectMembers(List aProjectMembers) {
		this.unassignedProjectMembersModel.setData(aProjectMembers);
	}

	public List<Integer> getAssignedProjectMembers() {
		List<Integer> theProjectMemberIds = new ArrayList<Integer>();
		theProjectMemberIds.addAll(this.assignedProjectMembersGrid.getAllRowIds());
		return theProjectMemberIds;
	}
}
