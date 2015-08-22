hanlp-solr-plugin
========

HanLP中文分词solr插件
----------------------

## 整合方法
 0. 将[hanlp-portable-${version}.jar](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.hankcs%22%20AND%20a%3A%22hanlp%22)和```hanlp-solr-plugin-${version}.jar```共两个jar放入```${webapp}/WEB-INF/lib```下
 0. 修改solr core的配置文件```${core}/conf/schema.xml```：
    ```
     <fieldType name="text_cn" class="solr.TextField">
        <analyzer type="index" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
        <analyzer type="query" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
    </fieldType>
    ```

## 配置方法
 HanLP分词器主要通过class path下的```hanlp.properties```进行配置，请阅读[HanLP自然语言处理包文档](https://github.com/hankcs/HanLP)以了解更多。
 
## 版权
 Apache License Version 2.0
