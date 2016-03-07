package org.ryan.nclcs.vce.service;

import java.io.Serializable;

import org.ryan.nclcs.vce.dao.INclcsVceServiceBaseDAO;
import org.springframework.transaction.annotation.Transactional;


public abstract class NclcsVceServiceBaseServiceImpl <T, PK extends Serializable, K extends INclcsVceServiceBaseDAO<T,PK>> implements INclcsVceServiceBaseService<T, PK> {
	
	/**
	 * 取得当前DAO
	 */
	protected abstract K getCurrentDAO();
	
	public T get(PK id) {
		return (T) getCurrentDAO().get(id);
	}
	
	
	public T save(final T entity) {
		return (T) getCurrentDAO().save(entity);
	}
	
	
	public T update(final T entity) {
		return (T) getCurrentDAO().update(entity);
	}
	
	
	public void delete(final T entity) {
		getCurrentDAO().delete(entity);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(final PK id) {
		getCurrentDAO().delete(id);
	}

}
