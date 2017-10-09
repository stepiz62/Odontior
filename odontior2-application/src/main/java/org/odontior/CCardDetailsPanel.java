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
import java.util.Vector;

import javax.swing.*;

import org.joty.data.WrappedField;
import org.joty.workstation.gui.*;
import org.joty.workstation.data.JotyDataBuffer.Record;
import org.joty.workstation.gui.DataAccessDialog;
import org.joty.workstation.gui.DataInsertTransferHandler;
import org.joty.workstation.gui.Factory;
import org.joty.workstation.gui.GridManager;
import org.joty.workstation.gui.JotyButton;
import org.joty.workstation.gui.JotyDialog;
import org.joty.workstation.gui.JotyLabel;
import org.joty.workstation.gui.NavigatorPanel;
import org.joty.workstation.gui.Table;
import org.joty.workstation.gui.TableTerm;
import org.joty.workstation.gui.Term;
import org.joty.workstation.gui.GridManager.IRenderAnalogicalSelector;

public class CCardDetailsPanel extends NavigatorPanel {

	boolean m_unified;
	BuildDetailsDialogAdapter m_interventionsDetailsAdapter,
	m_imagesDetailsAdapter;
	GraphCardPanel m_cardGraphPanel;

	public CCardDetailsPanel() {
		m_table.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		m_table.setSize(246, 422);
		m_table.setLocation(271, 23);

		m_cardGraphPanel = new GraphCardPanel(this);
		m_cardGraphPanel.setLocation(5, 20);
		m_cardGraphPanel.setSize(260, 431);
		add(m_cardGraphPanel);

		m_actionOnRowHandler = new ActionOnRowInterface() {
			@Override
			public void doAction(Term srcTerm, int column) {
				if (column > 1)
					if (column == 4)
						openDetail(tableTerm("Images"), m_imagesDetailsAdapter);
					else
						openDetail(tableTerm("Interventions"), m_interventionsDetailsAdapter);
			}
		};

		m_unified = contextParameter("UniqueCcard").compareTo("1") == 0;

		Table interventions = Factory.createTable(this, "interventions");
		interventions.setBounds(6, 482, 715, 76);
		add(interventions);
		TableTerm term = tableTerm("interventions");
		term.setKeyName("id");
		enabledAsDetail("interventions");
		interventions.addToolTipRow(m_common.appLang("DragToBuildAppointment"));
		interventions.addToolTipRow(m_common.appLang("DragToBuildQueuedEvt"));

		m_interventionsDetailsAdapter = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				return DataAccessDialog.getInstance("InterventionDialog", callContext(), InterventionDialog.QueryType.contextSpecific);
			}

