package com.yucheng.cmis.platform.reload.op;

import java.sql.Connection;

import com.ecc.echain.workflow.cache.OUCache;
import com.ecc.emp.component.factory.ComponentFactory;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.prd.initializer.PrdInitializer;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.permission.initializer.PermissionInitializer;
import com.yucheng.cmis.pub.CMISAgentFactory;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDaoFactory;
import com.yucheng.cmis.pub.FNCFactory;
import com.yucheng.cmis.pub.FNCItemsFactory;
import com.yucheng.cmis.pub.dao.config.SqlConfigLoader;
import com.yucheng.cmis.pub.util.SInfoFactory;
import com.yucheng.cmis.pub.util.SetSysInfo;

/**
 * 用户登陆验证 成功返回success，失败返回fail
 * 
 * 
 */
public class CMISReloadSystem extends CMISOperation {
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection con = null;
		try{
			String type = null;
			try {
				type = (String)context.getDataValue("type");
			} catch (Exception e) {}
		
			if(type == null || type.length() ==0)
				throw new EMPJDBCException("没有传递需要重载的所有类型");
			con = this.getConnection(context);
			
			EMPFlowComponentFactory empFlowComponentFactory = (EMPFlowComponentFactory) ComponentFactory.getComponentFactory("CMISBiz");
			Context rootCtx = empFlowComponentFactory.getContextNamed(empFlowComponentFactory.getRootContextName());
			//清空缓存context
			empFlowComponentFactory.reloadAllFlow();
			 String webInfPath = (String)rootCtx.getDataValue("webInfPath");
			 String tableModelPath = webInfPath + "/tables/";
			System.err.println(empFlowComponentFactory.getFlowCache().size());
			if("record".equals(type)){//重载记录集权限
				PermissionInitializer pi = new PermissionInitializer();
				pi.initialize(context,this.getConnection(context));
			}else if("sql".equals(type)){//重载命名SQL
				SqlConfigLoader sc = new SqlConfigLoader();
	    		sc.loadSqlConfig();
			}else if(type.equals("dic")){//重载字典
				CMISDataDicService service = (CMISDataDicService)context.getService(CMISConstance.ATTR_DICSERVICE);
				service.loadDicData(rootCtx, con);
				//重载树形字典
				CMISTreeDicService tree_service = (CMISTreeDicService)context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				tree_service.loadDicData(context, con);
			}else if(type.equals("date")){//日期
				SetSysInfo.init(rootCtx, con);
			}else if(type.equals("seq")){//序列
//				CMISSeqFactory.getSeqFactoryInstance().init();
//				CMISSequenceService4BSBXD service = (CMISSequenceService4BSBXD)context.getService(CMISConstance.ATTR_SEQUENCESERVICE_BS);
//				service.initialize();
			}else if(type.equals("compnt")){//组件
				CMISComponentFactory.init();
			    CMISAgentFactory.init();
			    CMISDaoFactory.init();
			}else if(type.equals("tabmode")){//表模型
				TableModelLoader modelLoader = (TableModelLoader)rootCtx.getService(CMISConstance.ATTR_TABLEMODELLOADER);
				modelLoader.initTableModels(tableModelPath);
			}else if(type.equals("org")){//机构用户岗位 
				SInfoFactory.init(rootCtx, con);
			}else if(type.equals("fnc")){//重载财务报表配置样式
				FNCFactory.init(rootCtx, con);
				FNCItemsFactory.init(rootCtx, con);
			}else if("wfRole".equals(type)){ //流程角色 或者岗位或者机构的 重载  add by wuming 2012.10.11
				OUCache.getInstance().reloadData();
			}else if("flowDic".equals(type)){ //重载流程字典项   add by 唐顺岩  2013-08-11
				PrdInitializer prdin = new PrdInitializer();
				prdin.initialize(rootCtx, con);
			}else if(type.equals("all")){//重载以上所有   
				//重载记录集权限
				PermissionInitializer pi = new PermissionInitializer();
				pi.initialize(context,this.getConnection(context));
				//重载命名SQL
				SqlConfigLoader sc = new SqlConfigLoader();
	    		sc.loadSqlConfig();
				//重载字典
				CMISDataDicService service = (CMISDataDicService)context.getService(CMISConstance.ATTR_DICSERVICE);
				service.loadDicData(rootCtx, con);
				//重载树形字典
				CMISTreeDicService tree_service = (CMISTreeDicService)context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				tree_service.loadDicData(context, con);
				//日期
				SetSysInfo.init(rootCtx, con);
				//CMISSequenceService4BSBXD seqServic = (CMISSequenceService4BSBXD)context.getService(CMISConstance.ATTR_SEQUENCESERVICE_BS);
				//seqServic.initialize();
				
				//组件
				CMISComponentFactory.init();
			    CMISAgentFactory.init();
			    CMISDaoFactory.init();
			    //表模型
			    TableModelLoader modelLoader = (TableModelLoader)rootCtx.getService(CMISConstance.ATTR_TABLEMODELLOADER);
				modelLoader.initTableModels(tableModelPath);
				//机构用户岗位 
				SInfoFactory.init(rootCtx, con); 
				//重载财务报表配置样式
				FNCFactory.init(rootCtx, con);
				FNCItemsFactory.init(rootCtx, con);
				//流程角色 或者岗位或者机构的 重载
				OUCache.getInstance().reloadData();
				
				//重载流程字典项   add by 唐顺岩  2013-08-11
				PrdInitializer prdin = new PrdInitializer();
				prdin.initialize(rootCtx, con);
			}else{
				throw new EMPJDBCException("不支持的类型");
			}
		}catch(Exception e){
			throw new EMPException(e);
		}finally{
			this.releaseConnection(context, con);
		}
		
		
		
		return "0";
	}

	
}
