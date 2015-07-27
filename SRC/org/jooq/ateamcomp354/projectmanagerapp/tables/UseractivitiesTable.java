/**
 * This class is generated by jOOQ
 */
package org.jooq.ateamcomp354.projectmanagerapp.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.ateamcomp354.projectmanagerapp.DefaultSchema;
import org.jooq.ateamcomp354.projectmanagerapp.Keys;
import org.jooq.ateamcomp354.projectmanagerapp.tables.records.UseractivitiesRecord;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UseractivitiesTable extends TableImpl<UseractivitiesRecord> {

	private static final long serialVersionUID = 12385335;

	/**
	 * The reference instance of <code>userActivities</code>
	 */
	public static final UseractivitiesTable USERACTIVITIES = new UseractivitiesTable();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<UseractivitiesRecord> getRecordType() {
		return UseractivitiesRecord.class;
	}

	/**
	 * The column <code>userActivities.id</code>.
	 */
	public final TableField<UseractivitiesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>userActivities.activity_id</code>.
	 */
	public final TableField<UseractivitiesRecord, Integer> ACTIVITY_ID = createField("activity_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>userActivities.user_id</code>.
	 */
	public final TableField<UseractivitiesRecord, Integer> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>userActivities</code> table reference
	 */
	public UseractivitiesTable() {
		this("userActivities", null);
	}

	/**
	 * Create an aliased <code>userActivities</code> table reference
	 */
	public UseractivitiesTable(String alias) {
		this(alias, USERACTIVITIES);
	}

	private UseractivitiesTable(String alias, Table<UseractivitiesRecord> aliased) {
		this(alias, aliased, null);
	}

	private UseractivitiesTable(String alias, Table<UseractivitiesRecord> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<UseractivitiesRecord> getPrimaryKey() {
		return Keys.PK_USERACTIVITIES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<UseractivitiesRecord>> getKeys() {
		return Arrays.<UniqueKey<UseractivitiesRecord>>asList(Keys.PK_USERACTIVITIES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<UseractivitiesRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<UseractivitiesRecord, ?>>asList(Keys.FK_USERACTIVITIES_ACTIVITY_1, Keys.FK_USERACTIVITIES_USERS_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UseractivitiesTable as(String alias) {
		return new UseractivitiesTable(alias, this);
	}

	/**
	 * Rename this table
	 */
	public UseractivitiesTable rename(String name) {
		return new UseractivitiesTable(name, null);
	}
}