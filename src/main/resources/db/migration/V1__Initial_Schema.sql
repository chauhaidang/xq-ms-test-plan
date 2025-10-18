-- Initial schema for xq-ms-test-plan microservice
-- Creates requirements and linked_requirements tables

-- Requirements table
CREATE TABLE IF NOT EXISTS requirements (
    req_id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    scopes VARCHAR(500) NOT NULL,
    tags VARCHAR(200) NOT NULL,
    ref VARCHAR(500) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    CONSTRAINT uk_requirements_title UNIQUE (title)
);

-- Create index on uuid for faster lookups
CREATE INDEX idx_requirements_uuid ON requirements(uuid);

-- Create index on title for faster lookups
CREATE INDEX idx_requirements_title ON requirements(title);

-- Linked Requirements table
CREATE TABLE IF NOT EXISTS linked_requirements (
    link_id BIGSERIAL PRIMARY KEY,
    reqa_id INTEGER NOT NULL,
    reqb_id INTEGER NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- Create indexes for foreign key lookups
CREATE INDEX idx_linked_requirements_reqa ON linked_requirements(reqa_id);
CREATE INDEX idx_linked_requirements_reqb ON linked_requirements(reqb_id);

-- Add comments for documentation
COMMENT ON TABLE requirements IS 'Stores test plan requirements';
COMMENT ON COLUMN requirements.req_id IS 'Auto-generated primary key';
COMMENT ON COLUMN requirements.uuid IS 'Unique identifier for the requirement';
COMMENT ON COLUMN requirements.title IS 'Requirement title (unique)';
COMMENT ON COLUMN requirements.description IS 'Detailed description of the requirement';
COMMENT ON COLUMN requirements.scopes IS 'Scope information for the requirement';
COMMENT ON COLUMN requirements.tags IS 'Tags associated with the requirement';
COMMENT ON COLUMN requirements.ref IS 'Reference links or documents';

COMMENT ON TABLE linked_requirements IS 'Stores relationships between requirements';
COMMENT ON COLUMN linked_requirements.link_id IS 'Auto-generated primary key';
COMMENT ON COLUMN linked_requirements.reqa_id IS 'First requirement ID in the relationship';
COMMENT ON COLUMN linked_requirements.reqb_id IS 'Second requirement ID in the relationship';
