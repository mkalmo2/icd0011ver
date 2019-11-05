DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS orders_history;

CREATE TABLE orders (
  id SERIAL NOT NULL PRIMARY KEY,
  version INTEGER NOT NULL,
  number VARCHAR(255)
);

CREATE TABLE orders_history (
  id INTEGER NOT NULL,
  version INTEGER NOT NULL,
  number VARCHAR(255),
  PRIMARY KEY (id, version)
);
