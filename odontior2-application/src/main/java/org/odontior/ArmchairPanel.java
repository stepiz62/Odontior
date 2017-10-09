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
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;

public class ArmchairPanel extends NavigatorPanel {
	public ArmchairPanel() {
		m_table.setBounds(82, 14, 533, 79);

		JotyTextField jotyTextField = Factory.createText(this, "name", "name", 32);
		jotyTextField.setBounds(82, 104, 176, 20);
		add(jotyTextField);
		term("name").setMandatory();

		JLabel label = new JotyLabel(appLang("Name"));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(4, 107, 74, 14);
		add(label);

		JotyTextField descrption = Factory.createText(this, "description", "description", 64);
		descrption.setBounds(82, 135, 533, 20);
		add(descrption);
		term("description").setMandatory();

		JLabel lblData = new JotyLabel(appLang("Description"));
		lblData.setHorizontalAlignment(SwingConstants.RIGHT);
		lblData.setBounds(4, 139, 74, 14);
		add(lblData);

		JLabel lblListOf = new JotyLabel(appLang("ListOf"));
		lblListOf.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListOf.setBounds(4, 14, 74, 14);
		add(lblListOf);

		JotySeparator separator = new JotySeparator();
		separator.setBounds(7, 99, 608, 8);
		add(separator);

		addIntegerKeyElem("ID", true, true);
		addTermToGrid("name", appLang("Name"));
		addTermToGrid("description", appLang("Description"));
		enableRole("Administrators", Permission.all);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 20, 300 };
	}
}
