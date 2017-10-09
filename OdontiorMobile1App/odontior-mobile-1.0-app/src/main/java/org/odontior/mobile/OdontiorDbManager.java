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

import org.joty.common.AbstractDbManager;
 
public class OdontiorDbManager extends AbstractDbManager {

    public enum Subcases {InterventionUniqueness, TimeSlotCoverage, PatientLocUniqueness, PractitionerLocUniqueness};

	public boolean dbExceptionCheck(String textToInspect, String errorCode, ExcCheckType exceptionCheckType) {
		boolean retVal = false;
		switch (exceptionCheckType) {
			case INVALID_CREDENTIALS:
				if (textToInspect.indexOf("Access denied for user") >= 0)
					retVal = true;
				break;
			case CONSTR_VIOLATION_ON_UPDATE:
                if (textToInspect.indexOf("Duplicate entry") >= 0) {
                    m_derivedCode = 0;
                    if (textToInspect.indexOf("TimeSlotCoverage") >= 0)
                        m_derivedCode = Subcases.TimeSlotCoverage.ordinal();
                    else if (textToInspect.indexOf("PatientLocUniqueness") >= 0)
                        m_derivedCode = Subcases.PatientLocUniqueness.ordinal();
                    retVal = true;
                }
				break;
			case CONSTR_VIOLATION_ON_DELETE:
				if (textToInspect.indexOf("Cannot delete or update a parent row") >= 0)
					retVal = true;
				break;
		}
		return retVal;
	}
}
