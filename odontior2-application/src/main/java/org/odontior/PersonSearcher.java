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

import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.SearcherDialog;

public class PersonSearcher extends SearcherDialog {

	public enum SearchWhat {
		patients, customers
	}

	public PersonSearcher(Object openingMode) {
		super(null, openingMode);
		m_btnClose.setSize(100, 23);
		m_searcherPanel.m_btnSelect.setSize(111, 23);

		m_accessorMode = true;

		m_btnClose.setLocation(202, 8);
		m_buttonPane.setBounds(11, 337, 304, 40);
		m_contentPanel.setBounds(11, 324, 304, 10);
		m_searcherPanel.m_btnSelect.setLocation(201, 70);
		m_searcherPanel.m_table.setBounds(10, 110, 304, 208);
		m_searcherPanel.m_lblSearchResult.setBounds(10, 97, 179, 14);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 21, 181, 72);
		m_searcherPanel.m_btnPrevious.setBounds(201, 21, 111, 23);
		m_searcherPanel.m_defaultButton.setBounds(335, 28, 63, 23);
		m_searcherPanel.m_lblCriteria.setBounds(10, 4, 159, 14);
		m_searcherPanel.m_btnFind.setBounds(201, 42, 111, 23);
		m_searcherPanel.setBounds(1, 0, 325, 322);
		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new PersonCriteria(getMode() == SearchWhat.patients));
		criteriaPanel.setBounds(1, 1, 182, 70);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);
		getContentPane().setLayout(null);
		addLongKeyElem("id");
		setBounds(10, 11, 330, 407);
	}

	@Override
	protected boolean initDialog() {
		setTitle(getTitle() + " - " + (getMode() == SearchWhat.patients ? "Patiens" : "Customers"));
		return super.initDialog();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("PersonSearcher");
	}

}
