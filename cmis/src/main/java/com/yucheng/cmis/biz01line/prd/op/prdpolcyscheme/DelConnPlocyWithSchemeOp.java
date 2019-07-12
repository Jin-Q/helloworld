package com.yucheng.cmis.biz01line.prd.op.prdpolcyscheme;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.prd.component.PrdPolcySchemeComponent;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DelConnPlocyWithSchemeOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String schemeId = null;
			if(context.containsKey("schemeId")){
				schemeId = (String)context.getDataValue("schemeId");
			}else {
				throw new EMPException("获取关联政策方案ID失败！");
			}
			PrdPolcySchemeComponent ppsc = (PrdPolcySchemeComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PRDConstant.PRDPOLCYSCHEMECOMPONENT, context, connection);
			//拆分政策资料ID
			String schemecodeArr = (String)context.getDataValue("schemecodeArr");
			schemecodeArr = schemecodeArr.substring(0, schemecodeArr.length()-1);
			String plocyArr[] = schemecodeArr.split(",");
			for(int i=0;i<plocyArr.length;i++){
				ppsc.delPrdPlocyWithPrdScheme(schemeId, plocyArr[i]);
			}
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
