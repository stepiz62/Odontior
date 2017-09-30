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

import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;

public class PractitionerDialog extends DataAccessDialog {
	public PractitionerDialog() {
		super(null, null);

		m_accessorMode = true;

		m_buttonPane.setBounds(2, 339, 289, 37);
		m_contentPanel.setBounds(2, 2, 289, 334);
		m_btnHome.setLocation(217, 6);
		m_btnPrevious.setLocation(154, 6);
		m_btnNext.setLocation(183, 6);
		m_btnSave.setLocation(42, 6);
		m_btnDelete.setLocation(115, 6);
		m_btnEditOrNew.setLocation(43, 6);
		m_btnNew.setLocation(3, 6);
		m_btnClose.setLocation(256, 6);
		m_btnCancel.setLocation(77, 6);
		setBounds(12, 57, 299, 406);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new PractitionerPanel());
		dataAccessPanel.setBounds(1, 1, 280, 332);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
	}

	@Override
	protected void reloadAsLiteralStruct() {
		((OdontiorApp) m_app).loadPractitionersInLiteralStruct();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Practitioners");
	}

}
