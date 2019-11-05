DROP SEQUENCE IF EXISTS seq_invoice;

CREATE SEQUENCE seq_invoice AS INTEGER START WITH 100;

DROP TABLE IF EXISTS invoice_row;
DROP TABLE IF EXISTS invoice;

CREATE TABLE invoice (
  id INTEGER NOT NULL,
  number VARCHAR(255),
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NULL
);

CREATE TABLE invoice_row (
  invoice_id INTEGER NOT NULL,
  item_name VARCHAR(255),
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NULL
);
