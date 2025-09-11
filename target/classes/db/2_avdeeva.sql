CREATE TABLE product (
                product_id SERIAL PRIMARY KEY,
                product_name VARCHAR(50) NOT NULL UNIQUE,
                product_price DOUBLE PRECISION  NOT NULL CHECK (product_price >= 0),
                product_description VARCHAR(100) NOT NULL,
                product_image_url VARCHAR(50),
                product_category_id INTEGER NOT NULL REFERENCES category_product ON DELETE RESTRICT ON UPDATE CASCADE,
                product_creation_user TEXT NOT NULL,
                product_creation_date TIMESTAMP DEFAULT NOW() NOT NULL,
                product_modified_date TIMESTAMP DEFAULT NOW(),
                product_modified_user TEXT,
                product_is_active SMALLINT DEFAULT 0,
                product_is_del SMALLINT DEFAULT 0
);

CREATE UNIQUE INDEX ON product (LOWER(product_name));
CREATE INDEX idx_product_category ON product(product_category_id);
CREATE INDEX idx_product_price ON product(product_price);