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
        <analyzer type="index">
            <tokenizer class="com.hankcs.lucene.HanLPTokenizerFactory" enableIndexMode="true"/>
        </analyzer>
        <analyzer type="query">
            <tokenizer class="com.hankcs.lucene.HanLPTokenizerFactory" enableIndexMode="false"/>
        </analyzer>
    </fieldType>
    ```

## 高级配置
 目前本插件支持如下基于```schema.xml```的配置:

| 配置项名称       | 功能   |  默认值  |
| --------   | -----:  | :----:  |
| enableIndexMode    | 设为索引模式 |   true     |
| enableCustomDictionary    | 是否启用用户词典 |   true     |
| customDictionaryPath    | 用户词典路径(绝对路径或程序可以读取的相对路径,多个词典用空格隔开) |   null     |
| stopWordDictionaryPath    | 停用词词典路径 |   null     |
| enableNumberQuantifierRecognize    | 是否启用数词和数量词识别 |   true     |
| enableNameRecognize    | 开启人名识别 |   true     |
| enableTranslatedNameRecognize    | 是否启用音译人名识别 |   false     |
| enableJapaneseNameRecognize    | 是否启用日本人名识别 |   false     |
| enableOrganizationRecognize    | 开启机构名识别 |   false     |
| enablePlaceRecognize    | 开启地名识别 |   false     |
| enableNormalization    | 是否执行字符正规化（繁体->简体，全角->半角，大写->小写） |   false     |
| enableTraditionalChineseMode    | 开启精准繁体中文分词 |   false     |
| enableDebug    | 开启除错模式 |   false     |

 更高级的配置主要通过class path下的```hanlp.properties```进行配置，请阅读[HanLP自然语言处理包文档](https://github.com/hankcs/HanLP)以了解更多相关配置，如：

0. 停用词
0. 用户词典
0. 词性标注
0. ……

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

## 版权
 Apache License Version 2.0
