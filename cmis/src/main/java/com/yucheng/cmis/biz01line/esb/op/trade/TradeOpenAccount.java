package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.dc.util.StringUtils;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ESBUtil;
//核心客户查询，开户接口
public class TradeOpenAccount extends ESBTranService {

	private static final Logger logger = Logger.getLogger(TradeOpenAccount.class);

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection conn) throws Exception {
		logger.info("------------------进入核心客户查询接口  start----------------");
		//返回报文
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection sysHeadBody = (KeyedCollection)kColl.get("SYS_HEAD");
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		KeyedCollection appHeadBody = (KeyedCollection)kColl.get("APP_HEAD");
		String txnDt = (String) sysHeadBody.getDataValue("TxnDt");//交易日期
		String brCd = (String) appHeadBody.getDataValue("BrCd");//机构
		String tlrNo = (String)appHeadBody.getDataValue("TlrNo");//柜员号

		try {
			String certType = (String)reqBody.getDataValue("IdentTp");//证件类型
			String certCode = (String)reqBody.getDataValue("IdentNo");//证件号码
			String sex = "";//性别
			if("18".equals(String.valueOf(certCode.length()))){
				int a = Integer.parseInt(certCode.substring(16, 17));
				if(a%2==0){
					sex = "F";
				}else{
					sex = "M";
				}
			}else{
				throw new Exception("身份证无效！");
			}
			String cusName = (String)reqBody.getDataValue("CstNm");//客户名称
			String rsdnAdr = (String)reqBody.getDataValue("RsdnAdr");//居住地址
			String ocpTp = (String)reqBody.getDataValue("OcpTp");//职业
			String pstDsc = (String)reqBody.getDataValue("PstDsc");//职务
			String agrcFlg = (String)reqBody.getDataValue("AgrcFlg");//是否农户
			String highEdctTp = (String)reqBody.getDataValue("HighEdctTp");//文化程度
			String rsdnAdrZoneCd = (String)reqBody.getDataValue("RsdnAdrZoneCd");//居住地址区域编码
			String mblNo = (String)reqBody.getDataValue("MblNo");//手机号码
			String wrkCoNm = (String)reqBody.getDataValue("WrkCoNm");//单位名称
			String coChrctrstcDsc = (String)reqBody.getDataValue("CoChrctrstcDsc");//单位性质
			String idyCtgryTp = (String)reqBody.getDataValue("IdyCtgryTp");//工作行业
			//String identIssuDt = (String)reqBody.getDataValue("IdentIssuDt");//证件签发日期
			//String cstIdentMatDt = (String)reqBody.getDataValue("CstIdentMatDt");//客户证件到期日期
			String identIssuDt = "2019-03-06";
			String cstIdentMatDt = "2019-03-21";
			//新增前先去查询核心是否有该客户
			if(certType==null || "".equals(certType) || certCode==null || "".equals(certCode)){
				throw new Exception("证件类型或证件号码不允许为空！");
			}
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			
			logger.info("------------------组装核心客户查询接口请求报文  start----------------");
			//组装报文头
			qryheadKcoll.addDataField("SvcCd","20130001");
			qryheadKcoll.addDataField("ScnCd","01");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			//报文体
			qrybodyColl.addDataField("QryInd","2");//2-按证件查询
			qrybodyColl.addDataField("AcctNoCrdNo",""); 
			qrybodyColl.addDataField("CstNo",""); 
			qrybodyColl.addDataField("IdentTp","101"); 
			qrybodyColl.addDataField("IdentNo",certCode); 
			qrybodyColl.addDataField("CtcTelNo",""); 
			qrybodyColl.addDataField("TaskSrlNo","");
			logger.info("------------------组装核心客户查询接口请求报文  end----------------");
			
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			//String opType = "01";//新增核心客户
			String cstNo = "";
			if(retKColl.containsKey("BODY")){
				KeyedCollection reBodyKcoll = (KeyedCollection) retKColl.get("BODY");
			    if(reBodyKcoll.containsKey("CstNo")){
			    	cstNo = (String)reBodyKcoll.getDataValue("CstNo");
			    }else{
			    	throw new Exception("核心客户查询失败！");
			    }
			    if(StringUtils.isEmptyString(cstNo)){
					logger.info("------------------核心客户不存在，组装核心客户新增报文  start----------------");
			    	KeyedCollection headKcoll = new KeyedCollection();
					KeyedCollection bodyKcoll = new KeyedCollection();
					headKcoll.put("SvcCd", "20120001");
					headKcoll.put("ScnCd", "02");
					headKcoll.addDataField("TxnMd","ONLINE");
					headKcoll.addDataField("UsrLngKnd","CHINESE");
					headKcoll.addDataField("jkType","cbs");  
					
					bodyKcoll.addDataField("OprtnTp","01");//操作类型
					bodyKcoll.addDataField("CstNo",cstNo);//客户号
					bodyKcoll.addDataField("CstGrpgTp","");//客户分组
					bodyKcoll.addDataField("CstTp","01");//客户类型，已转行
					bodyKcoll.addDataField("CstClCtgry","101");//客户分类类别码值对应待处理-----------------
					bodyKcoll.addDataField("CstShrtNm",cusName);//客户简称
					bodyKcoll.addDataField("CstChinNm",cusName);//客户中文名称
					bodyKcoll.addDataField("CstEnNm","");//输入中文名称，英文给空字符串
					bodyKcoll.addDataField("CstNmSufxInf","");//客户名后缀
					bodyKcoll.addDataField("HlpMmryNm","");//助记名称
					bodyKcoll.addDataField("Adr",rsdnAdr);//地址
					bodyKcoll.addDataField("CstNtntyCd","CHN");//客户国籍代码,不用转换
					bodyKcoll.addDataField("RskCntrlCtyCd","CHN");//默认CHN
					bodyKcoll.addDataField("LoProvCd","");//省份码值对应关系，待处理
					bodyKcoll.addDataField("CstCityCd","");//客户城市代码对应关系，待处理
					bodyKcoll.addDataField("NtvPlcInf","");//籍贯信息
					bodyKcoll.addDataField("DmstOvrsFlg","I");//境内境外标志
					bodyKcoll.addDataField("IdyTp","00000");//行业类型，码值对应关系待定---------------------------
					bodyKcoll.addDataField("CstBsnKnd","999");//客户业务种类,码值对应关系待定---------------------------
					bodyKcoll.addDataField("RsrvFldInf","");
					bodyKcoll.addDataField("RsrvFldInf1","");
					bodyKcoll.addDataField("RsrvFldInf2","");
					bodyKcoll.addDataField("RsrvFldInf3","");
					bodyKcoll.addDataField("RsrvFldInf4","");
					bodyKcoll.addDataField("SmyDsc","");//摘要
					bodyKcoll.addDataField("InnrCstFlg","1");//内部客户标识，-------------------暂无
					bodyKcoll.addDataField("CstExgRatePrfrncRto","");//客户汇率优惠比例
					bodyKcoll.addDataField("RskRto","");//风险权重
					bodyKcoll.addDataField("CstSt","0");//客户状态，码值对应关系待处理---------------------------暂无
					bodyKcoll.addDataField("CstStDsc","");//客户状态描述
					bodyKcoll.addDataField("AcctTxnSt","A");//账户交易状态
					bodyKcoll.addDataField("ClsDt","");//关闭日期
					bodyKcoll.addDataField("CstInd","N");//客户标识
					bodyKcoll.addDataField("CnlTp","020114");//渠道类型,信贷系统
					bodyKcoll.addDataField("FnlAmdtTlrNo",tlrNo);//最后修改柜员号
					bodyKcoll.addDataField("WrtLngTp","");//书写语言种类
					bodyKcoll.addDataField("CmnctnLngTp","");//交流语言种类
					bodyKcoll.addDataField("FnlModDt",txnDt);//最后更改日期
					bodyKcoll.addDataField("TempCstFlg","");//临时客户标志
					bodyKcoll.addDataField("CrtTlrNo",tlrNo);//创建柜员号
					bodyKcoll.addDataField("CrtDt",txnDt);//创建日期
					bodyKcoll.addDataField("PrntLngTp","");//打印语言种类
					bodyKcoll.addDataField("DataIncomInd","N");//资料不全标识
					bodyKcoll.addDataField("PEPInd","");//PEP标识
					bodyKcoll.addDataField("CstLvl","01");//客户等级/客户级别
					bodyKcoll.addDataField("EvltLvlDt","");//评级日期
					bodyKcoll.addDataField("RcvTaxFlg","N");//收税标志
					bodyKcoll.addDataField("CstAlsNm","");//客户别名
					bodyKcoll.addDataField("CstMgrCd",tlrNo);//柜员------------暂无
					bodyKcoll.addDataField("PrftCnrlCd","");//利润中心代码
					bodyKcoll.addDataField("CntrlBrBnkCd",brCd);//控制分行代码
					bodyKcoll.addDataField("DstcNm","300002");//地区名称/区域名称--------------暂无
					bodyKcoll.addDataField("Pstcd","300002");//邮政编码--------------暂无
					bodyKcoll.addDataField("CntrlDeptCd","");//控制部门代码
					bodyKcoll.addDataField("BlcklistCstFlg","N");//黑名单客户标志
					bodyKcoll.addDataField("PblcAgntNm","");//代办人姓名
					bodyKcoll.addDataField("PblcAgntIdentTp","");//代理人证件类型
					bodyKcoll.addDataField("PblcAgntIdentNo","");//代理人证件号码
					bodyKcoll.addDataField("PblcAgntIdentExprtnDt","");//代办人证件到期日期
					bodyKcoll.addDataField("SurnmLoFrntFlg","Y");//姓氏在前标志
					bodyKcoll.addDataField("TtlTp","");//称谓类型
					bodyKcoll.addDataField("RsdntSt","c");//居民状态
					bodyKcoll.addDataField("NtnCd","");//民族代码
					bodyKcoll.addDataField("BrthDt","");//出生日期
					bodyKcoll.addDataField("GndTp",sex);//性别-------------------------------暂无
					bodyKcoll.addDataField("MdnSt","S");//婚姻状况,码值对应关系待后
					bodyKcoll.addDataField("MdnNm","");//婚前名称
					bodyKcoll.addDataField("BrthCtyNm","CHN");//出生国家名称
					bodyKcoll.addDataField("MthrMdnNm","");//母亲婚前名称
					bodyKcoll.addDataField("OcpTp",ocpTp);//职业类型
					bodyKcoll.addDataField("RsdnDsc","");//居住状况
					bodyKcoll.addDataField("MjrQualfCd","");//专业职称代码,不用转
					bodyKcoll.addDataField("HighEdctTp","");//最高学历类型
					bodyKcoll.addDataField("SoclInsNo","");//社会保险号
					bodyKcoll.addDataField("SprtPrsnNum","");//供养人数
					bodyKcoll.addDataField("WrkCoNm","");//工作单位名称
					bodyKcoll.addDataField("WrkCoBlngIdyCd","");//工作单位所属行业代码
					bodyKcoll.addDataField("EmplymntBegTm","");//雇佣开始时间
					bodyKcoll.addDataField("PrsDsc","");//兴趣爱好
					bodyKcoll.addDataField("PstDsc",pstDsc);//职务,不用转
					bodyKcoll.addDataField("HighDgrTp","");//最高学位类型
					bodyKcoll.addDataField("SlryCcy","CNY");//薪资币种
					bodyKcoll.addDataField("PerMoSlryAmt","");//每月薪水
					bodyKcoll.addDataField("SlryAcctNo","");//工资账号
					bodyKcoll.addDataField("SlryAcctOpnAcctBnkNo","");//工资账户开户行行号
					bodyKcoll.addDataField("AnulIncmAmt","");//年收入金额
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
					identInfKColl.addDataField("IdentTp","101");//证件类型
					identInfKColl.addDataField("IdentNo", certCode);//证件号码
					identInfKColl.addDataField("IssuCtyCd", "CHN");//签发国家代码
					identInfKColl.addDataField("IssuCityCd", "");//签发城市代码
					identInfKColl.addDataField("IssuDt", identIssuDt.replaceAll("-", ""));//签发日期-------------------------------暂无
					identInfKColl.addDataField("IssuAdr", "");//签发地址
					identInfKColl.addDataField("ExprtnDt", cstIdentMatDt.replaceAll("-", ""));//到期日期-------------------------------暂无
					identInfKColl.addDataField("ChiefFlg", "Y");//首要标志
					identInfKColl.addDataField("AuthInstCd", "");//授权机构代码
					identInfKColl.addDataField("IssuAdrDstcCd", "");//签发地地区代码
					identInfKColl.addDataField("OprtnTp", "01");//操作类型
					identInfKColl.addDataField("IssuProvCd", "");//签发省份代码
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
					cstCtcInfKColl.addDataField("CtcMdTp", "");
					cstCtcInfKColl.addDataField("CtcMdInf", "");
					cstCtcInfKColl.addDataField("PrfrdCtcMdFlg", "");
					cstCtcInfKColl.addDataField("OprtnTp", "01");
					cstCtcInfIColl.add(cstCtcInfKColl);
					bodyKcoll.addDataElement(cstCtcInfIColl);
					
					bodyKcoll.addDataField("ChkRsltFlg", "");//核查结果标志
					bodyKcoll.addDataField("NonChkRsn", "");
					bodyKcoll.addDataField("DisplExplnDsc", "");
					bodyKcoll.addDataField("RtndFlg", "");
					bodyKcoll.addDataField("Rmk", "");
					bodyKcoll.addDataField("LglPrsnCd", "");
					bodyKcoll.addDataField("CstUsedNm", "");
					bodyKcoll.addDataField("SplyRsn", "");
					bodyKcoll.addDataField("AgrcFlg", "");
					bodyKcoll.addDataField("CrtCnlInf", "");
					bodyKcoll.addDataField("CrdtLvl", "");
					bodyKcoll.addDataField("BnkInnrIdyCtgryTp", "01");
					bodyKcoll.addDataField("CtyStdIdyCtgryTp", "01");
					bodyKcoll.addDataField("SoclInsNo1", "");
					bodyKcoll.addDataField("CstStdPrmptInf", "");//客户标准提示

					IndexedCollection cmnctnAdrInfIColl = new IndexedCollection();
					cmnctnAdrInfIColl.setName("CmnctnAdrInfArry");
					KeyedCollection cmnctnAdrInfKColl = new KeyedCollection(); 
					cmnctnAdrInfKColl.addDataField("OprtnTp", "01");
					cmnctnAdrInfKColl.addDataField("CstNo", cstNo);
					cmnctnAdrInfKColl.addDataField("AdrTp", "04");//地址类型
					cmnctnAdrInfKColl.addDataField("CtyNm", "CHN");//国家名称
					cmnctnAdrInfKColl.addDataField("ProvNm", "");//省份名称
					cmnctnAdrInfKColl.addDataField("CityCd", "");//城市代码
					cmnctnAdrInfKColl.addDataField("DstcCd", "");//地区代码
					cmnctnAdrInfKColl.addDataField("Adr", rsdnAdr);//地址
					cmnctnAdrInfKColl.addDataField("Pstcd", "");//邮政编码
					cmnctnAdrInfKColl.addDataField("AdrMd", "");
					cmnctnAdrInfKColl.addDataField("PrfrdIdentFlg", ""); 
					cmnctnAdrInfIColl.add(cmnctnAdrInfKColl);
					bodyKcoll.addDataElement(cmnctnAdrInfIColl);
					
					IndexedCollection qryPswdInfIColl = new IndexedCollection();
					qryPswdInfIColl.setName("QryPswdInfArry");
					KeyedCollection qryPswdInfKColl = new KeyedCollection();  
					qryPswdInfKColl.addDataField("PswdTp", "QY");
					qryPswdInfKColl.addDataField("QryPswdStrg", "000000");
					qryPswdInfKColl.addDataField("PswdTkEffDt", txnDt); //密码生效日期
		 
					qryPswdInfIColl.add(qryPswdInfKColl);
					bodyKcoll.addDataElement(qryPswdInfIColl);
					
					bodyKcoll.addDataField("EvdnDsc", "");
					bodyKcoll.addDataField("SpfNtrPrsnCd", "");
					bodyKcoll.addDataField("CoChrctrstcDsc", "");
					bodyKcoll.addDataField("CstRmk", "");
					bodyKcoll.addDataField("SoclRltnpDsc", "");
					logger.info("------------------核心客户不存在，组装核心客户新增报文  end----------------");
					retKColl = ESBUtil.sendEsbMsg(headKcoll,bodyKcoll);
					KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
					KeyedCollection body=(KeyedCollection)retKColl.getDataElement("BODY");
					IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
					KeyedCollection retObj=(KeyedCollection)retArr.get(0);
					String retCd=(String)retObj.getDataValue("RetCd");
					cstNo = (String) body.getDataValue("CstNo");
					logger.info("------------------返回核心客户号："+ cstNo +"----------------");
					if(!"000000".equals(retCd)){
						throw new EMPException((String)retObj.getDataValue("RetInf"));
					}
			    }
				logger.info("------------------插入CUS_BASE信息表  start----------------");
			    //插入cus_base信息表
			    Map<String,Object> cusBaseMap = new HashMap<String,Object>();
			    cusBaseMap.put("cus_id",cstNo);
			    cusBaseMap.put("cus_name",cusName);
			    cusBaseMap.put("cus_type","Z1");//客户类型：Z1指一般自然人
			    cusBaseMap.put("cert_type",certType);
			    cusBaseMap.put("cert_code",certCode);
			    cusBaseMap.put("cus_country","CHN");//国别默认为“CHN”
			    cusBaseMap.put("cus_status", "20");//客户状态,20为正式客户
			    cusBaseMap.put("belg_line", "20");//客户状态,20为正式客户
			    cusBaseMap.put("input_date", "BL300");//所属条线
			    SqlClient.insert("insertCusBase", null, cusBaseMap, conn);
				logger.info("------------------插入CUS_BASE信息表  end----------------");

				logger.info("------------------插入CUS_INDIV信息表  start----------------");
			    //插入cus_indiv信息表
			    Map<String,Object> cusIndivMap = new HashMap<String,Object>();
			    String birthDay = certCode.substring(6, 10)+'-'+certCode.substring(10, 12)+'-'+certCode.substring(12, 14);//拼接日期
			    cusIndivMap.put("cus_id", cstNo);//客户号
			    cusIndivMap.put("agri_flg", agrcFlg);//是否农户
			    cusIndivMap.put("indiv_dt_of_birth", birthDay);//出生年月日
			    cusIndivMap.put("indiv_edt", highEdctTp);//文化程度
			    cusIndivMap.put("post_addr", rsdnAdr);//通讯地址
			    cusIndivMap.put("post_code", rsdnAdrZoneCd);//邮政编码
			    cusIndivMap.put("phone", mblNo);//第二联系方式（手机）
			    cusIndivMap.put("mobile", mblNo);//手机
			    cusIndivMap.put("indiv_rsd_addr", rsdnAdr);//居住地址
			    cusIndivMap.put("indiv_zip_code", rsdnAdrZoneCd);//居住地址编码
			    cusIndivMap.put("indiv_com_name",wrkCoNm);//工作单位
			    cusIndivMap.put("indiv_com_typ",coChrctrstcDsc);//单位性质
			    cusIndivMap.put("indiv_com_fld",idyCtgryTp);//单位所属行业
			    cusIndivMap.put("indiv_com_job_ttl",pstDsc);//职务
			    cusIndivMap.put("com_init_loan_date",txnDt);//建立信贷关系时间
			    cusIndivMap.put("hx_cus_id",cstNo);//核心客户码
			    SqlClient.insert("insertCusIndiv", null, cusIndivMap, conn);
				logger.info("------------------插入CUS_INDIV信息表  end----------------");
			    retKColl.addDataField("cstNo", cstNo);
			}else{
				throw new Exception("核心客户查询失败！");
			}
		} catch (EMPException ee) {
			throw new EMPException(ee);
		} catch (Exception e) {
			throw new EMPException(e);
		}
		logger.info("------------------进入核心客户查询接口  end----------------");
		return retKColl;
	}
}