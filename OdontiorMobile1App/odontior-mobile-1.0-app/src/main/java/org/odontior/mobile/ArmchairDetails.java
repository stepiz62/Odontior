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

import org.joty.common.JotyTypes;
import org.joty.mobile.gui.DataDetailsActivity;
import org.joty.mobile.gui.Term;
import org.joty.mobile.gui.TextTerm;

public class ArmchairDetails extends DataDetailsActivity{
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_armchair_details);
       new TextTerm(this, JotyTypes._text, R.id.name, "name");
       new TextTerm(this, JotyTypes._text, R.id.description, "description");
       setLangHint(R.id.name, "name");
       setLangHint(R.id.description, "description");
       for (Term term : m_terms)
           term.m_required = true;
       setLangTitle("Armchair");
   }

    @Override
    protected void saveEffects() {
        super.saveEffects();
        ((OdontiorApp) m_app).armchairsModified = true;
    }
}
