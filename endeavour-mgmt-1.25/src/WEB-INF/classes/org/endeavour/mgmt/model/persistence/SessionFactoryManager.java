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

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.connection.ConnectionProvider;

public class SessionFactoryManager {

	private SessionFactory sessionFactory = null;
	private Configuration configuration = null;
	private ConnectionProvider connectionProvider = null;
	private static SessionFactoryManager keeper = null;

	public SessionFactoryManager() {
		this.configuration = new Configuration();
		this.sessionFactory = this.configuration.configure().buildSessionFactory();
	}

	public static SessionFactoryManager getInstance() {
		if (keeper == null) {
			keeper = new SessionFactoryManager();
		}
		return keeper;
	}

	public Session openSession() {
		return this.sessionFactory.openSession();
	}

	public void shutDown() {
		this.sessionFactory.close();
	}

	public Connection openConnection() {
		Connection theConnection = null;
		try {
			this.connectionProvider = this.configuration.buildSettings().getConnectionProvider();
			theConnection = this.connectionProvider.getConnection();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return theConnection;
	}

	public void closeConnection() {
		if (this.connectionProvider != null) {
			this.connectionProvider.close();
		}
	}
}
