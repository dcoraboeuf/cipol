package net.cipol.core;

public interface SQL {

	String POLICY_FIND_ALL = "select * from POLICY";
	
	String POLICY_FIND_BY_UID = "select * from POLICY where uid = :uid";
	
	String RULESET_FIND_BY_POLICY = "select * from RULESET where policy = :uid order by id";
	
	String RULEDEF_FIND_BY_RULESET = "select * from RULEDEF where ruleset = :rulesetid order by id";
	
	String PARAM_FIND_BY_CATEGORY_AND_REFERENCE = "select * from PARAM where category = :category and reference = :reference order by name, id";
	
	String GROUP_FIND_BY_CATEGORY_AND_REFERENCE = "select * from GROUPS where category = :category and reference = :reference order by name";

}
