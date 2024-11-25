-- usuarios
insert into user (email, FULL_NAME, PASSWORD, RFID_UID, rol) 
values 
	("john.doe@example.com", "John Doe", "password123", "aaaaaaa", "teacher"),
	("jane.smith@example.com", "Jane Smith", "password456", "sssssss", "student");
	
-- rooms
insert into room (code) 
values
	("Room101"),
	("Room102";

-- groups
insert into groups (name)
values
	("GroupA"),
	("GroupB");

-- groups > user
insert into gruped_users (user_id, group_name)
values
	(1, "GroupA"),
	(2, "GroupB");

-- checkins
insert into checkin (user_id, room_code, checkin_time)
values
  (1, 'Room101', '2023-10-23 08:15:00'),
  (1, 'Room101', '2023-10-23 09:15:00'),
  (2, 'Room102', '2023-10-23 10:15:00'),
  (2, 'Room102', '2023-10-23 11:15:00');
  	
-- horarios (schedules)
insert into
  schedule (
    manager_id,
    group_name,
    room_code,
    week_day,
    start_time,
    end_time
  )
values
  (
    2,
    'GroupA',
    'Room101',
    'Monday',
    '08:00:00',
    '09:00:00'
  ),
  (
    2,
    'GroupA',
    'Room101',
    'Monday',
    '09:00:00',
    '10:00:00'
  ),
  (
    1,
    'GroupB',
    'Room102',
    'Tuesday',
    '10:00:00',
    '11:00:00'
  ),
  (
    1,
    'GroupB',
    'Room102',
    'Tuesday',
    '11:00:00',
    '12:00:00'
  );
  
 
 -- Attendance
insert into attendance (user_id, check_time, schedule_id, type)
values
  (1, '2023-10-23 08:15:00', 1, 'attended'),
  (1, '2023-10-23 09:15:00', 2, 'late'),
  (2, '2023-10-23 10:15:00', 3, 'attended'),
  (2, '2023-10-23 11:15:00', 4, 'justified');
  
 