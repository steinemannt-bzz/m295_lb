SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema zoo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `zoo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `zoo` ;

-- -----------------------------------------------------
-- Table `zoo`.`keeper`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zoo`.`keeper` (
  `keeperId` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(50) NOT NULL,
  `lastname` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`keeperId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `zoo`.`animal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `zoo`.`animal` (
  `animalId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `species` VARCHAR(100) NOT NULL,
  `dateAcquired` DATE NULL DEFAULT NULL,
  `weight` DECIMAL(10,2) NULL DEFAULT NULL,
  `habitat` VARCHAR(100) NULL DEFAULT NULL,
  `isEndangered` TINYINT(1) NULL DEFAULT NULL,
  `keeperId` INT NULL DEFAULT NULL,
  PRIMARY KEY (`animalId`),
  INDEX `keeperId` (`keeperId` ASC) VISIBLE,
  CONSTRAINT `animal_ibfk_1`
    FOREIGN KEY (`keeperId`)
    REFERENCES `zoo`.`keeper` (`keeperId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
