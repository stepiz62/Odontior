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

import javax.swing.JComponent;

import org.joty.workstation.gui.CriteriaPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.MultiPanelDialog;
import org.joty.workstation.gui.SearcherMultiPanelDialog_V;
import org.odontior.PersonSearcher.SearchWhat;

public class CustomerDialog extends SearcherMultiPanelDialog_V {

	public enum OpenMode {
		normal, patientCreatingAsCustomer
	}
	
	public CustomerDialog() {
		this(null, null);
	}
		
	public CustomerDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode, true);		
		m_searcherPanel.m_btnFind.setLocation(117, 45);
		m_searcherPanel.m_btnFind.setSize(111, 23);
		m_searcherPanel.m_btnSelect.setSize(111, 23);
		m_searcherPanel.m_btnPrevious.setBounds(117, 11, 111, 23);

		m_accessorMode = true;

		m_searcherPanel.m_btnSelect.setLocation(117, 29);
		m_searcherPanel.m_criteriaContainer.setBounds(10, 75, 218, 75);
		m_searcherPanel.m_lblSearchResult.setBounds(12, 162, 123, 14);
		m_searcherPanel.m_lblCriteria.setBounds(12, 57, 108, 14);
		m_btnDelete.setLocation(208, 5);
		m_btnClose.setLocation(573, 5);
		m_btnHome.setLocation(539, 5);
		m_searcherPanel.m_table.setBounds(10, 179, 218, 323);
		m_btnSearcherExpand.setBounds(215, 3, 28, 12);
		m_lblManage.setBounds(258, 5, 150, 14);
		m_lblSearch.setBounds(10, 5, 91, 14);
		m_searcherPanel.setBounds(10, 23, 239, 513);
		m_btnSelect.setBounds(636, 504, 63, 23);
		m_buttonPane.setBounds(258, 498, 605, 38);
		m_contentPanel.setBounds(258, 23, 607, 474);
		m_tabbedPane.setBounds(6, 3, 593, 464);
		m_lblManage.setText(appLang("ManageCustomer"));
		CriteriaPanel criteriaPanel = Factory.addCriteriaPanel(m_searcherPanel, new PersonCriteria(false));
		criteriaPanel.setBounds(1, 36, 231, 81);
		m_searcherPanel.m_criteriaContainer.add(criteriaPanel);
		setBounds(12, 57, 873, 565);

		JComponent component = Factory.addTab(m_tabbedPane, new CustomerPanel(false));
		m_tabbedPane.addTab("", null, component, null);
		getContentPane().setLayout(null);

		addLongKeyElem("ID");
		if(getMode() == OpenMode.patientCreatingAsCustomer) 
			setModal(true);
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Customers");
	}

	@Override
	protected boolean initDialog() {
		boolean retVal = super.initDialog();
		if(getMode() == OpenMode.patientCreatingAsCustomer) {
			onNew();
			m_currSheet.textTerm("lastName").setVal(((OdontiorApp) m_app).patientLastName());
			m_currSheet.textTerm("lastName").setDirty();
			m_currSheet.textTerm("firstName").setVal(((OdontiorApp) m_app).patientFirstName());
			m_currSheet.textTerm("firstName").setDirty();
			m_currSheet.guiDataExch(false);
		}
		return retVal;
	}

	@Override
	protected boolean save() {
		boolean retVal = super.save();
		if(retVal && getMode() == OpenMode.patientCreatingAsCustomer)  {
			m_app.m_justSelectedValue = m_currSheet.m_identifyingID;
			close();
		}
		return retVal;
	}

	@Override
	public void onCancel() {
		super.onCancel();
		if(getMode() == OpenMode.patientCreatingAsCustomer) 
			close();
	}
	
	
}
