
drop table metge_especialitat;
drop table agenda_metges; 
drop table cita;
drop table especialitat;
drop table metge;
drop table persona;

CREATE TABLE persona (
  per_codi INT(3) AUTO_INCREMENT,
  per_nif VARCHAR(9) NOT NULL,
  per_nom VARCHAR(12) NOT NULL,
  per_cognom1 VARCHAR(20) NOT NULL,
  per_cognom2 VARCHAR(20),
  per_adreca VARCHAR(20) NOT NULL,
  per_poblacio VARCHAR(15) NOT NULL,
  per_sexe INT(1) NOT NULL CHECK(sexe = 0 OR sexe = 1),
  per_login VARCHAR(10),
  per_passw VARCHAR(10),
  per_es_metge BOOLEAN NOT NULL,
  CONSTRAINT pk_persona PRIMARY KEY (per_codi),
  CONSTRAINT ck_nif_len CHECK (LENGTH(per_nif) = 9),
  CONSTRAINT uq_nif UNIQUE (per_nif),
  CONSTRAINT uq_login UNIQUE (per_login)
);


CREATE TABLE metge (
  met_codi INT(3),
  met_codi_empleat INT(3) NOT NULL,
  CONSTRAINT pk_empleat PRIMARY KEY (met_codi),
  CONSTRAINT fk_empleat_persona FOREIGN KEY (met_codi) REFERENCES persona(per_codi)
);


CREATE TABLE especialitat (
  esp_codi INT(3) AUTO_INCREMENT,
  esp_nom VARCHAR(30) NOT NULL,
  CONSTRAINT pk_especialitat PRIMARY KEY (esp_codi)
);


CREATE TABLE cita (
  cit_codi INT(3) AUTO_INCREMENT,
  cit_data_hora DATETIME NOT NULL,
  cit_codi_persona INT NOT NULL,
  cit_codi_empleat INT NOT NULL,
  cit_codi_esp INT NOT NULL,
  cit_informe TEXT,
  cit_es_oberta BOOLEAN NOT NULL DEFAULT true,
  CONSTRAINT pk_codi_cita PRIMARY KEY (cit_codi),
  CONSTRAINT fk_cita_persona FOREIGN KEY (cit_codi_persona) REFERENCES persona(per_codi),
  CONSTRAINT fk_cita_metge FOREIGN KEY (cit_codi_empleat) REFERENCES metge(met_codi),
  CONSTRAINT fk_cita_especialitat FOREIGN KEY (cit_codi_esp) REFERENCES especialitat(esp_codi),
  CONSTRAINT check_metge_pacient CHECK (cit_codi_empleat <> cit_codi_persona)
);



CREATE TABLE metge_especialitat (
  me_met_codi INT,
  me_esp_codi INT,
  CONSTRAINT pk_metge_especialitat PRIMARY KEY (me_met_codi, me_esp_codi),
  CONSTRAINT fk_me_medico FOREIGN KEY (me_met_codi) REFERENCES metge(met_codi),
  CONSTRAINT fk_me_especialidad FOREIGN KEY (me_esp_codi) REFERENCES especialitat(esp_codi)
);

CREATE TABLE agenda_metges(
  am_me_codi INT ,
  am_dia_setmana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
  am_hora TIME,
  am_esp_codi int(3) NOT NULL,
  CONSTRAINT pk_entrada_horari PRIMARY KEY (am_me_codi, am_dia_setmana, am_hora),
  CONSTRAINT fk_eh_metge FOREIGN KEY (am_me_codi) REFERENCES metge(met_codi),
  CONSTRAINT fk_eh_especialitat FOREIGN KEY (am_esp_codi) REFERENCES especialitat(esp_codi),
  CONSTRAINT check_hora_fragments CHECK (MINUTE(am_hora) IN (0,30)),
  CONSTRAINT check_hora_franja CHECK (HOUR(am_hora) >= 7 AND HOUR(am_hora) <= 21)
);

INSERT INTO especialitat (esp_codi, esp_nom)
VALUES
  (1, 'Cardiología'),
  (2, 'Dermatología'),
  (3, 'Endocrinología'),
  (4, 'Gastroenterología'),
  (5, 'Hematología'),
  (6, 'Neurología'),
  (7, 'Oncología'),
  (8, 'Oftalmología'),
  (9, 'Pediatría'),
  (10, 'Psiquiatría');

  --Script datos demo

  -- Añadir datos a la tabla persona
