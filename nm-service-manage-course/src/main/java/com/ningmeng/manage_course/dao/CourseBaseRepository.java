package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {


}
