package com.sanados.test.annotations.structure;

import ch.qos.logback.classic.Logger;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sanados on 26.02.2016.
 * Base Pojo of which all Pojo need to extend
 */
abstract public class Saveable {
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Saveable.class);

	private final Provider<IDbReference> DB_REFERENCE_PROVIDER = new DbReferenceProvider(this.getClass());

	@Inject
	protected DataSource DATASOURCE;

	@DbCharacteristic(position = 1, fieldType = DbCharacteristic.Type.LONG)
	private Long id;

	private static List<Field> getAllFields(List<Field> fields, final Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	public long save() {
		try {
			PreparedStatement statement = DATASOURCE.getConnection().prepareStatement(DB_REFERENCE_PROVIDER.get().getSaveSql(), Statement.RETURN_GENERATED_KEYS);
			for (Field field : getAllFields(new LinkedList<>(), this.getClass())) {
				if (field.isAnnotationPresent(DbCharacteristic.class)) {
					DbCharacteristic dbCharacteristicAnnotation = field.getAnnotation(DbCharacteristic.class);
					int[] positions = dbCharacteristicAnnotation.position();
					String fieldName = field.getName();
					String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

					Method getterMethod = this.getClass().getMethod(methodName);
					LOGGER.debug("accessing {} and setting on positions: {}", field.getName(), positions);
					switch (dbCharacteristicAnnotation.fieldType()) {
						case INTEGER:
							Integer integerValue = (Integer) getterMethod.invoke(this);
							for (int position : positions) {
								try {
									statement.setInt(position, integerValue);
								} catch (Exception e) {
									LOGGER.error("setting int failed", e);
								}
							}
							break;
						case LONG:
							Long longValue = (Long) getterMethod.invoke(this);
							for (int position : positions) {
								statement.setLong(position, longValue);
							}
							break;
						case DATE:
							java.util.Date dateValue = (java.util.Date) getterMethod.invoke(this);
							for (int position : positions) {
								statement.setDate(position, new java.sql.Date(dateValue.getTime()));
							}
							break;
						case STRING:
						default:
							LOGGER.debug("accessing {}", field.getName());
							String stringValue = (String) getterMethod.invoke(this);
							for (int position : positions) {
								statement.setString(position, stringValue);
							}
							break;
					}
				}
			}

			LOGGER.debug("final save statement was: {}", statement.toString());

			int affectedRows = statement.executeUpdate();
			if (affectedRows != 1) {
				LOGGER.info("Saving was uneventful, no row touched with sql: {}", statement.toString());
				return 0L;
			}

			LOGGER.debug("affected rows: {}", affectedRows);

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys != null && generatedKeys.next()) {
					this.setId(generatedKeys.getLong(1));
				} else {
					LOGGER.info("not gotten generated key back, maybe table is missing auto increment");
				}
			}
		} catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			LOGGER.error("failed to save pojo: {}", e.getMessage(), e);
		}
		return 0L;
	}

	public void load(final long id) {
		try {
			PreparedStatement statement = DATASOURCE.getConnection().prepareStatement(DB_REFERENCE_PROVIDER.get().getLoadByIdSql());
			statement.setLong(1, id);
			LOGGER.debug("fetching object with sql: {}", statement.toString());
			ResultSet rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				for (Field field : getAllFields(new LinkedList<>(), this.getClass())) {
					LOGGER.debug("Field: {}", field.getName());
					if (field.isAnnotationPresent(DbCharacteristic.class)) {
						DbCharacteristic dbCharacteristicAnnotation = field.getAnnotation(DbCharacteristic.class);
						System.out.println(dbCharacteristicAnnotation);
						int[] position = dbCharacteristicAnnotation.position();
						String fieldName = field.getName();
						String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

						switch (dbCharacteristicAnnotation.fieldType()) {
							case INTEGER:
								callMethod(methodName, rs.getInt(position[0]), Integer.class);
								break;
							case LONG:
								callMethod(methodName, rs.getLong(position[0]), Long.class);
								break;
							case DATE:
								callMethod(methodName, new java.util.Date(rs.getTimestamp(position[0]).getTime()), java.util.Date.class);
								break;
							case STRING:
							default:
								LOGGER.debug("type was: {}", dbCharacteristicAnnotation.fieldType());
								callMethod(methodName, rs.getString(position[0]), String.class);
								break;
						}

					}
				}
			}
		} catch (SQLException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("failed to load pojo: {}", e.getMessage(), e);
		}
	}

	private void callMethod(final String methodName, final Object value, final Class argument) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Class[] arguments = new Class[1];
		arguments[0] = argument;
		Method setterMethod = this.getClass().getMethod(methodName, arguments);
		setterMethod.invoke(this, value);
	}


	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
}
