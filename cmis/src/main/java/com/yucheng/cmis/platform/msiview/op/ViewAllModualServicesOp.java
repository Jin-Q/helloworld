package com.yucheng.cmis.platform.msiview.op;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.msiview.ViewAllModualServices;
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
public class ViewAllModualServicesOp extends CMISOperation {
	
	private static final String modelId = "ModualService";
	private static final String VIEW_MODEL_DEFAULT = "default";
	private static final String VIEW_MODEL_MODUAL = "modual";
	private static final String VIEW_MODEL_METHOD = "method";
	
	public String doExecute(Context context) throws EMPException {
		try{
			IndexedCollection iColl = null;
			//视图
			String viewModel = null;
			try {
				viewModel = (String)context.getDataValue("viewModel");
			} catch (Exception e) {
				viewModel = VIEW_MODEL_DEFAULT;
			}
			
			//查询条件
			String filterModualName = null;//根据模块名查询
			String filterMehtodName = null;//根据方法名查询
			try {
				KeyedCollection queryData = (KeyedCollection)context.getDataElement(modelId);
				filterModualName = (String)queryData.getDataValue("modual_name");
				filterMehtodName = (String)queryData.getDataValue("method_name");
			} catch (Exception e) {}
			
			//缺省视图
			if(viewModel.equals(this.VIEW_MODEL_DEFAULT)){
				iColl = new IndexedCollection("ModualServiceList");
				this.createDefaultViewData(iColl, filterModualName, filterMehtodName);
			}
				
			
			//模块视图
			if(viewModel.equals(this.VIEW_MODEL_MODUAL)){
				iColl = new IndexedCollection("ModualServiceList2");
				this.createModualViewData(iColl, filterModualName);
			}
			
			//服务视图
			if(viewModel.equals(this.VIEW_MODEL_METHOD)){
				iColl = new IndexedCollection("ModualServiceList3");
				this.createMethodViewData(iColl, filterModualName, filterMehtodName);
			}
			
			
			this.putDataElement2Context(iColl, context);
			try {
				context.addDataField("viewModel", viewModel);
			} catch (Exception e) {
				context.setDataValue("viewModel", viewModel);
			}
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
		}
		return "0";
	}
	
	/**
	 * 服务视图
	 * @param iColl IndexedCollection
	 * @param filterModualName 模块名查询
	 * @param filterMehtodName 方法名查询
	 * @throws Exception
	 */
	private void createMethodViewData(IndexedCollection iColl, String filterModualName,String filterMehtodName) throws Exception{
		ViewAllModualServices ms = new ViewAllModualServices();
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		
		List<ModualServiceVO> modSerList =  ms.parseMoudalServicesDesc();
		
		
		//将modSerList转化为ICOLL
		for (int i = 0; i < modSerList.size(); i++) {
			ModualServiceVO modVO = modSerList.get(i);
			String modualName = modVO.getModualName();
			
			List<ClassServiceVO> classList = modVO.getClassServiceList();
			
			for(ClassServiceVO classVO: classList){
				String serviceId = classVO.getServiceId();
				
				//filterModualName过滤
				if(filterModualName!=null && filterModualName.trim().length()>0)
					if(!(modualName.contains(filterModualName) || serviceId.contains(filterModualName))) continue;
						
				
				List<MethodServiceVO> medSerList = classVO.getMethodServiceList();
				
				for (int j = 0; j < medSerList.size(); j++) {
					MethodServiceVO medVO = medSerList.get(j);
					String methodName = medVO.getMethod();
					String desc = medVO.getDesc();
					String example  = medVO.getExample();
					String inParam = "";
					String outParam = "";
					Map<String, String> inMap = medVO.getInParam();
					for (Iterator iterator = inMap.keySet().iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						inParam = inParam+key+":"+inMap.get(key)+";";
					}
					
					Map<String, String> outMap = medVO.getOutParam();
					for (Iterator iterator = outMap.keySet().iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						outParam = outParam+key+":"+outMap.get(key)+";";
					}
					
					
					//filterMehtodName过滤
					if(filterMehtodName!=null && filterMehtodName.trim().length()>0)
						if(!(methodName.contains(filterMehtodName) || desc.contains(filterMehtodName))) continue;
					
					KeyedCollection kColl = new KeyedCollection();
					
					kColl.addDataField("modual_name", modualName+"-"+serviceId);
					kColl.addDataField("method_name", methodName);
					kColl.addDataField("method_desc", desc);
					kColl.addDataField("in_param", inParam);
					kColl.addDataField("out_param", outParam);
					kColl.addDataField("example", example);

					iColl.add(kColl);
				}
			}
		}
	}
	
