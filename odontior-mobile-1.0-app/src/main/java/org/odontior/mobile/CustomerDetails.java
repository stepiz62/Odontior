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
import android.view.Menu;

import org.joty.common.JotyTypes;
import org.joty.mobile.gui.DataDetailsActivity;
import org.joty.mobile.gui.DataMainActivity;
import org.joty.mobile.gui.ResultController;
import org.joty.mobile.gui.Term;
import org.joty.mobile.gui.TextTerm;

public class CustomerDetails extends DataDetailsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        new TextTerm(this, JotyTypes._text, R.id.lastname, "lastname");
        new TextTerm(this, JotyTypes._text, R.id.firstname, "firstName");
        if (m_extras != null) {
            String patientName = m_extras.getString("patientName");
            if (patientName != null) {
                int blankIdx = patientName.indexOf(" ");
                term(R.id.lastname).defaultValue().setValue(patientName.substring(0, blankIdx));
                term(R.id.firstname).defaultValue().setValue(patientName.substring(blankIdx + 1));
            }
        }
        new TextTerm(this, JotyTypes._text, R.id.address, "address");
        new TextTerm(this, JotyTypes._text, R.id.zipcode, "zipcode");
        new TextTerm(this, JotyTypes._text, R.id.city, "city");
        new TextTerm(this, JotyTypes._text, R.id.taxcode, "taxcode");
        new TextTerm(this, JotyTypes._text, R.id.mobilephone, "mobilephone");
        new TextTerm(this, JotyTypes._text, R.id.email, "email");
        setLangHint(R.id.lastname, "lastname");
        setLangHint(R.id.firstname, "firstName");
        setLangHint(R.id.address, "address");
        setLangHint(R.id.zipcode, "zipcode");
        setLangHint(R.id.city, "city");
        setLangHint(R.id.taxcode, "taxcode");
        setLangHint(R.id.mobilephone, "mobilephone");
        setLangHint(R.id.email, "email");
        for (Term term : m_terms)
            term.m_required = true;
        term(R.id.email).m_required = false;
        setLangTitle("Customer");
    }

    private void relatedPatients() {
        ResultController resultController = new ResultController(CustPatList.class);
        resultController.instantiateOwnData();
        resultController.m_accessorCoordinates.jotyDialogClassName = "org.odontior.CustomerDialog";
        resultController.m_accessorCoordinates.jotyPanelIndex = 0;
        resultController.m_accessorCoordinates.jotyTermName = "Patients";
        resultController.m_extras.putString("customername", term(R.id.lastname).render() + " " + term(R.id.firstname).render());
        resultController.m_accessorCoordinates.paramContext.setContextParam("customerId",  m_detailsController.m_id);
        m_mainActivity.m_resultControllersStack.push(resultController);
        resultController.m_type = DataMainActivity.Type.searcher;
        resultController.m_orderByClause = "lastName";
        resultController.accessResult(this, false, false);
    }

    @Override
    protected void addIntermediateOptions(Menu menu) {
        if (! m_editing) {
            addMenuItemToAppMenu(menu, ((OdontiorApp) m_app).relatedPatientsId, m_app.m_common.appLang("RelatedPatients"), R.drawable.patients, false,
                    new Action() {
                        public void doIt() {
                            relatedPatients();
                        }
                    });
        }
    }

}