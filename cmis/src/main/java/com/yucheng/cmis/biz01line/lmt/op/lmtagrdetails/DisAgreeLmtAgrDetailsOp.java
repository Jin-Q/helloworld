package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DisAgreeLmtAgrDetailsOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAgrDetails";
	
	/**
	 * 对台账进行一票否决，将台账状态变为终止 30
	 * 先检查该授信项下是否发生业务，没有发生可以否决
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flagInfo = null;
		String limit_code = null;
		try{
			connection = this.getConnection(context);
			
			limit_code = (String) context.getDataValue("limit_code");
			if(limit_code==null||"".equals(limit_code)){
				throw new Exception("主键[limit_code]为空！");
			}
			
			TableModelDAO dao = getTableModelDAO(context);
			//调用业务接口检查授信项下是否发生业务
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			boolean b = service.checkIqp4DisAgree(limit_code, context, connection);
			if(b){
				//获得台账信息
				KeyedCollection kCollDet = dao.queryDetail(modelId, limit_code, connection);
				if(kCollDet==null||kCollDet.getDataValue("limit_code")==null){
					throw new Exception("根据台账编号["+limit_code+"]查询台账信息为空！");
				}
				//更新台账状态
				kCollDet.setDataValue("lmt_status", "30");
				dao.update(kCollDet, connection);
				flagInfo = PUBConstant.SUCCESS;
			}else{
				flagInfo = PUBConstant.EXISTS;
			}
			context.addDataField("flag", flagInfo);
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
