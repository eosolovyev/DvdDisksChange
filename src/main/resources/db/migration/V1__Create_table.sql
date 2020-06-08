drop table if exists user;
drop table if exists user_role;
create table user (
    id bigint not null auto_increment,
    password varchar(255), username varchar(255),
    primary key (id)
) engine=InnoDB;

alter table user add constraint unique (username);

create table user_role (
    user_id bigint not null,
    roles varchar(255)
) engine=InnoDB;

create table disk(
    id bigint not null auto_increment,
    name varchar(255),
    skinName varchar(255),
    userName varchar(255),
    primary key (id)
) engine=InnoDB;

alter table disk add constraint unique (name);

create table user_current_disk(
    user_id bigint not null,
    disk_id bigint not null,
    primary key (user_id, disk_id)
) engine=InnoDB;

create table users_old_disks(
    user_id bigint not null ,
    disk_id bigint not null ,
    primary key (user_id, disk_id)
) engine=InnoDB;

alter table user_current_disk add constraint www1 foreign key (user_id) references user(id);
alter table user_current_disk add constraint www2 foreign key (disk_id) references disk(id);

alter table users_old_disks add constraint wwe1 foreign key (user_id) references user(id);
alter table users_old_disks add constraint wwe2 foreign key (disk_id) references disk(id);