INSERT INTO users (userID, first_name, last_name, email, password, balance)
VALUES (1, 'Test', 'User', 'test@email.com', '$2a$10$82yUiOnczdZcqmrPHYSSJusXbvt9pm4SezIz8SgGiOnMUiee42pmi', 0),
       (2, 'Admin', 'Account', 'admin@mail.com', '$2a$10$82yUiOnczdZcqmrPHYSSJusXbvt9pm4SezIz8SgGiOnMUiee42pmi', 0),
       (3, 'Alice', 'Dupont', 'alice@mail.com', '$2a$10$82yUiOnczdZcqmrPHYSSJusXbvt9pm4SezIz8SgGiOnMUiee42pmi', 0),
       (4, 'Richard', 'Leroy', 'richard@mail.com', '$2a$10$82yUiOnczdZcqmrPHYSSJusXbvt9pm4SezIz8SgGiOnMUiee42pmi', 0),
       (5, 'Edward', 'Smith', 'edward@mail.com', '$2a$10$82yUiOnczdZcqmrPHYSSJusXbvt9pm4SezIz8SgGiOnMUiee42pmi', 0);

INSERT INTO bank_accounts(bank_accountID, email, RIB, userID)
VALUES (1, 'test@email.com', 'FR7630001007941234567890000', 1),
       (2, 'alice@mail.com', 'FR7630001007941234567890001', 2);

INSERT INTO beneficiary(sender, recipient)
VALUES (1, 2), (2, 1), (2, 3);

INSERT INTO transactions(transactionID, sender_id, recipient, description, amount, bank_accountID)
VALUES (1, 1, 'admin@email.com', 'Voyage', 100.00, 1),
       (2, 2, 'alice@email.com', 'Restaurant', 50.00, 2),
       (3, 1, 'admin@email.com', 'Voyage', 100.00, 1),
       (4, 2, 'test@email.com', 'Cinema', 7.50, 2);

SELECT * FROM transactions;
