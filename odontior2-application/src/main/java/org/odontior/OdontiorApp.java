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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.joty.common.BasicPostStatement;
import org.joty.common.BasicPostStatement.Item;
import org.joty.data.JotyDate;
import org.joty.workstation.app.Application;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.GridBuffer;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.SearcherMultiPanelDialog;
import org.odontior.common.OdontiorDbManager;
import org.odontior.common.OdontiorDbManager.Subcases;

public class OdontiorApp extends Application {

	public ImageIcon m_appLogo2;
	
	class OrganizerHistory {
		class OrganizerStep {
			long locationID;
			JotyDate day;
			long dayPartID;

			OrganizerStep(long locationID, JotyDate day, long dayPartID) {
				this.locationID = locationID;
				this.day = day;
				this.dayPartID = dayPartID;
			}
		}

		public Stack<OrganizerStep> stack;
		private OrganizerStep current;

		public OrganizerHistory() {
			stack = new Stack<OrganizerStep>();
		}

		void back() {
			if (!isEmpty()) {
				pop();
				go(current.locationID, current.day, current.dayPartID);
			}
		}

		void go(long locationID, JotyDate day, long dayPartID) {
			JotyDialog dlg = DataAccessDialog.getInstance("OrganizerDialog");
			if (dlg != null) {
				dlg.perform();
				if (locationID > 0) {
					dlg.getGridManager().setSelectionOnKeyVal(locationID);
					OrganizerPanel panel = (OrganizerPanel) dlg.m_currSheet;
					panel.gotoDate(day);
					panel.term("Appointments").setSelection(dayPartID, true);
				}
			}
		}

		boolean isEmpty() {
			return stack.size() == 0;
		}

		void openDialog(long locationID, JotyDate day, long dayPartID) {
			save();
			go(locationID, day, dayPartID);
		}

		void pop() {
			current = stack.pop();
		}

		void push(long locationID, JotyDate day, long dayPartID) {
			stack.add(new OrganizerStep(locationID, day, dayPartID));
		}

