DROP SCHEMA IF EXISTS clinic;
CREATE SCHEMA clinic;
USE clinic;

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
  id INT NOT NULL AUTO_INCREMENT,
  city VARCHAR(255) NOT NULL,
  street VARCHAR(255) NOT NULL,
  building_number VARCHAR(10) NOT NULL,
  flat_number VARCHAR(10),
  postal_code VARCHAR(10) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS departments;
CREATE TABLE departments(
	id INT NOT NULL AUTO_INCREMENT,
    department_name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    id_address INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(id_address) REFERENCES addresses(id)
);

-- Sample departments
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Weteranów', '17', NULL, '20-281');
INSERT INTO departments VALUES(NULL, 'Oddział dermatologiczny №1', 'Dermatologia', 1);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Doctora Witolda Chodźki', '2', NULL, '20-479');
INSERT INTO departments VALUES(NULL, 'Oddział dermatologiczny №2', 'Dermatologia', 2);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Ignacego Radziszewskiego', '4', NULL, '20-188');
INSERT INTO departments VALUES(NULL, 'Oddział okulistyczny №1', 'Okulistyka', 3);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Metalurgiczna', '71', NULL, '20-831');
INSERT INTO departments VALUES(NULL, 'Oddział okulistyczny №2', 'Okulistyka', 4);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Fabryczna', '16', NULL, '20-642');
INSERT INTO departments VALUES(NULL, 'Oddział okulistyczny №3', 'Okulistyka', 5);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Lubartowska', '47', NULL, '20-890');
INSERT INTO departments VALUES(NULL, 'Oddział psychologii №1', 'Psychologia', 6);

DROP TABLE IF EXISTS patients;
CREATE TABLE patients(
	id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    pesel VARCHAR(11) NOT NULL,
    phone_number VARCHAR(20) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    gender VARCHAR(10) NOT NULL,
    id_address INT,
    PRIMARY KEY(id),
    FOREIGN KEY(id_address) REFERENCES addresses(id)
);

CREATE TABLE users (
  username varchar(50) NOT NULL,
  `password` varchar(68) NOT NULL,
  enabled tinyint NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  UNIQUE KEY authorities_idx_1 (username, authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
);

-- Sample patients/users
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Doctora Witolda Chodźki', '12', NULL, '20-546');
INSERT INTO patients VALUES(NULL, 'Zbigniew', 'Reszka', '54039763148', '+48321749870', 'zbigniew.reszka@gmail.com', 'MALE', 'zbigniew.reszka@gmail.com', 7);
INSERT INTO users VALUES('zbigniew.reszka@gmail.com', '{bcrypt}$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);
INSERT INTO authorities VALUES('zbigniew.reszka@gmail.com', 'PATIENT');

INSERT INTO addresses VALUES(NULL, 'Kraśnik', 'Józefa Piłsudskiego', '50', NULL, '13-340');
INSERT INTO patients VALUES(NULL, 'Anna', 'Kamińska', '84315964731', '+48931472530', 'anna.kaminska@gmail.com', 'FEMALE', 'anna.kaminska@gmail.com', 8);
INSERT INTO users VALUES('anna.kaminska@gmail.com', '{bcrypt}$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);
INSERT INTO authorities VALUES('anna.kaminska@gmail.com', 'PATIENT');

INSERT INTO addresses VALUES(NULL, 'Świdnik', 'Energetyków', '48', NULL, '19-621');
INSERT INTO patients VALUES(NULL, 'Paweł', 'Kowalski', '74123690158', '+48375964820', 'pawel.kowalski@gmail.com', 'MALE', 'pawel.kowalski@gmail.com', 9);
INSERT INTO users VALUES('pawel.kowalski@gmail.com', '{bcrypt}$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);
INSERT INTO authorities VALUES('pawel.kowalski@gmail.com', 'PATIENT');

CREATE TABLE doctors(
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  phone_number VARCHAR(20),
  email VARCHAR(100),
  position VARCHAR(50),
  id_department INT,
  id_address INT,
  PRIMARY KEY(id),
  FOREIGN KEY (id_department) REFERENCES departments(id),
  FOREIGN KEY (id_address) REFERENCES addresses(id)
);

-- Sample doctors
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Diamentowa', '67', '15A', '20-546');
INSERT INTO doctors VALUES(NULL, 'Marta', 'Stachyra', '+48321796584', 'marta.stachyra@gmail.com', 'HEAD_OF_DEPARTMENT', 1, 10);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Krochmalna', '74', '87', '20-981');
INSERT INTO doctors VALUES(NULL, 'Piotr', 'Raczkowski', '+48964753791', 'piotr.raczkowski@gmail.com', 'SPECIALIST', 1, 11);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Granitowa', '53', '66', '20-852');
INSERT INTO doctors VALUES(NULL, 'Michał', 'Gawroński', '+48746921068', 'michal.gawronski@gmail.com', 'SPECIALIST', 1, 12);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Wrotkowska', '4', '71', '20-648');
INSERT INTO doctors VALUES(NULL, 'Krystian', 'Michalski', '+48637546970', 'krystian.michalski@gmail.com', 'SPECIALIST', 1, 13);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Niecała', '32', '78', '20-466');
INSERT INTO doctors VALUES(NULL, 'Lech', 'Gocłowski', '+48863102589', 'lech.goclowski@gmail.com', 'HEAD_OF_DEPARTMENT', 2, 14);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Krochmalna', '389', '12', '20-664');
INSERT INTO doctors VALUES(NULL, 'Maria', 'Duda', '+48634785209', 'maria.duda@gmail.com', 'HEAD_OF_DEPARTMENT', 3, 15);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Warszawska', '34', '17', '20-678');
INSERT INTO doctors VALUES(NULL, 'Marta', 'Wiśniewska', '+48679254091', 'marta.wisniewska@gmail.com', 'HEAD_OF_DEPARTMENT', 4, 16);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Abramowicka', '62', '83', '20-799');
INSERT INTO doctors VALUES(NULL, 'Aleksandr', 'Dudkiewicz', '+48637492048', 'aleksandr.dudkiewicz@gmail.com', 'HEAD_OF_DEPARTMENT', 5, 17);
INSERT INTO addresses VALUES(NULL, 'Lublin', 'Adama Mickiewicza', '37', '39', '20-433');
INSERT INTO doctors VALUES(NULL, 'Magda', 'Gryniewicz', '+48697130572', 'marta.gryniewicz@gmail.com', 'HEAD_OF_DEPARTMENT', 6, 18);

DROP TABLE IF EXISTS appointments;
CREATE TABLE appointments(
	id INT NOT NULL AUTO_INCREMENT,
    appointment_time TIME NOT NULL,
    appointment_date DATE NOT NULL,
    id_department INT,
    id_patient INT,
    id_doctor INT,
    PRIMARY KEY(id),
    FOREIGN KEY (id_department) REFERENCES departments(id),
    FOREIGN KEY (id_patient) REFERENCES patients(id),
    FOREIGN KEY (id_doctor) REFERENCES doctors(id)
);