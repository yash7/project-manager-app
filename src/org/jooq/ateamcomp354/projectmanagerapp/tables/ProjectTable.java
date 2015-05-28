/**
 * This class is generated by jOOQ
 */
package org.jooq.ateamcomp354.projectmanagerapp.tables;


import ateamcomp354.projectmanagerapp.jooq.converters.C_BooleanConverter;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.ateamcomp354.projectmanagerapp.DefaultSchema;
import org.jooq.ateamcomp354.projectmanagerapp.Keys;
import org.jooq.ateamcomp354.projectmanagerapp.tables.records.ProjectRecord;
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
public class ProjectTable extends TableImpl<ProjectRecord> {

	private static final long serialVersionUID = -1077187507;

	/**
	 * The reference instance of <code>project</code>
	 */
	public static final ProjectTable PROJECT = new ProjectTable();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ProjectRecord> getRecordType() {
		return ProjectRecord.class;
	}

	/**
	 * The column <code>project.id</code>.
	 */
	public final TableField<ProjectRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>project.project_name</code>.
	 */
	public final TableField<ProjectRecord, String> PROJECT_NAME = createField("project_name", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>project.description</code>.
	 */
	public final TableField<ProjectRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>project.completed</code>.
	 */
	public final TableField<ProjectRecord, Boolean> COMPLETED = createField("completed", org.jooq.impl.SQLDataType.INTEGER.defaulted(true), this, "", new C_BooleanConverter());

	/**
	 * Create a <code>project</code> table reference
	 */
	public ProjectTable() {
		this("project", null);
	}

	/**
	 * Create an aliased <code>project</code> table reference
	 */
	public ProjectTable(String alias) {
		this(alias, PROJECT);
	}

	private ProjectTable(String alias, Table<ProjectRecord> aliased) {
		this(alias, aliased, null);
	}

	private ProjectTable(String alias, Table<ProjectRecord> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ProjectRecord> getPrimaryKey() {
		return Keys.PK_PROJECT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ProjectRecord>> getKeys() {
		return Arrays.<UniqueKey<ProjectRecord>>asList(Keys.PK_PROJECT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectTable as(String alias) {
		return new ProjectTable(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ProjectTable rename(String name) {
		return new ProjectTable(name, null);
	}
}
