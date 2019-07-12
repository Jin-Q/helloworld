package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateCusIndivRepRecordOp extends CMISOperation {

	private final String modelId = "CusIndiv";
	private final String modelIdBase = "CusBase";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);
			connection.setAutoCommit(false);
			KeyedCollection kColl = null;
			KeyedCollection kCollBase = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollBase = (KeyedCollection) context.getDataElement(modelIdBase);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0 ||kCollBase==null||kCollBase.size()==0)
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
			
			String cusId = (String) kCollBase.getDataValue("cus_id");

			TableModelDAO dao = this.getTableModelDAO(context);

			//更新数据
			kColl.addDataField("cus_id", cusId);
			kCollBase.setDataValue("cus_status", "20");	//设置客户为正式客户
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			String certType = kCollBase.getDataValue("cert_type").toString();
			String certCode = kCollBase.getDataValue("cert_code").toString();
			
			//新增前先去查询核心是否有该客户
			
			if(certType==null || "".equals(certType) || certCode==null || "".equals(certCode)){
				throw new Exception("证件类型或证件号码不允许为空！");
			}
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			if("20".equals(certType)||"a".equals(certType)){//对公
				//组装报文头
				qryheadKcoll.put("SvcCd", "20130001");
				qryheadKcoll.put("ScnCd", "01");
				qryheadKcoll.addDataField("TxnMd","ONLINE");
				qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
				qryheadKcoll.addDataField("jkType","cbs");
				//BODY
				qrybodyColl.addDataField("QryInd","2");//2-按证件查询
				qrybodyColl.addDataField("AcctNoCrdNo",""); 
				qrybodyColl.addDataField("CstNo",""); 
				qrybodyColl.addDataField("IdentTp",getCertType(certType)); 
				qrybodyColl.addDataField("IdentNo",certCode); 
				qrybodyColl.addDataField("CtcTelNo",""); 
				qrybodyColl.addDataField("TaskSrlNo",""); 
			}else{//对私
				//新，查询(20130001)客户信息查询(01)客户指定条件信息查询
				
				qryheadKcoll.addDataField("SvcCd","20130001");
				qryheadKcoll.addDataField("ScnCd","01");
				qryheadKcoll.addDataField("TxnMd","ONLINE");
				qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
				qryheadKcoll.addDataField("jkType","cbs");
			
				qrybodyColl.addDataField("QryInd","2");//2-按证件查询
				qrybodyColl.addDataField("AcctNoCrdNo",""); 
				qrybodyColl.addDataField("CstNo",""); 
				qrybodyColl.addDataField("IdentTp",getCertType(certType)); 
				qrybodyColl.addDataField("IdentNo",certCode); 
				qrybodyColl.addDataField("CtcTelNo",""); 
				qrybodyColl.addDataField("TaskSrlNo",""); 
				//组装报文头
			
			}
			KeyedCollection retKColl = new KeyedCollection(); 
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			String opType = "01";//新增核心客户
			String cstNo = "";
			if(retKColl.containsKey("BODY")){
				KeyedCollection reBodyKcoll = (KeyedCollection) retKColl.get("BODY");
			    if(reBodyKcoll.containsKey("CstNo")&&NewStringUtils.isNotBlank((String)reBodyKcoll.getDataValue("CstNo"))){
			    	cstNo = (String)reBodyKcoll.getDataValue("CstNo");
			    	opType = "02";
			    }
				
			}
			if("01".equals(opType)){
				KeyedCollection headKcoll = new KeyedCollection();
				KeyedCollection bodyKcoll = new KeyedCollection();
				headKcoll.put("SvcCd", "20120001");
				headKcoll.put("ScnCd", "02");
				headKcoll.addDataField("TxnMd","ONLINE");
				headKcoll.addDataField("UsrLngKnd","CHINESE");
				headKcoll.addDataField("jkType","cbs");  
				
				bodyKcoll.addDataField("OprtnTp",opType);//操作类型
				bodyKcoll.addDataField("CstNo",cstNo);//客户号
				bodyKcoll.addDataField("CstGrpgTp","");//客户分组
				bodyKcoll.addDataField("CstTp",getCusType(valueOf(kCollBase.getDataValue("cus_type"))));//客户类型，已转行
				bodyKcoll.addDataField("CstClCtgry","101");//客户分类类别码值对应待处理
				bodyKcoll.addDataField("CstShrtNm",valueOf(kCollBase.getDataValue("cus_name")));//客户简称
				bodyKcoll.addDataField("CstChinNm",valueOf(kCollBase.getDataValue("cus_name")));//客户中文名称
				bodyKcoll.addDataField("CstEnNm","");//输入中文名称，英文给空字符串
				bodyKcoll.addDataField("CstNmSufxInf","");//客户名后缀
				bodyKcoll.addDataField("HlpMmryNm","");//助记名称
				bodyKcoll.addDataField("Adr",valueOf(kColl.getDataValue("indiv_rsd_addr")));//地址
				bodyKcoll.addDataField("CstNtntyCd",valueOf(kCollBase.getDataValue("cus_country")));//客户国籍代码,不用转换
				bodyKcoll.addDataField("RskCntrlCtyCd",valueOf(kCollBase.getDataValue("cus_country")));//默认CHN
				bodyKcoll.addDataField("LoProvCd",valueOf(kColl.getDataValue("indiv_houh_reg_add")).substring(0, 2));//省份码值对应关系，待处理
				bodyKcoll.addDataField("CstCityCd",valueOf(kColl.getDataValue("indiv_houh_reg_add")));//客户城市代码对应关系，待处理
				bodyKcoll.addDataField("NtvPlcInf",valueOf(kColl.getDataValue("indiv_brt_place")));//籍贯信息
				bodyKcoll.addDataField("DmstOvrsFlg","I");//境内境外标志
				bodyKcoll.addDataField("IdyTp","00000");//行业类型，码值对应关系待定
				bodyKcoll.addDataField("CstBsnKnd","999");//客户业务种类,码值对应关系待定
				bodyKcoll.addDataField("RsrvFldInf","");
				bodyKcoll.addDataField("RsrvFldInf1","");
				bodyKcoll.addDataField("RsrvFldInf2","");
				bodyKcoll.addDataField("RsrvFldInf3","");
				bodyKcoll.addDataField("RsrvFldInf4","");
				bodyKcoll.addDataField("SmyDsc","");//摘要
				bodyKcoll.addDataField("InnrCstFlg",getYN(valueOf(kColl.getDataValue("is_rela_cust"))));//内部客户标识，
				bodyKcoll.addDataField("CstExgRatePrfrncRto","");//客户汇率优惠比例
				bodyKcoll.addDataField("RskRto","");//风险权重
				bodyKcoll.addDataField("CstSt","0");//客户状态，码值对应关系待处理
				bodyKcoll.addDataField("CstStDsc","");//客户状态描述
				bodyKcoll.addDataField("AcctTxnSt","A");//账户交易状态
				bodyKcoll.addDataField("ClsDt","");//关闭日期
				bodyKcoll.addDataField("CstInd","N");//客户标识
				bodyKcoll.addDataField("CnlTp","020114");//渠道类型,信贷系统
				bodyKcoll.addDataField("FnlAmdtTlrNo",valueOf(kCollBase.getDataValue("cust_mgr")));//最后修改柜员号
				bodyKcoll.addDataField("WrtLngTp","");//书写语言种类
				bodyKcoll.addDataField("CmnctnLngTp","");//交流语言种类
				bodyKcoll.addDataField("FnlModDt",valueOf(context.getDataValue(CMISConstance.OPENDAY)).replaceAll("-", ""));//最后更改日期
				bodyKcoll.addDataField("TempCstFlg","");//临时客户标志
				bodyKcoll.addDataField("CrtTlrNo",valueOf(kCollBase.getDataValue("cust_mgr")));//创建柜员号
				bodyKcoll.addDataField("CrtDt",valueOf(kCollBase.getDataValue("input_date")).replaceAll("-", ""));//创建日期
				bodyKcoll.addDataField("PrntLngTp","");//打印语言种类
				bodyKcoll.addDataField("DataIncomInd","N");//资料不全标识
				bodyKcoll.addDataField("PEPInd","");//PEP标识
				bodyKcoll.addDataField("CstLvl","01");//客户等级/客户级别
				bodyKcoll.addDataField("EvltLvlDt","");//评级日期
				bodyKcoll.addDataField("RcvTaxFlg","N");//收税标志
				bodyKcoll.addDataField("CstAlsNm","");//客户别名
				bodyKcoll.addDataField("CstMgrCd",valueOf(context.getDataValue(CMISConstance.ATTR_CURRENTUSERID)));//柜员
				bodyKcoll.addDataField("PrftCnrlCd","");//利润中心代码
				bodyKcoll.addDataField("CntrlBrBnkCd",valueOf(context.getDataValue(CMISConstance.ATTR_ORGID)));//控制分行代码
				bodyKcoll.addDataField("DstcNm",valueOf(kColl.getDataValue("area_code")));//地区名称/区域名称
				bodyKcoll.addDataField("Pstcd",valueOf(kColl.getDataValue("post_code")));//邮政编码
				bodyKcoll.addDataField("CntrlDeptCd","");//控制部门代码
				bodyKcoll.addDataField("BlcklistCstFlg","N");//黑名单客户标志
				bodyKcoll.addDataField("PblcAgntNm","");//代办人姓名
				bodyKcoll.addDataField("PblcAgntIdentTp","");//代理人证件类型
				bodyKcoll.addDataField("PblcAgntIdentNo","");//代理人证件号码
				bodyKcoll.addDataField("PblcAgntIdentExprtnDt","");//代办人证件到期日期
				bodyKcoll.addDataField("SurnmLoFrntFlg","Y");//姓氏在前标志
				bodyKcoll.addDataField("TtlTp","");//称谓类型
				bodyKcoll.addDataField("RsdntSt","0");//居民状态
				bodyKcoll.addDataField("NtnCd",valueOf(kColl.getDataValue("indiv_ntn")));//民族代码
				bodyKcoll.addDataField("BrthDt",valueOf(kColl.getDataValue("indiv_dt_of_birth")).replaceAll("-", ""));//出生日期
				bodyKcoll.addDataField("GndTp",getSex(valueOf(kColl.getDataValue("indiv_sex"))));//性别
				//查询客户婚姻状况
				IndexedCollection CusSocIkcoll=dao.queryList("CusIndivSocRel", "where cus_id='"+cusId+"'", connection);
				String married="";
				if(CusSocIkcoll.size()>0){
					married="M";
				}else{
					married="S";
				}
				bodyKcoll.addDataField("MdnSt","S");//婚姻状况
				bodyKcoll.addDataField("MdnNm","");//婚前名称
				bodyKcoll.addDataField("BrthCtyNm",valueOf(kCollBase.getDataValue("cus_country")));//出生国家名称
				bodyKcoll.addDataField("MthrMdnNm","");//母亲婚前名称
				bodyKcoll.addDataField("OcpTp",getOcc(valueOf(kColl.getDataValue("indiv_occ"))));//职业类型
				bodyKcoll.addDataField("RsdnDsc","");//居住状况
				bodyKcoll.addDataField("MjrQualfCd",valueOf(kColl.getDataValue("indiv_crtfctn")));//专业职称代码,不用转
				bodyKcoll.addDataField("HighEdctTp",getEdu(valueOf(kColl.getDataValue("indiv_edt"))));//最高学历类型
				bodyKcoll.addDataField("SoclInsNo","");//社会保险号
				bodyKcoll.addDataField("SprtPrsnNum","");//供养人数
				bodyKcoll.addDataField("WrkCoNm",valueOf(kColl.getDataValue("indiv_com_name")));//工作单位名称
				bodyKcoll.addDataField("WrkCoBlngIdyCd",valueOf(kColl.getDataValue("indiv_com_typ")));//工作单位所属行业代码
				bodyKcoll.addDataField("EmplymntBegTm",valueOf(kColl.getDataValue("indiv_work_job_y")));//雇佣开始时间
				bodyKcoll.addDataField("PrsDsc",valueOf(kColl.getDataValue("indiv_hobby")));//兴趣爱好
				bodyKcoll.addDataField("PstDsc",valueOf(kColl.getDataValue("indiv_crtfctn")));//职务,不用转
				bodyKcoll.addDataField("HighDgrTp",getDegree(valueOf(kColl.getDataValue("indiv_dgr"))));//最高学位类型
				bodyKcoll.addDataField("SlryCcy","CNY");//薪资币种
				bodyKcoll.addDataField("PerMoSlryAmt","");//每月薪水
				bodyKcoll.addDataField("SlryAcctNo",valueOf(kColl.getDataValue("indiv_sal_acc_no")));//工资账号
				bodyKcoll.addDataField("SlryAcctOpnAcctBnkNo",valueOf(kColl.getDataValue("indiv_sal_acc_bank")));//工资账户开户行行号
				bodyKcoll.addDataField("AnulIncmAmt",valueOf(kColl.getDataValue("indiv_ann_incm")));//年收入金额
				bodyKcoll.addDataField("IncmVerfInd","");//收入验证标识
				bodyKcoll.addDataField("IncmVerfTm","");//收入验证时间
				bodyKcoll.addDataField("IncmVrfrCd","");//收入验证人编码
				bodyKcoll.addDataField("OcpnTm","");//入住时间
				bodyKcoll.addDataField("RntlCcy","");//租金币种
				bodyKcoll.addDataField("MoRntlAmt","");//月租金
				bodyKcoll.addDataField("CltlCcy","");//抵押币种
				bodyKcoll.addDataField("MoEndMrtgAmt","");//月末押付金额
				bodyKcoll.addDataField("ChldrnNum","");//子女数量
				bodyKcoll.addDataField("GrpFrzFlg","");//群冻结标志
				bodyKcoll.addDataField("FrzRsnTp","");//冻结原因类型
				bodyKcoll.addDataField("MbrshpNo","");//会员编号
				IndexedCollection identInfIColl = new IndexedCollection();
				identInfIColl.setName("IdentInfArry");
				
				KeyedCollection identInfKColl = new KeyedCollection();
				identInfKColl.addDataField("CstNo", cstNo);//客户号
				identInfKColl.addDataField("IdentTp", getCertType(valueOf(kCollBase.getDataValue("cert_type"))));//证件类型
				identInfKColl.addDataField("IdentNo", valueOf(kCollBase.getDataValue("cert_code")));//证件号码
				identInfKColl.addDataField("IssuCtyCd", valueOf(kCollBase.getDataValue("cus_country")));//签发国家代码
				identInfKColl.addDataField("IssuCityCd", valueOf(kColl.getDataValue("indiv_houh_reg_add")));//签发城市代码
				identInfKColl.addDataField("IssuDt", valueOf(kColl.getDataValue("indiv_id_start_dt")).replaceAll("-", ""));//签发日期
				identInfKColl.addDataField("IssuAdr", valueOf(kColl.getDataValue("indiv_houh_reg_add")));//签发地址
				identInfKColl.addDataField("ExprtnDt", valueOf(kColl.getDataValue("indiv_id_exp_dt")).replaceAll("-", ""));//到期日期
				identInfKColl.addDataField("ChiefFlg", "Y");//首要标志
				identInfKColl.addDataField("AuthInstCd", "");//授权机构代码
				identInfKColl.addDataField("IssuAdrDstcCd", valueOf(kColl.getDataValue("indiv_houh_reg_add")));//签发地地区代码
				identInfKColl.addDataField("OprtnTp", opType);//操作类型
				identInfKColl.addDataField("IssuProvCd", valueOf(kColl.getDataValue("indiv_houh_reg_add")).substring(0, 2));//签发省份代码
				identInfKColl.addDataField("LastChkDt", "");//上次核查日期
				identInfIColl.add(identInfKColl);
				bodyKcoll.addDataElement(identInfIColl);
				
				IndexedCollection cstCtcInfIColl = new IndexedCollection();
				cstCtcInfIColl.setName("CstCtcInfArry");
				KeyedCollection cstCtcInfKColl = new KeyedCollection();
				cstCtcInfKColl.addDataField("CstNo", cstNo);
				cstCtcInfKColl.addDataField("CtcPrsnNm", "");
				cstCtcInfKColl.addDataField("CtcMd", "");
				cstCtcInfKColl.addDataField("ChiefFlg", "");
				cstCtcInfKColl.addDataField("CtcMdTp", "01");
				cstCtcInfKColl.addDataField("CtcMdInf", kColl.getDataValue("mobile"));
				cstCtcInfKColl.addDataField("PrfrdCtcMdFlg", "");
				cstCtcInfKColl.addDataField("OprtnTp", opType);
				cstCtcInfIColl.add(cstCtcInfKColl);
				bodyKcoll.addDataElement(cstCtcInfIColl);
				
				bodyKcoll.addDataField("ChkRsltFlg", "");//核查结果标志
				bodyKcoll.addDataField("NonChkRsn", "");
				bodyKcoll.addDataField("DisplExplnDsc", "");
				bodyKcoll.addDataField("RtndFlg", "");
				bodyKcoll.addDataField("Rmk", "");
				bodyKcoll.addDataField("TxnLglPrsnCd", "");
				bodyKcoll.addDataField("CstUsedNm", "");
				bodyKcoll.addDataField("SplyRsn", "");
				//是否农户
				if(kColl.containsKey("agri_flg")){
					if("1".equals((String)kColl.getDataValue("agri_flg"))){
						bodyKcoll.addDataField("AgrcFlg", "Y");
					}else if("2".equals((String)kColl.getDataValue("agri_flg"))){
						bodyKcoll.addDataField("AgrcFlg", "N");
					}else{
						bodyKcoll.addDataField("AgrcFlg", "");
					}
				}else{
					bodyKcoll.addDataField("AgrcFlg", "");
				}
				bodyKcoll.addDataField("CrtCnlInf", "");
				bodyKcoll.addDataField("CrdtLvl", "");
				bodyKcoll.addDataField("BnkInnrIdyCtgryTp", valueOf(kColl.getDataValue("indiv_com_fld")));
				bodyKcoll.addDataField("CtyStdIdyCtgryTp", valueOf(kColl.getDataValue("indiv_com_fld")));
				bodyKcoll.addDataField("SoclInsNo1", "");
				bodyKcoll.addDataField("CstStdPrmptInf", "");//客户标准提示
	
				IndexedCollection cmnctnAdrInfIColl = new IndexedCollection();
				cmnctnAdrInfIColl.setName("CmnctnAdrInfArry");
				KeyedCollection cmnctnAdrInfKColl = new KeyedCollection(); 
				cmnctnAdrInfKColl.addDataField("OprtnTp", opType);
				cmnctnAdrInfKColl.addDataField("CstNo", cstNo);
				cmnctnAdrInfKColl.addDataField("AdrTp", "04");//地址类型
				cmnctnAdrInfKColl.addDataField("CtyNm", valueOf(kCollBase.getDataValue("cus_country")));//国家名称
				cmnctnAdrInfKColl.addDataField("ProvNm", valueOf(kColl.getDataValue("indiv_rsd_addr")).substring(0, 2));//省份名称
				cmnctnAdrInfKColl.addDataField("CityCd", valueOf(kColl.getDataValue("indiv_rsd_addr")));//城市代码
				cmnctnAdrInfKColl.addDataField("DstcCd", valueOf(kColl.getDataValue("indiv_rsd_addr")));//地区代码
				cmnctnAdrInfKColl.addDataField("Adr", valueOf(kColl.getDataValue("street3")));//地址
				cmnctnAdrInfKColl.addDataField("Pstcd", valueOf(kColl.getDataValue("post_code")));//邮政编码
				cmnctnAdrInfKColl.addDataField("AdrMd", "");
				cmnctnAdrInfKColl.addDataField("PrfrdIdentFlg", ""); 
				cmnctnAdrInfIColl.add(cmnctnAdrInfKColl);
				bodyKcoll.addDataElement(cmnctnAdrInfIColl);
				
				IndexedCollection qryPswdInfIColl = new IndexedCollection();
				qryPswdInfIColl.setName("QryPswdInfArry");
				KeyedCollection qryPswdInfKColl = new KeyedCollection();  
				qryPswdInfKColl.addDataField("PswdTp", "QY");
				qryPswdInfKColl.addDataField("QryPswdStrg", "000000");
				qryPswdInfKColl.addDataField("PswdTkEffDt", (valueOf(context.getDataValue("OPENDAY"))).replace("-", "")); //密码生效日期
	 
				qryPswdInfIColl.add(qryPswdInfKColl);
				bodyKcoll.addDataElement(qryPswdInfIColl);
				
				bodyKcoll.addDataField("EvdnDsc", "");
				bodyKcoll.addDataField("SpfNtrPrsnCd", "");
				bodyKcoll.addDataField("CoChrctrstcDsc", "");
				bodyKcoll.addDataField("CstRmk", "");
				bodyKcoll.addDataField("SoclRltnpDsc", "");
	
				retKColl = ESBUtil.sendEsbMsg(headKcoll,bodyKcoll);
				KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
				KeyedCollection body=(KeyedCollection)retKColl.getDataElement("BODY");
				IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				if(!"000000".equals(retCd)){
					connection.rollback();
					context.addDataField("flag", "fail");
					context.addDataField("message", (String)retObj.getDataValue("RetInf"));
					return "0";
					//throw new EMPException((String)retObj.getDataValue("RetInf"));
				}else{
					String hxCusId=valueOf(body.getDataValue("CstNo"));
					//KeyedCollection cusKcoll=new KeyedCollection("CusBase");
					//cusKcoll.addDataField("cus_id", cusId);
					//cusKcoll.addDataField("hx_cus_id", hxCusId);
					//dao.update(cusKcoll, connection);
					KeyedCollection cusKcoll=new KeyedCollection();
					cusKcoll.put("cus_id", cusId);
					SqlClient.update("updateCusBase", cusKcoll, hxCusId, null, connection);
				}
			}else{
				KeyedCollection cusKcoll=new KeyedCollection();
				cusKcoll.put("cus_id", cusId);
				SqlClient.update("updateCusBase", cusKcoll, cstNo, null, connection);
			}
			//将提示信息中的数据完成
			/*String condition = " where cus_id='" + cusId + "' and end_flag='1' and opr_type in('1','3')";
			IndexedCollection iColl = dao.queryList("CusSubmitInfo", condition, connection);
			if(iColl.size() > 0){
				KeyedCollection kCollSub = (KeyedCollection) iColl.get(0);
				kCollSub.setDataValue("end_flag", "0");  //设置为完成
				kCollSub.setDataValue("over_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
				dao.update(kCollSub, connection); 
			}else{
				flag = "fail";
				context.addDataField("flag", flag);
				context.addDataField("message", "补录失败！");
				return "0";
			}*/
			flag = "succ";
			context.addDataField("flag", flag);
			context.addDataField("message", "补录完成！");
			
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
	
	public String valueOf(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	
	//证件类型转换
	public String getCertType(String oldCertType){
		Map<String, String> tCertType=new HashMap<String, String>();
		tCertType.put("0", "101");
		tCertType.put("6", "116");
		tCertType.put("7", "102");
		tCertType.put("8", "117");
		tCertType.put("9", "121");
		tCertType.put("a", "231");
		tCertType.put("b", "211");
		tCertType.put("1", "104");
		tCertType.put("2", "103");
		tCertType.put("3", "107");
		tCertType.put("4", "113");
		tCertType.put("c", "120");
		tCertType.put("X", "232");
		tCertType.put("d", "122");
		tCertType.put("e", "123");
		tCertType.put("f", "124");
		tCertType.put("5", "125");
		tCertType.put("99", "120");
		tCertType.put("20", "231");
		return tCertType.get(oldCertType);
	}
	
	//客户类型转换
	public String getCusType(String oldCusType){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("Z1", "01");
		tMap.put("Z2", "01");
		tMap.put("Z3", "01");
		return tMap.get(oldCusType);
	}
	
	//是和否
	public String getYN(String oldYN){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("1", "Y");
		tMap.put("2", "N");
		return tMap.get(oldYN);
	}
	
	//客户状态
	public String getCusStatus(String oldCusStatus){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("00", "3");
		tMap.put("20", "2");
		tMap.put("90", "5");
		tMap.put("04", "1");
		tMap.put("90", "5");
		return tMap.get(oldCusStatus);
	}
	
	//性别
	public String getSex(String oldSex){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("01", "M");
		tMap.put("02", "F");
		tMap.put("03", "N");
		return tMap.get(oldSex);
	}
	
	//职业类型
	public String getOcc(String oldOcc){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("0", "1");
		tMap.put("1", "2");
		tMap.put("3", "3");
		tMap.put("4", "4");
		tMap.put("5", "5");
		tMap.put("6", "6");
		tMap.put("X", "8");
		tMap.put("Y", "9");
		tMap.put("Z", "10");
		return tMap.get(oldOcc);
	}
	
	//最高学历
	public String getEdu(String oldEdu){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("10", "20");
		tMap.put("20", "13");
		tMap.put("30", "12");
		tMap.put("40", "11");
		tMap.put("50", "11");
		tMap.put("60", "11");
		tMap.put("70", "10");
		tMap.put("80", "10");
		tMap.put("90", "22");
		tMap.put("99", "22");
		return tMap.get(oldEdu);
	}
	
	//最高学位
	public String getDegree(String oldDegree){
		Map<String, String> tMap=new HashMap<String, String>();
		tMap.put("0", "Q");
		tMap.put("1", "B");
		tMap.put("2", "B");
		tMap.put("3", "S");
		tMap.put("4", "X");
		tMap.put("9", "Q");
		return tMap.get(oldDegree);
	}
}
