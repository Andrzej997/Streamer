INSERT INTO USERS_SCHEMA.USERS
(USER_ID, USER_NAME, PASSWORD, NAME, SURNAME, NATIONALITY, EMAIL, account_expiration_date, account_locked, password_expiration_date)
VALUES (DEFAULTDBSEQ.nextval, 'test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
'test', 'test', 'PL', 'test@test', current_date + 120, false, current_date + 120);

INSERT INTO USERS_SCHEMA.USERS
(USER_ID, USER_NAME, PASSWORD, NAME, SURNAME, NATIONALITY, EMAIL, account_expiration_date, account_locked, password_expiration_date)
VALUES (DEFAULTDBSEQ.nextval, 'test2', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
'test2', 'test2', 'PL', 'test2@test2', current_date + 120, false, current_date + 120);

INSERT INTO USERS_SCHEMA.USERS
(USER_ID, USER_NAME, PASSWORD, NAME, SURNAME, NATIONALITY, EMAIL, account_expiration_date, account_locked, password_expiration_date)
VALUES (DEFAULTDBSEQ.nextval, 'test3', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
'test3', 'test3', 'PL', 'test3@test3', current_date + 120, false, current_date + 120);

INSERT INTO USERS_SCHEMA.USERS
(USER_ID, USER_NAME, PASSWORD, NAME, SURNAME, NATIONALITY, EMAIL, account_expiration_date, account_locked, password_expiration_date)
VALUES (DEFAULTDBSEQ.nextval, 'admin', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
'admin', 'admin', 'PL', 'admin@admin', current_date + 120, false, current_date + 120);

INSERT INTO USERS_SCHEMA.USERS
(USER_ID, USER_NAME, PASSWORD, NAME, SURNAME, NATIONALITY, EMAIL, account_expiration_date, account_locked, password_expiration_date)
VALUES (DEFAULTDBSEQ.nextval, 'test10', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
'test10', 'test10', 'PL', 'test10@test10', current_date + 120, false, current_date + 120);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (1, 'ROLE_ADMIN', 1);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (2, 'ROLE_USER', 1);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (3, 'ROLE_USER', 2);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (4, 'ROLE_USER', 3);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (5, 'ROLE_ADMIN', 4);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (6, 'ROLE_USER', 4);

INSERT INTO USERS_SCHEMA.AUTHORITY
(authority_id, authority, user_id)
VALUES (7, 'ROLE_USER', 5);