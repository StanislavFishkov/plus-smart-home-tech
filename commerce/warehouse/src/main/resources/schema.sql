CREATE TABLE IF NOT EXISTS products (
    id UUID,
    fragile BOOLEAN NOT NULL,
    width DECIMAL(10,2) NOT NULL,
    height DECIMAL(10,2) NOT NULL,
    depth DECIMAL(10,2) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT check_products_quantity CHECK(quantity >= 0)
);