	/**
	 * 模式视图
	 * 
	 * @param iColl IndexedCollection
	 * @param filterModualName 模块名查询
	 * @throws Exception
	 */
	private void createModualViewData(IndexedCollection iColl, String filterModualName) throws Exception{
		ViewAllModualServices ms = new ViewAllModualServices();
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		
		List<ModualServiceVO> modSerList =  ms.parseMoudalServicesDesc();
		
		
		//将modSerList转化为ICOLL
		for (int i = 0; i < modSerList.size(); i++) {
			ModualServiceVO modVO = modSerList.get(i);
			String modualName = modVO.getModualName();
			String modualId = modVO.getModualId();
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("modual_id", modualId);
			kColl.addDataField("modual_name", modualName);
			iColl.add(kColl);
		}
	}
	
	
	/**
	 * 缺省视图的数据准备
	 * @param iColl IndexedCollection
	 * @param filterModualName 模块名查询
	 * @param filterMehtodName 方法名查询
	 * @throws Exception
	 */
	private void createDefaultViewData(IndexedCollection iColl, String filterModualName, String filterMehtodName) throws Exception{
		ViewAllModualServices ms = new ViewAllModualServices();
		String projectPath = CMISPropertyManager.getInstance().getCmisProjectPath();
		
		List<ModualServiceVO> modSerList =  ms.parseMoudalServicesDesc();
		
		
		//将modSerList转化为ICOLL
		for (int i = 0; i < modSerList.size(); i++) {
			ModualServiceVO modVO = modSerList.get(i);
			String modualName = modVO.getModualName();
			
			List<ClassServiceVO> classList = modVO.getClassServiceList();
			
			for(ClassServiceVO classVO: classList){
				String serviceId = classVO.getServiceId();
				
				//filterModualName过滤
				if(filterModualName!=null && filterModualName.trim().length()>0)
					if(!(modualName.contains(filterModualName) || serviceId.contains(filterModualName))) continue;
						
				
				List<MethodServiceVO> medSerList = classVO.getMethodServiceList();
				
				for (int j = 0; j < medSerList.size(); j++) {
					MethodServiceVO medVO = medSerList.get(j);
					String methodName = medVO.getMethod();
					String desc = medVO.getDesc();
					String example  = medVO.getExample();
					String inParam = "";
					String outParam = "";
					Map<String, String> inMap = medVO.getInParam();
					for (Iterator iterator = inMap.keySet().iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						inParam = inParam+key+":"+inMap.get(key)+";";
					}
					
					Map<String, String> outMap = medVO.getOutParam();
					for (Iterator iterator = outMap.keySet().iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						outParam = outParam+key+":"+outMap.get(key)+";";
					}
					
					
					//filterMehtodName过滤
					if(filterMehtodName!=null && filterMehtodName.trim().length()>0)
						if(!(methodName.contains(filterMehtodName) || desc.contains(filterMehtodName))) continue;
					
					KeyedCollection kColl = new KeyedCollection();
					
					kColl.addDataField("modual_name", modualName+"-"+serviceId);
					kColl.addDataField("method_name", methodName);
					kColl.addDataField("method_desc", desc);
					kColl.addDataField("in_param", inParam);
					kColl.addDataField("out_param", outParam);
					kColl.addDataField("example", example);

					iColl.add(kColl);
				}
			}
			
		}
	}
	
}
