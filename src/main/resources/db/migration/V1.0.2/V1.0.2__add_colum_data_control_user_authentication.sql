ALTER TABLE user_authentication
    ADD COLUMN CREATE_DATE timestamp without time zone,
    ADD COLUMN CREATE_USER bigint,
    ADD COLUMN UPDATE_DATE timestamp without time zone,
    ADD COLUMN UPDATE_USER bigint;


ALTER TABLE user_authentication
    ADD CONSTRAINT FK_CREATE_USER FOREIGN KEY (CREATE_USER )
    REFERENCES user_authentication( id )
    ON DELETE No Action
    ON UPDATE No Action;

ALTER TABLE user_authentication
    ADD CONSTRAINT FK_UPDATE_USER FOREIGN KEY (UPDATE_USER )
    REFERENCES user_authentication( id )
    ON DELETE No Action
    ON UPDATE No Action;


UPDATE user_authentication set CREATE_USER = 1, CREATE_DATE = now();

ALTER TABLE user_authentication ALTER COLUMN CREATE_DATE SET NOT NULL;
ALTER TABLE user_authentication ALTER COLUMN CREATE_USER SET NOT NULL;
