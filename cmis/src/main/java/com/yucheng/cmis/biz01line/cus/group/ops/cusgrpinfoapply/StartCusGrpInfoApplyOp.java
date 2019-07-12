package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;



import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoApplyComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
/**
 *@Classname	StartCusGrpInfoApplyOp.java
 *@Version 1.0	
 *@Since   1.0 	Mar 11, 2010 
 *@Copyright 	yuchengtech
 *@Author 		g
 *@Description：关联(集团)客户申请提交校验类
 *@Lastmodified 
 *@Author	    
 */
public class StartCusGrpInfoApplyOp extends CMISOperation{
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		int sign = 0;
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
			
			
			/**
			 * 判断该申请集团向下的成员是否已经存在别的有效集团中
			 */
			IndexedCollection iCollApply = tmdao.queryList("CusGrpMemberApply", " where serno='"+serno+"'", connection);
			for(int i=0;i<iCollApply.size();i++){
				KeyedCollection kc = (KeyedCollection) iCollApply.get(i);
				String cus_id = (String) kc.getDataValue("cus_id");
				String cus_name = (String) kc.getDataValue("cus_name");
				/**
				 * 返回sign= 1表示存在 0表示不再在
				 */
				sign = cusGrpInfoApplyComponent.isExistCusGrpMember(cus_id);
				if(sign == 1){
					throw new EMPException("["+cus_name+"]已经是有效集团的成员，不能再为该集团增加该成员！！！");
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
