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

package com.stepiz62.odontior.accessor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;

import org.joty.basicaccessor.BasicAccessor;
 
public class OdontiorAccessor extends BasicAccessor {

	protected String _appointment,
					_appointminterv,
					_armchair,
					_clinicalcard,
					_country,
					_customer,
					_daypart,
					_estimate,
					_estimateitem,
					_image,
					_insurance,
					_insurancecompany,
					_insurancetype,
					_intervention,
					_invoice,
					_invoiceitem,
					_invoicenumber,
					_patient,
					_patientcustomer,
					_practitioner,
					_quadrant,
					_reimbursement,
					_reimbursementplan,
					_reservednote,
					_state,
					_tooth,
					_treatment,
					_waitinglist,
				
				 	_clinicalcardcounts,
					_clinicalcardschema, 
				
					_setcardnumber,
					_setinvoicenumber,
					_setinvoicenumbershared,
					
					_semanticsChecker,
					_interventionChecker;
	
	private PanelDataDef m_customerPanenDataDef;
	private PanelDataDef m_patientPanelDataDef;
	private PanelDataDef m_insuranceCompanyPanelDataDef;
	
	protected void namer () {
		_appointment           = "appointment";
		_appointminterv        = "appointminterv";
		_armchair              = "armchair";
		_clinicalcard          = "clinicalcard";
		_country               = "country";
		_customer              = "customer";
		_daypart               = "daypart";
		_estimate              = "estimate";
		_estimateitem          = "estimateitem";
		_image                 = "image";
		_insurance             = "insurance";
		_insurancecompany      = "insurancecompany";
		_insurancetype         = "insurancetype";
		_intervention          = "intervention";
		_invoice               = "invoice";
		_invoiceitem           = "invoiceitem";
		_invoicenumber         = "invoicenumber";
		_patient               = "patient";
		_patientcustomer       = "patientcustomer";
		_practitioner          = "practitioner";
		_quadrant              = "quadrant";
		_reimbursement         = "reimbursement";
		_reimbursementplan     = "reimbursementplan";
		_reservednote          = "reservednote";
		_state                 = "state";
		_tooth                 = "tooth";
		_treatment             = "treatment";
		_waitinglist           = "waitinglist";

	 	_clinicalcardcounts	 	= "clinicalcardcounts";
		_clinicalcardschema	  	= "clinicalcardschema"; 

		_setcardnumber	  		= "setcardnumber";
		_setinvoicenumber	  	= "setinvoicenumber";
		_setinvoicenumbershared	= "setinvoicenumbershared";
		
		_semanticsChecker		= "semanticschecker";
		_interventionChecker	= "interventionchecker";
	}
	
	public String selectedContextQuery(String mainTable) {
		boolean isImage = mainTable.compareTo(_image) == 0;
		return String.format(
				"		SELECT i.*, c.number %1$s, c.patient_id                                         			" +
				"		FROM %2$s i                                                        							" +
				"		INNER JOIN " + _clinicalcard + " c ON (c.id = %3$s OR %4$s = 1 AND c.patient_id = %5$s)  	" +
				"									and i.clinicalcard_id = c.id                   					" +
				"		%6$s                            															" +
				"		WHERE tooth_id like '%7$s'                                                       			",
				isImage ? "" : ", t.symbol, t.descr, t.dflt_duration",
				mainTable,
				paramValue("clinicalCardID"),
				paramValue("UniqueCcard"),
				paramValue("patientID"),
				isImage ? "" : "INNER JOIN " + _treatment + " t on t.id = i.treatment_id",
				paramValue("toothID"));
	}
	
