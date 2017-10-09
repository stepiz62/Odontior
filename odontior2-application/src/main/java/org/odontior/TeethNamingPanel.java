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

import javax.swing.SwingConstants;

import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.NavigatorEditorPanel;
import org.joty.workstation.gui.Table;

public class TeethNamingPanel extends NavigatorEditorPanel {
	GraphCardPanel m_cardGraphPanel;

	TeethNamingPanel() {

		m_cardGraphPanel = new GraphCardPanel(this);
		m_cardGraphPanel.setLocation(2, 3);
		m_cardGraphPanel.setSize(260, 431);
		add(m_cardGraphPanel);

		m_table.setBounds(267, 2, 100, 431);
		Factory.addTextToGrid(this, "Symbol", appLang("Symbol"));
		setExistenceMonitorKeyField("id");
		gridCellDescriptor("Symbol").setEditable();

		addIntegerKeyElem("ID", true, true);

		enableRole("Administrators", m_common.m_shared ? Permission.read : Permission.all);
	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (retVal)
			m_cardGraphPanel.init(true);
		return retVal;
	}

	@Override
	protected void setGridFormat(Table table) {
		table.setColAlignement(0, SwingConstants.CENTER);
		table.m_sortClickDenied = true;
	}

}
