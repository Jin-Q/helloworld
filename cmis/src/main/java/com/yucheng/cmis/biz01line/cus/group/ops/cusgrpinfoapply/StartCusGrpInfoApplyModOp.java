package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;



import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoApplyComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class StartCusGrpInfoApplyModOp extends CMISOperation{
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		int sign = 0;
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		String cusId = "";
		String cusIdApply = "";
		try {
			connection = this.getConnection(context);
			TableModelDAO tmdao = this.getTableModelDAO(context);
			String serno = context.getDataValue("serno").toString();
			String grp_no = context.getDataValue("grp_no").toString();
			String grp_name = context.getDataValue("grp_name").toString();
			
			CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSGRPINFOAPPLYCOMPONENT, context, connection);
			//查询该集团下是否存在集团成员 是 返回1，否 返回0
			int flag = cusGrpInfoApplyComponent.checkCusGrpMember(serno);
			if(flag == 0){
				throw new EMPException("该集团下没有集团成员，请确认！ ");
			}
			//断该变更的集团是否存在申请或者变更的业务
          // String  qStr= "   (approve_status = '000' or approve_status = '991' or approve_status = '992' ) and CORRE_TRANS_TYPE='2' ";
			//IndexedCollection iCollApply = tmdao.queryList("LmtApplyGrp", " qStr", connection);

			
			/**
			 * 判断该申请集团向下的成员是否已经存在别的有效集团中
			 */
			//申请的客户成员
			IndexedCollection iCollApply = tmdao.queryList("CusGrpMemberApply", " where serno='"+serno+"'", connection);
			//该集团向下已经有效的客户成员
			IndexedCollection iColl = tmdao.queryList("CusGrpMember", " where grp_no='"+grp_no+"'", connection);
			
			for(int i=0;i<iCollApply.size();i++){
				KeyedCollection kcApply = (KeyedCollection) iCollApply.get(i);
				cusIdApply = (String) kcApply.getDataValue("cus_id");
				list1.add(cusIdApply);
				for(int j=0;j<iColl.size();j++){
					KeyedCollection kc = (KeyedCollection) iColl.get(j);
					cusId = (String) kc.getDataValue("cus_id");
					if(cusId.equals(cusIdApply)){
						list2.add(cusId);
					}
				}
			}
			/**
			 * 判断cus_grp_member_apply有无增加成员，则将该成员添加到cus_grp_member
			 */
			for(int i=0;i<list1.size();i++){
				boolean isTrue = list2.contains(list1.get(i));
				if(!isTrue){
					String cus_id =list1.get(i); //取得新增加的客户成员id
					sign = cusGrpInfoApplyComponent.isExistCusGrpMember(cus_id);
					if(sign == 1){
						throw new EMPException("客户编号为["+cus_id+"]已经是有效集团的成员，不能再为该集团增加该成员！！！");
					}
				}	
			}
			
			
			context.addDataField("serNo", serno);
			context.addDataField("grpNo", grp_no);
			context.addDataField("grpName", grp_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EMPException(e);
		} finally{
			this.releaseConnection(context, connection);
		}
		return null;
	}
}

