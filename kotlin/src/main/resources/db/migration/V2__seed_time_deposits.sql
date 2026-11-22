-- Sample data so both endpoints can be tried right away via Swagger UI.
INSERT INTO time_deposits (id, plan_type, days, balance)
VALUES (1, 'basic', 45, 1234567.00),
       (2, 'student', 100, 10000.00),
       (3, 'student', 400, 10000.00),
       (4, 'premium', 40, 50000.00),
       (5, 'premium', 90, 50000.00),
       (6, 'basic', 20, 2500.00);

INSERT INTO withdrawals (id, time_deposit_id, amount, date)
VALUES (1, 1, 1000.00, '2026-01-15'),
       (2, 1, 250.50, '2026-02-10'),
       (3, 5, 5000.00, '2026-03-01');
