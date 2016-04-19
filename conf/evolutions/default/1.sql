# Chat schema

# --- !Ups
create table `messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `room` VARCHAR(80) NOT NULL,
  `datetime` TIMESTAMP NOT NULL,
  `user` TEXT NOT NULL,
  `message` TEXT NOT NULL
);
create index room_idx on messages(room) using HASH;
create index dt_idx on messages(datetime) using BTREE;

# --- !Downs
drop table `messages`
