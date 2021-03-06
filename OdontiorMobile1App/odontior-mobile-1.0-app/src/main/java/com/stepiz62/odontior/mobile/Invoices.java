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
import org.joty.mobile.gui.TextTerm;

public class Invoices extends DataMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        enableRole("Administrative personnel", Permission.all);

        new TextTerm(this, JotyTypes._int, R.id.number, "number");
        setLangHint(R.id.number, "number");
        new TextTerm(this, JotyTypes._date, R.id.date, "date");
        setLangHint(R.id.date, "date");
        term(R.id.date).setOperatorsRadioGroup(R.id.dateoperators);
        new TextTerm(this, JotyTypes._text, R.id.lastname, "c.lastname");
        setLangHint(R.id.lastname, "lastname");

        setCollaboratorActivities(InvoiceList.class, null);
        m_accessorMode = true;
        m_accessorCoordinates.jotyDialogClassName = "com.stepiz62.odontior.InvoiceDialog";
        m_accessorCoordinates.jotyPanelIndex = 0;
        m_accessorCoordinates.paramContext = m_app.m_paramContext;
        setOrderByExpr("date");
        setType(Type.searcher);
        setTitle(m_app.m_common.appLang("Invoices"));
    }

    @Override
    protected boolean newIsAllowed() {
        return false;
    }

}
