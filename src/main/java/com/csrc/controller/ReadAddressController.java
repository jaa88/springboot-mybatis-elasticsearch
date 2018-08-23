package com.csrc.controller;

import com.csrc.es.VillageInterfaceImpl;
import com.csrc.mapper.ReadAddressMapper;
import com.csrc.mapper.ReadVillageAddressMapper;
import com.csrc.model.AddressNode;
import com.csrc.model.VillageAddressNode;
import com.csrc.model.VillageAddressNodeEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jianan on 2018/8/20.
 */
@RestController
public class ReadAddressController {
    @Autowired
    ReadAddressMapper readAddressMapper;

    @Autowired
    ReadVillageAddressMapper readVillageAddressMapper;

    @Autowired
    VillageInterfaceImpl villageInterface;

    @RequestMapping("/readAddress")
    public void readAddress() throws InterruptedException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("处理开始："+df.format(new Date()));
        File file=new File("F:\\bb.txt");
        BufferedReader reader=null;
        String temp=null;

        List<AddressNode> list=new ArrayList<AddressNode>();
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                String[] tempArr=temp.split("@@");
                AddressNode node=new AddressNode();
                node.setNoNum(tempArr[0]);
                node.setAddressName(tempArr[1]);
                node.setAddressLevel(tempArr[2]);
                node.setAddressLevelName(tempArr[3]);
                list.add(node);
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
        System.out.println("处理结束："+df.format(new Date()));
        int lineNum=0;
        System.out.println("开始分段执行");
        while(lineNum<list.size()){
            System.out.println("开始linNum:"+lineNum);
            List<AddressNode> listTemp=list.subList(lineNum,(list.size()-lineNum>10000)?lineNum+10000:list.size());
            readAddressMapper.importIntoDb(listTemp);
            System.out.println("结束linNum:"+lineNum);
            Thread.sleep(4000);
            lineNum+=10000;
        }
    }


    @RequestMapping("/readVillageAddress")
    public void readVillageAddress() throws InterruptedException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("处理开始："+df.format(new Date()));
        File file=new File("F:\\bb.txt");
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

        List<VillageAddressNodeEs> list=new ArrayList<VillageAddressNodeEs>();
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
                }else if(tempArr[2].equals("4")){
                    townAddr=tempArr[1];
                    townAddrNo=tempArr[0];
                }else {
                    villageAddr=tempArr[1];
                    villageAddrNo=tempArr[0];

                    VillageAddressNodeEs node=new VillageAddressNodeEs();
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
                    node.setFullAddressName(provAddr+cityAddr+areaAddr+townAddr+tempArr[1]);
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
            List<VillageAddressNodeEs> listTemp=list.subList(lineNum,(list.size()-lineNum>10000)?lineNum+10000:list.size());
            //readVillageAddressMapper.importIntoDb(listTemp);//存入结构化数据库中，但是暂时用不到
            villageInterface.saveEntity(listTemp);
            System.out.println("结束linNum:"+lineNum);
            Thread.sleep(5000);
            lineNum+=10000;
        }
        System.out.println("处理结束："+df.format(new Date()));
    }
}
