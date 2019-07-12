/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces;

import net.sf.json.JSONObject;

import com.dc.eai.data.CompositeData;

public abstract interface Service {
	public abstract String exec(JSONObject jsonObj);
}