package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class AddPlocyRelFirstOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String flowValue = (String)context.getDataValue("flowValue");
			String flowNodeValue = (String)context.getDataValue("flowNodeValue");
			//结息流程节点（用于多选）
			String flowNodeHelp[] = flowNodeValue.split(",");
			
			//获取页面政策资料信息,其中编码需要进行转换
			String schemecode = (String)context.getDataValue("schemecode");			
			//String schemedesc = StringUtil.getStrEncode((String)context.getDataValue("schemedesc"),"ISO8859-1","UTF-8");	
			String schemeid = (String)context.getDataValue("schemeId");	
			String schemetype = (String)context.getDataValue("schemetype");	
			/**通过选中状态判断是否对该条记录进行流程几点配置更新*/
			String ifSelect = (String)context.getDataValue("ifSelect");
			//if(ifSelect.equals("1")){
				//插入关联表prd_scheme_space_rel
				KeyedCollection kColl = new KeyedCollection();
				kColl.addDataField("flowValue", flowValue);
				kColl.addDataField("flowNodeValue", flowNodeValue);
				kColl.addDataField("schemecode", schemecode);
				kColl.addDataField("schemeid", schemeid);
				kColl.addDataField("schemetype", schemetype);
				
				PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
				ppsc.insertPrdSchemeSpaceRel(ifSelect,kColl);
			//}
		
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
