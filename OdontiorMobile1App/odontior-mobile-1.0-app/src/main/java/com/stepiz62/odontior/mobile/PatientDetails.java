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
import android.view.Menu;

import org.joty.common.JotyTypes;
import org.joty.mobile.gui.DataDetailsActivity;
import org.joty.mobile.gui.DataMainActivity;
import org.joty.mobile.gui.DetailsController;
import org.joty.mobile.gui.RadioTerm;
import org.joty.mobile.gui.ResultController;
import org.joty.mobile.gui.Term;
import org.joty.mobile.gui.TextTerm;

public class PatientDetails extends DataDetailsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        new TextTerm(this, JotyTypes._text, R.id.lastname, "lastname");
        new TextTerm(this, JotyTypes._text, R.id.firstname, "firstName");
        setLangHint(R.id.lastname, "lastname");
        setLangHint(R.id.firstname, "firstName");
        RadioTerm masterTerm = new RadioTerm(this, R.id.radioMale, "sex");
        new RadioTerm(this, R.id.radioFemale, masterTerm, null);

        setLangText(R.id.radioMale, "Male");
        setLangText(R.id.radioFemale, "Female");
        new TextTerm(this, JotyTypes._date, R.id.dob, "dateOfBirth");
        setLangHint(R.id.dob, "DOB");
        for (Term term : m_terms)
            term.m_required = true;
        setLangTitle("Patient");
        m_paramContext =  createParamContext();
    }


    @Override
    protected void addIntermediateOptions(Menu menu) {
        if (! m_editing) {
            addMenuItemToAppMenu(menu, ((OdontiorApp) m_app).relatedCustomesId, m_app.m_common.appLang("RelatedCustomers"), R.drawable.customers, false,
                    new Action() {
                        public void doIt() {
                            relatedCustomers();
                        }
                    });
            addMenuItemToAppMenu(menu, ((OdontiorApp) m_app).relatedImagesId, m_app.m_common.appLang("Images"), R.drawable.images, false,
                    new Action() {
                        public void doIt() {
                            relatedImages();
                        }
                    });
        }
    }

    private void relatedCustomers() {
        ResultController resultController = new ResultController(PatCustList.class);
        resultController.m_detailsActivityClass = PatCustDetails.class;
        resultController.m_detailsController = new DetailsController(resultController.m_detailsActivityClass);
        resultController.instantiateOwnData();
        resultController.m_accessorCoordinates.jotyDialogClassName = "com.stepiz62.odontior.PatientDialog";
        resultController.m_accessorCoordinates.jotyPanelIndex = 1;
        resultController.m_detailsController.m_accessorCoordinates = resultController.m_accessorCoordinates;

        resultController.m_mainFilter = "patient_id = " + m_detailsController.m_id;
         resultController.m_detailsController.addIntegerKeyElem("patcust_id");
        resultController.m_extras.putString("patientname", patientName());
        resultController.m_extras.putString("patientId",  m_detailsController.m_id);
        resultController.m_detailsController.m_extras = resultController.m_extras;
        m_mainActivity.m_resultControllersStack.push(resultController);
        resultController.m_type = DataMainActivity.Type.searcher;
        resultController.m_orderByClause = "lastName";
        resultController.m_newAction = true;
        resultController.accessResult(this, false, false);
    }

    private void relatedImages() {
        ResultController resultController = new ResultController(PatImageList.class);
        resultController.instantiateOwnData();
        resultController.m_accessorCoordinates.jotyDialogClassName = "com.stepiz62.odontior.ImageDialog";
        resultController.m_accessorCoordinates.jotyPanelIndex = 0;

        resultController.m_paramContext.setContextParam("patientID", m_detailsController.m_id);
        resultController.m_paramContext.setContextParam("clinicalCardID", 0);
        resultController.m_paramContext.setContextParam("UniqueCcard", 1);
        resultController.m_paramContext.setContextParam("toothID", "%");

        resultController.m_extras.putString("patientname", patientName());
        resultController.m_extras.putString("patientId",  m_detailsController.m_id);
        m_mainActivity.m_resultControllersStack.push(resultController);
        resultController.m_type = DataMainActivity.Type.searcher;
        resultController.m_orderByClause = "tooth_id, timestamp desc";
        resultController.m_smallBlobs.add("imagepreview");
        resultController.accessResult(this, false, false);
    }


    private String patientName() {
        return term(R.id.lastname).render() + " " + term(R.id.firstname).render();
    }

    @Override
    protected void setContextParams() {
        m_paramContext.setContextParam("patientID", m_detailsController.m_id);
    }

 }
