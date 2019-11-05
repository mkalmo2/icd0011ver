CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL
);

CREATE TABLE phone (
    person_id INTEGER NOT NULL,
    number VARCHAR(100) NOT NULL,
    FOREIGN KEY (person_id)
        REFERENCES person(id) ON DELETE CASCADE
);
