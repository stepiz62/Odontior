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

public class InsuranceDialog extends DataAccessDialog {

	public InsuranceDialog(Object callContext) {
		super(callContext, null);

		m_accessorMode = true;

		m_buttonPane.setSize(518, 37);
		m_btnClose.setLocation(480, 5);
		m_btnHome.setLocation(446, 5);
		m_btnNext.setLocation(251, 5);
		m_btnPrevious.setLocation(222, 5);
		m_contentPanel.setBounds(2, 2, 519, 429);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new InsurancePanel());
		dataAccessPanel.setBounds(1, 1, 539, 445);
		m_contentPanel.add(dataAccessPanel);
		m_buttonPane.setLocation(2, 432);
		getContentPane().setLayout(null);
		setBounds(10, 19, 528, 498);
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Insurances");
	}

}
