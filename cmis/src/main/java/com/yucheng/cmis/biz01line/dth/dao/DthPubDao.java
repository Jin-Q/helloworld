package com.yucheng.cmis.biz01line.dth.dao;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class DthPubDao extends CMISDao {
	
	/**
	 * 行长驾驶舱专用无返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @throws DaoException 
	 */
	public void delSqlNoReturn(KeyedCollection kcoll) throws DaoException {
		try {
			if(kcoll.containsKey("submitType")){	//一般情况下传submitType进行区分
				String submitType = kcoll.getDataValue("submitType").toString();
				if (1 == 0){
					
				}else{	/***** 默认则是直接取Key_Value进行更新操作 ******/
					SqlClient.executeUpd(submitType,  kcoll.getDataValue("Key_Value"), null, null, this.getConnection());
				}
			}else{	//至于没传submitType的情况不建议写在这里
				
			}
		} catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
	}
	
	/**
	 * 行长驾驶舱专用带返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 * @throws DaoException 
	 */
	public KeyedCollection delSqlReturnKcoll(KeyedCollection kcoll) throws DaoException {
		Object results = "";
		try{
			if(kcoll.containsKey("submitType")){	//一般情况下传submitType进行区分
				String submitType = kcoll.getDataValue("submitType").toString();
				if (1 == 0){
					
				}else{	/***** 默认则是直接取Key_Value进行查询 ******/
					results = (Object) SqlClient.queryFirst(submitType, kcoll.getDataValue("Key_Value"), null, this.getConnection());
					kcoll.addDataField("results", results);
				}
			}else{	//至于没传submitType的情况不建议写在这里
				
			}
		}catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
		return kcoll;
	}
	
	/**
	 * 行长驾驶舱专用，返回不带分页icoll的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return results 返回icoll
	 * @throws DaoException 
	 */	
	public IndexedCollection delSqlReturnIcoll(KeyedCollection kcoll) throws DaoException {
		IndexedCollection results = new IndexedCollection();
		try{			
			if(kcoll.containsKey("submitType")){	//一般情况下传submitType进行区分
				String submitType = kcoll.getDataValue("submitType").toString();
				if(submitType.equals("getLmtWarnChartEight")||submitType.equals("getLmtWarnChartNine")
						||submitType.equals("getLmtWarnChartTen")||submitType.equals("getLmtWarnChartEleven")){//担保下十大户处理
					
					String organNo = kcoll.getDataValue("organNo").toString() ;
					String br_type = getBrtype(organNo);
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("br_type", br_type);
					paramMap.put("organNo", organNo);
					paramMap.put("guar_type", kcoll.getDataValue("guar_type").toString());
					results = (IndexedCollection)SqlClient.queryList4IColl("getLmtWarnChartGuar", paramMap,this.getConnection());
				}else if(submitType.equals("getCostWarnChartFiveclass")){//五级分类下业务处理 BLall,BL100,BL200,BL300
					String class_type = kcoll.getDataValue("class_type").toString();
					String organNo = kcoll.getDataValue("organNo").toString() ;
					String br_type = getBrtype(organNo);
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("br_type", br_type);
					paramMap.put("organNo", organNo);
					if(class_type.equals("BLall")){
						results = (IndexedCollection)SqlClient.queryList4IColl("getCostWarnChartFour", paramMap,this.getConnection());
					}else{
						paramMap.put("belg_line", class_type);
						results = (IndexedCollection)SqlClient.queryList4IColl("getCostWarnChartFiveSeven", paramMap,this.getConnection());
					}
				}else if(submitType.equals("getCostWarnTenCus")){//用信十大户处理
					String prd_type = kcoll.getDataValue("prd_type").toString();
					String organNo = kcoll.getDataValue("organNo").toString() ;
					String br_type = getBrtype(organNo);
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("br_type", br_type);
					paramMap.put("organNo", organNo);
					if(prd_type.equals("YX")){	//用信单独处理
						results = (IndexedCollection)SqlClient.queryList4IColl("getCostWarnChartEight", null,this.getConnection());
					}else if(prd_type.equals("DK")){
						results = (IndexedCollection)SqlClient.queryList4IColl("getCostWarnChartFourteen", paramMap,this.getConnection());
					}else{
						paramMap.put("prd_type", prd_type);
						results = (IndexedCollection)SqlClient.queryList4IColl("getCostWarnChartNine", paramMap,this.getConnection());
					}
				}else if (submitType.equals("getLoanDirectionChart") || submitType.equals("getloanStruChart") ) {	//贷款投向处理
					results = (IndexedCollection)SqlClient.queryList4IColl(submitType,  kcoll.getDataValue("value"),this.getConnection());
				}else if (submitType.equals("getCusSaveChartZ") || submitType.equals("getCusSaveChartF")|| submitType.equals("getCusWarnChart") 
						|| submitType.equals("getCcrWarnChart") || submitType.equals("getCcrWarnChartTwo")
						|| submitType.equals("getLmtWarnChartOnetwo") || submitType.equals("getLmtWarnChartThreefour")
						|| submitType.equals("getLmtWarnChartFive")|| submitType.equals("getLmtWarnChartSix")
						|| submitType.equals("getLmtWarnChartSeven")|| submitType.equals("getCostWarnChartOne")
						|| submitType.equals("getCostWarnChartTwothree")) {	//分机构处理
					
					String organNo = kcoll.getDataValue("organNo").toString() ;
					String br_type = getBrtype(organNo);
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("br_type", br_type);
					paramMap.put("organNo", organNo);
					results = (IndexedCollection)SqlClient.queryList4IColl(submitType, paramMap,this.getConnection());
				}else {	/***** 默认则是直接进行查询 ******/
					results = (IndexedCollection)SqlClient.queryList4IColl(submitType, null,this.getConnection());
				}
			}else{	//至于没传submitType的情况不建议写在这里
				
			}
		}catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}		
		return results;
	}
	
	/*** 总分支行机构判断处理 ***/
	public String getBrtype(String organNo)throws Exception {
		String br_type = "";
		
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		OrganizationServiceInterface serviceUser = (OrganizationServiceInterface) serviceJndi
				.getModualServiceById("organizationServices","organization");
		SOrg org_info = serviceUser.getOrgByOrgId(organNo, this.getConnection());
		String suporganno = org_info.getSuporganno();
		if(suporganno.equals("9350000000")){
			br_type = "all";	//机构类型：总行
		}else{
//			if("9350500000".equals(suporganno)){
//				br_type ="single";	//支行
//			}else{
//				br_type ="sup";	//分行
//			}
			br_type = "sup";//看到自己本行和下属机构
		}
		
		return br_type;
	}
	
}