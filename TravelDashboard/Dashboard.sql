USE Dashboard_Atos;

CREATE TABLE User (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    userUsername VARCHAR(64) NOT NULL,
    userPassword VARBINARY(32) NOT NULL,
    userEmail VARCHAR(64) NOT NULL,
    userPassResetCode VARCHAR(32)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 AUTO_INCREMENT = 100000;

CREATE TABLE ExcelFile (
    excelFileID INT PRIMARY KEY AUTO_INCREMENT,
    excelUserID INT NOT NULL,
    excelFileName VARCHAR(64),
    excelUploadTime VARCHAR(64),
    CONSTRAINT ex_fk
        FOREIGN KEY (excelUserID)
        REFERENCES User (userID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE Claim (
    claimID INT PRIMARY KEY AUTO_INCREMENT,
    claimExcelFileID INT NOT NULL,
    claimWbsNumber VARCHAR(64),
    claimAmountInPound DOUBLE,
    claimCostElement VARCHAR(128),
    claimLocation VARCHAR(64),
    claimExpenseDate VARCHAR(64),
    claimTripEndDate VARCHAR(64),
    claimEmployeeID INT,
    claimWeekNumber INT,
    CONSTRAINT cl_fk
        FOREIGN KEY (claimExcelFileID)
        REFERENCES ExcelFile (excelFileID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE Budget (
    budgetID INT PRIMARY KEY AUTO_INCREMENT,
    budgetUserID INT NOT NULL,
    budgetDate VARCHAR(64),
    budgetAmount DOUBLE,
    CONSTRAINT bu_fk
        FOREIGN KEY (budgetUserID)
        REFERENCES User (userID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE KEY ID_Date (budgetUserID, budgetDate)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