	class Invoice_PanelDataDef extends PanelDataDef { 
		boolean m_forEstimating;
		boolean isOpenNormal(String mode) {
			return m_forEstimating || mode == null || mode.compareTo("normal") == 0;
		}
		void addMoreTermDataDefs () {};
		boolean isViewer() {return true;};
		String moreSelectTerms() { return String.format(", pc.customer_id, p.firstname pat_firstname, p.lastname pat_lastname %1$s",
				m_forEstimating ? ", ins.name insurance, ins.reimbursementplan_id" : "");};
		String fromClauseExtension(String mode) { return " inner join " + _patient + " p on p.id = pc.patient_id  " +
															(m_forEstimating ? "left join " + _insurance + " ins on ins.id = i.insurance_id" : "");};
														
		
		Invoice_PanelDataDef(boolean forEstimating) {
			m_forEstimating = forEstimating;
			final InvoiceNamer iNamer = new InvoiceNamer(forEstimating);
			setUpdatableSet(iNamer.tableName());
			setStatementHolder(new ExprHolder() {
				public String getSelectiveExpr(String mode) {	
					return String.format(
						"	SELECT i.*, c.firstname, c.lastname, pc.isThePatient %1$s                " +
						"	FROM %2$s i                                                              " +
						"	INNER JOIN " + _patientcustomer + " pc ON pc.patcust_id = i.patientcustomer_id    " +
						"	INNER JOIN " + _customer + " c ON c.id = pc.customer_id	%3$s			         ",
						moreSelectTerms(),
						iNamer.tableName(),
						fromClauseExtension(mode)
										); }
			});
			setSharingAlias("i");
			m_termDataDefs.put("items", new InvoiceDataPanel_DataDef(forEstimating, this));
			addMoreTermDataDefs();
			m_statementDefs.put("DeleteInvoiceItems", new DataDef() {
				{
					setStatementHolder(new ExprHolder() {
						public String getSelectiveExpr(String mode) {
							return "Delete from " + iNamer.itemTableName() + " Where " + iNamer.tableRefIdFieldName() + " = " + 
																				paramValue("entityIdExpr");
						}
					});
				}
			});
			m_statementDefs.put("DeleteInvoiceItem", new DataDef() {
				{
					setStatementHolder(new ExprHolder() {
						public String getSelectiveExpr(String mode) {
							return "Delete from " + iNamer.itemTableName() + " Where ID = " + paramValue("idsSetId");
						}
					});
				}
			});
			m_statementDefs.put("InsertInvoiceItem", new DataDef() {
				{
					setStatementHolder(new ExprHolder() {
						public String getSelectiveExpr(String mode) {
							return String.format(
									"Insert into " + iNamer.itemTableName() + " (intervention_id, " + iNamer.tableRefIdFieldName() + ") values(%s, %s)",
									paramValue("idsSetId"),
									paramValue("entityIdExpr")
												);
						}
					});
				}
			});
		}
	};
	
	class InvoiceEditing_PanelDataDef extends Invoice_PanelDataDef {  
		InvoiceEditing_PanelDataDef(boolean forEstimating) {
			super(forEstimating);
			m_statementDefs.put("LoadReimbursement", new DataDef() {
				{
					setStatementHolder(new ExprHolder() {
						public String getSelectiveExpr(String mode) {
							return "Select treatment_id, percentage from " + _reimbursement + " where reimbursementplan_id = " + 
																	paramValue("reimbursementplan_id");
						}
					});
				}
			});
		}
		String furtherExclusionClause() { 
			return m_forEstimating ? 
					String.format(
							"		AND intrv.id NOT IN (                            "
						+	"			SELECT intervention_id                       "
						+	"			FROM " + _estimateitem + " where estimate_id = %s)    ",
						paramValue("ID")
							)
					: ""; 
		}
		void addMoreTermDataDefs() {
			m_termDataDefs.put("availItems", new DataDef() {  
				{
					setStatementHolder(new ExprHolder() {
						public String getSelectiveExpr(String mode) {	
							return String.format(
										"	SELECT DISTINCT intrv.id, " + _treatment + ".descr treatment, " + _treatment + ".id treatment_id,	"
										+ "				" + _tooth + ".symbol tooth, intrv.execution, intrv.price  								"
										+ "	FROM " + _intervention + " intrv                                                                    "
										+ "	INNER JOIN " + _tooth + " ON " + _tooth + ".id = intrv.tooth_id                                     "
										+ "	INNER JOIN " + _treatment + " ON " + _treatment + ".id = intrv.treatment_id                         "
										+ "	INNER JOIN " + _clinicalcard + " cl ON intrv.clinicalcard_id = cl.id                                "
										+ "	INNER JOIN " + _patientcustomer + " pc ON pc.patient_id = cl.patient_id								"
										+ "	INNER JOIN " + _customer + " c ON c.id = pc.customer_id										        "
										+ "	WHERE pc.patcust_id = %1$s                                                                          "
										+ "		AND intrv.id NOT IN (                                                                           "
										+ "			SELECT DISTINCT ii.intervention_id                                                          "
										+ "			FROM " + _invoice + " i                                                                     "
										+ "			INNER JOIN " + _invoiceitem + " ii ON ii.invoice_id = i.id                                  "
										+ "			INNER JOIN " + _intervention + " intrv ON intrv.id = ii.intervention_id                     "
										+ "			INNER JOIN " + _clinicalcard + " cl ON intrv.clinicalcard_id = cl.id                        "
										+ "			INNER JOIN " + _patientcustomer + " pc ON pc.patient_id = cl.patient_id						"
										+ "			INNER JOIN " + _customer + " c ON c.id = pc.customer_id								        "
										+ "			WHERE pc.patcust_id = %1$s                                                               	"
										+ "			)                                                                                           ",
										paramValue("patientCustomerID")) 
									+ furtherExclusionClause(); 										
						} });
				}
			});
		};
		boolean isViewer() {return false;};
		String moreSelectTerms() { return "";};
		String fromClauseExtension(String mode) { return isOpenNormal(mode) ? 
				" and pc.patcust_id = " + paramValue("patientCustomerID") : 
				" and pc.patient_id = " + paramValue("patientID");};
	}

