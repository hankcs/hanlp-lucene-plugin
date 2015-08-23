hanlp-solr-plugin
========

HanLP中文分词solr插件
----------------------
基于HanLP，支持Solr5.x，兼容Lucene5.x。

## 快速上手
 0. 将[hanlp-portable.jar](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.hankcs%22%20AND%20a%3A%22hanlp%22)和[hanlp-solr-plugin.jar](https://github.com/hankcs/hanlp-solr-plugin/releases)共两个jar放入```${webapp}/WEB-INF/lib```下
 0. 修改solr core的配置文件```${core}/conf/schema.xml```：
 
    ```
<fieldType name="text_cn" class="solr.TextField">
    <analyzer type="index" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
    <analyzer type="query" enableIndexMode="true" class="com.hankcs.lucene.HanLPAnalyzer"/>
</fieldType>
    ```

## 调用方法
在Query改写的时候，可以利用HanLPAnalyzer分词结果中的词性等属性，如
```java
String text = "中华人民共和国很辽阔";
for (int i = 0; i < text.length(); ++i)
{
    System.out.print(text.charAt(i) + "" + i + " ");
}
System.out.println();
Analyzer analyzer = new HanLPAnalyzer();
TokenStream tokenStream = analyzer.tokenStream("field", text);
tokenStream.reset();
while (tokenStream.incrementToken())
{
    CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
    // 偏移量
    OffsetAttribute offsetAtt = tokenStream.getAttribute(OffsetAttribute.class);
    // 距离
    PositionIncrementAttribute positionAttr = kenStream.getAttribute(PositionIncrementAttribute.class);
    // 词性
    TypeAttribute typeAttr = tokenStream.getAttribute(TypeAttribute.class);
    System.out.printf("[%d:%d %d] %s/%s\n", offsetAtt.startOffset(), offsetAtt.endOffset(), positionAttr.getPositionIncrement(), attribute, typeAttr.type());
}
```
在另一些场景，支持以自定义的分词器（比如开启了命名实体识别的分词器、繁体中文分词器、CRF分词器等）构造HanLPTokenizer，比如：
```java
tokenizer = new HanLPTokenizer(HanLP.newSegment()
                                    .enableJapaneseNameRecognize(true)
                                    .enableIndexMode(true), null, false);
tokenizer.setReader(new StringReader("林志玲亮相网友:确定不是波多野结衣？"));
```

## 高级配置
 HanLP分词器主要通过class path下的```hanlp.properties```进行配置，请阅读[HanLP自然语言处理包文档](https://github.com/hankcs/HanLP)以了解更多相关配置，如：
 
0. 停用词
0. 用户词典
0. 词性标注 
0. ……

## 版权
 Apache License Version 2.0
