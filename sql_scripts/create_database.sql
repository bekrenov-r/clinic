drop schema if exists clinic;
create schema clinic;
use clinic;

drop table if exists addresses;
create table addresses
(
    id              int          not null auto_increment,
    city            varchar(255) not null,
    street          varchar(255) not null,
    building_number varchar(10)  not null,
    flat_number     varchar(10),
    zip_code     varchar(10)  not null,
    primary key (id)
);

drop table if exists departments;
create table departments
(
    id              int          not null auto_increment,
    name varchar(255) not null,
    specialization  varchar(255) not null,
    auto_confirm_appointment boolean not null,
    address_id      int          not null,
    primary key (id),
    foreign key (address_id) references addresses (id)
);

drop table if exists patients;
create table patients
(
    id           int          not null auto_increment,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    pesel        varchar(11)  not null,
    phone_number varchar(20)  default null,
    email        varchar(255) default null,
    gender       varchar(10)  not null,
    address_id   int,
    primary key (id),
    foreign key (address_id) references addresses (id)
);

drop table if exists users;
create table users
(
    username   varchar(50) not null,
    `password` varchar(68) not null,
    enabled    tinyint     not null,
    primary key (username)
);

drop table if exists authorities;
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    unique key authorities_idx_1 (username, authority),
    constraint authorities_ibfk_1 foreign key (username) references users (username)
);

drop table if exists employees;
create table employees
(
    employee_type varchar(100),
    id            int         not null auto_increment,
    first_name    varchar(50),
    last_name     varchar(50),
    phone_number  varchar(20),
    email         varchar(100),
    pesel varchar(11),
    occupation      varchar(50),
    id_department int,
    address_id    int,
    primary key (id),
    foreign key (id_department) references departments (id),
    foreign key (address_id) references addresses (id)
);

drop table if exists appointments;
create table appointments
(
    id               int  not null auto_increment,
    appointment_time time not null,
    appointment_date date not null,
    status text not null,
    prescription text,
    details text,
    id_department    int,
    id_patient       int,
    id_doctor        int,
    primary key (id),
    foreign key (id_department) references departments (id),
    foreign key (id_patient) references patients (id),
    foreign key (id_doctor) references employees (id)
);

drop table if exists activation_tokens;
create table activation_tokens(
    id int not null auto_increment,
    token varchar(20),
    username varchar(50),
    primary key (id),
    foreign key (username) references users(username)
);

-- Sample departments
insert into addresses(city, street, building_number, flat_number, zip_code)
values
    ('Lublin', 'Weteranów', '17', null, '20-281'),
    ('Lublin', 'Doctora Witolda Chodźki', '2', null, '20-479'),
    ('Lublin', 'Metalurgiczna', '71', null, '20-831'),
    ('Lublin', 'Fabryczna', '16', null, '20-642'),
    ('Lublin', 'Ignacego Radziszewskiego', '4', null, '20-188'),
    ('Lublin', 'Lubartowska', '47', null, '20-890');

insert into departments(name, specialization, auto_confirm_appointment, address_id)
values
    ('Oddział dermatologiczny №1', 'DERMATOLOGY', false, 1),
    ('Oddział dermatologiczny №2', 'DERMATOLOGY', true, 2),
    ('Oddział okulistyczny №1', 'OPHTHALMOLOGY', true, 3),
    ('Oddział okulistyczny №2', 'OPHTHALMOLOGY', false, 4),
    ('Oddział okulistyczny №3', 'OPHTHALMOLOGY', false, 5),
    ('Oddział psychologii №1', 'PSYCHOLOGY', false, 6);

-- Sample patients/users
insert into addresses(city, street, building_number, flat_number, zip_code)
values
    ('Lublin', 'Doctora Witolda Chodźki', '12', null, '20-546'),
    ('Świdnik', 'Energetyków', '48', null, '19-621'),
    ('Kraśnik', 'Józefa Piłsudskiego', '50', null, '13-340');

insert into patients(first_name, last_name, pesel, phone_number, email, gender, address_id)
values
    ('Zbigniew', 'Reszka', '82081957619', '321749870', 'zbigniew.reszka@gmail.com', 'MALE', 7),
    ('Anna', 'Kamińska', '75110179764', '931472530', 'anna.kaminska@gmail.com', 'FEMALE', 8),
    ('Paweł', 'Kowalski', '96071957936', '375964820', 'pawel.kowalski@gmail.com', 'MALE', 9);
