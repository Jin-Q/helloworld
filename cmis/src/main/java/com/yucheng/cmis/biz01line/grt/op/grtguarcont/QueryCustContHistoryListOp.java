package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCustContHistoryListOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/**担保合同编号*/
			String guar_cont_no = "";
			/**票据池编号*/
			String drfpo_no = "";
			/**保理池或者应收账款池编号*/
			String po_no = "";
			
			IndexedCollection iColl =null;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			if(context.containsKey("guar_cont_no")){  
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				iColl = service.getHistoryContByGuarContNo(guar_cont_no,"1", pageInfo, context, connection);
			}
			/**票据池时，根据票据池编号获取其所关联的担保合同编号*/
			if(context.containsKey("drfpo_no")){
				drfpo_no = (String) context.getDataValue("drfpo_no");
				iColl = service.getHistoryContByGuarContNo(drfpo_no,"2", pageInfo, context, connection);
			}
			/**保理池或者应收账款池时，根据池编号获取其所关联的担保合同编号*/
			//modified by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start 
			if(context.containsKey("po_no")){
				po_no = (String) context.getDataValue("po_no");
				iColl = service.getHistoryContByGuarContNo(po_no,"3", pageInfo, context, connection);
			}
			//modified by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end 
			iColl.setName("LmtUseList");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
