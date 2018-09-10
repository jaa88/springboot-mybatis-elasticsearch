package com.csrc.controller;

import com.csrc.model.AddressNode;
import com.csrc.service.AddressService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jianan on 2018/9/3.
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @RequestMapping(value="/searchAddress", method= RequestMethod.GET)
    public List<AddressNode> searchWithGet(String addressName) throws IOException {
        List<AddressNode> entityList = null;
        if(StringUtils.isNotEmpty(addressName)) {
            entityList = addressService.searchEntity(addressName);
        }
        return entityList;
    }

    @RequestMapping(value="/searchAddress", method= RequestMethod.POST)
    public List<AddressNode> searchAddressWithPost(@RequestBody Map<String,String> map) throws IOException {
        List<AddressNode> entityList = null;
        if(StringUtils.isNotEmpty(map.get("addressName"))) {
            entityList = addressService.searchEntity(map.get("addressName"));
            //若返回的十条数据中有三级四级地址，将它们提到前面，底层使用的归并排序，不会破坏三级、四级、五级各自内部初始顺序
            Collections.sort(entityList,new Comparator<AddressNode>(){
                @Override
                public int compare(AddressNode o1, AddressNode o2) {
                    return o1.getAddressLevel()-o2.getAddressLevel();
                }
            });
        }
        return entityList;
    }

    @RequestMapping(value = "/deleteEsIndex",method = RequestMethod.GET)
    public String deleteIndex(String index) {
        return addressService.deleteIndex(index);
    }

    @RequestMapping("/readAddress")
    public void readVillageAddress() throws InterruptedException, FileNotFoundException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("处理开始："+df.format(new Date()));
        File file= ResourceUtils.getFile("classpath:address.txt");
        BufferedReader reader=null;
        String temp=null;
        String provAddrNo="";
        String cityAddrNo="";
        String areaAddrNo="";
        String townAddrNo="";
        String villageAddrNo="";
        String provAddr="";
        String cityAddr="";
        String areaAddr="";
        String townAddr="";
        String villageAddr="";

        List<AddressNode> list=new ArrayList<AddressNode>();
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                String[] tempArr=temp.split("@@");
                if(tempArr[2].equals("1")){
                    provAddr=tempArr[1];
                    provAddrNo=tempArr[0];
                }else if(tempArr[2].equals("2")){
                    cityAddr=tempArr[1];
                    cityAddrNo=tempArr[0];
                }else if(tempArr[2].equals("3")){
                    areaAddr=tempArr[1];
                    areaAddrNo=tempArr[0];

                    AddressNode node=new AddressNode();
                    node.setAreaAddr(areaAddr);
                    node.setAreaAddrNo(areaAddrNo);
                    node.setCityAddr(cityAddr);
                    node.setCityAddrNo(cityAddrNo);
                    node.setProvAddr(provAddr);
                    node.setProvAddrNo(provAddrNo);
                    node.setFullAddressName(provAddr+cityAddr+areaAddr);//组装3级全地址，例如江苏省南通市海安县
                    list.add(node);

                }else if(tempArr[2].equals("4")){
                    townAddr=tempArr[1];
                    townAddrNo=tempArr[0];

                    AddressNode node=new AddressNode();
                    node.setAreaAddr(areaAddr);
                    node.setAreaAddrNo(areaAddrNo);
                    node.setCityAddr(cityAddr);
                    node.setCityAddrNo(cityAddrNo);
                    node.setProvAddr(provAddr);
                    node.setProvAddrNo(provAddrNo);
                    node.setTownAddr(townAddr);
                    node.setTownAddrNo(townAddrNo);
                    node.setFullAddressName(provAddr+cityAddr+areaAddr+townAddr);//组装4级全地址，例如江苏省南通市海安县李堡镇
                    list.add(node);
                }else {
                    villageAddr=tempArr[1];
                    villageAddrNo=tempArr[0];
                    AddressNode node=new AddressNode();
                    node.setAreaAddr(areaAddr);
                    node.setAreaAddrNo(areaAddrNo);
                    node.setCityAddr(cityAddr);
                    node.setCityAddrNo(cityAddrNo);
                    node.setProvAddr(provAddr);
                    node.setProvAddrNo(provAddrNo);
                    node.setTownAddr(townAddr);
                    node.setTownAddrNo(townAddrNo);
                    node.setVillageAddr(villageAddr);
                    node.setVillageAddrNo(villageAddrNo);
                    node.setFullAddressName(provAddr+cityAddr+areaAddr+townAddr+villageAddr);//数据库中存储的是5级全地址
                    list.add(node);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(reader!=null){
                try{
                    reader.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("处理完成，共有多少数据："+list.size());
        int lineNum=0;
        System.out.println("开始分段执行");
        while(lineNum<list.size()){
            System.out.println("开始linNum:"+lineNum);
            List<AddressNode> listTemp=list.subList(lineNum,(list.size()-lineNum>500)?lineNum+500:list.size());
            //addressMapper.importIntoDb(listTemp);//存入结构化数据库中，但是暂时用不到
            addressService.saveEntity(listTemp);//存入es中
            System.out.println("结束linNum:"+lineNum);
            lineNum+=500;//分段执行，以免outOfMemory
            Thread.sleep(1000);//给es或数据库一个缓冲时间
        }
        System.out.println("处理结束："+df.format(new Date()));
    }
}
