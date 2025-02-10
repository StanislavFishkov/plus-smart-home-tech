CREATE TABLE IF NOT EXISTS orders (
    id UUID,
    username VARCHAR(255) NOT NULL,
    shopping_cart_id UUID NOT NULL,
    payment_id UUID,
    delivery_id UUID,
    order_state VARCHAR(255) NOT NULL,
    delivery_weight DECIMAL(10,2),
    delivery_volume DECIMAL(10,2),
    fragile BOOLEAN,
    total_price DECIMAL(10,2),
    delivery_price DECIMAL(10,2),
    product_price DECIMAL(10,2),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_order_products PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_products_order_id_orders_id FOREIGN KEY (order_id)
        REFERENCES orders(id) ON DELETE CASCADE
);