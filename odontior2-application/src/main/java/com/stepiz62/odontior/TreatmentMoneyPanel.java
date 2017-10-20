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

package com.stepiz62.odontior;

import java.beans.Beans;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.SearcherMultiPanelDialog;

public class TreatmentMoneyPanel extends DataAccessPanel {

	public TreatmentMoneyPanel() {
		if (!Beans.isDesignTime()) {
			Vector<DataAccessPanel> panels = ((SearcherMultiPanelDialog) injectedDialog()).m_dataPanels;
			if (panels.size() > 0)
				setTargetPanel(panels.get(0));
		}

		JLabel lblDefaultPrice = new JotyLabel(appLang("DefaultPrice"));
		lblDefaultPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDefaultPrice.setBounds(8, 14, 95, 14);
		add(lblDefaultPrice);

		JLabel lblMaxPrice = new JotyLabel(appLang("MaxPrice"));
		lblMaxPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxPrice.setBounds(205, 14, 95, 14);
		add(lblMaxPrice);

		JotyTextField dflt_price = Factory.createMoney(this, "dflt_price", "dflt_price");
		dflt_price.setBounds(104, 11, 82, 20);
		add(dflt_price);
		if (!Beans.isDesignTime() && getTargetPanel() != null) {
			getTargetPanel().term("dflt_price").setMandatory();
			getTargetPanel().term("dflt_price").defaultValue().setValue(0d);

		}

		JotyTextField max_price = Factory.createMoney(this, "max_price", "max_price");
		max_price.setBounds(303, 11, 82, 20);
		add(max_price);

		enableRole("Administrative personnel", Permission.readWrite);
	}
}
