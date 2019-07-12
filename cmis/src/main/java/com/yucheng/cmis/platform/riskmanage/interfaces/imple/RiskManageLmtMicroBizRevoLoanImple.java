package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-5-27
*@description TODO 需求编号：【XD150123005】小微自助循环贷款改造
*@version v1.0
*
 */
public class RiskManageLmtMicroBizRevoLoanImple implements RiskManageInterface{

	private final String appmodelId = "LmtAppDetails";//授信申请表
	private final String agrmodelId = "LmtAgrDetails";//授信额度台账表
	private final String wlormodelId = "WfiLvOverdrawnRight";//控制透支额度配置表（条线：小微）
	private final String sqlId1 = "queryOrgMicroBizCrdAmt";//查询机构下所有小微自助循环贷总额度
	private final String sqlId2 = "queryOtherMicroBizLmt";//查询客户是否存在审批中的小微自助循环贷额度
	private final String sqlId3 = "queryCurMicroBizCrdAmt";// 查询客户提交时的小微自助循环贷额度
	
	/*小微自助循环贷款 ：有且只能有一个有效的
	 * 1、检查授信分项是否存在多个品种为小微自助循环贷款的分项
	 * 2、检查透支额度是否超过机构透支总额度
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection lmtApplyKColl=dao.queryDetail("LmtApply",serno, connection);
			String lmt_type=TagUtil.replaceNull4String(lmtApplyKColl.getDataValue("lmt_type"));
			if("01".equals(lmt_type)){
				String condition=" where serno ='" + serno+"' and limit_name='100051' ";
				IndexedCollection iColl = dao.queryList(appmodelId, condition, connection);
				if(iColl!=null&&iColl.size()>0){
					 returnFlag = "不通过";
					 returnInfo = "该授信申请的授信类别为小微条线授信，不能存在法人账户透支的授信分项,请检查！";
					 returnMap.put("OUT_是否通过", returnFlag);
					 returnMap.put("OUT_提示信息", returnInfo);
					 return returnMap;
				}
			}
			String condition=" where serno ='" + serno+"' and limit_name='100088' ";
			IndexedCollection iColl = dao.queryList(appmodelId, condition, connection);
		
		   if(iColl==null||iColl.size()==0){//不存在分项为小微自助循环贷款品种
			   returnFlag = "通过";
			   returnInfo = "该授信不属于小微自助循环贷授信,检查通过！";
		   }else if(iColl!=null&&iColl.size()>1){
			   returnFlag = "不通过";
			   returnInfo = "小微自助循环贷不允许存在多个授信分项,请检查！";
		   }else{
			   KeyedCollection appkColl=(KeyedCollection) iColl.get(0);
			   String cusId=(String) appkColl.getDataValue("cus_id");//获取客户号

			   String lrisk_type=(String) appkColl.getDataValue("lrisk_type");//低风险业务类型
			   String guar_type=(String) appkColl.getDataValue("guar_type");//担保方式 
			   String show="非低风险";
			   if("10".equals(lrisk_type)){
				   show="低风险";
			   }
			   condition=" where cus_id ='" + cusId+"' and limit_name='100088' and lrisk_type<>'"+lrisk_type+"' and lmt_status not in ('00', '30') ";
			   IndexedCollection agriColl = dao.queryList(agrmodelId, condition, connection);
			   if(agriColl!=null&&agriColl.size()>0){
				   returnFlag = "不通过";
				   returnInfo = "该客户的"+show+"业务已存在生效或冻结的小微自助循环贷额度,请检查！";
				}else{
					KeyedCollection paramKcoll = new KeyedCollection(); // Sql参数
					paramKcoll.put("cus_id", cusId);
					paramKcoll.put("serno", serno);
					KeyedCollection overdrawnKcoll = (KeyedCollection) SqlClient.queryFirst(sqlId2, paramKcoll, null, conn);
					if(overdrawnKcoll!=null && overdrawnKcoll.containsKey("serno") && (overdrawnKcoll.getDataValue("serno"))!=null
							&& !"".equals(overdrawnKcoll.getDataValue("serno"))){
						returnFlag = "不通过";
						returnInfo = "该客户已存在审批中的小微自助循环贷款授信,不允许重新发起授信！";
					}else{
					    KeyedCollection CusInfo = dao.queryDetail("CusBase", cusId,connection);//获得客户信息
					    String mainBrId=(String) CusInfo.getDataValue("main_br_id");//获取主管机构号
						BigDecimal curCrdAmt=new BigDecimal(0);//提交时的额度金额
						KeyedCollection curOverdrawnKcoll = (KeyedCollection) SqlClient.queryFirst(sqlId3, paramKcoll, null, conn);
						if (curOverdrawnKcoll!=null && curOverdrawnKcoll.containsKey("curcrdamt") && (curOverdrawnKcoll.getDataValue("curcrdamt"))!=null 
								&& !"".equals(curOverdrawnKcoll.getDataValue("curcrdamt"))) {
							curCrdAmt = BigDecimalUtil.replaceNull(curOverdrawnKcoll.getDataValue("curcrdamt"));
						}
						
					    /*单户限额控制 */
						if(isInCtrl(mainBrId,guar_type,curCrdAmt,dao,connection)){
							KeyedCollection OrgInfo = dao.queryDetail("SOrg", mainBrId,connection);//获取主管机构信息
							String locate=(String) OrgInfo.getDataValue("locate");//获取位置属性  例如机构码为9350581003 位置属性 为9350000000,9350500000,9350581003
							String[] organnos=locate.split(","); 
							returnFlag = "通过";
							returnInfo = "小微自助循环贷款总额度不控制,检查通过！";
							for(int i=organnos.length-1;i>=0;i--){//顺序：支行->分行->总行 9350581003->9350500000->9350000000
								IndexedCollection wfiLvOverdrawnRightList = dao.queryList(wlormodelId, " where org_id='"+organnos[i]+"'  and belg_line='BL200' ",connection);//获取主管机构信息
								if(wfiLvOverdrawnRightList==null||wfiLvOverdrawnRightList.isEmpty()){
									continue;
								}
								KeyedCollection wfiLvOverdrawnRightInfo=(KeyedCollection) wfiLvOverdrawnRightList.get(0);
								String isInUse=(String) wfiLvOverdrawnRightInfo.getDataValue("is_inuse");
								if("1".equals(isInUse)){
									BigDecimal overdrawn_amt=BigDecimalUtil.replaceNull(wfiLvOverdrawnRightInfo.getDataValue("overdrawn_amt"));//机构的控制总吸限额
									paramKcoll.put("organno", organnos[i]);
									KeyedCollection overdrawnKcoll2 = (KeyedCollection) SqlClient.queryFirst(sqlId1, paramKcoll, null, conn);
									if (overdrawnKcoll2 != null && overdrawnKcoll2.containsKey("crdamt")) {
										BigDecimal OrgcrdAmt = (BigDecimal) overdrawnKcoll2.getDataValue("crdamt");
										BigDecimal CurOrgcrdAmt = OrgcrdAmt.add(curCrdAmt);// 机构所占额度+提交时的申请额度
										if (CurOrgcrdAmt.compareTo(overdrawn_amt) > 0) {// 机构所占额度+提交时的申请额度>机构透支控制总额度
											returnFlag = "不通过";
											returnInfo = "机构号["+ organnos[i]
													+ "]所占的小微自助循环贷额度+本次申请的小微自助循环贷额度超过小微自助循环贷总限额"
													+ "[" + OrgcrdAmt + "+"+ curCrdAmt + "="+ CurOrgcrdAmt + ">"+ overdrawn_amt + "],请检查！";
											break;
										}
									}
									returnFlag = "通过";
									returnInfo = "本次申请的小微自助循环贷额度没有超过当前机构的总限额,检查通过！";
								}
							}							
						}else{
							returnFlag = "不通过";
							String str="";
							if("100".equals(guar_type)){
								str="抵押限额";
							}else if("200".equals(guar_type)){
								str="质押限额";
							}else if("210".equals(guar_type)){
								str="准全额质押限额";
							}else if("220".equals(guar_type)){
								str="低风险质押限额";
							}else if("300".equals(guar_type)){
								str="保证限额";
							}else if("400".equals(guar_type)){
								str="信用限额";
							}
							returnInfo = "本次申请的小微自助循环贷额度超过该客户主管机构的"+str+",请检查！";
						}
						
					}
			   }
			  
		   }
		   returnMap.put("OUT_是否通过", returnFlag);
		   returnMap.put("OUT_提示信息", returnInfo);
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (conn != null)
				this.releaseConnection(dataSource, conn);
		}
		return returnMap;
	}
	/**
	 * 判断是否超过客户主管机构的单户限额
	 * @param orgid 客户主管机构
	 * @param guar_type 担保类型
	 * @param curCrdAmt 当前申请额度
	 * @param dao 
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	private boolean isInCtrl(String orgid,String guar_type,BigDecimal curCrdAmt,TableModelDAO dao,Connection connection) throws Exception{
		IndexedCollection wlorList = dao.queryList(wlormodelId, " where org_id='"+orgid+"' and belg_line='BL200'",connection);//获取主管机构信息
		if(wlorList!=null && wlorList.size()>0){
			KeyedCollection wlorInfo=(KeyedCollection) wlorList.get(0);
			String is_ctrl=(String) wlorInfo.getDataValue("is_ctrl");
			if("1".equals(is_ctrl)){
				BigDecimal ctrlAmt=new BigDecimal(0);//控制限额
				boolean isInCtrl=true;
				if("100".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("impawn_amt"));
				}else if("200".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("pledge_amt"));
				}else if("210".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("fullpledge_amt"));
				}else if("220".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("riskpledge_amt"));
				}else if("300".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("guarantee_amt"));
				}else if("400".equals(guar_type)){
					ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("credit_amt"));
				}else{
					isInCtrl=false;
				}
				if(isInCtrl && curCrdAmt.compareTo(ctrlAmt)>0){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 获取数据库连接
	 * 
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	private Connection getConnection(Context context, DataSource dataSource)
			throws EMPJDBCException, SessionException {
		if (dataSource == null)
			throw new SessionException("登陆超时，请重新登陆或联系管理员 !"
					+ this.toString());
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		
		EMPLog.log( EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Apply new connection from data source: "+dataSource+" success!");
		return connection;
	}
	
	/**
	 * 释放数据库连接
	 * 
	 * @param dataSource
	 * @param connection
	 * @throws EMPJDBCException
	 */
	private void releaseConnection(DataSource dataSource, Connection connection)
			throws EMPJDBCException {
		ConnectionManager.releaseConnection(dataSource, connection);
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Do release the connection from data source: " + dataSource + " success!");
	}
}
