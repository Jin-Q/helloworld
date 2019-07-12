package com.yucheng.cmis.biz01line.iqp.op.iqpbillincome;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.pubopera.PubOperaComponent;

public class UpdateIqpBillIncomeRecordOp extends CMISOperation {

	private final String modelId = "IqpBillIncome";
	private final String billDetailModel = "IqpBillDetail";
	private final String billRelModel = "IqpBatchBillRel";
	private final String batModel = "IqpBatchMng"; //批次表模型
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String porderno = "";
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			if(kColl.containsKey("porder_no")){
				porderno = (String)kColl.getDataValue("porder_no");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String batchno = (String)kColl.getDataValue("batch_no");
			
			/** 判断插入操作还是新增操作 */
			Map<String,String> incomeMap = new HashMap<String,String>();
			incomeMap.put("batch_no",batchno);
			incomeMap.put("porder_no",porderno);
			KeyedCollection inKColl = dao.queryDetail(modelId, incomeMap, connection);
			String pordernoHelp = (String)inKColl.getDataValue("porder_no");
			if(pordernoHelp != null && !"".equals(pordernoHelp)){
				dao.update(kColl, connection);
			} else {
				dao.insert(kColl, connection);
			}
			/** -------更新批次信息-------- */
			KeyedCollection batKColl = new KeyedCollection();
			batKColl.addDataField("batch_no", batchno);
			/** 通过批次号、票据编号重新统计票据批次表中信息 ，封装需要查询的票据信息SQL */
			IndexedCollection relIColl1 = dao.queryList(billRelModel, " where batch_no = '"+batchno+"'", connection);
			String porderSQLHelp = " where porder_no in (";
			for(int i=0;i<relIColl1.size();i++){
				KeyedCollection kc = (KeyedCollection)relIColl1.get(i);
				String porderNo = (String)kc.getDataValue("porder_no");
				porderSQLHelp = porderSQLHelp+"'"+porderNo+"',";
			}
			porderSQLHelp = porderSQLHelp.substring(0, porderSQLHelp.length()-1)+")";
			/** 计算票据总金额 */
			double billAmt = 0;
			IndexedCollection biIColl = dao.queryList(billDetailModel, porderSQLHelp, connection);
			if(biIColl != null && biIColl.size() > 0){
				int billNum = biIColl.size();
				for(int i=0;i<biIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)biIColl.get(i);
					String amt = (String)kc.getDataValue("drft_amt");
					billAmt += Double.parseDouble(amt);
				}
				batKColl.addDataField("bill_qnt", billNum);//票据数量
				batKColl.addDataField("bill_total_amt", billAmt);//票据总金额
			}
			
			/** 计算票据利息,回购利息 */
			double intAmt = 0;
			double rbuyAmt = 0;
			String conditionStr = porderSQLHelp + " and batch_no = '"+batchno+"'";
			IndexedCollection inIColl = dao.queryList(modelId, conditionStr, connection);
			if(inIColl != null && inIColl.size() > 0){
				for(int i=0;i<inIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)inIColl.get(i);
					String amt = (String)kc.getDataValue("int");
					String ramt = (String)kc.getDataValue("rebuy_int");
					if(ramt == null){
						ramt = "0";
					}
					intAmt += Double.parseDouble(amt);
					rbuyAmt += Double.parseDouble(ramt);
				}
				batKColl.addDataField("int_amt", intAmt);//票据利息
				batKColl.addDataField("rebuy_int", rbuyAmt);//回购利息
			}
			batKColl.addDataField("rpay_amt", billAmt-intAmt);//实付金额=票面金额-票据利息（不计算回购利息）
			batKColl.setName(batModel);
			dao.update(batKColl, connection);
			
			/**如果有付息信息，需保存付息信息*/
			String biz_type = (String)kColl.getDataValue("biz_type");
			if(biz_type.equals("07")){//直贴
				if(context.containsKey("IqpBillPintDetailList")){
					IndexedCollection pintIColl = (IndexedCollection)context.getDataElement("IqpBillPintDetailList");
					if(pintIColl!=null&&pintIColl.size()>0){
						PubOperaComponent pubOperaComponent = (PubOperaComponent)CMISComponentFactory.getComponentFactoryInstance()
						.getComponentInstance("PubOpera", context, connection);
						pubOperaComponent.deleteDateByTableAndCondition("Iqp_Bill_Pint_Detail", " where batch_no = '"+batchno+"' and porder_no = '"+porderno+"' ");
					}
					for(int i=0;i<pintIColl.size();i++){
						KeyedCollection pintKColl = (KeyedCollection)pintIColl.get(i);
						pintKColl.setName("IqpBillPintDetail");
						String optype = (String)pintKColl.getDataValue("optType");
						String pk = (String) pintKColl.getDataValue("pk");
						BigDecimal int_amt = new BigDecimal(kColl.getDataValue("int")+"");
						if("add".equals(optype)||"".equals(optype)){
							/** 判断该条数据是否已经存在，存在则修改原数据 */
							if(pk.equals("")){
								BigDecimal pint_amt = new BigDecimal(pintKColl.getDataValue("pint_perc")+"").multiply(int_amt);
								pintKColl.setDataValue("pint_amt", pint_amt);
								dao.insert(pintKColl, connection);
							}else{
								pintKColl.removeDataElement("pk");
								BigDecimal pint_amt = new BigDecimal(pintKColl.getDataValue("pint_perc")+"").multiply(int_amt);
								pintKColl.setDataValue("pint_amt", pint_amt);
								dao.insert(pintKColl, connection);
							}
						}else if("del".equals(optype)){//删除操作
							dao.deleteByPk("IqpBillPintDetail", pk, connection);
						}else {
							//新增后又删除，该条记录作废，不做任何处理,此处标出制作逻辑说明
						}
					}
				}
			}
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
