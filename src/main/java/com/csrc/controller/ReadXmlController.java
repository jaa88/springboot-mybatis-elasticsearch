package com.csrc.controller;

import com.csrc.Model.AttributeNode;
import com.csrc.mapper.ReadXmlMapper;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianan on 2018/6/11.
 */
@RestController
public class ReadXmlController {
    @Autowired
    ReadXmlMapper readXmlMapper;

    @RequestMapping("/readXml")
    public void readXml(HttpServletResponse response) throws IOException, WriteException {
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=\"jiananSheet.xls\"");
        response.setContentType("application/msexcel");
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        WritableSheet sheet = workbook.createSheet("ServiceSheet",0);
        /*readXmlMapper.deleteAll();//先删光喽*///数据库导入暂时关闭
        String filePath="D:\\xmlResource";
        File file=new File(filePath);
        String[] workspaceNameList=file.list();
        List<AttributeNode> attributeNodeList=new ArrayList<AttributeNode>();
        for(int a=0;a<workspaceNameList.length;a++){
            File workspaceFile = new File(filePath + "\\" + workspaceNameList[a]);
            if(workspaceFile.isDirectory()){
                String[] projectNameList=workspaceFile.list();
                for(int b=0;b<projectNameList.length;b++){
                    File projectFile = new File(filePath + "\\" + workspaceNameList[a] + "\\" + projectNameList[b]);
                    String[] xmlFileList=projectFile.list();
                    for(int c=0;c<xmlFileList.length;c++){
                        String url=filePath + "\\" + workspaceNameList[a] + "\\" + projectNameList[b]+"\\"+xmlFileList[c];
                        String workspaceName=workspaceNameList[a];
                        String projectName=projectNameList[b];
                        String xmlName=xmlFileList[c];
                        attributeNodeList.addAll(doReadByServiceUrl(url,workspaceName,projectName,xmlName));
                        /*
                        if(attributeNodeList.size()>0){
                        readXmlMapper.importIntoDb(attributeNodeList);
                    }*///导入数据库的先关闭，而且需要改代码的
                }
                }
            }
        }
        sheet=createSheet(sheet,attributeNodeList);
        workbook.write();
        workbook.close();
        os.close();
    }

    public static WritableSheet createSheet(WritableSheet sheet,List<AttributeNode> attributeNodeList) throws WriteException {
        String[] sheetTitle={"workspaceName","projectName","xmlName","serviceNo","serviceName","出参入参","参数","参数简介","参数类型"};
        for(int i=0;i<sheetTitle.length;i++){
            Label titleCeil=new Label(i,0,sheetTitle[i]);
            sheet.addCell(titleCeil);
        }

        for(int i=0;i<attributeNodeList.size();i++){
            for(int j=0;j<9;j++){
                Label dataCeil=new Label(j,i+1,"");
                switch (j){
                    case 0:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getWorkspaceName());
                        break;
                    case 1:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getProjectName());
                        break;
                    case 2:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getXmlName());
                        break;
                    case 3:
                        dataCeil= new Label(j,i+1,String.valueOf(attributeNodeList.get(i).getServiceNo()));
                        break;
                    case 4:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getServiceName());
                        break;
                    case 5:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getAttributeMode());
                        break;
                    case 6:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getAttributeNodeName());
                        break;
                    case 7:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getAttributeTitle());
                        break;
                    case 8:
                        dataCeil= new Label(j,i+1,attributeNodeList.get(i).getAttributeType());
                        break;
                    default:
                        break;
                }
                sheet.addCell(dataCeil);
            }
        }
        return sheet;
    }


    public static List<AttributeNode> doReadByServiceUrl(String url,String workspaceName,String projectName,String xmlName){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<AttributeNode> attributeNodeList=new ArrayList<AttributeNode>();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            System.out.println("url:"+url);
            Document document = db.parse(url);
            NodeList serviceList = document.getElementsByTagName("service");
            int serviceNo=0;
            for (int i = 0; i < serviceList.getLength(); i++) {
                serviceNo++;
                String serviceName="";
                Node nodeService = serviceList.item(i);
                NamedNodeMap attrService = nodeService.getAttributes();
                for (int j = 0; j < attrService.getLength(); j++) {
                    Node attr = attrService.item(j);
                    if(attr.getNodeName().equals("name")){
                        serviceName=attr.getNodeValue();
                    }
                }
                NodeList childNodes = nodeService.getChildNodes();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE&&childNodes.item(k).getNodeName().equals("attribute")) {
                        AttributeNode attributeNode=new AttributeNode();
                        Node nodeAttribute=childNodes.item(k);
                        NamedNodeMap attrAttribute = nodeAttribute.getAttributes();
                        for(int z=0;z<attrAttribute.getLength();z++){
                            Node attr = attrAttribute.item(z);
                            if(attr==null){
                                continue;
                            }
                            if(attr.getNodeName().equals("name")){
                                attributeNode.setAttributeNodeName(attr.getNodeValue());
                            }
                            if(attr.getNodeName().equals("title")){
                                attributeNode.setAttributeTitle(attr.getNodeValue());
                            }
                            if(attr.getNodeName().equals("type")){
                                attributeNode.setAttributeType(attr.getNodeValue());
                            }
                            if(attr.getNodeName().equals("mode")){
                                attributeNode.setAttributeMode(attr.getNodeValue());
                            }
                            attributeNode.setServiceNo(serviceNo);
                            attributeNode.setServiceName(serviceName);
                            attributeNode.setXmlName(xmlName);
                            attributeNode.setWorkspaceName(workspaceName);
                            attributeNode.setProjectName(projectName);
                        }
                        attributeNodeList.add(attributeNode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributeNodeList;
    }
}
