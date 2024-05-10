DROP TABLE Transactions CASCADE;
DROP TABLE BankAccounts CASCADE;
DROP TABLE Beneficiary CASCADE;
DROP TABLE Users CASCADE;
CREATE TABLE users (
    userID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    firstName varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL
);

CREATE TABLE bankAccounts (
    bankAccountID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    RIB varchar(255),
    userID int NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(userID)
);

CREATE TABLE transactions (
    transactionID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    senderID INT NOT NULL ,
    FOREIGN KEY (senderID) REFERENCES users(userID),
    recipient varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    amount int NOT NULL,
    bankAccountID INT NOT NULL,
    FOREIGN KEY (bankAccountID) REFERENCES bankAccounts(bankAccountID)
);

CREATE TABLE beneficiary (
    sender INT NOT NULL,
    recipient INT NOT NULL,
    PRIMARY KEY (sender, recipient),
    FOREIGN KEY (sender) REFERENCES users(userID),
    FOREIGN KEY (recipient) REFERENCES users(userID)
);

INSERT INTO users (firstName, email, password)
VALUES ('User1','user1@example.com', 'password1'),
    ('User2','user2@example.com', 'password2');

INSERT INTO bankAccounts (email, RIB, userID)
VALUES ('user1@example.com', 'FR1234567890123456789012345', 1),
    ('user2@example.com', 'FR9876543210987654321012345', 2);

INSERT INTO beneficiary (sender, recipient)
VALUES (1, 2), (2, 1);

INSERT INTO transactions (transactionID, senderID, recipient, description, amount, bankAccountID)
VALUES (1, 2, 'user2@example.com', 'Payment for Book', 100.00, 2);
