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

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import org.joty.mobile.data.JotyCursorWrapper;
import org.joty.mobile.gui.JotyResourceCursorAdapter;
import org.joty.mobile.gui.JotyResultFragment;

public class TimeSlotListFragment extends JotyResultFragment {

    class BusyState {
        boolean truthVector[] = new boolean[Organizer.SLOT_SET_SIZE];

        BusyState() {
            clear();
        }

        void clear() {
            for (int i = 0; i < Organizer.SLOT_SET_SIZE; i++)
                truthVector[i] = false;
        }

        public void clear(int initDayPArtId, long handlesQty) {
            set(false, initDayPArtId, handlesQty);
        }

        int getMaxEndingPartID(int theDayPartID) {
            int i = theDayPartID;
            while (i <= Organizer.SLOT_SET_SIZE && !truthVector[i - 1])
                i++;
            return i - 1;
        }

        void set(boolean on, long initDayPartId, long slotsQty) {
            int index;
            for (int j = 0; j < slotsQty; j++) {
                if (initDayPartId + j > Organizer.SLOT_SET_SIZE)
                    break;
                index = (int) (initDayPartId - 1 + j);
                truthVector[index] = on;
             }
        }
    }
    public BusyState m_busyState;

    public class TimeSlotAdapter extends JotyResourceCursorAdapter {
        int m_colIndex;
        long m_duration;

        public TimeSlotAdapter(Context context, int layout, JotyCursorWrapper cursorWrapper, int flags) {
            super(context, layout, cursorWrapper, flags);
            boolean datumExists;
            int occupiedSlots;
            m_busyState = new BusyState();

            m_busyState.clear();
            for (int i = 0; i < cursorWrapper.getCount(); i++) {
                if (i==0)
                    cursorWrapper.moveToPosition(0);
                datumExists = !cursorWrapper.isNull(cursorWrapper.getColumnIndex("firstname"));
                if (datumExists) {
                    m_duration = cursorWrapper.getLong(cursorWrapper.getColumnIndex("duration"));
                    occupiedSlots = (int) (m_duration / Organizer.SLOT_MINUTES + 1) + (m_duration % Organizer.SLOT_MINUTES > 0 ? 1 : 0);
                    m_busyState.set(true, i + 1, occupiedSlots);
                    cursorWrapper.move(occupiedSlots);
                    i +=  occupiedSlots - 1;
                } else
                    cursorWrapper.moveToNext();
            }
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView hour = (TextView) view.findViewById(R.id.time);
            hour.setText(cursor.getString(cursor.getColumnIndex("timelabel")));

            TextView coverage = (TextView) view.findViewById(R.id.busyflag);
            coverage.setText(m_busyState.truthVector[cursor.getPosition()] ? "|||" : "");

            TextView firstname = (TextView) view.findViewById(R.id.firstname);
            firstname.setText(cursor.getString(cursor.getColumnIndex("firstname")));

            TextView lastname = (TextView) view.findViewById(R.id.lastname);
            lastname.setText(cursor.getString(cursor.getColumnIndex("lastname")));

            TextView duration = (TextView) view.findViewById(R.id.duration);
            m_colIndex = cursor.getColumnIndex("duration");
            duration.setText(cursor.isNull(m_colIndex) ? "" : String.valueOf(cursor.getLong(m_colIndex)));
        }

        public boolean busySlot() {
            return m_busyState.truthVector[m_cursor.getPosition()];
        }
    }

    @Override
    protected String getKeyFieldName() {
        return "id";
    }

    @Override
    protected JotyResourceCursorAdapter createAdapter(final JotyCursorWrapper cursorWrapper) {
        return new TimeSlotAdapter(getActivity(), R.layout.row_time_slot, cursorWrapper, 0);
    }

}
