
CREATE TABLE Guest (
    guest_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    email TEXT NOT NULL,
    address TEXT NOT NULL
);

CREATE TABLE Reservation (
    reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
    guest_id INTEGER NOT NULL,
    check_in_date TEXT,
    check_out_date TEXT,
    number_of_guests INTEGER,
    FOREIGN KEY (guest_id) REFERENCES Guest(guest_id) ON DELETE CASCADE
);

CREATE TABLE Room (
    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservation_id INTEGER NOT NULL,
    room_type TEXT NOT NULL,
    price_per_night REAL,
    FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id) ON DELETE CASCADE
);

CREATE TABLE User (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE Review (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservation_id INTEGER NOT NULL,
    review_text TEXT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id)
);

INSERT INTO User (username, password) VALUES ('admin', 'pass');
INSERT INTO User (username, password) VALUES ('user', 'pass');
