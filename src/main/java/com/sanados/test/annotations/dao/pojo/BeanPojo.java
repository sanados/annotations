package com.sanados.test.annotations.dao.pojo;

import com.sanados.test.annotations.dao.IBean;
import com.sanados.test.annotations.dao.bean.Bean;
import com.sanados.test.annotations.structure.DbCharacteristic;
import com.sanados.test.annotations.structure.IBeanTransformation;
import com.sanados.test.annotations.structure.Saveable;

import java.util.Date;


/**
 * Created by sanados on 26.02.2016.
 * magic
 */
public class BeanPojo extends Saveable implements IBeanTransformation<IBean>, IBean {

	/**
	 * BEGIN definition of field and how to store in DB
	 * annotation decides filling of prepared statement
	 */
	@DbCharacteristic(position = {2, 5}, fieldType = DbCharacteristic.Type.STRING)
	private String title;

	@DbCharacteristic(position = {3, 6}, fieldType = DbCharacteristic.Type.DATE)
	private Date lastModified;

	@DbCharacteristic(position = {4, 7}, fieldType = DbCharacteristic.Type.INTEGER)
	private Integer viewCount;

	/**
	 * END definition of field and how to store in DB
	 */

	/**
	 * BEGIN Getter and Setter
	 */
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	@Override
	public void setLastModified(final Date lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public Integer getViewCount() {
		return viewCount;
	}

	@Override
	public void setViewCount(final Integer viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * END Getter and Setter
	 */


	/**
	 * transformation methods to be able to switch between bean and pojo
	 */
	@Override
	public Bean convertToBean() {
		/** TODO overkill all the things ... make generic convert to bean
		 *  all bean extend from BaseBean
		 *  all pojo extend Saveable
		 */
		return new Bean(this);
	}

	/**
	 * Enrich object with another object that implements the same interface
	 *
	 * @param loadingBean either bean or pojo
	 *                    <p>
	 *                    the function tries to load the incoming object from DWH with id from incoming object
	 *                    after that it overwrites all fields that are mentioned in the interface with the
	 *                    value from the incoming object
	 */
	@Override
	public void loadFromBean(final IBean loadingBean) {
		//try to load and set fields that are not in bean
		this.load(loadingBean.getId());

		//overwrite local fields with values from bean
		//hard coded ... would be doable by reflection and getDeclared Methods
		//we have enough overkill already ... maybe
		/** TODO overkill all the things ... make generic loadFromBean */
		this.setId(loadingBean.getId());
		this.setTitle(loadingBean.getTitle());
		this.setLastModified(loadingBean.getLastModified());
		this.setViewCount(loadingBean.getViewCount());
	}

}
