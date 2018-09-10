<div>
    <p><strong>一开始同事的思路：研究分词算法，将用户输入的地址按省市县镇村分隔。</strong></p>
    <p>个人感觉：实现不了，因为用户输入并不是标准输入，例如标准地址为“江苏省南通市海安县李堡镇陈庄村**组**号”，但是用户
    的输入不可控，他可能输入“江苏南通海安....”，也许输入“江苏海安....”，可能多字，可能少字，可能错别字</p>
</div>
<div>
    <p><strong>我的实现思路：分词、倒排索引、文本相似度匹配</strong></p>
    <p>思路听起来很高大上，其实有现成的工具ElasticSearch（底层是Lucene全文搜索引擎）帮我们完成了几乎所有的工作。</p>
    <p><strong>问题分析抽象：</strong></p>
    <p>其实这个场景很清晰，我们就是要从一个地址中识别出各级别，但是这个地址本身不能够为我们提供这些标准数据。<br/>
        转化一下思路，我们可不可以拿用户输入的，去和标准的五级完整数据（例如江苏省南通市海安县李堡镇陈庄村）匹配？取匹配度最高的那一条，标准的那条对应的五级就很清晰明了了。<br/>
        例如用户输入“江苏省海安县李堡镇陈庄村8组88号”，发现和“江苏省南通市海安县李堡镇陈庄村”匹配度最高，而这条文档又可以提供出省、市、县、镇、村相关信息。
    </p>
   
    <p><strong>付诸实现：详见代码</strong></p>
<br/>
</div>


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

检索时，拿fullAddressName进行匹配，默认带出10条数据，相似度降序排列.例如键入"http://localhost:7070/searchAddress?addressName=海安李堡陈庄村十一组47号"时，结果默认展示10条，按相似度降序排列，用的时候一般用第一条，咋用看需求

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
	"fullAddressName": "天津市市辖区静海区独流镇十一堡村委会",
	"provAddrNo": "120000000000",
	"provAddr": "天津市",
	"cityAddrNo": "120100000000",
	"cityAddr": "市辖区",
	"areaAddrNo": "120118000000",
	"areaAddr": "静海区",
	"townAddrNo": "120118102000",
	"townAddr": "独流镇",
	"villageAddrNo": "120118102218",
	"villageAddr": "十一堡村委会"
     }, {
	"fullAddressName": "吉林省松原市扶余市三岔河镇西十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220781000000",
	"areaAddr": "扶余市",
	"townAddrNo": "220781100000",
	"townAddr": "三岔河镇",
	"villageAddrNo": "220781100211",
	"villageAddr": "西十一号村委会"
     }, {
	"fullAddressName": "吉林省松原市长岭县前七号镇东十一村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220722000000",
	"areaAddr": "长岭县",
	"townAddrNo": "220722104000",
	"townAddr": "前七号镇",
	"villageAddrNo": "220722104213",
	"villageAddr": "东十一村委会"
     }, {
	"fullAddressName": "河北省张家口市崇礼区狮子沟乡十一号村委会",
	"provAddrNo": "130000000000",
	"provAddr": "河北省",
	"cityAddrNo": "130700000000",
	"cityAddr": "张家口市",
	"areaAddrNo": "130709000000",
	"areaAddr": "崇礼区",
	"townAddrNo": "130709205000",
	"townAddr": "狮子沟乡",
	"villageAddrNo": "130709205216",
	"villageAddr": "十一号村委会"
     }, {
	"fullAddressName": "吉林省松原市扶余市三岔河镇东十一号村委会",
	"provAddrNo": "220000000000",
	"provAddr": "吉林省",
	"cityAddrNo": "220700000000",
	"cityAddr": "松原市",
	"areaAddrNo": "220781000000",
	"areaAddr": "扶余市",
	"townAddrNo": "220781100000",
	"townAddr": "三岔河镇",
	"villageAddrNo": "220781100210",
	"villageAddr": "东十一号村委会"
    }]