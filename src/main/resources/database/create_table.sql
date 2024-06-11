DROP TABLE Transactions CASCADE;
DROP TABLE bank_accounts CASCADE;
DROP TABLE Beneficiary CASCADE;
DROP TABLE Users CASCADE;

CREATE TABLE users (
    userID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
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
    FOREIGN KEY (bank_accountID) REFERENCES bank_accounts(bank_accountID),
    transaction_date TIMESTAMP NOT NULL
);

CREATE TABLE beneficiary (
    sender INT NOT NULL,
    recipient INT NOT NULL,
    PRIMARY KEY (sender, recipient),
    FOREIGN KEY (sender) REFERENCES users(userID),
    FOREIGN KEY (recipient) REFERENCES users(userID)
);

