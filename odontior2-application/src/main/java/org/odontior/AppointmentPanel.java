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

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import org.joty.app.JotyException;
import org.joty.data.JotyDate;
import org.joty.workstation.app.Application;
import org.joty.workstation.data.JotyDataBuffer;
import org.joty.workstation.data.JotyDataBuffer.QueueManager;
import org.joty.workstation.gui.CheckBox;
import org.joty.workstation.gui.ComboBox;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataAccessPanel;
import org.joty.workstation.gui.DataInsertTransferHandler;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.GridManager;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.JotyTextField;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.Table.JotyJTable;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.TextArea;
import org.odontior.PersonSearcher.SearchWhat;

public class AppointmentPanel extends DataAccessPanel {

	JotyButton m_btnBrowseIntervention;
	private long m_oldInterventionId;
	private long m_oldDuration;
	JotyButton m_btnAdd;
	JotyButton m_btnSum;
	JCheckBox m_chckbxAutoCalc;
	Table m_interventions;
	CheckBox m_chkboxMacro;
	private JotyButton m_btnClearInterv;
	private JotyButton m_btnDeleteInterv;
	private JotyButton m_btnBrowsePatient;
	private JotyButton m_btnIntervDetails;
	private JLabel lblIntervention;
	private JotySeparator separator;
	private ComboBox opSymbol;
	private ComboBox opDescr;
	private ComboBox tooth;
	private JotySeparator separator_2;
	private JLabel lblInterventions;
	private boolean m_macroStatusIsSettling;
	private JotyTextField m_txtDuration;
	private AppointmentMonitor m_appointmentMonitor = new AppointmentMonitor();
	private BuildDetailsDialogAdapter m_interventionDetailsAdapter;
	private JotyButton m_btnPatientDetails;

