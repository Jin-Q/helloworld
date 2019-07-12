package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class IqpActrecBondInPoOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";
	private final String modelIdMana = "IqpActrecpoMana";

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String po_no = null;
			String cont_no = null;
			String invc_no = null;
			try{
				po_no = context.getDataValue("po_no").toString();
	
				cont_no = context.getDataValue("cont_no").toString();
				invc_no = context.getDataValue("invc_no").toString();
			}catch(Exception e){}
			
			if (po_no == null || po_no.length() == 0)
				throw new EMPJDBCException("The value of param[" + po_no
						+ "] cannot be null!");
			
			if (cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("The value of param[" + cont_no
						+ "] cannot be null!");
			
			if (invc_no == null || invc_no.length() == 0)
				throw new EMPJDBCException("The value of param[" + invc_no
						+ "] cannot be null!");
			
			//中文转码
			cont_no = URLDecoder.decode(cont_no,"UTF-8");
			invc_no = URLDecoder.decode(invc_no,"UTF-8");
			po_no = URLDecoder.decode(po_no,"UTF-8");
			
			String cont_no_arr[] = cont_no.split(",");
			String invc_no_arr[] = invc_no.split(",");
			String po_no_arr[] = po_no.split(",");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//更新池状态为生效
			KeyedCollection kCollMana = dao.queryFirst(modelIdMana, null, "where po_no = '"+po_no_arr[0]+"'", connection);
			kCollMana.setDataValue("status", "2");
			IqpActrecBondComponent component = new IqpActrecBondComponent();
			String sAmt = component.getAllInvcAndBondAmt(po_no_arr[0], connection);
			kCollMana.setDataValue("invc_quant", Integer.parseInt(sAmt
					.split("@")[0]));
			kCollMana.setDataValue("invc_amt", Double.parseDouble(sAmt
					.split("@")[1]));
			kCollMana.setDataValue("crd_rgtchg_amt", Double.parseDouble(sAmt
					.split("@")[2]));
			//dao.update(kCollMana, connection);
			//context.addDataField("flag", "success");
		//	this.putDataElement2Context(kColl, context);
			String po_type = (String)kCollMana.getDataValue("po_type");
			String exwa_type = "";
			if(po_type!=null&&"1".equals(po_type)){
				exwa_type = "01";//应收账款池
			}else{
				exwa_type = "02";//保理池
			}
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//新增出入库申请主表信息
			KeyedCollection kCollInfo = new KeyedCollection("MortStorExwaInfo");
			kCollInfo.addDataField("serno", serno);
			kCollInfo.addDataField("stor_exwa_mode","04");//入池
			kCollInfo.addDataField("input_date",context.getDataValue("OPENDAY"));
			kCollInfo.addDataField("exwa_type", exwa_type);
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
			for(int i=0;i<invc_no_arr.length;i++){
				invc_no = invc_no_arr[i];
				po_no = po_no_arr[i];
				cont_no = cont_no_arr[i];
				kCollDetail.setDataValue("guaranty_no",po_no);
				kCollDetail.setDataValue("warrant_no",invc_no+"#"+cont_no);
				kCollDetail.setDataValue("warrant_type","99");//其他
				kCollDetail.setDataValue("warrant_state","2");//入池记账中
				
				Map<String,String> pkMap = new HashMap<String,String>();
				pkMap.put("cont_no", cont_no);
				pkMap.put("invc_no", invc_no);
				KeyedCollection kCollori = dao.queryDetail(modelId, pkMap, connection);
				if(kCollori!=null){
					ori_warrant_state = (String) kCollori.getDataValue("status");
				}
				kCollDetail.setDataValue("ori_warrant_state",ori_warrant_state);
				dao.insert(kCollDetail, connection);
			}
			
			//更新应收账款明细为入池记账中
			Map<String,String> pkMap = new HashMap<String,String>();
			for(int i=0;i<invc_no_arr.length;i++){
				pkMap.put("cont_no", cont_no_arr[i]);
				pkMap.put("invc_no", invc_no_arr[i]);
				KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
				kColl.setDataValue("status", "5");
				dao.update(kColl, connection);
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
				context.addDataField("flag", "应收账款池入池授权发送失败!");
				throw new EMPException("应收账款池入池授权发送失败!");
			}
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
