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

import java.beans.Beans;

import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;

import com.stepiz62.odontior.InvoiceDataPanel.CustomerIdentifier;

public class InvoicePanel extends DataScrollingPanel implements CustomerIdentifier {
	InvoiceDataPanel m_invoiceDataPanel;
	boolean m_asEstimate;

	public InvoicePanel() {
		m_asEstimate = Beans.isDesignTime() ? false : ((InvoiceDialog) m_dialog).instantiatedForEstimating();
		m_invoiceDataPanel = new InvoiceDataPanel(this, false, Beans.isDesignTime() ? false : m_asEstimate);
		m_invoiceDataPanel.setBounds(10, 8, 572, 317);
		add(m_invoiceDataPanel);
		m_invoiceDataPanel.setLayout(null);

		Factory.createNum(this, "isThePatient", "isThePatient");
		Factory.createText(this, "pat_firstname", "pat_firstname", 50);
		Factory.createText(this, "pat_lastname", "pat_lastname", 50);
		Factory.createHiddenLong(this, "customer_id");

		enableRole("Administrative personnel", Permission.all);

	}

	@Override
	public void defineGrid() {
		super.defineGrid();
		addTermToGrid("firstname", appLang("FirstName"));
		addTermToGrid("note", appLang("Note"));
	}

	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		if (m_invoiceDataPanel.customerDetailsButton() != null)
			m_invoiceDataPanel.customerDetailsButton().setEnabled(documentIdentified());
		m_invoiceDataPanel.m_btnPrint.setEnabled(!bState && documentIdentified());
	}

	@Override
	public String patientCustomerID() {
		return String.valueOf(getGridManager().m_gridBuffer.integerValue("patientcustomer_id"));
	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("id", integerKeyElemVal("ID"));
		if (m_asEstimate)
			setContextParam("reimbursementplan_id", getGridManager().m_gridBuffer.integerValue("reimbursementplan_id"));
	}

	@Override
	protected void statusChangeProc() {
		super.statusChangeProc();
		JotyDataBuffer buffer = getGridManager().m_gridBuffer;
		m_invoiceDataPanel.viewPatient(buffer.m_cursorPos == -1 || buffer.integerValue("isThePatient") == 1 ? null : (buffer.strValue("pat_firstname") + " " + buffer.strValue("pat_lastname")));
	}

}
