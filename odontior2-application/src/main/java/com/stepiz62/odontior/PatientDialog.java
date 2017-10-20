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

import javax.swing.JComponent;

import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.SearcherMultiPanelDialog_V;

public class PatientDialog extends SearcherMultiPanelDialog_V {

	public PatientDialog() {
		super(null, null, true);
		m_searcherPanel.m_btnFind.setLocation(117, 45);
		m_searcherPanel.m_btnFind.setSize(111, 23);
		m_searcherPanel.m_btnSelect.setSize(111, 23);
		m_searcherPanel.m_btnPrevious.setBounds(117, 11, 111, 23);
		m_searcherPanel.m_lblSearchResult.setSize(217, 14);
		m_searcherPanel.m_lblCriteria.setBounds(12, 57, 143, 14);

		m_accessorMode = true;

		m_btnDelete.setLocation(208, 5);
		m_btnHome.setLocation(537, 5);
		m_btnClose.setLocation(571, 5);
		m_searcherPanel.m_lblSearchResult.setLocation(12, 160);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 75, 218, 74);
		m_searcherPanel.m_btnSelect.setLocation(117, 27);
		m_searcherPanel.m_table.setBounds(10, 174, 218, 318);
		m_btnSearcherExpand.setBounds(215, 3, 28, 12);
		m_lblManage.setBounds(258, 5, 101, 14);
		m_lblSearch.setBounds(10, 5, 195, 14);
		m_searcherPanel.setBounds(10, 23, 239, 501);
		m_btnSelect.setBounds(628, 490, 63, 23);
		m_buttonPane.setBounds(258, 484, 605, 40);
		m_contentPanel.setBounds(258, 23, 605, 459);
		m_tabbedPane.setBounds(6, 5, 593, 446);
		m_lblManage.setText(appLang("ManagePatient"));
		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new PersonCriteria(true));
		criteriaPanel.setBounds(1, 1, 216, 70);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);
		setBounds(12, 57, 873, 549);

		JComponent component = Factory.addTab(m_tabbedPane, new PatientPanel(false), null);
		m_tabbedPane.addTab(appLang("MainData"), null, component, null);

		DataAccessPanel dataAccessPanel = Factory.addTab(m_tabbedPane, new PatientCustomers(), null);
		m_tabbedPane.addTab(appLang("RelatedCustomers"), null, dataAccessPanel, null);

		DataAccessPanel dataAccessPanel_1 = Factory.addTab(m_tabbedPane, new ReservedNotes(), null);
		m_tabbedPane.addTab(appLang("ReservedNotes"), null, dataAccessPanel_1, null);
		getContentPane().setLayout(null);

		addLongKeyElem("ID");

	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Patients");
	}
}
