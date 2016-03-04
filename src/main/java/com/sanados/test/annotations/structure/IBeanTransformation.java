package com.sanados.test.annotations.structure;

/**
 * Created by sanados on 26.02.2016.
 * magic
 */
public interface IBeanTransformation<T extends IBaseBean> extends IBaseBean {
	T convertToBean();

	void loadFromBean(final T loadingBean);
}
