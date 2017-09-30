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

import java.beans.Beans;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.MultiPanelDialog;

public class InterventionMoneyPanel extends DataAccessPanel {

	public InterventionMoneyPanel() {
		if (!Beans.isDesignTime()) {
			Vector<DataAccessPanel> panels = ((MultiPanelDialog) injectedDialog()).m_dataPanels;
			if (panels.size() > 0)
				setTargetPanel(panels.get(0));
		}

		JotyTextField price = Factory.createMoney(this, "price", "price");
		price.setBounds(136, 25, 82, 20);
		add(price);
		
		if (!Beans.isDesignTime() && getTargetPanel() != null)
			getTargetPanel().term("price").setMandatory();

		JLabel lblPrice = new JotyLabel(appLang("Price"));
		lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrice.setBounds(31, 28, 100, 14);
		add(lblPrice);

		enableRole("Administrative personnel", Permission.all);

	}
}
