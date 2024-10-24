package com.briup.service.impl;

import com.briup.bean.Category;
import com.briup.bean.vo.CategoryVO;
import com.briup.dao.ICategoryDao;
import com.briup.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ICategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao categoryDao;

    @Override
    public List<CategoryVO> findAllCategoey() {
        List<CategoryVO> categoryVOS=new ArrayList<>();
        List<Category> categories=categoryDao.findByParentIdIsNull();
        for(Category category:categories){
            List<Category> cate=categoryDao.findByParentId(category.getId());
            CategoryVO categoryVO=new CategoryVO(category,cate);
            categoryVOS.add(categoryVO);
        }
        return categoryVOS;
    }
}
