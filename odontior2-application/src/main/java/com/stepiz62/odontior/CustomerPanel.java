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
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.TextArea;

public class CustomerPanel extends DataScrollingPanel {
	JButton m_btnInvoices;
	JButton m_btnEstimates;
	JotyTextField m_firstName;
	JotyTextField m_lastName;

	public CustomerPanel(boolean likeTheAncestor) {
		super(false, likeTheAncestor);
		m_currentButtonBehavior = ButtonBehavior.notEditingIdentified;

		m_btnInvoices = new JotyButton(appLang("Invoices"));
		m_btnInvoices.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openInvoicesOrEstimates(true);
			}
		});
		m_btnInvoices.setBounds(503, 375, 81, 23);
		add(m_btnInvoices);

		m_btnEstimates = new JotyButton(appLang("Estimates"));
		m_btnEstimates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openInvoicesOrEstimates(false);
			}
		});
		m_btnEstimates.setBounds(503, 409, 81, 23);
		add(m_btnEstimates);

		JLabel lblInsurance = new JotyLabel(appLang("Insurances"));
		lblInsurance.setBounds(13, 273, 127, 14);
		add(lblInsurance);

		Table insurances = Factory.createTable(this, "Insurances");
		insurances.setBounds(10, 289, 574, 63);
		add(insurances);

		TableTerm term = tableTerm("Insurances");
		term.setKeyName("id");
		term.addField("name", appLang("FriendlyName"));
		term.addField("company", appLang("Company"));
		term.addField("sincedate", appLang("Since"));
		term.addField("type", appLang("Type"));
		insurances.m_colWidths = new int[] { 170, 200, 70, 100 };
		insurances.setAllColsAlignement(SwingConstants.CENTER);
		term.m_buildDetailsHandler = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				return DataAccessDialog.getInstance("InsuranceDialog", callContext());
			}

			@Override
			public String identifierFromCaller() {
				return m_firstName.getText() + " " + m_lastName.getText();
			}
		};
		enabledAsDetail("Insurances");

		setLayout(null);
		JLabel lblTitle = new JotyLabel(appLang("Title"));
		lblTitle.setBounds(4, 14, 67, 14);
		lblTitle.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblTitle);

		JLabel lblFirstName = new JotyLabel(appLang("FirstName"));
		lblFirstName.setBounds(149, 14, 69, 14);
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblFirstName);

		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setBounds(357, 14, 101, 14);
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblLastName);

		JLabel lblAddress = new JotyLabel(appLang("Address"));
		lblAddress.setBounds(4, 65, 67, 14);
		lblAddress.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblAddress);

		JLabel lblZipCode = new JotyLabel(appLang("ZipCode"));
		lblZipCode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZipCode.setBounds(388, 65, 109, 14);
		add(lblZipCode);

		JotyTextField textField_5 = Factory.createText(this, "city", "city", 32);
		textField_5.setBounds(76, 90, 120, 20);
		add(textField_5);
		term("city").setMandatory();

		JLabel lblCity = new JotyLabel(appLang("City"));
		lblCity.setBounds(4, 93, 67, 14);
		lblCity.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblCity);

		JLabel lblCountry = new JotyLabel(appLang("Country"));
		lblCountry.setBounds(4, 150, 67, 14);
		lblCountry.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblCountry);

		JotyTextField textField = Factory.createText(this, "title", "title", 10);
		textField.setBounds(76, 11, 63, 20);
		add(textField);

		m_firstName = Factory.createText(this, "firstName", "firstName", 24);
		m_firstName.setBounds(222, 11, 120, 20);
		add(m_firstName);
		term("firstName").setMandatory();

		m_lastName = Factory.createText(this, "lastName", "lastName", 24);
		m_lastName.setBounds(459, 11, 125, 20);
		add(m_lastName);
		term("lastName").setMandatory();

		JotyTextField textField_3 = Factory.createText(this, "address", "address", 48);
		textField_3.setBounds(76, 62, 271, 20);
		add(textField_3);
		term("address").setMandatory();

		JotyTextField textField_4 = Factory.createText(this, "zipcode", "zipcode", 10);
		textField_4.setBounds(503, 62, 81, 20);
		add(textField_4);
		term("zipcode").setMandatory();

		JotyTextField textField_7 = Factory.createText(this, "mobilePhone", "mobilePhone", 20);
		textField_7.setBounds(489, 118, 95, 20);
		add(textField_7);
		term("mobilePhone").setMandatory();

		JLabel lblPhone = new JotyLabel(appLang("MobilePhone"));
		lblPhone.setBounds(385, 121, 101, 14);
		lblPhone.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblPhone);

		JotyTextField textField_8 = Factory.createText(this, "email", "email", 48);
		textField_8.setBounds(76, 179, 271, 20);
		add(textField_8);

		JLabel lblEmail = new JotyLabel(appLang("Email"));
		lblEmail.setBounds(4, 182, 67, 14);
		lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblEmail);

		JotyTextField textField_9 = Factory.createText(this, "taxCode", "taxCode", 32);
		textField_9.setBounds(448, 36, 136, 20);
		add(textField_9);
		term("taxCode").setMandatory();

		JLabel lblTaxCode = new JotyLabel(appLang("TaxCode"));
		lblTaxCode.setBounds(357, 38, 88, 14);
		lblTaxCode.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblTaxCode);

		JLabel lblHomePhone = new JotyLabel(appLang("HomePhone"));
		lblHomePhone.setBounds(385, 146, 101, 14);
		lblHomePhone.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblHomePhone);

		JLabel lblWorkPhone = new JotyLabel(appLang("WorkPhone"));
		lblWorkPhone.setBounds(385, 171, 101, 14);
		lblWorkPhone.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblWorkPhone);

		JotyTextField textField_11 = Factory.createText(this, "homePhone", "homePhone", 20);
		textField_11.setBounds(489, 143, 95, 20);
		add(textField_11);

		JotyTextField textField_12 = Factory.createText(this, "workPhone", "workPhone", 20);
		textField_12.setBounds(489, 168, 95, 20);
		add(textField_12);

		ComboBox country = Factory.createComboBox(this, "country", "country_id", false, "countries");
		country.setBounds(76, 146, 285, 22);
		add(country);

		JLabel label = new JotyLabel(appLang("State"));
		label.setBounds(4, 122, 67, 14);
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		add(label);

		ComboBox comboBox = Factory.createComboBox(this, "state", "state_id", false, "states");
		comboBox.setBounds(76, 118, 285, 22);
		add(comboBox);

		TextArea text = Factory.createTextArea(this, "notes", "notes", 255);
		text.setBounds(10, 225, 574, 42);
		add(text);

		Table patients = Factory.createTable(this, "Patients");
		patients.setBounds(10, 372, 480, 63);
		add(patients);

		JLabel lblPatiens = new JotyLabel(appLang("Patients"));
		lblPatiens.setBounds(13, 356, 127, 14);
		add(lblPatiens);

		term = tableTerm("Patients");
		term.setKeyName("id");
		term.addField("firstname", appLang("FirstName"));
		term.addField("lastname", appLang("LastName"));
		term.addField("relationship", appLang("CustomerPosition"));
		term.m_buildDetailsHandler = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				setContextParam("patientId", term.m_dataBuffer.integerValue("id"));
				return DataAccessDialog.getInstance("PatientDetailsDialog", callContext());
			}
		};
		enabledAsDetail("Patients");
		term.setRowActionButton(m_btnInvoices);
		term.setRowActionButton(m_btnEstimates);

		JLabel lblNotes = new JotyLabel(appLang("Note"));
		lblNotes.setBounds(13, 209, 127, 14);
		add(lblNotes);

		addIntegerKeyElem("id", true, true);

		if (getDialog() instanceof CustomerDetailsDialog)
			keyElem("id").setInteger(Long.parseLong(contextParameter("customerId")));

		setAsPublisher();

		enableRole("Administrative personnel", Permission.all);
		enableRole("Doctors", Permission.read);
		enableRole("Practitioners", Permission.read);
		enableRole("Schedulers", Permission.read);

	}

	private void openInvoicesOrEstimates(boolean invoices) {
		JotyDataBuffer buffer = tableTerm("Patients").m_dataBuffer;
		setContextParam("patientCustomerID", buffer.integerValue("patcust_id"));
		setContextParam("firstName", term("firstName").strVal());
		setContextParam("lastName", term("lastName").strVal());
		setContextParam("isThePatient", buffer.integerValue("isThePatient"));
		setContextParam("patientName", buffer.strValue("firstname") + " " + buffer.strValue("lastname"));
		if (invoices)
			DataAccessDialog.tryCreate("CustomerInvoicesDialog", callContext(), CustomerInvoicesDialog.QueryType.normal);
		else
			DataAccessDialog.tryCreate("CustomerEstimatesDialog", callContext());
	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("customerId", keyElem("id").integerVal());
	}

}
