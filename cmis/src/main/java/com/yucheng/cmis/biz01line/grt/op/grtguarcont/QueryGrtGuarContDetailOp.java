package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryGrtGuarContDetailOp  extends CMISOperation {
	
	private final String modelId = "GrtGuarCont";
	private final String guar_cont_no_name = "guar_cont_no";
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/*if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}*/
			String guar_cont_no_value = null;
			
			try {
				guar_cont_no_value = (String)context.getDataValue(guar_cont_no_name);
			} catch (Exception e) {}
			if(guar_cont_no_value == null || guar_cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guar_cont_no_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			
			KeyedCollection kColl = dao.queryDetail(modelId, guar_cont_no_value, connection);
			BigDecimal used_amt = new BigDecimal(0.00);
			if(kColl.containsKey("guar_cont_type")&&kColl.getDataValue("guar_cont_type")!=null&&"00".equals(kColl.getDataValue("guar_cont_type").toString())){
				//一般担保  --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
				//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
				String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no_value+"'";
			    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
			    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
			    String conditionStr = "";
			    //if(iCollSelectIsChange.size() > 0){
					//新增，续作,解除状态的
			    //	conditionStr = "where corre_rel in ('2','4','3') and guar_cont_no='"+guar_cont_no_value+"'";
				//}else{
					//正常，被解除状态
					conditionStr = "where guar_cont_no='"+guar_cont_no_value+"' and is_add_guar='2' and corre_rel in('1','5')";
				//}
				/**查询关联表中此担保合同已已经引入的金额*/
				IndexedCollection iColl =  dao.queryList("GrtLoanRGur", conditionStr, connection);
				for(int i=0;i<iColl.size();i++){
				   KeyedCollection kColl1 = (KeyedCollection)iColl.get(i);
				   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
				   if(is_per_gur != null && !"".equals(is_per_gur)){
					   String pk_id = (String)kColl1.getDataValue("pk_id");
					   String cont_no = (String)kColl1.getDataValue("cont_no");
					   if(cont_no != null && !"".equals(cont_no)){
						   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
						   if("2".equals(res)){
							   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
						   }else{
							   used_amt = used_amt.add(new BigDecimal(0));
						   }
					   }else{
						   String sernoSelect = (String)kColl1.getDataValue("serno");
						   String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
						   if("2".equals(res)){
							   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
						   }else{
							   used_amt = used_amt.add(new BigDecimal(0));
						   }
					   }
				   }
				}
			}else{
				//最高额担保
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no_value, context, connection));
			}
			kColl.put("used_amt", used_amt);
			//构建担保管理模块业务组件
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().
			getComponentInstance("GrtGuarCont", context, connection);
			if(kColl.getDataValue("guar_model").equals("01")){//票据池
				
				//根据合同编号得到池编号
				String drfpoNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no_value);
				//构建票据池组件类
				DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
				/**根据票据池编号获取票据池中处于在池状态的票据票面金额价值总额*/
				Double count = dpoComponent.getDrftAmtByDrfpoNo(drfpoNo,"01");
				kColl.addDataField("drfpo_no", drfpoNo);
				kColl.addDataField("bill_amt", count);
			}else if(kColl.getDataValue("guar_model").equals("02")){//应收账款池
				//根据合同编号得到应收账款池编号
				String poNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no_value);
				IqpActrecBondComponent component = new IqpActrecBondComponent();
				String sAmt = component.getAllInvcAndBondAmt(poNo, connection).split("@")[2];
				kColl.addDataField("po_no",poNo);
				kColl.addDataField("bill_amt",sAmt);
				//应收账款池标志位
				kColl.addDataField("poType","1");
			}else if(kColl.getDataValue("guar_model").equals("03")){//保理池
				//根据合同编号得到保理池编号
				String poNo = ggc.getDrfpoNoByGuarContNo(guar_cont_no_value);
				IqpActrecBondComponent component = new IqpActrecBondComponent();
				String sAmt = component.getAllInvcAndBondAmt(poNo, connection).split("@")[2];
				kColl.addDataField("po_no",poNo);
				kColl.addDataField("bill_amt",sAmt);
				//保理池标志位
				kColl.addDataField("poType","2");
			}else if(kColl.getDataValue("guar_model").equals("00")&&(kColl.getDataValue("guar_way").equals("02")||kColl.getDataValue("guar_way").equals("03"))){//保证人信息
				String guarAmt = ggc.getGuarAmtByGuarContNo(guar_cont_no_value);
				kColl.addDataField("bill_amt",guarAmt);
			}else if(kColl.getDataValue("guar_model").equals("00")&&(kColl.getDataValue("guar_way").equals("01")||kColl.getDataValue("guar_way").equals("00"))){//担保品信息
				//获取押品编号，根据押品编号获得担保金额的总和
				String guarAmt = ggc.queryGrtGuarantyAmt(guar_cont_no_value);
				kColl.addDataField("bill_amt",guarAmt);
			}else if(kColl.getDataValue("guar_model").equals("05")){//联保
				String agr_no = (String) kColl.getDataValue("agr_no");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				//获取总的授信额度
		    	BigDecimal total_amt = new BigDecimal(serviceLmt.searchLmtAgrAmtByAgrNO(agr_no, "03", connection));
		    	kColl.addDataField("bill_amt",total_amt);
			}
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			if("1".equals(kColl.getDataValue("rel"))){  
				try{
					KeyedCollection resultKc = null;
					resultKc=service.queryRLmtGuarContInfo(guar_cont_no_value,"grt",connection);
					resultKc.addDataField("guar_amt",kColl.getDataValue("guar_amt"));
					resultKc.setName("RLmtGuar");
					this.putDataElement2Context(resultKc, context); 
				}catch(Exception ee){
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "根据授信担保合同编号查询担保合同与授信关系表错误，错误描述"+ee.getMessage(), null);
				}
   
			}else if("2".equals(kColl.getDataValue("rel"))){
				try{
					KeyedCollection resultKc = null;
					resultKc=service.queryRLmtGuarContInfo(guar_cont_no_value,"",connection);
					resultKc.addDataField("guar_amt",kColl.getDataValue("guar_amt"));
					resultKc.setName("RLmtGuar");
					this.putDataElement2Context(resultKc, context); 
				}catch(Exception ee){
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "根据授信担保合同编号查询担保合同与授信关系表错误，错误描述"+ee.getMessage(), null);
				}
			}else if("3".equals(kColl.getDataValue("rel"))){
				
			}
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
	    	SInfoUtils.addUSerName(kColl, new String[] { "manager_id" });
			
			
			/**ws添加 ---start-----------------*/
			String rel = "";
			String pk_id = "";   
			String limit_code = "";   
		    if(context.containsKey("rel")){
		    	rel = (String)context.getDataValue("rel");	
		    }
			if("ywRel".equals(rel)){
				if(context.containsKey("pk_id")){
					pk_id = (String)context.getDataValue("pk_id");
				}
//				CMISModualServiceFactory serviceJndi1 = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service1 = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				int num = service1.checkGetLoanRGurNum(guar_cont_no_value, context, connection);
				KeyedCollection kCollRel = service1.selectGetLoanRGur(pk_id, context, connection);
				String guar_cont_state = (String)kColl.getDataValue("guar_cont_state");
				if(num>1 || !guar_cont_state.equals("00")){   
				   context.addDataField("canUpdate", "canNot");
				   context.setDataValue("op", "view"); 
				}  
				kCollRel.setId("GrtLoan"); 
				//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
				String modify_rel_serno = "";
				if(context.containsKey("modify_rel_serno")){
					modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
				}
				if(!"".equals(modify_rel_serno)){
					String conditionStr1 = "where biz_serno='"+modify_rel_serno+"' and guar_cont_no='"+guar_cont_no_value+"'";
					IndexedCollection selectIColl = dao.queryList("GrtLoanRGurTmp", conditionStr1, connection); 
					if(selectIColl!=null&&selectIColl.size()>0){
						KeyedCollection kCollRelTmp = (KeyedCollection) selectIColl.get(0);
						kCollRel.put("guar_amt", kCollRelTmp.getDataValue("guar_amt"));
					}
				}
				//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 END
				this.putDataElement2Context(kCollRel, context);  
			}
			
		/*	else if("sxRel".equals(rel)){
				if(context.containsKey("limit_code")){
				   limit_code = (String)context.getDataValue("limit_code");
				}
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				int num = service.checkRLmtAppGuarContNum(guar_cont_no_value, context, connection);
				KeyedCollection kCollRel = service.selectRLmtAppGuarCont(limit_code, guar_cont_no_value, context, connection);
				String guar_cont_state = (String)kColl.getDataValue("guar_cont_state");
				if(num>1 || !guar_cont_state.equals("00")){    
				   context.addDataField("canUpdate", "canNot"); 
				}  
				kCollRel.setId("RLmtGuar");      
				this.putDataElement2Context(kCollRel, context); 
				
			}   
		*/
			/**--------------end--------------*/  
			
			this.putDataElement2Context(kColl, context);
			
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
