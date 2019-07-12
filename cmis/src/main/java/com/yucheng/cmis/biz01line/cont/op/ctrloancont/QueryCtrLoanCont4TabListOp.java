package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

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

public class QueryCtrLoanCont4TabListOp extends CMISOperation {


	private final String modelId = "CtrLoanCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String conditionStr ="";
			String po_no = null;
			String cargo_id = null;
			String guaranty_no = null;
			
			/**取得Tab参数*/
			if(context.containsKey("po_no")){
				po_no = (String)context.getDataValue("po_no");  
			}
			if(context.containsKey("cargo_id")){
				cargo_id = (String)context.getDataValue("cargo_id");  
			}
			if(context.containsKey("guaranty_no")){
				guaranty_no = (String)context.getDataValue("guaranty_no");  
			}
			if(po_no!=null&&!"".equals(po_no)){
				conditionStr += " where cont_no in (select b.cont_no from grt_guaranty_re a, grt_loan_r_gur b where a.guar_cont_no = b.guar_cont_no and trim(b.cont_no) is not null and a.guaranty_id = '"+po_no+"') order by serno desc,cont_no desc"; 
			}else if(cargo_id!=null&&!"".equals(cargo_id)){
				conditionStr += " where cont_no in (select a.cont_no from grt_loan_r_gur a, grt_guaranty_re b, mort_cargo_pledge c where a.guar_cont_no = b.guar_cont_no and b.guaranty_id = c.guaranty_no and c.cargo_id = '"+cargo_id+"') order by serno desc,cont_no desc"; 
			}else if(guaranty_no!=null&&!"".equals(guaranty_no)){
				conditionStr += " where cont_no in (select a.cont_no from grt_loan_r_gur a, grt_guaranty_re b where a.guar_cont_no = b.guar_cont_no and b.guaranty_id = '"+guaranty_no+"') order by serno desc,cont_no desc"; 
			}
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
		//详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			
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
