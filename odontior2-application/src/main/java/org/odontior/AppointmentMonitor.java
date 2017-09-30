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

import org.joty.data.JotyDate;
import org.joty.workstation.app.Application;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.data.WResultSet;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.Table;

public class AppointmentMonitor {
	class ByRefLong {
		public long value;
	}

	JotyDataBuffer m_sourceBuffer = null;
	long m_sourceDuration;
	boolean m_appointmentCreation;
	Table m_srcTable;
	Application m_app = Application.m_app;

	void addressOrganizer(long locationID, JotyDate day, long dayPartID) {
		((OdontiorApp) m_app).m_organizerHistory.openDialog(locationID, day, dayPartID);
	}

	void addressWaitlist(long appointmentID) {
		DataAccessDialog.tryCreate("WaitlistDialog");
		((WaitlistPanel) m_app.getOpenedDialog("WaitlistDialog").m_currSheet).select(appointmentID);
	}

	public boolean checkExistence(long theInterventionID, boolean insertIn2L, boolean isNewRecord, long oldInterventionId) {
		boolean retval = false, doesExist = false;
		long theLocationID = 0, theDayPartID = 0;
		ByRefLong existingInstanceID = new ByRefLong();
		JotyDate theDay = null;
		boolean alert, doesExist2L = false;
		if (theInterventionID != 0) {
			WResultSet rs = recordRS("A_1", "intervention_ID", theInterventionID);
			if (rs != null) {
				existingInstanceID.value = rs.integerValue("ID");
				doesExist = true;
			}
			if (!doesExist) {
				doesExist2L = existInTab("A_4", "intervention_ID", theInterventionID, existingInstanceID, "appointment_ID");
				if (doesExist2L)
					rs = recordRS("A_1", "ID", existingInstanceID.value);
			}
			if (rs != null) {
				theLocationID = rs.integerValue("location_ID");
				theDayPartID = rs.integerValue("daypart_ID");
				theDay = (JotyDate) rs.dateValue("date");
				rs.integerValue("duration");
				rs.close();
			}
		}
		alert = doesExist;
		if (doesExist && !insertIn2L) {
			alert = isNewRecord;
			if (!isNewRecord)
				alert = oldInterventionId != theInterventionID;
		}
		alert = alert || doesExist2L;
		if (alert) {
			if (existInTab("A_5", "ExtID", existingInstanceID.value, null, null)) {
				Application.warningMsg(m_app.m_common.appLang("ExistAlreadyAppoinIn_W"));
				addressWaitlist(existingInstanceID.value);
			} else {
				Application.warningMsg(m_app.m_common.appLang("ExistAlreadyAppoinIn_O"));
				addressOrganizer(theLocationID, theDay, theDayPartID);
			}
		} else
			retval = true;
		return retval;
	}

	public boolean existInTab(String tabLiteral, String fieldName, long key, ByRefLong instanceID, String retValFieldName) {
		WResultSet rs = recordRS(tabLiteral, fieldName, key);
		boolean retVal = rs != null;
		if (retVal) {
			if (instanceID != null)
				instanceID.value = rs.integerValue(retValFieldName);
			rs.close();
		}
		return retVal;
	}

	WResultSet recordRS(String tabLiteral, String fieldName, long key) {
		WResultSet rs = m_app.openAccessorSubstWResultSet(tabLiteral, WResultSet.selectStmnt("<JOTY_CTX>") + " where " + fieldName + " = " + String.valueOf(key));
		if (rs != null)
			if (rs.isEOF()) {
				rs.close();
				return null;
			} else
				return rs;
		else
			return null;
	}

	public boolean validate(long carriedID) {
		JotyDialog appointmentDialog = Application.m_app.getOpenedDialog("AppointmentDialog", true);
		boolean retVal = appointmentDialog == null || appointmentDialog.close();
		if (retVal) {
			m_srcTable = m_app.m_currDnDjtable.getTable();
			m_appointmentCreation = m_srcTable.m_panel.getDialog() == m_app.getOpenedDialog("CCardDetailsDialog", true);
			m_sourceBuffer = m_srcTable.getBuffer();
			m_sourceDuration = m_sourceBuffer.integerValue(m_appointmentCreation && m_sourceBuffer.integerValue("duration") == 0 ? "dflt_duration" : "duration");
		}
		return retVal && (!m_appointmentCreation || checkExistence(carriedID, false, true, 0));
	}

}

