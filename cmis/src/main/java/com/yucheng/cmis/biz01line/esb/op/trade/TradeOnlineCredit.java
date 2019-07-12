package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.dc.util.StringUtils;
import com.easycon.common.date.DateUtil;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.TimeUtil;

//网贷授信申请接口
public class TradeOnlineCredit extends ESBTranService{
	private static final Logger logger = Logger.getLogger(TradeOnlineCredit.class);

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection conn) throws Exception {
		
		logger.info("------------------------ 进入网贷授信申请接口 start -----------------------");
		logger.info("------------------------------------输入报文："+kColl);
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		KeyedCollection appHeadBody = (KeyedCollection)kColl.get("APP_HEAD");
		KeyedCollection sysHeadBody = (KeyedCollection)kColl.get("SYS_HEAD");
		try{
			String txnDt = (String) sysHeadBody.getDataValue("TxnDt");//交易日期
			String cusName = (String)reqBody.getDataValue("CstNm");//客户名称
			String cusType = (String)reqBody.getDataValue("CstTp");//客户类型
			String certType = (String)reqBody.getDataValue("IdentTp");//证件类型
			String certCode = (String)reqBody.getDataValue("IdentNo");//证件号码
			String manageOrgId = (String)reqBody.getDataValue("OpnCrdInstNo");//开卡机构号
			String brCd = (String)appHeadBody.getDataValue("BrCd");//机构
			String tlrNo = (String)appHeadBody.getDataValue("TlrNo");//柜员号
			String pstDsc = (String)reqBody.getDataValue("PstDsc");//职务
			String highEdctTp = (String)reqBody.getDataValue("HighEdctTp");//文化程度
			String mblNo = (String)reqBody.getDataValue("MblNo");//手机号码
			String rsdnAdr = (String)reqBody.getDataValue("RsdnAdr");//居住地址
			String identIssuDt = (String)reqBody.getDataValue("IdentIssuDt");//证件签发日
			String cstIdentMatDt = (String)reqBody.getDataValue("CstIdentMatDt");//证件到期日
			Double crdAmt=Double.valueOf(reqBody.getDataValue("AplyLoanAmt").toString().trim());//授信额度
			String termType=(String)reqBody.getDataValue("CrdtStdTrmTp");//期限类型
			
			if(StringUtils.isEmptyString(certType)||StringUtils.isEmptyString(certCode)){
				retKColl.put("RetCd", "999999");
				retKColl.put("RetInf", "【网贷授信申请】交易处理失败，错误信息：证件类型或证件号码为空！");
			}
			new Thread(new AsyExecutor(kColl)).start();
			retKColl.put("RetCd", "000000");
		}catch(Exception e){
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "网贷报文缺少必输字段！");
			return retKColl;
		}
		return retKColl;
	}
	
	public KeyedCollection bizLogic(KeyedCollection kColl) throws Exception {
		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		Context context = factory.getContextNamed(factory.getRootContextName());
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = dataSource.getConnection();
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		logger.info("------------------------ 进入网贷授信申请接口 start -----------------------");
		logger.info("------------------------------------输入报文："+kColl);
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		KeyedCollection appHeadBody = (KeyedCollection)kColl.get("APP_HEAD");
		KeyedCollection sysHeadBody = (KeyedCollection)kColl.get("SYS_HEAD");
		try{
			String cusId = "";
			String cstNo = "";//核心客户号
			String openDate = "";//开户日期
			String sex = "";//性别
			String cusName = (String)reqBody.getDataValue("CstNm");//客户名称
			String certType = (String)reqBody.getDataValue("IdentTp");//证件类型
			if("1".equals(certType)){
				certType = "0";//证件类型  0-身份证
			}
			String certCode = (String)reqBody.getDataValue("IdentNo");//证件号码
			String agrcFlg = (String)reqBody.getDataValue("AgrcFlg");//是否农户
			String brCd = (String)appHeadBody.getDataValue("BrCd");//机构
			String tlrNo = (String)appHeadBody.getDataValue("TlrNo");//柜员号
			String pstDsc = (String)reqBody.getDataValue("PstDsc");//职务
			String highEdctTp = (String)reqBody.getDataValue("HighEdctTp");//文化程度
			String rsdnAdrZoneCd = (String)reqBody.getDataValue("RsdnAdrZoneCd");//居住地址区域编码
			String mblNo = (String)reqBody.getDataValue("MblNo");//手机号码
			String wrkCoNm = (String)reqBody.getDataValue("WrkCoNm");//单位名称
			String coChrctrstcDsc = (String)reqBody.getDataValue("CoChrctrstcDsc");//单位性质
			String idyCtgryTp = (String)reqBody.getDataValue("IdyCtgryTp");//工作行业
			String rsdnAdr = (String)reqBody.getDataValue("RsdnAdr");//居住地址
			String identIssuDt = (String)reqBody.getDataValue("IdentIssuDt");//证件签发日
			String cstIdentMatDt = (String)reqBody.getDataValue("CstIdentMatDt");//证件到期日
			String txnDt = (String) sysHeadBody.getDataValue("TxnDt");//交易日期
			String ocpTp = (String)reqBody.getDataValue("OcpTp");//职业类型
			String anulIncmScopTp = (String)reqBody.getDataValue("AnulIncmScopTp");//年收入情况
			txnDt = txnDt.substring(0, 4) + "-" + txnDt.substring(4, 6) + "-" + txnDt.substring(6, 8);
			
			if(StringUtils.isEmptyString(certType)||StringUtils.isEmptyString(certCode)){
				retKColl.put("RetCd", "999999");
				retKColl.put("RetInf", "【网贷授信申请】交易处理失败，错误信息：证件类型或证件号码为空！");
			}else{
				Map<String,Object> userParam = new HashMap<String,Object>();
				userParam.put("cert_type", certType);
				userParam.put("cert_code", certCode);
				logger.info("------------------------校验客户是否存在,客户号不存在则新增该客户信息-----------------------");
				Map<String,Object> count = (Map<String, Object>) SqlClient.queryFirst("queryCusBase", userParam, null, conn);
				if(count==null){
					cusId = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", conn, context);
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
					
					logger.info("***********************返回报文："+retKColl);
					
					KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
					IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
					KeyedCollection retObj=(KeyedCollection)retArr.get(0);
					String retCd=(String)retObj.getDataValue("RetCd");
					logger.info("------------------返回核心客户号："+ cstNo +"----------------");
					if("000000".equals(retCd)){
						if(retKColl.containsKey("BODY")){
							KeyedCollection reBodyKcoll = (KeyedCollection) retKColl.get("BODY");
						    if(reBodyKcoll.containsKey("CstNo")){
						    	cstNo = (String)reBodyKcoll.getDataValue("CstNo");
						    	openDate = (String)reBodyKcoll.getDataValue("OpnAcctDt");
						    	if(openDate!=null&&!"".equals(openDate)){
						    		openDate = openDate.substring(0, 4) + "-" + openDate.substring(4, 6) + "-" + openDate.substring(6, 8);
						    	}
						    	sex = (String)reBodyKcoll.getDataValue("GndTp");
						    	if("M".equals(sex)){
						    		sex = "01";
						    	}else if("F".equals(sex)){
						    		sex = "02";
						    	}else{
						    		sex = "03";
						    	}
						    }else{
						    	throw new Exception("核心客户查询失败！");
						    }
						 }
					}else{
						throw new Exception("核心客户查询失败！");
					}
					
					logger.info("------------------插入CUS_BASE信息表  start----------------");
				    //插入cus_base信息表
				    //Map<String,Object> cusBaseMap = new HashMap<String,Object>();
					KeyedCollection cusBaseMap = new KeyedCollection();
				    cusBaseMap.put("cus_id",cusId);
				    cusBaseMap.put("cus_name",cusName);
				    cusBaseMap.put("cus_short_name",cusName);
				    cusBaseMap.put("cus_type","Z1");//客户类型：Z1指一般自然人
				    cusBaseMap.put("cert_type",certType);
				    cusBaseMap.put("cert_code",certCode);
				    cusBaseMap.put("open_date",openDate);
				    cusBaseMap.put("main_br_id",brCd);
				    cusBaseMap.put("cust_mgr",tlrNo);
				    cusBaseMap.put("input_id",brCd);
				    cusBaseMap.put("input_br_id",tlrNo);
				    cusBaseMap.put("last_update_date",TimeUtil.getCurDate());
				    cusBaseMap.put("cus_country","CHN");//国别默认为“CHN”
				    cusBaseMap.put("loan_card_flg","2");//是否有中证码，默认无
				    cusBaseMap.put("cus_crd_grade","00");//信用等级
				    cusBaseMap.put("cus_status", "20");//客户状态,20为正式客户
				    cusBaseMap.put("belg_line", "BL300");//所属条线
				    cusBaseMap.put("input_date", openDate);//登记日期
				    cusBaseMap.put("hx_cus_id", cstNo);//核心客户号
				    cusBaseMap.setName("CusBase");
					dao.insert(cusBaseMap, conn);
				    //SqlClient.insert("insertCusBase", null, cusBaseMap, conn);
					logger.info("------------------插入CUS_BASE信息表  end----------------");

					logger.info("------------------插入CUS_INDIV信息表  start----------------");
				    //插入cus_indiv信息表
					KeyedCollection cusIndivMap = new KeyedCollection();
				    String birthDay = certCode.substring(6, 10)+'-'+certCode.substring(10, 12)+'-'+certCode.substring(12, 14);//拼接日期
				    cusIndivMap.put("cus_id", cusId);//客户号
				    cusIndivMap.put("indiv_sex", sex);//性別
				    cusIndivMap.put("indiv_id_exp_dt", cstIdentMatDt);//证件到期日
				    cusIndivMap.put("indiv_id_start_dt", identIssuDt);//证件签发日
				    cusIndivMap.put("agri_flg", agrcFlg);//是否农户
				    cusIndivMap.put("indiv_ntn", "");//民族
				    cusIndivMap.put("indiv_brt_place", certCode.substring(0, 6));//籍贯
				    cusIndivMap.put("indiv_houh_reg_add", certCode.substring(0, 6));//户籍地址
				    cusIndivMap.put("indiv_dt_of_birth", birthDay);//出生年月日
				    cusIndivMap.put("indiv_pol_st", "");//政治面貌
				    cusIndivMap.put("indiv_edt", highEdctTp);//文化程度
				    String indivDgr = "";
				    if("10".equals(highEdctTp)){
				    	indivDgr = "3";
				    }else if("20".equals(highEdctTp)||"30".equals(highEdctTp)){
				    	indivDgr = "4";
				    }else{
				    	indivDgr = "0";
				    }
				    cusIndivMap.put("indiv_dgr", indivDgr);//最高学位
				    cusIndivMap.put("post_addr", rsdnAdr);//通讯地址
				    cusIndivMap.put("post_code", rsdnAdrZoneCd);//邮政编码
				    cusIndivMap.put("area_code", rsdnAdrZoneCd);//区域编码
				    cusIndivMap.put("phone", mblNo);//第二联系方式（手机）
				    cusIndivMap.put("mobile", mblNo);//手机
				    cusIndivMap.put("indiv_rsd_addr", rsdnAdr);//居住地址
				    cusIndivMap.put("indiv_zip_code", rsdnAdrZoneCd);//居住地址编码
				    cusIndivMap.put("indiv_occ",ocpTp);//从事职业
				    cusIndivMap.put("indiv_rsd_st", "1");//居住状况
				    cusIndivMap.put("indiv_com_name",wrkCoNm);//工作单位
				    cusIndivMap.put("indiv_com_typ",coChrctrstcDsc);//单位性质
				    cusIndivMap.put("indiv_com_fld",idyCtgryTp);//单位所属行业
				    cusIndivMap.put("indiv_com_addr","");//单位地址
				    cusIndivMap.put("indiv_work_job_y","");//单位工作起始年
				    cusIndivMap.put("indiv_com_job_ttl",pstDsc);//职务
				    String indivCrtfctn = "";
				    if("1".equals(pstDsc)){
				    	indivCrtfctn = "1";
				    }else if("2".equals(pstDsc)){
				    	indivCrtfctn = "2";
				    }else if("3".equals(pstDsc)){
				    	indivCrtfctn = "3";
				    }else{
				    	indivCrtfctn = "0";
				    }
				    cusIndivMap.put("indiv_crtfctn",indivCrtfctn);//职称
				    cusIndivMap.put("indiv_ann_incm","");//年收入情况
				    cusIndivMap.put("cus_bank_rel","B1");//关联关系
				    cusIndivMap.put("com_init_loan_date",openDate);//信贷建立日期
				    cusIndivMap.put("hold_card","1");//持卡情况
				    cusIndivMap.put("is_rela_cust","2");//是否为我行关联客户
				    cusIndivMap.put("street3",rsdnAdr);//街道
				    cusIndivMap.put("street_unit",rsdnAdr);//街道单元
				    cusIndivMap.put("is_long_indiv","1");//是否为长期证件
				    cusIndivMap.put("hx_cus_id",cstNo);//核心客户码
				    cusIndivMap.setName("CusIndiv");
				    dao.insert(cusIndivMap, conn);
				    //SqlClient.insert("insertCusIndiv", null, cusIndivMap, conn);
					logger.info("------------------插入CUS_INDIV信息表  end----------------");
				}else{
					cusId = count.get("cusId").toString();
				}
				logger.info("------------------------ 校验客户是否存在,客户号不存在，调用核心客户查询接口  end -----------------------");
				
				Map<String,Object> lmtParam = new HashMap<String,Object>();
				
				String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", conn, context);
				String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", conn, context);//生成业务流水号
				String appNo = (String)reqBody.getDataValue("CrdtCtrNo").toString().trim();
				String limitCode = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", conn, context);
				String openday  = (String)SqlClient.queryFirst("querySysInfo", null, null, conn);
				Double crdAmt=Double.valueOf(reqBody.getDataValue("AplyLoanAmt").toString().trim());//授信额度
				String termType=(String)reqBody.getDataValue("CrdtStdTrmTp");//期限类型
				
				String endDate="";//结束日期
				String bussType = reqBody.getDataValue("PdTp").toString().trim();//业务类型 （001 消费类 002 经营类 对应100086、100087产品编号）
				String bussCode = reqBody.getDataValue("PdCd").toString().trim();//产品代码

				String term = String.valueOf((Integer.parseInt(termType.substring(0, 2))*12 + Integer.parseInt(termType.substring(3, 5))));
				lmtParam.put("app_no", appNo);
				lmtParam.put("cus_id", cusId);
				
				//查询授信协议
				Map<String,Object> lmtAgrIndivKcoll = (Map<String, Object>) SqlClient.queryFirst("queryLmtAgrIndivByCusId", lmtParam, null, conn);
				
				if(lmtAgrIndivKcoll!=null){
					agrNo = lmtAgrIndivKcoll.get("agrNo").toString();
					//查询台账总额、最大授信台账日期
					lmtParam.put("agr_no", agrNo);
					logger.info("------------------------  更新授信协议  start  -----------------------");
					KeyedCollection kColl4SXXY=new KeyedCollection();
					kColl4SXXY.put("agr_no", agrNo);
					Map<String,Object> lmtAgrDetailsKcoll = (Map<String, Object>) SqlClient.queryFirst("queryLmtAgrDetailsByAgrNo", lmtParam, null, conn);
					if(lmtAgrDetailsKcoll!=null){
						String crdTotlAmt = lmtAgrDetailsKcoll.get("crdTotlAmt")==null?"0":lmtAgrDetailsKcoll.get("crdTotlAmt").toString();
						String oldEndDate = lmtAgrDetailsKcoll.get("endDate").toString();
						Double totalAmt = Double.parseDouble(crdTotlAmt)+crdAmt;
						if(endDate.compareTo(oldEndDate)>=0){
							kColl4SXXY.put("totl_end_date", endDate);
						}else{
							kColl4SXXY.put("totl_end_date", oldEndDate);
						}
						kColl4SXXY.put("crd_totl_amt", totalAmt);
					}else{
						kColl4SXXY.put("crd_totl_amt", crdAmt);
					}
					kColl4SXXY.setName("LmtAgrIndiv");
					dao.update(kColl4SXXY, conn);
					logger.info("------------------------  更新授信协议  end  -----------------------");
					logger.info("------------------------  生成授信台账  start  -----------------------");
					
					KeyedCollection kColl4LAD=new KeyedCollection();
					kColl4LAD.put("agr_no", agrNo);
					kColl4LAD.put("cus_id", cusId);
					kColl4LAD.put("cur_type", "CNY");
					kColl4LAD.put("crd_amt", crdAmt);
					kColl4LAD.put("guar_type","400");//担保方式
					kColl4LAD.put("term_type", "002");//期限类型，默认月
					kColl4LAD.put("term", term);
					kColl4LAD.put("start_date", txnDt);
					//计算结束日期
					
					endDate = DateUtils.getSTDTermEndDate(txnDt,termType);
					
					kColl4LAD.put("end_date", endDate);
					kColl4LAD.put("sub_type", "01");//授信分项默认为一般授信
					kColl4LAD.put("limit_type", "01");//循环额度
					kColl4LAD.put("limit_code", limitCode);
					kColl4LAD.put("app_no", appNo);
					
					/*if(!"".equals(bussType) && "1001100002".equals(bussType)){
						kColl4LAD.put("limit_name", "100091");//消费贷款
					}else if(!"".equals(bussType) && "1001000001".equals(bussType)){
						kColl4LAD.put("limit_name", "100092");//微贷经营贷款
					}*/
					if(!"".equals(bussCode) && "1001100002".equals(bussCode)){
						kColl4LAD.put("limit_name", "100091");//产品名称
						kColl4LAD.put("prd_id", "100091");//产品代码
					}else if(!"".equals(bussCode) && "1001000001".equals(bussCode)){
						kColl4LAD.put("limit_name", "100092");//产品名称
						kColl4LAD.put("prd_id", "100092");//产品代码
					}
					
					kColl4LAD.put("enable_amt", crdAmt);
					kColl4LAD.put("is_pre_crd", "2");//非预授信
					kColl4LAD.put("lmt_status", "10");//额度状态默认正常
					//担保方式为准全额质押/低风险质押时，风险标志为低风险
					kColl4LAD.put("lrisk_type", "20");
					kColl4LAD.put("green_indus", "2");//非绿色产业类型
					kColl4LAD.setName("LmtAgrDetails");
					dao.insert(kColl4LAD, conn);
					logger.info("------------------------  生成授信台账  end  -----------------------");
					
				}else{
					logger.info("------------------------  生成授信台账  start  -----------------------");
					
					KeyedCollection kColl4LAD=new KeyedCollection();
					kColl4LAD.put("agr_no", agrNo);
					kColl4LAD.put("cus_id", cusId);
					kColl4LAD.put("cur_type", "CNY");
					kColl4LAD.put("crd_amt", crdAmt);
					kColl4LAD.put("guar_type","400");//担保方式
					kColl4LAD.put("term_type", "002");//期限类型，默认月
					kColl4LAD.put("term", term);
					kColl4LAD.put("start_date", txnDt);
					//计算结束日期
					
					endDate = DateUtils.getSTDTermEndDate(txnDt,termType);
					
					kColl4LAD.put("end_date", endDate);
					kColl4LAD.put("sub_type", "01");//授信分项默认为一般授信
					kColl4LAD.put("limit_type", "01");//循环额度
					kColl4LAD.put("limit_code", limitCode);
					kColl4LAD.put("app_no", appNo);
					
					/*if(!"".equals(bussType) && "1001100002".equals(bussType)){
						kColl4LAD.put("limit_name", "100091");//消费贷款
					}else if(!"".equals(bussType) && "1001000001".equals(bussType)){
						kColl4LAD.put("limit_name", "100092");//微贷经营贷款
					}*/
					if(!"".equals(bussCode) && "1001100002".equals(bussCode)){
						kColl4LAD.put("limit_name", "100091");//产品名称
						kColl4LAD.put("prd_id", "100091");//产品代码
					}else if(!"".equals(bussCode) && "1001000001".equals(bussCode)){
						kColl4LAD.put("limit_name", "100092");//产品名称
						kColl4LAD.put("prd_id", "100092");//产品代码
					}
					
					kColl4LAD.put("enable_amt", crdAmt);
					kColl4LAD.put("is_pre_crd", "2");//非预授信
					kColl4LAD.put("lmt_status", "10");//额度状态默认正常
					//担保方式为准全额质押/低风险质押时，风险标志为低风险
					kColl4LAD.put("lrisk_type", "20");
					kColl4LAD.put("green_indus", "2");//非绿色产业类型
					kColl4LAD.setName("LmtAgrDetails");
					dao.insert(kColl4LAD, conn);
					logger.info("------------------------  生成授信台账  end  -----------------------");
	
					logger.info("------------------------  生成授信协议  start  -----------------------");
					KeyedCollection kColl4SXXY=new KeyedCollection();
					kColl4SXXY.put("serno", serno);
					kColl4SXXY.put("agr_no", agrNo);
					kColl4SXXY.put("cus_id", cusId);
					kColl4SXXY.put("biz_type", "01");
					kColl4SXXY.put("cur_type", "CNY");
					kColl4SXXY.put("crd_totl_amt", crdAmt);
					kColl4SXXY.put("totl_end_date", endDate);
					kColl4SXXY.put("totl_start_date", txnDt);
					kColl4SXXY.put("guar_type","400");//担保方式
					kColl4SXXY.put("is_self_revolv","2");
					kColl4SXXY.put("util_mode","02");
					kColl4SXXY.put("manager_id",tlrNo);
					kColl4SXXY.put("manager_br_id",brCd);
					kColl4SXXY.put("input_id",tlrNo);
					kColl4SXXY.put("input_br_id",brCd);
					kColl4SXXY.put("input_date",openday);
					kColl4SXXY.put("self_start_date", txnDt);
					kColl4SXXY.put("lrisk_type", "20");
					kColl4SXXY.put("agr_status", "002");
					kColl4SXXY.put("lrisk_totl_amt", "0");
					kColl4SXXY.put("app_no", appNo);
					kColl4SXXY.setName("LmtAgrIndiv");
	
					dao.insert(kColl4SXXY, conn);
					logger.info("------------------------  生成授信协议  end  -----------------------");
				}
				retKColl.put("RetCd", "000000");
				retKColl.put("RetInf", "【网贷授信申请】业务处理成功");
			}
			logger.info("------------------------ 进入网贷授信申请接口 end -----------------------");
		}catch(Exception e){
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "【网贷授信申请接口】交易处理失败，错误信息："+e.getMessage());
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.close();
			}
		}
		return retKColl;
	}
	
	class AsyExecutor implements Runnable {
		private KeyedCollection kColl;
		private Connection conn;
		public AsyExecutor (KeyedCollection kColl) {
			this.kColl=kColl;
			this.conn=conn;
		}
		@Override
		public void run() {
			try {
				bizLogic(kColl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
