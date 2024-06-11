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

INSERT INTO transactions(transactionID, sender_id, recipient, description, amount, bank_accountID, transaction_date)
VALUES (1,1,'admin@email.com','Voyage',100.00,1,'2024-06-01 09:25:45'),
       (2,2,'alice@email.com','Restaurant',50.00,2,'2024-06-01 11:25:45'),
       (3,1,'admin@email.com','Trip',100.00,1,'2024-06-01 11:25:45'),
       (4,2,'test@email.com','Cinema',8.00,2,'2024-06-01 18:25:45'),
       (5,1,'admin@mail.com','Goodies',10.00,1,'2024-06-03 11:25:45'),
       (6,1,'admin@mail.com','Bubble tea',10.00,1,'2024-06-03 11:25:45'),
       (7,1,'admin@mail.com','Birthday',10.00,1,'2024-06-04 11:25:45'),
       (8,1,'alice@mail.com','Concert',30.00,1,'2024-06-07 11:26:39'),
       (9,1,'admin@mail.com','Italy trip',20.00,1,'2024-06-09 11:30:45'),
       (10,1,'alice@mail.com','Popmart',15.00,1,'2024-06-10 11:31:04'),
       (11,1,'alice@mail.com','Snacks',10.00,1,'2024-06-11 11:31:23');
