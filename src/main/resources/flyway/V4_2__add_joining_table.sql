
CREATE TABLE person_phone (
   person_id INTEGER NOT NULL,
   phone_id INTEGER NOT NULL,
   FOREIGN KEY (person_id)
       REFERENCES person(id) ON DELETE CASCADE,
   FOREIGN KEY (phone_id)
       REFERENCES phone(id) ON DELETE CASCADE
);
