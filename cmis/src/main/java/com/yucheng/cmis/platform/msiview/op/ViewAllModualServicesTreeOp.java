package com.yucheng.cmis.platform.msiview.op;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.msiview.ViewAllModualServices;
import com.yucheng.cmis.platform.msiview.dao.MsiviewDao;
import com.yucheng.cmis.platform.msiview.domain.ClassServiceVO;
import com.yucheng.cmis.platform.msiview.domain.MethodServiceVO;
import com.yucheng.cmis.platform.msiview.domain.ModualServiceVO;
import com.yucheng.cmis.pub.util.CMISPropertyManager;

/**
 * 
 * 	系统所有模块服务展示OP
 * <p>
 * 	因目前阶段没有Eclipse插件开发人员，只能在WEB页面中展示。
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewAllModualServicesTreeOp extends CMISOperation {
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String optType = null;
			String id = null;
			
			try {
				optType = (String) context.getDataValue("opt_type");
			} catch (Exception e) {
				optType = "default";
			}
			
//			getMSITreeViewDataFromDB(connection);
			
			//树图展示
			if(optType.equals("default")){
				String str = getMSITreeViewDataFromDB(connection);
				try {
					context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
				} catch (DuplicatedDataNameException e) {
					context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
				}
			}
			else{
				id = (String) context.getDataValue("id");
			}
			
			//右上角服务描述
			if(optType.equals("leftUp")){
				String classId  = id.substring(4);
				MsiviewDao dao = new MsiviewDao();
				
				if( id.startsWith("CLS_")){
					KeyedCollection kColl = dao.queryClassViewByClassId(classId, connection);
					kColl.setName("MsiClassView");
					this.putDataElement2Context(kColl, context);
				}
				else if( id.startsWith("MED_")){
					//服务集合信息
					KeyedCollection kColl = dao.queryClassByMethodId(classId, connection);
					kColl.setName("MsiClassView");
					this.putDataElement2Context(kColl, context);
				}
			}
			
			//右下角服务描述
			if(optType.equals("leftDown") && id.startsWith("MED_")){
				String metohdId  = id.substring(4);
				MsiviewDao dao = new MsiviewDao();
				KeyedCollection kColl = dao.queryMethodByMethodId(metohdId, connection);
				kColl.setName("MsiMethodView");
				this.putDataElement2Context(kColl, context);
			}
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if(connection!=null) this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**
	 * <p>从数据库中注册的模块、服务集(接口)、服务(方法)拼成JSON，以供前端树展示
	 * 
	 * <p>前端在点击树图时需要打开相应的信息：模块信息、服务集、服务信息，这里能过数据来具体：
	 * 		模块ID前"MOD_",服务集前面"CLS_",服务前回"MED_"
	 * 
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private String getMSITreeViewDataFromDB(Connection con) throws Exception{
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("{id:'root',label:'MSI View',children:[");
			
			MsiviewDao dao = new MsiviewDao();
			List<Map<String, String>> mdoauList = dao.queryAllMsiModual(con);
			for (Map<String, String> modualMap : mdoauList) {
				String modualId = modualMap.get("id");	//模块ID
				String modualName = modualMap.get("name");	//模块名称
				
				//拼模块
				buf.append("{id:'MOD_"+modualId+"',label:'"+modualName+"',children:[");
				
				List<Map<String, String>> classList = dao.queryClassByModualId(modualId,con);
				for (Map<String, String> classMap : classList) {
					String classId = classMap.get("id");
					String className = classMap.get("name");
					
					//拼服务集
					buf.append("{id:'CLS_"+classId+"',label:'"+className+"',children:[");
					
					List<Map<String, String>> methidList = dao.queryMethodByClassId(classId, con);
					for (Map<String, String> metodMap : methidList) {
						String methodId = metodMap.get("id");
						String methodName = metodMap.get("name");
						
						//拼服务
						buf.append("{id:'MED_"+methodId+"',label:'"+methodName+"'},");
					}
					
					//截去多余的服务后面的,
					if(buf.toString().endsWith(","))
						buf.delete(buf.length()-1, buf.length());
					
					buf.append("]},");
				}
				
				//截去多余的服务集后面的,
				if(buf.toString().endsWith(","))
					buf.delete(buf.length()-1, buf.length());
				
				buf.append("]},");
				
			}
			
			//截去多余的模块后面的,
			if(buf.toString().endsWith(","))
				buf.delete(buf.length()-1, buf.length());
			
			buf.append("]}");
			
			return buf.toString();
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	/**
	 * <p>MSI View树图展示
	 * <p>数据是直接通过ViewAllModualServices解析得来的。因为不能保证数据源(id)的唯一性，这里通过计数器
	 * 		对其主键进行了加工，保证唯一性
	 * 
	 * @param iColl IndexedCollection
	 * @param filterModualName 模块名查询
	 * @param filterMehtodName 方法名查询
	 * @throws Exception
	 */
	private String getMSITreeViewData() throws Exception{
		ViewAllModualServices ms = new ViewAllModualServices();
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		List<ModualServiceVO> modSerList =  ms.parseMoudalServicesDesc();
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		StringBuffer buf = new StringBuffer();
		buf.append("{id:'root',label:'系统模块服务定义查看',children:[");
		
		for (ModualServiceVO modVO : modSerList) {
			String modualId = modVO.getModualId();
			String modualName = modVO.getModualName();
			count1++;
			buf.append("{id:'"+count1+"_"+modualId+"',label:'"+modualName+"',children:[");
			buf.append("");
			
			List<ClassServiceVO> classList = modVO.getClassServiceList();
			
			for (ClassServiceVO classVO : classList) {
				String serviceId = classVO.getServiceId();
				String serviceDesc = classVO.getServiceDesc();
				String className = classVO.getClassName();
				
				count2++;
				buf.append("{id:'"+count2+"_"+serviceId+"',label:'"+serviceDesc+"',children:[");
				
				List<MethodServiceVO> methodList = classVO.getMethodServiceList();
				
				for (MethodServiceVO medVO : methodList) {
					String methodName = medVO.getMethod();
					String methodDesc = medVO.getDesc();
					count3 ++;
					buf.append("{id:'"+count3+"_"+methodName+"',label:'"+methodDesc+"'},");
					
				}
				
				if(buf.toString().endsWith(","))
					buf.delete(buf.length()-1, buf.length());
				
				buf.append("]},");
			}
			
			if(buf.toString().endsWith(","))
				buf.delete(buf.length()-1, buf.length());
			
			buf.append("]},");
			
		}
		
		if(buf.toString().endsWith(","))
			buf.delete(buf.length()-1, buf.length());
		
		buf.append("]}");
		
		return buf.toString();
		
	}
	
}
