/**
 * This class is generated by jOOQ
 */
package org.killbill.billing.plugin.coupon.dao.gen.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CouponsHistory extends org.jooq.impl.TableImpl<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord> {

	private static final long serialVersionUID = -383397066;

	/**
	 * The reference instance of <code>killbill.coupons_history</code>
	 */
	public static final org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory COUPONS_HISTORY = new org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord> getRecordType() {
		return org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord.class;
	}

	/**
	 * The column <code>killbill.coupons_history.record_id</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, org.jooq.types.ULong> RECORD_ID = createField("record_id", org.jooq.impl.SQLDataType.BIGINTUNSIGNED.nullable(false), this, "");

	/**
	 * The column <code>killbill.coupons_history.id</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, java.lang.String> ID = createField("id", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

	/**
	 * The column <code>killbill.coupons_history.target_record_id</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, org.jooq.types.ULong> TARGET_RECORD_ID = createField("target_record_id", org.jooq.impl.SQLDataType.BIGINTUNSIGNED.nullable(false), this, "");

	/**
	 * The column <code>killbill.coupons_history.coupon_code</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, java.lang.String> COUPON_CODE = createField("coupon_code", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

	/**
	 * The column <code>killbill.coupons_history.coupon_name</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, java.lang.String> COUPON_NAME = createField("coupon_name", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>killbill.coupons_history.tenant_record_id</code>.
	 */
	public final org.jooq.TableField<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, org.jooq.types.ULong> TENANT_RECORD_ID = createField("tenant_record_id", org.jooq.impl.SQLDataType.BIGINTUNSIGNED.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>killbill.coupons_history</code> table reference
	 */
	public CouponsHistory() {
		this("coupons_history", null);
	}

	/**
	 * Create an aliased <code>killbill.coupons_history</code> table reference
	 */
	public CouponsHistory(java.lang.String alias) {
		this(alias, org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory.COUPONS_HISTORY);
	}

	private CouponsHistory(java.lang.String alias, org.jooq.Table<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord> aliased) {
		this(alias, aliased, null);
	}

	private CouponsHistory(java.lang.String alias, org.jooq.Table<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.killbill.billing.plugin.coupon.dao.gen.Killbill.KILLBILL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord, org.jooq.types.ULong> getIdentity() {
		return org.killbill.billing.plugin.coupon.dao.gen.Keys.IDENTITY_COUPONS_HISTORY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord> getPrimaryKey() {
		return org.killbill.billing.plugin.coupon.dao.gen.Keys.KEY_COUPONS_HISTORY_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsHistoryRecord>>asList(org.killbill.billing.plugin.coupon.dao.gen.Keys.KEY_COUPONS_HISTORY_PRIMARY, org.killbill.billing.plugin.coupon.dao.gen.Keys.KEY_COUPONS_HISTORY_RECORD_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory as(java.lang.String alias) {
		return new org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory rename(java.lang.String name) {
		return new org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsHistory(name, null);
	}
}
