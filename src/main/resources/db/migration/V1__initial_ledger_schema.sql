CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE accounts(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    account_number VARCHAR(64) UNIQUE NOT NULL,
    account_type VARCHAR(32) NOT NULL CHECK (account_type IN ('ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE')),
    currency VARCHAR(3) NOT NULL,
    book_balance BIGINT DEFAULT 0 NOT NULL,
    pending_balance BIGINT DEFAULT 0 NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE journal_entries(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    idempotency_key VARCHAR(128) UNIQUE NOT NULL,
    description TEXT,
    status VARCHAR(32) DEFAULT 'SETTLED' NOT NULL CHECK (status IN ('PENDING', 'SETTLED', 'FAILED')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE postings(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    journal_entry_id UUID NOT NULL REFERENCES journal_entries(id) ON DELETE RESTRICT,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    amount BIGINT NOT NULL CHECK(amount > 0),
    direction VARCHAR(8) NOT NULL CHECK(direction IN ('DEBIT', 'CREDIT')),
    sequence_order INT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE outbox_events(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    aggregate_type VARCHAR(64) NOT NULL,
    aggregate_id VARCHAR(64) NOT NULL,
    event_type VARCHAR(64) NOT NULL,
    payload JSONB NOT NULL,
    status VARCHAR(32) DEFAULT 'PENDING' NOT NULL CHECK(status IN ('PENDING', 'PUBLISHED', 'FAILED')),
    retry_count INT DEFAULT 0 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_postings_account ON postings(account_id);
CREATE INDEX idx_outbox_pending ON outbox_events(status, created_at) WHERE status = 'PENDING';