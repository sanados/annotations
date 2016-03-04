package com.sanados.test.annotations.dao.bean;

import com.sanados.test.annotations.dao.IBean;
import com.sanados.test.annotations.structure.BaseBean;

import java.util.Date;

/**
 * Created by sanados on 26.02.2016.
 * magic
 */
public class Bean extends BaseBean implements IBean {

	private String title;
	private Date lastModifield;
	private Integer viewCount;

	/**
	 * no argument bean constructor
	 */
	public Bean() {
	}

	/**
	 * Constructor to fill bean with data from another bean
	 *
	 * @param bean either bean or pojo
	 */
	public Bean(final IBean bean) {
		super(bean);
		this.title = bean.getTitle();
		this.lastModifield = bean.getLastModified();
		this.viewCount = bean.getViewCount();
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public Date getLastModified() {
		return this.lastModifield;
	}

	@Override
	public void setLastModified(Date lastModified) {
		this.lastModifield = lastModified;
	}


	@Override
	public Integer getViewCount() {
		return viewCount;
	}

	@Override
	public void setViewCount(final Integer viewCount) {
		this.viewCount = viewCount;
	}
}
