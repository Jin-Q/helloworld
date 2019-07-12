package com.yucheng.cmis.biz01line.fnc.config.agent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncConfDefFormatAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午08:25:07  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfDefFormatAgent extends CMISAgent {

	
	public String addRecord(FncConfDefFormat pfncConfDefFormat) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncConfDefFormat,
				PUBConstant.FNCCONFDEFFORMAT); // 1成功  其他失败

		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}
/**
 * 
 * @param pfncConfItems  报表配置项目列表信息
 * @return   flagInfo 信息编码
 * @throws AgentException
 */
	public String updateRecord(FncConfDefFormat pfncConfDefFormat)
			throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncConfDefFormat,
				PUBConstant.FNCCONFDEFFORMAT);// 1成功  其他失败

		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;

	}
/**
 * 
 * @param itemId  项目编号
 * @return  报表配置项目列表信息对象
 * @throws AgentException
 */
	public FncConfDefFormat queryDetail(String styleId,String itemId) throws AgentException {

		FncConfDefFormat pfncConfDefFormat = new FncConfDefFormat();
		
         //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("style_id", styleId);
		pk_values.put("item_id", itemId);

		//进行查询操作
		pfncConfDefFormat = (FncConfDefFormat) this.findCMISDomainByKeywords(
				pfncConfDefFormat, PUBConstant.FNCCONFDEFFORMAT, pk_values);

		return pfncConfDefFormat;
	}
	/**
	 * 
	 * @param itemId   项目编号
	 * @return   flagInfo 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String styleId,String itemId) throws AgentException {
		
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		 //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("style_id", styleId);
		pk_values.put("item_id", itemId);
		
        //进行删除操作
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCCONFDEFFORMAT, pk_values);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;

}
	
	public List getFormats(String styleId)throws AgentException{
		List list = new ArrayList();
		String sql = "select B.STYLE_ID,B.ITEM_ID,B.FNC_CONF_ORDER,B.FNC_CONF_COTES,B.FNC_CONF_ROW_FLG,B.FNC_CONF_INDENT,B.FNC_CONF_PREFIX," +
						"B.FNC_ITEM_EDIT_TYP,B.fnc_conf_disp_amt,B.fnc_conf_chk_frm,B.fnc_conf_cal_frm," +
						"B.fnc_cnf_app_row,A.ITEM_NAME from FNC_CONF_ITEMS A,FNC_CONF_DEF_FMT B " +
						"where A.ITEM_ID = B.ITEM_ID and STYLE_ID = '"+styleId+"'  " +
								"order by B.FNC_CONF_COTES asc, B.FNC_CONF_ORDER asc ";
		
		try {
			Statement st = this.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				FncConfDefFormat fdf = new FncConfDefFormat();
				
				fdf.setStyleId(rs.getString(1));
				fdf.setItemId(rs.getString(2));
				fdf.setFncConfOrder(rs.getInt(3));
				fdf.setFncConfCotes(rs.getInt(4));
				fdf.setFncConfRowFlg(rs.getString(5));
				fdf.setFncConfIndent(rs.getInt(6));
				fdf.setFncConfPrefix(rs.getString(7));
				fdf.setFncItemEditTyp(rs.getString(8));
				fdf.setFncConfDispAmt(rs.getDouble(9));
				fdf.setFncConfChkFrm(rs.getString(10));
				fdf.setFncConfCalFrm(rs.getString(11));
				fdf.setFncCnfAppRow(rs.getInt(12));
				fdf.setItemName(rs.getString(13));
				
				
				list.add(fdf);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public String getFncConfTyp(String styleId)throws AgentException{
		String fncConfTyp = null;
		String sql = "select Fnc_Conf_Typ from FNC_CONF_STYLES where STYLE_ID='"+styleId+"'";
		try {
			Statement st = this.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				fncConfTyp = rs.getString(1);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fncConfTyp;
	}
	
	public List getItems(String fncConfTyp)throws AgentException{
		List list = new ArrayList();
		String sql = "select ITEM_ID,ITEM_NAME from FNC_CONF_ITEMS where Fnc_Conf_Typ='"+fncConfTyp+"'";
		
		try {
			Statement st = this.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				FncConfDefFormat fdf = new FncConfDefFormat();
				fdf.setItemId(rs.getString(1));
				fdf.setItemName(rs.getString(2));
				list.add(fdf);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
