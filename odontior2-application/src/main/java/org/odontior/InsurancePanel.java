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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TextArea;
import org.joty.workstation.gui.ComboBox.ActionPostExecutor;

public class InsurancePanel extends NavigatorPanel {
	private JLabel lblReimbursement;
	private JLabel lblMaxamount;
	private JLabel lblPercentage;
	private JLabel lblDeductible;
	private JButton btnBrowse;
	private JButton m_btnCompanyDetails;
	private ComboBox m_reimbursementPlans;

	public InsurancePanel() {
		super();
		m_table.setSize(496, 75);
		m_table.setLocation(10, 11);

		ComboBox company = Factory.createDescrRenderer(this, "company", "insurancecompany_id", "insuranceCompanies", false);

		company.setBounds(12, 138, 334, 20);
		add(company);
		company.setActionPostExecutor(new ActionPostExecutor() {
			@Override
			public void doIt(ComboBox comboBox) {
				loadReimbursementPlans();
			}
		});
		term("company").setModifiable(false);
		term("company").setMandatory();

		JLabel lblCompany = new JotyLabel(appLang("Company"));
		lblCompany.setHorizontalAlignment(SwingConstants.LEFT);
		lblCompany.setBounds(13, 121, 105, 14);
		add(lblCompany);

		JotyTextField jotyTextField = Factory.createDate(this, "sinceDate", "sincedate");
		jotyTextField.setBounds(436, 98, 70, 20);
		add(jotyTextField);
		term("sinceDate").setMandatory();

		JotyTextField jotyTextField_1 = Factory.createText(this, "contact", "contact", 32);
		jotyTextField_1.setBounds(81, 215, 191, 20);
		add(jotyTextField_1);

		JotyTextField jotyTextField_2 = Factory.createText(this, "contactPhone", "contactphone", 20);
		jotyTextField_2.setBounds(386, 215, 120, 20);
		add(jotyTextField_2);

		JotyTextField jotyTextField_3 = Factory.createText(this, "supervisor", "supervisor", 32);
		jotyTextField_3.setBounds(386, 188, 120, 20);
		add(jotyTextField_3);

		ComboBox insuranceTypes = Factory.createComboBox(this, "type", "type_id", false, "insuranceTypes");
		insuranceTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateVisibilityBasingOnData();
			}
		});
		insuranceTypes.setBounds(81, 98, 264, 20);
		add(insuranceTypes);
		term("type").setMandatory();

		TextArea textArea = Factory.createTextArea(this, "note", "note", 255);
		textArea.setBounds(58, 349, 448, 66);
		add(textArea);

		JLabel lblNote = new JotyLabel(appLang("Note"));
		lblNote.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNote.setBounds(10, 350, 43, 14);
		add(lblNote);

		JLabel lblType = new JotyLabel(appLang("Type"));
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(0, 101, 78, 14);
		add(lblType);

		JLabel lblSupervisor = new JotyLabel(appLang("Supervisor"));
		lblSupervisor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSupervisor.setBounds(308, 191, 78, 14);
		add(lblSupervisor);

		JLabel lblContactPhone = new JotyLabel(appLang("ContactPhone"));
		lblContactPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactPhone.setBounds(285, 218, 100, 14);
		add(lblContactPhone);

		JLabel lblContact = new JotyLabel(appLang("Contact"));
		lblContact.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContact.setBounds(1, 218, 78, 14);
		add(lblContact);

		JLabel lblSinceDate = new JotyLabel(appLang("SinceDate"));
		lblSinceDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSinceDate.setBounds(367, 101, 68, 14);
		add(lblSinceDate);

		Factory.createHiddenLong(this, "customer_id", "customerId");

		m_currentButtonBehavior = ButtonBehavior.editing;
		btnBrowse = new JotyButton(appLang("Browse"), term("company"));
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acquireSelectedValueFrom("InsuranceCompanyDialog", "company", null, null, null, null, true);
			}
		});
		btnBrowse.setBounds(348, 137, 74, 23);
		add(btnBrowse);

		m_reimbursementPlans = Factory.createComboBox(this, "reimbursementPlans", "reimbursementplan_id", false, null);
		m_reimbursementPlans.setBounds(117, 187, 191, 22);
		add(m_reimbursementPlans);
		term("company").effectsPostPone(term("reimbursementPlans"));

		lblReimbursement = new JotyLabel(appLang("ReimbursementPlan"));
		lblReimbursement.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReimbursement.setBounds(-2, 191, 117, 14);
		add(lblReimbursement);

		JLabel lblStart = new JotyLabel(appLang("Start"));
		lblStart.setHorizontalAlignment(SwingConstants.LEFT);
		lblStart.setBounds(60, 269, 50, 14);
		add(lblStart);

		JotyTextField jotyTextField_4 = Factory.createDate(this, "benefitperiod_start", "benefitperiod_start");
		jotyTextField_4.setBounds(58, 284, 70, 20);
		add(jotyTextField_4);

		JLabel lblEnd = new JotyLabel(appLang("End"));
		lblEnd.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnd.setBounds(162, 269, 50, 14);
		add(lblEnd);

		JotyTextField jotyTextField_5 = Factory.createDate(this, "benefitperiod_end", "benefitperiod_end");
		jotyTextField_5.setBounds(161, 284, 70, 20);
		add(jotyTextField_5);

		JLabel lblBenefitPeriod = new JotyLabel(appLang("BenefitPeriod"));
		lblBenefitPeriod.setHorizontalAlignment(SwingConstants.CENTER);
		lblBenefitPeriod.setBounds(60, 251, 171, 14);
		add(lblBenefitPeriod);

		JotySeparator separator = new JotySeparator();
		separator.setBounds(60, 267, 171, 2);
		add(separator);

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(266, 247, 2, 95);
		add(separator_1);

		JotyTextField alreadyspent = Factory.createMoney(this, "alreadyspent", "alreadyspent");
		alreadyspent.setBounds(424, 248, 82, 20);
		add(alreadyspent);

		JotySeparator separator_2 = new JotySeparator();
		separator_2.setBounds(58, 246, 446, 3);
		add(separator_2);

		JLabel lblAlreadyspent = new JotyLabel(appLang("AmountAlreadySpent"));
		lblAlreadyspent.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAlreadyspent.setBounds(279, 251, 141, 14);
		add(lblAlreadyspent);

		JotyTextField maxamount = Factory.createMoney(this, "maxamount", "maxamount");
		maxamount.setBounds(424, 272, 82, 20);
		add(maxamount);

		JotyTextField percentage = Factory.createDecimal(this, "percentage", "percentage", 4);
		percentage.setBounds(424, 296, 44, 20);
		add(percentage);

		JotyTextField deductible = Factory.createMoney(this, "deductible", "deductible");
		deductible.setBounds(424, 321, 82, 20);
		add(deductible);

		lblMaxamount = new JotyLabel(appLang("MaxAmount"));
		lblMaxamount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxamount.setBounds(279, 273, 141, 14);
		add(lblMaxamount);

		lblDeductible = new JotyLabel(appLang("Deductible"));
		lblDeductible.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDeductible.setBounds(279, 324, 141, 14);
		add(lblDeductible);

		lblPercentage = new JotyLabel(appLang("Percentage"));
		lblPercentage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPercentage.setBounds(279, 300, 141, 14);
		add(lblPercentage);

		JLabel lblName = new JotyLabel(appLang("FriendlyName"));
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(58, 166, 165, 14);
		add(lblName);

		JotyTextField name = Factory.createText(this, "name", "name", 32);
		name.setBounds(225, 164, 281, 20);
		add(name);

		JotySeparator separator_3 = new JotySeparator();
		separator_3.setBounds(12, 134, 493, 3);
		add(separator_3);

		m_btnCompanyDetails = new JotyButton(appLang("Details"));
		m_btnCompanyDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContextParam("companyId", term("company").integerVal());
				DataAccessDialog.tryCreate("InsuranceCompanyDetailsDialog", callContext());
			}
		});
		m_btnCompanyDetails.setBounds(424, 137, 82, 23);
		add(m_btnCompanyDetails);
		term("name").setMandatory();

		addTermToGrid("name", appLang("FriendlyName"));
		addTermToGrid("company", appLang("Company"));
		addTermToGrid("sincedate", appLang("Since"));
		addTermToGrid("type", appLang("Type"));

		addIntegerKeyElem("ID", true, true);
		enableRole("Administrative personnel", Permission.all);
	}

	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		btnBrowse.setEnabled(bState && !documentIdentified());
		m_btnCompanyDetails.setEnabled(!term("company").isNull());
		m_reimbursementPlans.setEnabled(bState && !term("company").isNull());
	}

	void loadReimbursementPlans() {
		comboTerm("reimbursementPlans").buildLiteralStruct("D_10", "id", "name", "insurancecompany_id = " + comboTerm("company").selectionData());
		comboTerm("reimbursementPlans").loadDescrList();
		if (m_initializing)
			comboTerm("reimbursementPlans").guiDataExch(false);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 100, 100, 40, 60 };
		table.setAllColsAlignement(SwingConstants.CENTER);
	}

	@Override
	protected void updateVisibilityBasingOnData() {
		long type = comboTerm("type").selectionData();
		boolean enableStatus = type == 1 || type == 2;
		lblMaxamount.setVisible(enableStatus);
		term("maxamount").getComponent().setVisible(enableStatus);
		lblDeductible.setVisible(enableStatus);
		term("deductible").getComponent().setVisible(enableStatus);
		enableStatus = type == 1 || type == 3;
		lblPercentage.setVisible(enableStatus);
		term("percentage").getComponent().setVisible(enableStatus);
		enableStatus = type == 1 || type == 4;
		lblReimbursement.setVisible(enableStatus);
		term("reimbursementPlans").getComponent().setVisible(enableStatus);
	}

}
