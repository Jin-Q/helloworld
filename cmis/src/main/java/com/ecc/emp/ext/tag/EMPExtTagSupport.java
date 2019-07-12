/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.ecc.emp.ext.tag;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.web.jsptags.EMPTagSupport;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

public abstract class EMPExtTagSupport extends EMPTagSupport
{

    public EMPExtTagSupport()
    {
    }
    
    /**
     * 取dicname的值
     * 
     * modify by JackYu
     * 添加：
     * 	支持带过滤条件的dicname
     * 
     *  过滤条件STD_ZB_TIME_TYPE?enname in (001,002,003)
     * 		   STD_ZB_TIME_TYPE?enname not in (004)
     * 
     * @param dictname
     * @return
     */
    protected IndexedCollection getDictMapCollection(String dictname)
    {
        IndexedCollection iColl = null;
        boolean isFilter = false;
        String filterStr = null;
        
        //判断dicname是否带过滤条件
        if(dictname != null && dictname.indexOf("?")!=-1){
        	isFilter = true;
        	
        	filterStr = dictname.substring(dictname.indexOf("?")+1);
        	dictname = dictname.substring(0,dictname.indexOf("?"));
        }
        
        try
        {
            iColl = (IndexedCollection)getDataElement(dictname);
        }
        catch(Exception exception) { }
        if(iColl == null)
            try
            {
                iColl = (IndexedCollection)getDataElement((new StringBuilder(String.valueOf(ATTR_DICTDATANAME))).append(".").append(dictname).toString());
            }
            catch(Exception exception1) { }
            
        try {
        	//如果带过滤条件，则过滤数据
        	if(isFilter){
        		iColl = filterCollection(iColl, filterStr);
        	}
        	
		} catch (Exception e) {
			return iColl;
		} 
            
        return iColl;
    }
    
    /**
     * 根据过滤条件过滤IndexedCollection
     * 
     * 过滤条件STD_ZB_TIME_TYPE?enname in (001,002,003)
     * 		   STD_ZB_TIME_TYPE?enname not in (004)
     * 
     * @param iColl 数据源
     * @param filterStr 过滤条件
     * @return 过滤后的IndexedCollection
     */
    private IndexedCollection filterCollection(IndexedCollection iColl, String filterStr){
    	if(iColl == null) return null;
    	IndexedCollection retIColl = new IndexedCollection();
    	try {
        	boolean filterModel = false; //true- in 包含； false - not不包含
        	String filterName = null;//过滤字段名
        	String filterValue = null;//过滤值
        	
        	String filterNamePatter = "[E|e][N|n][N|n][A|a][M|m][E|e]|[C|c][N|n][N|n][A|a][M|m][E|e]";
        	String filterModelInPatter = "[I|i][N|n]";
        	String filterModelNotInPatter = "[N|n][O|o][T|t]\\s*[I|i][N|n]";
        	String filterValuePatter = "\\(.*\\)";
        	
        	String patterStr = "("+filterNamePatter+")\\s*(" +filterModelInPatter+" | "+filterModelNotInPatter+")\\s*(" +filterValuePatter+")";
        	
        	Pattern p = Pattern.compile(patterStr);
    		Matcher m = p.matcher(filterStr);
    		
    		if(m.find()){
    			filterName = m.group(1);//过滤字段名
    			filterModel = m.group(2).contains("not")?false:true; //true- in 包含； false - not不包含
    			filterValue = m.group(3);//过滤值
    		}
    		
    		//包含
    		if(filterModel){
    			for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
    				KeyedCollection kColl = iterator.next();
    				String value = (String)kColl.getDataValue(filterName.toLowerCase());
    				if(filterValue!=null && filterValue.contains(value)){
    					retIColl.add(kColl);
    				}
    			}
    		//不包含	
    		}else{
    			for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
    				KeyedCollection kColl = iterator.next();
    				String value = (String)kColl.getDataValue(filterName.toLowerCase());
    				if(filterValue!=null && !filterValue.contains(value)){
    					retIColl.add(kColl);
    				}
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return retIColl;
    }
    
    protected String getDecoratedValue(String value, String dictname, boolean languageResouce)
    {
        String decoratedValue;
        if(value == null || dictname == null)
            return value;
        decoratedValue = value;
        IndexedCollection iColl;
        iColl = getDictMapCollection(dictname);
        if(iColl == null)
        {
            EMPLog.log("CMIS", EMPLog.WARNING, 0, (new StringBuilder("\u4F7F\u7528\u5B57\u5178\u4FEE\u9970\u6570\u636E\u65F6\u51FA\u9519\uFF01\u4E0D\u5B58\u5728\u540D\u4E3A")).append(dictname).append("\u7684\u5B57\u5178\u9879\uFF01").toString());
            return value;
        }
        try
        {
            String language = getLanguage();
            for(int i = 0; i < iColl.size(); i++)
            {
                KeyedCollection kColl = (KeyedCollection)iColl.get(i);
                String enname = (String)kColl.getDataValue(ATTR_ENNAME);
                if(!value.equals(enname))
                    continue;
                String cnname = null;
                if(languageResouce)
                    try
                    {
                        cnname = (String)kColl.getDataValue((new StringBuilder(String.valueOf(ATTR_CNNAME))).append("_").append(language).toString());
                    }
                    catch(Exception exception) { }
                if(cnname == null)
                    cnname = (String)kColl.getDataValue(ATTR_CNNAME);
                decoratedValue = cnname;
                break;
            }

        }
        catch(Exception e)
        {
            EMPLog.log("CMIS", EMPLog.WARNING, 0, "\u4F7F\u7528\u5B57\u5178\u4FEE\u9970\u6570\u636E\u65F6\u51FA\u9519\uFF01", e);
        }
        return decoratedValue;
    }

    protected boolean judgeReadPermission(String fieldId)
    {
        String canread = (String)pageContext.getRequest().getAttribute("canread");
        if(canread == null)
            return true;
        String canreads[] = canread.split(";");
        for(int i = 0; i < canreads.length; i++)
            if(canreads[i].equals(fieldId))
                return true;

        return false;
    }

    public boolean judgeWritePermission(String fieldId)
    {
        String canwrite = (String)pageContext.getRequest().getAttribute("canwrite");
        if(canwrite == null)
            return true;
        String canwrites[] = canwrite.split(";");
        for(int i = 0; i < canwrites.length; i++)
            if(canwrites[i].equals(fieldId))
                return true;

        return false;
    }

    protected void writeAttribute(StringBuffer buffer, String name, Object value)
    {
        if(value != null)
        {
            String valueStr = value.toString();
            if(valueStr != null)
            {
                valueStr = valueStr.replaceAll("'", "&#39;");
                buffer.append(" ").append(name).append("='").append(valueStr).append("'");
            }
        }
    }

    public static String ATTR_DICTDATANAME = "dictColl";
    public static String ATTR_ENNAME = "enname";
    public static String ATTR_CNNAME = "cnname";

}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\WorkSpace_TJ\cmis-main\WebContent\WEB-INF\lib\emp2.2-101210.jar
	Total time: 109 ms
	Jad reported messages/errors:
Couldn't resolve all exception handlers in method getDecoratedValue
	Exit status: 0
	Caught exceptions:
*/