-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create a simple breeds table
CREATE TABLE IF NOT EXISTS breeds (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_breed_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT unique_breed_category UNIQUE (name, category_id)
);

-- Create pets table
CREATE TABLE IF NOT EXISTS pets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    breed_id BIGINT,
    category_id BIGINT,
    age INTEGER NOT NULL,
    description TEXT,
    image VARCHAR(255),
    gender VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    owner_id BIGINT,
    shelter_id BIGINT,
    veterinarian_id BIGINT,
    adopted_by BIGINT,
    CONSTRAINT fk_pet_breed FOREIGN KEY (breed_id) REFERENCES breeds(id),
    CONSTRAINT fk_pet_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Insert some default categories
INSERT INTO categories (name) VALUES 
    ('Dogs'),
    ('Cats'),
    ('Birds')
ON CONFLICT DO NOTHING; 