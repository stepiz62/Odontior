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

public class InvoiceDialog extends SearcherMultiPanelDialog {

	public InvoiceDialog() {
		super(null, null, true);

		m_accessorMode = true;

		m_btnClose.setLocation(555, 5);
		m_btnHome.setLocation(521, 5);
		m_btnDelete.setLocation(208, 5);
		m_btnNew.setLocation(21, 5);
		m_buttonPane.setSize(602, 40);
		m_btnSelect.setLocation(530, 593);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 24, 473, 45);
		m_searcherPanel.m_lblSearchResult.setBounds(10, 71, 234, 14);
		m_searcherPanel.m_btnPrevious.setBounds(493, 24, 97, 23);
		m_searcherPanel.m_defaultButton.setBounds(500, 46, 63, 23);
		m_searcherPanel.m_lblCriteria.setBounds(10, 7, 93, 14);
		m_searcherPanel.m_btnFind.setBounds(493, 45, 97, 23);
		m_searcherPanel.m_table.setBounds(10, 86, 579, 82);
		m_searcherPanel.setLayout(null);
		m_tabbedPane.setBounds(5, 6, 592, 361);
		m_contentPanel.setSize(602, 375);
		m_buttonPane.setLocation(6, 586);
		m_contentPanel.setLocation(6, 210);
		m_lblSearch.setLocation(6, 1);
		m_lblManage.setLocation(6, 195);
		m_searcherPanel.setBounds(7, 14, 601, 176);
		m_searcherPanel.m_btnSelect.setBounds(493, 49, 97, 23);
		getContentPane().setLayout(null);

		m_isViewer = true;

		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new InvoiceCriteria(instantiatedForEstimating()));
		criteriaPanel.setBounds(1, 1, 447, 43);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);

		setBounds(87, 129, 621, 655);

		InvoicePanel invoicePanel = new InvoicePanel();

		DataAccessPanel dataAccessPanel = Factory.addTab(m_tabbedPane, invoicePanel);
		m_tabbedPane.addTab(appLang("MainData"), null, dataAccessPanel, null);
		getContentPane().setLayout(null);

		addLongKeyElem("ID");
	}

	public boolean instantiatedForEstimating() {
		return false;
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Invoices");
	}
}
