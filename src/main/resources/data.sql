DROP TABLE Transactions CASCADE;
DROP TABLE bank_accounts CASCADE;
DROP TABLE Beneficiary CASCADE;
DROP TABLE Users CASCADE;
CREATE TABLE users (
    userID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL
);

CREATE TABLE bank_accounts (
    bank_accountID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    RIB varchar(255),
    userID int NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(userID)
);

CREATE TABLE transactions (
    transactionID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sender_id INT NOT NULL ,
    FOREIGN KEY (sender_id) REFERENCES users(userID),
    recipient varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    amount int NOT NULL,
    bank_accountID INT NOT NULL,
    FOREIGN KEY (bank_accountID) REFERENCES bank_accounts(bank_accountID)
);

CREATE TABLE beneficiary (
    sender INT NOT NULL,
    recipient INT NOT NULL,
    PRIMARY KEY (sender, recipient),
    FOREIGN KEY (sender) REFERENCES users(userID),
    FOREIGN KEY (recipient) REFERENCES users(userID)
);

INSERT INTO users (first_name, email, password)
VALUES ('User1','user1@example.com', 'password1'),
    ('User2','user2@example.com', 'password2');

INSERT INTO bank_accounts (email, RIB, userID)
VALUES ('user1@example.com', 'FR1234567890123456789012345', 1),
    ('user2@example.com', 'FR9876543210987654321012345', 2);

INSERT INTO beneficiary (sender, recipient)
VALUES (1, 2), (2, 1);

INSERT INTO transactions (transactionID, sender_id, recipient, description, amount, bank_accountID)
VALUES (1, 2, 'user2@example.com', 'Payment for Book', 100.00, 2);
