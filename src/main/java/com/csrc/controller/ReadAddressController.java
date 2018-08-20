package com.csrc.controller;

import com.csrc.mapper.ReadAddressMapper;
import com.csrc.model.AddressNode;
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
}





/*@RestController
public class ReadAddressController {
    @RequestMapping("/readAddress")
    public void readAddress(){
        File file=new File("F:\\aa.txt");
        BufferedReader reader=null;
        String temp=null;
        int line=1;
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                System.out.println("line"+line+":"+temp);
                System.out.println("季安安");
                line++;
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
    }
}*/
