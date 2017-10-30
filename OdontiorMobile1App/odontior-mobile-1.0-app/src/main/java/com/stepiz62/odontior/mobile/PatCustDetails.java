/*
	Copyright (c) 2015, Stefano Pizzocaro. All rights reserved. Use is subject to license terms.

	This file is part of Odontior Mobile 1.0.

	Odontior Mobile 1.0 is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	Odontior Mobile 1.0 is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public License
	along with Odontior Mobile 1.0.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.stepiz62.odontior.mobile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import org.joty.common.BasicPostStatement;
import org.joty.common.JotyTypes;
import org.joty.data.WrappedField;
import org.joty.mobile.app.JotyApp;
import org.joty.mobile.data.WResultSet;
import org.joty.mobile.gui.CheckTerm;
import org.joty.mobile.gui.DataDetailsActivity;
import org.joty.mobile.gui.Term;
import org.joty.mobile.gui.TextTerm;

public class PatCustDetails extends DataDetailsActivity {
    TextTerm m_customer, m_relationship, m_patient;
    CheckTerm m_isThePatient;
    String m_patientName;
    boolean m_oldIsThePatient = false;
    private String m_customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_patientName = m_detailsController.m_extras.getString("patientname");
        setContentView(R.layout.activity_patcust_details);
        m_customer = new TextTerm(this, JotyTypes._long, R.id.relatedcustomer, "customer_id", true);
        if (!m_newRecord) {
            m_customerName = m_extras.getString("relatedCustomer");
                m_customer.setVal(m_customerName);
        }

        m_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseCustomer();
            }
        });

        m_isThePatient = new CheckTerm(this, R.id.isThePatient, "isThePatient");
        m_isThePatient.defaultValue().setValue(0);
        m_relationship = new TextTerm(this, JotyTypes._text, R.id.relationship, "relationship");
        m_patient = new TextTerm(this, JotyTypes._long, R.id.patient_id, "patient_id", true);
        m_patient.defaultValue().setValue(Long.parseLong(m_detailsController.m_extras.getString("patientId")));
        Button customerDetails = (Button) findViewById(R.id.lookAtCustomer);
        customerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!m_customer.isNull())
                    openCustomerActivity(String.valueOf(m_customer.getInteger()));
            }
        });
        customerDetails.setText(m_app.m_common.appLang("Details"));
        for (Term term : m_terms)
            term.m_required = true;
        setTitle(m_app.m_common.appLang("RelatedCustomers") + " - " + m_patientName);
    }

    void openCustomerActivity(String id) {
        openDetails(id, CustomerDetails.class, "com.stepiz62.odontior.CustomerDialog", id == null ? R.id.relatedcustomer : 0);
    }

    @Override
    protected void setEditing(boolean truth) {
        super.setEditing(truth);
        if (truth)
            m_oldIsThePatient = m_isThePatient.m_chkBox.isChecked();
    }

    @Override
    public void addExtrasInOpenActivity(Bundle extras) {
        super.addExtrasInOpenActivity(extras);
        extras.putString("patientName", m_patientName);
        extras.putString("relatedCustomer", m_customer.m_strVal);
    }

    @Override
    public void forwardExtraExtras() {
         m_extras.putString("relatedCustomer", m_customerName);
    }

    @Override
    protected void saveEffects() {
        WrappedField lastGotNameWField = m_app.m_valuesOnChoice.get("lastName");
        if (lastGotNameWField != null)
            m_customerName = m_app.m_valuesOnChoice.get("lastName").m_strVal + " " + m_app.m_valuesOnChoice.get("firstName").m_strVal;
     }

    private void browseCustomer() {
        m_selectionTarget = m_termMap.get(R.id.relatedcustomer);
        guiDataExchange();
        m_app.openSearcherAsSelector(Customers.class, new String[]{"lastName", "firstName"});
        enableWidgets(true);
    }

    @Override
    protected void selectionCallBack(Term target) {
        target.setInteger(m_app.m_valuesOnChoice.getId());
        target.m_strVal = m_app.m_valuesOnChoice.auxFields() ?
                m_app.m_valuesOnChoice.get("lastName").m_strVal + " " + m_app.m_valuesOnChoice.get("firstName").m_strVal :
                m_patientName;
        if (!m_app.m_valuesOnChoice.auxFields()) {
            m_isThePatient.m_chkBox.setChecked(true);
            completeIsThePatientSetting(true);
        }
    }

    @Override
    public void onWidgetClick(View view) {
        super.onWidgetClick(view);
        if (view.getId() == R.id.isThePatient)
            manageIsThePatient((CheckBox) view);
    }

    private void manageIsThePatient(final CheckBox checkBox) {
         if (checkBox.isChecked()) {
             RelatedpersonsListFragment listFragment = (RelatedpersonsListFragment) m_mainActivity.m_resultActivity.m_listFragment;
             if (listFragment.m_isThePatientExists && ! m_oldIsThePatient) {
                 m_app.warningMsg(m_app.m_common.appLang("PatientAsCustomerExists"));
                 checkBox.setChecked(false);
             } else {
                 m_app.yesNoQuestion(m_app.m_common.appLang("WantPatientAsCustomer"), new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         if (which == DialogInterface.BUTTON_POSITIVE)
                             doTheSetting(true);
                         else
                             checkBox.setChecked(false);
                     }
                 });
             }
        } else
            doTheSetting(false);
    }

    private void doTheSetting(boolean isThePatient) {
        if (isThePatient) {
            AccessorCoordinates accessorCoordinates = new AccessorCoordinates(m_mainActivity.m_accessorCoordinates.paramContext);
            accessorCoordinates.jotyDialogClassName = "com.stepiz62.odontior.CustomerDialog";
            BasicPostStatement postStatement = m_mainActivity.createContextPostStatement(accessorCoordinates);
            int blankIdx = m_patientName.indexOf(" ");
            postStatement.m_mainFilter = "lastname = '" + m_patientName.substring(0, blankIdx) + "' and firstname = '" + m_patientName.substring(blankIdx + 1) + "'";
            JotyApp.QueryResponseHandlersManager respManager = m_app.new QueryResponseHandlersManager(postStatement, m_app.new QueryResponseHandler() {
                @Override
                public void handleQuery(boolean result, WResultSet rs, BasicPostStatement postStatement) {
                    boolean initNewCustomer = false;
                    if (result) {
                        if (rs.isEOF()) {
                            result = false;
                            initNewCustomer = true;
                        } else {
                            m_customer.setVal(rs.integerValue("id"));
                            m_customer.setDirty();
                            m_customer.m_strVal = m_patientName;
                            m_customer.guiDataExch(false);
                         }
                    }
                    completeIsThePatientSetting(result);
                    if (initNewCustomer) {
                        m_app.m_valuesOnChoice.clear();
                        m_selectionTarget = m_termMap.get(R.id.relatedcustomer);
                        openCustomerActivity(null);
                    }
                }
            });
            respManager.open();
        } else
            completeIsThePatientSetting(true);
    }

    void completeIsThePatientSetting(boolean done) {
        if (done) {
            m_relationship.clear();
            if (m_isThePatient.m_chkBox.isChecked()) {
                m_relationship.setVal(m_app.m_common.appLang("Oneself"));
                m_relationship.setDirty();
                m_relationship.guiDataExch(false);
            }
        } else
            m_isThePatient.m_chkBox.setChecked(false);
        enableWidgets(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void enableWidgets(boolean truth) {
        super.enableWidgets(truth);
        boolean customerMissing = m_customer.isNull();
        m_relationship.enable(truth && !m_isThePatient.m_chkBox.isChecked() && !customerMissing);
        m_customer.enable(truth && !m_isThePatient.m_chkBox.isChecked());
    }

}
