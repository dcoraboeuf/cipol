package net.cipol.core;

public interface SQL {

	String POLICY_FIND_ALL = "select * from POLICY";
	
	String POLICY_FIND_BY_UID = "select * from POLICY where uid = :uid";

}
