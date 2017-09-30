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

package org.odontior.mobile;

import android.os.Bundle;
import android.view.View;

import org.joty.common.BasicPostStatement;
import org.joty.common.JotyTypes;
import org.joty.data.JotyDate;
import org.joty.data.WrappedField;
import org.joty.mobile.data.WResultSet;
import org.joty.mobile.gui.CheckTerm;
import org.joty.mobile.gui.DataDetailsActivity;
import org.joty.mobile.gui.Term;
import org.joty.mobile.gui.TextTerm;


public class Appointment extends DataDetailsActivity {

    TextTerm m_date, m_duration, m_dayPart, m_location, m_patient;
    private long m_oldDuration, m_oldPatient_id;
    private String m_lastName, m_firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        m_date = new TextTerm(this, JotyTypes._date, R.id.date, "date", true);
        m_date.setAuxView(R.id.dow);
        m_dayPart = new TextTerm(this, R.id.time, "daypart_id", "dayparts");
        m_location = new TextTerm(this, R.id.armchair, "location_id", "armchairs");
        m_patient = new TextTerm(this, JotyTypes._long, R.id.patient, "patient_id", true);
        if (m_extras != null) {
            m_lastName = m_extras.getString("lastName");
            m_firstName = m_extras.getString("firstName");
            m_patient.setVal(m_lastName + " " + m_firstName);
        }
        m_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_selectionTarget = m_termMap.get(R.id.patient);
                guiDataExchange();
                m_app.openSearcherAsSelector(Patients.class, new String[]{"lastName", "firstName"});
            }
        });
        m_patient.m_required = true;
        m_duration = new TextTerm(this, JotyTypes._long, R.id.duration, "duration");
        m_duration.m_required = true;
        CheckTerm macro = new CheckTerm(this, R.id.macro, "macro");
        macro.defaultValue().setValue(0);
        setLangText(R.id.dayLbl, "Day");
        setLangText(R.id.dateLbl, "Date");
        setLangText(R.id.timeLbl, "Time");
        setLangText(R.id.armchairLbl, "Armchair");
        setLangHint(R.id.patient, "Patient");
        setLangHint(R.id.duration, "Duration");
        setText(R.id.minutesLbl, "(" + m_app.m_common.appLang("Minutes") + ")");
        setContextActivity(this);
        m_accessorMode = true;
        m_oldDuration = 0;
        m_oldPatient_id = 0;
        setContextValueFromParam(m_date, "date");
        setContextValueFromParam(m_dayPart, "daypartid");
        setContextValueFromParam(m_location, "LocationID");
        setLangTitle("Appointment");
    }

    @Override
    public void forwardExtraExtras() {
        m_extras.putString("lastName", m_lastName);
        m_extras.putString("firstName", m_firstName);
    }

    @Override
    protected void setEditing(boolean truth) {
        super.setEditing(truth);
        m_oldDuration = m_duration.integerVal();
        m_oldPatient_id = m_patient.integerVal();
    }

    @Override
    protected void selectionCallBack(Term target) {
        // if (target.m_resId == R.id.patient) .......
        target.setInteger(m_app.m_valuesOnChoice.getId());
        target.m_strVal = m_app.m_valuesOnChoice.get("lastName").m_strVal + " " + m_app.m_valuesOnChoice.get("firstName").m_strVal;
        // end if
    }

    @Override
    public boolean costraintViolationMsgOnUpdate() {
        boolean retVal = ((OdontiorApp) m_app).organizerCostraintViolationMsgOnUpdate();
        if( ! retVal)
            retVal = super.costraintViolationMsgOnUpdate();
        return retVal;
    }


    long handlesQty(long duration) {
        final int startUpTime = 5;
        duration += startUpTime;
        long qty = (duration / Organizer.SLOT_MINUTES) + ((duration % Organizer.SLOT_MINUTES) > 0 ? 1 : 0);
        return qty <= 0 ? 1 : qty;
    }

    @Override
    protected boolean doStoreData(boolean result, WResultSet rs, BasicPostStatement postStatement) {
        boolean retVal = m_dayPart.integerVal() - 1 + handlesQty(m_duration.integerVal()) <= Organizer.SLOT_SET_SIZE;
        if (retVal) {
            if (!m_newRecord)
                accessSemanticsChecker(m_oldDuration, m_oldPatient_id, false);
            retVal = super.doStoreData(result, rs, postStatement);
            if (retVal)
                accessSemanticsChecker(m_duration.integerVal(), m_patient.integerVal(), true);
        } else
            m_app.toast(m_app.m_common.appLang("DurationExceeds"));
        return retVal;
    }

    @Override
    protected void doDeletion() {
        super.doDeletion();
        accessSemanticsChecker(m_duration.integerVal(), m_patient.integerVal(), false);
    }

    private void accessSemanticsChecker(long duration, long patient_id, boolean adding) {
        ((OdontiorApp) m_app).accessSemanticsChecker((JotyDate) m_date.dateVal(), m_dayPart.integerVal(), m_location.integerVal(),
                patient_id, duration, adding);
    }

    @Override
    protected void saveEffects() {
       WrappedField lastGotNameWField = m_app.m_valuesOnChoice.get("lastName");
        if (lastGotNameWField != null) {
            m_lastName = m_app.m_valuesOnChoice.get("lastName").m_strVal;
            m_firstName = m_app.m_valuesOnChoice.get("firstName").m_strVal;
        }
    }

}
