package com.briup.dao;

import com.briup.bean.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ICategoryDao{
    List<Category> findByParentIdIsNull();
    List<Category> findByParentId(Long id);
}
