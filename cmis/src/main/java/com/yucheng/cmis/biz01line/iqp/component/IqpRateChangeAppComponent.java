package com.yucheng.cmis.biz01line.iqp.component;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class IqpRateChangeAppComponent extends CMISComponent {
private static final String CTRCONTSUBMODEL = "CtrLoanContSub";//合同从表
	
	/**
	 * 利率调整流程结束后处理方法
	 * serno 业务流水号
	 * @return 
	 * @throws Exception
	 */
	public void doWfAgreeForIqpChangeApp(String serno) throws Exception{
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);	
			
			/** 1.数据准备：通过业务流水号查询【出账申请】 */
			KeyedCollection kColl =  dao.queryDetail("IqpRateChangeApp", serno, this.getConnection());
			
			String bill_no = (String)kColl.getDataValue("bill_no");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String cur_type = (String)kColl.getDataValue("cur_type");
			String loan_amt = (String)kColl.getDataValue("loan_amt");
			String loan_balance = (String)kColl.getDataValue("loan_balance");
			String distr_date = (String)kColl.getDataValue("distr_date");
			String end_date= (String)kColl.getDataValue("end_date");
			
			String ir_accord_type = (String)kColl.getDataValue("ir_accord_type");
			String ir_type = (String)kColl.getDataValue("ir_type");
			String ruling_ir = (String)kColl.getDataValue("ruling_ir");
			String pad_rate_y = (String)kColl.getDataValue("pad_rate_y");
			String ir_adjust_type = (String)kColl.getDataValue("ir_adjust_type");
			String ir_float_type = (String)kColl.getDataValue("ir_float_type");
			String ir_float_rate = (String)kColl.getDataValue("ir_float_rate");
			String ir_float_point = (String)kColl.getDataValue("ir_float_point");
			String reality_ir_y = (String)kColl.getDataValue("reality_ir_y");
			String overdue_float_type = (String)kColl.getDataValue("overdue_float_type");
			String overdue_rate = (String)kColl.getDataValue("overdue_rate");
			String overdue_rate_y = (String)kColl.getDataValue("overdue_rate_y");
			String default_float_type = (String)kColl.getDataValue("default_float_type");
			String default_rate = (String)kColl.getDataValue("default_rate");
			String default_rate_y = (String)kColl.getDataValue("default_rate_y");
			String ruling_ir_code = (String)kColl.getDataValue("ruling_ir_code");
			
			String new_ir_accord_type = (String)kColl.getDataValue("new_ir_accord_type");
			String new_ir_type = (String)kColl.getDataValue("new_ir_type");
			String new_ruling_ir = (String)kColl.getDataValue("new_ruling_ir");
			String new_pad_rate_y = (String)kColl.getDataValue("new_pad_rate_y");
			String new_ir_adjust_type = (String)kColl.getDataValue("new_ir_adjust_type");
			String new_ir_float_type = (String)kColl.getDataValue("new_ir_float_type");
			String new_overdue_float_type = (String)kColl.getDataValue("new_overdue_float_type");
			String new_default_float_type = (String)kColl.getDataValue("new_default_float_type");
			String new_default_rate = (String)kColl.getDataValue("new_default_rate");
			String new_default_rate_y = (String)kColl.getDataValue("new_default_rate_y");
			String new_ir_float_rate = (String)kColl.getDataValue("new_ir_float_rate");
			String new_ir_float_point = (String)kColl.getDataValue("new_ir_float_point");
			String new_reality_ir_y = (String)kColl.getDataValue("new_reality_ir_y");
			String new_overdue_rate = (String)kColl.getDataValue("new_overdue_rate");
			String new_overdue_rate_y = (String)kColl.getDataValue("new_overdue_rate_y");
			String new_ruling_ir_code = (String)kColl.getDataValue("new_ruling_ir_code");
			
			String new_inure_date = (String)kColl.getDataValue("new_inure_date"); //调整后利率生效日期
			String manager_br_id = (String)kColl.getDataValue("manager_br_id");
			
			/** 2.数据准备：通过客户编号查询【客户信息】 */
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,this.getContext(),this.getConnection());
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
			
			/** 3.数据准备：通过借据编号查询台账表 */
			KeyedCollection kCollAcc = dao.queryDetail("AccLoan", bill_no, this.getConnection());
			String prd_id = (String)kCollAcc.getDataValue("prd_id");
			String cont_no = (String)kCollAcc.getDataValue("cont_no");
			String fina_br_id = (String)kCollAcc.getDataValue("fina_br_id");
			String date = this.getContext().getDataValue(PUBConstant.OPENDAY).toString();//营业日期
			/**合同信息**/
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, this.getConnection());
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			
			/**4.生成授权主表信息**/
			KeyedCollection authorizeKColl = new KeyedCollection("PvpAuthorize");
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",this.getConnection(), this.getContext());//生成授权编号
			
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", serno);//业务流水号（利率调整流水号）
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", bill_no);//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKLLXXWH + TradeConstance.SERVICE_SCENE_XDLLTZ);
			authorizeKColl.addDataField("tran_amt", 0.00);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", fina_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//利率调整授权编码
			authorizeKColl.addDataField("fldvalue02", "DUEBILL_NO@" + bill_no);//借据编号
