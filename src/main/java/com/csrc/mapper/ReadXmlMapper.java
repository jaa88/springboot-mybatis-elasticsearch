package com.csrc.mapper;

import com.csrc.Model.AttributeNode;

import java.util.List;

/**
 * Created by jianan on 2018/6/11.
 */
public interface ReadXmlMapper {
    public int importIntoDb(List<AttributeNode> list);
    public void deleteAll();
}
