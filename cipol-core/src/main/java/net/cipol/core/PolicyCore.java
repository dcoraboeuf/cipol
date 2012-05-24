package net.cipol.core;

import static net.cipol.core.SQLColumns.CATEGORY;
import static net.cipol.core.SQLColumns.DESCRIPTION;
import static net.cipol.core.SQLColumns.DISABLED;
import static net.cipol.core.SQLColumns.ID;
import static net.cipol.core.SQLColumns.MEMBERS;
import static net.cipol.core.SQLColumns.NAME;
import static net.cipol.core.SQLColumns.PATH;
import static net.cipol.core.SQLColumns.REFERENCE;
import static net.cipol.core.SQLColumns.RULESETID;
import static net.cipol.core.SQLColumns.RULE_ID;
import static net.cipol.core.SQLColumns.UID;
import static net.cipol.core.SQLColumns.VALUE;
import static net.cipol.core.SQLValues.CATEGORY_POLICY;

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
import net.cipol.model.support.PolicyField;
import net.cipol.security.CipolRole;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyCore extends AbstractDaoService implements PolicyService {
	
	private final Logger log = LoggerFactory.getLogger(PolicyService.class);

	@Autowired
	public PolicyCore(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	@Transactional
	@Secured(CipolRole.ADMIN)
	public String importPolicy(Policy policy, String proposedName) {
		// Actual name
		String name = StringUtils.isNotBlank(proposedName) ? StringUtils.trim(proposedName) : policy.getName();
		// Creates the policy
		String uid = createPolicy(name);
		// Description
		updatePolicy(uid, PolicyField.DESCRIPTION, policy.getDescription());
		// FIXME Groups
		// FIXME Rules
		// OK
		return uid;
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
					.addValue(UID, uid)
					.addValue(NAME, name));
		// OK
		return uid;
	}
	
	@Override
	@Transactional
	@Secured(CipolRole.ADMIN)
	@CacheEvict("policy")
	public void deletePolicy(String uid) {
		NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		log.debug("Deleting policy {}", uid);
		// Delete the associated parameters
		t.update(SQL.PARAM_REMOVE_ALL_FOR_POLICY, Collections.singletonMap(UID, uid));
		// Groups
		t.update(SQL.GROUP_REMOVE_ALL, 
				new MapSqlParameterSource()
					.addValue(CATEGORY, CATEGORY_POLICY)
					.addValue(REFERENCE, uid));
		// Delete the policy
		t.update(SQL.POLICY_DELETE, Collections.singletonMap(UID, uid));
	}
	
	@Override
	@Transactional
	@CacheEvict("policy")
	@Secured(CipolRole.ADMIN)
	public void updatePolicy(String uid, PolicyField field, String value) {
		log.debug("Update policy {} with {} = {}", new Object[] {uid, field, value});
		// Loads the policy
		Policy policy = loadPolicy(uid);
		// Update
		BeanWrapper wrapper = new BeanWrapperImpl(policy);
		wrapper.setPropertyValue(field.getPropertyName(), value);
		// Update the policy
		getNamedParameterJdbcTemplate().update(
				SQL.POLICY_UPDATE,
				new MapSqlParameterSource()
					.addValue(UID, uid)
					.addValue(NAME, policy.getName())
					.addValue(DESCRIPTION, policy.getDescription()));
	}
	
	@Override
	@Transactional
	@CacheEvict("policy")
	@Secured(CipolRole.ADMIN)
	public void groupCreate(String uid, String name) {
		log.debug("Create policy group {} for {}", new Object[] {name, uid});
		try {
			// Checks the policy exists
			checkPolicyExists (uid);
			// Creates the group
			getNamedParameterJdbcTemplate().update(SQL.POLICY_GROUP_CREATE,
					new MapSqlParameterSource()
						.addValue(UID, uid)
						.addValue(NAME, name));
		} catch (DuplicateKeyException ex) {
			throw new PolicyGroupAlreadyExistsException(uid, name);
		}
	}
	
	@Override
	@Transactional
	@CacheEvict("policy")
	@Secured(CipolRole.ADMIN)
	public void groupDelete(String uid, String name) {
		log.debug("Delete policy group {} for {}", new Object[] {name, uid});
		// Checks the policy exists
		checkPolicyExists (uid);
		// Deletes the group
		getNamedParameterJdbcTemplate().update(SQL.POLICY_GROUP_DELETE,
				new MapSqlParameterSource()
					.addValue(UID, uid)
					.addValue(NAME, name));
	}

	protected void checkPolicyExists(String uid) throws EmptyResultDataAccessException {
		try {
			getNamedParameterJdbcTemplate().queryForMap(SQL.POLICY_FIND_BY_UID, Collections.singletonMap(UID, uid));
		} catch (EmptyResultDataAccessException ex) {
			throw new PolicyNotFoundException(uid);
		}
	}

	@Override
	@Cacheable("policy")
	@Transactional(readOnly = true)
	@Secured(CipolRole.ANONYMOUS)
	public Policy loadPolicy(final String policyId) {
		log.debug("Loading policy {}", policyId);
		try {
			// TODO Uses external mappers that use a named-parameter JDBC template
			final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
			// Gets the policy
			Policy policy = t.queryForObject(SQL.POLICY_FIND_BY_UID, Collections.singletonMap(UID, policyId), new RowMapper<Policy>() {
				@Override
				public Policy mapRow(ResultSet rs, int i)
						throws SQLException {
					Policy policy = new Policy();
					policy.setUid(policyId);
					policy.setName(rs.getString(NAME));
					policy.setDescription(rs.getString(DESCRIPTION));
					// Policy groups
					List<Group> groups = t.query(SQL.GROUP_FIND_BY_CATEGORY_AND_REFERENCE,
								new MapSqlParameterSource()
									.addValue(CATEGORY, CATEGORY_POLICY)
									.addValue(REFERENCE, policyId),
								new RowMapper<Group>() {
									@Override
									public Group mapRow(ResultSet rs, int arg1)
											throws SQLException {
										Group o = new Group();
										o.setName(rs.getString(NAME));
										List<String> members = Arrays.asList(StringUtils.split(rs.getString(MEMBERS), ","));
										o.setMembers(members);
										return o;
									}
								}
							);
					policy.setGroups(groups);
					// Policy rulesets
					List<RuleSet> rulesets = t.query(SQL.RULESET_FIND_BY_POLICY,
							Collections.singletonMap(UID, policyId),
							new RowMapper<RuleSet>() {
								@Override
								public RuleSet mapRow(ResultSet rs, int i)
										throws SQLException {
									RuleSet o = new RuleSet();
									o.setId(rs.getInt(ID));
									o.setPath(rs.getString(PATH));
									o.setDescription(rs.getString(DESCRIPTION));
									o.setDisabled(rs.getBoolean(DISABLED));
									// Rules
									List<RuleDefinition> ruleDefinitions = t.query (
											SQL.RULEDEF_FIND_BY_RULESET,
											Collections.singletonMap(RULESETID, rs.getInt(ID)),
											new RowMapper<RuleDefinition>() {
												@Override
												public RuleDefinition mapRow(
														ResultSet rs,
														int i)
														throws SQLException {
													RuleDefinition o = new RuleDefinition();
													o.setRuleId(rs.getString(RULE_ID));
													o.setDescription(rs.getString(DESCRIPTION));
													o.setDisabled(rs.getBoolean(DISABLED));
													// Rule parameters
													// TODO Uses a common mapper
													List<ParamValue> params = t.query (
															SQL.PARAM_FIND_BY_CATEGORY_AND_REFERENCE,
															new MapSqlParameterSource()
																.addValue(CATEGORY, "RULEDEF")
																.addValue(REFERENCE, rs.getInt(ID)),
															new RowMapper<ParamValue>() {
																@Override
																public ParamValue mapRow(
																		ResultSet rs,
																		int i)
																		throws SQLException {
																	ParamValue o = new ParamValue();
																	o.setName(rs.getString(NAME));
																	o.setValue(rs.getString(VALUE));
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
				policy.setUid(rs.getString(UID));
				policy.setName(rs.getString(NAME));
				policy.setDescription(rs.getString(DESCRIPTION));
				return policy;
			}
		});
	}

}
