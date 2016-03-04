package com.sanados.test.annotations.dao;

import com.sanados.test.annotations.structure.IBaseBean;

import java.util.Date;

/**
 * Created by sanados on 26.02.2016.
 * magic
 */
public interface IBean extends IBaseBean {
	String getTitle();

	void setTitle(final String title);

	Date getLastModified();

	void setLastModified(final Date lastModified);

	Integer getViewCount();

	void setViewCount(final Integer viewCount);
}
