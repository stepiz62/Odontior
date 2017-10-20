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

import org.joty.app.JotyException;
import org.joty.common.BasicPostStatement;
import org.joty.common.BasicPostStatement.Item;
import org.joty.data.JotyDate;
import org.joty.workstation.gui.*;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.TextArea;

public class ClinicalCardPanel extends DataScrollingPanel {

	JButton m_btnOpenDetails;
	boolean m_specific;

	public ClinicalCardPanel() {
		super(true);
		m_specific = getDialog().getMode() == ClinicalCardDialog.QueryType.specific;

		JotyTextField textField = Factory.createDate(this, "Opened", "OpeningDate");
		textField.setBounds(248, 19, 70, 20);
		add(textField);
		term("Opened").defaultValue().setValue(new JotyDate(m_app, "now"));
		term("Opened").setMandatory();

		JotyTextField textField_1 = Factory.createDate(this, "Closed", "ClosingDate");
		textField_1.setBounds(248, 46, 70, 20);
		add(textField_1);

		JLabel lblOpened = new JotyLabel(appLang("Opened"));
		lblOpened.setHorizontalAlignment(SwingConstants.TRAILING);
		lblOpened.setBounds(175, 22, 70, 14);
		add(lblOpened);

		JLabel lblClosed = new JotyLabel(appLang("Closed"));
		lblClosed.setHorizontalAlignment(SwingConstants.TRAILING);
		lblClosed.setBounds(175, 52, 70, 14);
		add(lblClosed);

		JotyTextField textField_2 = Factory.createLongNum(this, "number", "number", 3);
		textField_2.setBounds(119, 19, 46, 20);
		add(textField_2);
		setReadOnly("number");

		JLabel lblNumber = new JotyLabel(appLang("Number"));
		lblNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNumber.setBounds(44, 22, 70, 14);
		add(lblNumber);

		TextArea textArea = Factory.createTextArea(this, "note", "note", 50);
		textArea.setBounds(9, 84, 310, 149);
		add(textArea);

		m_buildDetailsHandler = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				return DataAccessDialog.getInstance("CCardDetailsDialog", callContext(), null);
			}

			@Override
			public String identifierFromCaller() {
				return "N. " + term("number").integerVal() + " - " + getDialog().getTitle();
			}

		};

		m_currentButtonBehavior = ButtonBehavior.notEditingIdentified;

		if (!m_specific) {
			m_btnOpenDetails = new JotyButton(appLang("OpenDetails"));
			m_btnOpenDetails.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContextParam("clinicalCardID", integerKeyElemVal("ID"));
					setContextParam("cardNumber", term("number").integerVal());
					openDetail(null, null);
				}
			});
			m_btnOpenDetails.setBounds(208, 244, 111, 23);
			add(m_btnOpenDetails);
		}

		JLabel lblNotes = new JotyLabel(appLang("Note"));
		lblNotes.setBounds(9, 66, 132, 14);
		add(lblNotes);

		addIntegerKeyElem("ID", true, true);
		if (!m_specific)
			defRelationElement("patient_id", "id", true);

		addTermToGrid("number", appLang("Number"));

		enableRole("Doctors", m_specific ? Permission.readWrite : Permission.all);
		enableRole("Practitioners", Permission.read);
		enableRole("Administrative personnel", Permission.read);
		enableRole("Schedulers", Permission.read);
	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (m_specific)
			getGridManager().setSelectionOnKeyVal(Long.parseLong(contextParameter("clinicalCardID")));
		return retVal;
	}

	private boolean setCardNumber(String patientId) {
		BasicPostStatement postStatement = accessMethodPostStatement("setCardNumber", 1, -1);
		postStatement.addItem("patientId", patientId, Item._long);
		return invokeAccessMethod(postStatement);
	}

	@Override
	protected boolean storeData() throws JotyException {
		boolean retVal = true;
		if (m_isNewRec) {
			setTermAsReturnedValue(term("number"));
			retVal = setCardNumber(contextParameter("patientId"));
		}
		return retVal && super.storeData();
	}
}
