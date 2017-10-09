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

import org.joty.mobile.gui.DataResultActivity;
import org.joty.mobile.gui.JotyResourceCursorAdapter;


public class PatImageList extends DataResultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patimages_list);
        enableRole("Doctors", Permission.all);
        enableRole("Practitioners", Permission.read);
        setTitle(m_app.m_common.appLang("Images") + " - " + m_resultController.m_extras.getString("patientname"));
    }

    @Override
    protected void doActionOnRecord(long id, JotyResourceCursorAdapter adapter, int position) {
        AccessorCoordinates accessorCoordinates = new AccessorCoordinates();
        accessorCoordinates.jotyDialogClassName = m_resultController.m_accessorCoordinates.jotyDialogClassName;
        accessorCoordinates.jotyTermName = "image";
        openImage(null, "image", "id = " + id, accessorCoordinates);
    }

  }
