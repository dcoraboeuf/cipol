package net.cipol.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.model.PolicySummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PolicyCore extends AbstractDaoService implements PolicyService {

	@Autowired
	public PolicyCore(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	@Cacheable("policy")
	public Policy loadPolicy(String policyId) {
		try {
			return getNamedParameterJdbcTemplate().queryForObject("select * from POLICY where uid = :uid", Collections.singletonMap("uid", policyId), new RowMapper<Policy>() {
				@Override
				public Policy mapRow(ResultSet rs, int i)
						throws SQLException {
					Policy policy = new Policy();
					policy.setUid(rs.getString("uid"));
					policy.setName(rs.getString("name"));
					policy.setDescription(rs.getString("description"));
					// FIXME Policy groups
					// FIXME Policy rulesets
					return policy;
				}
			});
		} catch (EmptyResultDataAccessException ex) {
			throw new PolicyNotFoundException(policyId);
		}
	}
	
	@Override
	public List<PolicySummary> listPolicies() {
		return getJdbcTemplate().query("select * from POLICY", new RowMapper<PolicySummary>(){
			@Override
			public PolicySummary mapRow(ResultSet rs, int i)
					throws SQLException {
				PolicySummary policy = new PolicySummary();
				policy.setUid(rs.getString("uid"));
				policy.setName(rs.getString("name"));
				policy.setDescription(rs.getString("description"));
				return policy;
			}
		});
	}

}
