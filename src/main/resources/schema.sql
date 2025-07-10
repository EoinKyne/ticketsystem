CREATE TABLE IF NOT EXISTS Ticket (
    id UUID NOT NULL,
    location varchar(50) NOT NULL,
    ticket_creation_date_time timestamp,
    product varchar(50) NOT NULL,
    destination varchar (50) NOT NULL,
    version INT,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS Weight_Ticket (
    id UUID NOT NULL,
    ticket_number UUID NOT NULL REFERENCES Ticket(id),
    gross_weight double precision,
    ticket_gross_weight_date_time timestamp,
    tare_weight double precision,
    ticket_tare_weight_date_time timestamp,
    nett_weight double precision,
    version INT,
    PRIMARY KEY (id)
);


