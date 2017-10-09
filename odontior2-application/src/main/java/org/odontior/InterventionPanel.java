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
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joty.workstation.gui.*;
import org.joty.data.JotyDate;
import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.DataScrollingPanel;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TextArea;
import org.odontior.InterventionDialog.QueryType;

public class InterventionPanel extends DataScrollingPanel {
	boolean m_nCard;

	public InterventionPanel() {
		super(true);
		JotyTextField diagnosisTime = Factory.createDateTime(this, "diagnosisTime", "timestamp");
		diagnosisTime.setBounds(360, 105, 120, 20);
		add(diagnosisTime);
		term("diagnosisTime").defaultValue().setValue(new JotyDate(m_app, "now"));
		term("diagnosisTime").setMandatory();

		JotyTextField executionTime = Factory.createDateTime(this, "executionTime", "execution");
		executionTime.setBounds(360, 139, 120, 20);
		add(executionTime);

		JLabel lblExecutionTime = new JotyLabel(appLang("ExecutionTime"));
		lblExecutionTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExecutionTime.setBounds(207, 141, 151, 14);
		add(lblExecutionTime);

		JLabel lblDiagnosisTime = new JotyLabel(appLang("DiagnosisTime"));
		lblDiagnosisTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDiagnosisTime.setBounds(207, 107, 151, 14);
		add(lblDiagnosisTime);

		TextArea note = Factory.createTextArea(this, "note", "note", 255);
		note.setBounds(11, 187, 470, 89);
		add(note);

		JLabel lblNote = new JotyLabel(appLang("Note"));
		lblNote.setBounds(12, 172, 125, 14);
		add(lblNote);

		ComboBox tooth = Factory.createDescrRenderer(this, "tooth", "tooth_id", "teeth", "toothID", false);
		tooth.setLocation(406, 15);
		tooth.setSize(20, 0);
		add(tooth);

		JLabel lblTreatment = new JotyLabel(appLang("Treatment"));
		lblTreatment.setBounds(12, 34, 120, 14);
		add(lblTreatment);

		ComboBox opSymbol = Factory.createDescrRenderer(this, "opSymbol", "treatment_id", "treatmentSymbols", false);
		term("opSymbol").setMandatory();
		opSymbol.setBounds(13, 58, 102, 20);
		add(opSymbol);

		ComboBox opDescr = Factory.createDescrRenderer(this, "opDescr", null, "treatmentDescriptions", true);
		opDescr.setBounds(115, 58, 372, 20);
		add(opDescr);

		JLabel lblContext = new JotyLabel(appLang("Context"));
		lblContext.setHorizontalAlignment(SwingConstants.RIGHT);
		lblContext.setVerticalAlignment(SwingConstants.BOTTOM);
		lblContext.setBounds(309, 18, 94, 14);
		add(lblContext);

		JotyTextField duration = Factory.createLongNum(this, "duration", "duration", 3);
		duration.setBounds(153, 105, 46, 20);
		add(duration);

		JLabel lblDuration = new JotyLabel(appLang("Duration"));
		lblDuration.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDuration.setBounds(55, 107, 94, 14);
		add(lblDuration);

		m_currentButtonBehavior = ButtonBehavior.editing;
		JButton btnBrowse = new JotyButton(appLang("Browse"), term("opSymbol"));
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acquireSelectedValueFrom("TreatmentDialog", "opSymbol", new String[] { "duration", "price" }, new String[] { "dflt_duration", "dflt_price" }, null, null, true);
				synchroCombo("opSymbol", "opDescr");
			}
		});
		btnBrowse.setBounds(137, 26, 74, 23);
		add(btnBrowse);

		addIntegerKeyElem("ID", true, true);

		addTermToGrid("diagnosisTime", appLang("DiagnosisTime"));
		addTermToGrid("opSymbol", appLang("Treatment"));
		addTermToGrid("executionTime", appLang("ExecutionTime"));
		addTermToGrid("duration", appLang("Duration"));
		m_nCard = getDialog().getMode() != QueryType.contextSpecific || contextParameter("UniqueCcard").compareTo("1") == 0;
		if (m_nCard)
			Factory.addNumToGrid(this, "number", appLang("CardN"));

		if (openAsSelector())
			addTermToGrid("tooth", appLang("Tooth"));

		setAsPublisher();

		enableRole("Practitioners", Permission.all);
		enableRole("Administrative personnel", Permission.read);

		setModifyOnly(!Beans.isDesignTime() && (((InterventionDialog) getDialog()).isInterventionSpecific() || contextParameter("UniqueCcard").compareTo("1") == 0));

		JotySeparator separator2 = new JotySeparator();
		separator2.setBounds(14, 52, 470, 2);
		add(separator2);

		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 484, 278);
		add(panel);

	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (((InterventionDialog) m_dialog).isInterventionSpecific())
			getGridManager().setSelectionOnKeyVal(Long.parseLong(contextParameter("interventionID")));
		return retVal;
	}

	private boolean openAsSelector() {
		return Beans.isDesignTime() ? true : (m_dialog.getMode() == QueryType.asSelector);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = m_nCard ? (openAsSelector() ? new int[] { 100, 40, 100, 30, 15, 15 } : new int[] { 100, 40, 100, 30, 15 }) : new int[] { 100, 60, 100, 30 };
		table.setAllColsAlignement(SwingConstants.CENTER);
	}

	@Override
	protected void setRelatedFields(WResultSet rs) {
		rs.setIntegerValue("tooth_id", contextParamLong("toothID"));
		rs.setIntegerValue("clinicalcard_id", contextParamLong("clinicalcardID"));
		super.setRelatedFields(rs);
	}

	@Override
	protected void statusChangeProc() {
		super.statusChangeProc();
		synchroCombo("opSymbol", "opDescr");
	}

}
