CREATE TABLE APP_USERS (
    id BIGSERIAL PRIMARY KEY,
    account_balance_pln NUMERIC(19, 4) default 0,
    account_balance_usd NUMERIC(19, 4) default 0,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL
);
