/*
	Copyright (c) 2015, Stefano Pizzocaro. All rights reserved. Use is subject to license terms.
	
	This file is part of Odontior 2.0.
	
	Odontior 2.0 is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Odontior 2.0 is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public License
	along with Odontior 2.0.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.odontior;

public class CustomerEstimatesDialog extends CustomerInvoicesDialog {
	public CustomerEstimatesDialog(Object callContext) {
		super(callContext, null);
	}

	@Override
	public boolean instantiatedForEstimating() {
		return true;
	}

	@Override
	public boolean isOpenNormal() {
		return true;
	}

	@Override
	protected void setEntityName() {
		m_entityName = appLang("CustomersEstimates");
	}
}
