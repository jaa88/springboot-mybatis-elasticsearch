package com.csrc.es;

import com.csrc.model.VillageAddressNodeEs;

import java.util.List;

/**
 * Created by jianan on 2018/8/23.
 */
public interface VillageInterface {
    void saveEntity(VillageAddressNodeEs entity);

    void saveEntity(List<VillageAddressNodeEs> entityList);

    List<VillageAddressNodeEs> searchEntity(String searchContent);
}
