CREATE TABLE VERSION (
	VALUE INTEGER NOT NULL,
	UPDATED TIMESTAMP NOT NULL
);

CREATE TABLE POLICY (
	UID VARCHAR(40) NOT NULL,
	NAME VARCHAR(200) NOT NULL,
	DESCRIPTION VARCHAR(3000) NULL,
	CONSTRAINT PK_POLICY PRIMARY KEY (UID),
	CONSTRAINT UQ_POLICY_NAME UNIQUE (NAME)
);

CREATE TABLE POLICY_ACL (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	POLICY VARCHAR(40) NOT NULL,
	NAME VARCHAR(40) NOT NULL,
	ROLE VARCHAR(40) NOT NULL,
	CONSTRAINT PK_POLICY_ACL PRIMARY KEY (ID),
	CONSTRAINT UQ_POLICY_ACL UNIQUE (POLICY, NAME),
	CONSTRAINT FK_POLICY_ACL_POLICY FOREIGN KEY (POLICY) REFERENCES POLICY (UID) ON DELETE CASCADE
);

CREATE TABLE GROUPS (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	CATEGORY VARCHAR(20) NOT NULL,
	REFERENCE VARCHAR(40) NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	MEMBERS VARCHAR(3000) NOT NULL,	
	CONSTRAINT PK_GROUP PRIMARY KEY (ID),
	CONSTRAINT UQ_GROUP UNIQUE (CATEGORY, REFERENCE, NAME)
);

CREATE INDEX IDX_GROUP_CATEGORY_REFERENCE ON GROUPS ( CATEGORY, REFERENCE);

CREATE TABLE RULESET (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	POLICY VARCHAR(40) NOT NULL,
	PATH VARCHAR(300) NOT NULL,
	DESCRIPTION VARCHAR(3000) NULL,
	DISABLED BOOLEAN NOT NULL,
	CONSTRAINT PK_RULESET PRIMARY KEY (ID),
	CONSTRAINT UQ_RULESET_POLICY_PATH UNIQUE (POLICY, PATH),
	CONSTRAINT FK_RULESET_POLICY FOREIGN KEY (POLICY) REFERENCES POLICY (UID) ON DELETE CASCADE
);

CREATE INDEX IDX_RULESET_POLICY ON RULESET ( POLICY );

CREATE TABLE RULEDEF (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	RULESET INTEGER NOT NULL,
	RULEID VARCHAR(100) NOT NULL,
	DESCRIPTION VARCHAR(3000) NULL,
	DISABLED BOOLEAN NOT NULL,
	CONSTRAINT PK_RULEDEF PRIMARY KEY (ID),
	CONSTRAINT FK_RULEDEF_RULESET FOREIGN KEY (RULESET) REFERENCES RULESET (ID) ON DELETE CASCADE
);

CREATE TABLE PARAM (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	CATEGORY VARCHAR(120) NOT NULL,
	REFERENCE VARCHAR(40) NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	VALUE VARCHAR(3000) NULL,	
	CONSTRAINT PK_PARAM	PRIMARY KEY (ID),
	CONSTRAINT UQ_PARAM UNIQUE (CATEGORY, REFERENCE, NAME)
);

CREATE INDEX IDX_PARAM_CATEGORY_REFERENCE ON PARAM ( CATEGORY, REFERENCE);

CREATE TABLE INSTANCE (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	CATEGORY VARCHAR(120) NOT NULL,
	REFERENCE VARCHAR(40) NOT NULL,
	CONSTRAINT PK_INSTANCE PRIMARY KEY (ID),
	CONSTRAINT UQ_INSTANCE UNIQUE (CATEGORY, REFERENCE)
);