//			authorizeKColl.addDataField("fldvalue03", "CLIENT_NO@" + cus_id);//客户编号
//			authorizeKColl.addDataField("fldvalue05", "CCY@" + cur_type);//币种
//			authorizeKColl.addDataField("fldvalue06", "LOAN_AMT@" + loan_amt);//贷款金额
//			authorizeKColl.addDataField("fldvalue07", "LOAN_BALANCE@" + TagUtil.formatDate(loan_balance));//贷款余额
			authorizeKColl.addDataField("fldvalue08", "DRAW_DOWN_DATE@" + TagUtil.formatDate(distr_date));//发放日期
//			authorizeKColl.addDataField("fldvalue09", "EXPIRY_DATE@" + end_date);//到期日期
			
//			authorizeKColl.addDataField("fldvalue10", "INT_ACCORD_TYPE@" + ir_accord_type);//利率依据方式
			authorizeKColl.addDataField("fldvalue11", "INT_KIND@" + ir_type);//利率种类
//			authorizeKColl.addDataField("fldvalue12", "BASE_INT_RATE@" + ruling_ir);//基准利率（年）
//			authorizeKColl.addDataField("fldvalue13", "FLUCTUATION_MODE@" + ir_adjust_type);//利率调整方式
//			authorizeKColl.addDataField("fldvalue14", "INTEST_RATE_FLUCT_MODE@" + ir_float_type);//利率浮动方式
			authorizeKColl.addDataField("fldvalue15", "INT_RATE_FLT_RATE@" + (ir_float_rate==null?0:ir_float_rate));//利率浮动比
			authorizeKColl.addDataField("fldvalue16", "INT_RATE_FLOW_SPREAD@" + (ir_float_point==null?0:ir_float_point));//利率浮动点数
//			authorizeKColl.addDataField("fldvalue17", "ADVANCE_INT_RATE@" + pad_rate_y);//垫款利率
			authorizeKColl.addDataField("fldvalue18", "ACT_INT_RATE@" + (reality_ir_y==null?0:reality_ir_y));//执行利率（年）
//			authorizeKColl.addDataField("fldvalue19", "OVERDUE_FLT_MODE@" + overdue_float_type);//逾期利率浮动方式
			authorizeKColl.addDataField("fldvalue20", "OVERDUE_FLT_RATE@" + (overdue_rate==null?0:overdue_rate));//逾期利率浮动比
			authorizeKColl.addDataField("fldvalue21", "OVERDUE_INT_RATE@" + (overdue_rate_y==null?0:overdue_rate_y));//逾期利率（年）
//			authorizeKColl.addDataField("fldvalue22", "PENALTY_INT_FLT_MODE@" + default_float_type);//违约利率浮动方式
			authorizeKColl.addDataField("fldvalue23", "PNY_INT_FLT_RATE@" + (default_rate==null?0:default_rate));//违约利率浮动比
//			authorizeKColl.addDataField("fldvalue24", "PNY_RATE_FLOW_SPREAD@" + default_point);//违约利率浮动点
			authorizeKColl.addDataField("fldvalue25", "PENALTY_INT_RATE@" + (default_rate_y==null?0:default_rate_y));//违约利率（年）
//			authorizeKColl.addDataField("fldvalue26", "BASE_INT_RATE_CODE@" + ruling_ir_code);//基准利率代码
			
			authorizeKColl.addDataField("fldvalue27", "NEW_INT_ACCORD_TYPE@" + new_ir_accord_type);//调整后利率依据方式
			authorizeKColl.addDataField("fldvalue28", "NEW_INT_KIND@" + new_ir_type);//调整后利率种类
//			authorizeKColl.addDataField("fldvalue29", "NEW_BASE_INT_RATE@" + new_ruling_ir);//调整后基准利率（年）
//			authorizeKColl.addDataField("fldvalue30", "NEW_ADV_INT_RATE@" + new_pad_rate_y);//调整后垫款利率（年）
			authorizeKColl.addDataField("fldvalue31", "NEW_FLT_MODE@" + new_ir_adjust_type);//调整后利率调整方式
			authorizeKColl.addDataField("fldvalue32", "NEW_RATE_FLUCT_MODE@" + new_ir_float_type);//调整后利率浮动方式
