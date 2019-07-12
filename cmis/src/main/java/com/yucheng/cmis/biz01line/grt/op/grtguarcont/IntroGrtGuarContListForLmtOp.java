package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class IntroGrtGuarContListForLmtOp extends CMISOperation {

	private final String modelId = "GrtGuarCont";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
            String cus_id = null;
			try {
				cus_id = (String) context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new Exception("cus_id数据异常，请联系后台管理员！");
			}
			try {
				queryData = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) { }
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			int size = 15;
			//查询合同类型为最高额担保合同的记录
			
			if(context.containsKey("type")){
				if("01".equals(context.getDataValue("type"))){  //最高额
					conditionStr += "AND guar_cont_type='01'";
				}else{   //一般担保合同
					conditionStr += "AND guar_cont_type='00'";
				}
			}
			conditionStr += " AND cus_id='"+cus_id+"' AND guar_cont_state IN('01','00')";  //引用担保合同时可引用 登记、生效的担保合同
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			    list.add("guar_cont_no");
			    list.add("guar_cont_cn_no");
			 	list.add("guar_cont_type");
			 	list.add("guar_way");
			 	list.add("guar_amt");
			 	list.add("cur_type");
			 	list.add("guar_start_date");
			 	list.add("guar_end_date");
			 	list.add("guar_cont_state");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(iColl, context);

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
