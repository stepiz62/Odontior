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

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.Table;

public class ClinicalCardDialog extends DataAccessDialog {
	public enum QueryType {
		normal, specific
	}

	public ClinicalCardDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode);

		m_accessorMode = true;

		m_btnHome.setLocation(264, 4);
		m_btnSelect.setBounds(558, 339, 28, 28);
		m_defaultButton.setLocation(-852, -673);
		m_btnSave.setLocation(64, 4);
		m_btnDelete.setLocation(140, 4);
		m_btnCancel.setLocation(98, 4);
		m_btnEditOrNew.setLocation(64, 4);
		m_btnNew.setLocation(12, 4);
		m_buttonPane.setSize(333, 37);
		m_btnClose.setLocation(295, 4);
		m_btnNext.setLocation(225, 4);
		m_btnPrevious.setLocation(196, 4);
		m_buttonPane.setLocation(86, 281);
		m_contentPanel.setBounds(85, 1, 333, 279);

		DataAccessPanel dataPanel = Factory.addDataAccessPanel(m_contentPanel, new ClinicalCardPanel());
		dataPanel.setBounds(21, 1, 351, 277);
		m_contentPanel.add(dataPanel);
		getContentPane().setLayout(null);

		Table table = new Table(dataPanel, null);
		table.setBounds(4, 19, 76, 298);
		getContentPane().add(table);
		((DataScrollingPanel) dataPanel).setController(table);

		JLabel lblNavigator = new JotyLabel(appLang("Navigator"));
		lblNavigator.setBounds(5, 2, 70, 14);
		getContentPane().add(lblNavigator);

		setBounds(10, 19, 428, 346);

	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("ClinicalCards");
	}

}
