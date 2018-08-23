
将国家统计局爬取的地址数据，导入es中用于检索，原数据存放格式为：

     110000000000	北京市	1	省
     110100000000	市辖区	2	市
     110101000000	东城区	3	区县
     110101001000	东华门街道办事处	4	街道
     110101001001	多福巷社区居委会	5	村、社区
     110101001002	银闸社区居委会	5	村、社区
中间的间隔为空格或者tab，不太好分隔，首先在nodepad++中将所有的空格和tab都使用@@替换，这样可以使用@@分隔。处理后的格式为：

     110000000000@@北京市@@1@@省
     110100000000@@市辖区@@2@@市
     110101000000@@东城区@@3@@区县
     110101001000@@东华门街道办事处@@4@@街道
     110101001001@@多福巷社区居委会@@5@@村、社区
     110101001002@@银闸社区居委会@@5@@村、社区
     110101001005@@东厂社区居委会@@5@@村、社区
     110101001006@@智德社区居委会@@5@@村、社区

es中存储的数据结构为若干以下格式的文档，约67万条：

     {
        "fullAddressName": "北京市市辖区东城区东华门街道办事处黄图岗社区居委会",
        "provAddrNo": "﻿110000000000",
        "provAddr": "北京市",
        "cityAddrNo": "110100000000",
        "cityAddr": "市辖区",
        "areaAddrNo": "110101000000",
        "areaAddr": "东城区",
        "townAddrNo": "110101001000",
        "townAddr": "东华门街道办事处",
        "villageAddrNo": "110101001008",
        "villageAddr": "黄图岗社区居委会"
     }

检索时，拿fullAddressName进行匹配，默认带出10条数据，相似度降序排列.例如：http://localhost:7070/search?name=海安县李堡镇陈庄村十一组47号，结果默认展示10条，按相似度降序排列，用的时候一般用第一条，咋用看需求

    [{
	"fullAddressName": "江苏省南通市海安县李堡镇陈庄村委会",
	"provAddrNo": "320000000000",
	"provAddr": "江苏省",
	"cityAddrNo": "320600000000",
	"cityAddr": "南通市",
	"areaAddrNo": "320621000000",
	"areaAddr": "海安县",
	"townAddrNo": "320621103000",
	"townAddr": "李堡镇",
	"villageAddrNo": "320621103200",
	"villageAddr": "陈庄村委会"
    }, {
	"fullAddressName": "江苏省南通市海安县李堡镇陈庄村委会",
	"provAddrNo": "320000000000",
	"provAddr": "江苏省",
	"cityAddrNo": "320600000000",
	"cityAddr": "南通市",
	"areaAddrNo": "320621000000",
	"areaAddr": "海安县",
	"townAddrNo": "320621103000",
	"townAddr": "李堡镇",
	"villageAddrNo": "320621103200",
	"villageAddr": "陈庄村委会"
     }, {
	"fullAddressName": "吉林省松原市长岭县前七号镇西十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220722000000",
	"areaAddr": "长岭县",
	"townAddrNo": "220722104000",
	"townAddr": "前七号镇",
	"villageAddrNo": "220722104207",
	"villageAddr": "西十一号村委会"
     }, {
	"fullAddressName": "吉林省松原市长岭县前七号镇西十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220722000000",
	"areaAddr": "长岭县",
	"townAddrNo": "220722104000",
	"townAddr": "前七号镇",
	"villageAddrNo": "220722104207",
	"villageAddr": "西十一号村委会"
     }, {
	"fullAddressName": "吉林省长春市榆树市八号镇十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220100000000",
	"cityAddr": "长春市",
	"areaAddrNo": "220182000000",
	"areaAddr": "榆树市",
	"townAddrNo": "220182110000",
	"townAddr": "八号镇",
	"villageAddrNo": "220182110205",
	"villageAddr": "十一号村委会"
     }, {
	"fullAddressName": "吉林省长春市榆树市八号镇十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220100000000",
	"cityAddr": "长春市",
	"areaAddrNo": "220182000000",
	"areaAddr": "榆树市",
	"townAddrNo": "220182110000",
	"townAddr": "八号镇",
	"villageAddrNo": "220182110205",
	"villageAddr": "十一号村委会"
     } , {
	"fullAddressName": "吉林省松原市长岭县三十号乡三十一村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220722000000",
	"areaAddr": "长岭县",
	"townAddrNo": "220722209000",
	"townAddr": "三十号乡",
	"villageAddrNo": "220722209202",
	"villageAddr": "三十一村委会"
     }, {
	"fullAddressName": "吉林省松原市长岭县三十号乡三十一村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220722000000",
	"areaAddr": "长岭县",
	"townAddrNo": "220722209000",
	"townAddr": "三十号乡",
	"villageAddrNo": "220722209202",
	"villageAddr": "三十一村委会"
    }, {
  	"fullAddressName": "江苏省南通市海安县李堡镇李堡社区居委会",
	"provAddrNo": "320000000000",
	"provAddr": "江苏省",
	"cityAddrNo": "320600000000",
	"cityAddr": "南通市",
	"areaAddrNo": "320621000000",
	"areaAddr": "海安县",
	"townAddrNo": "320621103000",
	"townAddr": "李堡镇",
	"villageAddrNo": "320621103007",
	"villageAddr": "李堡社区居委会"
    }, {
	"fullAddressName": "江苏省南通市海安县李堡镇李堡社区居委会",
	"provAddrNo": "320000000000",
	"provAddr": "江苏省",
	"cityAddrNo": "320600000000",
	"cityAddr": "南通市",
	"areaAddrNo": "320621000000",
	"areaAddr": "海安县",
	"townAddrNo": "320621103000",
	"townAddr": "李堡镇",
	"villageAddrNo": "320621103007",
	"villageAddr": "李堡社区居委会"
    }]