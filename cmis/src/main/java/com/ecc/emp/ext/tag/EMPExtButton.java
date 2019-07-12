// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EMPExtButton.java

package com.ecc.emp.ext.tag;

import javax.servlet.jsp.JspException;

// Referenced classes of package com.ecc.emp.ext.tag:
//            EMPExtTagSupport

public class EMPExtButton extends EMPExtTagSupport
{
	

    public EMPExtButton()
    {
        op = null;
        label = null;
    }

    public int doStartTag()
        throws JspException
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            if(id == null || id.length() == 0)
                id = op;
            sb.append("<button id=\"button_").append(id).append("\"");
            if(op != null && !"".equals(op))
            {
                sb.append(" style=\"display:none\"");
                //sb.append(str("op", op));
                sb.append("op='" + op + "'");
            }
            
            String btn_mouseout = this.mouseoutCss != null && !this.mouseoutCss.trim().equals("")?this.mouseoutCss:"btn_mouseout";
            String btn_mouseover = this.mouseoverCss != null && !this.mouseoverCss.trim().equals("")?this.mouseoverCss:"btn_mouseover";
            String btn_mousedown = this.mousedownCss != null && !this.mousedownCss.trim().equals("")?this.mousedownCss:"btn_mousedown";
            String btn_mouseup = this.mouseupCss != null && !this.mouseupCss.trim().equals("")?this.mouseupCss:"btn_mouseup";
            
            sb.append(" class=\"" + btn_mouseout + "\"").append(" onmouseover=\"this.className='" + btn_mouseover + "'\"")
            .append(" onmouseout=\"this.className='" + btn_mouseout + "'\"").append(" onmousedown=\"this.className='" + btn_mousedown + "'\"")   
            .append(" onmouseup=\"this.className='" + btn_mouseup + "'\"").append(" onclick=\"" + getButtonClickFunc() + "\">" + getLabel() + "</button>");
            
            outputContent(sb.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 1;
    }

    protected String getButtonClickFunc()
    {
        StringBuffer sb = new StringBuffer();
       
        if(getLocked()!=null && getLocked().equals("true")){
        	sb.append("lockScreen();");
        }
        sb.append("do" + id.substring(0, 1).toUpperCase() + id.substring(1)).append("(this)");
        return sb.toString();
    }

    public int doEndTag()
        throws JspException
    {
        return 0;
    }

    public String getLabel()
    {
        return getResourceValue(label);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getOp()
    {
        return op;
    }

    public void setOp(String op)
    {
        this.op = op;
    }


	public String getMouseoutCss() {
		return mouseoutCss;
	}

	public void setMouseoutCss(String mouseoutCss) {
		this.mouseoutCss = mouseoutCss;
	}

	public String getMouseoverCss() {
		return mouseoverCss;
	}

	public void setMouseoverCss(String mouseoverCss) {
		this.mouseoverCss = mouseoverCss;
	}

	public String getMousedownCss() {
		return mousedownCss;
	}

	public void setMousedownCss(String mousedownCss) {
		this.mousedownCss = mousedownCss;
	}

	public String getMouseupCss() {
		return mouseupCss;
	}

	public void setMouseupCss(String mouseupCss) {
		this.mouseupCss = mouseupCss;
	}
	
	

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}



	private String op;
    private String label;
    private String mouseoutCss = "";
    private String mouseoverCss = "";
    private String mousedownCss = "";
    private String mouseupCss = "";
    private String locked;
    
}
