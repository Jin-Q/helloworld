package com.yucheng.cmis.biz01line.homepage;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * 工具集的操作:
 * @author JackYu
 *
 */
public class HomePageGadgetOp extends CMISOperation{

	final String modelId = "HomepageGadget";
	
	final String QUERY_ALL_GADGET = "queryAll";
	final String QUERY_GADGET_DUTYS = "queryByUser";
	final String QUERY_GADGET_DETAIL = "queryDetail";
	final String REGIST_GADGET = "regist";
	final String DELETE_GADGET = "delete";
	final String UPDATE_GADGET = "update";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String operator  = null;
		
		try {
			try {
				if(context.containsKey("operator")){
					operator  = (String)context.getDataValue("operator");
				}else{
					operator="queryAll";
				}
				
			} catch (EMPException e) {
				throw new EMPException("operator can't be null");
			}
			/**add by lisj 2014年11月6日  修复前台小工具BUG (BUG描述：前台传入menuId影响权限配置信息)begin**/
			context.put("menuId", "customHomePage");
			/**add by lisj 2014年11月6日  修复前台小工具BUG (BUG描述：前台传入menuId影响权限配置信息)end**/
			//查询所有gadget
			if(operator!=null && operator.equals(QUERY_ALL_GADGET)) queryGadget(context);
			
			//查询可添加的gadget
			else if(operator!=null && operator.equals(QUERY_GADGET_DUTYS)) queryGadgetByPermision(context);
			
			//注册Gadget
			else if(operator!=null && operator.equals(REGIST_GADGET))registGadget(context);
			
			//删除Gadget
			else if(operator!=null && operator.equals(DELETE_GADGET)) deleteGadget(context);
			
			//查询明细
			else if(operator!=null && operator.equals(QUERY_GADGET_DETAIL)) queryDetailGadget(context);
			
			//更新gadget
			else if(operator!=null && operator.equals(UPDATE_GADGET)) updateGadget(context);
		} catch (EMPException e) {
			context.addDataField("flag", "error");
			e.printStackTrace();
			throw e;
		} catch(Exception e){
			context.addDataField("flag", "error");
			e.printStackTrace();
		}
		
		return "0";
	}
	
	/**
	 * 注册一个Gadget
	 * @param context
	 * @throws EMPException
	 */
	private void registGadget(Context context) throws EMPException {
		KeyedCollection kColl = (KeyedCollection)context.getDataElement("HomepageGadget");
		
		if(kColl == null || !kColl.containsKey("gadget_id"))
			throw new EMPException("Gadget or gadget_id can't be null");
		
		Connection connection = this.getConnection(context);
		
		UNIDGenerator unid = new UNIDGenerator();
		kColl.setDataValue("gadget_id", unid.getUNID());
		
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
		dao.insert(kColl, connection); 
		
		context.addDataField("flag", "sucess");
	}

	
	/**
	 * 查询所有gadget
	 * @param context
	 * @throws EMPException
	 */
	private void queryGadget(Context context)throws EMPException {
		 Connection connection = this.getConnection(context);
		 
         TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
 
         
         IndexedCollection iColl = dao.queryList(modelId, connection);
         iColl.setName(iColl.getName()+"List");
         this.putDataElement2Context(iColl, context);
	}
	
	/**
	 * 添加gadget，对用户已经添加过的gadget不再显示
	 * 
	 * 对查询gadget的权限控制:根据用户的岗位信息对其过滤 
	 * @param context
	 * @throws EMPException
	 */
	private void queryGadgetByPermision(Context context)throws EMPException {
		
		
		HomePageComponet hpcom = new HomePageComponet();
		
		String userId = null;//用户
		String dutyList = null;//岗位
		
		try {
			userId = (String) context.get(CMISConstance.ATTR_CURRENTUSERID);
			dutyList = (String) context.get(CMISConstance.ATTR_DUTYNO_LIST);
		} catch (Exception e) {
			throw new EMPException("gadgetId 为空");
		}
		
		int size = 10;
		PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
	
		DataSource dataSource = this.getDataSource(context);
		
		IndexedCollection iColl = hpcom.queryHomePageGadgetByPermision(userId, dutyList, dataSource);
		
		iColl.setName("HomepageGadgetList");
        this.putDataElement2Context(iColl, context);
		TableModelUtil.parsePageInfo(context, pageInfo);

	}
	
	/**
	 * 删除一个Gadget
	 * @param context
	 * @throws EMPException
	 */
	private void deleteGadget(Context context)throws EMPException {
		Connection connection = this.getConnection(context); 
		
		String pk_value = (String)context.getDataValue("gadget_id");
        
        TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
        
        dao.deleteAllByPk(modelId, pk_value, connection);   
        
        context.addDataField("flag", "sucess");
	}
	
	/**
	 * 查询gadget明细
	 * @param context
	 * @throws EMPException
	 */
	private void queryDetailGadget(Context context)throws EMPException {
		Connection connection = this.getConnection(context); 
		
		String pk_value = (String)context.getDataValue("gadget_id");
        
        TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
        
        KeyedCollection kColl = dao.queryAllDetail(modelId, pk_value, connection);   
        
        this.putDataElement2Context(kColl, context);
        
        context.addDataField("flag", "sucess");
		
		
	}
	
	/**
	 * 更新gadget
	 * @param context
	 * @throws EMPException
	 */
	private void updateGadget(Context context)throws EMPException {
		Connection connection = this.getConnection(context); 
		KeyedCollection kColl = (KeyedCollection)context.getDataElement("HomepageGadget");
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		dao.update(kColl, connection);
		
		context.addDataField("flag", "sucess");
	}
	
}
