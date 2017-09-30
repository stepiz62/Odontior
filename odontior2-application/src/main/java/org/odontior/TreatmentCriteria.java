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
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;

public class TreatmentCriteria extends CriteriaPanel {
	public TreatmentCriteria() {

		setOrderByExpr("descr");

		JotyTextField symbol = Factory.createText(this, "symbol", "symbol", 8);
		symbol.setBounds(6, 21, 76, 20);
		add(symbol);

		JLabel lblSymbol = new JotyLabel(appLang("Symbol"));
		lblSymbol.setBounds(6, 7, 76, 14);
		add(lblSymbol);

		JotyTextField description = Factory.createText(this, "description", "descr", 48);
		description.setBounds(86, 21, 233, 20);
		add(description);

		JLabel lblDescription = new JotyLabel(appLang("Description"));
		lblDescription.setBounds(86, 7, 233, 14);
		add(lblDescription);

		ComboBox comboBox = Factory.createComboBox(this, "myCombo", "type", false, "treatmentTypes");
		comboBox.setBounds(169, 52, 151, 20);
		add(comboBox);

		JLabel lblType = new JotyLabel(appLang("Type"));
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(63, 53, 103, 14);
		add(lblType);
	}
}
