/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces;

import com.dc.eai.data.CompositeData;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceImpl implements Service {
	private static Log log = LogFactory.getLog(ServiceImpl.class);

	public String exec(JSONObject jsonObj) {
		String data = "";
		if (log.isDebugEnabled()) {
			log.info("[ҵ�����Ѿ��������]");
		}
		return data;
	}
}