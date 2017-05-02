drop table if exists USERS;
create table USERS(
    USERNAME			VARCHAR(64) NOT NULL,
    FIRST_NAME			VARCHAR(64) NOT NULL,
    LAST_NAME			VARCHAR(64) NOT NULL,
    EMAIL				VARCHAR(64) NOT NULL,
    PASSWORD 			VARCHAR(64) NOT NULL,
    ENABLED				BOOLEAN,
    ACCOUNT_NON_EXPIRED	BOOLEAN,
    CREDS_NON_EXPIRED	BOOLEAN,
    ACCOUNT_NON_LOCKED	BOOLEAN,
    ROLE				VARCHAR(32) NOT NULL,
PRIMARY KEY (USERNAME),
UNIQUE(EMAIL)
);

drop table if exists USER_LOGONS;
create table USER_LOGONS(
    Date			DATETIME,
	USERNAME		VARCHAR(128) NOT NULL
);

INSERT INTO users values('mwelsh', 'Michael', 'Welsh', 'michael.welsh@cloudaxis.com', 'pooper', 1, 1, 1, 1, 'ROLE_ADMIN');
INSERT INTO users values('dhuang', 'Da Ming', 'Huang', 'damien.huang@cloudaxis.com', 'damien', 1, 1, 1, 1, 'ROLE_ADMIN');

ALTER TABLE users_profile MODIFY user_id INTEGER NOT NULL AUTO_INCREMENT = 50;

