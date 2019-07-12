package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class InsertCtrRecordOp extends CMISOperation {
	private static final String FROMMODEL = "IqpLoanApp";//源表模型
	private static final String TOMODEL = "CtrLoanCont";//目标表模型
	
	@Override
	public String doExecute(Context context) throws EMPException {
		/**将业务申请数据插入合同信息，需要包含从表以及相关tab页签数据*/
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kModel = null;
			KeyedCollection subModel = null;
			String prdId = "";
			String modelCollection ="";
			try {
				kModel = (KeyedCollection)context.getDataElement(FROMMODEL);
				kModel.setDataValue("approve_status", "997");
				modelCollection = (String)context.getDataValue("modelCollection");
				subModel = (KeyedCollection)kModel.getDataElement("IqpLoanAppSub");
				prdId = (String)kModel.getDataValue("prd_id");
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**封装合同生成的默认信息，非映射取值,包括合同编号，合同装填等默认初始化信息,测试使用*/
			KeyedCollection toDefKColl = new KeyedCollection();
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", "fromDate", "1", connection, context);
			toDefKColl.addDataField("cont_no",cont_no);//合同编号
			toDefKColl.addDataField("cont_status","100");//合同状态
			String serno = (String)kModel.getDataValue("serno");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			String[] list = null; 
			list = modelCollection.split(",");
			if(list.length>0){
				for(int i=0;i<list.length;i++){
					if(list[i].equals("CusManager")||list[i].equals("IqpCusAcct")||list[i].equals("TogetherRqstr")||list[i].equals("GrtLoanRGur")){
						IndexedCollection iColl = dao.queryList(list[i], serno, connection);
						for(int j=0;j<iColl.size();j++){
							KeyedCollection kColl = (KeyedCollection)iColl.get(j);
							kColl.setDataValue("cont_no",cont_no);
							dao.update(kColl, connection);
						} 
					}
				}
			}
			
			
			String apply_date = (String)kModel.getDataValue("apply_date");
			String termType = (String)subModel.getDataValue("term_type");
			String term = (String)subModel.getDataValue("apply_term");
			String type = "";
			if("001".equals(termType)){
				type = "Y";
			}else if("002".equals(termType)){
				type = "M";
			}else if("003".equals(termType)){
				type = "D";
			}
			toDefKColl.addDataField("cont_end_date",DateUtils.getAddDate(type, apply_date, Integer.parseInt(term)));//合同到期日
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", kModel, toDefKColl, TOMODEL, context, connection);
			dao.update(kModel, connection);
			context.addDataField("flag", reultKColl.getDataValue("code"));
			context.addDataField("msg", reultKColl.getDataValue("msg"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