INSERT INTO persona (per_nif, per_nom, per_cognom1, per_cognom2, per_adreca, per_poblacio, per_sexe, per_login, per_passw, per_es_metge)
VALUES ('12345678A', 'Juan', 'García', 'Martínez', 'Calle Mayor 1', 'Madrid', 1, 'juangar', 'pass123', false),
       ('23456789B', 'María', 'Pérez', 'Gómez', 'Calle Alcalá 2', 'Madrid', 0, 'mariap', 'pass456', false),
       ('34567890C', 'Pedro', 'Sánchez', 'González', 'Calle Gran Vía 3', 'Madrid', 1, 'pedrosan', 'pass789', false),
       ('45678901D', 'Ana', 'Fernández', 'Rodríguez', 'Calle Mayor 4', 'Madrid', 0, 'anafer', 'pass012', true),
       ('56789012E', 'David', 'López', 'Sánchez', 'Calle Alcalá 5', 'Madrid', 1, 'davidlop', 'pass345', true),
       ('67890123F', 'Cristina', 'González', 'Martínez', 'Calle Gran Vía 6', 'Madrid', 0, 'cristig', 'pass678', false),
       ('78901234G', 'José', 'Sánchez', 'Gómez', 'Calle Mayor 7', 'Madrid', 1, 'josesan', 'pass901', true),
       ('89012345H', 'Sara', 'Rodríguez', 'Fernández', 'Calle Alcalá 8', 'Madrid', 0, 'sararod', 'pass234', true),
       ('90123456I', 'Javier', 'Martínez', 'García', 'Calle Gran Vía 9', 'Madrid', 1, 'javiermar', 'pass567', false),
       ('01234567J', 'Lucía', 'Gómez', 'Sánchez', 'Calle Mayor 10', 'Madrid', 0, 'luciag', 'pass890', true);

-- Añadir datos a la tabla metge
INSERT INTO metge (met_codi, met_codi_empleat)
VALUES (4, 4),
       (5, 5),
       (7, 7),
       (8, 9),
       (10, 10);


-- Añadir datos a la tabla especialitat
INSERT INTO especialitat (esp_nom) VALUES
('Cardiología'),
('Traumatología'),
('Pediatría'),
('Oncología'),
('Neurología'),
('Endocrinología'),
('Oftalmología'),
('Ginecología'),
('Urología'),
('Dermatología');

