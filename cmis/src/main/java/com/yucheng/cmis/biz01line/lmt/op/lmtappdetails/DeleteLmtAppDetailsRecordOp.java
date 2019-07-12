package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteLmtAppDetailsRecordOp extends CMISOperation {

	private final String modelId = "LmtAppDetails";

	private final String limit_code_name = "limit_code";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String limit_code_value = null;
			try {
				limit_code_value = (String)context.getDataValue(limit_code_name);
			} catch (Exception e) {}
			if(limit_code_value == null || limit_code_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除授信分项信息失败，错误描述，传入主键字段[授信额度编号]为空！");
				return "0";
			}

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, limit_code_value, connection);
			if(count!=1){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除授信分项信息失败，错误描述：未找到对应的分项记录！");
				return "0";
			}
			
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			if(context.containsKey("BelgLine") && "BL300".equals(context.getDataValue("BelgLine"))){   //个人条线
				lmtComponent.updateLmtAppIndivAmt(context.getDataValue("serno").toString());  //根据流水号更新个人授信申请基表数据
			}else{
				//lmtComponent.updateLmtApplyAmt(context.getDataValue("serno").toString(),(String)context.getDataValue("lmt_type"),(String)context.getDataValue("lrisk_type"));  //根据流水号更新授信申请基表数据
				lmtComponent.updateLmtApplyAmt(context.getDataValue("serno").toString(),(String)context.getDataValue("lrisk_type"));  //根据流水号更新授信申请基表数据
			}
			
			//判断分项下的担保合同是否可以删除
			/**如果授信担保合同关系表中数据是一条，且担保合同状态为登记状态,则可以删除担保合同记录*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface servicelmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IqpServiceInterface iqpService = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
			
			String guar_cont_no = "";
			int i = 0 ;
			IndexedCollection iColl_RLmtAppGuarCont = dao.queryList("RLmtAppGuarCont", " WHERE LIMIT_CODE='"+limit_code_value+"'", connection);
			for (Iterator iterator = iColl_RLmtAppGuarCont.iterator(); iterator.hasNext();) {
				KeyedCollection obj = (KeyedCollection) iterator.next();
				/**如果授信担保合同关系表中数据是一条，且担保合同状态为登记状态,则可以删除担保合同记录*/
				String guar_cont_no_str = (String)obj.getDataValue("guar_cont_no");
				Boolean guar_cont_state = service.getGuarContStatusByGuarContNo(guar_cont_no_str, connection);
				int num = servicelmt.checkRLmtAppGuarContNum(guar_cont_no_str, context, connection);
				int iqpNum = iqpService.checkGetLoanRGurNum(guar_cont_no_str, context, connection);
				if(num == 1 && iqpNum == 0 && guar_cont_state){
					guar_cont_no += "'"+guar_cont_no_str+ "'";
					if(i < iColl_RLmtAppGuarCont.size()-1){
						guar_cont_no += ",";
					}
				}
				i++;
			}
			/**调用担保模块接口*/
			if(!"".equals(guar_cont_no)){
				//删除担保合同及担保合同与押品关系
				service.deleteGuarContInfo(guar_cont_no, connection, context);
			}
			
			/** 删除分项信息时同步删除挂接的担保合同关系  */
			Map<String, String> conditionFields = new HashMap<String, String>();
			conditionFields.put(limit_code_name, limit_code_value);
			conditionFields.put("serno", (String)context.getDataValue("serno"));
			lmtComponent.deleteByField("RLmtAppGuarCont", conditionFields);
			
			context.addDataField("flag","success");
			context.addDataField("msg","success");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("msg","删除授信分项信息失败，错误描述："+e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
