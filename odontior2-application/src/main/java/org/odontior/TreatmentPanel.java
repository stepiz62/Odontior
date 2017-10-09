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

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.RadioButton;
import org.joty.workstation.gui.Table;

public class TreatmentPanel extends DataScrollingPanel {
	public TreatmentPanel() {

		JLabel lblSymbol = new JotyLabel(appLang("Symbol"));
		lblSymbol.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSymbol.setBounds(5, 6, 61, 14);
		add(lblSymbol);

		JotyTextField symbol = Factory.createText(this, "symbol", "symbol", 12);
		symbol.setBounds(67, 3, 84, 20);
		add(symbol);
		term("symbol").setMandatory();

		JLabel lblDescription = new JotyLabel(appLang("Description"));
		lblDescription.setBounds(4, 30, 141, 14);
		add(lblDescription);

		JotyTextField description = Factory.createText(this, "description", "descr", 48);
		description.setBounds(3, 45, 303, 20);
		add(description);
		term("description").setMandatory();

		JotyTextField dflt_duration = Factory.createLongNum(this, "dflt_duration", "dflt_duration", 3);
		dflt_duration.setBounds(265, 3, 39, 20);
		add(dflt_duration);
		term("dflt_duration").setMandatory();

		JLabel lblDefaultDuration = new JotyLabel(appLang("DefDuration"));
		lblDefaultDuration.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDefaultDuration.setBounds(157, 6, 105, 14);
		add(lblDefaultDuration);

		JLabel lblType = new JotyLabel(appLang("Type"));
		lblType.setBounds(312, 1, 105, 14);
		add(lblType);

		RadioButton type = Factory.createGroupMasterRadio(this, "toothOp", "type");
		type.setText(appLang("ToothTreatment"));
		type.setBounds(312, 16, 132, 16);
		add(type);

		RadioButton rdbtnGeneralTreatment = Factory.createRadioForGroup(this, "generalOp", "toothOp");
		rdbtnGeneralTreatment.setText(appLang("GeneralTreatment"));
		rdbtnGeneralTreatment.setBounds(312, 34, 132, 16);
		add(rdbtnGeneralTreatment);

		RadioButton rdbtnAny = Factory.createRadioForGroup(this, "anyOp", "toothOp");
		rdbtnAny.setText(appLang("Any"));
		rdbtnAny.setBounds(312, 52, 132, 16);
		add(rdbtnAny);

		addIntegerKeyElem("ID", true, true);

		addTermToGrid("symbol", appLang("Symbol"));
		addTermToGrid("description", appLang("Description"));

		enableRole("Administrators", Permission.all);
		enableRole("Doctors", Permission.readWrite);
		enableRole("Practitioners", Permission.read);
		enableRole("Administrative personnel", Permission.read);
	}

	@Override
	public boolean init() {
		return super.init();
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 20, 250 };
	}

}
