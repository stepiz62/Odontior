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
import java.beans.Beans;

import javax.swing.*;

import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Panel;

public class GraphCardPanel extends Panel {
	JLabel m_lblTopLeft;
	JLabel m_lblTopRight;
	JLabel m_lblBottomLeft;
	JLabel m_lblBottomRight;
	JButton m_btnQuadrants;

	public GraphCardPanel() { // FOR wbe only
		this(new NavigatorPanel());
	}

	public GraphCardPanel(NavigatorPanel parentClient) {
		super();
		if (Beans.isDesignTime())
			parentClient.m_table.setBounds(10, 11, 5, 71);

		Factory.m_DnDfeatures = false;

		m_btnQuadrants = new JButton("Quadrants");
		m_btnQuadrants.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("org.odontior.QuadrantsNamingDialog");
			}
		});
		m_btnQuadrants.setBounds(82, 327, 95, 23);
		add(m_btnQuadrants);

		JPanel tooth1 = Factory.addAnalogicalRowSelector(parentClient, 9);
		tooth1.setBounds(136, 13, 31, 21);
		add(tooth1);

		JPanel tooth2 = Factory.addAnalogicalRowSelector(parentClient, 10);
		tooth2.setBounds(170, 23, 25, 15);
		add(tooth2);

		JPanel tooth3 = Factory.addAnalogicalRowSelector(parentClient, 11);
		tooth3.setBounds(188, 40, 25, 21);
		add(tooth3);

		JPanel tooth4 = Factory.addAnalogicalRowSelector(parentClient, 12);
		tooth4.setBounds(199, 62, 25, 21);
		add(tooth4);

		JPanel tooth5 = Factory.addAnalogicalRowSelector(parentClient, 13);
		tooth5.setBounds(209, 84, 25, 21);
		add(tooth5);

		JPanel tooth6 = Factory.addAnalogicalRowSelector(parentClient, 14);
		tooth6.setBounds(212, 106, 31, 32);
		add(tooth6);

		JPanel tooth7 = Factory.addAnalogicalRowSelector(parentClient, 15);
		tooth7.setBounds(217, 139, 31, 26);
		add(tooth7);

		JPanel tooth8 = Factory.addAnalogicalRowSelector(parentClient, 16);
		tooth8.setBounds(221, 167, 32, 21);
		add(tooth8);

		JPanel tooth9 = Factory.addAnalogicalRowSelector(parentClient, 24);
		tooth9.setBounds(220, 227, 32, 29);
		add(tooth9);

		JPanel tooth10 = Factory.addAnalogicalRowSelector(parentClient, 23);
		tooth10.setBounds(216, 258, 31, 34);
		add(tooth10);

		JPanel tooth11 = Factory.addAnalogicalRowSelector(parentClient, 22);
		tooth11.setBounds(211, 293, 31, 27);
		add(tooth11);

		JPanel tooth12 = Factory.addAnalogicalRowSelector(parentClient, 21);
		tooth12.setBounds(203, 322, 31, 21);
		add(tooth12);

		JPanel tooth13 = Factory.addAnalogicalRowSelector(parentClient, 20);
		tooth13.setBounds(190, 344, 25, 21);
		add(tooth13);

		JPanel tooth14 = Factory.addAnalogicalRowSelector(parentClient, 19);
		tooth14.setBounds(176, 366, 24, 21);
		add(tooth14);

		JPanel tooth15 = Factory.addAnalogicalRowSelector(parentClient, 18);
		tooth15.setBounds(160, 385, 15, 23);
		add(tooth15);

		JPanel tooth16 = Factory.addAnalogicalRowSelector(parentClient, 17);
		tooth16.setBounds(136, 394, 19, 21);
		add(tooth16);

		JPanel tooth17 = Factory.addAnalogicalRowSelector(parentClient, 25);
		tooth17.setBounds(103, 394, 19, 21);
		add(tooth17);

		JPanel tooth18 = Factory.addAnalogicalRowSelector(parentClient, 26);
		tooth18.setBounds(82, 385, 15, 23);
		add(tooth18);

		JPanel tooth19 = Factory.addAnalogicalRowSelector(parentClient, 27);
		tooth19.setBounds(57, 366, 24, 21);
		add(tooth19);

		JPanel tooth20 = Factory.addAnalogicalRowSelector(parentClient, 28);
		tooth20.setBounds(43, 344, 25, 21);
		add(tooth20);

		JPanel tooth21 = Factory.addAnalogicalRowSelector(parentClient, 29);
		tooth21.setBounds(24, 322, 31, 21);
		add(tooth21);

		JPanel tooth22 = Factory.addAnalogicalRowSelector(parentClient, 30);
		tooth22.setBounds(15, 293, 31, 27);
		add(tooth22);

		JPanel tooth23 = Factory.addAnalogicalRowSelector(parentClient, 31);
		tooth23.setBounds(10, 258, 31, 34);
		add(tooth23);

		JPanel tooth24 = Factory.addAnalogicalRowSelector(parentClient, 32);
		tooth24.setBounds(6, 227, 32, 29);
		add(tooth24);

		JPanel tooth25 = Factory.addAnalogicalRowSelector(parentClient, 8);
		tooth25.setBounds(5, 167, 32, 21);
		add(tooth25);

		JPanel tooth26 = Factory.addAnalogicalRowSelector(parentClient, 7);
		tooth26.setBounds(10, 139, 31, 26);
		add(tooth26);

		JPanel tooth27 = Factory.addAnalogicalRowSelector(parentClient, 6);
		tooth27.setBounds(15, 106, 31, 32);
		add(tooth27);

		JPanel tooth28 = Factory.addAnalogicalRowSelector(parentClient, 5);
		tooth28.setBounds(24, 84, 25, 21);
		add(tooth28);

		JPanel tooth29 = Factory.addAnalogicalRowSelector(parentClient, 4);
		tooth29.setBounds(34, 62, 25, 21);
		add(tooth29);

		JPanel tooth30 = Factory.addAnalogicalRowSelector(parentClient, 3);
		tooth30.setBounds(45, 40, 25, 21);
		add(tooth30);

		JPanel tooth31 = Factory.addAnalogicalRowSelector(parentClient, 2);
		tooth31.setBounds(63, 23, 25, 15);
		add(tooth31);

		JPanel tooth32 = Factory.addAnalogicalRowSelector(parentClient, 1);
		tooth32.setBounds(91, 13, 31, 21);
		add(tooth32);

		JPanel overallMouth = Factory.addAnalogicalRowSelector(parentClient, 33);
		overallMouth.setBounds(17, 197, 226, 21);
		add(overallMouth);

		// child's teeth

		JPanel tooth34 = Factory.addAnalogicalRowSelector(parentClient, 39);
		tooth34.setBounds(136, 100, 15, 15);
		add(tooth34);

		JPanel tooth35 = Factory.addAnalogicalRowSelector(parentClient, 40);
		tooth35.setBounds(150, 116, 15, 15);
		add(tooth35);

		JPanel tooth36 = Factory.addAnalogicalRowSelector(parentClient, 41);
		tooth36.setBounds(157, 134, 15, 15);
		add(tooth36);

		JPanel tooth37 = Factory.addAnalogicalRowSelector(parentClient, 42);
		tooth37.setBounds(161, 150, 15, 15);
		add(tooth37);

		JPanel tooth38 = Factory.addAnalogicalRowSelector(parentClient, 43);
		tooth38.setBounds(164, 168, 15, 15);
		add(tooth38);

		JPanel tooth39 = Factory.addAnalogicalRowSelector(parentClient, 48);
		tooth39.setBounds(164, 230, 15, 15);
		add(tooth39);

		JPanel tooth40 = Factory.addAnalogicalRowSelector(parentClient, 47);
		tooth40.setBounds(161, 248, 15, 15);
		add(tooth40);

		JPanel tooth41 = Factory.addAnalogicalRowSelector(parentClient, 46);
		tooth41.setBounds(157, 266, 15, 15);
		add(tooth41);

		JPanel tooth42 = Factory.addAnalogicalRowSelector(parentClient, 45);
		tooth42.setBounds(150, 284, 15, 15);
		add(tooth42);

		JPanel tooth43 = Factory.addAnalogicalRowSelector(parentClient, 44);
		tooth43.setBounds(136, 301, 15, 15);
		add(tooth43);

		JPanel tooth44 = Factory.addAnalogicalRowSelector(parentClient, 49);
		tooth44.setBounds(107, 301, 15, 15);
		add(tooth44);

		JPanel tooth45 = Factory.addAnalogicalRowSelector(parentClient, 50);
		tooth45.setBounds(93, 284, 15, 15);
		add(tooth45);

		JPanel tooth46 = Factory.addAnalogicalRowSelector(parentClient, 51);
		tooth46.setBounds(86, 266, 15, 15);
		add(tooth46);

		JPanel tooth47 = Factory.addAnalogicalRowSelector(parentClient, 52);
		tooth47.setBounds(82, 248, 15, 15);
		add(tooth47);

		JPanel tooth48 = Factory.addAnalogicalRowSelector(parentClient, 53);
		tooth48.setBounds(79, 230, 15, 15);
		add(tooth48);

		JPanel tooth49 = Factory.addAnalogicalRowSelector(parentClient, 38);
		tooth49.setBounds(79, 168, 15, 15);
		add(tooth49);

		JPanel tooth50 = Factory.addAnalogicalRowSelector(parentClient, 37);
		tooth50.setBounds(82, 150, 15, 15);
		add(tooth50);

		JPanel tooth51 = Factory.addAnalogicalRowSelector(parentClient, 36);
		tooth51.setBounds(86, 134, 15, 15);
		add(tooth51);

		JPanel tooth52 = Factory.addAnalogicalRowSelector(parentClient, 35);
		tooth52.setBounds(93, 116, 15, 15);
		add(tooth52);

		JPanel tooth53 = Factory.addAnalogicalRowSelector(parentClient, 34);
		tooth53.setBounds(107, 100, 15, 15);
		add(tooth53);

		JSeparator separator;
		if (Beans.isDesignTime())
			separator = new JSeparator();
		else
			separator = new JotySeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(128, 2, 5, 425);
		add(separator);

		m_lblTopLeft = new JLabel("TL");
		m_lblTopLeft.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblTopLeft.setBounds(11, 5, 31, 14);
		add(m_lblTopLeft);

		m_lblTopRight = new JLabel("TR");
		m_lblTopRight.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblTopRight.setBounds(218, 5, 31, 14);
		add(m_lblTopRight);

		m_lblBottomLeft = new JLabel("BL");
		m_lblBottomLeft.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblBottomLeft.setBounds(11, 403, 31, 14);
		add(m_lblBottomLeft);

		m_lblBottomRight = new JLabel("BR");
		m_lblBottomRight.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblBottomRight.setBounds(218, 403, 31, 14);
		add(m_lblBottomRight);

	}

	public void init(boolean forNaming) {
		WResultSet rs = m_app.openAccessorSubstWResultSet("D_12", "SELECT * FROM <JOTY_CTX> order by id");
		m_lblTopLeft.setText(rs.stringValue("label"));
		rs.next();
		m_lblTopRight.setText(rs.stringValue("label"));
		rs.next();
		m_lblBottomRight.setText(rs.stringValue("label"));
		rs.next();
		m_lblBottomLeft.setText(rs.stringValue("label"));
		rs.close();
		m_btnQuadrants.setVisible(forNaming);
		if (forNaming) {
			m_btnQuadrants.setToolTipText(appLang("NameQuadrants"));
			m_btnQuadrants.setText("Quadrants");
		}
	}
}
