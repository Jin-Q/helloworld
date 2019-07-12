package com.yucheng.cmis.biz01line.iqp.op.iqpassetregiapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.msiimple.ESBServiceInterfaceImple;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;


/*
 * 资产化登记校验产品是否可做资产化登记
 * 产品为：消费性、经营性、成长贷、流贷业务、特色贷、便利贷、组合贷、固贷业务
 * @author 邓亚辉
 * */
public class CheckIqpAssetRegiPrdOp extends CMISOperation {
	//private final String iqpModelId = "IqpAssetRegiApp";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		String bill_no = null;

		try {
			connection = this.getConnection(context);
			try {
				bill_no = (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value bill_no cannot be null!");
			String priid = null;
			//String lm_prd_id = null;
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where bill_no = '"+bill_no+"' and table_model in('AccLoan','AccPad')";
			IndexedCollection acciColl = dao.queryList("AccView", condition, connection);
			/*调用ESB接口进行核算业务品种比对*/
     		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			//ESBServiceInterfaceImple service = (ESBServiceInterfaceImple) serviceJndi.getModualServiceById("esbServices", "esb");
			//lm_prd_id = service.getPrdBasicAssetPro2LM(bill_no,context,connection);
     		/**modified by lisj 2015-4-2 需求编号：XD150303017 关于资产证券化的信贷系统改造需求 begin**/
			if(acciColl.size()!=0){
				for(int i=0;i<acciColl.size();i++){
					KeyedCollection accKColl = (KeyedCollection) acciColl.get(i);
					priid = (String) accKColl.getDataValue("prd_id");//获取产品编号
					
					IndexedCollection prdIColl = dao.queryList("PrdBasicinfo", " where supcatalog in ('PRD20120802659', 'PRD20120802664', 'PRD20120802665', 'PRD20120802666', 'PRD20120802667', 'PRD20120802668', 'PRD20120802658', 'PRD20120802671') and prdid = '"+priid+"' ", connection);
					if(prdIColl!=null && prdIColl.size() > 0){
						context.addDataField("flag", "success");
						context.addDataField("msg", "");
					}else{
						context.addDataField("flag", "error");
						context.addDataField("msg", "该产品不可做资产证券化登记");
					}				
//					/*调用规则进行判断*/
//					ShuffleServiceInterface shuffleService = null;
//					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
//					Map<String, String> inMap = new HashMap<String, String>();
//					inMap.put("IN_PRD_ID", priid);
//					Map<String, String> outMap = new HashMap<String, String>();
//					outMap=shuffleService.fireTargetRule("IQPASSETREGI", "CHECKIQPASSETPRD", inMap);
//					String outFlag = outMap.get("OUT_是否通过").toString();
//					//String outInfo = outMap.get("OUT_提示信息").toString();
//					if("通过".equals(outFlag)){
//						context.addDataField("flag", "success");
//						context.addDataField("msg", "");
//					}else{
//						context.addDataField("flag", "error");
//						context.addDataField("msg", "该产品不可做资产证券化登记");
//					}
				}
			}
			/**modified by lisj 2015-4-2 需求编号：XD150303017 关于资产证券化的信贷系统改造需求 end**/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new EMPException();
		}finally{
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}


}