	public class InvoiceNamer {
		public InvoiceNamer(boolean forEstimating) {
			m_forEstimating = forEstimating;
		}

		boolean m_forEstimating;

		public String tableName() {
			return m_forEstimating ? _estimate : _invoice;
		}

		public String itemTableName() {
			return m_forEstimating ? _estimateitem : _invoiceitem;
		}

		public String tableRefIdFieldName() {
			return m_forEstimating ? "estimate_ID" : "invoice_ID";
		}
	}

	class InvoiceDataPanel_DataDef extends DataDef {  
		boolean m_forEstimating;
		boolean m_isViewer;
		InvoiceDataPanel_DataDef (boolean forEstimating, PanelDataDef parentPanelDataDef)	{
			m_forEstimating = forEstimating;
			final InvoiceNamer iNamer = new InvoiceNamer(forEstimating);
			setStatementHolder(new ExprHolder() {
				public String getSelectiveExpr(String mode) {	
					return String.format(
							"	SELECT ii.id, tr.descr treatment, " + _tooth + ".symbol tooth, intrv.execution, intrv.price %1$s   							" +
							"	FROM (SELECT ii.* FROM " + iNamer.tableName() + " i INNER JOIN " + iNamer.itemTableName() + " ii 							" +
							"														ON i.id = ii." + iNamer.tableRefIdFieldName() + "  						" +
							"						WHERE i.id = %2$s) ii                                           										" +
							"			INNER JOIN " + _intervention + " intrv  on intrv.id = ii.intervention_id 	         								" +
							"			INNER JOIN " + _tooth + " on " + _tooth + ".id = intrv.tooth_id                                						" +
							"			INNER JOIN " + _treatment + " tr on tr.id = intrv.treatment_id	%3$s												",
							(m_forEstimating ? ", re.percentage" : ""), 
							paramValue("ID"),
							(m_forEstimating ? 
									String.format("LEFT JOIN " + _reimbursement + " re on tr.id = re.treatment_id  and re.reimbursementplan_id = %s",
											paramValue("reimbursementplan_id"))
									: "")
							);
				} });
			parentPanelDataDef.m_termDataDefs.put("insurance", new DataDef() {
				{
					setStatementSql("Select i.* from " + _insurance + " i inner join " + _patientcustomer + " pp on i.customer_id = pp.customer_id and pp.patcust_id = %s");
					addContextParamName("patientCustomerID"); 
				}
			});
		}
	};
	
