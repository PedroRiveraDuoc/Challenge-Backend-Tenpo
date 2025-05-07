CREATE TABLE api_call_logs (
    id UUID PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    parameters_json TEXT,
    response_json TEXT,
    status VARCHAR(10) NOT NULL
); 