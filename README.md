hanlp-lucene-plugin
========

HanLP中文分词Lucene插件
----------------------
基于HanLP，支持包括Solr在内的任何基于Lucene的系统。

## Maven

```
    <dependency>
      <groupId>com.hankcs.nlp</groupId>
      <artifactId>hanlp-lucene-plugin</artifactId>
      <version>1.1.2</version>
    </dependency>
```

## Solr快速上手
 1. 将[hanlp-portable.jar](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.hankcs%22%20AND%20a%3A%22hanlp%22)和[hanlp-lucene-plugin.jar](https://github.com/hankcs/hanlp-lucene-plugin/releases)共两个jar放入```${webapp}/WEB-INF/lib```下
 1. 修改solr core的配置文件```${core}/conf/schema.xml```：
 
    ```
    <!-- 默认文本类型: 指定使用HanLP分词器，同时开启索引模式。
	 通过solr自带的停用词过滤器，使用"stopwords.txt"（默认空白）过滤。
	 在搜索的时候，还支持solr自带的同义词词典。-->
    <fieldType name="text_general" class="solr.TextField" positionIncrementGap="100">
      <analyzer type="index">
        <tokenizer class="com.hankcs.lucene.HanLPTokenizerFactory" enableIndexMode="true"/>
        <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
        <!-- 取消注释可以启用索引期间的同义词词典
        <filter class="solr.SynonymFilterFactory" synonyms="index_synonyms.txt" ignoreCase="true" expand="false"/>
        -->
        <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
      <analyzer type="query">
        <tokenizer class="com.hankcs.lucene.HanLPTokenizerFactory" enableIndexMode="false"/>
        <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
        <filter class="solr.SynonymFilterFactory" synonyms="synonyms.txt" ignoreCase="true" expand="true"/>
        <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
    </fieldType>
    ```
 * solr允许为不同的字段指定不同的分词器，由于绝大部分默认字段都是text_general类型的，可以说这种做法比较适合新手。如果你是solr老手的话，你可能会更喜欢单独为不同的字段指定不同的分词器及其他配置。如果你的业务系统中有其他字段，比如location，summary之类，也需要一一指定其type="text_general"。切记，否则这些字段仍旧是solr默认分词器，会造成这些字段“搜索不到”。
 * 另外，切记不要在query中开启indexMode，否则会影响PhaseQuery。indexMode只需在index中开启一遍即可。

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
