package com.sanados.test.annotations.structure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by sanados on 26.02.2016.
 * magic
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbCharacteristic {

	int[] position() default {1};

	Type fieldType() default Type.LONG;

	enum Type {
		STRING,
		INTEGER,
		LONG,
		DATE
	}

}
