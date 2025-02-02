CREATE TABLE community_membership_entity
(
    community_membership_id INT
    AUTO_INCREMENT PRIMARY KEY,
    community_id INT NOT NULL,
    email VARCHAR
    (255) NOT NULL,
    amount DOUBLE NOT NULL,
    is_loan_taken BOOLEAN NOT NULL,
    is_accepted BOOLEAN NOT NULL,
    is_loan_defaulter BOOLEAN NOT NULL
);
