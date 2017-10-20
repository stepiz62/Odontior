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

import java.awt.datatransfer.Transferable;

import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import org.joty.common.JotyTypes;
import org.joty.common.BasicPostStatement;
import org.joty.workstation.gui.*;
import org.joty.workstation.app.Application;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.QueuedDataTransferHandler;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.Term;

public class WaitlistPanel extends NavigatorPanel {
	class WaitlistTransferHandler extends QueuedDataTransferHandler {
		long m_autoIdVal;

		public WaitlistTransferHandler(int actionAsSource, int actionAsTarget, Table table, boolean moveToDrain) {
			super(actionAsSource, actionAsTarget, table, moveToDrain);
		}

		@Override
		protected boolean checkExportTransaction() {
			return false;
		}

		@Override
		protected void completeImportTransaction(long dndID, boolean internalMove, boolean delayedDndId) {
			super.completeImportTransaction(m_appointmentMonitor.m_appointmentCreation ? 
												m_autoIdVal : 
												dndID, 
											internalMove, 
											m_appointmentMonitor.m_appointmentCreation && m_app.m_webMode);
			if (getSuccess() && m_app.m_webMode) {
				String returnedValue = m_app.m_webClient.getReturnedValue(0);
				m_autoIdVal = returnedValue == null ? 0 : Long.parseLong(returnedValue);
			}
		}

		@Override
		protected int getRowToBeSelected(long carriedID, Long identifyingID) {
			return super.getRowToBeSelected(m_appointmentMonitor.m_appointmentCreation ? m_autoIdVal : carriedID, identifyingID);
		}

		@Override
		protected boolean importAction(String carried_val, String identifying_id_val, Transferable transferable, boolean carriedValDelayed) {
			Application app = Application.m_app;
			boolean retVal = true;
			m_autoIdVal = 0;
			if (m_appointmentMonitor.m_appointmentCreation) {
				String sqlStmnt = String.format("Insert into <JOTY_CTX> (patient_id, intervention_id, duration, macro %s) ", m_common.m_shared ? ", sharingKey" : "") + 
									String.format("values(%d, %s, %d, %d %s)", 
											m_appointmentMonitor.m_sourceBuffer.integerValue("patient_id"), 
											carried_val, 
											m_appointmentMonitor.m_sourceDuration, 
											0, 
											m_common.m_shared ? (", '" + m_common.m_sharingKey + "'") : "");
				BasicPostStatement substPostStatement = m_app.createLiteralSubstPostStatement("A_1");
				if (app.m_webMode)
					retVal = app.m_webClient.manageCommand(sqlStmnt, true, "id", substPostStatement);
				else {
					retVal = app.m_db.executeReturningStmnt(sqlStmnt, "id", substPostStatement);
					if (retVal)
						m_autoIdVal = app.m_db.getAutoIdVal();
				}
				if (retVal)
					retVal = ((OdontiorApp) m_app).accessInterventionChecker(Long.parseLong(carried_val), true);
			}
			if (retVal)
				retVal = super.importAction((m_appointmentMonitor.m_appointmentCreation ? 
						String.valueOf(m_autoIdVal) : carried_val), identifying_id_val, transferable, m_appointmentMonitor.m_appointmentCreation && app.m_webMode);
			if (retVal && !m_appointmentMonitor.m_appointmentCreation) {
				retVal = m_app.executeSQL("Update <JOTY_CTX> set date = null, daypart_id = null, location_id = null Where id = " + carried_val, null, m_app.createLiteralSubstPostStatement("A_1"));
				if (retVal) {
					JotyDataBuffer srcBuffer = m_appointmentMonitor.m_sourceBuffer;
					OrganizerPanel src = (OrganizerPanel) m_app.getOpenedDialog("OrganizerDialog").m_currSheet;
					long practitionerId = srcBuffer.integerValue("practitioner");
					retVal = ((OdontiorApp) m_app).accessSemanticsChecker(src.getDay(), src.getDayPartID(), src.getLocationId(), 
							srcBuffer.integerValue("patient_id"), practitionerId , m_appointmentMonitor.m_sourceDuration, false);
				}
			}
			return retVal;
		}

		@Override
		public void manageExportActions(Transferable transferable, boolean transaction, boolean foreignCall) throws Exception {
			if (foreignCall)
				super.manageExportActions(transferable, transaction, foreignCall);
		}

		@Override
		protected boolean validate(long carriedID, TransferSupport support) {
			checkPublishers();
			return m_appointmentMonitor.validate(carriedID);
		}

	}

	private AppointmentMonitor m_appointmentMonitor = new AppointmentMonitor();

	public WaitlistPanel() {
		m_table.setBounds(10, 10, 502, 289);

		Factory.addNumToGrid(this, null, null);
		Factory.addTextToGrid(this, "firstname", appLang("FirstName"));
		Factory.addTextToGrid(this, "lastname", appLang("LastName"));
		Factory.addTextToGrid(this, "treatment", appLang("Treatment"));
		Factory.addTextToGrid(this, "tooth", appLang("Tooth"));
		Factory.addLongNumToGrid(this, "duration", appLang("Duration"));
		Factory.addTextToGrid(this, "practitioner", appLang("Practitioner"));
		Factory.addCheckToGrid(this, "macro", appLang("Macro"));
		addField("patient_id", JotyTypes._long);
//		addFieldToGrid("patient_id");
		
		m_actionOnRowHandler = new ActionOnRowInterface() {

			@Override
			public void doAction(Term srcTerm, int column) {
				setContextParam("appointmentID", integerKeyElemVal("ExtID"));
				DataAccessDialog.tryCreate("AppointmentDialog", callContext(), AppointmentDialog.QueryType.undefined);
			}

		};
		subscribe("AppointmentDialog");

		addIntegerKeyElem("ExtID", true, true);

		enableRole("Schedulers", Permission.all);
		enableRole("Practitioners", Permission.read);
		enableRole("Administrative personnel", Permission.read);
		setRowsQueuing(0, "ExtID", "PreviousID", "NextID", new WaitlistTransferHandler(
								TransferHandler.MOVE, TransferHandler.COPY_OR_MOVE, (Table) getGridManager().getListComponent(), false));
		setFirstColAsPositioner();
		setAsPublisher();
		subscribe("PatientDialog");
	}

	void select(long appointmentID) {
		getGridManager().setSelectionOnKeyVal(appointmentID);
	}

	@Override
	protected void setGridFormat(Table table) {
		table.m_colWidths = new int[] { 25, 80, 80, 120, 40, 40, 120, 20 };
		table.setAllColsAlignement(SwingConstants.CENTER);
		table.addToolTipRow(m_app.m_common.appLang("EditRecord"));
		table.addToolTipRow(m_app.m_common.appLang("DragToOrganizer"));
	}

	@Override
	public void costraintViolationMsgOnUpdate() {
		if (! ((OdontiorApp) m_app).organizerCostraintViolationMsgOnUpdate())
			super.costraintViolationMsgOnUpdate();
	}
	

}
