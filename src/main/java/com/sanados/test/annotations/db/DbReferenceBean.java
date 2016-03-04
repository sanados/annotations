package com.sanados.test.annotations.db;

import com.sanados.test.annotations.dao.pojo.BeanPojo;
import com.sanados.test.annotations.structure.DbReference;
import com.sanados.test.annotations.structure.IDbReference;


/**
 * Created by sanados on 26.02.2016.
 * magic
 */
@DbReference(beanType = BeanPojo.class)
public class DbReferenceBean implements IDbReference {
	public static final String TABLE = "bean";
	public static final String LOAD_BY_ID_SQL = "SELECT "
			+ "	* "
			+ "FROM "
			+ TABLE
			+ " WHERE "
			+ TABLE + "Id = ?";

	public static final String SAVE_SQL = "INSERT INTO "
			+ TABLE
			+ " SET "
			+ TABLE + "Id = ?, "
			+ " title = ?, "
			+ " lastModified = ?, "
			+ " viewCount = ? "
			+ "ON DUPLICATE KEY UPDATE "
			+ " title = ?, "
			+ " lastModified = ?, "
			+ " viewCount = ? ";

	@Override
	public String getLoadByIdSql() {
		return LOAD_BY_ID_SQL;
	}

	@Override
	public String getSaveSql() {
		return SAVE_SQL;
	}
}
