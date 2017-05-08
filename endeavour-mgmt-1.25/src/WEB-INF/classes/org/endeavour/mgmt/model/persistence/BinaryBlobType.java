/***************************************************************************************
 *Endeavour Agile ALM
 *Copyright (C) 2009  Ezequiel Cuellar
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ***************************************************************************************/

package org.endeavour.mgmt.model.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

@SuppressWarnings("unchecked")
public class BinaryBlobType implements UserType {

	public int[] sqlTypes() {
		return new int[] { Types.BLOB };
	}

	public Class returnedClass() {
		return byte[].class;
	}

	public boolean equals(Object aX, Object aY) {
		return (aX == aY) || (aX != null && aY != null && java.util.Arrays.equals((byte[]) aX, (byte[]) aY));
	}

	public Object nullSafeGet(ResultSet aRs, String[] aNames, Object aOwner) throws HibernateException, SQLException {
		return aRs.getBytes(aNames[0]);
	}

	public void nullSafeSet(PreparedStatement aSt, Object aValue, int aIndex) throws HibernateException, SQLException {
		aSt.setBytes(aIndex, (byte[]) aValue);
	}

	public Object deepCopy(Object aValue) {
		Object theResult = null;
		if (aValue != null) {
			byte[] theBytes = (byte[]) aValue;
			theResult = new byte[theBytes.length];
			System.arraycopy(theBytes, 0, theResult, 0, theBytes.length);
		}
		return theResult;
	}

	public boolean isMutable() {
		return true;
	}

	public int hashCode(Object aObject) {
		return 0;
	}

	public Object assemble(Serializable aSerializable, Object aObject) {
		return null;
	}

	public Object replace(Object aArg1, Object aArg2, Object aArg3) {
		return null;
	}

	public Serializable disassemble(Object aArg1) {
		return null;
	}
}
