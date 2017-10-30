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

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joty.data.JotyDate;
import org.joty.data.WrappedField;
import org.joty.workstation.gui.*;
import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.ImageComponent;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;

public class ImagePanel extends NavigatorPanel {
	JotyButton m_btnOpenAllAs;
	ImageComponent m_imageComponent;
	boolean m_cardUnified;

	public ImagePanel() {

		m_table.setBounds(7, 5, 303, 227);

		JotyTextField jotyTextField = Factory.createDateTime(this, "timestamp", "timestamp");
		jotyTextField.setBounds(7, 260, 122, 20);
		add(jotyTextField);
		term("timestamp").setMandatory();
		term("timestamp").defaultValue().setValue(new JotyDate(m_app, "now"));

		m_imageComponent = Factory.createImageComponent(this, "image", "image", "imagepreview", "jpg", "jpg files");
		m_imageComponent.setBounds(225, 260, 87, 85);
		add(m_imageComponent);

		JLabel lblTimestamp = new JotyLabel(appLang("Timestamp"));
		lblTimestamp.setBounds(7, 243, 122, 14);
		add(lblTimestamp);

		JLabel lblImage = new JotyLabel(appLang("Image"));
		lblImage.setBounds(225, 243, 85, 14);
		add(lblImage);

		addIntegerKeyElem("ID", true, true);

		enableRole("Doctors", Permission.all);
		enableRole("Practitioners", Permission.read);

		addTermToGrid("timestamp", appLang("Timestamp"));
		addTermToGrid("image", appLang("Image"));
		m_cardUnified = contextParameter("UniqueCcard").compareTo("1") == 0;
		if (m_cardUnified)
			Factory.addNumToGrid(this, "number", appLang("CardN"));
		gridCellDescriptor("image").setIdentityRenderer(new WrappedField.IdentityRenderer() {
			@Override
			public String render() {
				return textTerm("timestamp").toString();
			}
		});

		setModifyOnly(contextParameter("UniqueCcard").compareTo("1") == 0);

		m_currentButtonBehavior = ButtonBehavior.notEditing;

		m_btnOpenAllAs = new JotyButton(appLang("OpenAllAsTiles"));
		m_btnOpenAllAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageTerm("image").setActorButton(m_btnOpenAllAs);
				imageTerm("image").openAllAsTiles();
			}
		});
		m_btnOpenAllAs.setBounds(7, 322, 170, 23);
		add(m_btnOpenAllAs);

	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = m_cardUnified ? new int[] { 100, 40, 15 } : new int[] { 100, 30 };
		table.setAllColsAlignement(SwingConstants.CENTER);
		table.setCustomRowHeight(ImageComponent.previewHeight);
	}

	@Override
	protected void setRelatedFields(WResultSet rs) {
		rs.setIntegerValue("tooth_id", contextParamLong("toothID"));
		rs.setIntegerValue("clinicalcard_id", contextParamLong("clinicalcardID"));
		super.setRelatedFields(rs);
	}
}
