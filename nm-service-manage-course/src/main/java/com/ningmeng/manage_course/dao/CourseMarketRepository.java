package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.CourseMarket;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2020/2/21.
 */
public interface CourseMarketRepository extends MongoRepository<CourseMarket,String> {
}
