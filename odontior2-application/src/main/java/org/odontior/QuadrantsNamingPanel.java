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

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.NavigatorEditorPanel;
import org.joty.workstation.gui.Table;

public class QuadrantsNamingPanel extends NavigatorEditorPanel {
	GraphCardPanel m_cardGraphPanel;

	QuadrantsNamingPanel() {

		Factory.m_DnDfeatures = false;

		JPanel tooth1 = Factory.addAnalogicalRowSelector(this, 1);
		tooth1.setBounds(24, 17, 31, 21);
		add(tooth1);

		JPanel tooth2 = Factory.addAnalogicalRowSelector(this, 2);
		tooth2.setBounds(65, 17, 31, 21);
		add(tooth2);

		JPanel tooth3 = Factory.addAnalogicalRowSelector(this, 3);
		tooth3.setBounds(65, 46, 31, 21);
		add(tooth3);

		JPanel tooth4 = Factory.addAnalogicalRowSelector(this, 4);
		tooth4.setBounds(24, 46, 31, 21);
		add(tooth4);

		m_table.setBounds(116, 11, 100, 92);
		Factory.addTextToGrid(this, "Label", appLang("Symbol"));
		setExistenceMonitorKeyField("id");
		gridCellDescriptor("Label").setEditable();

		addIntegerKeyElem("ID", true, true);

		enableRole("Administrators", m_common.m_shared ? Permission.read : Permission.all);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.setColAlignement(0, SwingConstants.CENTER);
		table.m_sortClickDenied = true;
	}

}
