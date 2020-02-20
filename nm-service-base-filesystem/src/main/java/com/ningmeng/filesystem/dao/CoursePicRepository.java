package com.ningmeng.filesystem.dao;

import com.ningmeng.framework.domain.course.CoursePic;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2020/2/21.
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    @Query(value = "select * from course_pic where courseid=#{courseid}",nativeQuery = true)
    CoursePic findOne(@Param( "courseid" ) String courseid);

    @Query(value = "delete from course_pic where  courseid=#{courseId}",nativeQuery =true )
    long deleteByCourseid(@Param( "courseId" ) String courseId);
}