	protected void customInit() {
		if (m_connGrabber != null) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Date yesterday = null;
					long lastMillis = System.currentTimeMillis();
					while (true) {
						if (System.currentTimeMillis() - lastMillis > 30000) {
							Calendar now = Calendar.getInstance();
							now.setTime( new Date(System.currentTimeMillis()));
							if (now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 0) {
								 yesterday = new Date(System.currentTimeMillis() - 1000 * 3600 * 24);
								try {
									Connection conn = m_connGrabber.acquireConnection(true, false);
									Statement stmnt = conn.prepareStatement(new String("fictitious"));
									stmnt.executeUpdate("Delete from " + _semanticsChecker + " where day < '" + yesterday.toString() + "'");
									stmnt.close();
									conn.close();
								} catch (Throwable th) {
									int fictitious = 1;
								}
							}
							lastMillis = System.currentTimeMillis();
						}
					}
				}
			});
			thread.start();
		}
	}


	protected void loadDefs() {
		super.loadDefs();
		m_literalSubsts.put("D_1", _daypart);
		m_literalSubsts.put("D_2", _armchair);
		m_literalSubsts.put("D_3", _tooth);
		m_literalSubsts.put("D_4", _state);
		m_literalSubsts.put("D_5", _country);
		m_literalSubsts.put("D_6", _insurancetype);
		m_literalSubsts.put("D_7", _treatment);
		m_literalSubsts.put("D_8", _insurancecompany);
		m_literalSubsts.put("D_9", _practitioner);
		m_literalSubsts.put("D_10", _reimbursementplan);
		m_literalSubsts.put("D_12", _quadrant);
		
		m_literalSubsts.put("A_1", _appointment);
		m_literalSubsts.put("A_4", _appointminterv);
		m_literalSubsts.put("A_5", _waitinglist);
		
		m_dialogDataDefs.put("org.odontior.PersonSearcher", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {   
					{
						setStatementHolder(new ExprHolder() {
							public String getSelectiveExpr(String mode) {	return selectStmnt(mode.compareTo("patients") == 0 ? _patient : _customer); }
						});
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.AppointmentDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSetHolder(new ExprHolder() {
							public String getSelectiveExpr(String mode) {	
								switch (mode) {
								case "contextSpecific":
									return String
											.format("		SELECT " + _appointment + ".*                         "
													+ "		FROM " + _appointment + "                             "
													+ "		WHERE location_id = %1$s                              "
													+ "			AND DATE = %2$s                                   "
													+ "			AND daypart_id = %3$s                             ",
													paramValue("locationID"),
													paramValue("date"),
													paramValue("dayPartID"));
								default:
									return String
											.format("		SELECT " + _appointment + ".*                         "
													+ "		FROM " + _appointment + "                             "
													+ "		WHERE id = %1$s ",
													paramValue("appointmentID"));
								}	
							}
						});
						setStatementHolder(new ExprHolder() { 
							public String getSelectiveExpr(String mode) { 
								return "  SELECT a.*, i.treatment_id, i.tooth_id, p.lastname, p.firstname    "
										+ "	FROM (  "
										+ getUpdatableSet(mode)
										+ " ) a "
										+ "	INNER JOIN " + _patient + " p ON a.patient_id = p.id                 "
										+ "	LEFT JOIN " + _intervention + " i ON a.intervention_id = i.id        ";
								} 
						});
						setSharingAlias("a");
						m_termDataDefs.put("Interventions", new DataDef() {
							{
								setStatementSql("	SELECT a.*, i.duration, i.treatment, i.tooth, not ISNULL(i.execution) done, i.diagtime    "
												+ "	FROM " + _appointminterv + " a                                                            "
												+ "	LEFT JOIN (                                                                               "
												+ "		SELECT ii.id, o.symbol treatment, t.symbol tooth, ii.execution,                       "
												+ "						ii.duration, ii.timestamp diagtime                       			  "
												+ "		FROM " + _intervention + " ii                                                         "
												+ "		LEFT JOIN " + _treatment + " o ON ii.treatment_id = o.id                              "
												+ "		LEFT JOIN " + _tooth + " t ON ii.tooth_id = t.id                                      "
												+ "		) i ON a.intervention_id = i.id                                                       "
												+ " 	WHERE a.appointment_id = %s");
								addContextParamName("appointmentID");
							}
						});
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.ImageDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_image);
						setStatementHolder(new ExprHolder() { public String getExpr() { return selectedContextQuery(getUpdatableSet(null)); } });
						setSharingAlias("i");
						m_termDataDefs.put("image", new DataDef() {
							{
								setUpdatableSet(_image);
							}
						});
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.InterventionDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet( _intervention);
						setStatementHolder(new ExprHolder() {
							@Override
							public String getSelectiveExpr(String mode) {								
								boolean isInterventionSpecific = mode.compareTo("interventionSpecific") == 0;
								switch (mode) {
								case "contextSpecific":
									return selectedContextQuery(getUpdatableSet(mode));
								case "asSelector":
								case "interventionSpecific":
								{
									String whereClause = String.format("WHERE %1$s = %2$s", 
											isInterventionSpecific ? "i.id" : "p.id",
											isInterventionSpecific ? 
															paramValue("interventionID"):
															paramValue("patientID"));
									return String
											.format("	SELECT *                                                    	"
													+ "	FROM " + _intervention + " i                                	"
													+ "	INNER JOIN (                                                	"
													+ "		SELECT c.id ccard_id, c.number                          	"
													+ "		FROM " + _patient + " p                                 	"
													+ "		INNER JOIN " + _clinicalcard + " c ON p.id = c.patient_id   "
													+ "		%1$s                                       					"
													+ "		) c ON i.clinicalcard_id = c.ccard_id                   	"
													+ "	%2$s                   											",
													isInterventionSpecific ? "" : whereClause,
													isInterventionSpecific ? whereClause : "");
								}
								}
								return null;
							}
						});
						setSharingAlias("i");
						}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.CCardDetailsDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setStatementSql(
								"	SELECT *				                                                               " +
								"	FROM (                                                                                 " +
								"		SELECT clinicalcard_id, patient_id, tooth_id, count(executed) executed,    		   " +
								"				count(unexecuted) unexecuted, count(images) images                         " +
								"		FROM " + _clinicalcardcounts + "                                                   " +
								"		WHERE clinicalcard_id = %1$s                                                       " +
								"			OR %2$s = 1                                                                    " +
								"			AND patient_id = %3$s                                                          " +
								"		GROUP BY tooth_id                                                                  " +
								"		) a                                                                                " +
								"	RIGHT JOIN " + _clinicalcardschema + " b ON a.tooth_id = b.id                           " +
								"	ORDER BY ID                                                                            ");
						addContextParamName("clinicalCardID");
						addContextParamName("UniqueCcard");
						addContextParamName("patientID");
						setNoSharingClause();
						m_termDataDefs.put("Images", new DataDef() {
											{
												setUpdatableSet(_image);
												setStatementHolder(new ExprHolder() {
													public String getExpr() {	return selectedContextQuery(_image); }
												});
											}
										});
						m_termDataDefs.put("Interventions", new DataDef() {
											{
												setUpdatableSet( _intervention);
												setStatementHolder(new ExprHolder() {
													public String getExpr() {	return selectedContextQuery( _intervention); }
												});
											}
										});
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.ClinicalCardDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {  
					{
						setUpdatableSet(_clinicalcard);
						setStatementHolder(new ExprHolder() { 
							public String getSelectiveExpr(String mode) { 
								return "Select * from " + _clinicalcard + (mode.compareTo("specific") == 0 ? 
										(" where id = " + paramValue("clinicalCardID")) : "");
								} 
						});
					}
				});
			}
		});

		m_dialogDataDefs.put("org.odontior.OrganizerDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {  
					{
						setUpdatableSet(_armchair);
						setSqlToDefault();
						m_termDataDefs.put("Appointments", new DataDef() {
							{
								setStatementSql("	SELECT d.*, e.id appointment_id, e.firstname, e.lastname, e.duration, i.treatment, i.tooth,  "
											+ "				oo.NAME practitioner, e.patient_id, e.macro,                     			    	"
											+ "				not isnull(i.execution) done                                                    	"
											+ "		FROM " + _daypart + " d                                                                 	"
											+ "		LEFT JOIN (                                                                             	"
											+ "			SELECT a.*, c.firstname, c.lastname                                                 	"
											+ "			FROM " + _appointment + " a                                                             "
											+ "			INNER JOIN " + _patient + " c ON a.patient_id = c.id                                    "
											+ "			WHERE DATE = %1$s                                                                   	"
											+ "				AND location_id = %2$s                                                          	"
											+ "			) e ON e.daypart_id = d.id                                                         	 	"
											+ "		LEFT JOIN (                                                                             	"
											+ "			SELECT ii.id, o.symbol treatment, t.symbol tooth, ii.execution                      	"
											+ "			FROM " + _intervention + " ii                                                           "
											+ "			LEFT JOIN " + _treatment + " o ON ii.treatment_id = o.id                                "
											+ "			LEFT JOIN " + _tooth + " t ON ii.tooth_id = t.id                                        "
											+ "			) i ON e.intervention_id = i.id                                                     	"
											+ "		LEFT JOIN " + _practitioner + " oo ON e.practitioner_id = oo.id                             "
											+ "		order by d.id															                	");
								addContextParamName("Day");
								addContextParamName("LocationID");
							}
						});
					}
				});
			}
		});
	
		m_dialogDataDefs.put("org.odontior.WaitlistDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {  
					{
						setUpdatableSet(_waitinglist);
						setStatementSql("		SELECT a.*, b.*                                                         "
										+ "		FROM " + _waitinglist + " a                                             "
										+ "		INNER JOIN (                                                            "
										+ "			SELECT e.*, i.treatment, i.tooth, oo.name practitioner              "
										+ "			FROM (                                                              "
										+ "				SELECT a.id, a.intervention_id, a.macro, a.duration,       		"	// avoiding a.* for sharingKey collision
										+ "						a.patient_id, a.practitioner_id, c.firstname,           "
										+ "						c.lastname                    				            "
										+ "				FROM " + _appointment + " a                                     "
										+ "				INNER JOIN " + _patient + " c ON a.patient_id = c.id            "
										+ "				) e                                                             "
										+ "			LEFT JOIN (                                                         "
										+ "				SELECT ii.id, o.symbol treatment, t.symbol tooth                "
										+ "				FROM " + _intervention + " ii                                   "
										+ "				LEFT JOIN " + _treatment + " o ON ii.treatment_id = o.id        "
										+ "				LEFT JOIN " + _tooth + " t ON ii.tooth_id = t.id                "
										+ "				) i ON e.intervention_id = i.id                        			"
										+ "			LEFT JOIN " + _practitioner + " oo ON e.practitioner_id = oo.id     "
										+ "			) b ON a.ExtID = b.ID                            		   			");
					}
				});
			}
		});

		
		m_dialogDataDefs.put("org.odontior.ArmchairDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_armchair);
						setSqlToDefault();
					}
				});
			}
		});
		
		m_patientPanelDataDef = new PanelDataDef() { 
			{
				setUpdatableSet(_patient);
				setSqlToDefault();
				m_termDataDefs.put("appointments", new DataDef() {
					{
						setUpdatableSet(_appointment);
						setStatementSql("	SELECT d.*, e.id appointment_id, e.date, e.duration, i.treatment, i.tooth,    	" +
										"	oo.NAME practitioner, e.patient_id, e.macro, e.name seat,                    	" +				
										"	e.location_id, e.dayPart_id, not isnull(i.execution) done                    	" +     
										"	FROM " + _daypart + " d                                                         " +             
										"	INNER JOIN (                                                                 	" +              
										"		SELECT a.*, c.firstname, c.lastname, armc.name                           	" +                        
										"		FROM " + _appointment + " a                                              	" +             
										"		INNER JOIN " + _patient + " c ON  a.patient_id = c.id and a.patient_id = %s " +
										"		inner join " + _armchair + " armc on a.location_id = armc.id             	" +                 
										"		) e ON e.daypart_id = d.id                                               	" +             
										"	LEFT JOIN (                                                                  	" +              
										"		SELECT ii.id, o.symbol treatment, t.symbol tooth, ii.execution           	" +             
										"		FROM " + _intervention + " ii                                               " +             
										"		LEFT JOIN " + _treatment + " o ON ii.treatment_id = o.id                    " +             
										"		LEFT JOIN " + _tooth + " t ON ii.tooth_id = t.id                            " +             
										"		) i ON e.intervention_id = i.id                                          	" +             
										"	LEFT JOIN " + _practitioner + " oo ON e.practitioner_id = oo.id                 " +  
										"	%s																			 	" +
										"	order by e.date, d.id														 	");
						addContextParamName("patientID");
						addParamActuator(new ParamActuator() {
							public String getValue() {
								return paramValue("todayOnward").compareTo("1") == 0 ? "WHERE e.date >= date(now()) " : "";
							}
						});		
					}
				});
				m_termDataDefs.put("queuedEvents", new DataDef() {
					{
						setUpdatableSet(_appointment);
						setStatementSql("	SELECT b.*                                                                        	   " +
										"	FROM " + _waitinglist + " a                                                            " +
										"	INNER JOIN (                                                                           " +
										"		SELECT e.*, i.treatment, i.tooth, oo.name practitioner                             " +
										"		FROM (                                                                             " +
										"			SELECT a.*                                                                     " +
										"			FROM " + _appointment + " a                                                    " +
										"			INNER JOIN " + _patient + " c ON a.patient_id = c.id  AND a.patient_id = %s    " +    
										"			) e                                                                            " +
										"		LEFT JOIN (                                                                        " +
										"			SELECT ii.id, o.symbol treatment, t.symbol tooth                               " +
										"			FROM " + _intervention + " ii                                                  " +
										"			LEFT JOIN " + _treatment + " o ON ii.treatment_id = o.id                       " +
										"			LEFT JOIN " + _tooth + " t ON ii.tooth_id = t.id                               " +
										"			) i ON e.intervention_id = i.id                                                " +
										"		LEFT JOIN " + _practitioner + " oo ON e.practitioner_id = oo.id                    " +
										"		) b ON a.ExtID = b.ID										              			");
						addContextParamName("patientID");
					}
				});
				m_termDataDefs.put("ClinicalCards", new DataDef() {
					{
						setUpdatableSet(_clinicalcard);
						setSqlToDefault();
					}
				});
			}
		};
		
		m_dialogDataDefs.put("org.odontior.PatientDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_patientPanelDataDef);
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_patientcustomer);
						setStatementSql("select pc.*, c.lastname, c.firstname from " + _patientcustomer + " pc inner join " + _customer + " c on pc.customer_id = c.id");
						setSharingAlias("pc");
					}
				});
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_reservednote);
						setSqlToDefault();
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.PatientDetailsDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_patientPanelDataDef);
			}
		});
		
		m_customerPanenDataDef = new PanelDataDef() {   
			{
				setUpdatableSet(_customer);
				setSqlToDefault();
				m_termDataDefs.put("Insurances", new DataDef() {
					{
						setStatementSql(
										"	SELECT i.*, c.company, t.description type                  		          " +
										"	FROM (                                                                    " +
										"		SELECT i.*                                                            " +
										"		FROM " + _insurance + " i                                             " +
										"		WHERE i.customer_id = %1$s                                            " +
										"		) i                                                                   " +
										"	INNER JOIN " + _insurancecompany + " c ON i.insurancecompany_id = c.id    " +
										"	LEFT JOIN " + _insurancetype + " t ON t.id = i.type_id                    "
						);
						addContextParamName("customerId");
					}
				});
				m_termDataDefs.put("Patients", new DataDef() {
					{
						setStatementSql(
								"Select p.*, pp.isThePatient, pp.relationship, pp.patcust_id " +
								"from " + _patient + " p inner join " + _patientcustomer + " pp on p.id = pp.patient_id where pp.customer_id = %s"
						);
						addContextParamName("customerId");
					}
				});
			}
		};
		
		m_dialogDataDefs.put("org.odontior.CustomerDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_customerPanenDataDef);
			}
		});
		m_dialogDataDefs.put("org.odontior.CustomerDetailsDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_customerPanenDataDef);
			}
		});
		m_dialogDataDefs.put("org.odontior.PractitionerDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_practitioner);
						setSqlToDefault();
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.ReimbursementDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {  
					{
						setUpdatableSet(_reimbursement);
						setStatementSql(
								"	SELECT t.*, r.id reimbursement_id, r.percentage,	         " +
								"   r.reimbursementplan_id, r.treatment_id				         " +
								"	FROM (                                                       " +
								"		SELECT r.*                                               " +
								"		FROM " + _reimbursement + " r                            " +
								"		WHERE r.reimbursementplan_id = %1$s                      " +
								"		) r                                                      " +
								"	RIGHT JOIN " + _treatment + " t ON t.id = r.treatment_id     "
						);
						addContextParamName("reimbursementPlanID");
						setSharingAlias("t");
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.ReimbursementPlanDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {
					{
						setUpdatableSet(_reimbursementplan);
						setStatementSql(
								"	SELECT e.*                    				" +                     
								"	FROM " + _reimbursementplan + " e      		" +                    
								"	WHERE e.insurancecompany_id = %s			" 						
								);
						addContextParamName("companyID");
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.TreatmentDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_treatment);
						setSqlToDefault();
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.TeethNamingDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_tooth);
						setSqlToDefault();
						setNoSharingClause();
					}
				});
			}
		});		
		m_dialogDataDefs.put("org.odontior.QuadrantsNamingDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() { 
					{
						setUpdatableSet(_quadrant);
						setSqlToDefault();
						setNoSharingClause();
					}
				});
			}
		});		
		m_dialogDataDefs.put("org.odontior.CustomerInvoicesDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new InvoiceEditing_PanelDataDef(false));
			}
		});
		m_dialogDataDefs.put("org.odontior.CustomerEstimatesDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new InvoiceEditing_PanelDataDef(true));
			}
		});
		m_insuranceCompanyPanelDataDef = new PanelDataDef() {
			{
				setUpdatableSet(_insurancecompany);
				setSqlToDefault();
				m_termDataDefs.put("ReimbursementPlans", new DataDef() {
					{
						setStatementSql(
								"	SELECT e.*                    				" +                     
								"	FROM " + _reimbursementplan + " e      		" +                    
								"	WHERE e.insurancecompany_id = %s			"
								);
						addContextParamName("ID");
					}
				});
			}
		};
		m_dialogDataDefs.put("org.odontior.InsuranceCompanyDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_insuranceCompanyPanelDataDef);
			}
		});
		m_dialogDataDefs.put("org.odontior.InsuranceCompanyDetailsDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(m_insuranceCompanyPanelDataDef);
			}
		});
		m_dialogDataDefs.put("org.odontior.InsuranceDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new PanelDataDef() {
					{
						setUpdatableSet(_insurance);
						setStatementSql("	SELECT i.*                             		" +
										"	FROM " + _insurance + " i                   " +
										"	WHERE i.customer_id = %s   				    " );
						addContextParamName("customerId");
					}
				});
			}
		});
		m_dialogDataDefs.put("org.odontior.InvoiceDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new Invoice_PanelDataDef(false));
			}
		});
		m_dialogDataDefs.put("org.odontior.EstimateDialog", new DialogDataDef() {
			{			
				m_panelDataDefs.add(new Invoice_PanelDataDef(true));
			}
		});
	}
	
	public long setInvoiceNumber(Long input, Date date) throws SQLException {
		return setInvoiceNumber(input, date, null);
	}
	
	public long setInvoiceNumber(Long input, Date date, String sharingKey) throws SQLException {
		long retVal = 0;
		boolean shared = sharingKey != null;
		String stmnt = shared ? "{call " + _setinvoicenumbershared + "(?, ?, ?, ?)}" : "{call " + _setinvoicenumber + "(?, ?, ?)}";
		CallableStatement cs = m_conn.prepareCall(stmnt);
		cs.setLong(1, input);
		cs.setDate(2, date);	
		cs.registerOutParameter(3, Types.INTEGER);
		if (shared) 
			cs.setString(4, sharingKey);
		cs.execute();
		retVal = cs.getLong(3);
		cs.close();
		return retVal;
	}

	public long setCardNumber(Long patientId) throws SQLException {
		long retVal = 0;
		String stmnt = "{call " + _setcardnumber + "(?, ?)}";
		CallableStatement cs = m_conn.prepareCall(stmnt);
		cs.setLong(1, patientId);
		cs.registerOutParameter(2, Types.INTEGER);
		cs.execute();
		retVal = cs.getLong(2);
		cs.close();
		return retVal;
	}
	
	public void accessSemanticsChecker(Date date, Long dayPartId, Long locationId, Long patientId, 
										Long practitionerId, Long duration, Long adding) throws SQLException {
		accessSemanticsChecker( date, dayPartId, locationId, patientId, practitionerId, duration, adding, null);
	}
	
	public void accessSemanticsChecker(Date date, Long dayPartId, Long locationId, Long patientId, 
										Long practitionerId, Long duration, Long adding, String sharingKey) throws SQLException {
		boolean shared = sharingKey != null;
		final int SLOT_MINUTES = 5;
		int occupiedSlots = (int) (duration / SLOT_MINUTES + 1 + (duration % SLOT_MINUTES > 0 ? 1 : 0));
		Statement stmnt = m_conn.prepareStatement(new String("fictitious"));
		if (adding==1)
			for (int i = 0; i < occupiedSlots; i++)
				stmnt.executeUpdate("Insert into " + _semanticsChecker + "(day, part_id, location_id, patient_id, practitioner_id " + (shared ? ", sharingkey" : "") + ")" +
																" values ('" + date.toString() + "', " + 
																			(dayPartId + i) + ", " + 
																			locationId + ", " + 
																			patientId + ", " + 
																			(practitionerId == null ? "null" : practitionerId)  + 
																			(shared ? (", '" + sharingKey) + "'" : "") + 
																		")");
		else
			stmnt.executeUpdate("Delete from " + _semanticsChecker + " where day = '" + date.toString() + "' and " + 
					"part_id >= " + dayPartId + " and " + 
					"part_id < " + (dayPartId + occupiedSlots) + 
					" and location_id = " + locationId + 
					" and patient_id = " + patientId + 
					(shared ? (" and sharingkey = '" + sharingKey + "'") : ""));
		stmnt.close();
	}

	public void accessInterventionChecker(Long interventionId, Long adding) throws SQLException {
		accessInterventionChecker(interventionId, adding, null);
	}
	
	public void accessInterventionChecker(Long interventionId, Long adding, String sharingKey) throws SQLException {
		boolean shared = sharingKey != null;
		Statement stmnt = m_conn.prepareStatement(new String("fictitious"));
		if (adding == 1)
			stmnt.executeUpdate("Insert into " + _interventionChecker + "(intervention_id " + (shared ? ", sharingkey" : "") + ")" +
																" values (" +  interventionId + (shared ? (", '" + sharingKey) + "'" : "") + ")");
		else
			stmnt.executeUpdate("Delete from " + _interventionChecker + " where intervention_id = " + interventionId + 
																				(shared ? (" and sharingkey = '" + sharingKey + "'") : ""));		
		stmnt.close();
		
	}
}
