package com.sanados.test.annotations.structure;

import java.io.Serializable;

/**
 * Created by sanados on 26.02.2016.
 * <p>
 * Base Bean of which all bean need to extend
 */
abstract public class BaseBean implements Serializable, IBaseBean {

	private Long id;

	public BaseBean() {
	}

	public BaseBean(final IBaseBean bean) {
		this.id = bean.getId();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}
}
