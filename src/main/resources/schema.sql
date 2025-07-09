CREATE TABLE IF NOT EXISTS Ticket (
    id UUID NOT NULL,
    location varchar(50) NOT NULL,
    ticket_creation_date_time timestamp,
    gross_weight double precision,
    ticket_gross_weight_date_time timestamp,
    tare_weight double precision,
    ticket_tare_weight_date_time timestamp,
    nett_weight double precision,
    version INT,
    PRIMARY KEY (id)
);