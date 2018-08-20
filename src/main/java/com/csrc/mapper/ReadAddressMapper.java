package com.csrc.mapper;


import com.csrc.model.AddressNode;

import java.util.List;

/**
 * Created by jianan on 2018/6/11.
 */
public interface ReadAddressMapper {
    public int importIntoDb(List<AddressNode> list);

}
