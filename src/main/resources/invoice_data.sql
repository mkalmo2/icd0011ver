
INSERT INTO invoice (id, number, start_date, end_date)
  VALUES (1, 'i1_v1', '2024-01-01', '2024-02-01');

INSERT INTO invoice (id, number, start_date, end_date)
  VALUES (1, 'i1_v2', '2024-02-01', '2024-03-01');

INSERT INTO invoice (id, number, start_date, end_date)
  VALUES (1, 'i1_v3', '2024-03-01', null);

INSERT INTO invoice_row (invoice_id, item_name, start_date, end_date)
  VALUES (1, 'item1', '2024-02-01', '2024-02-15');

INSERT INTO invoice_row (invoice_id, item_name, start_date, end_date)
  VALUES (1, 'item2', '2024-02-01', '2024-02-15');

INSERT INTO invoice_row (invoice_id, item_name, start_date, end_date)
  VALUES (1, 'item3', '2024-03-01', null);
