CREATE TABLE IF NOT EXISTS shopping_carts (
    id UUID,
    username VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT pk_shopping_carts PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS unique_shopping_carts_username_by_active ON shopping_carts (username)
    WHERE active;

CREATE TABLE IF NOT EXISTS shopping_cart_products (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY (sequence NAME shopping_cart_products_id_sequence INCREMENT 10 START 1 MINVALUE 1),
    shopping_cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_shopping_cart_products PRIMARY KEY (id),
    CONSTRAINT fk_shopping_cart_products_shopping_cart_id_shopping_carts_id FOREIGN KEY (shopping_cart_id)
        REFERENCES shopping_carts(id) ON DELETE CASCADE,
    CONSTRAINT unique_shopping_cart_products_shopping_cart_id_product_id UNIQUE (shopping_cart_id, product_id)
);