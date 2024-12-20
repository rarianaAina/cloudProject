-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email_verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT FALSE,
    login_attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create verification_tokens table
CREATE TABLE verification_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255),
    expiry_date TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_verification_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

-- Create two_factor_codes table
CREATE TABLE two_factor_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(6),
    expiry_date TIMESTAMP,
    used BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_two_factor_codes_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT check_code_format
        CHECK (code ~ '^\d{6}$')
);

-- Create sessions table
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expire_le TIMESTAMP NOT NULL,
    cree_le TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_utilisateur BIGINT NOT NULL,
    CONSTRAINT fk_sessions_user
        FOREIGN KEY (id_utilisateur)
        REFERENCES users (id)
        ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_verification_tokens_user_id ON verification_tokens(user_id);
CREATE INDEX idx_two_factor_codes_user_id ON two_factor_codes(user_id);
CREATE INDEX idx_sessions_user_id ON sessions(id_utilisateur);
CREATE INDEX idx_users_email ON users(email);