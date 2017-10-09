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

import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.Factory;

public class AppointmentDialog extends DataAccessDialog {
	public enum QueryType {
		contextSpecific, undefined
	}

	int m_dataActionCounter = 0;

	public AppointmentDialog(Object callContext, Object openingMode) {
		super(callContext, openingMode);

		m_accessorMode = true;

		m_btnClose.setLocation(487, 5);
		m_btnHome.setLocation(453, 5);
		m_buttonPane.setBounds(2, 384, 525, 37);
		m_contentPanel.setBounds(2, 2, 525, 383);
		setBounds(10, 19, 534, 450);
		DataAccessPanel dataAccessPanel = Factory.addDataAccessPanel(m_contentPanel, new AppointmentPanel());
		dataAccessPanel.setBounds(1, 1, 541, 381);
		m_contentPanel.add(dataAccessPanel);
		getContentPane().setLayout(null);
	}

	void checkToRefreshOrganizer() {
		m_dataActionCounter++;
		if (m_dataActionCounter > 1) /* due to the storage calculation in  Appointments buffer there located */
			m_app.getOpenedDialog("OrganizerDialog").m_currSheet.checkPublishers();
	}

	@Override
	protected boolean dataCreationEnabled() {
		return isUndefined() ? false : super.dataCreationEnabled();
	}

	public boolean isUndefined() {
		return ((QueryType) getMode()) == QueryType.undefined;
	}

	@Override
	protected boolean save() {
		if (!isUndefined())
			checkToRefreshOrganizer();
		return super.save();
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("Appointment");
	}

}
