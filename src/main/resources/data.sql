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
    constraint authorities_ibfk_1 foreign key (username) references users (username)
);

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
    ('Kraśnik', 'Józefa Piłsudskiego', '50', null, '13-340');

-- Sample departments
insert into departments(name, specialization, auto_confirm_appointment, address_id)
values
    ('Oddział dermatologiczny №1', 'DERMATOLOGY', false, 1),
    ('Oddział dermatologiczny №2', 'DERMATOLOGY', true, 2),
    ('Oddział okulistyczny №1', 'OPHTHALMOLOGY', true, 3),
    ('Oddział okulistyczny №2', 'OPHTHALMOLOGY', false, 4),
    ('Oddział okulistyczny №3', 'OPHTHALMOLOGY', false, 5),
    ('Oddział psychologii №1', 'PSYCHOLOGY', false, 6);

-- Sample doctors/users
insert into doctors(first_name, last_name, phone_number, email, pesel, occupation, id_department, id_address)
values
    ('Marta', 'Stachyra', '321796584', 'marta.stachyra@gmail.com', '89122468687', 'HEAD_OF_DEPARTMENT', 1, 7),
    ('Piotr', 'Raczkowski', '964753791', 'piotr.raczkowski@gmail.com', '97030218817', 'SPECIALIST', 1, 8);

insert into users
values
    ('marta.stachyra@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('piotr.raczkowski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);

insert into authorities
values
    ('marta.stachyra@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('marta.stachyra@gmail.com', 'DOCTOR'),
    ('piotr.raczkowski@gmail.com', 'DOCTOR');

-- Sample patients/users
insert into patients(first_name, last_name, pesel, phone_number, email, gender, address_id)
values
    ('Zbigniew', 'Reszka', '82081957619', '321749870', 'zbigniew.reszka@gmail.com', 'MALE', 9);

insert into users
values
    ('zbigniew.reszka@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);

insert into authorities
values
    ('zbigniew.reszka@gmail.com', 'PATIENT');

-- Admin account
insert into users values('jan.kowalski@example.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);
insert into authorities values('jan.kowalski@example.com', 'ADMIN');