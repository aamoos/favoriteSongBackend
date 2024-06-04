insert into authority(authority_name) values ('ROLE_ADMIN');
insert into authority(authority_name) values ('ROLE_USER');
insert into users(activated, password, user_id, user_name, user_seq) values(true, '$2a$10$OJtaRJO5NDWAgAzZeGyfpuWGWxJ1BfnTwT0nmO9UNZ1O.IHPXfSfy', 'aamoos@naver.com', '김재성', '2');
insert into user_authority(user_seq, authority_name) values('2', 'ROLE_USER');
