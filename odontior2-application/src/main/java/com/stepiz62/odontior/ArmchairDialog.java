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

public class ArmchairDialog extends DataAccessDialog {
	public ArmchairDialog() {
		super(null, null);

		m_accessorMode = true;

		m_buttonPane.setBounds(2, 179, 636, 37);
		m_contentPanel.setBounds(2, 2, 636, 176);
		m_btnHome.setLocation(568, 6);
		m_btnPrevious.setLocation(276, 6);
		m_btnNext.setLocation(305, 6);
		m_btnSave.setLocation(83, 5);
		m_btnDelete.setLocation(155, 5);
		m_btnEditOrNew.setLocation(83, 5);
		m_btnNew.setLocation(3, 6);
		m_btnClose.setLocation(602, 6);
		m_btnCancel.setLocation(117, 5);
		setBounds(12, 57, 644, 243);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new ArmchairPanel());
		dataAccessPanel.setBounds(1, 1, 280, 332);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Armchairs");
	}
}
