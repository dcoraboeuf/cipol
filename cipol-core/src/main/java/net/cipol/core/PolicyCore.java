package net.cipol.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import net.cipol.api.PolicyService;
import net.cipol.model.Group;
import net.cipol.model.ParamValue;
import net.cipol.model.Policy;
import net.cipol.model.PolicySummary;
import net.cipol.model.RuleDefinition;
import net.cipol.model.RuleSet;
import net.cipol.security.CipolRole;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyCore extends AbstractDaoService implements PolicyService {

	@Autowired
	public PolicyCore(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	@Transactional
	@Secured(CipolRole.ADMIN)
	public String createPolicy(String name) {
		// Creates an UUID
		String uid = UUID.randomUUID().toString();
		// Inserts the record
		getNamedParameterJdbcTemplate().update(SQL.POLICY_CREATE,
				new MapSqlParameterSource()
					.addValue("uid", uid)
					.addValue("name", name));
		// OK
		return uid;
	}
	
	@Override
	@Transactional
	@Secured(CipolRole.ADMIN)
	public void deletePolicy(String uid) {
		getNamedParameterJdbcTemplate().update(SQL.POLICY_DELETE, Collections.singletonMap("uid", uid));
		// FIXME Delete dependencies (using triggers)
	}

	@Override
	@Cacheable("policy")
	@Transactional(readOnly = true)
	@Secured(CipolRole.ANONYMOUS)
	public Policy loadPolicy(final String policyId) {
		try {
			// TODO Uses external mappers that use a named-parameter JDBC template
			final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
			// Gets the policy
			Policy policy = t.queryForObject(SQL.POLICY_FIND_BY_UID, Collections.singletonMap("uid", policyId), new RowMapper<Policy>() {
				@Override
				public Policy mapRow(ResultSet rs, int i)
						throws SQLException {
					Policy policy = new Policy();
					policy.setUid(policyId);
					policy.setName(rs.getString("name"));
					policy.setDescription(rs.getString("description"));
					// Policy groups
					List<Group> groups = t.query(SQL.GROUP_FIND_BY_CATEGORY_AND_REFERENCE,
								new MapSqlParameterSource()
									.addValue("category", "POLICY")
									.addValue("reference", policyId),
								new RowMapper<Group>() {
									@Override
									public Group mapRow(ResultSet rs, int arg1)
											throws SQLException {
										Group o = new Group();
										o.setName(rs.getString("name"));
										List<String> members = Arrays.asList(StringUtils.split(rs.getString("members"), ","));
										o.setMembers(members);
										return o;
									}
								}
							);
					policy.setGroups(groups);
					// Policy rulesets
					List<RuleSet> rulesets = t.query(SQL.RULESET_FIND_BY_POLICY,
							Collections.singletonMap("uid", policyId),
							new RowMapper<RuleSet>() {
								@Override
								public RuleSet mapRow(ResultSet rs, int i)
										throws SQLException {
									RuleSet o = new RuleSet();
									o.setPath(rs.getString("path"));
									o.setDescription(rs.getString("description"));
									o.setDisabled(rs.getBoolean("disabled"));
									// Rules
									List<RuleDefinition> ruleDefinitions = t.query (
											SQL.RULEDEF_FIND_BY_RULESET,
											Collections.singletonMap("rulesetid", rs.getInt("id")),
											new RowMapper<RuleDefinition>() {
												@Override
												public RuleDefinition mapRow(
														ResultSet rs,
														int i)
														throws SQLException {
													RuleDefinition o = new RuleDefinition();
													o.setRuleId(rs.getString("ruleId"));
													o.setDescription(rs.getString("description"));
													o.setDisabled(rs.getBoolean("disabled"));
													// Rule parameters
													// TODO Uses a common mapper
													List<ParamValue> params = t.query (
															SQL.PARAM_FIND_BY_CATEGORY_AND_REFERENCE,
															new MapSqlParameterSource()
																.addValue("category", "RULEDEF")
																.addValue("reference", rs.getInt("id")),
															new RowMapper<ParamValue>() {
																@Override
																public ParamValue mapRow(
																		ResultSet rs,
																		int i)
																		throws SQLException {
																	ParamValue o = new ParamValue();
																	o.setName(rs.getString("name"));
																	o.setValue(rs.getString("value"));
																	return o;
																}
															});
													o.setParameters(params);
													// OK
													return o;
												}
											});
									o.setRules(ruleDefinitions);
									// OK
									return o;
								}
							});
					policy.setRules(rulesets);
					// OK
					return policy;
				}
			});
			// OK
			return policy;
		} catch (EmptyResultDataAccessException ex) {
			throw new PolicyNotFoundException(policyId);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	@Secured(CipolRole.USER)
	public List<PolicySummary> listPolicies() {
		return getJdbcTemplate().query(SQL.POLICY_FIND_ALL, new RowMapper<PolicySummary>(){
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
