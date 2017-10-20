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

import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;

public class ReimbursementPlanDialog extends DataAccessDialog {

	public ReimbursementPlanDialog(Object callContext) {
		super(callContext, null);

		m_accessorMode = true;

		m_btnSave.setLocation(68, 5);
		m_btnClose.setLocation(409, 5);
		m_btnHome.setLocation(375, 5);
		m_btnCancel.setLocation(102, 5);
		m_btnEditOrNew.setLocation(68, 5);
		m_btnNew.setLocation(9, 5);
		m_btnNext.setLocation(258, 5);
		m_btnPrevious.setLocation(229, 5);
		m_btnSelect.setBounds(480, 348, 28, 28);
		m_buttonPane.setBounds(2, 264, 455, 37);
		m_contentPanel.setBounds(2, 2, 455, 261);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new ReimbursementPlanPanel());
		dataAccessPanel.setBounds(1, 1, 450, 259);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
		setBounds(87, 129, 465, 330);
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("ReimbursementPlans");
	}
}
