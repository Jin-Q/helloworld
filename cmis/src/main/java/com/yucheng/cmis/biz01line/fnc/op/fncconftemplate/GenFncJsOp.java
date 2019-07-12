package com.yucheng.cmis.biz01line.fnc.op.fncconftemplate;

/**
 * 生成查询静态页面,总共会生成 四个文件
 * 1，查询条件页面2，查询结果显示字段 3，查询结果排序，4，js页面
 * 在模版staticQuery页面里嵌入
 */

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfTemplateComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class GenFncJsOp extends CMISOperation {
	

	private final String modelId = "FncConfTemplate";


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String fnc_id = "";
			try {
				//获取报表编号
				fnc_id = (String)context.getDataValue("fnc_id");
			} catch (Exception e) {}
			if(fnc_id == null || fnc_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+fnc_id+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kCollTemp = dao.queryDetail(modelId, fnc_id, connection);
			String bsId = (String) kCollTemp.getDataValue("fnc_bs_style_id");//资产负债表
			String plId = (String) kCollTemp.getDataValue("fnc_pl_style_id");//损益表编号
			
			//根据财报编号 ，生成 相应的查询JSP页面。
			FncConfTemplateComponent fcComponent = (FncConfTemplateComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("FncConfTemplate",context, connection);
			fcComponent.generateFncJs(bsId);//生成资产负债表js
			fcComponent.generateFncJs(plId);//生成损益表js
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"op层GenPageOp处理出错:" + e.getMessage(),e);	
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
