/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   EMPExtText.java

package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.KeyedCollection;

// Referenced classes of package com.ecc.emp.ext.tag.field:
//            EMPExtFieldBase

/**
 * 反编译：
 * 
 * 	text输入框 dataType为数值类型时，则样式居右
 *  add by JackYu at 2011年4月12日14:18:10  
 * 
 */
public class EMPExtText extends EMPExtFieldBase
{

    public EMPExtText()
    {
        onfocus = null;
        onblur = null;
        onchange = null;
        onselect = null;
        autocomplete = null;
        maxlength = null;
        minlength = null;
        size = null;
    }

    protected String getType()
    {
        return "Text";
    }

    protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl)
    {
        sb.append("<input");
        writeAttribute(sb, "name", id);
        writeAttribute(sb, "value", getValue(kColl));
        writeAttribute(sb, "maxlength", maxlength);
        writeAttribute(sb, "size", size);
        
        if(cssElementClass != null && cssElementClass.length() > 0)
            writeAttribute(sb, "class", cssElementClass);
        else if(dataType == null)
            writeAttribute(sb, "class", "emp_field_text_input");
        
        //数值类型 样式居右
        else if(dataType.equals("Currency")||dataType.equals("Rate")||dataType.equals("Rate4Month")
				||dataType.equals("Percent")||dataType.equals("Percent2")||dataType.equals("Permille")
				||dataType.equals("Short")||dataType.equals("Int")||dataType.equals("Long")
				||dataType.equals("Float")||dataType.equals("Double")||dataType.equals("Decimal5")
				||dataType.equals("DecimalA"))
        	writeAttribute(sb, "class", "emp_field_text_input_number");
        
        addDefaultAttributes(sb, kColl);
        writeAttribute(sb, "onblur", onblur);
        writeAttribute(sb, "onchange", onchange);
        writeAttribute(sb, "onfocus", onfocus);
        writeAttribute(sb, "onselect", onselect);
        writeAttribute(sb, "autocomplete", autocomplete);
        sb.append("/>");
    }

    protected void renderOtherAttribute(StringBuffer sb, KeyedCollection kColl, boolean flat)
    {
        writeAttribute(sb, "maxlength", maxlength);
        writeAttribute(sb, "minlength", minlength);
    }

    public Object clone()
    {
        EMPExtText tag = new EMPExtText();
        cloneDafaultAttributes(tag);
        tag.maxlength = maxlength;
        tag.minlength = minlength;
        tag.size = size;
        tag.onfocus = onfocus;
        tag.onblur = onblur;
        tag.onchange = onchange;
        tag.onselect = onselect;
        tag.autocomplete = autocomplete;
        return tag;
    }

    public String getAutocomplete()
    {
        return autocomplete;
    }

    public void setAutocomplete(String autocomplete)
    {
        this.autocomplete = autocomplete;
    }

    public String getMaxlength()
    {
        return maxlength;
    }

    public void setMaxlength(String maxlength)
    {
        this.maxlength = maxlength;
    }

    public String getMinlength()
    {
        return minlength;
    }

    public void setMinlength(String minlength)
    {
        this.minlength = minlength;
    }

    public String getOnblur()
    {
        return onblur;
    }

    public void setOnblur(String onblur)
    {
        this.onblur = onblur;
    }

    public String getOnchange()
    {
        return onchange;
    }

    public void setOnchange(String onchange)
    {
        this.onchange = onchange;
    }

    public String getOnfocus()
    {
        return onfocus;
    }

    public void setOnfocus(String onfocus)
    {
        this.onfocus = onfocus;
    }

    public String getOnselect()
    {
        return onselect;
    }

    public void setOnselect(String onselect)
    {
        this.onselect = onselect;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    private static final long serialVersionUID = 1L;
    protected String onfocus;
    protected String onblur;
    protected String onchange;
    protected String onselect;
    protected String autocomplete;
    protected String maxlength;
    protected String minlength;
    protected String size;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\WorkSpace_TJ\cmis-main\WebContent\WEB-INF\lib\emp2.2-101210.jar
	Total time: 154 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/