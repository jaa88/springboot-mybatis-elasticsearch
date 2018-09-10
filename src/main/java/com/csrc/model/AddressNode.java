package com.csrc.model;

/**
 * Created by jianan on 2018/8/23.
 */
public class AddressNode {
    public static final String INDEX_NAME = "address";

    public static final String TYPE = "address";

    private String fullAddressName;
    private String provAddrNo;
    private String provAddr;//省
    private String cityAddrNo;
    private String cityAddr;//市
    private String areaAddrNo;
    private String areaAddr;//区县
    private String townAddrNo;
    private String townAddr;//镇
    private String villageAddrNo;
    private String villageAddr;//村
    private Integer addressLevel;

    public AddressNode() {
        super();
    }

    public String getProvAddr() {
        return provAddr;
    }

    public void setProvAddr(String provAddr) {
        this.provAddr = provAddr;
    }

    public String getCityAddr() {
        return cityAddr;
    }

    public void setCityAddr(String cityAddr) {
        this.cityAddr = cityAddr;
    }

    public String getAreaAddr() {
        return areaAddr;
    }

    public void setAreaAddr(String areaAddr) {
        this.areaAddr = areaAddr;
    }

    public String getTownAddr() {
        return townAddr;
    }

    public void setTownAddr(String townAddr) {
        this.townAddr = townAddr;
    }

    public String getVillageAddr() {
        return villageAddr;
    }

    public void setVillageAddr(String villageAddr) {
        this.villageAddr = villageAddr;
    }

    public String getCityAddrNo() {
        return cityAddrNo;
    }

    public void setCityAddrNo(String cityAddrNo) {
        this.cityAddrNo = cityAddrNo;
    }

    public String getAreaAddrNo() {
        return areaAddrNo;
    }

    public void setAreaAddrNo(String areaAddrNo) {
        this.areaAddrNo = areaAddrNo;
    }

    public String getTownAddrNo() {
        return townAddrNo;
    }

    public void setTownAddrNo(String townAddrNo) {
        this.townAddrNo = townAddrNo;
    }

    public String getVillageAddrNo() {
        return villageAddrNo;
    }

    public void setVillageAddrNo(String villageAddrNo) {
        this.villageAddrNo = villageAddrNo;
    }

    public String getFullAddressName() {
        return fullAddressName;
    }

    public void setFullAddressName(String fullAddressName) {
        this.fullAddressName = fullAddressName;
    }

    public String getProvAddrNo() {
        return provAddrNo;
    }

    public void setProvAddrNo(String provAddrNo) {
        this.provAddrNo = provAddrNo;
    }

    public Integer getAddressLevel() {
        return addressLevel;
    }

    public void setAddressLevel(Integer addressLevel) {
        this.addressLevel = addressLevel;
    }
}
