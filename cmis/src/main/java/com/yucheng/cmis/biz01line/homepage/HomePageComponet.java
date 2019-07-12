package com.yucheng.cmis.biz01line.homepage;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.dao.SqlOperator;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISComponent;

public class HomePageComponet extends CMISComponent{

	/**
	 * 找到该用户定制的gadget
	 * 
	 * @param userId 用户ID
	 * @param dataSource 数据源
	 * @return 符合JS解析的字符串:工具编号,标题,颜色,高,宽,URL,最大化时URL;工具编号,标题,颜色,高,宽,URL;工具编号,标题,颜色,高,宽,URL,最大化时URL
	 * @throws EMPException 
	 */
	public String queryCustomGadget(String userId, DataSource dataSource) throws EMPException{
		StringBuffer sb = new StringBuffer();
		sb.append("select ")
		  .append("		 hg.gadget_id as gadget_id ,")
		  .append("		 hg.gadget_title as gadget_title ,")
		  .append("		 hg.gadget_color as gadget_color ,")
		  .append("		 hg.gadget_height as gadget_height ,")
		  .append("		 hg.gadget_width as gadget_width ,")
		  .append("		 hg.gadget_url as gadget_url, ")
		  .append("		 hg.gadget_url_resize as gadget_url_resize ")
		  .append("from HOMEPAGE_GADGET hg,HOMEPAGE_GADGET_CUSTOM hgc ")
		  .append("where hg.gadget_id = hgc.gadget_id ")
		  .append("	     and hgc.user_id='"+userId+"'");
		
		SqlOperator sqlOp = SqlOperator.createSqlOperator(sb.toString(), dataSource);
		KeyedCollection kColl = sqlOp.executeSQL(new ArrayList());
		IndexedCollection iColl = (IndexedCollection) kColl.get("resultSet");
		
		sb = new StringBuffer();
		//拼接字符串
		for (int i = 0; i < iColl.size(); i++) {
			KeyedCollection tempKColl = (KeyedCollection)iColl.get(i);
			sb.append(tempKColl.getDataValue("gadget_id")).append(",")
			  .append(tempKColl.getDataValue("gadget_title")).append(",")
			  .append(tempKColl.getDataValue("gadget_color")).append(",")
			  .append(tempKColl.getDataValue("gadget_height")).append(",")
			  .append(tempKColl.getDataValue("gadget_width")).append(",")
			  .append(tempKColl.getDataValue("gadget_url")).append(",")
			  .append(tempKColl.getDataValue("gadget_url_resize")).append(";");
		}
		
		
		return sb.toString();
	}
	
	/**
	 * 删除该用户定制的gadget
	 * @param userId 用户ID
	 * @param gadget gadget
	 * @param dataSource 数据源
	 * @return
	 * @throws EMPException
	 */
	public boolean moveCustomGadget(String userId, String gadgetId, DataSource dataSource) throws EMPException{
		try {
			String delSql = "delete from homepage_gadget_custom where user_id='"+userId+"' and gadget_id='"+gadgetId+"'";
			SqlOperator sqlOp = SqlOperator.createSqlOperator(delSql, dataSource);
			sqlOp.executeSQL(new ArrayList());
		} catch (EMPException e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}
		return true;
	}
	
	/**
	 * 添加gadget
	 * @param userId 用户ID
	 * @param gadget gadget
	 * @param dataSource 数据源
	 * @return
	 * @throws EMPException
	 */
	public boolean addCustomGadget(String userId, String gadgetId, DataSource dataSource) throws EMPException{
		try {
			String addSql = "insert into homePage_gadget_custom(user_id, gadget_id)values('"+userId+"','"+gadgetId+"')";
			SqlOperator sqlOp = SqlOperator.createSqlOperator(addSql, dataSource);
			sqlOp.executeSQL(new ArrayList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}
		
		return true;
	}
	
	/**
	 * 根据岗位查询该用户可以添加的gadgets,
	 * @param userId 用户编号
	 * @param dutyList context中的dutyList,多个以,分隔;如果为空或是ALL 则所有人可添加
	 * @param dataSource 数据源
	 * @return IndexedCollection
	 */
	public IndexedCollection queryHomePageGadgetByPermision(String userId,String dutyList, DataSource dataSource)throws EMPException{
		StringBuffer sb = new StringBuffer();
		
		sb.append("select ")
		  .append("		 hg.gadget_id as gadget_id ,")
		  .append("		 hg.gadget_title as gadget_title ,")
		  .append("		 hg.gadget_color as gadget_color ,")
		  .append("		 hg.gadget_height as gadget_height ,")
		  .append("		 hg.gadget_width as gadget_width ,")
		  .append("		 hg.gadget_url as gadget_url, ")
		  .append("		 gadget_remark as gadget_remark ")
		  .append("from homePage_gadget hg  ")
		  .append("where hg.gadget_id not in( ")
		  .append("		select gadget_id from homePage_gadget_custom where user_id='"+userId+"'")
		  .append(")")
		  .append("and (")
		  .append("		 hg.gadget_dutys like '%ALL%' ")
		  .append("		 or hg.gadget_dutys is null ")
		  .append("		 or hg.gadget_dutys = ''")
		  .append("      or hg.gadget_dutys = ' '");
		
		//根据该用户岗位判断其是否需有gadget的添加权限
		//如果HOMEPAGE_GADGET表中gadget_dutys为ALL则所有人可见
		if(dutyList!=null){
			String[] dutys = dutyList.split(",");
			for (int i = 0; i < dutys.length; i++) {
				sb.append(" or hg.gadget_dutys like '%"+dutys[i]+"%' ");
			}
		}
		
		sb.append(")");
		
		SqlOperator sqlOp = SqlOperator.createSqlOperator(sb.toString(), dataSource);
		KeyedCollection kColl = sqlOp.executeSQL(new ArrayList());
		IndexedCollection iColl = (IndexedCollection) kColl.get("resultSet");
		
		return iColl;
	}
	
}
