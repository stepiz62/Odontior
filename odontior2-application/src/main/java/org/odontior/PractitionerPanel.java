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
import org.joty.workstation.gui.TextArea;

public class PractitionerPanel extends NavigatorPanel {
	public PractitionerPanel() {
		m_table.setBounds(66, 14, 208, 79);

		JotyTextField jotyTextField = Factory.createText(this, "name", "name", 32);
		jotyTextField.setBounds(10, 114, 264, 20);
		add(jotyTextField);
		term("name").setMandatory();

		JLabel label = new JotyLabel(appLang("Name"));
		label.setBounds(10, 100, 156, 14);
		add(label);

		TextArea textArea = Factory.createTextArea(this, "data", "data", 512);
		textArea.setBounds(10, 153, 264, 163);
		add(textArea);

		JLabel lblData = new JotyLabel(appLang("Data"));
		lblData.setBounds(10, 139, 170, 14);
		add(lblData);

		JLabel lblListOf = new JotyLabel(appLang("ListOf"));
		lblListOf.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListOf.setBounds(9, 14, 53, 14);
		add(lblListOf);

		addIntegerKeyElem("ID", true, true);
		addTermToGrid("name", appLang("Name"));

		enableRole("Administrators", Permission.all);
		enableRole("Administrative personnel", Permission.read);
		enableRole("Practitioners", Permission.read);
		enableRole("Doctors", Permission.read);
		enableRole("Schedulers", Permission.read);
	}
}
