package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoApplyComponent;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberApplyComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckExistInGrpMemberApply extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
 		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String cus_id = (String)context.getDataValue("cus_id");
			String menuId = (String)context.getDataValue("menuId");
			String flag = "cg";
			
			//构件业务处理类
			CusGrpMemberApplyComponent cgic = (CusGrpMemberApplyComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(PUBConstant.CUSGRPMEMBERAPPLYCOMPONENT, context, connection);
			CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFOAPPLYCOMPONENT,context,connection);
			String str = cusGrpInfoApplyComponent.CheakParCusIdApply(cus_id);
			
			if(menuId.equals("grpCognizChg")){
				int cgmCheak2 = cgic.cheakCusGrpMemberApplyInt(cus_id);// 查询该客户是否有集团成员新增变更的申请
				if(cgmCheak2>0){//判断该客户是否已经插入集团客户成员申请表
					flag = "sq";
				}
			}else if(!str.equals("canInsert")){
				flag = "mgs";
			}else{
				int cgmCheak = cgic.cheakCusGrpMemberApply(cus_id);
				if(cgmCheak>0){//判断该客户是否已经插入集团客户成员申请表
					flag = "cyls";;
				}else{
					int cgmCheak2 = cgic.cheakCusGrpMemberApplyInt(cus_id);// 查询该客户是否有集团成员新增变更的申请
					if(cgmCheak2>0){//判断该客户是否已经插入集团客户成员申请表
						flag = "sq";
					}
				}				
			}		
			
			KeyedCollection kColl_CusBase = dao.queryDetail("CusCom", cus_id, connection);
			String com_grp_mode = kColl_CusBase.getDataValue("com_grp_mode").toString();
			if(com_grp_mode.equals("9")){
//				flag = "cant_grp";	//非集团客户，不能集团认定
			}else if(com_grp_mode.equals("3")){
				flag = "cant_member";	//母公司不能手动新增到成员表
			}
			
			context.addDataField("flag",flag);
			context.addDataField("com_grp_mode",com_grp_mode);
		}catch (EMPException ee) {
			context.addDataField("flag","");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag","");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}