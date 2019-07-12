package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class SureToPoolOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		
		String drfpo_no = "";
		String porder_no = "";
		KeyedCollection IqpCorreInfo = new KeyedCollection("IqpCorreInfo");//票据关联信息的kColl
		KeyedCollection IqpBillDetailInfo = new KeyedCollection("IqpBillDetailInfo");//票据详细信息的kColl
		KeyedCollection IqpDrfpoMana = new KeyedCollection("IqpDrfpoMana");//票据管理信息的kColl
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			//票据池编号
			drfpo_no = (String) context.getDataValue("drfpo_no");
			//票据编号
			porder_no = (String) context.getDataValue("porder_no");
			//中文转码
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
			porder_no = URLDecoder.decode(porder_no,"UTF-8");
			
			String porder[] = porder_no.split(",");
			//更改汇票状态为“质押”状态
			IqpBillDetailInfo.addDataField("porder_no","");
			IqpBillDetailInfo.addDataField("status","05");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpBillDetailInfo.setDataValue("porder_no",porder_no);
				//dao.update(IqpBillDetailInfo, connection);//////////////////////////////////////////等待记账成功通知
			}
			//构建票据池组件类
			DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			/**查询票据池中处于在池状态的票据票面金额价值总额*/
			Double count = dpoComponent.getDrftAmtByDrfpoNo(drfpo_no,"01");
			//更改票据池状态为有效状态，更新在池票据总金额字段
			IqpDrfpoMana.addDataField("drfpo_no",drfpo_no);
			IqpDrfpoMana.addDataField("status","01");
			IqpDrfpoMana.addDataField("bill_amt", count);
			//dao.update(IqpDrfpoMana, connection);///////////////////////////////////////////////////等待记账成功通知
			//返回异步标志，状态更改成功
			
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//新增出入库申请主表信息
			KeyedCollection kCollInfo = new KeyedCollection("MortStorExwaInfo");
			kCollInfo.addDataField("serno", serno);
			kCollInfo.addDataField("stor_exwa_mode","04");//入池
			kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
			kCollInfo.addDataField("exwa_type", "03");//票据池
			kCollInfo.addDataField("approve_status", "997");
			dao.insert(kCollInfo, connection);
			
			KeyedCollection kCollDetail = new KeyedCollection("MortStorExwaDetail");
			kCollDetail.addDataField("serno",serno);
			kCollDetail.addDataField("guaranty_no","");
			kCollDetail.addDataField("warrant_no","");
			kCollDetail.addDataField("warrant_type","");
			kCollDetail.addDataField("warrant_state","");
			kCollDetail.addDataField("ori_warrant_state","");
			String ori_warrant_state = "";
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				kCollDetail.setDataValue("guaranty_no",drfpo_no);
				kCollDetail.setDataValue("warrant_no",porder_no);
				KeyedCollection kColl = dao.queryDetail("IqpBillDetailInfo", porder_no, connection);
				if(kColl.containsKey("bill_type")&&kColl.getDataValue("bill_type")!=null&&"100".equals(kColl.getDataValue("bill_type"))){
					kCollDetail.setDataValue("warrant_type","53");//银票
				}else{
					kCollDetail.setDataValue("warrant_type","33");//商票
				}
				Map<String,String> pkMap = new HashMap<String,String>();
				pkMap.put("drfpo_no",drfpo_no);
				pkMap.put("porder_no",porder_no);
				KeyedCollection kColl_ori = dao.queryDetail("IqpCorreInfo", pkMap, connection);
				if(IqpCorreInfo!=null){
					ori_warrant_state = (String)kColl_ori.getDataValue("status");
				}	
				kCollDetail.setDataValue("warrant_state","2");//入池记账中
				kCollDetail.setDataValue("ori_warrant_state",ori_warrant_state);
				dao.insert(kCollDetail, connection);
			}
			
			//更改票据池与汇票的关联信息表的状态为“入池记账中”状态
			IqpCorreInfo.addDataField("drfpo_no",drfpo_no);
			IqpCorreInfo.addDataField("porder_no","");
			IqpCorreInfo.addDataField("status","04");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpCorreInfo.setDataValue("porder_no",porder_no);
				dao.update(IqpCorreInfo, connection);
			}
			/**调用生成授权，等待发送报文*/
			MortFlowComponent MortFlowComponent = (MortFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("MortFlowComponent",context,connection );
			MortFlowComponent.doWfAgreeForMort(serno);
			/**调用ESB接口，发送报文*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			try{
				serviceRel.tradeDZYWQZCRK(serno, context, connection);
				context.addDataField("flag","success");
			}catch(Exception e){
				context.addDataField("flag", "票据池入池授权发送失败!");
				throw new EMPException("票据池入池授权发送失败!");
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