	public AppointmentPanel() {

		JLabel lblTime = new JotyLabel(appLang("Time"));
		lblTime.setBounds(224, 5, 52, 14);
		add(lblTime);

		JLabel lblDate = new JotyLabel(appLang("Date"));
		lblDate.setBounds(285, 5, 52, 14);
		add(lblDate);

		JLabel lblSeat = new JotyLabel(appLang("Seat"));
		lblSeat.setBounds(364, 5, 52, 14);
		add(lblSeat);

		ComboBox time = isUndefined() ? Factory.createDescrRenderer(this, "daypart_id", "dayparts", true) : Factory.createDescrRenderer(this, "daypart", "daypart_id", "dayparts", "daypartID", false);
		time.setBounds(210, 19, 71, 20);
		add(time);

		if (!Beans.isDesignTime()) {
			JotyTextField date = isUndefined() ? Factory.createDateRenderer(this, "date", true) : Factory.createDateRenderer(this, "date", "date", "date", false);
			date.setBounds(285, 19, 74, 20);
			add(date);
		}

		ComboBox seat = isUndefined() ? Factory.createDescrRenderer(this, "location_id", "armchairs", true) : Factory.createDescrRenderer(this, "location", "location_id", "armchairs", "locationID", false);
		seat.setBounds(363, 19, 150, 20);
		add(seat);

		JLabel lblPatient = new JotyLabel(appLang("Patient"));
		lblPatient.setBounds(10, 42, 122, 14);
		add(lblPatient);

		JotyTextField lastname = Factory.createTextRenderer(this, "lastname", true);
		lastname.setBounds(10, 61, 95, 20);
		add(lastname);
		JotyTextField firstname = Factory.createTextRenderer(this, "firstname", true);
		firstname.setBounds(106, 61, 95, 20);
		add(firstname);

		Factory.createHiddenLong(this, "patient_id");
		term("patient_id").setMandatory("lastname");

		m_currentButtonBehavior = ButtonBehavior.editing;
		m_btnBrowsePatient = new JotyButton(appLang("Browse"), term("patient_id"));
		m_btnBrowsePatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acquireSelectedValueFrom("PersonSearcher", "patient_id", new String[] { "lastname", "firstname" }, null, SearchWhat.patients, null, false);
				clearIntervention(true);
			}
		});
		m_btnBrowsePatient.setBounds(203, 61, 74, 22);
		add(m_btnBrowsePatient);

		JLabel lblTreatmentSymbol = new JotyLabel(appLang("Intervention"));
		lblTreatmentSymbol.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTreatmentSymbol);

		opDescr = Factory.createDescrRenderer(this, "treatment_id", "treatmentDescriptions", true);
		opDescr.setBounds(112, 146, 323, 20);
		add(opDescr);

		opSymbol = Factory.createDescrRenderer(this, "TreatmentDescr", "treatment_id", "treatmentSymbols", true);
		opSymbol.setBounds(10, 146, 103, 20);
		add(opSymbol);

		tooth = Factory.createDescrRenderer(this, "tooth_id", "teeth", true);
		tooth.setBounds(434, 146, 79, 20);
		add(tooth);

		m_txtDuration = Factory.createLongNum(this, "duration", "duration", 3);
		m_txtDuration.setBounds(443, 63, 46, 20);
		add(m_txtDuration);
		term("duration").setMandatory();

		JLabel lblDuration = new JotyLabel(appLang("Duration"));
		lblDuration.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDuration.setBounds(341, 66, 95, 14);
		add(lblDuration);

		Factory.createHiddenLong(this, "intervention_id");

		m_btnBrowseIntervention = new JotyButton(appLang("Browse"));
		m_btnBrowseIntervention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pickIntervention("intervention_id", new String[] { "treatment_id", "TreatmentDescr", "tooth_id", "duration" });
			}
		});
		m_btnBrowseIntervention.setBounds(102, 119, 74, 22);
		add(m_btnBrowseIntervention);

		separator = new JotySeparator();
		separator.setBounds(10, 141, 502, 5);
		add(separator);

		m_chkboxMacro = Factory.createCheck(this, "macro", "macro");
		m_chkboxMacro.setText(appLang("Macro"));
		m_chkboxMacro.setBounds(6, 92, 126, 23);
		add(m_chkboxMacro);
		term("macro").defaultValue().setValue(0);

		m_chckbxAutoCalc = new JCheckBox(appLang("AutoCalc"));
		m_chckbxAutoCalc.setBounds(440, 86, 80, 23);
		add(m_chckbxAutoCalc);

		m_btnSum = new JotyButton(appLang("Sum"));
		m_btnSum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sumDuration();
			}
		});
		m_btnSum.setBounds(444, 112, 69, 22);
		add(m_btnSum);

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(437, 64, 2, 69);
		add(separator_1);

		m_btnAdd = new JotyButton(appLang("Add"));
		m_btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pickIntervention(null, null);
				if (getSelectedValue() != -1)
					if (addIntervention(getSelectedValue(), true)) {
						tableTerm("interventions").refresh();
						addingEffects();
					}
			}
		});
		m_btnAdd.setBounds(10, 180, 79, 22);
		add(m_btnAdd);

		m_interventions = Factory.createTable(this, "interventions");
		m_interventions.setBounds(95, 179, 418, 89);
		add(m_interventions);
		TableTerm term = tableTerm("interventions");
		term.setKeyName("id");
		term.addField("diagtime", appLang("DiagnosisTime"));
		term.addField("treatment", appLang("Treatment"));
		term.addField("tooth", appLang("Tooth"));
		term.addField("duration", appLang("Duration"));
		term.addFieldAsFlag("done", appLang("Done"));
		m_interventions.m_colWidths = new int[] { 90, 90, 30, 30, 30 };
		m_interventions.setAllColsAlignement(SwingConstants.CENTER);
		m_interventions.setToolTipText(appLang("EditRecord"));

		m_interventionDetailsAdapter = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				setContextParam("interventionID", m_chkboxMacro.getCheck() == 1 ? tableTerm("interventions").m_dataBuffer.integerValue("intervention_id") : term("intervention_id").integerVal());
				return DataAccessDialog.getInstance("InterventionDialog", callContext(), InterventionDialog.QueryType.interventionSpecific);
			}
		};
		term.m_buildDetailsHandler = m_interventionDetailsAdapter;

		new DataInsertTransferHandler(TransferHandler.NONE, TransferHandler.COPY, m_interventions, true) {
			@Override
			protected boolean importAction(String carried_val, String identifying_id_val, Transferable transferable, boolean carriedValDelayed) {
				return addIntervention(Long.parseLong(carried_val), true);
			}
			
			@Override
			protected boolean checkImportTransaction() {
				return true;
			}


			@Override
			protected void postImport() {
				addingEffects();
			}

			@Override
			public boolean canImport(TransferSupport support) {
				return ((DataAccessDialog) getDialog()).isEditing() && super.canImport(support);
			}
		};

		JLabel lblNote = new JotyLabel(appLang("Note"));
		lblNote.setBounds(12, 288, 120, 14);
		add(lblNote);

		TextArea note = Factory.createTextArea(this, "note", "note", 255);
		note.setBounds(10, 303, 503, 61);
		add(note);

		m_btnClearInterv = new JotyButton(appLang("Clear"));
		m_btnClearInterv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearIntervention(true);
			}
		});
		m_btnClearInterv.setBounds(181, 119, 69, 22);
		add(m_btnClearInterv);

		m_currentButtonBehavior = ButtonBehavior.free;
		m_btnDeleteInterv = new JotyButton(jotyLang("LBL_DEL"));
		m_btnDeleteInterv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Application.yesNoQuestion(appLang("WantDelAppInterv"))) {
					long duration = tableTerm("interventions").m_dataBuffer.integerValue("duration");
					if (deleteIntervention(-1)) {
						tableTerm("interventions").refresh();
						if (m_chckbxAutoCalc.getSelectedObjects() != null)
							addToDuration(-duration);
					}
				}
			}
		});
		m_btnDeleteInterv.setBounds(10, 217, 79, 22);
		add(m_btnDeleteInterv);
		tableTerm("interventions").setRowActionButton(m_btnDeleteInterv);
		m_btnDeleteInterv.setEnabled(false);

		lblIntervention = new JotyLabel(appLang("Intervention"));
		lblIntervention.setBounds(14, 123, 80, 14);
		add(lblIntervention);

		lblInterventions = new JotyLabel(appLang("IntervImmediateEffect"));
		lblInterventions.setBounds(10, 158, 503, 14);
		add(lblInterventions);

		separator_2 = new JotySeparator();
		separator_2.setBounds(10, 173, 502, 5);
		add(separator_2);

		JotySeparator separator_3 = new JotySeparator();
		separator_3.setBounds(10, 57, 336, 3);
		add(separator_3);

		ComboBox practitioner = Factory.createComboBox(this, "practitioner", "practitioner_id", false, "practitioners");
		practitioner.setBounds(313, 276, 200, 20);
		add(practitioner);

		JLabel lblPractitioner = new JotyLabel(appLang("Practitioner"));
		lblPractitioner.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPractitioner.setBounds(181, 279, 127, 14);
		add(lblPractitioner);

		m_btnIntervDetails = new JotyButton(appLang("Details"));
		m_btnIntervDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openDetail(null, m_interventionDetailsAdapter);
			}
		});
		m_btnIntervDetails.setBounds(260, 119, 69, 22);
		add(m_btnIntervDetails);

		m_btnPatientDetails = new JotyButton(appLang("Details"));
		m_btnPatientDetails.addActionListener(new ActionListener() {
			BuildDetailsDialogAdapter adapter = new BuildDetailsDialogAdapter() {
				@Override
				public JotyDialog createDialog(TableTerm term) {
					setContextParam("patientID", term("patient_id").integerVal());
					return DataAccessDialog.getInstance("PatientDetailsDialog", callContext());
				}
			};

			@Override
			public void actionPerformed(ActionEvent e) {
				openDetail(null, adapter);
			}
		});
		m_btnPatientDetails.setBounds(277, 61, 69, 22);
		add(m_btnPatientDetails);

		setAsPublisher();

		addIntegerKeyElem("ID", true);

		enableRole("Schedulers", Permission.all);
		enableRole("Practitioners", Permission.read);
		enableRole("Administrative personnel", Permission.read);

	}

	protected void addingEffects() {
		tableTerm("interventions").selectLastRow();
		if (m_chckbxAutoCalc.getSelectedObjects() != null)
			addToDuration(tableTerm("interventions").m_dataBuffer.integerValue("duration"));
	}

	private boolean addIntervention(long interventionID, boolean checkExistence) {
		boolean retVal = checkExistence ? m_appointmentMonitor.checkExistence(interventionID, true, m_isNewRec, m_oldInterventionId) : true;
		if (retVal) {
			retVal = Application.m_app.executeSQL("Insert into <JOTY_CTX> (appointment_id, intervention_id) values(" + integerKeyElemVal("ID") + ", " + interventionID + ")", 
													null, 
													m_app.createLiteralSubstPostStatement("A_4"));
			if (retVal)
				retVal = ((OdontiorApp) m_app).accessInterventionChecker(interventionID, true);
		}		
		return retVal;
	}

	private void addToDuration(long integerValue) {
		String currentText = m_txtDuration.getText().trim();
		long newValue = (currentText.length() == 0 ? 0 : Integer.parseInt(m_txtDuration.getText().trim())) + integerValue;
		if (newValue < 0)
			newValue = 0;
		m_txtDuration.setTextAndNotify(String.valueOf(newValue), true);
	}

	@Override
	public void checkAndSetLook() {
		super.checkAndSetLook();
		updateMacroLook(true);
	}

	private void clearIntervention(boolean clearDuration) {
		term("intervention_ID").clear();
		term("intervention_ID").setDirty();
		term("treatment_ID").clear();
		term("TreatmentDescr").clear();
		term("tooth_id").clear();
		if (clearDuration)
			m_txtDuration.setTextAndNotify("0");
	}

	private boolean deleteIntervention(int pos) {
		JotyDataBuffer buffer = tableTerm("interventions").m_dataBuffer;
		long appointIntervID = pos >= 0 ? buffer.getKeyLongVal(pos) :  buffer.getKeyLongVal();
		boolean retVal = m_app.executeSQL("Delete from <JOTY_CTX> where ID = " + appointIntervID, 
										null, m_app.createLiteralSubstPostStatement("A_4"));
		long interventionId = pos >= 0 ? buffer.integerValue("intervention_id", pos) :  buffer.integerValue("intervention_id");
		if (retVal)
			retVal = ((OdontiorApp) m_app).accessInterventionChecker(interventionId, false);
		return retVal;
	}

	private boolean deleteInterventions(boolean asTransactionComponent) {
		boolean retVal = true;
		if (tableTerm("interventions").getRowQty() > 0) {
			retVal = asTransactionComponent ? true : Application.yesNoQuestion(appLang("WantDelAppIntervs"));
			if (retVal)
				retVal = m_app.new JotyTransaction(asTransactionComponent) {
					@Override
					public boolean exec() throws JotyException {
						boolean retVal = true;
						for (int i = 0; i < tableTerm("interventions").getRowQty(); i++) {
							retVal = deleteIntervention(i);
							if (!retVal)
								break;
						}
						return retVal;
					}
				}.success;
			tableTerm("interventions").refresh();
		}
		return retVal;
	}
				

	@Override
	public boolean doDeletion() {
		boolean retVal = true;
		if (isUndefined()) {
			GridManager gridManager = m_app.getOpenedDialog("WaitlistDialog").getGridManager();
			QueueManager qManager = gridManager.m_gridBuffer.m_queueManager;
			retVal = qManager.deleteRecord(integerKeyElemVal("ID"));
			if (retVal) {
				qManager.prepareTransaction(((JotyJTable) gridManager.m_listComponent.getPaneComponent()).getSelectedRow());
				retVal = qManager.manageRemoval();
			}
		} else {
			((AppointmentDialog) m_dialog).checkToRefreshOrganizer();
			retVal = ((OdontiorApp) m_app).accessSemanticsChecker((JotyDate) term("date").dateVal(), term("daypart").integerVal(), term("location").integerVal(), 
					term("patient_id").integerVal(), term("practitioner").integerVal(), term("duration").integerVal(), false);
		}
		if (! term("intervention_ID").isNull() && retVal)
			retVal = ((OdontiorApp) m_app).accessInterventionChecker(term("intervention_id").integerVal(), false);
		if (retVal)
			retVal = deleteInterventions(true);
		return retVal && super.doDeletion();
	}

	
	@Override
	protected void doNew() {
		m_oldInterventionId = 0;
		m_oldDuration = 0;
		super.doNew();
	}

	@Override
	protected boolean edit() {
		storeScenario();
		return super.edit();
	}

	@Override
	public void enableComponents(boolean bState) {
		super.enableComponents(bState);
		m_btnBrowsePatient.setEnabled(bState && !documentIdentified());
		boolean enableInterventionButtons = bState && !term("patient_id").isNull();
		m_btnBrowseIntervention.setEnabled(enableInterventionButtons);
		m_btnIntervDetails.setEnabled(!term("treatment_id").isNull());
		m_btnPatientDetails.setEnabled(!term("patient_id").isNull());
		m_chckbxAutoCalc.setEnabled(bState);
		m_chkboxMacro.setEnabled(enableInterventionButtons);
	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (retVal && isUndefined() && m_isNewRec) {
			Application.warningMsg(appLang("FeedWaitlistFromOrganizerOrCCards"));
			retVal = false;
		}
		return retVal;
	}

	long interventionToCheck() {
		long retVal = 0;
		if (m_macroStatusIsSettling && m_chkboxMacro.getCheck() == 1 || (!m_macroStatusIsSettling && m_chkboxMacro.getCheck() == 0))
			retVal = term("intervention_id").integerVal();
		return retVal;
	}

	private boolean isUndefined() {
		return Beans.isDesignTime() ? false : ((AppointmentDialog) m_dialog).isUndefined();
	}

	@Override
	public void notifyEditingAction(ActionEvent e) {
		super.notifyEditingAction(e);
		if (getSource(e) == m_chkboxMacro)
			setMacroAppointment(((CheckBox) e.getSource()).getCheck() == 1);
	}

	private void pickIntervention(String termName, String[] dependentTermList) {
		setContextParam("patientID", term("patient_id").integerVal());
		acquireSelectedValueFrom("InterventionDialog", termName, dependentTermList, null, InterventionDialog.QueryType.asSelector, null, true);
	}

	@Override
	public void saveEffects(boolean leaveEditingOn) {
		super.saveEffects(leaveEditingOn);
		storeScenario();
	}

	@Override
	protected void setContextParams() {
		setContextParam("appointmentID", integerKeyElemVal("ID"));
	}

	private void setMacroAppointment(boolean truth) {
		boolean goOn = true;
		m_macroStatusIsSettling = true;
		DataAccessDialog dialog = (DataAccessDialog) m_dialog;
		goOn = dialog.shouldDo(true);
		m_macroStatusIsSettling = false;
		if (goOn) {
			setContextParam("appointmentID", integerKeyElemVal("ID"));
			if (truth) {
				if (!term("intervention_id").isEmpty())
					goOn = m_app.new JotyTransaction() {
					@Override
					public boolean exec() {
						return updateIntervention("null") && addIntervention(term("intervention_id").integerVal(), false);
					}
				}.success;
				if (goOn) {
					tableTerm("interventions").refresh();
					clearIntervention(false);
				}
			} else {
				int interventionsSize = tableTerm("interventions").getRowQty();
				if (interventionsSize > 0) {
					if (interventionsSize == 1) {
						goOn = m_app.new JotyTransaction() {
							@Override
							public boolean exec() {
								return deleteIntervention(0) && updateIntervention(String.valueOf(tableTerm("interventions").m_dataBuffer.integerValue("intervention_id", 0)));
							}
						}.success;
						if (goOn)
							reLoadData();
					} else {
						goOn = deleteInterventions(false);
						if (goOn)
							clearIntervention(true);
					}
				}
			}
		}
		if (goOn) {
			checkAndSetLook();
			storeScenario();
			checkForPublishing();
		} else
			m_chkboxMacro.setCheck(truth ? 0 : 1);
	}

	private void storeScenario() {
		m_oldInterventionId = term("intervention_id").integerVal();
		m_oldDuration = term("duration").integerVal();
	}

	private void sumDuration() {
		m_txtDuration.setTextAndNotify("0", true);
		JotyDataBuffer buffer = tableTerm("interventions").m_dataBuffer;
		for (int i = 0; i < buffer.m_records.size(); i++)
			addToDuration(buffer.integerValue("duration", i));
	}

	private boolean updateIntervention(String interventionValStr) {
		boolean toNull = interventionValStr.compareTo("null") == 0;
		boolean retVal = ((OdontiorApp) m_app).accessInterventionChecker(toNull ? term("intervention_id").integerVal() : Long.parseLong(interventionValStr), ! toNull);
		if (retVal)
			retVal = m_app.executeSQL("Update <JOTY_CTX> set intervention_id = " + interventionValStr + " where ID = " + integerKeyElemVal("ID"), 
				null, 
				m_app.createLiteralSubstPostStatement("A_1"));
		return retVal;
	}

	void updateMacroLook(boolean basedOnGui) {
		boolean truth = basedOnGui ? m_chkboxMacro.getCheck() == 1 : term("macro").integerVal() == 1;
		m_chckbxAutoCalc.setVisible(truth);
		m_btnSum.setVisible(truth);
		m_btnAdd.setVisible(truth);
		m_interventions.setVisible(truth);
		m_btnDeleteInterv.setVisible(truth);
		separator_2.setVisible(truth);
		lblInterventions.setVisible(truth);

		m_btnClearInterv.setVisible(!truth);
		lblIntervention.setVisible(!truth);
		separator.setVisible(!truth);
		opDescr.setVisible(!truth);
		opSymbol.setVisible(!truth);
		tooth.setVisible(!truth);
		m_btnBrowseIntervention.setVisible(!truth);
		m_btnIntervDetails.setVisible(!truth);
	}

	@Override
	protected void updateVisibilityBasingOnData() {
		updateMacroLook(false);
	}

	@Override
	protected boolean validateComponents() {
		boolean retVal = super.validateComponents();
		if (retVal && textTerm("duration").integerVal() < 0) {
			Application.warningMsg(appLang("NegativeDurationNoSense"));
			retVal = false;
		}
		return retVal;
	}

	@Override
	protected boolean validation() {
		boolean retVal = super.validation();
		if (retVal) {
			long idToCheck = interventionToCheck();
			if (idToCheck > 0)
				retVal = m_appointmentMonitor.checkExistence(idToCheck, false, m_isNewRec, m_oldInterventionId);
			if (retVal && !isUndefined())
				retVal = ((OrganizerPanel) m_app.getOpenedDialog("OrganizerDialog").m_currSheet).checkForTimeStorage((int) term("daypart").integerVal(), term("duration").integerVal(), m_oldDuration);
		}
		return retVal;
	}
	
	@Override
	protected boolean storeData() throws JotyException {
		boolean retVal = true;
		if (! m_isNewRec)  {
			if (! isUndefined())
				retVal = accessSemanticsChecker(m_oldDuration, false);
			if (retVal && m_oldInterventionId != 0)
				retVal = ((OdontiorApp) m_app).accessInterventionChecker(m_oldInterventionId, false);
		}
		if (retVal)
			retVal = super.storeData();
		if (retVal && ! isUndefined())
			retVal = accessSemanticsChecker(term("duration").integerVal(), true);
		if (retVal && ! term("intervention_id").isNull())
			retVal = ((OdontiorApp) m_app).accessInterventionChecker(term("intervention_id").integerVal(), true);		
		return retVal;
	}

	private boolean accessSemanticsChecker(Long duration, boolean adding) {
		return ((OdontiorApp) m_app).accessSemanticsChecker((JotyDate) term("date").dateVal(), term("daypart").integerVal(), term("location").integerVal(), 
				term("patient_id").integerVal(), term("practitioner").integerVal(), duration, adding);
	}

	@Override
	public void costraintViolationMsgOnUpdate() {
		if (! ((OdontiorApp) m_app).organizerCostraintViolationMsgOnUpdate())
			super.costraintViolationMsgOnUpdate();
	}
	
	
}
