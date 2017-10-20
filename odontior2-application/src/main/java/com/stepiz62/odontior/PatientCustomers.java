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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.access.PostStatement;
import org.joty.common.BasicPostStatement;
import org.joty.workstation.gui.*;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.CheckBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.MultiPanelDialog;
import org.joty.workstation.gui.NavigatorPanel;

import com.stepiz62.odontior.PersonSearcher.SearchWhat;

public class PatientCustomers extends NavigatorPanel {
	JButton m_btnInvoices, m_btnBrowseCustomer, m_btnCustomerDetails;
	CheckBox m_isThePatient;
	JotyTextField m_firstName, m_lastName;
	private JotyTextField m_relationship;
	private boolean m_oldIsThePatient;
	private boolean m_gettingNewCustomer;
	
	public PatientCustomers() {
		m_currentButtonBehavior = ButtonBehavior.notEditingIdentified;

		m_btnInvoices = new JotyButton(appLang("Invoices"));
		m_btnInvoices.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openInvoicesOrEstimates(true);
			}
		});
		m_btnInvoices.setBounds(388, 243, 94, 23);
		add(m_btnInvoices);

		JButton m_btnEstimates = new JotyButton(appLang("Estimates"));
		m_btnEstimates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openInvoicesOrEstimates(false);
			}
		});
		m_btnEstimates.setBounds(485, 243, 94, 23);
		add(m_btnEstimates);

		m_table.setSize(393, 88);
		m_table.setLocation(186, 11);

		JLabel lblListOfCustomer = new JotyLabel(appLang("ListOfRelatedCustomers"));
		lblListOfCustomer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListOfCustomer.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblListOfCustomer.setBounds(2, 13, 181, 14);
		add(lblListOfCustomer);

		JotySeparator separator = new JotySeparator();
		separator.setBounds(10, 107, 569, 5);
		add(separator);

		JLabel lblCustomerDetails = new JotyLabel(appLang("RelatedCustomer"));
		lblCustomerDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCustomerDetails.setBounds(10, 141, 238, 14);
		add(lblCustomerDetails);

		m_isThePatient = Factory.createCheck(this, "isThePatient", "isThePatient");
		m_isThePatient.setText(appLang("IsThePatient"));
		m_isThePatient.setBounds(252, 206, 127, 23);
		add(m_isThePatient);
		term("isThePatient").defaultValue().setValue(0);

		setLayout(null);

		JLabel lblFirstName = new JotyLabel(appLang("FirstName"));
		lblFirstName.setBounds(4, 166, 84, 14);
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblFirstName);

		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setBounds(202, 166, 84, 14);
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblLastName);

		m_lastName = Factory.createTextRenderer(this, "lastname", true);
		m_lastName.setBounds(92, 163, 111, 20);
		add(m_lastName);
		m_firstName = Factory.createTextRenderer(this, "firstname", true);
		m_firstName.setBounds(288, 163, 111, 20);
		add(m_firstName);

		Factory.createHiddenLong(this, "customer_id");
		term("customer_id").setMandatory("lastname");

		m_currentButtonBehavior = ButtonBehavior.editing;
		m_btnBrowseCustomer = new JotyButton(appLang("Browse"), term("customer_id"));
		m_btnBrowseCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browseCustomer();
			}
		});
		m_btnBrowseCustomer.setBounds(305, 135, 94, 23);
		add(m_btnBrowseCustomer);

		JLabel lblRelation = new JotyLabel(appLang("Relationship"));
		lblRelation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRelation.setBounds(4, 209, 84, 14);
		add(lblRelation);

		m_relationship = Factory.createText(this, "Relationship", "relationship", 48);
		m_relationship.setBounds(92, 206, 156, 20);
		add(m_relationship);
		term("Relationship").setMandatory();

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setBounds(10, 158, 389, 5);
		add(separator_1);

		JotySeparator separator_2 = new JotySeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(403, 132, 3, 52);
		add(separator_2);

		m_btnCustomerDetails = new JotyButton(appLang("CustomerDetails"));
		m_btnCustomerDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContextParam("customerId", term("customer_id").integerVal());
				DataAccessDialog.tryCreate("CustomerDetailsDialog", callContext());
			}
		});
		m_btnCustomerDetails.setBounds(413, 163, 166, 23);
		add(m_btnCustomerDetails);

		JotySeparator separator_3 = new JotySeparator();
		separator_3.setBounds(10, 187, 569, 2);
		add(separator_3);

		subscribe("CustomerDialog");

		addTermToGrid("lastName", appLang("LastName"));
		addTermToGrid("firstName", appLang("FirstName"));
		addTermToGrid("relationship", appLang("ToPatientAs"));

		addIntegerKeyElem("patcust_id", true, true);
		defRelationElement("patient_id", "id");

		enableRole("Administrative personnel", Permission.all);
		enableRole("Practitioners", Permission.read);

	}


	
	@Override
	public void notifyEditingAction(ActionEvent e) {
		super.notifyEditingAction(e);
		if (getSource(e) == m_isThePatient)
			manageIsThePatient(e);
	}



	private void browseCustomer() {
		acquireSelectedValueFrom("PersonSearcher", "customer_id", new String[] { "lastname", "firstname" }, null, 
								SearchWhat.customers, null, false);
	}
	
	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		boolean customerMissing = term("customer_id").isNull();
		m_btnBrowseCustomer.setEnabled(bState && m_isThePatient.getCheck() == 0);
		term("Relationship").getComponent().setEnabled(bState && m_isThePatient.getCheck() == 0 && !customerMissing);
		m_isThePatient.setEnabled(bState);
		m_btnCustomerDetails.setEnabled(!customerMissing);
	}

	private String getPatientField(String fieldName) {
		return ((MultiPanelDialog) m_dialog).m_dataPanels.get(0).term(fieldName).strVal();
	}
	
	private void openInvoicesOrEstimates(boolean invoices) {
		setContextParam("patientCustomerID", keyElem("patcust_id").integerVal());
		setContextParam("firstName", term("firstName").strVal());
		setContextParam("lastName", term("lastName").strVal());
		setContextParam("isThePatient", term("isThePatient").integerVal());
		setContextParam("patientName", getPatientField("firstname") + " " + getPatientField("lastname"));
		if (invoices)
			DataAccessDialog.tryCreate("CustomerInvoicesDialog", callContext(), CustomerInvoicesDialog.QueryType.normal);
		else
			DataAccessDialog.tryCreate("CustomerEstimatesDialog", callContext());
	}

	private void manageIsThePatient(ActionEvent e) {
		if (m_isThePatient.getCheck() == 1) {
			boolean isThePatientExists = false;
			JotyDataBuffer buffer = getGridManager().m_gridBuffer;
			if (buffer.wfield("isThePatient").integerVal() == 0)
				for (int i = 0; i < buffer.m_records.size(); i++)
					if (buffer.wfield("isThePatient", i).integerVal() == 1) {
						isThePatientExists = true;
						break;
					}
			if (isThePatientExists && !m_oldIsThePatient) {
				m_app.warningMsg(m_common.appLang("PatientAsCustomerExists"));
				m_isThePatient.setCheck(0);
			} else {
				if (m_app.yesNoQuestion(m_common.appLang("WantPatientAsCustomer")))
					doTheSetting(true);
				else
					m_isThePatient.setCheck(0);
			}
		} else
			doTheSetting(false);
	}		
						
	private void doTheSetting(boolean isThePatient) {
		if (isThePatient) {
			PostStatement postStatement = m_app.accessorPostStatement("org.odontior.CustomerDialog", 0, null, m_app.m_paramContext, null);
	        postStatement.m_mainFilter = "lastname = '" + ((OdontiorApp) m_app).patientLastName() + "' and firstname = '" + ((OdontiorApp) m_app).patientFirstName() + "'";
			WResultSet rs = m_app.openDbWResultSetByPostStatement(postStatement);
			boolean initNewCustomer = false;
			if (rs != null) {
				if (rs.isEOF())
					initNewCustomer = true;
				else 
					setRelatedCustomer(rs.integerValue("id"));
				rs.close();
			}
			if (initNewCustomer) {
				m_app.m_justSelectedValue = -1;
				m_app.m_valuesContainer.clear();
				m_gettingNewCustomer = true;
				JotyDialog.tryCreate("CustomerDialog", null, CustomerDialog.OpenMode.patientCreatingAsCustomer);
			}
		} 
		completeIsThePatientSetting(true);
	}
	
	private void setRelatedCustomer(long customerId) {
		term("customer_id").setVal(customerId);
		term("customer_id").setDirty();
		term("firstName").setVal(((OdontiorApp) m_app).patientFirstName());
		term("lastName").setVal(((OdontiorApp) m_app).patientLastName());
		term("customer_id").guiDataExch(false);
		term("firstName").guiDataExch(false);
		term("lastName").guiDataExch(false);
	}

	void completeIsThePatientSetting(boolean done) {
		if (done) {
			term("Relationship").clear();
			if (m_isThePatient.getCheck() == 1) {
				term("Relationship").setVal(m_common.appLang("Oneself"));
				term("Relationship").setDirty();
				term("Relationship").guiDataExch(false);
			}
		} else
			m_isThePatient.setCheck(0);
		enableComponents(true);
	}


	@Override
	public void doActivation() {
		int isThePatientCurrent = m_isThePatient.getCheck();
		super.doActivation();
		m_isThePatient.m_term.setInteger(isThePatientCurrent);
		m_isThePatient.m_term.guiDataExch(false);
		if (m_gettingNewCustomer && m_app.getOpenedDialog("CustomerDialog", true) == null) {
			m_gettingNewCustomer = false;
			if (m_app.m_justSelectedValue == -1)
				m_isThePatient.setCheck(0);
			else
				setRelatedCustomer(m_app.m_justSelectedValue);
			completeIsThePatientSetting(m_app.m_justSelectedValue != -1);
		}			
	}
	
	
}
