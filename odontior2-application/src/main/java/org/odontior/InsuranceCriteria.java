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

import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;

public class InsuranceCriteria extends CriteriaPanel {
	InsuranceCriteria() {
		setOrderByExpr("Company");

		JotyTextField company = Factory.createText(this, "Company", "Company", 48);
		company.setBounds(80, 4, 176, 20);
		add(company);

		JLabel lblCompany = new JotyLabel(appLang("Company"));
		lblCompany.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompany.setBounds(0, 7, 77, 14);
		add(lblCompany);

		JotyTextField group = Factory.createText(this, "Group", "group_", 32);
		group.setBounds(329, 4, 131, 20);
		add(group);

		JLabel lblGroup = new JotyLabel(appLang("Group"));
		lblGroup.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroup.setBounds(263, 8, 64, 14);
		add(lblGroup);
	}

}
