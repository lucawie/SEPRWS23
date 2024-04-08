CREATE TABLE IF NOT EXISTS breed
(
  id BIGINT PRIMARY KEY,
  name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS horse
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  -- Instead of an ENUM (H2 specific) this could also be done with a character string type and a check constraint.
  sex ENUM ('MALE', 'FEMALE') NOT NULL,
  date_of_birth DATE NOT NULL,
  height NUMERIC(4,2),
  weight NUMERIC(7,2),
  breed_id BIGINT REFERENCES breed(id) ON DELETE CASCADE
);

-- creating tournament table(s)
CREATE TABLE IF NOT EXISTS tournament
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS standings
(
    tournament_id BIGINT REFERENCES tournament(id) ON DELETE CASCADE,
    horse_id BIGINT REFERENCES horse(id) ON DELETE CASCADE,
    entry_number INT CHECK (entry_number IS NULL OR entry_number BETWEEN 0 AND 8),
    round_reached INT CHECK (round_reached IS NULL OR round_reached BETWEEN 0 AND 4)
    );
