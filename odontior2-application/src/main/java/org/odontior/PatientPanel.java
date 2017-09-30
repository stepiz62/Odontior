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
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.gui.CheckBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.RadioButton;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.Term;

public class PatientPanel extends DataScrollingPanel {

	class ClinicalCardDetailsAdapter extends BuildDetailsDialogAdapter {
		boolean directInit;
		boolean direct;
		boolean unified;
		long clinicalCardId;
		JotyDataBuffer buffer;

		ClinicalCardDetailsAdapter(boolean direct, boolean unified) {
			directInit = direct;
			this.unified = unified;
		}

		@Override
		public JotyDialog createDialog(TableTerm term) {
			setContextParam("uniqueCcard", unified ? 1 : 0);
			direct = directInit && term.getSelection() >= 0;
			buffer = term.m_dataBuffer;
			if (direct && !unified)
				setContextParam("clinicalCardID", buffer.integerValue("ID"));
			return DataAccessDialog.getInstance(direct || unified ? "CCardDetailsDialog" : "ClinicalCardDialog", callContext(), direct || unified ? null : ClinicalCardDialog.QueryType.normal);
		}

		@Override
		public String identifierFromCaller() {
			return (unified ? "(" + appLang("Unified") + ") - " : (direct ? ("N. " + buffer.integerValue("number") + " - ") : "")) + patientName();
		}
	}

	JButton m_btnOpenClinCard;
	JButton m_btnAsUnif;
	JButton m_btnInvoices;
	private AppointmentMonitor m_appointmentMonitor = new AppointmentMonitor();

	private CheckBox m_chckbxTodayOnward;;

	public PatientPanel(boolean likeTheAncestor) {
		super(false, likeTheAncestor);

		JLabel lblFirstName = new JotyLabel(appLang("FirstName"));
		lblFirstName.setBounds(6, 17, 63, 14);
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblFirstName);