//			authorizeKColl.addDataField("fldvalue33", "NEW_OVERDUE_FLT_MODE@" + new_overdue_float_type);//调整后逾期利率浮动方式
//			authorizeKColl.addDataField("fldvalue34", "NEW_PNY_INT_FLT_MODE@" + new_default_float_type);//调整后违约利率浮动方式
			authorizeKColl.addDataField("fldvalue35", "NEW_PNY_INT_FLT_RATE@" + new_default_rate);//调整后违约利率浮动比
//			authorizeKColl.addDataField("fldvalue36", "NEW_PNY_FLOW_SPREAD@" + new_default_point);//调整后违约利率浮动点
			authorizeKColl.addDataField("fldvalue37", "NEW_PNT_INT_RATE@" + new_default_rate_y);//调整后违约利率（年）
			authorizeKColl.addDataField("fldvalue38", "NEW_INT_RATE_FLT_RATE@" +new_ir_float_rate);//调整后利率浮动比
			authorizeKColl.addDataField("fldvalue39", "NEW_INT_FLOW_SPREAD@" +new_ir_float_point);//调整后利率浮动点数
			authorizeKColl.addDataField("fldvalue40", "NEW_ACT_INT_RATE@" + new_reality_ir_y);//调整后执行利率
			authorizeKColl.addDataField("fldvalue41", "NEW_OVERDUE_FLT_RATE@" +new_overdue_rate);//调整后逾期利率浮动比
			authorizeKColl.addDataField("fldvalue42", "NEW_OVERDUE_INT_RATE@" +new_overdue_rate_y);//调整后逾期利率
//			authorizeKColl.addDataField("fldvalue43", "NEW_BASE_RATE_CODE@" + new_ruling_ir_code);//基准利率代码
			authorizeKColl.addDataField("fldvalue44", "NEW_INT_EFF_DATE@" + TagUtil.formatDate(new_inure_date));//调整后利率生效日期
			//chenBQ 20190409判断基准利率
			String IntRateTp = "C1";//核心基准利率类型
			int termDays = 0;//贷款期限日
			if("001".equals(term_type)) {
				termDays =360*cont_term;
			} else if ("002".equals(term_type)) {
				termDays =30*cont_term;
			} else if("003".equals(term_type)) {
				termDays=cont_term;
			}
			if(termDays<=360)
				IntRateTp = "C1";
			else if(termDays>360 && termDays<=1800)
				IntRateTp = "C3";
			else if (termDays>1800)
				IntRateTp = "C5";
			authorizeKColl.addDataField("fldvalue45", "IntRateTp@" + IntRateTp);//利率类型，核心利率类型 C1/C3/C5
			dao.insert(authorizeKColl, this.getConnection());
			
			
			/**5.发送授权信息**/
			ESBServiceInterface esbService = (ESBServiceInterface)jndiService.getModualServiceById("esbServices", "esb");
			KeyedCollection returnKColl = esbService.tradeLVTZ(serno, this.getContext(), this.getConnection());
			IndexedCollection retArr=(IndexedCollection)returnKColl.getDataElement("RetInfArry");
			KeyedCollection retObj=(KeyedCollection)retArr.get(0);
			String retCd=(String)retObj.getDataValue("RetCd");
			//String retCd = (String) returnKColl.getDataValue("RET_CODE");//000000-交易成功; 否则失败
			if(!"000000".equals(retCd)){
				throw new EMPException("交易失败！"+(String) retObj.getDataValue("RetInf"));
			}else{
				KeyedCollection authKColl = authorizeKColl;
				String authorize_no = (String) authKColl.getDataValue("authorize_no");
				//KeyedCollection reflectKColl = TagUtil.getReflectKColl(authorizeKColl);
				//成功后更新借据表
				KeyedCollection iqpkColl = (KeyedCollection)SqlClient.queryFirst("queryRateAppByAuthorizeNo", authorize_no, null, this.getConnection());
				Map value = new HashMap();
				value.put("ruling_ir", iqpkColl.getDataValue("ruling_ir"));
				value.put("ir_float_rate", TagUtil.replaceNull4Double(new_ir_float_rate==null?0:new_ir_float_rate));
				value.put("ir_float_point", TagUtil.replaceNull4Double(new_ir_float_point==null?0:new_ir_float_point));
				value.put("reality_ir_y", TagUtil.replaceNull4Double(new_reality_ir_y==null?0:new_reality_ir_y));
				value.put("overdue_rate_y", TagUtil.replaceNull4Double(new_overdue_rate_y==null?0:new_overdue_rate_y));
				value.put("default_rate_y", TagUtil.replaceNull4Double(new_default_rate_y==null?0:new_default_rate_y));
				SqlClient.update("updateAccRateInfo", authKColl.getDataValue("bill_no"), value, null, this.getConnection());
				//根据授权编号更新授权表的状态为授权已确认
				SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "04", null, this.getConnection());
			}
		}catch (Exception e) {
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ComponentException("错误信息："+e.getMessage());
		}
	}
}
