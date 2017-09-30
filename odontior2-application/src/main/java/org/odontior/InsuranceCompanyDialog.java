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
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.SearcherMultiPanelDialog;

public class InsuranceCompanyDialog extends SearcherMultiPanelDialog {

	public InsuranceCompanyDialog() {
		this(null, null);
	}

	public InsuranceCompanyDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode, true);

		m_accessorMode = true;

		m_btnDelete.setLocation(211, 5);
		m_btnNew.setLocation(10, 5);
		m_btnClose.setLocation(573, 5);
		m_btnHome.setLocation(539, 5);
		m_buttonPane.setSize(611, 40);
		m_btnSelect.setLocation(550, 559);
		m_searcherPanel.m_table.setSize(588, 82);
		m_searcherPanel.m_btnFind.setLocation(534, 32);
		m_lblManage.setSize(89, 14);
		m_lblManage.setLocation(5, 197);
		m_searcherPanel.setBounds(5, 14, 611, 177);
		m_searcherPanel.m_table.setLocation(10, 84);
		m_searcherPanel.m_lblSearchResult.setBounds(10, 70, 130, 14);
		m_searcherPanel.m_lblCriteria.setLocation(10, 8);
		m_lblSearch.setBounds(5, 1, 85, 14);
		m_searcherPanel.m_btnPrevious.setLocation(534, 6);
		m_searcherPanel.m_btnSelect.setLocation(534, 38);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 24, 478, 36);
		m_btnSearcherExpand.setLocation(8, 578);
		m_btnSearcherExpand.setSize(56, 14);
		m_tabbedPane.setBounds(6, 6, 600, 330);
		m_contentPanel.setBounds(5, 213, 611, 341);
		m_buttonPane.setLocation(5, 554);

		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new InsuranceCriteria());
		criteriaPanel.setBounds(1, 1, 474, 34);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);
		m_searcherPanel.setLayout(null);

		DataAccessPanel dataAccessPanel = Factory.addTab(m_tabbedPane, new InsuranceCompanyPanel(false));
		m_tabbedPane.addTab(appLang("Company"), null, dataAccessPanel, null);
		getContentPane().setLayout(null);
		setBounds(87, 129, 628, 621);

		addLongKeyElem("ID");
	}

	@Override
	protected void reloadAsLiteralStruct() {
		((OdontiorApp) m_app).loadInsCompaniesInLiteralStruct();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("InsuranceCompanies");
	}

}
