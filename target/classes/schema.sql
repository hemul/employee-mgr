-- Drop existing tables if they exist
DROP TABLE IF EXISTS temporary_schedule_days;
DROP TABLE IF EXISTS temporary_schedule;
DROP TABLE IF EXISTS employee_office_days;
DROP TABLE IF EXISTS employee;

-- Create tables with SQLite syntax
CREATE TABLE IF NOT EXISTS employee (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    birth_date DATE,
    department TEXT,
    position TEXT
);

CREATE TABLE IF NOT EXISTS employee_office_days (
    employee_id INTEGER NOT NULL,
    office_day TEXT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    PRIMARY KEY (employee_id, office_day)
);

CREATE TABLE IF NOT EXISTS temporary_schedule (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    employee_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS temporary_schedule_days (
    schedule_id INTEGER NOT NULL,
    office_day TEXT NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES temporary_schedule(id) ON DELETE CASCADE,
    PRIMARY KEY (schedule_id, office_day)
); 