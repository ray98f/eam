package com.wzmtr.eam;

import com.wzmtr.eam.dto.req.fault.CompareRowsReqDTO;
import com.wzmtr.eam.service.fault.FaultQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;

/**
 * Author: Li.Wang
 * Date: 2023/10/13 10:22
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FaultTest {
    @Autowired
    private FaultQueryService faultQueryService;
    @Test
    public void Test() {
        CompareRowsReqDTO compareRowsReqDTO = new CompareRowsReqDTO();
        HashSet<String> strings = new HashSet<>();
        strings.add("GZ2310120001");
        strings.add("GZ2310120003");
        compareRowsReqDTO.setFaultNos(strings);
        System.out.println(faultQueryService.compareRows(compareRowsReqDTO));
    }
}
