/*! SET storage_engine=INNODB */;

DROP TABLE IF EXISTS coupons;
CREATE TABLE coupons (
  record_id int(11) unsigned not null auto_increment,
  coupon_code varchar(36) NOT NULL,
  coupon_name varchar(100) NOT NULL,
  discount_type VARCHAR(15) NOT NULL,
  percentage_discount FLOAT NOT NULL DEFAULT 0,
  kb_tenant_id char(36) not null,
  PRIMARY KEY(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
CREATE UNIQUE INDEX coupons_code ON coupons(coupon_code);
CREATE INDEX coupons_tenant_record_id ON coupons(kb_tenant_id);

DROP TABLE IF EXISTS coupons_applied;
CREATE TABLE coupons_applied (
  record_id int(11) unsigned not null auto_increment,
  coupon_code varchar(36) NOT NULL,
  kb_subscription_id char(36) not null,
  kb_account_id char(36) not null,
  kb_tenant_id char(36) not null,
  PRIMARY KEY(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
CREATE INDEX coupons_applied_code ON coupons_applied(coupon_code);
CREATE INDEX coupons_applied_account ON coupons_applied(kb_account_id);
CREATE INDEX coupons_tenant_record_id ON coupons_applied(kb_tenant_id);
CREATE UNIQUE INDEX coupons_applied_unique ON coupons_applied(coupon_code, kb_subscription_id);

DROP TABLE IF EXISTS coupons_products;
CREATE TABLE coupons_products (
  record_id int(11) unsigned not null auto_increment,
  coupon_code varchar(36) NOT NULL,
  product_name varchar(50) NOT NULL,
  kb_tenant_id char(36) NOT NULL,
  PRIMARY KEY(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
CREATE INDEX coupons_products_code ON coupons_products(coupon_code);