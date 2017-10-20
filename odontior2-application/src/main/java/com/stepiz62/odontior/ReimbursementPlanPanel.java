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
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.TextArea;

public class ReimbursementPlanPanel extends NavigatorPanel {

	ReimbursementPlanPanel() {
		super();
		m_table.setLocation(10, 11);

		JotyTextField jotyTextField = Factory.createText(this, "name", "name", 48);
		jotyTextField.setBounds(87, 103, 353, 20);
		add(jotyTextField);
		term("name").setMandatory();

		TextArea textArea = Factory.createTextArea(this, "note", "Note", 512);
		textArea.setBounds(11, 142, 429, 73);
		add(textArea);

		JLabel lblName = new JotyLabel(appLang("Name"));
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(10, 106, 73, 14);
		add(lblName);

		JLabel lblNote = new JotyLabel(appLang("Note"));
		lblNote.setBounds(11, 127, 73, 14);
		add(lblNote);

		m_currentButtonBehavior = ButtonBehavior.notEditingIdentified;

		JButton btnDetails = new JotyButton(appLang("Details"));
		btnDetails.addActionListener(new ActionListener() {
			BuildDetailsDialogAdapter adapter = new BuildDetailsDialogAdapter() {
				@Override
				public JotyDialog createDialog(TableTerm GridTerm) {
					setContextParam("reimbursementPlanID", integerKeyElemVal("ID"));
					return DataAccessDialog.getInstance("ReimbursementDialog", callContext());
				}

				@Override
				public String identifierFromCaller() {
					return term("name").strVal() + " - " + contextParameter("companyName");
				}
			};

			@Override
			public void actionPerformed(ActionEvent e) {
				openDetail(null, adapter);
			}
		});
		btnDetails.setBounds(349, 227, 91, 23);
		add(btnDetails);

		Factory.createHiddenLong(this, "insurancecompany_id", "companyID");

		addTermToGrid("name", appLang("Name"));
		addTermToGrid("note", appLang("Note"));

		addIntegerKeyElem("ID", true, true);

		enableRole("Administrative personnel", Permission.all);
	}

	@Override
	protected void setGridFormat(Table table) {
		// table.m_colWidths = new int[] {50, 200};
	}

}
