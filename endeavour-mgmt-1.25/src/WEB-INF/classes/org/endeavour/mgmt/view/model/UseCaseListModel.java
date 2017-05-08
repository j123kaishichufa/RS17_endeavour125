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

package org.endeavour.mgmt.view.model;

import java.util.ArrayList;
import java.util.List;

import org.endeavour.mgmt.model.UseCase;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class UseCaseListModel extends GridBoxModel {

	public UseCaseListModel(List<UseCase> aUseCases) {
		super();
		super.rows = this.initializeUseCases(aUseCases);
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		UseCase theUseCase = (UseCase) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theUseCase.getName();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		UseCase theUseCase = (UseCase) this.rows.get(aRowIndex);
		return theUseCase.getId();
	}

	private List<UseCase> initializeUseCases(List<UseCase> aUseCases) {

		List<UseCase> theUseCases = new ArrayList<UseCase>();
		UseCase theUseCase = new UseCase();
		theUseCase.setId(-1);
		theUseCase.setName("");
		theUseCases.add(theUseCase);

		for (UseCase theUC : aUseCases) {
			theUseCases.add(theUC);
		}
		return theUseCases;
	}
}
