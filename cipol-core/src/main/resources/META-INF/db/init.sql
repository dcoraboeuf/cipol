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
