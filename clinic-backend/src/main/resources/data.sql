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

drop table if exists holidays;
create table holidays(
    holiday_date date
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
    ('Kraśnik', 'Józefa Piłsudskiego', '50', null, '13-340'),
    ('Lublin', 'Diamentowa', '67', '15A', '20-546'),
    ('Lublin', 'Krochmalna', '74', '87', '20-981');

-- Sample departments
insert into departments(name, specialization, auto_confirm_appointment, address_id)
values
    ('Oddział dermatologiczny №1', 'DERMATOLOGY', false, 1),
    ('Oddział dermatologiczny №2', 'DERMATOLOGY', true, 2),
    ('Oddział okulistyczny №1', 'OPHTHALMOLOGY', true, 3),
    ('Oddział okulistyczny №2', 'OPHTHALMOLOGY', false, 4),
    ('Oddział okulistyczny №3', 'OPHTHALMOLOGY', false, 5),
    ('Oddział psychologii №1', 'PSYCHOLOGY', false, 6);

-- Sample employees/users
INSERT INTO employees(employee_type, first_name, last_name, phone_number, email, pesel, occupation, id_department, address_id)
VALUES
    ('DOCTOR', 'Marta', 'Stachyra', '321796584', 'marta.stachyra@gmail.com', '66020735162', 'HEAD_OF_DEPARTMENT', 1, 7),
    ('DOCTOR', 'Piotr', 'Raczkowski', '964753791', 'piotr.raczkowski@gmail.com', '97030218817', 'SPECIALIST', 1, 8),
    ('DOCTOR', 'Lech', 'Gocłowski', '863102589', 'lech.goclowski@gmail.com', '89040453999', 'HEAD_OF_DEPARTMENT', 2, 11),
    ('RECEPTIONIST', 'Zuzanna', 'Majchrzak', '123456789', 'zuzanna.majchrzak@gmail.com', '84091665966', 'RECEPTIONIST', 1, 10);

insert into users
values
    ('marta.stachyra@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('piotr.raczkowski@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1),
    ('zuzanna.majchrzak@gmail.com', '$2a$12$eZMj.VH/CNTunH6Q9TE/M.Ryr5svpD.3xdUwdZ6bKc9NrhUMxtO2C', 1);

insert into authorities
values
    ('marta.stachyra@gmail.com', 'HEAD_OF_DEPARTMENT'),
    ('marta.stachyra@gmail.com', 'DOCTOR'),
    ('piotr.raczkowski@gmail.com', 'DOCTOR'),
    ('zuzanna.majchrzak@gmail.com', 'RECEPTIONIST');

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

insert into holidays values('2024-01-01'), ('2024-01-06'), ('2024-03-31'), ('2024-04-01'), ('2024-05-01'), ('2024-05-03'), ('2024-05-19'), ('2024-05-30'), ('2024-08-15'), ('2024-11-01'), ('2024-11-11'), ('2024-12-25'), ('2024-12-26');