		JLabel lblLastName = new JotyLabel(appLang("LastName"));
		lblLastName.setBounds(6, 57, 63, 14);
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblLastName);

		JotyTextField firstName = Factory.createText(this, "firstName", "firstName", 24);
		firstName.setBounds(73, 14, 120, 20);
		add(firstName);
		term("firstName").setMandatory();

		JotyTextField lastName = Factory.createText(this, "lastName", "lastName", 24);
		lastName.setBounds(74, 54, 120, 20);
		add(lastName);
		term("lastName").setMandatory();

		JotyTextField dateOfBirth = Factory.createDate(this, "dateOfBirth", "dateOfBirth");
		dateOfBirth.setBounds(326, 14, 70, 20);
		add(dateOfBirth);
		term("dateOfBirth").setMandatory();

		JLabel lblDateOfBirth = new JotyLabel(appLang("DOB"));
		lblDateOfBirth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDateOfBirth.setBounds(222, 17, 100, 14);
		add(lblDateOfBirth);

		RadioButton rdbtnMale = Factory.createGroupMasterRadio(this, "sex", "sex");
		rdbtnMale.setText(appLang("Male"));
		rdbtnMale.setBounds(320, 40, 147, 16);
		add(rdbtnMale);
		term("sex").setMandatory();

		RadioButton rdbtnFemale = Factory.createRadioForGroup(this, "sexFemale", "sex");
		rdbtnFemale.setText(appLang("Female"));
		rdbtnFemale.setBounds(320, 57, 147, 16);
		add(rdbtnFemale);

		m_currentButtonBehavior = ButtonBehavior.notEditingIdentified;

		m_btnInvoices = new JotyButton(appLang("RelatedInvoicesV"));
		m_btnInvoices.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("CustomerInvoicesDialog", callContext(), CustomerInvoicesDialog.QueryType.forPatient);
			}
		});
		m_btnInvoices.setBounds(416, 14, 164, 23);
		add(m_btnInvoices);

		m_btnOpenClinCard = new JotyButton(appLang("Descriptors"));
		m_btnOpenClinCard.addActionListener(new ActionListener() {
			ClinicalCardDetailsAdapter adapter = new ClinicalCardDetailsAdapter(false, false);

			@Override
			public void actionPerformed(ActionEvent e) {
				openDetail(tableTerm("ClinicalCards"), adapter);
			}
		});
		m_btnOpenClinCard.setBounds(367, 106, 100, 23);
		add(m_btnOpenClinCard);

		m_btnAsUnif = new JotyButton(appLang("Unified"));
		m_btnAsUnif.addActionListener(new ActionListener() {
			ClinicalCardDetailsAdapter adapter = new ClinicalCardDetailsAdapter(true, true);

			@Override
			public void actionPerformed(ActionEvent e) {
				openDetail(tableTerm("ClinicalCards"), adapter);
			}
		});
		m_btnAsUnif.setBounds(479, 106, 101, 23);
		add(m_btnAsUnif);

		JLabel lblClinicalCards = new JotyLabel(appLang("ClinicalCards"));
		lblClinicalCards.setBounds(368, 89, 212, 14);
		add(lblClinicalCards);

		Table clinicalCards = Factory.createTable(this, "ClinicalCards", null, "ID", null, null);
		clinicalCards.setBounds(367, 130, 213, 86);
		add(clinicalCards);

		TableTerm term = tableTerm("ClinicalCards");
		term.m_whereClauseImplementor = new GetWhereClauseInterface() {
			@Override
			public String method(DataAccessPanel m_panel, boolean usedInTransaction) {
				return " WHERE patient_id = " + String.valueOf(integerKeyElemVal("ID"));
			}
		};
		enabledAsDetail("ClinicalCards");
		term.m_buildDetailsHandler = new ClinicalCardDetailsAdapter(true, false);
		term.addField("number", appLang("Number"));
		term.addField("openingDate", appLang("Opened"));
		term.addField("closingDate", appLang("Closed"));
		clinicalCards.m_colWidths = new int[] { 70, 100, 100 };
		clinicalCards.setAllColsAlignement(SwingConstants.CENTER);

		JLabel lblAppointments = new JotyLabel(appLang("Appointments"));
		lblAppointments.setBounds(11, 221, 133, 14);
		add(lblAppointments);

		Table appointments = Factory.createTable(this, "Appointments", null, "ID", null, null);
		appointments.setBounds(8, 236, 572, 175);
		add(appointments);
		enabledAsDetail("Appointments");
		term = tableTerm("appointments");

		tableTerm("Appointments").m_actionOnRowHandler = new ActionOnRowInterface() {
			@Override
			public void doAction(Term srcTerm, int column) {
				JotyDataBuffer buffer = tableTerm("appointments").m_dataBuffer;
				m_appointmentMonitor.addressOrganizer(buffer.integerValue("location_id"), buffer.dateValue("date"), buffer.integerValue("daypart_id"));
			}
		};
		term.setKeyName("id");
		term.addField("date", appLang("Date"));
		term.addField("timelabel", appLang("Time"));
		term.addField("seat", appLang("Armchair"));
		term.addField("treatment", appLang("Treatment"));
		term.addField("tooth", appLang("Tooth"));
		term.addField("duration", appLang("Duration"));
		term.addField("practitioner", appLang("Practitioner"));
		term.addFieldAsFlag("macro", appLang("Macro"));
		term.addFieldAsFlag("done", appLang("Done"));
		appointments.m_colWidths = new int[] { 70, 40, 120, 120, 50, 50, 120, 15, 15 };
		appointments.setAllColsAlignement(SwingConstants.CENTER);
		appointments.m_sortClickDenied = true;

		term.subscribe("OrganizerDialog");
		term.subscribe("AppointmentDialog");

		JLabel lblQueuedEvents = new JotyLabel(appLang("QueuedEvents"));
		lblQueuedEvents.setBounds(11, 115, 147, 13);
		add(lblQueuedEvents);

		Table queuedEvents = Factory.createTable(this, "queuedEvents");
		tableTerm("queuedEvents").setKeyName("ID");
		queuedEvents.setBounds(8, 130, 351, 86);
		add(queuedEvents);
		enabledAsDetail("queuedEvents");
		term = tableTerm("queuedEvents");

		tableTerm("queuedEvents").m_actionOnRowHandler = new ActionOnRowInterface() {
			@Override
			public void doAction(Term srcTerm, int column) {
				JotyDataBuffer buffer = tableTerm("queuedEvents").m_dataBuffer;
				m_appointmentMonitor.addressWaitlist(buffer.integerValue("id"));
			}
		};
		term.setKeyName("id");
		term.addField("treatment", appLang("Treatment"));
		term.addField("tooth", appLang("Tooth"));
		term.addField("duration", appLang("Duration"));
		term.addField("practitioner", appLang("Practitioner"));
		term.addFieldAsFlag("macro", appLang("Macro"));
		queuedEvents.m_colWidths = new int[] { 150, 50, 70, 150, 15 };
		queuedEvents.setAllColsAlignement(SwingConstants.CENTER);
		term.subscribe("WaitlistDialog");
		term.subscribe("AppointmentDialog");

		JotySeparator separator = new JotySeparator();
		separator.setBounds(367, 104, 213, 2);
		add(separator);

		m_chckbxTodayOnward = Factory.createCheck(this, "todayOnward", null);
		m_chckbxTodayOnward.setText(appLang("TodayOnward"));
		m_chckbxTodayOnward.setBounds(142, 221, 164, 14);
		add(m_chckbxTodayOnward);
		term("todayOnward").setInteger(1);
		setContextParam("todayOnward", "1");
		term("todayOnward").setAsControlTerm();
		setRemainEnabled("todayOnward");

		setAsPublisher();

		addIntegerKeyElem("ID", true, true);

		enableRole("Administrative personnel", Permission.all);
		enableRole("Practitioners", Permission.read);
		enableRole("Doctors", Permission.read);

		if (getDialog() instanceof PatientDetailsDialog)
			keyElem("id").setInteger(Long.parseLong(contextParameter("patientId")));

	}

	@Override
	public void notifyEditingAction(ActionEvent e) {
		super.notifyEditingAction(e);
		if (getSource(e) == m_chckbxTodayOnward) {
			setContextParam("todayOnward", String.valueOf(m_chckbxTodayOnward.getCheck()));
			setContextParams();
			tableTerm("appointments").refresh();
		}
	}

	private String patientName() {
		return textTerm("firstName") + " " + textTerm("lastName");
	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("patientID", keyElem("id").integerVal());
		setContextParam("patientName", patientName());
	}
	
	
}
