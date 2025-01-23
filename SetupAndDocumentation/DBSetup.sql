DROP TABLE IF EXISTS `VideoGameExchange`.`Cities`;
CREATE TABLE `VideoGameExchange`.`Cities` (
  `cityId` INT NOT NULL AUTO_INCREMENT,
  `cityName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`CityId`));
  
  INSERT INTO `VideoGameExchange`.`Cities` (cityName) Values
	("Salt Lake City"), ("West Salem");
  
  DROP TABLE IF EXISTS `VideoGameExchange`.`States`;
  CREATE TABLE `VideoGameExchange`.`States` (
  `stateId` INT NOT NULL AUTO_INCREMENT,
  `stateName` VARCHAR(45) NOT NULL,
  `stateAbbr` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`StateId`));
  
  INSERT INTO `VideoGameExchange`.`States` (stateName, stateAbbr) Values 
	("Alabama", "AL"),("Alaska", "AK"),("Arizona", "AZ"),
    ("Arkansas", "AR"),("California", "CA"),
    ("Colorado", "CA"),("Connecticut", "CT"),
    ("Delaware", "DE"),("District of Colombia", "DC"),
	("Florida", "FL"),("Georgia", "GA"),("Hawaii", "HI"),
    ("Idaho", "ID"),("Illinois", "IL"), ("Indiana", "IN"), ("Iowa", "IA"),("Kansas", "KS"),
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
	`zipId` INT NOT NULL AUTO_INCREMENT,
    `zipCode` VARCHAR(5) NOT NULL,
    PRIMARY KEY (`ZipId`));
    
INSERT INTO `VideoGameExchange`.`Zips` (zipCode) VALUES 
	("84102"), ("54669");

DROP TABLE IF EXISTS `VideoGameExchange`.`Addresses`;
CREATE TABLE `VideoGameExchange`.`Addresses` (
  `addressId` INT NOT NULL AUTO_INCREMENT,
  `addressLine1` VARCHAR(45) NOT NULL,
  `addressLine2` VARCHAR(45) NULL,
  `cityId` INT NOT NULL,
  `stateId` INT NOT NULL,
  `zipId` INT NOT NULL,
  PRIMARY KEY (`addressId`),
  CONSTRAINT `fk_City`
    FOREIGN KEY (`cityId`)
    REFERENCES `VideoGameExchange`.`Cities` (`cityId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_State`
    FOREIGN KEY (`stateId`)
    REFERENCES `VideoGameExchange`.`States` (`stateId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_zip`
	FOREIGN KEY (`zipId`)
    REFERENCES `VideoGameExchange`.`Zips` (`zipId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
INSERT INTO `VideoGameExchange`.`Addresses` (addressLine1, addressLine2, cityId, stateId, zipId) VALUES
	("343 South, 500 East", "Apt 217", 1, 45, 1),
    ("W3897 Hidden River Rd", NULL, 2, 50, 2);

DROP TABLE IF EXISTS `VideoGameExchange`.`People`;
CREATE TABLE `VideoGameExchange`.`People` (
  `personId` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NULL,
  `emailAddr` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `addressId` INT NOT NULL,
  PRIMARY KEY (`personId`),
  CONSTRAINT `fk_Address`
    FOREIGN KEY (`addressId`)
    REFERENCES `VideoGameExchange`.`Addresses` (`addressId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
INSERT INTO `VideoGameExchange`.`People` (firstName, lastName, emailAddr, `password`, addressId) VALUES 
	("Matthew", "Stratton", "test@email.com", "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86", 2),
    ("David", "Stratton", "dev@example.com", "2e35c34967ac9e320c5b8516ea58d7ff9954c0b1c6495d8db610fd34f4df53e6f3648029f89d5e06986386616cf4693431c7d73b89d998f15d7a690ae384fd54", 1);

DROP TABLE IF EXISTS `VideoGameExchange`.`Systems`;
CREATE TABLE `VideoGameExchange`.`Systems` (
  `systemId` INT NOT NULL AUTO_INCREMENT,
  `systemName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SystemId`));
  
INSERT INTO `VideoGameExchange`.`Systems` (systemName) VALUES 
	("Windows"), ("PlayStatGamion"), ("PlayStation 2"), ("PlayStation 3"),
    ("PlayStation 4"), ("PlayStation 5"), ("XBox"), ("XBox 360"), ("XBox One"),
    ("NES"), ("WII"), ("SNES"), ("N64"), ("GameCube"), ("WII U"), ("Switch");
    
DROP TABLE IF EXISTS `VideoGameExchange`.`Publishers`;
CREATE TABLE `VideoGameExchange`.`Publishers` (
  `publisherId` INT NOT NULL AUTO_INCREMENT,
  `publisherName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`publisherId`));
  
INSERT INTO `VideoGameExchange`.`Publishers` (publisherName) VALUES
	("Blizzard"), ("Nintendo"), ("Sony");

DROP TABLE IF EXISTS `VideoGameExchange`.`Games`;
CREATE TABLE `VideoGameExchange`.`Games` (
  `gameID` INT NOT NULL AUTO_INCREMENT,
  `gameTitle` VARCHAR(100) NOT NULL,
  `publisherId` INT NOT NULL,
  `publicationYear` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`gameID`),
  CONSTRAINT `fk_Publisher`
    FOREIGN KEY (`publisherId`)
    REFERENCES `VideoGameExchange`.`Publishers` (`publisherId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
INSERT INTO `VideoGameExchange`.`Games` (gameTitle, publisherId, publicationYear) VALUES 
	("Diablo", 1, "1996"), ("Diablo 2", 1, "2000"), ("Jak and Daxter", 3, "2001");

DROP TABLE IF EXISTS `VideoGameExchange`.`Conditions`;
CREATE TABLE `VideoGameExchange`.`Conditions` (
  `conditionId`  TINYINT NOT NULL AUTO_INCREMENT,
  `conditionLabel` VARCHAR(4) NOT NULL,
  PRIMARY KEY (`ConditionId`));

INSERT INTO `VideoGameExchange`.`Conditions` (conditionLabel) VALUES 
	("MINT"), ("GOOD"), ("FAIR"), ("POOR");
    
DROP TABLE IF EXISTS `VideoGameExchange`.`OfferRecords`;
CREATE TABLE `VideoGameExchange`.`OfferRecords` (
  `offerRecordId` INT NOT NULL AUTO_INCREMENT,
  `offerStatus` VARCHAR(10) NOT NULL,
  `offerCreationTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`offerRecordId`));

