-- The README names the tables and columns in camelCase (timeDeposits, planType, timeDepositId).
-- They are created in snake_case instead: it is the PostgreSQL convention, and unquoted camelCase
-- identifiers are folded to lower case, so they would have to be double-quoted in every query.
CREATE TABLE time_deposits
(
    id        INTEGER PRIMARY KEY,
    plan_type VARCHAR(50)    NOT NULL,
    days      INTEGER        NOT NULL,
    balance   NUMERIC(19, 2) NOT NULL
);

CREATE TABLE withdrawals
(
    id              INTEGER PRIMARY KEY,
    time_deposit_id INTEGER        NOT NULL REFERENCES time_deposits (id),
    amount          NUMERIC(19, 2) NOT NULL,
    date            DATE           NOT NULL
);

CREATE INDEX idx_withdrawals_time_deposit_id ON withdrawals (time_deposit_id);
