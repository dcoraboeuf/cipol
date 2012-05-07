package net.cipol.core.db.triggers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.api.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicyDeleteTrigger implements Trigger {
	
	private static final Logger log = LoggerFactory
			.getLogger(PolicyDeleteTrigger.class);

	@Override
	public void init(Connection conn, String schemaName, String triggerName,
			String tableName, boolean before, int type) throws SQLException {
	}

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {
		String uid = (String) oldRow[0];
		log.debug("Deleting policy: {}", uid);
		// Parameters
		PreparedStatement psParams = conn.prepareStatement("delete from param where id in (" +
					"select p.id from param p, ruleset rs, ruledef rd where rs.policy = ? and rs.id = rd.ruleset and p.category = 'RULEDEF' and p.reference = rd.id" +
				")");
		try {
			psParams.setString(1, uid);
			int rows = psParams.executeUpdate();
			log.debug("{} parameters have been deleted for policy: {}", rows, uid);
		} finally {
			psParams.close();
		}
		// Groups
		PreparedStatement psGroups = conn.prepareStatement("delete from groups where category = 'POLICY' and reference = ?");
		try {
			psGroups.setString(1, uid);
			int rows = psGroups.executeUpdate();
			log.debug("{} groups have been deleted for policy: {}", rows, uid);
		} finally {
			psGroups.close();
		}
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public void remove() throws SQLException {
	}

}
