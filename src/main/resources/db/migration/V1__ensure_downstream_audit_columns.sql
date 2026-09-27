CREATE TABLE IF NOT EXISTS downstream_audit (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255),
    service_name VARCHAR(255) NOT NULL,
    operation VARCHAR(255) NOT NULL,
    request_blob TEXT NOT NULL,
    response_blob TEXT,
    http_status INTEGER NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

ALTER TABLE downstream_audit
    ADD COLUMN IF NOT EXISTS service_name VARCHAR(255);

ALTER TABLE downstream_audit
    ADD COLUMN IF NOT EXISTS operation VARCHAR(255);

UPDATE downstream_audit
SET service_name = 'unknown'
WHERE service_name IS NULL;

UPDATE downstream_audit
SET operation = 'unknown'
WHERE operation IS NULL;

ALTER TABLE downstream_audit
    ALTER COLUMN service_name SET NOT NULL,
    ALTER COLUMN operation SET NOT NULL;
