package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddGrtGuarContRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "GrtGuarCont";
	private final String modelIdRe = "GrtGuarantyRe";
	private final String modelIdTee = "GrtGuarantee";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guarContNo="";//担保合同编号
		String pk_id ="";
		KeyedCollection kCollRe = new KeyedCollection(modelIdRe);
		try{
			connection = this.getConnection(context);
			//从context中取出sequenceService
			
			KeyedCollection kColl = null;
			String serno ="";
			String guar_way = "";
			String guar_cont_type = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				
				kColl.addDataField("input_id",context.getDataValue("currentUserId"));
				kColl.addDataField("input_br_id",context.getDataValue("organNo"));
				kColl.addDataField("reg_date",context.getDataValue("OPENDAY"));
				guar_way = (String)kColl.getDataValue("guar_way");//担保方式
				guar_cont_type = (String)kColl.getDataValue("guar_cont_type");//担保合同类型
				//翻译机构信息
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
				SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			//	serno =(String)kColl.getDataValue("serno");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//抵押 00，质押 01，保证 02,,03,04
			if("01".equals(guar_cont_type)){//最高额担保合同
				if("00".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "A", "A", connection, context);
				}else if("01".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "B","B", connection, context);
				}else if("02".equals(guar_way) || "03".equals(guar_way) || "04".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "C","C", connection, context);
				}
			}else if("00".equals(guar_cont_type)){//一般担保合同
				if("00".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "D","D", connection, context);
				}else if("01".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "E","E", connection, context);
				}else if("02".equals(guar_way) || "03".equals(guar_way) || "04".equals(guar_way)){
					guarContNo = CMISSequenceService4Cont.querySequenceFromDB("HT", "F","F", connection, context);
				}
			}

			kColl.setDataValue("guar_cont_no", guarContNo);
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			kColl.setDataValue("bill_amt","0");
			//担保模式为票据池担保
			if(!kColl.getDataValue("drfpo_no").equals("")){
				String drfpoNo =(String) kColl.getDataValue("drfpo_no");
				kCollRe.addDataField("guar_cont_no", guarContNo);
				kCollRe.addDataField("guaranty_id",drfpoNo);
				dao.insert(kCollRe, connection);//担保模式为票据池时，在新增成功时，新增关联信息
			}
			//担保模式为应收账款池担保
			if(kColl.getDataValue("poType").equals("1")&&!kColl.getDataValue("po_no").equals("")){
				String poNo =(String) kColl.getDataValue("po_no");
				kCollRe.addDataField("guar_cont_no", guarContNo);
				kCollRe.addDataField("guaranty_id",poNo);
				dao.insert(kCollRe, connection);//担保模式为应收账款池时，在新增成功时，新增关联信息
			}
			//担保模式为保理池担保
			if(kColl.getDataValue("poType").equals("2")&&!kColl.getDataValue("po_no_bl").equals("")){
				String poNoBl =(String) kColl.getDataValue("po_no_bl");
				kCollRe.addDataField("guar_cont_no", guarContNo);
				kCollRe.addDataField("guaranty_id",poNoBl);
				dao.insert(kCollRe, connection);//担保模式为保理池时，在新增成功时，新增关联信息
			}
			//担保模式为联保
			if(!kColl.getDataValue("agr_no").equals("")){
				String cus_id = "";//保证人客户码
				String guarId ="";//保证人编号
				String agr_no =(String) kColl.getDataValue("agr_no");
				kCollRe.addDataField("guar_cont_no", guarContNo);
				kCollRe.addDataField("guaranty_id","");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				//根据协议编号获取成员名单信息
				IndexedCollection ic = serviceLmt.searchLmtJointNameList(agr_no, connection);
				//联保协议授信总金额
				BigDecimal total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(agr_no, "03", connection));
				KeyedCollection kcTee = new KeyedCollection(modelIdTee);
				kcTee.addDataField("guar_id","");
				kcTee.addDataField("guar_type","01");
				kcTee.addDataField("guar_amt",total_amt);
				kcTee.addDataField("cus_id","");
				kcTee.addDataField("is_spadd","2");
				for(int i=0;i<ic.size();i++){
					KeyedCollection kcTemp = (KeyedCollection) ic.get(i);
					//获取成员客户码
					cus_id = (String) kcTemp.getDataValue("cus_id");
					guarId = CMISSequenceService4JXXD.querySequenceFromDB("GT", "fromDate", connection, context);
					kcTee.setDataValue("guar_id",guarId);
					kcTee.setDataValue("cus_id",cus_id);
					kCollRe.setDataValue("guaranty_id", guarId);
					dao.insert(kcTee, connection);//担保模式为联保时，复制成员名单中的信息到保证人信息表中。
					dao.insert(kCollRe, connection);//担保模式为联保时，在新增成功时，新增关联信息。
				}
				
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				service.createRLmtGuarContByJoint(guarContNo, agr_no, "0", (String)kColl.getDataValue("is_per_gur"), (String)kColl.getDataValue("is_add_guar"), context, connection);
			}
			/**ws添加 判断是否调用业务模块添加业务合同关系表接口------start------------------*/ 
			String rel = null; 
			String limit_code = null;  
			String cont_no = null;  
			String isCreditChange = null;   
			if(context.containsKey("rel")){
				rel = (String)context.getDataValue("rel");   
			}
			if(context.containsKey("isCreditChange")){
				isCreditChange = (String)context.getDataValue("isCreditChange");   
			}
			if("ywRel".equals(rel)){
				serno = (String)context.getDataValue("serno");
				cont_no = (String)context.getDataValue("cont_no");
				pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				
				KeyedCollection kCollRel = new KeyedCollection();
				kCollRel.addDataField("pk_id",pk_id);//主键
				kCollRel.addDataField("serno",serno);       
				kCollRel.addDataField("guar_cont_no",guarContNo); 
				kCollRel.addDataField("cont_no",cont_no);   
				kCollRel.addDataField("isCreditChange",isCreditChange);         
				/**调用业务模块接口*/   
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				int rst = service.addGrtLoanRGur(kCollRel, context, connection);
				if(rst!=1){
					throw new EMPException("add Failed! Record Count: " + rst);  
				}       
			}else if("sxRel".equals(rel)){
				limit_code = (String)context.getDataValue("limit_code"); 
				
				KeyedCollection kCollRel = new KeyedCollection();
				kCollRel.addDataField("limit_code",limit_code);
				kCollRel.addDataField("guar_cont_no",guarContNo);
				kCollRel.addDataField("isCreditChange",isCreditChange);
				kCollRel.addDataField("is_per_gur", kColl.getDataValue("is_per_gur"));//是否阶段性担保
				kCollRel.addDataField("is_add_guar", kColl.getDataValue("is_add_guar"));//是否追加担保
				kCollRel.addDataField("serno",(String)context.getDataValue("serno"));   //担保合同与授信关系表新加流水号字段   2013-11-30  唐顺岩
				/**调用业务模块接口*/     
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				int rst = service.addRLmtAppGuarCont(kCollRel, (String)kColl.getDataValue("guar_cont_type"), context, connection);
				if(rst!=1){ 
					throw new EMPException("add Failed! Record Count: " + rst); 
				}
			}
			/**-------------------------end-----------------------------------------*/
			if("1".equals(kColl.getDataValue("rel"))){
				int rst =0;
				/**调用业务模块接口*/     
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				limit_code = (String)kColl.getDataValue("limit_code");
				String[] lmtCodes = limit_code.split(",");
				for(int i=0;i<lmtCodes.length;i++){
					KeyedCollection kCollRel = new KeyedCollection();
					kCollRel.addDataField("limit_code",lmtCodes[i]);
					kCollRel.addDataField("guar_cont_no",guarContNo);
					kCollRel.addDataField("type","grt");
					kCollRel.addDataField("is_per_gur", kColl.getDataValue("is_per_gur"));//是否阶段性担保
					kCollRel.addDataField("is_add_guar", kColl.getDataValue("is_add_guar"));//是否追加担保
					kCollRel.addDataField("isCreditChange",isCreditChange);
					if(!kColl.getDataValue("agr_no").equals("")){//担保模式为联保时，新增生成联保授信协议与担保合同关系
						//	rst = service.addRLmtAppGuarCont(kCollRel, (String)kColl.getDataValue("guar_cont_type"), context, connection);
					}else{
						rst = service.addRLmtAppGuarCont(kCollRel, (String)kColl.getDataValue("guar_cont_type"), context, connection);
					}
				}
			//	if(rst!=1){ 
			//		throw new EMPException("add Failed! Record Count: " + rst); 
			//	}
			}
			this.putDataElement2Context(kColl, context);
			context.addDataField("oper","update");
			context.addDataField("msg","success");
			context.addDataField("pk_id",pk_id);
			context.addDataField("guar_cont_no",guarContNo);
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
