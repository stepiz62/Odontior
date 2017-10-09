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

import java.awt.Font;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import org.joty.access.Logger;
import org.joty.data.JotyDate;
import org.joty.workstation.gui.*;
import org.joty.workstation.app.Application;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataUpdateTransferHandler;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.GridTerm;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyCalendarPanel;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.QueuedDataTransferHandler;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.Term;
import org.joty.workstation.gui.TermBuffer;
import org.joty.workstation.gui.GridTerm.LinkedAspectUpdater;
import org.joty.workstation.gui.GridTerm.SelectionHandler;
import org.joty.workstation.gui.GridTerm.Selector;
import org.joty.workstation.gui.JotyCalendarPanel.DateActor;

public class OrganizerPanel extends NavigatorPanel {

	class BusyState {
		boolean truthVector[] = new boolean[SLOT_SET_SIZE];

		BusyState() {
			clear();
		}

		void clear() {
			for (int i = 0; i < SLOT_SET_SIZE; i++)
				truthVector[i] = false;
		}

		public void clear(int initDayPArtId, long handlesQty) {
			set(false, initDayPArtId, handlesQty, false);
		}

		int getMaxEndingPartID(int theDayPartID) {
			int i = theDayPartID;
			while (i <= SLOT_SET_SIZE && !truthVector[i - 1])
				i++;
			return i - 1;
		}

		void set(boolean on, long initDayPartId, long slotsQty, boolean render) {
			int index;
			for (int j = 0; j < slotsQty; j++) {
				if (initDayPartId + j > SLOT_SET_SIZE)
					break;
				index = (int) (initDayPartId - 1 + j);
				if (render)
					m_appointmentsBuffer.setCellValue(on ? "|||" : null, index, 1);
				truthVector[index] = on;
			}
		}
	}

	class ByRefInt {
		public int value;
	}

	class DayHour {
		String time;
		String busyState;
		String patientSet;
	}

	class OrganizerTransferHandler extends DataUpdateTransferHandler {

		public OrganizerTransferHandler(int actionAsSource, int actionAsTarget, Table table, boolean moveToDrain, String targetIdField) {
			super(actionAsSource, actionAsTarget, table, moveToDrain, targetIdField);
		}

		@Override
		protected boolean checkImportTransaction() {
			return true;
		}

		@Override
		protected boolean importAction(String carried_val, String identifying_id_val, Transferable transferable, boolean carriedValDelayed) {
			boolean retVal;
			JotyDataBuffer srcBuffer = m_appointmentMonitor.m_sourceBuffer;
			if (m_appointmentMonitor.m_appointmentCreation) {
				retVal = m_app.executeSQL(String.format("Insert into <JOTY_CTX> (date, location_id, daypart_id, patient_id, intervention_id, duration, macro %s)", 
														m_common.m_shared ? ", sharingKey" : "") + 
										String.format("values(%s, %d, %s, %d, %s, %d, %d %s)", 
														m_day.render(true, false), 
														integerKeyElemVal("id"), 
														identifying_id_val, 
														srcBuffer.integerValue("patient_id"), 
														carried_val, 
														m_appointmentMonitor.m_sourceDuration,
														0, 
														m_common.m_shared ? (", '" + m_common.m_sharingKey + "'") : ""), 
										null, 
										m_app.createLiteralSubstPostStatement("A_1"));
				if (retVal)
					retVal = ((OdontiorApp) m_app).accessInterventionChecker(Long.parseLong(carried_val), true);
			} else {
				retVal = m_app.executeSQL(	String.format("Update <JOTY_CTX> set date = %s, location_id = %d, daypart_id = %s Where id = %s", 
																m_day.render(true, false), 
																integerKeyElemVal("id"), identifying_id_val, carried_val),
																null, 
																m_app.createLiteralSubstPostStatement("A_1"));
			}
			if (retVal)
				retVal = ((OdontiorApp) m_app).accessSemanticsChecker(m_day, Long.parseLong(identifying_id_val), integerKeyElemVal("id"), 
						srcBuffer.integerValue("patient_id"), srcBuffer.integerValue("practitioner"), m_appointmentMonitor.m_sourceDuration, true);
			if (retVal && ! m_appointmentMonitor.m_appointmentCreation)  {
				QueuedDataTransferHandler srcTransferHandler = (QueuedDataTransferHandler) m_app.m_currDnDjtable.getTransferHandler();
				try {
					srcTransferHandler.setSuccess();
					srcTransferHandler.manageExportActions(transferable, true, true);
					retVal = srcTransferHandler.getSuccess();
				} catch (Exception e) {
					retVal = false;
					Logger.exceptionToHostLog(e);
				}
			}
			return retVal;
		}

