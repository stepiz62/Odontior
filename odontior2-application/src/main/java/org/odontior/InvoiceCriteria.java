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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TermContainerPanel;

public class InvoiceCriteria extends CriteriaPanel {

	boolean m_asEstimate;

	public InvoiceCriteria(boolean asEstimate) {
		m_asEstimate = asEstimate;
		setOrderByExpr("date");

		JotyTextField date = Factory.createDate(this, "date", "date");
		date.setBounds(212, 11, 70, 20);
		add(date);

		JLabel lblDate = new JotyLabel(appLang("Date"));
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(130, 14, 49, 14);
		add(lblDate);
		if (!m_asEstimate) {
			JotyTextField number = Factory.createNum(this, "number", "number", 5);
			number.setBounds(76, 11, 49, 20);
			add(number);

			JLabel lblNumber = new JotyLabel(appLang("Number"));
			lblNumber.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNumber.setBounds(0, 14, 75, 14);
			add(lblNumber);
		}
		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setBounds(280, 14, 73, 14);
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblLastName);

		JotyTextField textField_2 = Factory.createText(this, "lastName", "c.lastName", 24);
		textField_2.setBounds(355, 11, 97, 20);
		add(textField_2);

		JComboBox dateOperator = new JComboBox();
		dateOperator.setBounds(180, 11, 35, 20);
		add(dateOperator);
		addOperatorsComboToTerm(term("date"), dateOperator);
	}

	@Override
	public void defineGrid(TermContainerPanel driverPanel) {
		driverPanel.addTermToGrid("date", appLang("Date"));
		if (!m_asEstimate)
			driverPanel.addTermToGrid("number", appLang("Number"));
		driverPanel.addTermToGrid("lastname", appLang("LastName"));
		Table masterGridTable = ((Table) driverPanel.m_gridManager.getListComponent());
		masterGridTable.m_colWidths = m_asEstimate ? new int[] { 30, 50, 50, 150 } : new int[] { 30, 15, 50, 50, 150 };
		masterGridTable.setColAlignement(0, SwingConstants.CENTER);
		masterGridTable.setColAlignement(1, SwingConstants.CENTER);
	}
}
