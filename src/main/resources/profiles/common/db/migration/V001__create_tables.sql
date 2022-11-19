create table menuitem (
  id bigint not null,
  parent bigint not null,
  menu_key varchar(255),
  menu_value varchar(255),
  target varchar(255),
  menu_service varchar(255),
  grid_def varchar(255),
  tooltip varchar(255),
  image varchar(255),
  expanded boolean,
  last_update datetime,
  PRIMARY KEY (id)
);

create table customer (
  id bigint auto_increment not null,
  firstname varchar(255),
  lastname varchar(255),
  address varchar(255),
  email varchar(255),
  PRIMARY KEY (id)
);