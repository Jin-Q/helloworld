/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.ecc.emp.ext.tag.field;

import com.ecc.emp.data.KeyedCollection;

// Referenced classes of package com.ecc.emp.ext.tag.field:
//            EMPExtFieldBase

public class EMPExtPop extends EMPExtFieldBase
{

    public EMPExtPop()
    {
        url = null;
        returnMethod = null;
        buttonLabel = "选择";
        popParam = "height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
        dataMapping = null;
        cssButtonClass = "emp_field_pop_button";
    }

    protected String getType()
    {
        return "Pop";
    }

    protected void renderInnerHtml(StringBuffer sb, KeyedCollection kColl)
    {
        sb.append("<input");
        writeAttribute(sb, "name", id);
        writeAttribute(sb, "value", getValue(kColl));
        if(cssElementClass != null && cssElementClass.length() > 0)
            writeAttribute(sb, "class", cssElementClass);
        else
            writeAttribute(sb, "class", "emp_field_pop_input");
        sb.append("/><button");
        writeAttribute(sb, "tabindex", tabindex);
        writeAttribute(sb, "class", cssButtonClass);
        sb.append(">").append(getResourceValue(buttonLabel)).append("</button>");
    }

    protected void renderOtherAttribute(StringBuffer buffer, KeyedCollection kColl, boolean flat)
    {
        String url = this.url;
        if(reqParams != null)
            if(url.indexOf('?') != -1)
                url = (new StringBuilder(String.valueOf(url))).append("&").append(reqParams).toString();
            else
                url = (new StringBuilder(String.valueOf(url))).append("?").append(reqParams).toString();
        url = parsePamaterStr(url);
        url = getGetURL(url);
        writeAttribute(buffer, "url", url);
        writeAttribute(buffer, "canwrite", this.isCanwrite());
        writeAttribute(buffer, "returnMethod", returnMethod);
        writeAttribute(buffer, "popParam", popParam);
        writeAttribute(buffer, "dataMapping", dataMapping);
    }

    public Object clone()
    {
        EMPExtPop tag = new EMPExtPop();
        cloneDafaultAttributes(tag);
        tag.url = url;
        tag.returnMethod = returnMethod;
        tag.popParam = popParam;
        tag.dataMapping = dataMapping;
        tag.cssButtonClass = cssButtonClass;
        tag.reqParams = reqParams;
        tag.buttonLabel = buttonLabel;
        return tag;
    }

    public void setPopParam(String popParam)
    {
        this.popParam = popParam;
    }

    public void setReturnMethod(String returnMethod)
    {
        this.returnMethod = returnMethod;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDataMapping()
    {
        return dataMapping;
    }

    public void setDataMapping(String dataMapping)
    {
        this.dataMapping = dataMapping;
    }

    public String getPopParam()
    {
        return popParam;
    }

    public String getReturnMethod()
    {
        return returnMethod;
    }

    public String getUrl()
    {
        return url;
    }

    public String getCssButtonClass()
    {
        return cssButtonClass;
    }

    public void setCssButtonClass(String cssButtonClass)
    {
        this.cssButtonClass = cssButtonClass;
    }

    public String getReqParams()
    {
        return reqParams;
    }

    public void setReqParams(String reqParams)
    {
        this.reqParams = reqParams;
    }

    public String getButtonLabel()
    {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel)
    {
        this.buttonLabel = buttonLabel;
    }

	public boolean isCanwrite() {
		if(canwrite==null)
			return new Boolean(false);
		else
			return canwrite;
	}

	public void setCanwrite(boolean canwrite) {
		this.canwrite = new Boolean(canwrite);
	}
	
	protected void cloneDafaultAttributes(EMPExtPop target) {
		super.cloneDafaultAttributes(target);
		target.canwrite = this.canwrite;
	}

	private static final long serialVersionUID = 1L;
    protected String url;
    protected String reqParams;
    protected String returnMethod;
    protected String buttonLabel;
    protected String popParam;
    protected String dataMapping;
    protected String cssButtonClass;
    protected Boolean canwrite;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\WorkSpace_TJ\cmis-main\WebContent\WEB-INF\lib\emp2.2-101210.jar
	Total time: 156 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/