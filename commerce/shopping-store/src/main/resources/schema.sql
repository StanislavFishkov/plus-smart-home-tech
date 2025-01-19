CREATE TABLE IF NOT EXISTS products (
    id UUID,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    image_src VARCHAR(255),
    quantity_state VARCHAR(255) NOT NULL,
    product_state VARCHAR(255) NOT NULL,
    rating DOUBLE PRECISION,
    product_category VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT check_products_rating CHECK(rating >= 1 AND rating <= 5),
    CONSTRAINT check_products_price CHECK(price >= 1)
);