		@Override
		protected boolean updateAction(String carried_val, String identifying_id_val) {
			return true;
		}

		@Override
		protected boolean validate(long carriedID, TransferSupport support) {
			checkPublishers();
			return m_appointmentMonitor.validate(carriedID) && checkForTimeStorage((int) targetIdentifyingID(support), m_appointmentMonitor.m_sourceDuration, 0);
		}

	}

	static final int HOURS_A_DAY = 24;

	static final int SLOT_MINUTES = 5;

	static final int SLOT_SET_SIZE = 288;
	static final int SLOTS_AN_HOUR = 12;
	BusyState m_busyState = new BusyState();
	int m_maxEndingPartID;
	int m_startUpTime = 5;
	private int m_dayTimeID;
	private int m_dayTimeSize;
	private boolean m_hourswitching;
	private JotyDate m_day;
	private JLabel m_lblCurrentDay;
	TermBuffer m_dayHoursBuffer, m_appointmentsBuffer;
	JButton m_btnBack;
	OdontiorApp m_app;
	Vector<DayHour> m_dayHours;
	private int m_hour;
	private int m_preferredHour;
	HashMap<String, Integer> m_preferences;

	JotyCalendarPanel m_calendar;

	private JLabel m_lblCurrentDow;

	private AppointmentMonitor m_appointmentMonitor = new AppointmentMonitor();