INSERT INTO cita (cit_data_hora, cit_codi_persona, cit_codi_empleat, cit_codi_esp, cit_informe, cit_es_oberta) VALUES
('2023-05-15 10:00:00', 2, 5, 1, 'Dolor de cabeza', true),
('2023-05-16 11:00:00', 1, 7, 1, 'Dolor de estómago', true),
('2023-05-16 14:00:00', 3, 10, 3, 'Revisión anual', true),
('2023-05-17 15:00:00', 4, 4, 2, 'Dolor de espalda', true),
('2023-05-17 16:00:00', 5, 7, 1, 'Gripe', true),
('2023-05-18 12:00:00', 2, 10, 2, 'Dolor de cuello', true),
('2023-05-19 09:00:00', 1, 5, 3, 'Dolor de oído', true),
('2023-05-19 10:00:00', 3, 4, 4, 'Revisión anual', true),
('2023-05-20 11:00:00', 4, 10, 2, 'Dolor de rodilla', true),
('2023-05-20 14:00:00', 5, 5, 3, 'Fiebre', true),
('2023-05-21 15:00:00', 6, 8, 1, 'Dolor de garganta', true),
('2023-05-21 16:00:00', 7, 7, 2, 'Dolor de cabeza', true),
('2023-05-22 12:00:00', 8, 5, 1, 'Dolor de estómago', true),
('2023-05-23 09:00:00', 9, 10, 3, 'Revisión anual', true),
('2023-05-23 10:00:00', 10, 4, 4, 'Dolor de espalda', true),
('2023-05-24 11:00:00', 1, 7, 2, 'Gripe', true),
('2023-05-24 14:00:00', 2, 8, 3, 'Dolor de cuello', true),
('2023-05-25 15:00:00', 3, 4, 4, 'Dolor de cabeza', true),
('2023-05-25 16:00:00', 4, 5, 1, 'Dolor de espalda', true),
('2023-05-26 12:00:00', 5, 10, 2, 'Dolor de cuello', true),
('2023-05-27 09:00:00', 6, 5, 3, 'Fiebre', true),
('2023-05-27 10:00:00', 7, 8, 1, 'Dolor de garganta', true),
('2023-05-12 08:30:00', 4, 7, 9, 'Checkup rutinario', true),
('2023-05-12 09:00:00', 2, 5, 3, 'Dolor de espalda', true),
('2023-05-12 10:30:00', 1, 10, 4, 'Problemas de sueño', true),
('2023-05-12 11:30:00', 8, 8, 7, 'Dolor de cabeza', true),
('2023-05-12 12:00:00', 9, 4, 1, 'Mareos', true),
('2023-05-12 13:30:00', 6, 10, 2, 'Problemas digestivos', true),
('2023-05-12 14:30:00', 1, 7, 6, 'Revisión de alergias', true),
('2023-05-12 15:30:00', 3, 5, 10, 'Infección de garganta', true),
('2023-05-12 16:00:00', 10, 4, 1, 'Fiebre', true),
('2023-05-12 17:00:00', 4, 7, 9, 'Dolor de oído', true),
('2023-05-12 18:30:00', 7, 8, 7, 'Fatiga', true),
('2023-05-12 19:00:00', 1, 5, 4, 'Problemas de visión', true),
('2023-05-12 20:00:00', 2, 10, 6, 'Dolor de cuello', true),
('2023-05-12 21:00:00', 6, 8, 2, 'Problemas respiratorios', true),
('2023-05-13 08:30:00', 9, 7, 5, 'Checkup rutinario', true),
('2023-05-13 09:00:00', 2, 5, 3, 'Dolor de espalda', true),
('2023-05-13 10:30:00', 1, 10, 4, 'Problemas de sueño', true),
('2023-05-13 11:30:00', 8, 8, 7, 'Dolor de cabeza', true),
('2023-05-13 12:00:00', 9, 4, 1, 'Mareos', true),
('2023-05-13 13:30:00', 6, 10, 2, 'Problemas digestivos', true),
('2023-05-13 14:30:00', 1, 7, 6, 'Revisión de alergias', true);

INSERT INTO metge_especialitat (me_met_codi, me_esp_codi) VALUES
(4, 1),
(4, 2),
(4, 5),
(5, 2),
(5, 3),
(5, 4),
(5, 8),
(7, 1),
(7, 3),
(7, 5),
(7, 6),
(8, 1),
(8, 3),
(8, 5),
(10, 2),
(10, 4),
(10, 6),
(10, 7),
(10, 8),
(10, 9);

INSERT INTO agenda_metges (am_me_codi, am_dia_setmana, am_hora, am_esp_codi) VALUES
(4, 'LUNES', '09:00:00', 1),
(4, 'LUNES', '09:30:00', 1),
(4, 'LUNES', '10:00:00', 2),
(4, 'LUNES', '10:30:00', 2),
(4, 'MARTES', '09:00:00', 3),
(4, 'MARTES', '09:30:00', 3),
(4, 'MARTES', '10:00:00', 4),
(4, 'MARTES', '10:30:00', 4),
(5, 'MIERCOLES', '11:00:00', 5),
(5, 'MIERCOLES', '11:30:00', 5),
(5, 'MIERCOLES', '12:00:00', 6),
(5, 'MIERCOLES', '12:30:00', 6),
(5, 'JUEVES', '11:00:00', 7),
(5, 'JUEVES', '11:30:00', 7),
(5, 'JUEVES', '12:00:00', 8),
(5, 'JUEVES', '12:30:00', 8),
(7, 'VIERNES', '16:00:00', 9),
(7, 'VIERNES', '16:30:00', 9),
(7, 'VIERNES', '17:00:00', 10),
(7, 'VIERNES', '17:30:00', 10),
(8, 'SABADO', '14:00:00', 1),
(8, 'SABADO', '14:30:00', 1),
(8, 'SABADO', '15:00:00', 2),
(8, 'SABADO', '15:30:00', 2),
(10, 'DOMINGO', '18:00:00', 3),
(10, 'DOMINGO', '18:30:00', 3),
(10, 'DOMINGO', '19:00:00', 4),
(10, 'DOMINGO', '19:30:00', 4);




