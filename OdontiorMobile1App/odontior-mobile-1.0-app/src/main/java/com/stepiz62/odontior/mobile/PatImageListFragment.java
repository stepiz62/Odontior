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
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joty.mobile.app.JotyApp;
import org.joty.mobile.data.JotyCursorWrapper;
import org.joty.mobile.gui.JotyResourceCursorAdapter;
import org.joty.mobile.gui.JotyResultFragment;

public class PatImageListFragment extends JotyResultFragment {

    @Override
    protected String getKeyFieldName() {
        return "id";
    }

    @Override
    protected JotyResourceCursorAdapter createAdapter(JotyCursorWrapper cursorWrapper) {
        return  new JotyResourceCursorAdapter(getActivity(), R.layout.row_image, cursorWrapper, 0){
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                JotyCursorWrapper cursorWrapper = (JotyCursorWrapper) cursor;

                TextView timestamp = (TextView) view.findViewById(R.id.timestamp);
                timestamp.setText(cursorWrapper.getDate("timestamp").render(false, true));

                TextView tooth = (TextView) view.findViewById(R.id.tooth);
                tooth.setText(JotyApp.m_app.m_common.m_literalStructMap.get("teeth").literal(cursorWrapper.getLong("tooth_id")));

                ImageView preview = (ImageView) view.findViewById(R.id.preview);
                byte[] bytes = cursorWrapper.getBlob("imagepreview");
                preview.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        };
    }

 }
