/*
 * <summary></summary>
 * <author>hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/10/6 18:51</create-date>
 *
 * <copyright file="SegmentWrapper.java">
 * Copyright (c) 2003-2015, hankcs. All Right Reserved, http://www.hankcs.com/
 * </copyright>
 */
package com.hankcs.lucene;

import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @author hankcs
 */
public class SegmentWrapper
{
    BufferedReader br;
    Segment segment;
    /**
     * 因为next是单个term出去的，所以在这里做一个记录
     */
    Term[] termArray;
    /**
     * termArray下标
     */
    int index;

    public SegmentWrapper(BufferedReader br, Segment segment)
    {
        this.br = br;
        this.segment = segment;
    }

    /**
     * 重置分词器
     *
     * @param br
     */
    public void reset(BufferedReader br)
    {
        this.br = br;
        termArray = null;
        index = 0;
    }

    public Term next() throws IOException
    {
        if (termArray != null && index < termArray.length) return termArray[index++];
        String line = br.readLine();
        while (isBlank(line))
        {
            if (line == null) return null;
            line = br.readLine();
        }

        List<Term> termList = segment.seg(line);
        if (termList.size() == 0) return null;
        termArray = termList.toArray(new Term[0]);
        index = 0;

        return termArray[index++];
    }

    /**
     * 判断字符串是否为空（null和空格）
     *
     * @param cs
     * @return
     */
    private static boolean isBlank(CharSequence cs)
    {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
        {
            return true;
        }
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(cs.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
}
