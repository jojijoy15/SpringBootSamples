
SET SCHEMA 'spring-security';

CREATE TABLE AUTHORITY(
	role_id INT GENERATED ALWAYS AS IDENTITY, 
	user_role VARCHAR(100),
	PRIMARY KEY(role_id)
);

CREATE TABLE USERS(
	user_id INT GENERATED ALWAYS AS IDENTITY,
	user_name VARCHAR(50),
	password VARCHAR(100),
	user_role INTEGER,
	CONSTRAINT user_role_fk
      FOREIGN KEY(user_role)
        REFERENCES AUTHORITY(role_id),
	PRIMARY KEY(user_id)
);

---

INSERT INTO AUTHORITY("user_role")
	VALUES('admin');

INSERT INTO USERS("user_name", "password", "user_role") 
	VALUES('jojijoy', '{noop}password' , 1);