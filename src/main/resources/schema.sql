-- -----------------------------------------------------
-- Table `capabilities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS capabilities (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(90)  NOT NULL
);

-- -----------------------------------------------------
-- Junction table `capability_technology`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS capability_technology (
  id             BIGINT PRIMARY KEY AUTO_INCREMENT,
  capability_id  BIGINT      NOT NULL,
  technology_id  BIGINT      NOT NULL,

  CONSTRAINT FK_ct_capability
    FOREIGN KEY (capability_id)
    REFERENCES capabilities (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT FK_ct_technology
    FOREIGN KEY (technology_id)
    REFERENCES technologies (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

