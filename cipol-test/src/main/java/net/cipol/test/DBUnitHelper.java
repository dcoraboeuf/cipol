package net.cipol.test;

import org.dbunit.database.IDatabaseConnection;

public class DBUnitHelper {

	private static final ThreadLocal<IDatabaseConnection> connection = new ThreadLocal<IDatabaseConnection>();

	static void setConnection(IDatabaseConnection c) {
		connection.set(c);
	}

	public static IDatabaseConnection getConnection() {
		return connection.get();
	}

}