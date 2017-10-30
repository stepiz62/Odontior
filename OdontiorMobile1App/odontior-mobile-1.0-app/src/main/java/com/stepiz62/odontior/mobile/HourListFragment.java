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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joty.mobile.app.JotyApp;
import org.joty.mobile.gui.JotyListFragment;
import org.joty.mobile.gui.NavigatorListFragment;

import java.util.HashMap;

public class HourListFragment extends NavigatorListFragment {
    String m_hoursStates[] = new String [Organizer.HOURS_A_DAY];
    HashMap<String, Integer> m_preferences = ((OdontiorApp) JotyApp.m_app).m_organizerPreferences;
    int m_preferredHour = m_preferences.get("hour");

    @Override
    protected BaseAdapter createAdapter() {

        return new BaseAdapter() {
            LayoutInflater mInflater;
            Context m_context = JotyApp.m_app.m_activity.getBaseContext();

            {
                mInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return Organizer.HOURS_A_DAY;
            }

            @Override
            public boolean hasStableIds() {
                return super.hasStableIds();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.row_hour, parent, false);
                } else {
                    view = convertView;
                }
                bindView(view, position);
                return view;
            }

            public void bindView(View view, int position) {
                TextView hour = (TextView) view.findViewById(R.id.hour);
                hour.setText(String.valueOf(position));

                TextView coverage = (TextView) view.findViewById(R.id.coverage);
                coverage.setText(m_hoursStates[position]);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_preferences.clear();
        m_preferences.put("hour", m_preferredHour);

    }

    @Override
    public void update(JotyListFragment listFragment) {
        TimeSlotListFragment.BusyState busyState = ((TimeSlotListFragment)listFragment).m_busyState;

       boolean someBusy, beginBusy, endBusy, someFree;
        int posIdx;
       int         i = 1;
        while (i <= Organizer.HOURS_A_DAY) {
             someBusy = false;
            someFree = false;
            posIdx = (i - 1) * Organizer.SLOTS_AN_HOUR;
            beginBusy = busyState.truthVector[posIdx];
            endBusy =  busyState.truthVector[posIdx + Organizer.SLOTS_AN_HOUR - 1];
            for (int j = 1; j <= Organizer.SLOTS_AN_HOUR; j++) {
                if (busyState.truthVector[posIdx + j - 1])
                    someBusy = true;
                else
                    someFree = true;
            }
            m_hoursStates[i-1] = (someFree ? (someBusy ? (beginBusy ? (endBusy ? "<" : "/") : (endBusy ? "\\" : ">")) : " ") : "|");
            i++;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (m_preferredHour >= 0) {
            getListView(view).setSelection(m_preferredHour);
            m_resultActivity.m_initialSelection = m_preferredHour * Organizer.SLOTS_AN_HOUR;
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        m_preferredHour = position;
        return true;
    }

}
