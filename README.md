hanlp-solr-plugin
========

HanLP中文分词solr插件
----------------------

 - 整合方法
 0. 将```hanlp-portable-${version}.jar```和```hanlp-solr-plugin-${version}.jar```共两个jar放入```${webapp}/WEB-INF/lib```下
 0. 修改solr core的配置文件```${core}/conf/schema.xml```：
    ```
     <fieldType name="text_cn" class="solr.TextField">
        <analyzer type="index" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
        <analyzer type="query" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
    </fieldType>
    ```

 - 配置方法
 HanLP分词器主要通过class path下的```hanlp.properties```进行配置，请阅读HanLP自然语言处理包文档以了解更多。
 
 - 版权
 Apache License Version 2.0
