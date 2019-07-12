package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;

public class FNCItemsFactory {
	
	/** 报表科目工厂实例化*/
	private static FNCItemsFactory instance = new FNCItemsFactory();

	/**
	 * 报表科目列表 
	 * KEY   :styleID
	 * Value :FncConfStyles对象
	 */
	private static Map<String,FncConfItems4Query> fncMap = null;
	
	public static final String CONFIGKEY_MODULEID = "moduleid";
	public static final String CONFIGKEY_CLASSNAME = "classname";
	public static final String CONFIGKEY_DESCRIBE = "describe";
	
	/**
	 * <p>报表工厂初始化</p>
	 * <p>从数据库中读取报表ITMES的所有信息</p>
	 * @throws Exception
	 * @todo 将读取到的信息存放到HashMap中 
	 */
	public static void init(Context context, Connection conn){
		try{
			fncMap = getItemsFromDB(context, conn);
		}catch(Exception ex){
			ex.printStackTrace();
			//throw new EMPException("报表工厂初始化失败，" + ex.getMessage());
		}
	}
	/**
	 * 读取数据库中的报表科目(FNC_CONF_ITEMS)中的所有报表的定义信息
	 * @param context
	 * @param conn
	 * @return
	 */
	public static Map<String, FncConfItems4Query> getItemsFromDB(Context context, Connection conn){

		Map<String, FncConfItems4Query> fncMap = new HashMap<String, FncConfItems4Query>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据START..." );
			String sql = "select ITEM_ID,ITEM_NAME,FNC_CONF_TYP,FNC_NO_FLG,REMARK,FORMULA,ITEM_UNIT " +
						 "from FNC_CONF_ITEMS";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"为什么加载不了ITEM?");
			while(rs.next()){
				FncConfItems4Query fi = new FncConfItems4Query();
				fi.setFncConfTyp(rs.getString("FNC_CONF_TYP"));
				fi.setFncNoFlg(rs.getString("FNC_NO_FLG"));
				fi.setFormula(rs.getString("FORMULA"));
				fi.setItemId(rs.getString("ITEM_ID"));
				fi.setItemName(rs.getString("ITEM_NAME"));
				fi.setRemark(rs.getString("REMARK"));
				fi.setItemUnit(rs.getString("ITEM_UNIT"));
				
			/*	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_NO_FLG:"+rs.getString("FNC_NO_FLG"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FORMULA："+rs.getString("FORMULA"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_ID:"+rs.getString("ITEM_ID"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_NAME:"+rs.getString("ITEM_NAME"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"REMARK:"+rs.getString("REMARK"));
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"ITEM_UNIT:"+rs.getString("ITEM_UNIT"));
*/				
				fncMap.put(rs.getString("ITEM_ID"), fi);
			}
		
		
			/*Set<Entry<String, FncConfItems4Query>> set = fncMap.entrySet(); 
			Iterator<Entry<String, FncConfItems4Query>> itor = set.iterator(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************item map *****************");
			while(itor.hasNext()) 
			{ 
			Entry<String, FncConfItems4Query> entry = itor.next(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getItemName()+"---"+(entry.getValue()).getItemId()+"------"+(entry.getValue()).getFormula()); 
			} */
		
			ps.close();
			rs.close();
			//conn.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据失败SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"读取数据库中的财务科目数据失败！" );
			e.printStackTrace();

	}
		return fncMap;
	}
	/**
	 * 根据报表科目编号从数据库中得到想对应的FncConfItems对象,包括这张报表对应的所有item项目信息
	 * @param styleId
	 * @return
	 * @throws EMPException
	 */
	public static FncConfItems4Query getFNCItmeInstance(String itemId) throws EMPException{

		if(fncMap == null || fncMap.size() <= 0){
			throw  new EMPException("报表ITEMS工厂尚未初始化，请先调用初始化方法后再使用getFNCItemsInstance方法");	
		}
		if(itemId == null || itemId.trim().equals("")){
			throw new EMPException("报表ITMES编号为空， 无法实例化");
		}
		
		FncConfItems4Query fncConfItems = null;
		try {
			fncConfItems = (FncConfItems4Query)fncMap.get(itemId);
		}catch(Exception e){
			throw new EMPException("不存在ITME编号为"+itemId+"对象；" + e.toString());
		}
		//System.out.println(styleId+"      ///////////   "+fncConfStyles);
		//System.out.println(fncConfStyles.getStyleId() + " +++++++ " + fncConfStyles.getFncConfDisName());
		return fncConfItems;
	}
	/** 
	 * <p>取得业务ITMES组件工厂实例</p>
	 * @return 业务组件工厂实例
	 */
	public static FNCItemsFactory getFNCItemsFactoryInstance() {
	   if(instance != null){
	     return instance;
	   } else {
		   return new FNCItemsFactory();
	   }
	}

}
