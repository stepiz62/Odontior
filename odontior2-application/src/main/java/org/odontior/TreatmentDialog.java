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

public class TreatmentDialog extends SearcherMultiPanelDialog {
	public TreatmentDialog() {
		this(null, null);
		m_searcherPanel.m_btnSelect.setSize(113, 23);
		m_searcherPanel.m_btnFind.setSize(113, 23);
		m_searcherPanel.m_btnPrevious.setSize(113, 23);
	}

	public TreatmentDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode, true);

		m_accessorMode = true;

		m_btnSearcherExpand.setLocation(6, 668);
		m_btnSearcherExpand.setSize(56, 14);
		m_progressiveAction = true;
		m_searcherPanel.m_btnSelect.setLocation(343, 85);
		m_btnSave.setLocation(63, 5);
		m_btnNext.setLocation(283, 5);
		m_btnDelete.setLocation(135, 5);
		m_btnCancel.setLocation(97, 5);
		m_btnEditOrNew.setLocation(63, 5);
		m_btnNew.setLocation(10, 5);
		setBounds(10, 14, 482, 714);
		m_searcherPanel.m_lblCriteria.setLocation(11, 10);
		m_lblManage.setBounds(10, 524, 226, 14);
		m_lblSearch.setBounds(10, 1, 163, 14);
		m_buttonPane.setBounds(6, 645, 464, 37);
		m_contentPanel.setBounds(6, 538, 464, 104);
		m_btnClose.setLocation(427, 5);
		m_btnHome.setLocation(350, 5);
		m_btnPrevious.setLocation(194, 5);
		m_btnPrevious.setLocation(245, 5);
		m_tabbedPane.setBounds(4, 3, 457, 96);
		m_searcherPanel.setBounds(5, 14, 465, 505);
		m_searcherPanel.m_lblSearchResult.setLocation(12, 108);
		m_searcherPanel.m_table.setBounds(10, 122, 446, 375);
		m_searcherPanel.m_btnFind.setLocation(343, 51);
		m_searcherPanel.m_btnPrevious.setLocation(343, 24);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 24, 328, 81);

		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new TreatmentCriteria());
		criteriaPanel.setBounds(1, 1, 328, 79);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);

		getContentPane().setLayout(null);

		DataAccessPanel component = Factory.addTab(m_tabbedPane, new TreatmentPanel());
		m_tabbedPane.addTab(appLang("MainData"), null, component, null);

		DataAccessPanel component_1 = Factory.addTab(m_tabbedPane, new TreatmentMoneyPanel());
		m_tabbedPane.addTab(appLang("Price"), null, component_1, null);

		addLongKeyElem("ID");

	}

	@Override
	protected void reloadAsLiteralStruct() {
		((OdontiorApp) m_app).loadTreatmentsInLiteralStruct();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Treatments");
	}

}
