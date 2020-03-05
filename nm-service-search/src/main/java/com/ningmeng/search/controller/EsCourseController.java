package com.ningmeng.search.controller;

import com.ningmeng.api.searchapi.EsCourseControllerApi;
import com.ningmeng.framework.domain.response.QueryResponseResult;
import com.ningmeng.framework.domain.search.CourseSearchParam;
import com.ningmeng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by 1 on 2020/2/28.
 */
@RestController
@RequestMapping("/search")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;


    @GetMapping("/course/{page}/{size}")
    @Override
    public QueryResponseResult list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) throws IOException {
        return esCourseService.list(page,size,courseSearchParam);
    }
}