			@Override
			public String identifierFromCaller() {
				return selectedContext();
			}
		};
		term.m_buildDetailsHandler = m_interventionsDetailsAdapter;
		term.addField("timestamp", appLang("DiagnosisTime"));
		term.addField("descr", appLang("Treatment"));
		term.addField("symbol", appLang("TrtSymbol"));
		term.addField("execution", appLang("ExecutionTime"));
		term.addField("duration", appLang("Duration"));
		if (m_unified)
			term.addField("number", appLang("CardN"));
		interventions.m_colWidths = new int[] { 60, 270, 60, 60, 20, 20 };
		interventions.setAllColsAlignement(SwingConstants.CENTER);

		new DataInsertTransferHandler(TransferHandler.COPY, TransferHandler.NONE, interventions, false);

		JLabel lbltoothSelection = new JotyLabel(appLang("ContextSelection"));
		lbltoothSelection.setHorizontalAlignment(SwingConstants.CENTER);
		lbltoothSelection.setBounds(5, 2, 512, 14);
		add(lbltoothSelection);

		JLabel lblInterventions = new JotyLabel(appLang("Interventions"));
		lblInterventions.setBounds(13, 462, 208, 14);
		add(lblInterventions);

		Table images = Factory.createTable(this, "Images");
		images.setBounds(531, 23, 190, 422);
		add(images);
		term = tableTerm("Images");
		term.setKeyName("id");

		enabledAsDetail("Images");
		m_imagesDetailsAdapter = new BuildDetailsDialogAdapter() {
			@Override
			public JotyDialog createDialog(TableTerm term) {
				return DataAccessDialog.getInstance("ImageDialog", callContext());
			}

			@Override
			public String identifierFromCaller() {
				return selectedContext();
			}
		};
		term.m_buildDetailsHandler = m_imagesDetailsAdapter;
		term.addField("timestamp", appLang("Timestamp"));
		term.addFieldAsImage("imagepreview", appLang("Image"));
		if (m_unified)
			term.addField("number", appLang("CardN"));
		term.fitHeightToPreview();
		term.fieldDescr("imagepreview").setTargetField("image");
		term.fieldDescr("imagepreview").setIdentityRenderer(new WrappedField.IdentityRenderer() {
			@Override
			public String render() {
				return appLang("Context") + ": " + getGridManager().m_gridBuffer.strValue("symbol") + " - " + tableTerm("Images").m_dataBuffer.getWField("timestamp").render();
			}
		});
		images.m_colWidths = new int[] { 120, m_unified ? 50 : 90, 40 };
		images.setAllColsAlignement(SwingConstants.CENTER);
		if (m_unified)
			images.setCustomRowHeight(36);

		JLabel lblImages = new JotyLabel(appLang("Images"));
		lblImages.setBounds(531, 6, 176, 14);
		add(lblImages);

		Factory.addTextToGrid(this, "quadrant", appLang("Quadrant"));
		Factory.addTextToGrid(this, "symbol", appLang("Symbol"));
		Factory.addLongNumToGrid(this, "executed", appLang("Executed"));
		Factory.addLongNumToGrid(this, "unexecuted", appLang("Unexecuted"));
		Factory.addLongNumToGrid(this, "images", appLang("Images"));

		JotySeparator separator_1 = new JotySeparator();
		separator_1.setBounds(5, 16, 512, 5);
		add(separator_1);

		JotySeparator separator_2 = new JotySeparator();
		separator_2.setBounds(5, 452, 512, 5);
		add(separator_2);

		if (!m_unified) {
			JButton btnViewCardDescriptor = new JotyButton(appLang("ViewCardDescr"));
			btnViewCardDescriptor.addActionListener(new ActionListener() {
				BuildDetailsDialogAdapter adapter = new BuildDetailsDialogAdapter() {
					@Override
					public JotyDialog createDialog(TableTerm GridTerm) {
						return DataAccessDialog.getInstance("ClinicalCardDialog", callContext(), ClinicalCardDialog.QueryType.specific);
					}

					@Override
					public String identifierFromCaller() {
						return getDialog().getTitle();
					}
				};

				@Override
				public void actionPerformed(ActionEvent e) {
					openDetail(null, adapter);
				}
			});
			btnViewCardDescriptor.setBounds(531, 452, 190, 23);
			add(btnViewCardDescriptor);
		}

		m_gridManager.m_renderAnalogicalSelector = new IRenderAnalogicalSelector() {
			int execColPos = -1, unexecColPos, imagesColPos;

			@Override
			public void render(GridManager gridManager, int row) {
				if (execColPos == -1) {
					execColPos = gridManager.getFieldIndex("executed");
					unexecColPos = gridManager.getFieldIndex("unexecuted");
					imagesColPos = gridManager.getFieldIndex("images");
				}
				Vector<WrappedField> record = gridManager.m_gridBuffer.m_records.get(row).m_data;
				gridManager.setHeavyStatus(row, record.get(execColPos).m_lVal > 0 || record.get(unexecColPos).m_lVal > 0 || record.get(imagesColPos).m_lVal > 0);
				gridManager.renderHeavyStatus(row);
			}
		};

		subscribe("InterventionDialog");
		subscribe("ImageDialog");

		addIntegerKeyElem("ID", true, true);

		enableRole("Practitioners", Permission.all);
		enableRole("Doctors", Permission.read);
		enableRole("Administrative personnel", Permission.read);
		enableRole("Schedulers", Permission.read);

	}

	@Override
	public void effectsOnTerms(Record row) {
		setContextParam("toothID", idFromGridRow());
		super.effectsOnTerms(row);
	}

	@Override
	public boolean init() {
		boolean retVal = super.init();
		if (retVal)
			m_cardGraphPanel.init(false);
		return retVal;
	}

	private String selectedContext() {
		return "Ctx: " + getGridManager().m_gridBuffer.strValue("symbol") + (m_unified ? "" : " - Card " + contextParameter("cardNumber")) + " " + contextParameter("patientName");
	}

	@Override
	protected void setGridFormat(Table table) {
		table.setAllColsAlignement(SwingConstants.CENTER);
		table.setCustomRowHeight(12);
		table.m_sortClickDenied = true;
	}

	@Override
	protected void setControllerOnKey(long keyVal) {
	}

	
}
