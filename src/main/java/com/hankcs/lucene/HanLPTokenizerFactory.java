package com.hankcs.lucene;

import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

public class HanLPTokenizerFactory extends TokenizerFactory
{
    private boolean enableIndexMode;
    private boolean enablePorterStemming;

    public HanLPTokenizerFactory(Map<String, String> args)
    {
        super(args);
        enableIndexMode = getBoolean(args, "enableIndexMode", true);
        enablePorterStemming = getBoolean(args, "enablePorterStemming", false);
    }

    @Override
    public Tokenizer create(AttributeFactory factory)
    {
        return new HanLPTokenizer(HanLP.newSegment().enableOffset(true).enableIndexMode(enableIndexMode), null, enablePorterStemming);
    }
}