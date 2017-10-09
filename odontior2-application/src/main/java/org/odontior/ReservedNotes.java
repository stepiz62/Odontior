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

import org.joty.data.JotyDate;
import org.joty.workstation.gui.*;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TextArea;

public class ReservedNotes extends NavigatorPanel {
	public ReservedNotes() {
		m_table.setBounds(10, 11, 564, 109);

		TextArea text = Factory.createTextArea(this, "text", "text", 1024);
		text.m_jtextArea.setLocation(10, 0);
		text.m_jtextArea.setSize(582, 159);
		text.setBounds(10, 205, 564, 197);
		add(text);

		JLabel lblText = new JotyLabel(appLang("Text"));
		lblText.setBounds(10, 190, 141, 14);
		add(lblText);

		JotyTextField jotyTextField = Factory.createText(this, "subject", "subject", 32);
		jotyTextField.setBounds(113, 131, 461, 20);
		add(jotyTextField);
		term("subject").setMandatory();

		JotyTextField jotyTextField_1 = Factory.createDate(this, "date", "date");
		jotyTextField_1.setBounds(112, 162, 70, 20);
		add(jotyTextField_1);
		term("date").setMandatory();
		term("date").defaultValue().setValue(new JotyDate(m_app, "now"));

		JLabel lblSubject = new JotyLabel(appLang("Subject"));
		lblSubject.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubject.setBounds(10, 134, 98, 14);
		add(lblSubject);

		JLabel lblDate = new JotyLabel(appLang("Date"));
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(10, 165, 98, 14);
		add(lblDate);

		ComboBox practitioner = Factory.createComboBox(this, "practitioner", "practitioner_id", false, "practitioners");
		practitioner.setBounds(374, 162, 200, 20);
		add(practitioner);

		JLabel lblPractitioner = new JotyLabel(appLang("Practitioner"));
		lblPractitioner.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPractitioner.setBounds(242, 165, 127, 14);
		add(lblPractitioner);

		addTermToGrid("subject", appLang("Subject"));
		addTermToGrid("date", appLang("Date"));
		addTermToGrid("practitioner", appLang("Practitioner"));

		addIntegerKeyElem("id", true, true);
		defRelationElement("patient_id", "id");

		enableRole("Doctors", Permission.all);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 200, 15, 40 };
	}
}
