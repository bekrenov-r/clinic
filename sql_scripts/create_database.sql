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
    department_name varchar(255) not null,
    specialization  varchar(255) not null,
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

drop table if exists doctors;
create table doctors
(
    id            int         not null auto_increment,
    first_name    varchar(50) not null,
    last_name     varchar(50) not null,
    phone_number  varchar(20),
    email         varchar(100),
    pesel varchar(11),
    occupation      varchar(50),
    id_department int,
    id_address    int,
    primary key (id),
    foreign key (id_department) references departments (id),
    foreign key (id_address) references addresses (id)
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
    foreign key (id_doctor) references doctors (id)
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
    ('Lublin', 'Lubartowska', '47', null, '20-890'),
    ('Lublin', 'Doctora Witolda Chodźki', '12', null, '20-546'),
    ('Świdnik', 'Energetyków', '48', null, '19-621'),
    ('Kraśnik', 'Józefa Piłsudskiego', '50', null, '13-340'),
    ('Lublin', 'Diamentowa', '67', '15A', '20-546'),
    ('Lublin', 'Krochmalna', '74', '87', '20-981'),
    ('Lublin', 'Granitowa', '53', '66', '20-852'),
    ('Lublin', 'Wrotkowska', '4', '71', '20-648'),
    ('Lublin', 'Niecała', '32', '78', '20-466'),
    ('Lublin', 'Krochmalna', '389', '12', '20-664'),
    ('Lublin', 'Warszawska', '34', '17', '20-678'),
    ('Lublin', 'Abramowicka', '62', '83', '20-799'),
    ('Lublin', 'Adama Mickiewicza', '37', '39', '20-433');

insert into departments(department_name, specialization, address_id)
values
    ('Oddział dermatologiczny №1', 'Dermatologia', 1),
    ('Oddział dermatologiczny №2', 'Dermatologia', 2),
    ('Oddział okulistyczny №1', 'Okulistyka', 3),
    ('Oddział okulistyczny №2', 'Okulistyka', 4),
    ('Oddział okulistyczny №3', 'Okulistyka', 5),
    ('Oddział psychologii №1', 'Psychologia', 6);

-- Sample patients/users

insert into patients(first_name, last_name, pesel, phone_number, email, gender, address_id)
values
    ('Zbigniew', 'Reszka', '54039763148', '+48321749870', 'zbigniew.reszka@gmail.com', 'MALE', 7),
    ('Anna', 'Kamińska', '84315964731', '+48931472530', 'anna.kaminska@gmail.com', 'FEMALE', 8),
    ('Paweł', 'Kowalski', '74123690158', '+48375964820', 'pawel.kowalski@gmail.com', 'MALE', 9);
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

-- Sample doctors
insert into doctors(first_name, last_name, phone_number, email, occupation, id_department, id_address)
values
    ('Marta', 'Stachyra', '+48321796584', 'marta.stachyra@gmail.com', 'HEAD_OF_DEPARTMENT', 1, 10),
    ('Piotr', 'Raczkowski', '+48964753791', 'piotr.raczkowski@gmail.com', 'SPECIALIST', 1, 11),
    ('Michał', 'Gawroński', '+48746921068', 'michal.gawronski@gmail.com', 'SPECIALIST', 1, 12),
    ('Krystian', 'Michalski', '+48637546970', 'krystian.michalski@gmail.com', 'SPECIALIST', 1, 13),
    ('Lech', 'Gocłowski', '+48863102589', 'lech.goclowski@gmail.com', 'HEAD_OF_DEPARTMENT', 2, 14),
    ('Maria', 'Duda', '+48634785209', 'maria.duda@gmail.com', 'HEAD_OF_DEPARTMENT', 3, 15),
    ('Marta', 'Wiśniewska', '+48679254091', 'marta.wisniewska@gmail.com', 'HEAD_OF_DEPARTMENT', 4, 16),
    ('Aleksandr', 'Dudkiewicz', '+48637492048', 'aleksandr.dudkiewicz@gmail.com', 'HEAD_OF_DEPARTMENT', 5, 17),
    ('Magda', 'Gryniewicz', '+48697130572', 'marta.gryniewicz@gmail.com', 'HEAD_OF_DEPARTMENT', 6, 18);
