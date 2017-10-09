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
import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.TermContainerPanel;

public class PersonCriteria extends CriteriaPanel {

	public PersonCriteria(boolean patient) {

		setOrderByExpr("lastName");

		JotyTextField textField = Factory.createText(this, "lastName", "lastName", 24);
		textField.setBounds(79, 11, 92, 20);
		add(textField);

		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setBounds(0, 14, 77, 14);
		add(lblLastName);

		JotyTextField textField_1 = Factory.createText(this, "firstName", "firstName", 24);
		textField_1.setBounds(79, 41, 92, 20);
		add(textField_1);

		JLabel lblFirstName = new JotyLabel(appLang("FirstName"));
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirstName.setBounds(0, 44, 77, 14);
		add(lblFirstName);
	}

	@Override
	public void defineGrid(TermContainerPanel driverPanel) {
		driverPanel.addTermToGrid("lastName", appLang("LastName"));
		driverPanel.addTermToGrid("firstName", appLang("FirstName"));
	}
}
