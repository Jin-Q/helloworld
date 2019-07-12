package com.yucheng.cmis.platform.workflow.op.wficallbackdisc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 *
 * 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求     
 * @author jwang 2015/7/27
 *
 */
public class CheckWfiCallBackDiscRecordOp extends CMISOperation {
	private final String modelId = "WfiCallBackDisc";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cb_enname="";
			try {
				cb_enname = (String)context.getDataValue("cb_enname");
			} catch (Exception e) {}
			if(cb_enname == null || cb_enname.length() == 0){
				context.addDataField("flag",PUBConstant.FAIL);
				context.addDataField("msg","异步查询失败，错误描述，传入打回标识号字段为空！");
				return "0";
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl =dao.queryList(modelId, "where cb_enname='"+cb_enname+"'", connection);
			if(iColl!=null&&iColl.size()>0){
				context.addDataField("flag", PUBConstant.FAIL);
				context.addDataField("msg","保存失败！错误描述，已存在打回标识号为["+cb_enname+"]的配置信息！");
			}else{
				context.addDataField("flag", PUBConstant.SUCCESS);
				context.addDataField("msg","");
			}	
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
