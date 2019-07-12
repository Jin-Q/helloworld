package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2014-12-15
*@description  需求:【XD140818051】 出账队列历史管理
*@version v1.0
*
 */
public class QueryGroupPvpHistoryListOp extends CMISOperation {

	private final String modelId = "PvpLoanApp";
	private final String modelIdCont = "CtrLoanCont";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String conditionStr ="";
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelIdCont);
			} catch (Exception e) {}
			/** 记录集权限 */
			context.put("menuId", "GroupPvpHis");
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection); 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "5000");
			if(queryData!=null && queryData.containsKey("cont_no")&&queryData.getDataValue("cont_no")!=null&&!"".equals(queryData.getDataValue("cont_no"))){
				conditionStr += " and p2.cont_no like '%"+queryData.getDataValue("cont_no")+"%'";
			}
			if(queryData!=null && queryData.containsKey("cus_id")&&queryData.getDataValue("cus_id")!=null &&!"".equals(queryData.getDataValue("cus_id"))){
				conditionStr += " and p2.cus_id like '%"+queryData.getDataValue("cus_id")+"%'";
			}
			conditionStr +=" and P1.CONT_NO = P2.CONT_NO AND P2.MANAGER_BR_ID = P3.ORG_NO(+) order by INPUT_DATE desc ";		
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conditionSelect = " SELECT P2.SERNO,                                                    "
									+"        P2.CONT_NO,                                                  "
									+"        P2.CN_CONT_NO,                                               "
									+"        P2.CUS_ID,                                                   "
									+"        P2.BIZ_TYPE,                                                 "
									+"        P2.BELONG_NET,                                               "
									+"        P2.RENT_TYPE,                                                "
									+"        P2.PRD_ID,                                                   "
									+"        P2.PRD_DETAILS,                                              "
									+"        P2.IS_PROMISSORY_NOTE,                                       "
									+"        P2.PROMISSORY_NOTE,                                          "
									+"        P2.ASSURE_MAIN,                                              "
									+"        P2.ASSURE_MAIN_DETAILS,                                      "
									+"        P2.IS_TRUST_LOAN,                                            "
									+"        P2.TRUST_COMPANY,                                            "
									+"        P2.CONT_CUR_TYPE,                                            "
									+"        P2.CONT_AMT,                                                 "
									+"        P2.CONT_BALANCE,                                             "
									+"        P2.CONT_AMT pvp_amt,                                         "
									+"        P2.CONT_START_DATE,                                          "
									+"        P2.CONT_END_DATE,                                            "
									+"        P2.EXCHANGE_RATE,                                            "
									+"        P2.SECURITY_RATE,                                            "
									+"        P2.SECURITY_AMT,                                             "
									+"        P2.CONT_NUMBER,                                              "
									+"        P2.CONT_STATUS,                                              "
									+"        P2.MANAGER_BR_ID,                                            "
									+"        P2.INPUT_ID,                                                 "
									+"        P2.INPUT_BR_ID,                                              "
									+"        P1.INPUT_DATE,                                               "
									+"        P3.LOADEPORAT                                                "
									+"   FROM PVP_GROUP_INPUT_INFO P1, CTR_LOAN_CONT P2, ORG_LOADEPORAT P3 " +conditionStr;
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, dataSource, conditionSelect);

			iColl.setName("CtrLoanContHistoryList");
			String[] args=new String[] { "prd_id","cus_id","cont_no" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase","CtrLoanContSub"};
			String[]modelForeign=new String[]{"prdid ","cus_id","cont_no"};
			String[] fieldName=new String[]{"prdname","cus_name","is_close_loan"};
			String[] resultName = new String[] { "prdname","cus_name","is_close_loan" };
			//详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName, resultName);
		    SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
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
