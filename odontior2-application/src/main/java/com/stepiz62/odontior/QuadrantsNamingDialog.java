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

public class QuadrantsNamingDialog extends DataAccessDialog {

	public QuadrantsNamingDialog() {
		super(null, null);
		m_btnDelete.setLocation(104, 5);
		m_btnCancel.setLocation(86, 5);
		m_btnSave.setLocation(60, 5);
		m_btnEditOrNew.setLocation(48, 5);
		m_btnNew.setLocation(10, 5);
		m_btnPrevious.setLocation(104, 5);
		m_btnNext.setLocation(104, 5);
		m_btnHome.setLocation(104, 5);
		m_btnClose.setLocation(192, 5);

		m_accessorMode = true;

		m_btnSelect.setBounds(558, 335, 28, 28);
		m_buttonPane.setBounds(2, 126, 230, 37);
		m_contentPanel.setBounds(2, 2, 230, 121);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new QuadrantsNamingPanel());
		dataAccessPanel.setBounds(1, 1, 228, 120);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
		setBounds(87, 129, 240, 193);

	}

	@Override
	protected void afterPerformed() {
		if (m_app.m_common.m_shared)
			Application.warningMsg(getContentPane(), jotyLang("NotEditableWhenShared"), m_app.m_name);
	}

	@Override
	public boolean close() {
		boolean retVal = super.close();
		TeethNamingDialog teethDialog = (TeethNamingDialog) m_app.getOpenedDialog("org.odontior.TeethNamingDialog", true);
		if (teethDialog != null)
			teethDialog.m_currSheet.init();
		return retVal;
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("NameQuadrants");
	}

}