DROP TABLE IF EXISTS `VideoGameExchange`.`GameOwnerRecords`;     
CREATE TABLE `VideoGameExchange`.`GameOwnerRecords` (
  `gameOwnerRecordId` INT NOT NULL AUTO_INCREMENT,
  `ownerId` INT NOT NULL, 
  `gameId` INT NOT NULL,
  `systemId` INT NOT NULL,
  `conditionId` TINYINT NOT NULL,
  `isInOffer` BOOLEAN NOT NULL DEFAULT 0,
  `offerRecordId` INT,
  `offerSenderId` INT,
  PRIMARY KEY (`gameOwnerRecordId`),
  CONSTRAINT `fk_Owner`
    FOREIGN KEY (`ownerId`)
    REFERENCES `VideoGameExchange`.`People` (`personId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Game`
    FOREIGN KEY (`gameId`)
    REFERENCES `VideoGameExchange`.`Games` (`gameID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_System`
    FOREIGN KEY (`systemId`)
    REFERENCES `VideoGameExchange`.`Systems` (`systemId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Condtition`
    FOREIGN KEY (`conditionId`)
    REFERENCES `VideoGameExchange`.`Conditions` (`conditionId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OfferRecordId`
	FOREIGN KEY (`offerRecordId`)
    REFERENCES `VideoGameExchange`.`OfferRecords` (`offerRecordId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OfferSneder`
	FOREIGN KEY (`offerSenderId`)
    REFERENCES `VideoGameExchange`.`People` (`personId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
INSERT INTO `VideoGameExchange`.`GameOwnerRecords` (ownerId, gameId, systemId, conditionId) VALUES 
	(2, 1, 1, 2), (2, 2, 1, 2), (1, 3, 3, 1); 
    

