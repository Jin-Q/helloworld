package com.yucheng.cmis.biz01line.psp.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 通过传递的方案编号获取方案详细信息
 * 1.读取数据库中配置的项目明细
 * 2.读取结果表中的项目结果
 * @author Pansq
 */
public class GetPspCheckListOp extends CMISOperation {
	private static final String SCHRELMODEL = "PspSchCatRel";
	private static final String CATMODEL = "PspCheckCatalog";
	private static final String ITEMRELMODEL = "PspCatItemRel";
	private static final String ITEMMODEL = "PspCheckItem";
	private static final String ITEMRESMODEL = "PspCheckItemResult";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String schemeId = (String)context.getDataValue("scheme_id");
			String taskId = "";
			if(context.containsKey("task_id")){
				taskId = (String)context.getDataValue("task_id");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection returnKColl = new KeyedCollection("returnKColl");
			IndexedCollection returnIColl = new IndexedCollection("returnIColl");
			/** 查询出方案下所有的目录 */
			IndexedCollection schRelIColl = dao.queryList(SCHRELMODEL, " where scheme_id ='"+schemeId+"' order by seq", connection);
			if(schRelIColl != null && schRelIColl.size() > 0){
				for(int i=0;i<schRelIColl.size();i++){
					KeyedCollection schRelKColl = (KeyedCollection)schRelIColl.get(i);
					String catalogId = (String)schRelKColl.getDataValue("catalog_id");
					KeyedCollection catKColl = dao.queryDetail(CATMODEL, catalogId, connection);
					KeyedCollection returnCatKColl = new KeyedCollection("returnCatKColl");
					/** 查询目录下所有检查项 */
					IndexedCollection returnItemIColl = new IndexedCollection("returnItemIColl");
					IndexedCollection catRelIColl = dao.queryList(ITEMRELMODEL, " where catalog_id ='"+catalogId+"' order by seq", connection);
					if(catRelIColl != null && catRelIColl.size() > 0){
						for(int j=0;j<catRelIColl.size();j++){
							KeyedCollection catRelKColl = (KeyedCollection)catRelIColl.get(j);
							String itemId = (String)catRelKColl.getDataValue("item_id");
							
							KeyedCollection itemKColl = dao.queryDetail(ITEMMODEL, itemId, connection);
							returnItemIColl.addDataElement(itemKColl);
							/** 查询是否存在项目结果信息 */
							Map param = new HashMap();
							param.put("task_id", taskId);
							param.put("scheme_id", schemeId);
							param.put("item_id", itemId);
							KeyedCollection itemResKColl = dao.queryDetail(ITEMRESMODEL, param, connection);
							String itemHelp = (String)itemResKColl.getDataValue("item_id");
							if(itemHelp != null){
								returnKColl.addDataField(itemHelp, itemResKColl.getDataValue("check_result"));
							}
						}
					}
					
					//判断隐藏事件，控制是否隐藏属性    start
					for(int k=0;k<returnItemIColl.size();k++){
						KeyedCollection hidKColl = (KeyedCollection) returnItemIColl.get(k);
						String is_need_event = (String) hidKColl.getDataValue("is_need_event");
						if(is_need_event.equals("1")){
							String event_type = (String) hidKColl.getDataValue("event_type");
							if(event_type.equals("01")){
								String imp_item_id = (String) hidKColl.getDataValue("imp_item_id");//目标项目号
								String hpp_cond = (String) hidKColl.getDataValue("hpp_cond");//成立条件
								String item_id = (String) hidKColl.getDataValue("item_id");//项目号
								String itvalue = "";
								if(returnKColl.containsKey(item_id)){
									itvalue = (String) returnKColl.getDataValue(item_id);//项目值
								}
								if(itvalue!=null&&!itvalue.equals("")){//无值按默认来
									String[] hpp_cond_sp = hpp_cond.split("\\|");
									boolean flag = false;
									for(int n=0; n<hpp_cond_sp.length; n++){
										String hpp_cond_sp_n = hpp_cond_sp[n];
										if(itvalue.equals(hpp_cond_sp_n)){
											flag = true;
											break;//一个匹配即成立
										}
									}
									
									if(flag){//条件成立，触发隐藏，通过设置隐藏属性实现
										for(int l=0;l<returnItemIColl.size();l++){
											KeyedCollection chaKColl = (KeyedCollection) returnItemIColl.get(l);
											String chaitem_id = (String) chaKColl.getDataValue("item_id");
											if(imp_item_id.equals(chaitem_id)){
												chaKColl.setDataValue("is_hidden", "1");
											}
										}
									}else{//条件不成立，设置不隐藏
										for(int l=0;l<returnItemIColl.size();l++){
											KeyedCollection chaKColl = (KeyedCollection) returnItemIColl.get(l);
											String chaitem_id = (String) chaKColl.getDataValue("item_id");
											if(imp_item_id.equals(chaitem_id)){
												chaKColl.setDataValue("is_hidden", "2");
											}
										}
									}
								}
							}
						}
					}
					//判断隐藏事件，控制是否隐藏属性    end
					
					returnCatKColl.addKeyedCollection(catKColl);
					returnCatKColl.addIndexedCollection(returnItemIColl);
					returnIColl.addDataElement(returnCatKColl);
				}
			}
			this.putDataElement2Context(returnIColl, context);
			this.putDataElement2Context(returnKColl, context);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
