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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Panel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.Term;
import org.joty.workstation.gui.TextArea;
import org.joty.workstation.gui.BufferedComboBoxTerm.BufferedComboBox;
import org.joty.workstation.gui.ComboBox.ActionPostExecutor;
import org.joty.workstation.gui.DataAccessPanel.ActionOnRowInterface;

public class InvoiceDataPanel extends Panel {
	public interface CustomerIdentifier {
		public String patientCustomerID();
	}

	public TableTerm m_items;
	public JButton m_btnPrint;
	private JLabel lblLabelPatient;
	private JLabel lblPatient;
	private JotyTextField txtAmount;
	private JotyTextField txtTaxRate;
	private JotyTextField txtTaxRatedExtraDescr;
	private JotyTextField txtNTaxRatedExtraDescr;
	private JotyTextField txtTaxRatedExtra;
	private JotyTextField txtNTaxRatedExtra;
	private JotyTextField alreadySpent;
	boolean m_forEstimating;
	DataScrollingPanel m_parent;
	JButton btnCustomerDetails;

	public InvoiceDataPanel() { // FOR wbe only
		this(new DataScrollingPanel(), false, false);
	}

	public InvoiceDataPanel(final DataScrollingPanel parent, boolean forEditing, boolean forEstimating) {
		m_parent = parent;
		m_forEstimating = forEstimating;

		JotyTextField date = Factory.createDate(parent, "date", "date");
		date.setBounds(197, 0, 69, 20);
		add(date);
		parent.term("date").setMandatory();

		JLabel lblDate = new JotyLabel(appLang("Date"));
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(131, 3, 64, 14);
		add(lblDate);

		if (!forEstimating) {
			JLabel lblNumber = new JotyLabel(appLang("Number"));
			lblNumber.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNumber.setBounds(5, 3, 60, 14);
			add(lblNumber);

			JotyTextField number = Factory.createLongNum(parent, "number", "number", 5);
			number.setBounds(70, 0, 60, 20);
			add(number);
			parent.setReadOnly("number");
		}

		JLabel lblFirstName = new JotyLabel(appLang("FirstName"));
		lblFirstName.setBounds(5, 48, 63, 14);
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblFirstName);

		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setBounds(165, 48, 63, 14);
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblLastName);

		JotyTextField firstName = Factory.createTextRenderer(parent, "firstName", "firstName", forEditing ? "firstName" : null, 50, true);
		firstName.setBounds(70, 45, 90, 20);
		add(firstName);
		JotyTextField lastName = Factory.createTextRenderer(parent, "lastName", "lastName", forEditing ? "lastName" : null, 50, true);
		lastName.setBounds(231, 45, 90, 20);
		add(lastName);

		JLabel lblBillTo = new JotyLabel(appLang("Customer"));
		lblBillTo.setBounds(5, 28, 115, 14);
		add(lblBillTo);

		JotySeparator separator = new JotySeparator();
		separator.setBounds(5, 42, 316, 4);
		add(separator);

		JLabel lblItems = new JotyLabel(appLang("Items"));
		lblItems.setBounds(5, 210, 149, 14);
		add(lblItems);

		txtAmount = Factory.createMoney(parent, "amount", "amount");
		txtAmount.setBounds(338, 0, 82, 20);
		add(txtAmount);

		txtTaxRate = Factory.createDecimal(parent, "taxrate", "taxrate", 4);
		txtTaxRate.setBounds(507, 0, 47, 20);
		add(txtTaxRate);
		parent.term("taxrate").setMandatory();

		JLabel lblTaxrate = new JotyLabel(appLang("TaxRate"));
		lblTaxrate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTaxrate.setBounds(423, 3, 79, 14);
		add(lblTaxrate);

		JLabel lblAmount = new JotyLabel(forEstimating ? appLang("Cost") : appLang("Amount"));
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(268, 3, 69, 14);
		add(lblAmount);

		TextArea textArea = Factory.createTextArea(parent, "note", "note", 1024);
		textArea.m_jtextArea.setLocation(5, 0);
		textArea.setBounds(3, 165, 565, 44);
		add(textArea);

		JLabel lblNote = new JotyLabel(appLang("Note"));
		lblNote.setBounds(5, 146, 90, 14);
		add(lblNote);

		Table itemsTable = Factory.createTable(parent, "items");
		itemsTable.setBounds(3, 227, 565, 86);
		add(itemsTable);
		m_items = parent.tableTerm("items");

		if (!parent.getDialog().isViewer())
			m_app.addToolTipRowToComponent(itemsTable, m_app.m_common.appLang("DoubleClkToRemove"));
		JLabel lblTaxRatedExtra = new JotyLabel(appLang("TaxRatedExtraAmount"));
		lblTaxRatedExtra.setBounds(331, 31, 160, 14);
		add(lblTaxRatedExtra);

		txtTaxRatedExtra = Factory.createMoney(parent, "taxratedextra", "taxratedextra");
		txtTaxRatedExtra.setBounds(472, 45, 82, 20);
		add(txtTaxRatedExtra);

		lblLabelPatient = new JotyLabel(appLang("Patient"));
		lblLabelPatient.setBounds(5, 70, 115, 14);
		add(lblLabelPatient);
		lblLabelPatient.setVisible(false);

		JLabel lblNonTaxRated = new JotyLabel(appLang("NonTaxRatedExtraAmount"));
		lblNonTaxRated.setBounds(331, 66, 160, 14);
		add(lblNonTaxRated);

		txtNTaxRatedExtra = Factory.createMoney(parent, "ntaxratedextra", "ntaxratedextra");
		txtNTaxRatedExtra.setBounds(472, 79, 82, 20);
		add(txtNTaxRatedExtra);

		txtTaxRatedExtraDescr = Factory.createText(parent, "tre_descr", "tre_descr", 50);
		txtTaxRatedExtraDescr.setBounds(331, 45, 137, 20);
		add(txtTaxRatedExtraDescr);

		txtNTaxRatedExtraDescr = Factory.createText(parent, "ntre_descr", "ntre_descr", 50);
		txtNTaxRatedExtraDescr.setBounds(331, 79, 137, 20);
		add(txtNTaxRatedExtraDescr);

		lblPatient = new JotyLabel("patient");
		lblPatient.setBounds(5, 86, 156, 14);
		add(lblPatient);

		if (!Beans.isDesignTime() && parent.getDialog().isViewer()) {
			btnCustomerDetails = new JotyButton(appLang("Details"));
			btnCustomerDetails.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					parent.setContextParam("customerId", parent.getGridManager().m_gridBuffer.integerValue("customer_id"));
					DataAccessDialog.tryCreate("CustomerDetailsDialog", parent.callContext());
				}
			});
			btnCustomerDetails.setBounds(249, 20, 70, 23);
			add(btnCustomerDetails);
		}

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setBounds(5, 84, 200, 4);
		add(separator_1);

		if (forEstimating) {
			JLabel lblInsurance = new JotyLabel(appLang("Insurance"));
			lblInsurance.setHorizontalAlignment(SwingConstants.RIGHT);
			lblInsurance.setBounds(15, 111, 100, 14);
			add(lblInsurance);

			JLabel lblAlreadySpent = new JotyLabel(appLang("AlreadySpent"));
			lblAlreadySpent.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAlreadySpent.setBounds(361, 111, 108, 14);
			add(lblAlreadySpent);

			alreadySpent = Factory.createMoney(parent, "alreadySpent", "alreadyspent");
			alreadySpent.setBounds(472, 107, 82, 20);
			add(alreadySpent);

			if (parent.getDialog().isViewer()) {
				JotyTextField txtInsurance = Factory.createText(parent, "txtInsurance", "insurance", 32);
				txtInsurance.setBounds(119, 107, 242, 22);
				add(txtInsurance);
				Factory.createHiddenLong(parent, "reimbursementplan_id");
			} else {
				BufferedComboBox insurance = Factory.createBufferedComboBox(parent, "insurance", "insurance_id");
				insurance.setActionPostExecutor(new ActionPostExecutor() {
					@Override
					public void doIt(ComboBox comboBox) {
						JotyDataBuffer buffer = m_parent.bufferedComboTerm("insurance").buffer();
						if (!getDialog().m_gridSelChanging || m_parent.m_initializing)
							if (comboBox.getSelectedIndex() == -1)
								m_parent.term("alreadySpent").clear();
							else
								m_parent.term("alreadySpent").setToVal(buffer.dblValue("alreadySpent"));
						long reimbursementID = buffer.integerValue("reimbursementplan_id");
						m_parent.setContextParam("reimbursementplan_id", reimbursementID);
						m_items.refresh();
						if (!getDialog().m_gridSelChanging || m_parent.m_initializing)
							if (m_parent instanceof CustomerInvoicesPanel) {
								CustomerInvoicesPanel parent = (CustomerInvoicesPanel) m_parent;
								parent.loadReimbursementMap(reimbursementID);
								parent.calcAmount();
							}
					}
				});
				parent.term("insurance").effectsPostPone(parent.term("alreadySpent"));
				parent.term("insurance").effectsPostPone(m_items);
				insurance.setBounds(119, 107, 242, 22);
				add(insurance);
				parent.bufferedComboTerm("insurance").config("id", "name", null);
			}
		}

		lblPatient.setVisible(false);
		m_items.setKeyName("id");
		m_items.addField("treatment", appLang("Treatment"));
		m_items.addField("tooth", appLang("ContextTooth"));
		m_items.addField("execution", appLang("ExecutionTime"));
		m_items.addFieldAsCurrency("price", appLang("Price"));
		itemsTable.m_colWidths = m_forEstimating ? new int[] { 200, 15, 50, 15, 15 } : new int[] { 200, 15, 50, 15 };

		m_btnPrint = new JotyButton(appLang("Print"));
		m_btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_app.m_reportManager.resetParams();
				m_app.m_reportManager.addParameter("InvoiceId", String.valueOf(m_parent.contextParamLong("ID")));
				m_app.m_reportManager.addParameter("loc_country", m_app.m_common.m_loc_country);
				m_app.m_reportManager.addParameter("loc_lang", m_app.m_common.m_loc_lang);
				m_app.launchReport("Invoice", null, true, InvoiceDataPanel.this.getDialog());
			}
		});
		m_btnPrint.setBounds(5, 111, 91, 23);
		add(m_btnPrint);

		JLabel label = new JLabel("%");
		label.setBounds(557, 5, 11, 14);
		add(label);
		itemsTable.setColAlignement(1, SwingConstants.CENTER);
		itemsTable.setColAlignement(2, SwingConstants.CENTER);

		if (m_forEstimating)
			m_items.addField("percentage", appLang("Reimbursement") + " (%)");

		if (parent instanceof CustomerInvoicesPanel)
			m_items.m_actionOnRowHandler = new ActionOnRowInterface() {
				@Override
				public void doAction(Term srcTerm, int column) {
					((CustomerInvoicesPanel) m_parent).processAction(false);
				}
		};
		parent.addIntegerKeyElem("ID", true, true);
		m_btnPrint.setVisible(!m_forEstimating);

	}

	public JButton customerDetailsButton() {
		return btnCustomerDetails;
	}

	public void viewPatient(String patientName) {
		boolean visible = patientName != null;
		lblLabelPatient.setVisible(visible);
		lblPatient.setVisible(visible);
		lblPatient.setText(patientName);
	}
}
