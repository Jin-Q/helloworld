package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class OutPoolDpoDrfpoManaListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no = "";
		try{
			connection = this.getConnection(context);
			drfpo_no = (String) context.getDataValue("drfpo_no");
			//构建组件类
			DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			/**查询已出池状态的汇票信息*/
			IndexedCollection iColl = dpoComponent.getPorderListByDrfpoNo(drfpo_no, "'03'");
		
			/**查询已在池状态的汇票信息*/
			IndexedCollection iCollIn = dpoComponent.getPorderListByDrfpoNo(drfpo_no,"'01','06'");
			iColl.setName("IqpDrfpoManaList");
			iCollIn.setName("IqpDrfpoManaInPoList");
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(iCollIn, context);
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
