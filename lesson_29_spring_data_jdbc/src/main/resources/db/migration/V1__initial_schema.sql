-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table web_server_clients
(
    id   bigint not null primary key,
    name varchar(50),
    login varchar(50) not null,
    pwd varchar(50) not null
);
