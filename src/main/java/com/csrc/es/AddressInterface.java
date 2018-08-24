package com.csrc.es;

import com.csrc.model.AddressNode;

import java.util.List;

/**
 * Created by jianan on 2018/8/23.
 */
public interface AddressInterface {
    void saveEntity(AddressNode entity);

    void saveEntity(List<AddressNode> entityList);

    List<AddressNode> searchEntity(String searchContent);
}
