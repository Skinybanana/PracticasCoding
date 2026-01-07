-- =========================
-- V2 - Full schema (MVP)
-- PostgreSQL
-- =========================

-- USERS
CREATE TABLE users (
  id            BIGSERIAL PRIMARY KEY,
  email         VARCHAR(255) NOT NULL UNIQUE,
  username      VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role          VARCHAR(50)  NOT NULL CHECK (role IN ('USER', 'ADMIN')),
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- PRODUCTS
CREATE TABLE products (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(200) NOT NULL,
  price       NUMERIC(12,2) NOT NULL CHECK (price >= 0),
  stock       INT NOT NULL CHECK (stock >= 0),
  active      BOOLEAN NOT NULL DEFAULT TRUE,
  version     INT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_products_name ON products (name);

-- CARTS (1 cart per user)
CREATE TABLE carts (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT NOT NULL UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT fk_carts_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- CART ITEMS
CREATE TABLE cart_items (
  id         BIGSERIAL PRIMARY KEY,
  cart_id    BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity   INT NOT NULL CHECK (quantity > 0),

  CONSTRAINT fk_cart_items_cart
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,

  CONSTRAINT fk_cart_items_product
    FOREIGN KEY (product_id) REFERENCES products(id),

  CONSTRAINT uq_cart_product UNIQUE (cart_id, product_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);
CREATE INDEX idx_cart_items_product_id ON cart_items (product_id);

-- ORDERS
CREATE TABLE orders (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT NOT NULL,
  status     VARCHAR(50) NOT NULL CHECK (status IN ('CREATED', 'PAID', 'CANCELED')),
  total      NUMERIC(12,2) NOT NULL CHECK (total >= 0),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_orders_user_id ON orders (user_id);

-- ORDER ITEMS (snapshot)
CREATE TABLE order_items (
  id                  BIGSERIAL PRIMARY KEY,
  order_id            BIGINT NOT NULL,
  product_id          BIGINT NULL, -- opcional: si borras un producto, tu orden sigue
  product_name        VARCHAR(200) NOT NULL,
  unit_price          NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
  quantity            INT NOT NULL CHECK (quantity > 0),
  line_total          NUMERIC(12,2) NOT NULL CHECK (line_total >= 0),

  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,

  CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_product_id ON order_items (product_id);
