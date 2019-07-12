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
*@author wangj
*@time 2015-5-07
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class RiskManageCtrSigleLmtAppDetailsImple implements RiskManageInterface{

	private final String appmodelId = "LmtAppDetails";//业务申请表
	private final String agrmodelId = "LmtAgrDetails";//业务表
	private final String wlormodelId = "WfiLvOverdrawnRight";//控制透支额度配置表
	private final String sqlId1 = "queryOrgOverdrawnCredAmt";//查询机构下所有的透支总额度
	private final String sqlId2 = "queryOtherCtrlmt";//查询客户是否存在审批中的法人透支额度
	private final String sqlId3 = "queryCurOverdrawnCredAmt";// 查询客户提交时的法人透支额度
	private  final String belg_line = "BL100";//    /* BL100 公司业务条线 BL200 小微业务条线 */
	/*单一法人授信法人账户透支 ：有且只能有一个有效的
	 * 1、检查是否存在多个授信品种为法人账户透支的授信分项
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
			if("02".equals(lmt_type)){
				String condition=" where serno ='" + serno+"' and limit_name='100088' ";
				IndexedCollection iColl = dao.queryList(appmodelId, condition, connection);
				if(iColl!=null&&iColl.size()>0){
					 returnFlag = "不通过";
					 returnInfo = "该授信申请的授信类别为公司条线授信，不能存在小微自助循环贷款的授信分项,请检查！";
					 returnMap.put("OUT_是否通过", returnFlag);
					 returnMap.put("OUT_提示信息", returnInfo);
					 return returnMap;
				}
			}
			String condition=" where serno ='" + serno+"' and limit_name='100051' ";
			IndexedCollection iColl = dao.queryList(appmodelId, condition, connection);
		
		   if(iColl==null||iColl.size()==0){//不存在法人透支额度
			   returnFlag = "通过";
			   returnInfo = "不存在授信品种为法人账户透支的授信分项,检查通过！";
		   }else if(iColl!=null&&iColl.size()>1){
			   returnFlag = "不通过";
			   returnInfo = "存在多个授信品种为法人账户透支的授信分项,请检查！";
		   }else{
			   KeyedCollection appkColl=(KeyedCollection) iColl.get(0);
			   String cusId=(String) appkColl.getDataValue("cus_id");//获取客户号
			   //String limit_code=(String) appkColl.getDataValue("limit_code");//获取额度编号
			   String lrisk_type=(String) appkColl.getDataValue("lrisk_type");//低风险业务类型
			   String guar_type=(String) appkColl.getDataValue("guar_type");//担保方式  /*100-抵押  200-质押 210-准全额质押  220-低风险质押 300-保证 400-信用*/
			   String show="低风险";//10 低风险 20 非低风险
			   if("10".equals(lrisk_type)){
				   show="非低风险";
			   }
			   condition=" where CUS_ID ='" + cusId+"' and limit_name='100051' and lrisk_type<>'"+lrisk_type+"' and lmt_status not in ('00', '30')  ";
			   IndexedCollection agriColl = dao.queryList(agrmodelId, condition, connection);
			   if(agriColl!=null&&agriColl.size()>0){
				   returnFlag = "不通过";
				   returnInfo = "该客户的"+show+"业务存在已生效或冻结的法人账户透支额度,请检查！";
				} else {
					KeyedCollection paramKcoll = new KeyedCollection(); // Sql参数
					paramKcoll.put("cus_id", cusId);
					paramKcoll.put("serno", serno);
					KeyedCollection overdrawnKcoll = (KeyedCollection) SqlClient.queryFirst(sqlId2, paramKcoll, null, conn);
					if (overdrawnKcoll != null &&overdrawnKcoll.containsKey("serno")&& !"".equals(overdrawnKcoll.getDataValue("serno"))
							&& overdrawnKcoll.getDataValue("serno")!=null) {
						returnFlag = "不通过";
						returnInfo = "该客户存在审批中的法人账户透支,请检查！";
					} else {
					    KeyedCollection CusInfo = dao.queryDetail("CusBase", cusId,connection);//获得客户信息
					    String mainBrId=(String) CusInfo.getDataValue("main_br_id");//获取主管机构号
						BigDecimal curCrdAmt=new BigDecimal(0);//提交时的额度金额
						KeyedCollection curOverdrawnKcoll = (KeyedCollection) SqlClient.queryFirst(sqlId3, paramKcoll, null, conn);
						if (curOverdrawnKcoll != null && curOverdrawnKcoll.containsKey("curcrdamt")) {
							curCrdAmt =BigDecimalUtil.replaceNull(curOverdrawnKcoll.getDataValue("curcrdamt"));
						}
						
						KeyedCollection OrgInfo = dao.queryDetail("SOrg", mainBrId,connection);//获取主管机构信息
						String locate=(String) OrgInfo.getDataValue("locate");//获取位置属性  例如机构码为9350581003 位置属性 为9350000000,9350500000,9350581003
						String[] organnos=locate.split(",");
						if(isInCtrl(organnos,belg_line,guar_type,curCrdAmt,dao,connection)){ /*单户限额控制 */
							returnFlag = "通过";
							returnInfo = "法人账户透支总额度不控制,检查通过！";
							for(int i=organnos.length-1;i>=0;i--){//顺序：支行->分行->总行 9350581003->9350500000->9350000000
								IndexedCollection wfiLvOverdrawnRightList = dao.queryList(wlormodelId, " where org_id='"+organnos[i]+"' and belg_line='"+belg_line+"'",connection);//获取主管机构信息
								if(wfiLvOverdrawnRightList==null||wfiLvOverdrawnRightList.isEmpty()){
									continue;
								}
								KeyedCollection wfiLvOverdrawnRightInfo=(KeyedCollection) wfiLvOverdrawnRightList.get(0);
								String isInUse=(String) wfiLvOverdrawnRightInfo.getDataValue("is_inuse");
								if("1".equals(isInUse)){
									BigDecimal overdrawn_amt=BigDecimalUtil.replaceNull(wfiLvOverdrawnRightInfo.getDataValue("overdrawn_amt"));//机构的控制透支额度
									paramKcoll.put("organno", organnos[i]);
									KeyedCollection overdrawnKcoll2 = (KeyedCollection) SqlClient.queryFirst(sqlId1, paramKcoll, null, conn);
									if (overdrawnKcoll2 != null && overdrawnKcoll2.containsKey("crdamt")) {
										BigDecimal OrgcrdAmt =BigDecimalUtil.replaceNull(overdrawnKcoll2.getDataValue("crdamt"));
										BigDecimal CurOrgcrdAmt = OrgcrdAmt.add(curCrdAmt);// 机构所占额度+提交时的申请额度
										if (CurOrgcrdAmt.compareTo(overdrawn_amt) > 0) {// 机构所占额度+提交时的申请额度>机构透支控制总额度
											returnFlag = "不通过";
											returnInfo = "机构["+ organnos[i]
													+ "]所占的法人透支额度+本次申请的透支额度超过法人账户透支总额度"
													+ "[" + OrgcrdAmt + "+"+ curCrdAmt + "="+ CurOrgcrdAmt + ">"+ overdrawn_amt + "],请检查！";
											break;
										}
									}
									returnFlag = "通过";
									returnInfo = "本次申请的透支额度没有超过当前机构的法人账户透支总额度,检查通过！";
								}
							}							
						}else{
							returnFlag = "不通过";
							String str="";
							IndexedCollection wlorList=null;
							for(int i=organnos.length-1;i>=0;i--){//顺序：支行->分行->总行 9350581003->9350500000->9350000000
								wlorList= dao.queryList(wlormodelId, " where org_id='"+organnos[i]+"' and belg_line='"+belg_line+"' and is_ctrl='1' ",connection);//获取主管机构信息
								if(wlorList!=null&&!wlorList.isEmpty()) break;//如果支行已经控制就按照支行限额比较，如果支行没有控制则查询分行或总行是否控制
							}
							KeyedCollection wlorInfo=(KeyedCollection) wlorList.get(0);
							BigDecimal ctrlAmt=new BigDecimal(0);//控制限额
							if("100".equals(guar_type)){
								str="抵押限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("impawn_amt"));
							}else if("200".equals(guar_type)){
								str="质押限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("pledge_amt"));
							}else if("210".equals(guar_type)){
								str="准全额质押限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("fullpledge_amt"));
							}else if("220".equals(guar_type)){
								str="低风险质押限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("riskpledge_amt"));
							}else if("300".equals(guar_type)){
								str="保证限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("guarantee_amt"));
							}else if("400".equals(guar_type)){
								str="信用限额";
								ctrlAmt=BigDecimalUtil.replaceNull(wlorInfo.getDataValue("credit_amt"));
							}
							returnInfo = "本次申请的法人账户透支额度【"+curCrdAmt+"】超过该客户主管机构或其上级机构的"+str+"【"+ctrlAmt+"】,请检查！";
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
	private boolean isInCtrl(String[] organnos,String belg_line,String guar_type,BigDecimal curCrdAmt,TableModelDAO dao,Connection connection) throws Exception{
		IndexedCollection wlorList=null;
		for(int i=organnos.length-1;i>=0;i--){//顺序：支行->分行->总行 9350581003->9350500000->9350000000
			wlorList= dao.queryList(wlormodelId, " where org_id='"+organnos[i]+"' and belg_line='"+belg_line+"' and is_ctrl='1' ",connection);//获取主管机构信息
			if(wlorList!=null&&!wlorList.isEmpty()) break;//如果支行已经控制就按照支行限额比较，如果支行没有控制则查询分行或总行是否控制
		}
		
		if(wlorList!=null&&!wlorList.isEmpty()){
			KeyedCollection wlorInfo=(KeyedCollection) wlorList.get(0);
			String is_ctrl=(String) wlorInfo.getDataValue("is_ctrl");
			if("1".equals(is_ctrl)){  /*guar_type 担保方式 :100-抵押  200-质押 210-准全额质押  220-低风险质押 300-保证 400-信用*/
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
				if(isInCtrl&&curCrdAmt.compareTo(ctrlAmt)>0){
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
