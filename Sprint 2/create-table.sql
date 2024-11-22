create table user(
	id int primary key auto_increment not null,
	email varchar(100) unique not null,
	full_name varchar(50) not null,
	password varchar(255) not null,
	rfid_uid varchar(16),
	rol varchar(10) not null,
	check (rol in ('student', 'teacher', 'admin'))
);

create table groups (
	name varchar(10) primary key not null
);

create table room (
	code varchar(10) primary key not null
);

create table gruped_users (
	user_id int not null,
	group_name varchar(10) not null,
	primary key (user_id, group_name),
	foreign key (user_id) references user(id),
	foreign key (group_name) references groups(name)
);


CREATE TABLE schedule (
    id INT PRIMARY KEY AUTO_INCREMENT,
    manager_id INT NOT NULL,             -- Referencia al usuario
    group_name varchar(10) NOT NULL,             -- Referencia al grupo
    room_code varchar(10) NOT NULL,      -- Referencia a la sala
    week_day ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday') NOT NULL,
    start_time TIME NOT NULL,         	 -- Hora de inicio
    end_time TIME NOT NULL,          	 -- Hora de finalización
    FOREIGN KEY (manager_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (group_name) REFERENCES groups(name) ON DELETE CASCADE,
    FOREIGN KEY (room_code) REFERENCES room(code) ON DELETE CASCADE,
    UNIQUE(manager_id, group_name, room_code, week_day, start_time),
    UNIQUE(manager_id, group_name, room_code, week_day, end_time),
    check (end_time > start_time)
);

create table checkin (
  id bigint primary key auto_increment,
  user_id int not null,
  room_code varchar(10) not null,
  checkin_time datetime not null,
  foreign key (user_id) references user(id),
  foreign key (room_code) references room(code),
  unique (user_id, room_code, checkin_time)
);

create table attendance (
	id int primary key auto_increment,
	user_id int not null,
	check_time datetime not null,
	schedule_id int not null,
	type enum('attended', 'late', 'missed', 'justified'),
	foreign key (user_id) references user(id),
	foreign key (schedule_id) references schedule(id),
	unique (user_id, check_time)
);








