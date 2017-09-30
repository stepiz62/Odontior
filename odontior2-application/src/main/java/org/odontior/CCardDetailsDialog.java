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

public class CCardDetailsDialog extends DataAccessDialog {

	public CCardDetailsDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode);

		m_accessorMode = true;

		m_btnClose.setLocation(691, 5);
		m_btnHome.setLocation(657, 5);
		m_btnSelect.setBounds(558, 339, 28, 28);
		m_buttonPane.setSize(729, 37);
		m_contentPanel.setBounds(2, 2, 729, 572);
		m_buttonPane.setLocation(2, 575);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new CCardDetailsPanel());
		dataAccessPanel.setBounds(1, 1, 727, 569);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
		setBounds(2, 2, 739, 641);
		m_isViewer = true;
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("ClinicalCard");
		setRecordEntity(appLang("Context"));
	}
}
