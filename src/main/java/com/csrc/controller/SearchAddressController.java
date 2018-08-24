package com.csrc.controller;

import com.csrc.es.AddressInterfaceImpl;
import com.csrc.model.AddressNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jianan on 2018/8/23.
 */
@RestController
public class SearchAddressController {
    @Autowired
    AddressInterfaceImpl villageInterface;

    @RequestMapping(value="/searchAddress", method=RequestMethod.GET)
    public List<AddressNode> search(String addressName) {
        List<AddressNode> entityList = null;
        if(StringUtils.isNotEmpty(addressName)) {
            entityList = villageInterface.searchEntity(addressName);
        }
        return entityList;
    }
}
