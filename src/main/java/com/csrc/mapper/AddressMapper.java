package com.csrc.mapper;


import com.csrc.model.AddressNode;

import java.util.List;

public interface AddressMapper {
    public int importIntoDb(List<AddressNode> list);

}
