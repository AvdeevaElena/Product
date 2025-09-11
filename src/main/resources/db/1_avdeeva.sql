CREATE TABLE category_product (
                                  category_id SERIAL PRIMARY KEY,
                                  category_name VARCHAR(100) NOT NULL UNIQUE,
                                  category_description VARCHAR(200) NOT NULL,
                                  category_creation_user TEXT NOT NULL,
                                  category_creation_date TIMESTAMP DEFAULT NOW() NOT NULL,
                                  category_modified_date TIMESTAMP DEFAULT NOW(),
                                  category_modified_user TEXT,
                                  category_is_del SMALLINT DEFAULT 0
);

CREATE UNIQUE INDEX ON category_product (LOWER(category_name));

INSERT INTO category_product (category_name, category_description, category_creation_user) VALUES
                                ('Рыба','Рыбные продукты', 'admim'),
                                ('Мясо','Мясные продукты', 'admim'),
                                ('Хлеб','Злаковые продукты', 'admim');
