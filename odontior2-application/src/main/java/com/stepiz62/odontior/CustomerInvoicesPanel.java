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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.app.JotyException;
import org.joty.data.JotyDate;
import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.GridTerm;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.Term;
import org.joty.workstation.gui.GridTerm.SelectionHandler;

public class CustomerInvoicesPanel extends DataScrollingPanel {
	InvoiceDataPanel m_invoiceDataPanel;
	public TableTerm m_items;
	HashSet<Long> m_insertsIdSet;
	HashSet<Long> m_deletesIdSet;
	boolean m_customerSpecific;
	boolean m_asEstimate;
	private JButton btnIntervDetails;
	HashMap<Long, Double> m_reimbursementMap;

	public CustomerInvoicesPanel() {
		super(true);
		m_insertsIdSet = new HashSet<Long>();
		m_deletesIdSet = new HashSet<Long>();
		m_reimbursementMap = new HashMap<Long, Double>();
		m_customerSpecific = Beans.isDesignTime() ? false : ((CustomerInvoicesDialog) m_dialog).isOpenNormal();
		m_asEstimate = Beans.isDesignTime() ? false : ((CustomerInvoicesDialog) m_dialog).instantiatedForEstimating();

		m_invoiceDataPanel = new InvoiceDataPanel(this, Beans.isDesignTime() ? false : m_customerSpecific, Beans.isDesignTime() ? false : m_asEstimate);

		m_invoiceDataPanel.setBounds(3, 11, 569, 322);
		add(m_invoiceDataPanel);
		m_invoiceDataPanel.setLayout(null);

		term("Amount").setReadOnly(true);

		if (!m_customerSpecific)
			Factory.createNum(this, "isThePatient", "isThePatient");

		Table availItems = Factory.createTable(this, "availItems");
		availItems.setBounds(6, 360, 565, 95);
		add(availItems);
		m_app.addToolTipRowToComponent(availItems, m_app.m_common.appLang("DoubleClkToAdd"));

		m_items = tableTerm("availItems");
		m_items.setKeyName("id");
		m_items.addField("treatment", appLang("Treatment"));
		m_items.addField("tooth", appLang("ContextTooth"));
		m_items.addField("execution", appLang("ExecutionTime"));
		m_items.addFieldAsCurrency("price", appLang("Price"));
		m_items.addFieldToBuffer("treatment_id");
		availItems.m_colWidths = new int[] { 200, 15, 50, 15 };
		availItems.setColAlignement(1, SwingConstants.CENTER);
		availItems.setColAlignement(2, SwingConstants.CENTER);
		m_items.m_actionOnRowHandler = new ActionOnRowInterface() {
			@Override
			public void doAction(Term srcTerm, int column) {
				m_items.setDirty();
				processAction(true);
			}
		};
		m_items.setAsDataComplement();
		m_items.m_selectionHandler = new SelectionHandler() {
			@Override
			public void selChange(GridTerm term) {
				gridModifiedSessionEnabling(true);
			}
		};
		m_items.subscribe("InterventionDialog");

		JLabel lblAvailable = new JotyLabel(appLang("AvailableItemsInv"));
		lblAvailable.setBounds(8, 345, 366, 14);
		add(lblAvailable);

		m_currentButtonBehavior = ButtonBehavior.editing;

		btnIntervDetails = new JotyButton(appLang("InterventionDetails"));
		btnIntervDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContextParam("interventionID", tableTerm("availItems").m_dataBuffer.integerValue("id"));
				DataAccessDialog.tryCreate("InterventionDialog", callContext(), InterventionDialog.QueryType.interventionSpecific);
			}
		});
		btnIntervDetails.setBounds(421, 338, 150, 20);
		add(btnIntervDetails);
		btnIntervDetails.setToolTipText(appLang("AvailableUntilNoexch"));
		addTermToGrid("date", appLang("Date"));
		if (m_asEstimate)
			addTermToGrid("amount", appLang("Cost"));
		else
			addTermToGrid("number", appLang("Number"));

		enableRole("Administrative personnel", Permission.all);
	}

	@Override
	public void beginEditing() {
		m_insertsIdSet.clear();
		m_deletesIdSet.clear();
		super.beginEditing();
	}

	public void calcAmount() {
		Double amount;
		JotyDataBuffer buffer = m_invoiceDataPanel.m_items.m_dataBuffer;
		Double taxRate = term("taxRate").doubleVal() / 100;
		Double taxRatedExtra = term("TaxRatedExtra").doubleVal();
		Double nonTaxRatedExtra = term("nTaxRatedExtra").doubleVal();

		Double ins_alreadyspent = 0d, ins_deductible = 0d, ins_maxamount = 0d, ins_Percentage = 0d, ins_reimbursement; 
		boolean insuranceExists = false;
		if (m_asEstimate) {
			insuranceExists = bufferedComboTerm("insurance").getSelection() >= 0;
			if (insuranceExists) {
				ins_alreadyspent = term("alreadyspent").doubleVal();
				JotyDataBuffer insuranceBuffer = bufferedComboTerm("insurance").buffer();
				ins_deductible = insuranceBuffer.dblValue("deductible");
				ins_maxamount = insuranceBuffer.dblValue("maxamount");
				ins_Percentage = insuranceBuffer.dblValue("percentage");
				insuranceBuffer.dateValue("benefitperiod_start");
				insuranceBuffer.dateValue("benefitperiod_end");
			}
		}
		amount = 0d;
		ins_reimbursement = 0d;
		Double contribute;
		for (int i = 0; i < m_invoiceDataPanel.m_items.getRowQty(); i++) {
			contribute = buffer.dblValue("price", i) * (1 + taxRate);
			amount += contribute;
			if (m_asEstimate)
				ins_reimbursement += contribute * buffer.dblValue("percentage", i) / 100;
		}
		amount += taxRatedExtra * (1 + taxRate) + nonTaxRatedExtra;
		if (m_asEstimate && insuranceExists) {
			if (ins_Percentage > 0)
				ins_reimbursement += amount * ins_Percentage / 100;
			ins_reimbursement -= (ins_deductible <= ins_alreadyspent ? 0 : (ins_deductible - ins_alreadyspent));
			ins_reimbursement = ins_reimbursement < 0 ? 0 : ins_reimbursement;
			if (ins_maxamount > 0)
				ins_reimbursement = ((ins_alreadyspent + ins_reimbursement) > ins_maxamount) ? (ins_maxamount - ins_alreadyspent) : ins_reimbursement;
			amount -= ins_reimbursement;
		}
		term("Amount").setToVal(amount);
	}

	@Override
	public void componentsKillFocus(Term term) {
		super.componentsKillFocus(term);
		switch (term.m_name) {
			case "taxrate":
			case "taxratedextra":
			case "ntaxratedextra":
				if (term.isDirty())
					calcAmount();
		}
	}

	@Override
	public boolean doDeletion() {
		setContextParam("entityIdExpr", entityIdExpr(true));
		boolean retVal = m_app.executeSQL("DeleteInvoiceItems", null, createContextPostStatement());
		return retVal && super.doDeletion();
	}

	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		gridModifiedSessionEnabling(bState);
		m_invoiceDataPanel.m_btnPrint.setEnabled(!bState && documentIdentified());
	}

	private boolean executeStatements(HashSet<Long> idsSet, boolean adding) {
		boolean retVal = true;
		String stetementLiteral = adding ? "InsertInvoiceItem" : "DeleteInvoiceItem";
		setContextParam("entityIdExpr", entityIdExpr(true));
		for (Long id : idsSet) {
			setContextParam("idsSetId", id);
			if (!m_app.executeSQL(stetementLiteral, null, createContextPostStatement())) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	protected void gridModifiedSessionEnabling(boolean overallEnabling) {
		btnIntervDetails.setEnabled(overallEnabling && m_items.getSelection() >= 0 && !m_items.isDirty());
		if (m_asEstimate)
			bufferedComboTerm("insurance").getComponent().setEnabled(overallEnabling && !m_items.isDirty());
	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (m_customerSpecific)
			viewPatient(contextParamLong("isThePatient") == 1);
		return retVal;
	}

	public void loadReimbursementMap(long reimbursementID) {
		m_reimbursementMap.clear();
		WResultSet rs = m_app.openAccessorWResultSet("LoadReimbursement", this);
		if (rs != null) {
			while (!rs.isEOF()) {
				m_reimbursementMap.put(rs.integerValue("treatment_id"), rs.doubleValue("percentage"));
				rs.next();
			}
			rs.close();
		}

	}

	public void processAction(boolean adding) {
		TableTerm srcTerm = adding ? m_items : m_invoiceDataPanel.m_items;
		TableTerm destTerm = adding ? m_invoiceDataPanel.m_items : m_items;
		HashSet<Long> commitSet = adding ? m_insertsIdSet : m_deletesIdSet;
		HashSet<Long> rollbackSet = adding ? m_deletesIdSet : m_insertsIdSet;
		long id = srcTerm.m_dataBuffer.integerValue("ID");
		if (id > 0)
			commitSet.add(id);
		else
			rollbackSet.remove(-id);
		Table srcTable = ((Table) srcTerm.getComponent());
		Table destTable = ((Table) destTerm.getComponent());
		if (srcTerm.m_dataBuffer.m_cursorPos >= 0) {
			srcTable.m_changeEventsEnabled = false;
			destTable.m_changeEventsEnabled = false;
			destTerm.newRow();
			destTerm.m_dataBuffer.copyFrom(srcTerm.m_dataBuffer);
			if (adding && m_asEstimate) {
				Double reimbursementPerc = m_reimbursementMap.get(srcTerm.m_dataBuffer.integerValue("treatment_id"));
				destTerm.m_dataBuffer.setValue("percentage", reimbursementPerc == null ? 0 : reimbursementPerc);
			}
			destTerm.m_dataBuffer.setInteger("id", -id);
			srcTerm.m_dataBuffer.deleteRecord();
			srcTable.newDataAvailable();
			destTable.newDataAvailable();
			srcTable.m_changeEventsEnabled = true;
			destTable.m_changeEventsEnabled = true;
			calcAmount();
			gridModifiedSessionEnabling(true);
		}
	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("id", integerKeyElemVal("ID"));
	}

	public boolean setInvoiceNumber(Long invoiceNumber, JotyDate date) {
		return invokeAccessMethod(((OdontiorApp) m_app).invoiceNumberPostStatement(invoiceNumber, date));
	}

	@Override
	protected void setRelatedFields(WResultSet rs) {
		rs.setIntegerValue("patientcustomer_id", contextParamLong("patientCustomerID"));
		super.setRelatedFields(rs);
	}

	@Override
	protected void statusChangeProc() {
		super.statusChangeProc();
		if (!m_customerSpecific)
			viewPatient(getGridManager().m_gridBuffer.integerValue("isThePatient") == 1);
	}

	@Override
	protected boolean storeData() throws JotyException {
		boolean retVal = true;
		if (m_isNewRec && !m_asEstimate) {
			setTermAsReturnedValue(term("number"));
			retVal = setInvoiceNumber((long) -1, (JotyDate) term("date").m_dateVal);
		}
		return retVal && super.storeData() && executeStatements(m_insertsIdSet, true) && executeStatements(m_deletesIdSet, false);
	}

	private void viewPatient(boolean isThePatient) {
		m_invoiceDataPanel.viewPatient(isThePatient ? null : (contextParameter("patientName")));
	}

}
