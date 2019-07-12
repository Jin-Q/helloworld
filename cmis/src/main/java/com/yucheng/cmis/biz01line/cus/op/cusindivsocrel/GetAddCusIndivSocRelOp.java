package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivSocRelComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetAddCusIndivSocRelOp extends CMISOperation{

	private final String modelId = "CusIndivSocRel";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		String cusid = (String) context.getDataValue("cus_id");
		String indiv_sex = (String) context.getDataValue("indiv_sex");
		
		context.setDataValue("cus_id", cusid);
		if("1".equals(indiv_sex)){
			indiv_sex = "2";
		}else if("2".equals(indiv_sex)){
			indiv_sex = "1";
		}
		context.setDataValue("indiv_sex", indiv_sex);
		
		KeyedCollection kColl = new KeyedCollection("CusIndivSocRel");
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			CusIndivSocRelComponent socCus = (CusIndivSocRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusIndivSocRel", context, connection);
			//是否有配偶
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection ic = dao.queryList(modelId, " where cus_id = '" + cusid + "' and indiv_cus_rel='1'", connection);
			if( ic.size() == 1 ){
				CusIndivSocRel my = socCus.getSpouseCusIndivSocRel(cusid);
				CusIndivSocRel spouse = socCus.getSpouseCusIndivSocRel(my.getCusIdRel());
				CusBase relBase = new CusBase();
				CusIndiv relIndiv = new CusIndiv();
//				cusIndiv.setCusId(my.getCusIdRel());
//				cusIndiv.setCertType(spouse.getIndivRelCertTyp());
//				cusIndiv.setCertCode(spouse.getIndivRlCertCode());
				CusBaseComponent baseComp = (CusBaseComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context, connection);
				relBase = baseComp.getCusBase(my.getCusIdRel());
				CusIndivComponent comp = (CusIndivComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusIndiv", context, connection);
				relIndiv = comp.getCusIndiv(my.getCusIdRel());
				if(spouse !=null ){
					
					kColl.addDataField("weatherSpouse", 1);
					//证件类型
					kColl.addDataField("indiv_rel_cert_typ", relBase.getCertType());
					//证件号码
					kColl.addDataField("indiv_rl_cert_code", relBase.getCertCode());
					//配偶客户码
					kColl.addDataField("cus_id_rel", my.getCusIdRel());
					//姓名
					kColl.addDataField("indiv_rel_cus_name", relBase.getCusName());
					//性别
					kColl.addDataField("indiv_sex", relIndiv.getIndivSex());
					
					kColl.addDataField("cus_id", cusid);
					//年收入
					kColl.addDataField("indiv_rl_y_incm", relIndiv.getIndivAnnIncm());
					
					//工作单位
					kColl.addDataField("indiv_com_name", relIndiv.getIndivComName());
				}
			}else
				kColl.addDataField("weatherSpouse", 2);
			
			this.putDataElement2Context(kColl, context);
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
