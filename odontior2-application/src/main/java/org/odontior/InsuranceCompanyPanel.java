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

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;

public class InsuranceCompanyPanel extends DataScrollingPanel {
	public InsuranceCompanyPanel(boolean likeTheAncestor) {
		super(false, likeTheAncestor);

		JotyTextField jotyTextField = Factory.createText(this, "Name", "company", 48);
		jotyTextField.setBounds(76, 9, 321, 20);
		add(jotyTextField);
		term("Name").setMandatory();

		JLabel lblAddress = new JotyLabel(appLang("Address"));
		lblAddress.setBounds(5, 40, 66, 14);
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblAddress);

		JLabel lblZipCode = new JotyLabel(appLang("ZipCode"));
		lblZipCode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZipCode.setBounds(419, 40, 81, 14);
		add(lblZipCode);

		JotyTextField textField_5 = Factory.createText(this, "city", "city", 32);
		textField_5.setBounds(76, 65, 195, 20);
		add(textField_5);
		term("city").setMandatory();

		JLabel lblCity = new JotyLabel(appLang("City"));
		lblCity.setBounds(5, 68, 66, 14);
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblCity);

		JLabel lblCountry = new JotyLabel(appLang("Country"));
		lblCountry.setBounds(5, 97, 66, 14);
		lblCountry.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblCountry);

		JotyTextField textField = Factory.createText(this, "title", "title", 10);
		textField.setBounds(346, 167, 63, 20);
		add(textField);

		JotyTextField textField_3 = Factory.createText(this, "address", "address", 48);
		textField_3.setBounds(76, 37, 271, 20);
		add(textField_3);
		term("address").setMandatory();

		JotyTextField textField_4 = Factory.createText(this, "zipcode", "zipcode", 10);
		textField_4.setBounds(502, 37, 81, 20);
		add(textField_4);
		term("zipcode").setMandatory();

		JotyTextField textField_8 = Factory.createText(this, "email", "email", 32);
		textField_8.setBounds(364, 95, 219, 20);
		add(textField_8);
		term("email").setMandatory();

		JLabel lblEmail = new JotyLabel(appLang("Email"));
		lblEmail.setBounds(316, 97, 46, 14);
		lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblEmail);

		JotyTextField textField_9 = Factory.createText(this, "group", "group_", 32);
		textField_9.setBounds(447, 9, 136, 20);
		add(textField_9);

		JLabel lblGroup = new JotyLabel(appLang("Group"));
		lblGroup.setBounds(395, 12, 50, 14);
		lblGroup.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblGroup);

		JLabel lblphone = new JotyLabel(appLang("Phone"));
		lblphone.setBounds(5, 126, 66, 14);
		lblphone.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblphone);

		JotyTextField textField_11 = Factory.createText(this, "contactPhone", "contactPhone", 20);
		textField_11.setBounds(488, 167, 95, 20);
		add(textField_11);

		JotyTextField textField_12 = Factory.createText(this, "phone", "phone", 20);
		textField_12.setBounds(76, 124, 95, 20);
		add(textField_12);
		term("phone").setMandatory();

		ComboBox country = Factory.createComboBox(this, "country", "country_id", false, "countries");
		country.setBounds(76, 93, 243, 22);
		add(country);

		JLabel label = new JotyLabel(appLang("State"));
		label.setBounds(281, 69, 54, 14);
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		add(label);

		ComboBox comboBox = Factory.createComboBox(this, "state", "state_id", false, "states");
		comboBox.setBounds(340, 65, 243, 22);
		add(comboBox);

		JLabel lblTitle = new JotyLabel(appLang("Title"));
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(283, 171, 62, 14);
		add(lblTitle);

		JLabel lblContactPhone = new JotyLabel(appLang("Phone"));
		lblContactPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContactPhone.setBounds(419, 171, 67, 14);
		add(lblContactPhone);

		JLabel lblName = new JotyLabel(appLang("Name"));
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(5, 12, 66, 14);
		add(lblName);

		JotyTextField jotyTextField_1 = Factory.createText(this, "contact", "contact", 32);
		jotyTextField_1.setBounds(75, 167, 190, 20);
		add(jotyTextField_1);

		JLabel lblContact = new JotyLabel(appLang("Contact"));
		lblContact.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContact.setBounds(5, 170, 66, 14);
		add(lblContact);

		Table reimbursementPlans = Factory.createTable(this, "ReimbursementPlans");
		reimbursementPlans.setBounds(349, 212, 234, 79);
		add(reimbursementPlans);

		JLabel lblReimbursementPlans = new JotyLabel(appLang("ReimbursementPlans"));
		lblReimbursementPlans.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReimbursementPlans.setBounds(188, 212, 154, 14);
		add(lblReimbursementPlans);

		JotySeparator separator = new JotySeparator();
		separator.setBounds(77, 164, 507, 4);
		add(separator);

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setBounds(77, 188, 507, 4);
		add(separator_1);

		TableTerm term = tableTerm("ReimbursementPlans");
		term.setKeyName("id");
		term.addField("name", appLang("name"));
		term.m_buildDetailsHandler = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				setContextParam("companyID", integerKeyElemVal("id"));
				setContextParam("companyName", term("name").strVal());
				return DataAccessDialog.getInstance("ReimbursementPlanDialog", callContext());
			}

			@Override
			public String identifierFromCaller() {
				return term("name").strVal();
			}
		};
		enabledAsDetail("ReimbursementPlans");

		addTermToGrid("name", appLang("Name"));
		addTermToGrid("group", appLang("Group"));
		addTermToGrid("City", appLang("City"));

		addIntegerKeyElem("ID", true, true);

		if (getDialog() instanceof InsuranceCompanyDetailsDialog)
			keyElem("id").setInteger(Long.parseLong(contextParameter("companyId")));

		enableRole("Administrative personnel", Permission.all);

	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("id", integerKeyElemVal("ID"));
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 100, 100, 30 };
	}

}
