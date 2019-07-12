package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.pubopera.PubOperaComponent;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpActrecpoManaRecordOp extends CMISOperation {

	private final String modelId = "IqpActrecpoMana";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String del4BANoFlag ="";//回款保证金明细删除方法调用标志
			String poNo="";
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("del4BailAccNoFlag")){
			  del4BANoFlag = (String) context.getDataValue("del4BailAccNoFlag");
			}
			if(!del4BANoFlag.equals("1")){
				try {
					kColl = (KeyedCollection) context.getDataElement(modelId);
				} catch (Exception e) {
				}
				if (kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to insert[" + modelId
							+ "] cannot be empty!");
			    poNo = kColl.getDataValue("po_no").toString();
				KeyedCollection kCollQuery = dao.queryFirst(modelId, null,
						" where po_no = '" + poNo + "'", connection);
				if (kCollQuery == null || kCollQuery.getDataValue("po_no") == null) {
					String main_br_id = (String) context.getDataValue("organNo");
					String serno = CMISSequenceService4JXXD.querySequenceFromSQ("PO",
							"all", main_br_id, connection, context);
					kColl.setDataValue("po_no", serno);
					dao.insert(kColl, connection);
					context.addDataField("po_no",serno);
					poNo= serno;
				} else {
					if (kColl.getDataValue("po_type").equals("2")) {
						kCollQuery.setDataValue("factor_mode", kColl.getDataValue(
								"factor_mode").toString());
						kCollQuery.setDataValue("is_rgt_res", kColl.getDataValue(
								"is_rgt_res").toString());
					}
					kCollQuery.setDataValue("cus_id", kColl.getDataValue("cus_id")
							.toString());
					kCollQuery.setDataValue("po_mode", kColl
							.getDataValue("po_mode").toString());
					//kCollQuery.setDataValue("bail_acc_no", kColl.getDataValue("bail_acc_no").toString());
					//kCollQuery.setDataValue("bail_acc_name", kColl.getDataValue("bail_acc_name").toString());
					kCollQuery.setDataValue("invc_quant", kColl.getDataValue(
							"invc_quant").toString());
					kCollQuery.setDataValue("invc_amt", kColl.getDataValue(
							"invc_amt").toString());
					kCollQuery.setDataValue("crd_rgtchg_amt", kColl.getDataValue(
							"crd_rgtchg_amt").toString());
					kCollQuery.setDataValue("pledge_rate", kColl.getDataValue(
							"pledge_rate").toString());
					kCollQuery.setDataValue("period_grace", kColl.getDataValue(
							"period_grace").toString());
					kCollQuery.setDataValue("memo", kColl.getDataValue("memo")
							.toString());
					kCollQuery.setDataValue("manager_id", kColl.getDataValue(
							"manager_id").toString());
					kCollQuery.setDataValue("manager_br_id", kColl.getDataValue(
							"manager_br_id").toString());
					dao.update(kCollQuery, connection);
					context.addDataField("po_no",poNo);
				}
			}
			//存在回款保证金明细，需保存
			if(context.containsKey("IqpBailaccDetailList")){
				IndexedCollection BailADL = (IndexedCollection)context.getDataElement("IqpBailaccDetailList");
				//保存前，删除数据库中该池的原保证金明细信息
				if(BailADL!=null&&BailADL.size()>0){
					PubOperaComponent pubOperaComponent = (PubOperaComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance("PubOpera", context, connection);
					pubOperaComponent.deleteDateByTableAndCondition("iqp_bailacc_detail", " where po_no = '"+poNo+"' ");
				}
				for(int i=0;i<BailADL.size();i++){
					KeyedCollection BailAD = (KeyedCollection)BailADL.get(i);
					BailAD.setDataValue("po_no", poNo);
					BailAD.setName("IqpBailaccDetail");
					String optype = (String)BailAD.getDataValue("optType");
					String pk = (String) BailAD.getDataValue("pk");
					if("add".equals(optype)||"".equals(optype)){
						/** 判断该条数据是否已经存在，存在则修改原数据 */
						if(pk.equals("")){
							dao.insert(BailAD, connection);
						}else{
							BailAD.removeDataElement("pk");
							dao.insert(BailAD, connection);
						}
					}else if("del".equals(optype)){//删除操作
						dao.deleteByPk("IqpBailaccDetail", pk, connection);
					}else {
						//新增后又删除，该条记录作废，不做任何处理（optype ="none"）
					}
				}
				if(del4BANoFlag.equals("1")){
					context.addDataField("po_no", poNo);
				}			
			}
			context.addDataField("flag", "success");
			/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
		} catch (EMPException ee) {
			context.addDataField("flag", "fail");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "fail");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
