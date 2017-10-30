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

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.MultiPanelDialog;
import org.joty.workstation.gui.Table;

public class InterventionDialog extends MultiPanelDialog {

	public enum QueryType {
		contextSpecific, asSelector, interventionSpecific
	}

	public InterventionDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode);

		m_accessorMode = true;

		m_btnDelete.setLocation(197, 5);
		m_btnCancel.setLocation(126, 5);
		m_btnSelect.setSize(75, 28);
		m_btnSelect.setLocation(426, 438);
		m_tabbedPane.setBounds(4, 3, 496, 309);
		m_buttonPane.setSize(504, 37);
		m_btnClose.setLocation(457, 5);
		m_btnHome.setLocation(423, 5);
		m_btnNext.setLocation(303, 5);
		m_btnPrevious.setLocation(274, 5);
		m_contentPanel.setBounds(3, 116, 504, 317);

		DataAccessPanel dataAccessPanel = Factory.addTab(m_tabbedPane, new InterventionPanel(), null);
		m_tabbedPane.addTab(appLang("ClinicalData"), null, dataAccessPanel, null);

		Table table = new Table(dataAccessPanel, null);
		table.setBounds(3, 4, 504, 107);
		getContentPane().add(table);
		((DataScrollingPanel) dataAccessPanel).setController(table);

		DataAccessPanel dataAccessPanel2 = Factory.addTab(m_tabbedPane, new InterventionMoneyPanel(), null);
		m_tabbedPane.addTab(appLang("CommercialData"), null, dataAccessPanel2, null);

		m_buttonPane.setLocation(3, 435);
		getContentPane().setLayout(null);
		setBounds(10, 19, 519, 500);
	}

	public boolean isInterventionSpecific() {
		return getMode() == QueryType.interventionSpecific;
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Interventions");
	}

}
