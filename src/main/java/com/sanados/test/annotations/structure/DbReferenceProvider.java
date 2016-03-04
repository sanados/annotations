package com.sanados.test.annotations.structure;

import ch.qos.logback.classic.Logger;
import com.google.common.reflect.ClassPath;
import com.google.inject.Provider;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juergen.Krieger on 04.03.2016.
 * Dynatrace
 */
public class DbReferenceProvider<T extends IDbReference> implements Provider<T> {
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DbReferenceProvider.class);
	private static final Map<Class<? extends Saveable>, Class<? extends IDbReference>> DB_REFERENCE_MAP = new HashMap<>();
	private Class<T> callerClass;

	public DbReferenceProvider(Class<T> callerClass) {
		if (DB_REFERENCE_MAP.size() <= 0) {
			populateMap();
		}
		this.callerClass = callerClass;
	}

	private void populateMap() {
		try {
			for (ClassPath.ClassInfo classInfo :
					ClassPath.from(getClass().getClassLoader()).getTopLevelClasses("com.sanados.test.annotations.db")) {

				//get annotated class from class that is to bind
				Class<? extends IDbReference> classToBind = (Class<? extends IDbReference>) classInfo.load();
				if (classToBind.isAnnotationPresent(DbReference.class)) {
					DbReference dbCharacteristicAnnotation = classToBind.getAnnotation(DbReference.class);
					LOGGER.debug("binding {} for Pojo {}", classToBind.getName(), dbCharacteristicAnnotation.beanType().getName());
					DB_REFERENCE_MAP.put(dbCharacteristicAnnotation.beanType(), (Class<? extends IDbReference>) classInfo.load());
				}

			}
		} catch (IOException e) {
			LOGGER.error("missing db references", e);
		}
	}

	@Override
	public T get() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		LOGGER.debug("trying to get DbReference for class: {}", stacktrace[2].getClassName());
		try {
			return (T) (DB_REFERENCE_MAP.get(callerClass)).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("error while instancing new dbreference");
		}
		return null;
	}
}
