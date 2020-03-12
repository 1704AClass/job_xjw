package com.ningmeng.manage_course.dao;

import com.ningmeng.manage_course.service.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2020/2/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void testFeign()
    {
        cmsPageClient.findById( "5a754adf6abb500ad05688d9" );
    }
}
