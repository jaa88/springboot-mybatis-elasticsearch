package com.csrc.Model;

import java.io.Serializable;

/**
 * Created by jianan on 2018/6/11.
 */
public class AttributeNode  implements Serializable{

    private static final long serialVersionUID = 2630099167052632508L;
    private String workspaceName;
    private String projectName;
    private String xmlName;
    private int serviceNo;
    private String serviceName;
    private String serviceTitle;
    private String attributeNodeName;
    private String attributeTitle;
    private String attributeType;
    private String attributeMode;

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAttributeMode() {
        return attributeMode;
    }

    public void setAttributeMode(String attributeMode) {
        this.attributeMode = attributeMode;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getAttributeNodeName() {
        return attributeNodeName;
    }

    public void setAttributeNodeName(String attributeNodeName) {
        this.attributeNodeName = attributeNodeName;
    }

    public String getAttributeTitle() {
        return attributeTitle;
    }

    public void setAttributeTitle(String attributeTitle) {
        this.attributeTitle = attributeTitle;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }
}