insert into users
values
    ('zbigniew.reszka@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('anna.kaminska@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('pawel.kowalski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);

insert into authorities
values
    ('zbigniew.reszka@gmail.com', 'PATIENT'),
    ('anna.kaminska@gmail.com', 'PATIENT'),
    ('pawel.kowalski@gmail.com', 'PATIENT');

-- Sample doctors/users
insert into addresses(city, street, building_number, flat_number, zip_code)
values
    ('Lublin', 'Diamentowa', '67', '15A', '20-546'),
    ('Lublin', 'Krochmalna', '74', '87', '20-981'),
    ('Lublin', 'Granitowa', '53', '66', '20-852'),
    ('Lublin', 'Wrotkowska', '4', '71', '20-648'),
    ('Lublin', 'Niecała', '32', '78', '20-466'),
    ('Lublin', 'Krochmalna', '389', '12', '20-664'),
    ('Lublin', 'Warszawska', '34', '17', '20-678'),
    ('Lublin', 'Abramowicka', '62', '83', '20-799'),
    ('Lublin', 'Adama Mickiewicza', '37', '39', '20-433'),
    ('Lublin', 'Kraśnicka', '123A', NULL, '20-123'),
    ('Lublin', 'Nałęczowska', '456B', NULL, '20-456'),
    ('Lublin', 'Zana', '789C', NULL, '20-789'),
    ('Lublin', 'Lipowa', '321D', NULL, '20-321'),
    ('Lublin', 'Głęboka', '654E', NULL, '20-654'),
    ('Lublin', 'Peowiaków', '987F', NULL, '20-987');;

INSERT INTO employees(employee_type, first_name, last_name, phone_number, email, pesel, occupation, id_department, address_id)
VALUES
    ('DOCTOR', 'Marta', 'Stachyra', '321796584', 'marta.stachyra@gmail.com', '66020735162', 'HEAD_OF_DEPARTMENT', 1, 10),
    ('DOCTOR', 'Piotr', 'Raczkowski', '964753791', 'piotr.raczkowski@gmail.com', '97030218817', 'SPECIALIST', 1, 11),
    ('DOCTOR', 'Michał', 'Gawroński', '746921068', 'michal.gawronski@gmail.com', '87092853552', 'SPECIALIST', 1, 12),
    ('DOCTOR', 'Krystian', 'Michalski', '637546970', 'krystian.michalski@gmail.com', '51090788675', 'SPECIALIST', 1, 13),
    ('DOCTOR', 'Lech', 'Gocłowski', '863102589', 'lech.goclowski@gmail.com', '89040453999', 'HEAD_OF_DEPARTMENT', 2, 14),
    ('DOCTOR', 'Maria', 'Duda', '634785209', 'maria.duda@gmail.com', '88080769149', 'HEAD_OF_DEPARTMENT', 3, 15),
    ('DOCTOR', 'Marta', 'Wiśniewska', '679254091', 'marta.wisniewska@gmail.com', '60082661681', 'HEAD_OF_DEPARTMENT', 4, 16),
    ('DOCTOR', 'Aleksandr', 'Dudkiewicz', '637492048', 'aleksandr.dudkiewicz@gmail.com', '92092437839', 'HEAD_OF_DEPARTMENT', 5, 17),
    ('RECEPTIONIST', 'Zuzanna', 'Majchrzak', '123456789', 'zuzanna.majchrzak@gmail.com', '84091665966', 'RECEPTIONIST', 1, 18),
    ('RECEPTIONIST', 'Wiktoria', 'Szymańska', '234567890', 'wiktoria.szymanska@gmail.com', '77073069461', 'RECEPTIONIST', 2, 19),
    ('RECEPTIONIST', 'Gabriela', 'Woźniak', '345678901', 'gabriela.wozniak@gmail.com', '03312036226', 'RECEPTIONIST', 3, 20),
    ('RECEPTIONIST', 'Natalia', 'Zawadzka', '456789012', 'natalia.zawadzka@gmail.com', '87122435761', 'RECEPTIONIST', 4, 21),
    ('RECEPTIONIST', 'Aleksandra', 'Kaczmarek', '567890123', 'aleksandra.kaczmarek@gmail.com', '57062166566', 'RECEPTIONIST', 5, 22),
    ('RECEPTIONIST', 'Patrycja', 'Kowalczyk', '678901234', 'patrycja.kowalczyk@gmail.com', '60111349665', 'RECEPTIONIST', 6, 23);

insert into users
values
    ('marta.stachyra@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('piotr.raczkowski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('michal.gawronski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('krystian.michalski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('lech.goclowski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('maria.duda@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('marta.wisniewska@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('aleksandr.dudkiewicz@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('marta.gryniewicz@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);

insert into authorities
values
    ('marta.stachyra@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('marta.stachyra@gmail.com', 'DOCTOR'),
    ('piotr.raczkowski@gmail.com', 'DOCTOR'),
    ('michal.gawronski@gmail.com', 'DOCTOR'),
    ('krystian.michalski@gmail.com', 'DOCTOR'),
    ('lech.goclowski@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('lech.goclowski@gmail.com', 'DOCTOR'),
    ('maria.duda@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('maria.duda@gmail.com', 'DOCTOR'),
    ('marta.wisniewska@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('marta.wisniewska@gmail.com', 'DOCTOR'),
    ('aleksandr.dudkiewicz@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('aleksandr.dudkiewicz@gmail.com', 'DOCTOR'),
    ('marta.gryniewicz@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('marta.gryniewicz@gmail.com', 'DOCTOR');

-- Sample appointments
insert into appointments(appointment_time, appointment_date, status, prescription, details, id_department, id_patient, id_doctor)
values
    ('12:00', curdate(), 'CONFIRMED', null, null, 1, 2, 2),
    ('10:15', curdate() + interval 1 day, 'CONFIRMED', null, null, 1, 1, 1),
    ('08:45', curdate() + interval 2 day, 'CONFIRMED', null, null, 1, 1, 2),
    ('14:15', curdate(), 'CONFIRMED', null, null, 1, 2, 3),
    ('15:30', curdate() + interval 7 day, 'CONFIRMED', null, null, 1, 3, 3),
    ('12:45', curdate() + interval 12 day, 'CONFIRMED', null, null, 1, 1, 4),
    ('11:00', curdate() + interval 3 day, 'CONFIRMED', null, null, 1, 2, 2),
    ('11:45', curdate(), 'CONFIRMED', null, null, 1, 3, 1),
    ('08:00', curdate() + interval 5 day, 'CONFIRMED', null, null, 1, 1, 3),
    ('09:30', curdate() + interval 17 day, 'CONFIRMED', null, null, 1, 2, 4),
    ('13:45', curdate() + interval 9 day, 'CONFIRMED', null, null, 1, 3, 2),
    ('12:00', curdate() + interval 13 day, 'CONFIRMED', null, null, 1, 3, 2),
    ('14:30', curdate() + interval 18 day, 'CONFIRMED', null, null, 1, 1, 2),
    ('12:00', curdate() + interval 4 day, 'CONFIRMED', null, null, 1, 2, 2),
    ('09:30', curdate() + interval 10 day, 'CONFIRMED', null, null, 1, 1, 2),
    ('10:15', curdate() + interval 3 day, 'CONFIRMED', null, null, 1, 2, 2),
    ('12:00', curdate() + interval 8 day, 'CONFIRMED', null, null, 1, 3, 2),
    ('08:45', curdate() + interval 9 day, 'CONFIRMED', null, null, 1, 1, 2),
    ('14:15', curdate() + interval 1 day, 'CONFIRMED', null, null, 1, 3, 2),
    ('14:30', curdate() + interval 2 day, 'CONFIRMED', null, null, 1, 2, 2),
    ('08:00', '2024-05-15', 'FINISHED', null, null, 1, 1, 2),
    ('12:45', '2024-03-10', 'FINISHED', null, null, 1, 2, 3),
    ('13:30', '2024-07-05', 'FINISHED', null, null, 1, 3, 1),
    ('15:45', '2024-09-20', 'FINISHED', null, null, 1, 1, 4),
    ('09:15', '2024-11-25', 'FINISHED', null, null, 1, 2, 2),
    ('11:30', '2024-04-30', 'FINISHED', null, null, 1, 3, 3),
    ('14:00', '2024-08-04', 'FINISHED', null, null, 1, 1, 1),
    ('10:45', '2024-12-09', 'FINISHED', null, null, 1, 2, 4),
    ('16:00', '2024-06-14', 'FINISHED', null, null, 1, 3, 2),
    ('08:30', '2024-10-19', 'FINISHED', null, null, 1, 1, 3);

-- Admin account
insert into users values('jan.kowalski@example.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);
insert into authorities values('jan.kowalski@example.com', 'ADMIN');
