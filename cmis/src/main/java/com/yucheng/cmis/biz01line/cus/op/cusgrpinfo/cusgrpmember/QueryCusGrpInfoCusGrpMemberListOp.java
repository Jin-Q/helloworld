package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo.cusgrpmember;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGrpInfoCusGrpMemberListOp extends CMISOperation {

	private final String modelId = "CusGrpMember";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			
			String grp_no_value = (String) context.getDataValue("CusGrpInfo.grp_no");
				
			if (grp_no_value == null) {
				throw new EMPException("parent primary key not found!");
			}
			
			String conditionStr = "where grp_no = '" + grp_no_value
					+ "' order by grp_no desc,cus_id desc";

			int size = 10;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			
			//添加客户证件号码字段
			CusBaseComponent cbc = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
			IndexedCollection iCollFinal = new IndexedCollection(iColl.getName());
			for(int i =0;i<iColl.size();i++){
				KeyedCollection kCollTemp = (KeyedCollection)iColl.get(i);
				String cus_id = (String)kCollTemp.getDataValue("cus_id");
				CusBase cc = cbc.getCusBase(cus_id);
				kCollTemp.addDataField("cert_code", cc.getCertCode());
				iCollFinal.add(kCollTemp);
			}
			SInfoUtils.addSOrgName(iCollFinal, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iCollFinal, new String[] { "cus_manager" });
			this.putDataElement2Context(iCollFinal, context);

			TableModelUtil.parsePageInfo(context, pageInfo);

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
