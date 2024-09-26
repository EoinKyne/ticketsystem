CREATE TABLE IF NOT EXISTS Ticket (
    id UUID NOT NULL,
    location varchar(50) NOT NULL,
    ticket_creation_date_time timestamp NOT NULL,
    ticket_first_weight_date_time timestamp,
    ticket_second_weight_date_time timestamp,
    gross_weight INT,
    tare_weight INT,
    net_weight INT,
    version INT,
    PRIMARY KEY (id)
);