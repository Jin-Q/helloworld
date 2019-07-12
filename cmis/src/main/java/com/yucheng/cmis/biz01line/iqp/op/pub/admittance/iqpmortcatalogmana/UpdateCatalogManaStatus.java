package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortcatalogmana;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateCatalogManaStatus extends CMISOperation {
	
	private final String modelId = "IqpMortCatalogMana";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String catalog_no = "";
			String status = "";
			if(context.containsKey("catalog_no")){
				catalog_no = context.getDataValue("catalog_no").toString();
			}else{
				context.addDataField("msg", "修改押品目录状态失败，失败原因：传入押品目录编号[catalog_no]为空！");
				context.addDataField("flag","N");
				return "0";
			}
			if(context.containsKey("status")){
				status = context.getDataValue("status").toString();
			}else{
				context.addDataField("msg", "修改押品目录状态失败，失败原因：传入状态信息[status]为空！");
				context.addDataField("flag","N");
				return "0";
			}
			
			CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
			
			if("0".equals(status)){  //变更目录状态为失效
				if(catalogManaComponent.isLeaf(catalog_no)){  //判断目录下是否存在有效的子目录
					context.addDataField("msg", "修改押品目录状态失败，失败原因：押品目录["+catalog_no+"]包含有效的子目录，不能做失效处理！");
					context.addDataField("flag","N");
					return "0";
				}
				if(catalogManaComponent.searchValueManaByCatalogNo(catalog_no)){  //校验是否存在有效价格信息
					context.addDataField("msg", "修改押品目录状态失败，失败原因：押品目录["+catalog_no+"]包含价格信息，不能做失效处理！");
					context.addDataField("flag","N");
					return "0";
				}
			}
			TableModelDAO dao  = this.getTableModelDAO(context);
			String catalog_name = (String) context.getDataValue("catalog_name");
			catalog_name = URLDecoder.decode(catalog_name,"UTF-8");
			if("0".equals(status)){//变更为失效状态
				IndexedCollection iColl = dao.queryList("MortGuarantyBaseInfo", null, " WHERE guaranty_type='"+catalog_no+"' ", connection);
				if(iColl.size()!=0){  //校验货物登记时，有没有引用过此目录。
					context.addDataField("msg", "修改押品价格状态失败，失败原因：已经存在此押品类型的货物信息，不能对其作失效处理");
					context.addDataField("flag","N");
					return "0";
				}
				KeyedCollection CatalogKc = dao.queryDetail(modelId, catalog_no, connection);
				String attr_type = (String) CatalogKc.getDataValue("attr_type");//获得类型属性的值
				if("01".equals(attr_type)){//非含价商品才会更新树形字典项
					//同步删除押品分类代码中相关字段。
					dao.deleteByPk("MortCmMortType", catalog_no,connection);
					Map paraMap = new HashedMap();
					paraMap.put("enname", catalog_no);
					paraMap.put("opttype","MORT_TYPE");
					//同步删除树形字典中的数据。
					dao.deleteByPks("STreedic",paraMap,connection);
				}
				
			}else if("1".equals(status)){//变更为生效状态
				
					KeyedCollection CatalogKc = dao.queryDetail(modelId, catalog_no, connection);
					String catalog_path = (String) CatalogKc.getDataValue("catalog_path");//获得目录路径的值
					String attr_type = (String) CatalogKc.getDataValue("attr_type");//获得类型属性的值
					String sup_catalog_no = (String) CatalogKc.getDataValue("sup_catalog_no");//获得上级目录的值
					
					KeyedCollection CatalogSupKc = dao.queryDetail(modelId, sup_catalog_no, connection);//获得上级目录为主键的记录。
					String statusSup = (String) CatalogSupKc.getDataValue("status");//获得上级目录状态值
					if(!"1".equals(statusSup)){
						context.addDataField("msg", "修改押品目录状态失败，失败原因：押品目录["+catalog_no+"]的上级目录为非生效状态，不能做生效处理！");
						context.addDataField("flag","N");
						return "0";
					}
					if("01".equals(attr_type)){//非含价商品才会更新树形字典项
						KeyedCollection treedic = new KeyedCollection("STreedic");
						treedic.addDataField("enname", catalog_no);
						treedic.addDataField("cnname", catalog_name);
						if("1".equals(context.getDataValue("catalog_lvl"))){
							treedic.addDataField("abvenname", "Z090100");
						}else{
							treedic.addDataField("abvenname",sup_catalog_no);
						}
						
						treedic.addDataField("locate", "M000000,Z000000,Z090000,Z090100,"+catalog_path+",");
						treedic.addDataField("memo", "押品分类代码");
						treedic.addDataField("opttype", "MORT_TYPE");
						dao.insert(treedic, connection);
						//同步新增押品分类代码记录。
						KeyedCollection mortCmMortType = new KeyedCollection("MortCmMortType");
						mortCmMortType.addDataField("mort_type_cd",catalog_no);
						mortCmMortType.addDataField("mort_type_name",catalog_name);
						if("1".equals(context.getDataValue("catalog_lvl"))){
							mortCmMortType.addDataField("par_mort_type_cd","Z090100");
						}else{
							mortCmMortType.addDataField("par_mort_type_cd",sup_catalog_no);
						}

						mortCmMortType.addDataField("par_mort_type_name","货物质押");
						mortCmMortType.addDataField("type_level","4");
						mortCmMortType.addDataField("max_mort_rate","0.6");
						mortCmMortType.addDataField("discount_val","0");
						mortCmMortType.addDataField("mort_type_flag","credit");
						mortCmMortType.addDataField("is_direct_eval","1");
						mortCmMortType.addDataField("mort_sort_cd","12");
						mortCmMortType.addDataField("report_list","qd_zy_dc");
						mortCmMortType.addDataField("report_type","3");
						mortCmMortType.addDataField("com_prop_score","30.00");
						mortCmMortType.addDataField("indiv_oper_rate_limit","130.00");
						mortCmMortType.addDataField("indiv_consume_rate_limit","130.00");
						dao.insert(mortCmMortType, connection);
					}
			}
			
			int count = catalogManaComponent.updateCatalogManaStatus(catalog_no, status);
			if(count!=1){
				context.addDataField("msg", "修改押品目录状态失败，更新记录数为"+count);
				context.addDataField("flag","N");
				return "0";
			}
			//重载时树形字典
			CMISTreeDicService tree_service = (CMISTreeDicService)context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			tree_service.loadDicData(context,connection);
			context.addDataField("flag", "Y");
			context.addDataField("msg", "Y");
		}catch(Exception e){
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
