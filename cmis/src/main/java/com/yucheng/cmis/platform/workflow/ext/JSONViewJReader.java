package com.yucheng.cmis.platform.workflow.ext;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.web.servlet.view.AbstractView;

/**
 * 流程接入获取节点办理人输出json格式处理类
 * @author nature
 *
 */
public class JSONViewJReader extends AbstractView {
	
	private KeyedCollection output;
	/**
	 * 显示View
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	@Override
	public void render(Map model, HttpServletRequest request, HttpServletResponse response, String jspRootPath) {
		try {
			Context context = (Context) model.get(EMPConstance.ATTR_CONTEXT);
			KeyedCollection outputKColl = output;;
			if(outputKColl == null)
				outputKColl = (KeyedCollection)context.getDataElement();
			Object[] dataNames = outputKColl.keySet().toArray();
			Map map = new HashMap();
			for(int i=0;i<dataNames.length;i++){
				String dataName = dataNames[i].toString();
				DataElement dataElement = outputKColl.getDataElement(dataName);
				DataField field = (DataField)context.getDataElement(dataElement.getName());
				map.put(dataName, field.getValue());
			}
			JSONObject obj = JSONObject.fromObject(map);
			String retStr = obj.toString();
			response.addHeader("Cache-Control","no-cache"); 
			response.getOutputStream().write( retStr.getBytes( "UTF-8" ));
		} catch (Exception e) 
		{
			EMPLog.log(EMPConstance.EMP_MVC, EMPLog.ERROR, 0, "Failed to return the jsonView!", e);
		}
	}
	

	public KeyedCollection getOutput() {
		return output;
	}

	public void setOutput(KeyedCollection output) {
		this.output = output;
	}
}
