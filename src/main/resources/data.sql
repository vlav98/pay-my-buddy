CREATE TABLE Persons (
    PersonID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Email varchar(255) NOT NULL,
    Password varchar(255) NOT NULL
);

CREATE TABLE BankAccounts (
    BankAccountID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    RIB varchar(255),
    PersonID int,
    FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
);

CREATE TABLE Transactions (
    TransactionID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    SenderID INT NOT NULL ,
    FOREIGN KEY (SenderID) REFERENCES Persons(PersonID),
    Recipient varchar(255) NOT NULL,
    Description varchar(255) NOT NULL,
    Amount int NOT NULL,
    BankAccountID INT NOT NULL,
    FOREIGN KEY (BankAccountID) REFERENCES BankAccounts(BankAccountID)
);

CREATE TABLE Friends (
    Person1ID INT NOT NULL,
    Person2ID INT NOT NULL,
    PRIMARY KEY (Person1ID, Person2ID),
    FOREIGN KEY (Person1ID) REFERENCES Persons(PersonID),
    FOREIGN KEY (Person2ID) REFERENCES Persons(PersonID)
);

INSERT INTO Persons (Email, Password)
VALUES ('user1@example.com', 'password1'),
    ('user2@example.com', 'password2');

INSERT INTO BankAccounts (RIB, PersonID)
VALUES ('FR1234567890123456789012345', 1),
    ('FR9876543210987654321012345', 2);

INSERT INTO Friends (Person1ID, Person2ID)
VALUES (1, 2), (2, 1);

INSERT INTO Transactions (TransactionID, SenderID, Recipient, amount, description, BankAccountID)
VALUES (1, 2, 'user2@example.com', 100.00, 'Payment for Book', 2);
