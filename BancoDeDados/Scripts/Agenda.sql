create database dbagenda;
show databases;
use dbagenda;
create table contatos(
idcon int primary key auto_increment,
nome varchar(50) not null,
fone varchar(15) not null,
email varchar(50)
);
/* CRUD CREATE*/
insert into contatos (nome, fone, email) values('Diego', 11947168855, 'dsena@7.com');

/* CRUD READ*/
select * from contatos;
select * from contatos order by nome;
select * from contatos where idcon = 1;

/*CRUD UPDATE*/
update contatos set nome = 'Jeffsu' where idcon = 5;
update contatos set nome = 'Jeffsu Ximenes', fone = 1196358484, email = 'jefsu@magna.com.br' where idcon = 5;

/*CRUD DELETE*/