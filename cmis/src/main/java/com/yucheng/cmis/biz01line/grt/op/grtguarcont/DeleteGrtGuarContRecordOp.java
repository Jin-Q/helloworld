package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteGrtGuarContRecordOp extends CMISOperation {

	private final String modelId = "GrtGuarCont";
	

	private final String guar_cont_no_name = "guar_cont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String guar_cont_no_value = null;
			try {
				guar_cont_no_value = (String)context.getDataValue(guar_cont_no_name);
			} catch (Exception e) {}
			if(guar_cont_no_value == null || guar_cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guar_cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IndexedCollection ic = serviceLmt.getLmtAppGuarReByGuarContNo(guar_cont_no_value, connection);
			//此担保合同已被授信引用，不能被删除。
			if(ic.size()!=0){
				KeyedCollection kc = (KeyedCollection) ic.get(0);
				context.addDataField("flag", "fail");
				context.addDataField("msg", "此担保合同已被授信申请"+kc.getDataValue("serno")+"所引入，不能做删除操作！");
				return "0";
			}else{
				//删除担保合同与授信关联关系表记录（结果表）
				serviceLmt.deleteRLmtGuarCont(guar_cont_no_value, connection);
			}
			//删除合同表中的记录
			int count=dao.deleteByPk(modelId, guar_cont_no_value, connection);
			//调用担保合同组件进行关联删除
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("GrtGuarCont", context, connection);
			int con = ggc.deleteGrtGuarantyReCont(guar_cont_no_value);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}else{
				context.addDataField("flag", "success");
				context.addDataField("msg", "");

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
