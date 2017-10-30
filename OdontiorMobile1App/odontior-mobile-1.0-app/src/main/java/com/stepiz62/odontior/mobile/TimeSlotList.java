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
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import org.joty.mobile.data.JotyCursorWrapper;
import org.joty.mobile.gui.DataResultActivity;
import org.joty.mobile.gui.DetailsController;
import org.joty.mobile.gui.JotyResourceCursorAdapter;


public class TimeSlotList extends DataResultActivity {

    boolean m_firstEngagedMessage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_list);
        m_openableDetailIdField = "appointment_id";
        Organizer organizer = (Organizer)  mainActivity();
        String armchairPrefix = m_app.m_common.appLang("Armchair");
        if (isPortrait())
            armchairPrefix = armchairPrefix.substring(0, 1) + ".";
        setTitle(organizer.m_date.dateVal().render() + " - " + armchairPrefix + ": " + organizer.m_armchair.render());
    }

    @Override
    public boolean checkRowForOpeningDetail(JotyResourceCursorAdapter adapter, Bundle m_extras) {
        boolean retVal = false;
        boolean busySlot = ((TimeSlotListFragment.TimeSlotAdapter) adapter).busySlot();
        if (busySlot) {
            if (adapter.isNull(m_openableDetailIdField)) {
                m_app.toast(m_app.m_common.appLang("TimeSlotEngaged"), ! m_firstEngagedMessage);
                m_firstEngagedMessage = false;
                }
            else {
                if (!adapter.isNull("treatment") || adapter.getInt("macro") == 1)
                    m_app.toast(m_app.m_common.appLang("NoControllableAppointment"));
                else {
                    retVal = true;
                    putFieldAsExtra(adapter, m_extras, "lastName");
                    putFieldAsExtra(adapter, m_extras, "firstName");
                }
            }
        } else
            retVal = true;
        return retVal;
    }

    @Override
    public void onNavigatorFragmentItemClick(AdapterView<?> parent, long id, BaseAdapter adapter, View view, int position) {
        selectItem(position * Organizer.SLOTS_AN_HOUR);
    }

    @Override
    protected void openDetailsPrepare(long id, JotyResourceCursorAdapter adapter, int position) {
        super.openDetailsPrepare(id, adapter, position);
        JotyCursorWrapper cursor = (JotyCursorWrapper) adapter.getCursor();
        DetailsController detailsController = mainActivity().m_detailsControllersStack.top();
        detailsController.m_paramContext.setContextParam("daypartid", cursor.getLong("id"));
        detailsController.m_paramContext.setContextParam("date", ((Organizer) mainActivity()).m_date.dateVal());
        detailsController.m_accessorCoordinates = m_app.dataMainActivity().new AccessorCoordinates();
        AccessorCoordinates coordinates = detailsController.m_accessorCoordinates;
        coordinates.jotyDialogClassName = "com.stepiz62.odontior.AppointmentDialog";
        coordinates.jotyPanelIndex = 0;
        coordinates.mode = "contextSpecific";
        coordinates.paramContext =  detailsController.m_paramContext;
    }
}