		void save() {
			OrganizerDialog dlg = ((OrganizerDialog) getOpenedDialog("OrganizerDialog", true));
			if (dlg != null) {
				OrganizerPanel panel = (OrganizerPanel) dlg.m_currSheet;
				push(panel.getLocationId(), panel.getDay(), panel.getDayPartID());
			}
		}

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application app = null;
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					app = new OdontiorApp();
					app.init("Odontior 2.0", "2.0.7", null, null);
					} catch (Exception e) {
					e.printStackTrace();
					warningMsg("Serious failure !");
					if (app.m_frame != null)
						app.m_frame.close();
				}
			}
		});
	}

	int m_reportCount = 0;

	HashMap<String, Integer> m_organizerPreferences;


	OrganizerHistory m_organizerHistory;

	public OdontiorApp() {
		m_organizerHistory = new OrganizerHistory();
	}

	@Override
	protected void buildAppMenuBar() {
		addItemToMenu(m_fileMenu, m_common.appLang("Patients"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("PatientDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Customers"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("CustomerDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("InsuranceCompanies"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("InsuranceCompanyDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Practitioners"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("PractitionerDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Organizer"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("OrganizerDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("WaitingList"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("WaitlistDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Treatments"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("TreatmentDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Invoices"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("InvoiceDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Estimates"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("EstimateDialog");
			}
		});
		addItemToMenu(m_fileMenu, m_common.appLang("Armchairs"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("ArmchairDialog");
			}
		});
		m_fileMenu.addSeparator();
		addItemToMenu(m_fileMenu, m_common.appLang("SetLastInvoiceNumber"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkRoleForExecution("Administrators"))
					setLastInvoiceNumber();
			}
		});
		m_fileMenu.addSeparator();
		addItemToMenu(m_fileMenu, m_common.jotyLang("LBL_EXIT"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitByMenu();
			}
		});
		m_toolsMenu.addSeparator();
		addItemToMenu(m_toolsMenu, m_common.appLang("TeethNaming"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataAccessDialog.tryCreate("org.odontior.TeethNamingDialog");
			}
		});
		m_menuBar.add(m_fileMenu);
		m_menuBar.add(m_authMenu);
		m_menuBar.add(m_viewMenu);
		m_menuBar.add(m_windowsMenu);
		m_menuBar.add(m_toolsMenu);
		if (!m_macOs)
			m_menuBar.add(m_helpMenu);
	}

	@Override
	public void endApp() {
		m_common.accessLocalData("OrganizerPreferences", m_organizerPreferences);
		super.endApp();
	}

	@Override
	public boolean init(String name, String version, String servletName, String webClientClass) {
		boolean retVal = super.init(name, version, servletName, webClientClass);
		m_organizerPreferences = (HashMap<String, Integer>) m_common.accessLocalData("OrganizerPreferences", null);
		if (m_organizerPreferences == null) {
			m_organizerPreferences = new HashMap<String, Integer>();
			m_organizerPreferences.put("hour", -1);
			m_organizerPreferences.put("locationId", 0);
		}
		m_applicationLink = "http://stepiz62.com/odontior";
		m_author = "Stefano Pizzocaro";
		m_copyrightYears = "2015-2017";
		return retVal;
	}

	public BasicPostStatement invoiceNumberPostStatement(Long invoiceNumber, JotyDate date) {
		BasicPostStatement postStatement = accessorMethodPostStatement("setInvoiceNumber", 1, -1);
		postStatement.addItem("number", String.valueOf(invoiceNumber), Item._long);
		postStatement.addItem("date", date == null ? null : date.render(true, false), Item._date);
		if (m_common.m_shared)
			postStatement.addItem("sharingKey", m_common.m_sharingKey, Item._text);
		return postStatement;
	}

	@Override
	protected void loadData() {
		m_appLogo = imageIcon("OdontiorLogo.png");
		m_appLogo2 = imageIcon("OdontiorLogo2.png");
		super.loadData();
	}

	@Override
	public void loadDescriptions() {
		super.loadDescriptions();

		m_common.setApplicationScopeAccessorMode();

		buildLiteralStruct("D_1", "id", "timelabel", "dayparts");
		buildLiteralStruct("D_2", "id", "name", "armchairs");
		buildLiteralStruct("D_3", "id", "symbol", "teeth");
		buildLiteralStruct("D_4", "id", "name", "states");
		buildLiteralStruct("D_5", "id", "name", "countries");

		LiteralStruct literalStruct = (LiteralStruct) m_common.createLiteralsCollection("treatmentTypes", false);
		literalStruct.addLiteral(-1, "", null);
		literalStruct.addLiteral(0, "Tooth treatment", null);
		literalStruct.addLiteral(1, "General treatment", null);
		literalStruct.addLiteral(2, "Any", null);

		buildLiteralStruct("D_6", "id", "description", "insuranceTypes",  m_common.m_literalCollectionInstance.new LiteralStructParams() {
			{
				sortedByID = true;
			}
		});

		loadTreatmentsInLiteralStruct();
		loadInsCompaniesInLiteralStruct();
		loadPractitionersInLiteralStruct();

	}

	public void loadInsCompaniesInLiteralStruct() {
		buildLiteralStruct("D_8", "id", "company", "insuranceCompanies",  m_common.m_modifiableLsParams);
	}

	public void loadPractitionersInLiteralStruct() {
		buildLiteralStruct("D_9", "id", "name", "practitioners",  m_common.m_modifiableLsParams);
	}

	public void loadTreatmentsInLiteralStruct() {
		buildLiteralStruct("D_7", "id", "descr", "treatmentDescriptions",  m_common.m_modifiableLsParams);
		buildLiteralStruct("D_7", "id", "symbol", "treatmentSymbols",  m_common.m_modifiableLsParams);
	}

	@Override
	protected void registerReports() {
		enableRoleToReport("Invoice", "Administrative personnel");
	}

	public boolean setInvoiceNumber(Long invoiceNumber, JotyDate date) {
		return invokeAccessMethod(invoiceNumberPostStatement(invoiceNumber, date));
	}

	public void setLastInvoiceNumber() {
		String input = null;
		boolean done = false;
		Long invoiceNumber = null;
		while (!done) {
			input = getInputFromUser(m_frame.getContentPane(), m_common.appLang("DigitLastInvoiceNumber"));
			if (input == null)
				break;
			try {
				invoiceNumber = Long.parseLong(input);
				if (invoiceNumber < 0)
					warningMsg(m_common.appLang("InvalidNumber"));
				else {
					setInvoiceNumber(invoiceNumber, null);
					done = true;
				}
			} catch (NumberFormatException e) {
				warningMsg(m_common.appLang("InvalidNumber"));
			}
		}
	}

	public boolean accessSemanticsChecker(JotyDate date, Long dayPartId, Long locationId, Long patientId, Long practitionerId, Long duration, boolean adding) {
		BasicPostStatement postStatement = accessorMethodPostStatement("accessSemanticsChecker", null, null);
		postStatement.addItem("date", date.render(true, false), Item._date);
		postStatement.addItem("dayPartId", String.valueOf(dayPartId), Item._long);
		postStatement.addItem("locationId", String.valueOf(locationId), Item._long);
		postStatement.addItem("patientId", String.valueOf(patientId), Item._long);
		postStatement.addItem("practitionerId", practitionerId == 0 ? null : String.valueOf(practitionerId), Item._long);
		postStatement.addItem("duration", String.valueOf(duration), Item._long);
		postStatement.addItem("adding",  adding ? "1" : "0", Item._long);
		if ( m_common.m_shared)
			postStatement.addItem("sharingKey",  m_common.m_sharingKey, Item._text);
		return invokeAccessMethod(postStatement);
	}
	
	public boolean accessInterventionChecker(Long interventionId, boolean adding) {
		BasicPostStatement postStatement = accessorMethodPostStatement("accessInterventionChecker", null, null);
		postStatement.addItem("interventionId", String.valueOf(interventionId), Item._long);
		postStatement.addItem("adding",  adding ? "1" : "0", Item._long);
		if (m_common.m_shared)
			postStatement.addItem("sharingKey",  m_common.m_sharingKey, Item._text);
		return invokeAccessMethod(postStatement);
	}
	
	public boolean organizerCostraintViolationMsgOnUpdate() {
		OdontiorDbManager dbManager = (OdontiorDbManager) m_dbManager;
		if (dbManager.derivedCode() == Subcases.InterventionUniqueness.ordinal())
			Application.warningMsg(m_common.appLang("InterventionUniqueness"));
		else if (dbManager.derivedCode() == Subcases.TimeSlotCoverage.ordinal())
			Application.warningMsg(m_common.appLang("DurationExceeds"));
		else if (dbManager.derivedCode() == Subcases.PatientLocUniqueness.ordinal())
			Application.warningMsg(m_common.appLang("PatientCannotBeAnyWhere"));
		else if (dbManager.derivedCode() == Subcases.PractitionerLocUniqueness.ordinal())
			Application.warningMsg(m_common.appLang("PractitionerCannotBeAnyWhere"));
		return dbManager.derivedCode() > 0;
	}


	private GridBuffer getPersonPanel(String dialogClassName) {
		return  ((SearcherMultiPanelDialog) m_app.getOpenedDialog(dialogClassName)).m_gridManager.m_gridBuffer;
	}
	
	public String patientLastName() {
		return getPersonPanel("PatientDialog").strValue("lastName");
	}
	
	public String patientFirstName() {
		return getPersonPanel("PatientDialog").strValue("firstName");
	}
	
}
