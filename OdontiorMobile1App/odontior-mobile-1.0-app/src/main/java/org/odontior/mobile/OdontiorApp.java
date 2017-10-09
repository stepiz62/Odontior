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

import org.joty.app.LiteralsCollection;
import org.joty.common.BasicPostStatement;
import org.joty.data.JotyDate;
import org.joty.mobile.app.JotyApp;
import org.joty.common.BasicPostStatement.Item;

import java.util.HashMap;

public class OdontiorApp extends JotyApp {

    public int organizerMenuId = JotyApp.JotyMenus.values().length;
    public int patientsMenuId = organizerMenuId + 1;
    public int customersMenuId = organizerMenuId + 2;
    public int invoicesMenuId = organizerMenuId + 3;
    public int armchairMenuId = organizerMenuId + 4;
    public int relatedCustomesId = organizerMenuId + 5;
    public int relatedImagesId = organizerMenuId + 6;
    public int relatedPatientsId = organizerMenuId + 7;
    LiteralsCollection m_literalCollectionInstance = new LiteralsCollection(this);
    HashMap<String, Integer> m_organizerPreferences;
    public boolean armchairsModified;

    @Override
    public void onCreate() {
        m_defaultStartPath = "http://stepiz62.com/odontior/mobile";
        //       m_defaultStartPath = "http://192.168.1.101:8080/Odontior2";
        //       m_testing = true;
        //       m_testPath = "http://192.168.1.102:8080/Odontior2/";  // by device
        //       m_testPath = "http://10.0.2.2:8080/Odontior2/";       // by emulator
        super.onCreate();
        setApp("Odontior Mobile", "1.0.3-b", null, null);
    }

    final LiteralsCollection.LiteralStructParams m_modifiableLsParams = m_literalCollectionInstance.new LiteralStructParams() {
        {
            modifiable = true;
        }
    };

    public void reloadArmchairs(ResponseHandlersManager respManager) {
        buildLiteralStruct("D_2", "id", "name", "armchairs", respManager);
    }

    @Override
    public void loadDescriptions(ResponseHandlersManager respManager) {
        respManager.topHandlerIncrementCounter(4);
        super.loadDescriptions(respManager);
        buildLiteralStruct("D_1", "id", "timelabel", "dayparts", respManager);
        reloadArmchairs(respManager);
        buildLiteralStruct("D_3", "id", "symbol", "teeth", respManager);
        buildLiteralStruct("D_7", "id", "descr", "treatmentDescriptions", m_modifiableLsParams, respManager);
        buildLiteralStruct("D_7", "id", "symbol", "treatmentSymbols", m_modifiableLsParams, respManager);
     }

    public boolean organizerCostraintViolationMsgOnUpdate() {
        OdontiorDbManager dbManager = (OdontiorDbManager) m_dbManager;
        if (dbManager.derivedCode() == OdontiorDbManager.Subcases.TimeSlotCoverage.ordinal())
            toast(m_common.appLang("DurationExceeds"));
        else if (dbManager.derivedCode() == OdontiorDbManager.Subcases.PatientLocUniqueness.ordinal())
            toast(m_common.appLang("PatientCannotBeAnyWhere"));
        return dbManager.derivedCode() > 0;
    }

    public void accessSemanticsChecker(JotyDate date, Long dayPartId, Long locationId, Long patientId, Long duration, boolean adding) {
        BasicPostStatement postStatement = accessorMethodPostStatement("accessSemanticsChecker", null, null);
        postStatement.addItem("date", date.render(true, false), Item._date);
        postStatement.addItem("dayPartId", String.valueOf(dayPartId), Item._long);
        postStatement.addItem("locationId", String.valueOf(locationId), Item._long);
        postStatement.addItem("patientId", String.valueOf(patientId), Item._long);
        postStatement.addItem("practitionerId", null, Item._long);
        postStatement.addItem("duration", String.valueOf(duration), Item._long);
        postStatement.addItem("adding",  adding ? "1" : "0", Item._long);
        if (m_common.m_shared)
            postStatement.addItem("sharingKey", m_common.m_sharingKey, Item._text);
        invokeAccessMethod(postStatement, null); // no ResponseHandlersManager object passed: calling method is used only within a Joty transaction
    }

    @Override
    protected void getPreferences() {
        super.getPreferences();
        m_organizerPreferences = (HashMap<String, Integer>) m_common.accessLocalData("OrganizerPreferences", null);
        if (m_organizerPreferences == null) {
            m_organizerPreferences = new HashMap<String, Integer>();
            m_organizerPreferences.put("hour", -1);
            m_organizerPreferences.put("locationId", 0);
        }
    }

    @Override
    public void endApp() {
        m_common.accessLocalData("OrganizerPreferences", m_organizerPreferences);
        super.endApp();
    }

    @Override
    protected void registerReports() {
         enableRoleToReport("Invoice", "Administrative personnel");
    }
}
