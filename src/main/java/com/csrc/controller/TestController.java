package com.csrc.controller;

import com.csrc.es.VillageInterfaceImpl;
import com.csrc.model.VillageAddressNodeEs;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianan on 2018/8/23.
 */
@RestController
public class TestController {
    @Autowired
    VillageInterfaceImpl villageInterface;

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public List<VillageAddressNodeEs> search(String name) {
        List<VillageAddressNodeEs> entityList = null;
        if(StringUtils.isNotEmpty(name)) {
            entityList = villageInterface.searchEntity(name);
        }
        return entityList;
    }
}
