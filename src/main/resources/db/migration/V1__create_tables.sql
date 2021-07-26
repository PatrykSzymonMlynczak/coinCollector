CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE product(
     id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
     my_price REAL,
     name VARCHAR(50) UNIQUE,
     PRIMARY KEY(id)
);

CREATE TABLE quantity_price_map(
     product_id BIGINT,
     price REAL,
     quantity REAL NOT NULL,
      FOREIGN KEY(product_id)
          REFERENCES product(id)
);

CREATE TABLE person(
     id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
     debt REAL,
     name VARCHAR(50) UNIQUE,
     PRIMARY KEY(id)
);

CREATE TABLE sale (
   id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
   discount REAL,
   earned REAL,
   income REAL,
   my_sort_price REAL,
   quantity REAL,
   day date,
   moment time without time zone,
   person_id bigint,
   product_id bigint,
   PRIMARY KEY(id),
     FOREIGN KEY(person_id)
         REFERENCES person(id),
     FOREIGN KEY(product_id)
         REFERENCES product(id)
);

