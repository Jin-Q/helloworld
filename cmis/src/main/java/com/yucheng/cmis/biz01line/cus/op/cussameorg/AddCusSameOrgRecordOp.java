package com.yucheng.cmis.biz01line.cus.op.cussameorg;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.op.conOtherSys.EsbReportTool;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.pubopera.PubOperaComponent;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.TimeUtil;

public class AddCusSameOrgRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSameOrg";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String flagInfo = "";
			String sameOrgNo = "";//同业机构号
			String comInsCode = "";//组织机构代码
			KeyedCollection kColl = null;
			
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//先查询是否已在本地开户(通过同业行号和组织机构代码进行查询)
			sameOrgNo = (String)kColl.getDataValue("same_org_no");
			comInsCode = (String)kColl.getDataValue("com_ins_code");
			String condition = "where (same_org_no='"+sameOrgNo+"' or com_ins_code='"+comInsCode+"') and same_org_type is not null ";
			String condition4tmp = "where same_org_no='"+sameOrgNo+"' or com_ins_code='"+comInsCode+"' ";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				flagInfo = PUBConstant.FAIL;
			}else{	//将同业客户信息发送到Ecif进行开户
				String cusId = sendSameOrgMsg(kColl, context, connection);
				if(!"".equals(cusId)){
					PubOperaComponent pubOperaComponent = (PubOperaComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance("PubOpera", context, connection);
					pubOperaComponent.deleteDateByTableAndCondition("Cus_Same_Org", condition4tmp);
					
					kColl.setDataValue("cus_id", cusId);
					int i = dao.insert(kColl, connection);
					flagInfo = PUBConstant.SUCCESS;
				}else{
					flagInfo = "error";
				}
			}
			
			context.addDataField("flag", flagInfo);
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
	
	//组装报文并发送到Ecif
	public String sendSameOrgMsg(KeyedCollection kColl, Context context, Connection connection) throws Exception{
		String cusId = "";
		cusId = "TY"+CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);
		/*StringBuffer sb = new StringBuffer();//记录日志信息
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
		CompositeData cd = null;
		//组装报文头
		cd = EsbReportTool.produceHead("11002000019", "09", context);
		//BODY
		CompositeData body_struct = new CompositeData();
		CompositeData mmci_struct = new CompositeData();
		try {
			Field sameOrgNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			sameOrgNo.setValue(kColl.getDataValue("same_org_no").toString());
			mmci_struct.addField("BANK_ID", sameOrgNo);
			
			Field status = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			status.setValue("");
			mmci_struct.addField("STATUS", status);
			
			Field type = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			type.setValue("");
			mmci_struct.addField("TYPE", type);
			
			//同业机构类型与ecif暂时不匹配 TODO
			Field sameOrgType = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			sameOrgType.setValue(kColl.getDataValue("same_org_type").toString());
			mmci_struct.addField("BANK_CATEGORY_CODE", sameOrgType);
			//所属直参行号
			Field directBankCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 14));
			directBankCode.setValue("");
			mmci_struct.addField("DIRECT_BANK_CODE", directBankCode);
			
			Field belongBra = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			belongBra.setValue("");
			mmci_struct.addField("BELONG_BRANCH_NO", belongBra);
			
			Field higherPart = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			higherPart.setValue("");
			mmci_struct.addField("HIGHER_PARTICIPANT_NAME", higherPart);
			
			Field belongPbc = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			belongPbc.setValue("");
			mmci_struct.addField("BELONG_PBC_CODE", belongPbc);
			
			Field address = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			address.setValue(kColl.getDataValue("address").toString());
			mmci_struct.addField("CITY", address);
			
			Field clearAcctStatus = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			clearAcctStatus.setValue("");
			mmci_struct.addField("CLEAR_ACCT_STATUS", clearAcctStatus);
			
			Field casModDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			casModDate.setValue("");
			mmci_struct.addField("CAS_MOD_DATE", casModDate);
			
			Field casModTime = new Field(new FieldAttr(FieldType.FIELD_STRING, 9));
			casModTime.setValue("");
			mmci_struct.addField("CAS_MOD_TIME", casModTime);
			
			Field sameOrgCnname = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			sameOrgCnname.setValue(kColl.getDataValue("same_org_cnname").toString());
			mmci_struct.addField("FULL_NAME", sameOrgCnname);
			
			Field shortName = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			shortName.setValue("");
			mmci_struct.addField("SHORT_NAME", shortName);
			
			//街道
			Field street = new Field(new FieldAttr(FieldType.FIELD_STRING, 200));
			street.setValue(kColl.getDataValue("street").toString());
			mmci_struct.addField("ADDRESS", street);
			
			Field postalCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 7));
			postalCode.setValue("");
			mmci_struct.addField("POSTAL_CODE", postalCode);
			
			//电话
			Field linkmanFax = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			linkmanFax.setValue(kColl.getDataValue("linkman_phone").toString());
			mmci_struct.addField("TELEPHONE_NO", linkmanFax);
			
			Field email = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			email.setValue(kColl.getDataValue("linkman_email").toString());
			mmci_struct.addField("EMAIL", email);
			
			Field effectDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			effectDate.setValue("");
			mmci_struct.addField("EFFECT_DATE", effectDate);
			
			Field abateDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			abateDate.setValue("");
			mmci_struct.addField("ABATE_DATE", abateDate);
			
			Field lastUpdTime = new Field(new FieldAttr(FieldType.FIELD_STRING, 14));
			lastUpdTime.setValue("");
			mmci_struct.addField("LAST_UPDATE_TIME", lastUpdTime);
			
			Field lastUpdOper = new Field(new FieldAttr(FieldType.FIELD_STRING, 80));
			lastUpdOper.setValue("");
			mmci_struct.addField("LAST_UPDATE_OPERATION", lastUpdOper);
			
			Field recordUpdTerms = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			recordUpdTerms.setValue("");
			mmci_struct.addField("RECORD_UPDATE_TERMS", recordUpdTerms);
			
			Field remark = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			remark.setValue("");
			mmci_struct.addField("REMARK", remark);
			
			//是否授信 TODO
			Field isAuth = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			isAuth.setValue("");
			mmci_struct.addField("IS_AUTH", isAuth);
			
			//信用等级
			Field crdGrade = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			crdGrade.setValue(kColl.getDataValue("crd_grade").toString());
			mmci_struct.addField("CREDIT_LEVEL", crdGrade);
			
			//评级到期日
			Field evalMaturity = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			evalMaturity.setValue(kColl.getDataValue("eval_maturity").toString());
			mmci_struct.addField("CREDIT_LEVEL_EFF_DATA", evalMaturity);
			
			Field cusIdTmp = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			cusIdTmp.setValue("");
			mmci_struct.addField("CLIENT_NO", cusIdTmp);
			
			Field country = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			country.setValue(kColl.getDataValue("country").toString());
			mmci_struct.addField("COUNTRY_CODE", country);
			
			//成立日期
			String sameOrgEst = kColl.getDataValue("same_org_est").toString();
			if(sameOrgEst!=null&&!"".equals(sameOrgEst)){
				sameOrgEst = sameOrgEst.replace("-", "");
			}
			Field mmOrgCDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			mmOrgCDate.setValue(sameOrgEst);
			mmci_struct.addField("MM_ORG_CREATE_DATE", mmOrgCDate);
			
			Field bankProLic = new Field(new FieldAttr(FieldType.FIELD_STRING, 15));
			bankProLic.setValue(kColl.getDataValue("bank_pro_lic").toString());
			mmci_struct.addField("FIN_ORG_LICENSE_NO", bankProLic);
			
			Field comInsNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			comInsNo.setValue(kColl.getDataValue("com_ins_no").toString());
			mmci_struct.addField("LICENSE_NO", comInsNo);
			
			//上级机构客户编号
			Field superBranch = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			superBranch.setValue(kColl.getDataValue("up_org_no").toString());
			mmci_struct.addField("SUPER_BRANCH_CLIENT_NO", superBranch);
			
			Field regCurType = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			regCurType.setValue(kColl.getDataValue("reg_cur_type").toString());
			mmci_struct.addField("REGIST_CAPITAL_CCY", regCurType);
			
			//注册资本
			Field regCapAmt = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,2));
			if(kColl.getDataValue("reg_cap_amt")==null||"".equals(kColl.getDataValue("reg_cap_amt"))){
				regCapAmt.setValue(Double.parseDouble("0"));
			}else{
				regCapAmt.setValue(Double.parseDouble(kColl.getDataValue("reg_cap_amt").toString()));
			}
			mmci_struct.addField("REGIST_CAPITAL", regCapAmt);
			
			//实收资本
			Field paidCapAmt = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,2));
			paidCapAmt.setValue(Double.parseDouble(kColl.getDataValue("paid_cap_amt").toString()));
			mmci_struct.addField("ACTUAL_CAPITAL", paidCapAmt);
			
			//实收资本
			Field assets = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20,2));
			assets.setValue(Double.parseDouble(kColl.getDataValue("assets").toString()));
			mmci_struct.addField("ASSET", assets);
			
			//监管评级
			Field custLevel = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			custLevel.setValue(kColl.getDataValue("cust_level").toString());
			mmci_struct.addField("RATING_LEVEL", custLevel);
			
			Field ratingExpDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			ratingExpDate.setValue("");
			mmci_struct.addField("RATING_EXPIRY_DATE", ratingExpDate);
			
			//上市标志
			Field mrkFlag = new Field(new FieldAttr(FieldType.FIELD_STRING, 1));
			mrkFlag.setValue(kColl.getDataValue("mrk_flag").toString());
			mmci_struct.addField("CUCYFG", mrkFlag);
			
			//上市地
			Field mrlArea = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			mrlArea.setValue(kColl.getDataValue("mrl_area").toString());
			mmci_struct.addField("CUAREA", mrlArea);
			
			//股票代码
			Field stockNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 10));
			stockNo.setValue(kColl.getDataValue("stock_no").toString());
			mmci_struct.addField("CUSKCD", stockNo);
			
			//联系人姓名
			Field linkmanName = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			linkmanName.setValue(kColl.getDataValue("linkman_name").toString());
			mmci_struct.addField("CONTACTER_NAME", linkmanName);
			
			//联系人职务
			Field linkmanDuty = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			linkmanDuty.setValue(kColl.getDataValue("linkman_duty").toString());
			mmci_struct.addField("CONTACTER_POST", linkmanDuty);
			
			//联系人手机
			Field linkmanMobile = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			linkmanMobile.setValue(kColl.getDataValue("linkman_mobile_no").toString());
			mmci_struct.addField("CONTACTER_MOBILE", linkmanMobile);
			
			//传真
			Field faxNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			faxNo.setValue(kColl.getDataValue("linkman_fax").toString());
			mmci_struct.addField("FAX_NO", faxNo);
			
			//主管客户经理---放登记人
			Field mainMgr = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
//			mainMgr.setValue(kColl.getDataValue("manager_id").toString());
			mainMgr.setValue(kColl.getDataValue("input_id").toString());
			mmci_struct.addField("CHI_CUST_MANAGER", mainMgr);
			
			//主管机构---放登记机构
			Field mainBrId = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
//			mainBrId.setValue(kColl.getDataValue("manager_br_id").toString());
			mainBrId.setValue(kColl.getDataValue("input_br_id").toString());
			mmci_struct.addField("MANA_ORG", mainBrId);
			
			//SWIFT_ID
			Field swiftNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 12));
			swiftNo.setValue(kColl.getDataValue("swift_no").toString());
			mmci_struct.addField("SWIFT_ID", swiftNo);
			
			//组织机构代码
			Field comInsCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 50));
			comInsCode.setValue(kColl.getDataValue("com_ins_code").toString());
			mmci_struct.addField("ORG_CODE", comInsCode);
			
			//中文名
			Field cnname = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			cnname.setValue(kColl.getDataValue("same_org_cnname").toString());
			mmci_struct.addField("MM_ORG_CN_NAME", cnname);
			
			//英文名
			Field enname = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			enname.setValue(kColl.getDataValue("same_org_enname").toString());
			mmci_struct.addField("MM_ORG_EN_NAME", enname);
			
			//网址
			Field orgSite = new Field(new FieldAttr(FieldType.FIELD_STRING, 100));
			orgSite.setValue(kColl.getDataValue("org_site").toString());
			mmci_struct.addField("MM_ORG_URL", orgSite);
			
			body_struct.addStruct("MM_CLIENT_INFO_STRUCT", mmci_struct);
			cd.addStruct("BODY", body_struct);
			
			sb.append("**********************请求报文开始************************\n");
			sb.append(cd);//记录请求报文
			sb.append("**********************请求报文结束************************\n");
			CompositeData resp = ESBClient.request(cd);//发送报文
			sb.append("**********************接收报文开始************************\n");
			sb.append(resp);//记录返回报文
			sb.append("**********************接收报文结束************************\n");
			
			//判断交易是否成功
			String retStatus = resp.getStruct("SYS_HEAD").getField("RET_STATUS").strValue();
			String retCode = resp.getStruct("SYS_HEAD").getArray("RET").getStruct(0).getField("RET_CODE").strValue();
			if ("S".equals(retStatus) && "000000".equals(retCode)){
				CompositeData bodyCd = resp.getStruct("BODY");
				//客户码 
				cusId = bodyCd.getStruct("MM_CLIENT_INFO_STRUCT").getField("CLIENT_NO").strValue();
			}
			sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		} catch (Exception e) {
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"----------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			throw e;
		} */
		
		return cusId;
	}
}
