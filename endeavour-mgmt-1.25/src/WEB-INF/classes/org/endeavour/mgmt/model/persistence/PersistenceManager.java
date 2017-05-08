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
import java.sql.Connection;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import thinwire.ui.Application;

@SuppressWarnings("unchecked")
public class PersistenceManager {

	private Session session = null;
	private Transaction transaction = null;
	private static Application.Local<PersistenceManager> instance = new Application.Local<PersistenceManager>();

	public PersistenceManager() {
		if (Application.current() != null) {
			instance.set(this);
		}
	}

	public static PersistenceManager getInstance() {
		if (instance.get() == null) {
			new PersistenceManager();
		}
		return instance.get();
	}

	public void beginTransaction() {
		this.session = SessionFactoryManager.getInstance().openSession();
		this.session.setFlushMode(FlushMode.MANUAL);
		this.transaction = session.beginTransaction();
	}

	public void endTransaction() {
		this.session.flush();
		this.transaction.commit();
		this.session.close();
		this.transaction = null;
		this.session = null;
	}

	public void closeConnection() {
		SessionFactoryManager.getInstance().closeConnection();
	}

	public void save(Object aObject) {
		this.session.save(aObject);
		this.session.flush();
	}

	public boolean contains(Object aObject) {
		return this.session.contains(aObject);
	}

	public void delete(Object aObject) {
		this.session.delete(aObject);
		this.session.flush();
	}

	public <E> Object findById(Class<E> aClass, Serializable anId) {
		return this.session.load(aClass, anId);
	}

	public Object findBy(String aQuery) {
		return this.session.createQuery(aQuery).uniqueResult();
	}

	public <E> List<E> findAllBy(String aQuery) {
		return this.session.createQuery(aQuery).list();
	}

	public <E> List<E> findAllBy(String aQuery, List<Object> aParameters) {
		Query theQuery = this.session.createQuery(aQuery);
		int theIndex = 0;
		for (Object theParameter : aParameters) {
			theQuery.setParameter(theIndex, theParameter);
			theIndex++;
		}
		return theQuery.list();
	}

	public Object findBy(String aQuery, List<Object> aParameters) {
		Query theQuery = this.session.createQuery(aQuery);
		int theIndex = 0;
		for (Object theParameter : aParameters) {
			theQuery.setParameter(theIndex, theParameter);
			theIndex++;
		}
		return theQuery.uniqueResult();
	}

	public Connection openConnection() {
		return SessionFactoryManager.getInstance().openConnection();
	}

	public void attachToSession(Object anObject) {
		try {
			this.session.update(anObject);
		} catch (Exception e) {
			this.session.evict(anObject);
		}
	}

	public void refresh(Object anObject) {
		this.session.refresh(anObject);
	}
}
