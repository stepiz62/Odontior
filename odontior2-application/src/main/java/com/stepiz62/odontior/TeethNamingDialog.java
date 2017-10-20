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

import org.joty.workstation.app.Application;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;

public class TeethNamingDialog extends DataAccessDialog {

	public TeethNamingDialog() {
		super(null, null);
		m_btnPrevious.setLocation(230, 5);
		m_btnNext.setLocation(259, 5);
		m_btnHome.setLocation(297, 5);
		m_btnClose.setLocation(335, 5);

		m_accessorMode = true;

		m_btnSelect.setBounds(558, 335, 28, 28);
		m_buttonPane.setBounds(2, 443, 377, 37);
		m_contentPanel.setBounds(2, 2, 377, 441);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new TeethNamingPanel());
		dataAccessPanel.setBounds(1, 1, 372, 439);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
		setBounds(87, 129, 387, 511);

	}

	@Override
	protected void afterPerformed() {
		if (m_app.m_common.m_shared)
			Application.warningMsg(getContentPane(), jotyLang("NotEditableWhenShared"), m_app.m_name);
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("TeethNaming");
	}

}
