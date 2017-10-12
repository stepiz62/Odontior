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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import org.joty.mobile.gui.IdleActivity;
import org.joty.mobile.app.JotyApp;

public class MainActivity extends IdleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(m_app.m_name);
        setContentView(R.layout.activity_main);
        try {
            ((TextView) findViewById(R.id.versionName)).
                    setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }}

    @Override
    protected void addMenuItemsToAppMenu(Menu menu) {
        OdontiorApp app = (OdontiorApp) m_app;
        addMenuItemToAppMenu(menu, app.organizerMenuId, m_app.m_common.appLang("organizer"), R.drawable.organizer, false,
                new Action() {
                    public void doIt() {
                        startActivity(Organizer.class);
                    }
                });
        addMenuItemToAppMenu(menu, app.patientsMenuId, m_app.m_common.appLang("patients"), R.drawable.patients, false,
                new Action() {
                    public void doIt() {
                        startActivity(Patients.class);
                    }
                });
        addMenuItemToAppMenu(menu, app.customersMenuId, m_app.m_common.appLang("customers"), R.drawable.customers, false,
                new Action() {
                    public void doIt() {
                        startActivity(Customers.class);
                    }
                });
        addMenuItemToAppMenu(menu, app.invoicesMenuId, m_app.m_common.appLang("invoices"), R.drawable.invoices, false,
                new Action() {
                    public void doIt() {
                       startActivity(Invoices.class);
                    }
                });
        addMenuItemToAppMenu(menu, app.armchairMenuId, m_app.m_common.appLang("armchairs"), R.drawable.armchair, false,
                new Action() {
                    public void doIt() {
                        startActivity(Armchairs.class);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OdontiorApp app = (OdontiorApp) m_app;
        if (app.armchairsModified) {
            app.armchairsModified = false;
            JotyApp.ResponseHandlersManager respManager = m_app.new ResponseHandlersManager();
            respManager.push(m_app.new ResponseHandler() {
                @Override
                public void handle(boolean result, JotyApp.ResponseHandlersManager respManager) {
                    m_app.endWaitCursor();
                }
            });
            m_common.setApplicationScopeAccessorMode();
            m_app.beginWaitCursor();
            app.reloadArmchairs(respManager);
            m_common.setApplicationScopeAccessorMode(false);
        }
    }
}
