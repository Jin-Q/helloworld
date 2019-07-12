/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataElementSerializer;
import com.ecc.emp.data.DataUtility;
import com.ecc.emp.data.KeyedCollection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EMPHTTPDataSerializerService extends EMPHTTPRequestService {
	public void updateModel(HttpServletRequest request,
			HttpServletResponse response, Context context,
			DataElement dataElementDef) throws EMPException {
		String reqData = (String) request.getAttribute("reqData");
		if ((reqData == null) || (reqData.length() == 0)) {
			return;
		}
		if (dataElementDef == null) {
			DataElement element = null;
			element = DataElementSerializer.serializeFrom(reqData);
			DataUtility.updateDataModel(context, element,
					this.factory.getDataTypeDefine());
		} else {
			DataElement dstElement = (DataElement) dataElementDef.clone();
			DataElement element = DataElementSerializer.serializeFrom(reqData);
			DataUtility.copyKeyedCollectionData((KeyedCollection) element,
					(KeyedCollection) dstElement, null);
			DataUtility.updateDataModel(context, dstElement,
					this.factory.getDataTypeDefine());
		}
	}

	protected String getResponseMessage(HttpServletRequest request,
			HttpServletResponse response, Context context,
			DataElement dataElement) throws Exception {
		try {
			String resData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<kColl></kcoll>";
			if (dataElement != null) {
				resData = DataElementSerializer.doSerialize(dataElement);
			}

			return resData;
		} catch (Exception e) {
			throw e;
		}
	}
}