package com.yucheng.cmis.biz01line.image.dao;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class ImagePubDao extends CMISDao {
	
	/**
	 * 无返回值的命名sql调用
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
	 * 带返回值的命名sql调用
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 * @throws DaoException 
	 */
	public KeyedCollection delSqlReturnKcoll(KeyedCollection kcoll) throws DaoException {
		Object results = "";
		try{
			if(kcoll.containsKey("submitType")){	//一般情况下传submitType进行区分
				String submitType = kcoll.getDataValue("submitType").toString();
				if (submitType.equals("getImageInfoASSURE") ||submitType.equals("GetGuarantyInfo")
						||submitType.equals("GetPubBusinessInfo") ||submitType.equals("GetRpddscntInfo")
						||submitType.equals("GetAssetstrsfInfo") ||submitType.equals("GetExtensionInfo")						
						||submitType.equals("GetCtrDiscInfo") ||submitType.equals("GetGrtGuarantee")
						/**modified  by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin**/
						||submitType.equals("GetActrecpoMana")||submitType.equals("GetDrfpoMana") ){	//返回新kcoll
						/**modified  by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end**/
					kcoll = (KeyedCollection) SqlClient.queryFirst(submitType, kcoll.getDataValue("info_type"), null, this.getConnection());
				}else if(submitType.equals("getAllImageInfo")){	//取所有影像信息
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("info_type", kcoll.getDataValue("info_type").toString());
					paramMap.put("prd_id", kcoll.getDataValue("prd_id").toString());
					paramMap.put("cus_type", kcoll.getDataValue("cus_type").toString());
					kcoll = (KeyedCollection) SqlClient.queryFirst(submitType,paramMap , null, this.getConnection());
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
	 * 返回不带分页icoll的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return results 返回icoll
	 * @throws DaoException 
	 */	
	public IndexedCollection delSqlReturnIcoll(KeyedCollection kcoll) throws DaoException {
		IndexedCollection results = new IndexedCollection();
		try{			
			if(kcoll.containsKey("submitType")){
				String submitType = kcoll.getDataValue("submitType").toString();
				if(submitType.equals("GetGuarantyId")){
					results = (IndexedCollection)SqlClient.queryList4IColl(submitType, kcoll.getDataValue("serno"),this.getConnection());
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
	
}