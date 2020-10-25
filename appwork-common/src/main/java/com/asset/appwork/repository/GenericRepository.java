package com.asset.appwork.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by karim on 10/19/20.
 * @classdesc this interface used for any repository need paging and sorting
 */
@NoRepositoryBean
public interface GenericRepository<T,IDT> extends PagingAndSortingRepository<T,IDT> {

}
