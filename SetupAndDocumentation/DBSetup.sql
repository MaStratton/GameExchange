DROP TABLE IF EXISTS `VideoGameExchange`.`Cities`;
CREATE TABLE `VideoGameExchange`.`Cities` (
  `CityId` INT NOT NULL AUTO_INCREMENT,
  `CityName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`CityId`));
  
  DROP TABLE IF EXISTS `VideoGameExchange`.`States`;
  CREATE TABLE `VideoGameExchange`.`States` (
  `StateId` INT NOT NULL AUTO_INCREMENT,
  `StateName` VARCHAR(45) NOT NULL,
  `StateAbbr` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`StateId`));
  
  INSERT INTO `VideoGameExchange`.`States` (StateName, StateAbbr) Values 
	("Alabama", "AL"),("Alaska", "AK"),("Arizona", "AZ"),
    ("Arkansas", "AR"),("California", "CA"),
    ("Colorado", "CA"),("Connecticut", "CT"),
    ("Delaware", "DE"),("District of Colombia", "DC"),
	("Florida", "FL"),("Georgia", "GA"),("Hawaii", "HI"),
    ("Idaho", "ID"),("Iowa", "IA"),("Kansas", "KS"),
    ("Kentucky", "KY"),("Louisiana", "LA"),("Maine", "ME"),
    ("Maryland", "MD"),("Massachusetts", "MA"),("Michigan", "MI"),
    ("Minesota", "MN"),("Mississippi", "MS"),("Missouri", "MO"),
    ("Montana", "MT"),("Nebraska", "NE"),("Nevada", "NV"),
    ("New Hampshire", "NH"),("New Jersy", "NJ"),("New Mexico", "NM"),
    ("New York", "NY"),("North Carolina", "NC"),("North Dekota", "ND"),
    ("Ohio", "OH"),("Oklahoma", "OK"),("Oregon", "OR"),
    ("Pennsylvania", "PA"),("Rhode Island", "RI"),("South Carolina", "SC"),
    ("South Dekota", "SD"),("Tennessee", "TN"),("Texas", "TX"),
    ("Utah", "UT"),("Vermont", "VT"),("Virginia", "VA"),("Washington", "WA"),
    ("West Virginia", "WV"),("Wisconsin", "WI"),("Wyoming", "WY");
    
DROP TABLE IF EXISTS `VideoGameExchange`.`Zips`;
CREATE TABLE `VideoGameExchange`.`Zips` (
	`ZipId` INT NOT NULL AUTO_INCREMENT,
    `ZipCode` VARCHAR(5) NOT NULL,
    PRIMARY KEY (`ZipId`));
    
DROP TABLE IF EXISTS `VideoGameExchange`.`Addresses`;
CREATE TABLE `VideoGameExchange`.`Addresses` (
  `AddressId` INT NOT NULL AUTO_INCREMENT,
  `AddressLine1` VARCHAR(45) NOT NULL,
  `AddressLine2` VARCHAR(45) NULL,
  `CityId` INT NOT NULL,
  `StateId` INT NOT NULL,
  `ZipId` INT NOT NULL,
  PRIMARY KEY (`AddressId`),
  INDEX `fk_Addresses_1_idx` (`CityId` ASC) VISIBLE,
  INDEX `fk_State_idx` (`StateId` ASC) VISIBLE,
  CONSTRAINT `fk_City`
    FOREIGN KEY (`CityId`)
    REFERENCES `VideoGameExchange`.`Cities` (`CityId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_State`
    FOREIGN KEY (`StateId`)
    REFERENCES `VideoGameExchange`.`States` (`StateId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_zip`
	FOREIGN KEY (`ZipId`)
    REFERENCES `VideoGameExchange`.`Zips` (`ZipId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

DROP TABLE IF EXISTS `VideoGameExchange`.`People`;
CREATE TABLE `VideoGameExchange`.`People` (
  `PersonId` INT NOT NULL AUTO_INCREMENT,
  `FirstName` VARCHAR(45) NOT NULL,
  `LastName` VARCHAR(45) NULL,
  `EmailAddr` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(128) NOT NULL,
  `AddressId` INT NOT NULL,
  PRIMARY KEY (`PersonId`),
  CONSTRAINT `fk_Address`
    FOREIGN KEY (`AddressId`)
    REFERENCES `VideoGameExchange`.`Addresses` (`AddressId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
DROP TABLE IF EXISTS `VideoGameExchange`.`GameConditions`;
CREATE TABLE `VideoGameExchange`.`GameConditions` (
  `ConditionId` INT NOT NULL AUTO_INCREMENT,
  `ConditionLabel` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`ConditionId`));
  
INSERT INTO `VideoGameExchange`.`GameConditions` (ConditionLabel) Values 
	("Poor"), ("fair"), ("Good"), ("Mint");

DROP TABLE IF EXISTS `VideoGameExchange`.`Systems`;
CREATE TABLE `VideoGameExchange`.`Systems` (
  `SystemId` INT NOT NULL AUTO_INCREMENT,
  `SystemName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SystemId`));
  
INSERT INTO `VideoGameExchange`.`Systems` (SystemName) VALUES 
	("Windows"), ("PlayStation"), ("PlayStation 2"), ("PlayStation 3"),
    ("PlayStation 4"), ("PlayStation 5"), ("XBox"), ("XBox 360"), ("XBox One"),
    ("NES"), ("WII"), ("SNES"), ("N64"), ("GameCube"), ("WII U"), ("Switch");
    
DROP TABLE IF EXISTS `VideoGameExchange`.`Publishers`;
CREATE TABLE `VideoGameExchange`.`Publishers` (
  `PublisherId` INT NOT NULL AUTO_INCREMENT,
  `PublisherName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`PublisherId`));

DROP TABLE IF EXISTS `VideoGameExchange`.`Games`;
CREATE TABLE `VideoGameExchange`.`Games` (
  `GameID` INT NOT NULL AUTO_INCREMENT,
  `GameTitle` VARCHAR(100) NOT NULL,
  `PublisherId` INT NOT NULL,
  `PublicationYear` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`GameID`),
  INDEX `fk_Publisher_idx` (`PublisherId` ASC) VISIBLE,
  CONSTRAINT `fk_Publisher`
    FOREIGN KEY (`PublisherId`)
    REFERENCES `VideoGameExchange`.`Publishers` (`PublisherId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

DROP TABLE IF EXISTS `VideoGameExchange`.`Conditions`;
CREATE TABLE `VideoGameExchange`.`Conditions` (
  `ConditionId`  TINYINT NOT NULL AUTO_INCREMENT,
  `ConditionName` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`ConditionId`));

INSERT INTO `VideoGameExchange`.`Conditions` (ConditionName) VALUES 
	("Mint"), ("Good"), ("Fair"), ("Poor");
    
DROP TABLE IF EXISTS `VideoGameExchange`.`OfferRecords`;
CREATE TABLE `VideoGameExchange`.`OfferRecords` (
  `OfferRecordId` INT NOT NULL AUTO_INCREMENT,
  `OfferStatus` VARCHAR(10) NOT NULL,
  `OfferCreationTime` TIMESTAMP NOT NULL,
  PRIMARY KEY (`OfferRecordId`));

DROP TABLE IF EXISTS `VideoGameExchange`.`GameOwnerRecords`;     
CREATE TABLE `VideoGameExchange`.`GameOwnerRecords` (
  `GameOwnerRecordId` INT NOT NULL AUTO_INCREMENT,
  `PersonId` INT NOT NULL,
  `GameId` INT NOT NULL,
  `SystemId` INT NOT NULL,
  `ConditionId` TINYINT NOT NULL,
  `IsInOffer` BOOLEAN NOT NULL,
  `OfferRecordId` INT,
  `OfferSenderId` INT,
  PRIMARY KEY (`GameOwnerRecordId`),
  INDEX `fk_Owner_idx` (`PersonId` ASC) VISIBLE,
  INDEX `fk_Game_idx` (`GameId` ASC) VISIBLE,
  INDEX `fk_System_idx` (`SystemId` ASC) VISIBLE,
  INDEX `fk_Condtition_idx` (`ConditionId` ASC) VISIBLE,
  CONSTRAINT `fk_Owner`
    FOREIGN KEY (`PersonId`)
    REFERENCES `VideoGameExchange`.`People` (`PersonId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Game`
    FOREIGN KEY (`GameId`)
    REFERENCES `VideoGameExchange`.`Games` (`GameID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_System`
    FOREIGN KEY (`SystemId`)
    REFERENCES `VideoGameExchange`.`Systems` (`SystemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Condtition`
    FOREIGN KEY (`ConditionId`)
    REFERENCES `VideoGameExchange`.`Conditions` (`ConditionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OfferRecordId`
	FOREIGN KEY (`OfferRecordId`)
    REFERENCES `VideoGameExchange`.`OfferRecords` (`OfferRecordId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OfferSneder`
	FOREIGN KEY (`OfferSenderId`)
    REFERENCES `VideoGameExchange`.`People` (`PersonId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
