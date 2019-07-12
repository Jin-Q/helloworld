package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo.cusgrpmember;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusGrpInfoCusGrpMemberRecordOp extends CMISOperation {
	 
	private final String modelId = "CusGrpMember";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//根据cusid从cuscom表中查出当前成员所属的主管机构和主管客户经理
			String cusId = (String) kColl.getDataValue("cus_id");
			if(cusId ==null || "".equals(cusId.trim())){
				throw new EMPJDBCException("The values ["+cusId+"] cannot be empty!");
			}
			/**
			 * 判断该客户是否有正在进行的一般授信或是一般授信变更操作
			 */
			TableModelDAO dao = this.getTableModelDAO(context);
			//一般授信业务
			IndexedCollection indexColl1 = dao.queryList("LmtApply", " where cus_id ='"+cusId+"' and approve_status in('000','111','992')", connection);
			if(indexColl1.size()>0){
				throw new CMISException("该客户["+cusId+"]有正在做的一般授信业务，请先完成该客户的一般授信业务");
			}
			//一般变更授信业务
			IndexedCollection indexColl2 = dao.queryList("LmtApply", " where cus_id ='"+cusId+"' and app_type='02' and approve_status in('000','111','992')", connection);
			if(indexColl2.size()>0){
				throw new CMISException("该客户["+cusId+"]有正在做的一般授信变更业务，请先完成该客户的一般授信变更业务");
			}
			////转换类
			ComponentHelper cHelper = new ComponentHelper();
			CusGrpMember cusGrpMember = new CusGrpMember();
			cusGrpMember = (CusGrpMember)cHelper.kcolTOdomain(cusGrpMember, kColl);
			
			//构件业务处理类
			CusGrpMemberComponent cusGrpMemberComponent = (CusGrpMemberComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPMEMBER,context,connection);
			CusGrpMember cgmCheak = new CusGrpMember();
			//查询该客户是否已存在集团之中
			cgmCheak = cusGrpMemberComponent.cheakCusGrpMember(cusGrpMember.getCusId());
			if(cgmCheak.getGrpNo()==null){
				cusGrpMemberComponent.addCusGrpMember(cusGrpMember);
			}else{
				  throw new CMISException(cusGrpMember.getCusId()+"已是集团成员，不能再新增", cusGrpMember.getCusId()+"已是集团成员，不能再新增!<span onclick='javascript:history.go(-1)'>点击返回新增页面</span>");
			}
			
			//该客户的授信协议增加进集团授信协议
		//	ILmt iLmt = (ILmt)CMISComponentFactory
		//	.getComponentFactoryInstance().getComponentInterface("LmtInterface", context, connection);
		//	iLmt.mergerCusLmtToGrpLmt(cusGrpMember.getCusId(),cusGrpMember.getGrpNo());
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