	public OrganizerPanel() {
		m_table.setBounds(726, 11, 11, 23);
		m_dayHours = new Vector<DayHour>(); /* dayHours Table-model must see this Vector */
		m_dayHours.setSize(HOURS_A_DAY);

		Factory.addTextToGrid(this, null, 1, null);

		Table dayHours = Factory.createTable(this, "dayHours");
		dayHours.setBounds(7, 64, 221, 380);
		add(dayHours);
		TableTerm dayHoursTerm = tableTerm("dayHours");
		dayHoursTerm.addField(null, appLang("Time"));
		dayHoursTerm.addField(null, null);
		dayHoursTerm.addField(null, appLang("Patient_s"));
		dayHours.m_colWidths = new int[] { 45, 15, 190 };
		dayHours.setAllColsAlignement(SwingConstants.CENTER);
		dayHours.m_sortClickDenied = true;
		dayHoursTerm.setNotClearable();
		enabledAsDetail("dayHours");
		dayHoursTerm.setToolTipText(null);
		dayHoursTerm.m_selectionHandler = new SelectionHandler() {
			@Override
			public void selChange(GridTerm term) {
				int selection = term.getSelection();
				if (selection > -1) {
					m_hour = selection;
					hourSelectionEffects();
				}
			}
		};
		dayHoursTerm.m_selector = new Selector() {
			@Override
			public void select(GridTerm term) {
				if (m_hour >= 0 && m_initializing)
					term.setSelection(m_hour, false);
			}
		};
		dayHoursTerm.m_actionOnRowHandler = new ActionOnRowInterface() {
			@Override
			public void doAction(Term srcTerm, int column) {
				m_preferredHour = m_hour;
			}
		};
		((Table) dayHoursTerm.getComponent()).setCustomRowHeight(32);
		dayHoursTerm.setAsDbFree();

		Table appointments = Factory.createTable(this, "appointments");
		appointments.setBounds(235, 184, 502, 260);
		add(appointments);
		TableTerm term = tableTerm("appointments");
		term.setKeyName("id");
		term.addField("timelabel", appLang("Time"));
		term.addField(null, null);
		term.addField("firstname", appLang("FirstName"));
		term.addField("lastname", appLang("LastName"));
		term.addField("treatment", appLang("Treatment"));
		term.addField("tooth", appLang("Tooth"));
		term.addField("duration", appLang("Duration"));
		term.addField("practitioner", appLang("Practitioner"));
		term.addFieldAsFlag("macro", appLang("Macro"));
		term.addFieldAsFlag("done", appLang("Done"));
		appointments.m_colWidths = new int[] { 55, 15, 130, 130, 160, 70, 90, 150, 15, 15 };
		appointments.setAllColsAlignement(SwingConstants.CENTER);
		term.m_buildDetailsHandler = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				if (m_appointmentsBuffer.getCellValue(1) != null && m_appointmentsBuffer.strValue("lastname") == null) {
					m_app.warningMsg(m_common.appLang("TimeSlotEngaged"));
					return null;
				} else {
					setContextParam("locationID", integerKeyElemVal("id"));
					setContextParam("date", m_day);
					setContextParam("dayPartID", term.m_dataBuffer.integerValue("id"));
					return DataAccessDialog.getInstance("AppointmentDialog", callContext(), AppointmentDialog.QueryType.contextSpecific);
				}
			}
		};
		enabledAsDetail("appointments");
		appointments.addToolTipRow(Application.m_common.appLang("DragToWaitlist"));
		term.m_linkedAspectUpdater = new LinkedAspectUpdater() {
			@Override
			public void update() {
				processTimeGraphics();
			}
		};
		appointments.m_sortClickDenied = true;
		term.setAsPublisher();
		term.subscribe("PatientDialog");
		JotyTextField chairName = Factory.createText(this, "chairName", "name", 32);
		chairName.setBounds(75, 4, 375, 25);
		add(chairName);
		chairName.setAsViewer();
		chairName.setFont(new Font("Tahoma", Font.BOLD, 20));

		JotyTextField chairDescr = Factory.createText(this, "chairDescr", "description", 64);
		chairDescr.setBounds(75, 30, 375, 25);
		add(chairDescr);
		chairDescr.setAsViewer();
		chairDescr.setFont(new Font("Tahoma", Font.PLAIN, 14));

		m_lblCurrentDay = new JotyLabel("New label");
		m_lblCurrentDay.setFont(new Font("Tahoma", Font.BOLD, 26));
		m_lblCurrentDay.setBounds(238, 136, 257, 34);
		add(m_lblCurrentDay);

		JotyTextField gotoDateBox = Factory.createDate(this, "gotoDateBox", null);
		gotoDateBox.setBounds(667, 90, 70, 20);
		add(gotoDateBox);
		setRemainEnabled("gotoDateBox");

		JButton btnGoToDate = new JotyButton(appLang("GoTo"));
		btnGoToDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textTerm("gotoDateBox").m_dateVal.m_isNull) {
					gotoDate((JotyDate) textTerm("gotoDateBox").m_dateVal);
				}
			}
		});
		btnGoToDate.setBounds(667, 66, 70, 23);
		add(btnGoToDate);

		m_btnBack = new JotyButton(appLang("Back"));
		m_btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_app.m_organizerHistory.back();
			}
		});
		m_btnBack.setBounds(667, 150, 70, 23);
		add(m_btnBack);

		if (!Beans.isDesignTime())
			m_app = (OdontiorApp) Application.m_app;

		m_calendar = new JotyCalendarPanel();
		m_calendar.setBounds(504, 30, 148, 143);
		add(m_calendar);
		if (!Beans.isDesignTime())
			m_calendar.setActor(new DateActor() {
				@Override
				public void action() {
					m_day.setTime(m_calendar.m_calendarDate.getTime().getTime());
					processDate();
				}
			});

		m_lblCurrentDow = new JotyLabel((String) null);
		m_lblCurrentDow.setFont(new Font("Tahoma", Font.BOLD, 26));
		m_lblCurrentDow.setBounds(238, 96, 257, 34);
		add(m_lblCurrentDow);

		JButton btnToday = new JotyButton(appLang("Today"));
		btnToday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_calendar.setDate(new JotyDate(m_app, "now"));
			}
		});
		btnToday.setBounds(430, 66, 65, 23);
		add(btnToday);

		JLabel logo = new JLabel();
		logo.setIcon(Beans.isDesignTime() ? null : m_app.m_appLogo2);
		logo.setBounds(9, 0, 64, 64);
		add(logo);

		m_appointmentsBuffer = (TermBuffer) termBuffer("appointments");
		m_dayHoursBuffer = (TermBuffer) termBuffer("dayHours");

		addIntegerKeyElem("id", true, true);

		setNavigationGridHidden();

		enableRole("Schedulers", Permission.all);
		enableRole("Practitioners", Permission.read);
		enableRole("Administrative personnel", Permission.read);

		m_day = new JotyDate(m_app, "now");
		setContextParam("day", m_day);
		if (!Beans.isDesignTime())
			new OrganizerTransferHandler(TransferHandler.MOVE, TransferHandler.COPY_OR_MOVE, appointments, false, "appointment_id");
	}

	@Override
	protected void checkForInitSelection() {
		m_inhibitChangeNotification = true;
		if (m_preferences.get("locationId") != 0)
			m_gridManager.setSelectionOnKeyVal(m_preferences.get("locationId"));
		else
			super.checkForInitSelection();
		m_inhibitChangeNotification = false;
	}

	public boolean checkForTimeStorage(int initialPartID, long theDuration, long oldDuration) {
		boolean retval;
		ByRefInt theHighestPartID = new ByRefInt();
		theHighestPartID.value = initialPartID;
		retval = processDuration(theHighestPartID, theDuration, true, oldDuration);
		if (!retval)
			m_app.warningMsg(m_common.appLang("DurationExceeds"));
		return retval;
	}

	int dayHourTimeId() {
		return (m_dayTimeID - 1) * SLOTS_AN_HOUR + 1;
	}

	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		m_btnBack.setEnabled(!bState && !m_app.m_organizerHistory.isEmpty());
	}

	public JotyDate getDay() {
		return m_day;
	}

	public long getDayPartID() {
		return tableTerm("appointments").m_dataBuffer.integerValue("ID");
	}

	public long getLocationId() {
		return idFromGridRow();
	}

	public void gotoDate(JotyDate date) {
		m_calendar.setDate(date);
	}

	long handlesQty(long duration) {
		duration += m_startUpTime;
		long qty = (duration / SLOT_MINUTES) + ((duration % SLOT_MINUTES) > 0 ? 1 : 0);
		// to deal with zero duration (not specified appointment) 
		return qty <= 0 ? 1 : qty;
	}

	private void hourSelectionEffects() {
		Table table = (Table) tableTerm("appointments").getComponent();
		int pos = SLOTS_AN_HOUR * m_hour;
		int upperPos = pos + 14;
		if (!m_initializing)
			table.ensureIndexIsVisible(upperPos < SLOT_SET_SIZE ? upperPos : SLOT_SET_SIZE - 1);
		table.ensureIndexIsVisible(pos);
		tableTerm("dayHours").setSelection(-1, false);
	}

	@Override
	public boolean init() {

		m_preferences = m_app.m_organizerPreferences;
		m_dayHoursBuffer.initWithEmptyRecords(HOURS_A_DAY);
		for (int i = 0; i < HOURS_A_DAY; i++)
			m_dayHoursBuffer.setCellValue(String.valueOf(i), i, 0);
		tableTerm("dayHours").bufferRender();
		m_preferredHour = m_preferences.get("hour");
		m_hour = m_preferredHour;
		m_calendar.setDate(m_day);
		boolean retVal = super.init();
		if (retVal && !documentIdentified()) {
			m_app.warningMsg(m_common.appLang("NoArmchairDefined"));
			retVal = false;
		}
		return retVal;
	}

	void locateHour(long timeID) {
		long hourID;
		timeID--;
		hourID = (timeID - timeID % SLOTS_AN_HOUR) / SLOTS_AN_HOUR + 1;
		if (hourID >= m_dayTimeID && hourID < m_dayTimeID + m_dayTimeSize) {} else {
			if (hourID < m_dayTimeID) {} else {}
		}
	}

	void processDate() {
		textTerm("gotoDateBox").killFocus();
		renderDate();
		if (integerKeyElemVal("id") > 0 && !m_initializing)
			refresh();
	}

	boolean processDuration(ByRefInt initDayPartID, long theDuration, boolean editing, long oldDuration) {
		boolean retval = false;
		long totalDuration;
		boolean doIt = true;
		boolean datumExists = !m_appointmentsBuffer.isNull("lastname", initDayPartID.value - 1);
		if (editing) {
			totalDuration = theDuration;
		} else {
			long bufferDuration = m_appointmentsBuffer.longValue("duration", initDayPartID.value - 1);
			doIt = datumExists;
			totalDuration = bufferDuration;
		}
		if (doIt) {
			long oldQty = 0;
			if (editing && datumExists) {
				oldQty = handlesQty(oldDuration);
				m_busyState.clear(initDayPartID.value, oldQty);
			}
			if (editing)
				m_maxEndingPartID = m_busyState.getMaxEndingPartID(initDayPartID.value);
			long qty = handlesQty(totalDuration);
			if (editing) {
				retval = initDayPartID.value - 1 + qty <= m_maxEndingPartID;
				if (retval && datumExists)
					m_busyState.set(true, initDayPartID.value, oldQty, false);
			} else {
				if (qty > 0) {
					m_busyState.set(true, initDayPartID.value, qty, true);
					initDayPartID.value += qty;
				}
				retval = true;
			}
		}
		return retval;
	}

	void processTimeGraphics() {
		String hourNames, firstName;
		boolean beginBusy, endBusy, someBusy, someFree;
		ByRefInt byRefInt = new ByRefInt();
		byRefInt.value = 1;
		m_busyState.clear();
		while (byRefInt.value <= SLOT_SET_SIZE) {
			m_appointmentsBuffer.setCellValue(null, byRefInt.value - 1, 1);
			if (!processDuration(byRefInt, 0, false, 0)) {
				byRefInt.value++;
				if (byRefInt.value > SLOT_SET_SIZE)
					break;
			}
		}
		((Table) tableTerm("appointments").getComponent()).newDataAvailable();

		int i, j, posIdx;
		if (!m_hourswitching) {
			i = 1;
			while (i <= HOURS_A_DAY) {
				hourNames = "";
				someBusy = false;
				someFree = false;
				posIdx = (i - 1) * SLOTS_AN_HOUR;
				beginBusy = m_appointmentsBuffer.getCellValue(posIdx, 1) != null;
				endBusy = m_appointmentsBuffer.getCellValue(posIdx + SLOTS_AN_HOUR - 1, 1) != null;
				for (j = 1; j <= SLOTS_AN_HOUR; j++) {
					if (!m_appointmentsBuffer.isNull("duration", posIdx + j - 1)) {
						if (hourNames.length() > 0)
							hourNames += " + ";
						firstName = m_appointmentsBuffer.strValue("firstname", posIdx + j - 1);
						if (firstName.length() > 0)
							firstName = firstName.charAt(0) + ".";
						hourNames += m_appointmentsBuffer.strValue("lastname", posIdx + j - 1) + " " + firstName;
					}
					if (m_appointmentsBuffer.getCellValue(posIdx + j - 1, 1) != null)
						someBusy = true;
					else
						someFree = true;
				}
				m_dayHoursBuffer.setCellValue(someFree ? (someBusy ? (beginBusy ? (endBusy ? "<" : "/") : (endBusy ? "\\" : ">")) : " ") : "|", i - 1, 1);
				m_dayHoursBuffer.setCellValue(hourNames, i - 1, 2);
				i++;
			}
			tableTerm("dayHours").termRender();
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		processTimeGraphics();
	}

	void renderDate() {
		m_lblCurrentDay.setText(m_day.render(false, false));
		m_lblCurrentDow.setText(m_calendar.dow());
	}

	@Override
	protected void setContextParams() {
		super.setContextParams();
		setContextParam("LocationID", keyElem("id").integerVal());
	}

	@Override
	protected void statusChangeProc() {
		m_inhibitGridTermsEffect = true;
		super.statusChangeProc();
		m_inhibitGridTermsEffect = false;
		if (!m_initializing)
			refresh();
	}

	public void storePreferences() {
		m_preferences.clear();
		m_preferences.put("hour", m_preferredHour);
		m_preferences.put("locationId", (int) keyElem("id").integerVal());
	}

	@Override
	public void costraintViolationMsgOnUpdate() {
		if (! ((OdontiorApp) m_app).organizerCostraintViolationMsgOnUpdate())
			super.costraintViolationMsgOnUpdate();
	}
	
}
