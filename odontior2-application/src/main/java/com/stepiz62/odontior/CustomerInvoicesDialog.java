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

import javax.swing.JLabel;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.Table;

public class CustomerInvoicesDialog extends DataAccessDialog {
	public enum QueryType {
		normal, forPatient
	}

	public CustomerInvoicesDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode);

		m_accessorMode = true;

		m_btnSave.setLocation(326, 5);
		m_btnClose.setLocation(741, 5);
		m_btnHome.setLocation(707, 5);
		m_btnNext.setLocation(546, 5);
		m_btnPrevious.setLocation(517, 5);
		m_btnDelete.setLocation(432, 5);
		m_btnCancel.setLocation(360, 5);
		m_btnEditOrNew.setLocation(326, 5);
		m_btnNew.setLocation(242, 5);
		m_btnSelect.setLocation(601, 478);
		m_buttonPane.setBounds(1, 477, 792, 41);
		m_contentPanel.setBounds(207, 4, 585, 473);

		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new CustomerInvoicesPanel());
		dataAccessPanel.setBounds(1, 1, 560, 447);
		m_contentPanel.add(dataAccessPanel);
		setBounds(87, 129, 799, 544);
		getContentPane().setLayout(null);

		Table table = new Table(dataAccessPanel, null);
		table.setBounds(4, 24, 200, 450);
		getContentPane().add(table);
		((DataScrollingPanel) dataAccessPanel).setController(table);

		JLabel lblList = new JotyLabel(appLang("List"));
		lblList.setBounds(6, 9, 106, 14);
		getContentPane().add(lblList);
		m_isViewer = getMode() == QueryType.forPatient;
	}

	public boolean instantiatedForEstimating() {
		return false;
	}

	public boolean isOpenNormal() {
		return getMode() == QueryType.normal;
	}

	@Override
	protected void setEntityName() {
		m_entityName = getMode() == QueryType.normal ? appLang("CustomersInvoices") : appLang("PatientsInvoices");
	}

}
