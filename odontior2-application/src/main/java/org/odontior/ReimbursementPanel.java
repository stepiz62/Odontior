/*
	Copyright (c) 2015, Stefano Pizzocaro. All rights reserved. Use is subject to license terms.
	
	This file is part of Odontior 2.0.
	
	Odontior 2.0 is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Odontior 2.0 is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public License
	along with Odontior 2.0.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.odontior;

import javax.swing.SwingConstants;

import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.NavigatorEditorPanel;
import org.joty.workstation.gui.Table;

public class ReimbursementPanel extends NavigatorEditorPanel {

	ReimbursementPanel() {
		m_table.setBounds(10, 11, 585, 303);
		Factory.addTextToGrid(this, "Symbol", appLang("Symbol"));
		Factory.addTextToGrid(this, "Descr", appLang("Treatment"));
		Factory.addDecimalToGrid(this, "Percentage", appLang("Percentage"));
		setExistenceMonitorKeyField("reimbursement_id");
		gridCellDescriptor("Percentage").setEditable();
		setDelRecordOnNullField("Percentage");

		setInsertFieldEvaluator(new InsertFieldEvaluator() {

			@Override
			public boolean setValue(String fieldName) {
				boolean retVal = true;
				if (fieldName.compareToIgnoreCase("reimbursementplan_id") == 0)
					m_gridBuffer.wfield(fieldName).setInteger(Long.parseLong(contextParameter("reimbursementPlanID")));
				else if (fieldName.compareToIgnoreCase("treatment_id") == 0)
					m_gridBuffer.wfield(fieldName).setInteger(m_gridManager.getCurrId());
				else
					retVal = false;
				return retVal;
			}
		});

		addIntegerKeyElem("ID", true, true);

		enableRole("Administrative personnel", Permission.all);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 100, 500, 80 };
		table.setColAlignement(2, SwingConstants.CENTER);
	}

}
