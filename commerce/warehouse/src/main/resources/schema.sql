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

CREATE TABLE IF NOT EXISTS order_booking (
    order_id UUID,
    delivery_id UUID,
    CONSTRAINT pk_order_booking PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS order_booking_products (
    order_id UUID,
    product_id UUID,
    quantity INTEGER NOT NULL,
    CONSTRAINT pk_order_booking_products PRIMARY KEY (order_id, product_id),
    CONSTRAINT check_order_booking_quantity CHECK(quantity >= 0),
    CONSTRAINT fk_order_booking_products_order_id_order_booking_order_id FOREIGN KEY (order_id)
            REFERENCES order_booking(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_booking_products_product_id_products_id FOREIGN KEY (product_id)
                REFERENCES products(id)
);