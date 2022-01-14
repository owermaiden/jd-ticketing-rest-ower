insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
VALUES ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Admin'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Manager'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Employee');
insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, enabled,
                  first_name, gender, last_name, user_name, role_id,pass_word)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, true, 'admin', 'MALE', 'admin', 'admin@admin.com',
        1,'$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba'),
       ('2021-01-05 00:00:00', 2, false, '2021-01-05 00:00:00', 1, true, 'omer', 'MALE', 'erden', 'omererden18@gmail.com',
        2,'$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK');
insert into projects(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, end_date, project_code, project_detail, project_name, project_status, start_date, manager_id)
values ('2021-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,'2021-01-05','10001','Something','Spring','COMPLETE','2021-01-05',1);
insert into tasks(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, assigned_date, task_detail, task_status, task_subject, user_id, project_id)
values ('2021-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,'2021-01-05','Something','COMPLETE','Something',1,1);
