package com.csrc.mapper;


import com.csrc.model.VillageAddressNodeEs;

import java.util.List;

/**
 * Created by jianan on 2018/6/11.
 */
public interface ReadVillageAddressMapper {
    public int importIntoDb(List<VillageAddressNodeEs> list);

}
