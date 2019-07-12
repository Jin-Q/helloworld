package com.yucheng.cmis.biz01line.authorize.op.pvpauthorize;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPvpAuthorizeListOp extends CMISOperation {


	private final String modelId = "PvpAuthorize";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String conditionStr = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			if(conditionStr !=null && !(conditionStr.equals(""))){
				//conditionStr +=" and status in ('00','01') and serno in (select b.serno from pvp_loan_app b where b.cont_no in (select c.cont_no from ctr_loan_cont c where c.biz_type = '"+biz_type+"')) order by serno desc";
				conditionStr += " order by tran_date desc,substr(serno,-14) desc";
			}else{
			    //conditionStr ="where status in ('00','01') and serno in (select b.serno from pvp_loan_app b where b.cont_no in (select c.cont_no from ctr_loan_cont c where c.biz_type = '"+biz_type+"')) order by serno desc";  
			    conditionStr = " order by tran_date desc,substr(serno,-14) desc";  
			}
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			/**add by lisj 2015-3-11 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
			if(iColl!=null && iColl.size()>0){
				for(Iterator<KeyedCollection> iterator = iColl.iterator();iterator.hasNext();){
					KeyedCollection temp = (KeyedCollection)iterator.next();
					String prd_id = (String) temp.getDataValue("prd_id");
					if(prd_id!=null && !"".equals(prd_id) &&  prd_id.equals("600022")){
						KeyedCollection capc = dao.queryDetail("CtrAssetProCont", temp.getDataValue("cont_no").toString(), connection);
						temp.setDataValue("cus_name", capc.getDataValue("pro_name").toString());
					}
					//added by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 start
					String manager_id = "";
			        String cont_no = "";
			        if(temp.containsKey("cont_no")&&temp.getDataValue("cont_no")!=null&&!"".equals(temp.getDataValue("cont_no"))){
			        	cont_no = (String) temp.getDataValue("cont_no");
			            KeyedCollection kColl4IQP = dao.queryFirst("CusManager", null, "where is_main_manager='1' and cont_no='"+cont_no+"'", connection);
			            if(kColl4IQP.containsKey("manager_id")&&kColl4IQP.getDataValue("manager_id")!=null&&!"".equals(kColl4IQP.getDataValue("manager_id"))){
			            	manager_id = (String) kColl4IQP.getDataValue("manager_id");
			            }else{
			            	KeyedCollection kColl4ExtensionAgr = dao.queryFirst("IqpExtensionAgr", null, "where agr_no='"+cont_no+"'", connection);
			            	if(kColl4ExtensionAgr.containsKey("manager_id")&&kColl4ExtensionAgr.getDataValue("manager_id")!=null&&!"".equals(kColl4ExtensionAgr.getDataValue("manager_id"))){
				            	manager_id = (String) kColl4ExtensionAgr.getDataValue("manager_id");
				            }
			            }
			        }
			        temp.put("manager_id", manager_id);
			        //added by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 end
				}
			}
			/**add by lisj 2015-3-11 需求编号：【XD150303017】关于资产证券化的信贷改造 end**/
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] forfield=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,forfield, fieldName);
		   
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","in_acct_br_id"});
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
