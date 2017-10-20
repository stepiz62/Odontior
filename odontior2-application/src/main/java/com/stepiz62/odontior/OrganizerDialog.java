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

public class OrganizerDialog extends DataAccessDialog {
	DataAccessPanel m_panel;

	public OrganizerDialog() {
		super(null, null);

		m_accessorMode = true;

		m_btnDelete.setLocation(192, 5);
		m_btnNext.setLocation(402, 5);
		m_btnPrevious.setLocation(373, 5);
		m_btnClose.setLocation(709, 5);
		m_btnHome.setLocation(675, 5);
		m_buttonPane.setSize(742, 37);
		m_buttonPane.setLocation(3, 451);
		m_contentPanel.setBounds(2, 2, 743, 447);
		m_panel = Factory.addDataAccessPanel(m_contentPanel, new OrganizerPanel());
		m_panel.setBounds(1, 1, 741, 448);
		setBounds(10, 11, 754, 518);
		m_contentPanel.add(m_panel);
		getContentPane().setLayout(null);
		m_isViewer = true;

	}

	@Override
	public boolean close() {
		((OrganizerPanel) m_panel).storePreferences();
		return super.close();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Organizer");
		setRecordEntity(appLang("Armchair"));
	}
}
