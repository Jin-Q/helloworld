package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo.cusgrpmember;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryCusGrpInfoCusGrpMemberPopListOp extends CMISOperation {
	
	private final String modelId = "CusGrpMember";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String grp_no_value = (String)context.getDataValue("CusGrpInfo.grp_no");
			
			if(grp_no_value==null){
				throw new EMPException("parent primary key not found!");
			}
			
			String currId = (String)context.getDataValue("currentUserId");
			
			String conditionStr = " where cus_manager ='"+currId+"' and grp_no='"+grp_no_value+"' ";
			
			int size = 15;
	
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("cus_name");
			list.add("grp_corre_type");
			list.add("cus_manager");
			list.add("main_br_id");
			list.add("grp_no");
			list.add("cus_type");
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			iColl.setName(iColl.getName()+"List");
			System.out.println(iColl.toString());
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
