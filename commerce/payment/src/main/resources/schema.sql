CREATE TABLE IF NOT EXISTS payments (
    id UUID,
    order_id UUID NOT NULL,
    payment_state VARCHAR(255) NOT NULL,
    total_payment DECIMAL(10,2) NOT NULL,
    delivery_total DECIMAL(10,2) NOT NULL,
    product_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_payments PRIMARY KEY (id)
);