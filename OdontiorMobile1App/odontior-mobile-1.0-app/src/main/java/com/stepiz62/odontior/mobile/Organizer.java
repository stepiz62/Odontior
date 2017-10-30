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

import android.os.Bundle;

import org.joty.common.JotyTypes;
import org.joty.mobile.gui.DataMainActivity;
import org.joty.mobile.gui.SpinnerTerm;
import org.joty.mobile.gui.TextTerm;


public class Organizer extends DataMainActivity {
    TextTerm m_date;
    SpinnerTerm m_armchair;

    static public final int HOURS_A_DAY = 24;
    static public final int SLOT_MINUTES = 5;
    static public final int SLOT_SET_SIZE = 288;
    static public final int SLOTS_AN_HOUR = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        enableRole("Schedulers", Permission.all);
        enableRole("Practitioners", Permission.read);
        enableRole("Administrative personnel", Permission.read);
        m_date = new TextTerm(this, JotyTypes._date, R.id.date, null);
        m_date.setToNow();
        m_date.m_nullable = false;
        m_date.setAuxView(R.id.dow);
        m_armchair = new SpinnerTerm(this, R.id.armchair, null, "armchairs");
        if (m_armchair.m_spinner.getCount() == 0 ) {
            finish();
            m_app.jotyWarning(m_common.appLang("NoArmchairDefined"));
        } else {
            setCollaboratorActivities(TimeSlotList.class, Appointment.class);
            m_detailsController.addIntegerKeyElem("id");
            m_paramContext = createParamContext();
            m_accessorMode = true;
            m_accessorCoordinates.jotyDialogClassName = "com.stepiz62.odontior.OrganizerDialog";
            m_accessorCoordinates.jotyPanelIndex = 0;
            m_accessorCoordinates.jotyTermName = "appointments";
            m_accessorCoordinates.paramContext = m_paramContext;
            setType(Type.contextor);
            setTitle(m_app.m_common.appLang("Organizer"));
        }
    }

    @Override
    protected void setContextParams() {
        m_paramContext.setContextParam("day", m_date.dateVal());
        m_paramContext.setContextParam("LocationID", m_armchair.integerVal());
    }

}
