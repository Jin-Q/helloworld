package com.yucheng.cmis.biz01line.lmt.riskmanage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * 检查客户经理授信限额
 * @author QZCB
 *
 */
public class LmtCheckCusMgrLmtAmt implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		Map<String,String> outMap=new HashMap<String,String>();
		
		String manager_id = "";
		String agr_no = "";
		BigDecimal sigAmtAll = null;//客户经理授信限额
		BigDecimal sigAmt = null;//客户经理单户授信限额
		//先获取主管客户经理
		KeyedCollection kcoll = dao.queryDetail("LmtAppIndiv", serno, connection);
		manager_id = (String)kcoll.getDataValue("manager_id");
		String cus_id = (String)kcoll.getDataValue("cus_id");
		String lrisk_type = (String)kcoll.getDataValue("lrisk_type");//是否低风险标志
		String openDay = (String)context.getDataValue("OPENDAY");
		
		BigDecimal sigAmtAllTmp = null;
		BigDecimal AmtThis = null;
		KeyedCollection parm = new KeyedCollection();
		parm.put("openDay", openDay);
		parm.put("managerId", manager_id);
		parm.put("cusId", cus_id);
		IndexedCollection ic = SqlClient.queryList4IColl("getIndivLmtAmtByMgrId", parm, connection);
		if(ic.size()>0){
			KeyedCollection kCollTmp = (KeyedCollection)ic.get(0);
			sigAmtAllTmp = (BigDecimal)kCollTmp.getDataValue("amt_tol");//客户经理其他金额汇总
		}
		parm.clear();
		parm.put("managerId", manager_id);
		parm.put("serno", serno);
		//查询在途授信金额
		IndexedCollection icOnWay = SqlClient.queryList4IColl("getIndivLmtWayAmtByMgrId", parm, connection);
		if(icOnWay.size()>0){
			KeyedCollection kCollTmp = (KeyedCollection)icOnWay.get(0);
			BigDecimal onWayAmt = (BigDecimal)kCollTmp.getDataValue("amt_tol");
			sigAmtAllTmp = sigAmtAllTmp.add(onWayAmt);
		}
		//获取该笔申请授信总额
		LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance()
		.getComponentInstance("LmtPubComponent", context,connection);
		KeyedCollection kCollAmt = lmtComponent.selectLmtAppIndivAmt(serno,"LMT_APP_DETAILS");
		AmtThis = (BigDecimal)kCollAmt.getDataValue("total_amt");
		
		sigAmtAllTmp = sigAmtAllTmp.add(AmtThis);//该客户经理实际授信总额
		
		String condition = "WHERE CODE_ID='"+manager_id+"'";
		IndexedCollection iCollQuo = dao.queryList("LmtQuotaManager", condition, connection);
		if(iCollQuo.size()>0){
			KeyedCollection kCollQuo = (KeyedCollection)iCollQuo.get(0);
			sigAmtAll = new BigDecimal((String)kCollQuo.getDataValue("single_amt_quota"));
			sigAmt = new BigDecimal((String)kCollQuo.getDataValue("sig_amt_quota"));
			if(sigAmtAllTmp.compareTo(sigAmtAll)<=0){
				outMap.put("OUT_是否通过", "通过");
				outMap.put("OUT_提示信息", "没有超过该客户经理授信限额");
			}else{
				outMap.put("OUT_是否通过", "不通过");
				outMap.put("OUT_提示信息", "超过该客户经理授信限额");
			}
			
			if("通过".equals(outMap.get("OUT_是否通过"))){
				//查询该客户在途授信申请信息
				String condTmp = "where cus_id='"+cus_id+"' and lrisk_type<>'"+lrisk_type+"' and approve_status not in('000','997','998')";
				IndexedCollection iCollApp = dao.queryList("LmtAppIndiv", condTmp, connection);
				if(iCollApp.size()>0){
					KeyedCollection kCollApp = (KeyedCollection)iCollApp.get(0);
					String sernoTmp = (String)kCollApp.getDataValue("serno");
					IndexedCollection icTmp = SqlClient.queryList4IColl("getIndivLmtAmtBySerno", sernoTmp, connection);
					if(icTmp.size()>0){
						KeyedCollection kCollTmp = (KeyedCollection)icTmp.get(0);
						BigDecimal tmpAmt = (BigDecimal)kCollTmp.getDataValue("amt_tol");//在途总额
						AmtThis = tmpAmt.add(AmtThis);
					}
					if(AmtThis.compareTo(sigAmt)>0){
						outMap.put("OUT_是否通过", "不通过");
						outMap.put("OUT_提示信息", "超过该客户经理单户授信限额");
					}else{
						outMap.put("OUT_是否通过", "通过");
						outMap.put("OUT_提示信息", "没有超过该客户经理单户授信限额");
					}
				}else{
					//查询该客户原有有效授信协议
					condTmp = "where cus_id='"+cus_id+"' and agr_status='002' and totl_end_date>'"+openDay+"'";
					IndexedCollection iCollAgr = dao.queryList("LmtAgrIndiv", condTmp, connection);
					if(iCollAgr.size()>0){
						KeyedCollection kCollAgr = (KeyedCollection)iCollAgr.get(0);
						agr_no = (String)kCollAgr.getDataValue("agr_no");
						IndexedCollection icTmp = SqlClient.queryList4IColl("selectLmtAppIndivAmtByAgr", agr_no, connection);
						if(icTmp.size()>0){
							KeyedCollection kCollTmp = (KeyedCollection)icTmp.get(0);
							if("10".equals(lrisk_type)){
								BigDecimal tmpAmt = (BigDecimal)kCollTmp.getDataValue("total_amt");//非低风险总额
								AmtThis = tmpAmt.add(AmtThis);
							}else{
								BigDecimal tmpAmt = (BigDecimal)kCollTmp.getDataValue("lrisk_total_amt");//低风险总额
								AmtThis = tmpAmt.add(AmtThis);
							}
						}
						if(AmtThis.compareTo(sigAmt)>0){
							outMap.put("OUT_是否通过", "不通过");
							outMap.put("OUT_提示信息", "超过该客户经理单户授信限额");
						}else{
							outMap.put("OUT_是否通过", "通过");
							outMap.put("OUT_提示信息", "没有超过该客户经理单户授信限额");
						}
					}else{
						if(AmtThis.compareTo(sigAmt)>0){
							outMap.put("OUT_是否通过", "不通过");
							outMap.put("OUT_提示信息", "超过该客户经理单户授信限额");
						}else{
							outMap.put("OUT_是否通过", "通过");
							outMap.put("OUT_提示信息", "没有超过该客户经理单户授信限额");
						}
					}
				}
			}
		}else{
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "该客户经理未设置授信限额");
		}
		
		return outMap;
	}

}
