package com.yucheng.cmis.biz01line.arp.dao;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class ArpPubDao extends CMISDao {
	/**
	 * 资产保全专用无返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return 
	 * @throws DaoException 
	 */
	public void delExecuteSql(String submitType, KeyedCollection kcoll) throws DaoException {
		try {
			
			/********************************** 1.不良资产begin *****************************************/
			if (submitType.equals("ArpBadassetHandoverApp")){ /***** 不良移交申请流程 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd("ArpBadassetHandoverAppDate", serno, null, null, this.getConnection());
				SqlClient.executeUpd("ArpBadassetHandoverAppRecv", serno, null, null, this.getConnection());
			}
			/********************************** 1.不良资产end *****************************************/
			/********************************** 2.法律措施begin *****************************************/
			else if (submitType.equals("updateLawsuitFee")){ /***** 重新合计诉讼费用 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd("updateLawsuitFee", serno, null, null, this.getConnection());
			}else if (submitType.equals("manaLawsuitFee")){ /***** 重新管理诉讼费用 ******/
				String case_no = kcoll.getDataValue("case_no").toString();
				SqlClient.executeUpd("manaLawsuitFee", case_no, null, null, this.getConnection());
			}else if (submitType.equals("ArpLawLawsuitAppFlow")){ /***** 诉讼申请流程后处理 ******/
				String serno = kcoll.getDataValue("serno").toString();
				String case_no = kcoll.getDataValue("case_no").toString();
				SqlClient.executeUpd("dealAppOverdate", serno, null, null, this.getConnection());	//诉讼申请表生成办结日期
				SqlClient.executeUpd("dealAppToInfo", serno, case_no, null, this.getConnection());	//诉讼申请表过渡信息表
				SqlClient.executeUpd("dealMemberToMana", serno, case_no, null, this.getConnection());	//涉及人员信息表过渡管理表
				SqlClient.executeUpd("dealDetailToMana", serno, case_no, null, this.getConnection());	//诉讼明细信息表过渡管理表
			}
			/********************************** 2.法律措施end *****************************************/			
			/********************************** 3.债权减免管理begin *****************************************/
			else if (submitType.equals("updateBondReduc")){ /***** 重新合计债权减免明细信息 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd(submitType, serno, null, null, this.getConnection());
			}
			/********************************** 3.债权减免管理end *****************************************/			
			/********************************** 4.以物抵债begin *****************************************/
			else if(submitType.equals("ArpCollDebtAppFlow")){/**以物抵债申请流程后处理*/
				String serno = kcoll.getDataValue("serno").toString();//以物抵债业务编号
				String debt_acc_no = kcoll.getDataValue("debt_acc_no").toString();//以物抵债台账编号
				SqlClient.executeUpd("dealCollDebtAppToAcc", serno, debt_acc_no, null, this.getConnection());//生成以物抵债台账
				SqlClient.executeUpd("dealCollDebtReAppToAcc", serno, debt_acc_no, null, this.getConnection());//生成抵债物关联表
			}else if(submitType.equals("updateArpCollDebtAccRe")){/**更新以物抵债物关联表 （台账）*/
				String serno = kcoll.getDataValue("serno").toString();//以物抵债业务编号
				SqlClient.executeUpd("updateArpCollDebtAccRe", serno, null, null, this.getConnection());//生成以物抵债台账
			}else if(submitType.equals("dealCollDispAppToAcc")){/** 抵债资产处置申请结束后生产台账*/
				String serno = kcoll.getDataValue("serno").toString();//以物抵债业务编号
				String asset_disp_no = kcoll.getDataValue("asset_disp_no").toString();//资产处置编号
				String asset_disp_mode = kcoll.getDataValue("asset_disp_mode").toString();//资产处置方式
				SqlClient.executeUpd("dealCollDispAppToAcc", serno, asset_disp_no, null, this.getConnection());//抵债资产处置台账
				if("00".equals(asset_disp_mode)){//出售
					SqlClient.executeUpd("dealSaleToAcc", serno,asset_disp_no, null, this.getConnection());
				}else if("01".equals(asset_disp_mode)){//出租
					SqlClient.executeUpd("dealRentToAcc", serno,asset_disp_no, null, this.getConnection());
				}else if("02".equals(asset_disp_mode)){//转固
					SqlClient.executeUpd("dealPegToAcc", serno,asset_disp_no, null, this.getConnection());
				}else if("03".equals(asset_disp_mode)){//核销
					SqlClient.executeUpd("dealWriteoffToAcc", serno,asset_disp_no, null, this.getConnection());
				}
			}
			/********************************** 4.以物抵债end *****************************************/			
			/********************************** 5.呆账管理begin *****************************************/
			else if (submitType.equals("updateDbtCongniz") || submitType.equals("updateDbtWriteoff")){ 
				/***** 重新合计呆账认定、核销申请信息 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd(submitType, serno, null, null, this.getConnection());
			}else if (submitType.equals("ArpDbtCongnizApp")){ /***** 呆账认定申请流程 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd("insertArpDbtCongnizAcc", serno, null, null, this.getConnection());
				SqlClient.executeUpd("dealArpDbtCongnizAppOverdate", serno, null, null, this.getConnection());
			}else if (submitType.equals("ArpDbtWriteoffApp")){ /***** 呆账核销申请流程 ******/
				String serno = kcoll.getDataValue("serno").toString();
				SqlClient.executeUpd("insertArpDbtWriteoffAcc", serno, null, null, this.getConnection());
				SqlClient.executeUpd("dealArpDbtWriteoffAppOverdate", serno, null, null, this.getConnection());
			}
			/********************************** 5.呆账管理end *****************************************/			
		} catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
	}
	
	/**
	 * 资产保全专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 * @throws DaoException 
	 */
	public KeyedCollection delReturnSql(String submitType, KeyedCollection kcoll) throws DaoException {
		Object results = "";
		try{
			/********************************** 1.不良资产begin *****************************************/
			if (submitType.equals("checkBadasset")){ /***** 不良移交申请校验 ******/
				results = (Object) SqlClient.queryFirst("checkBadasset", kcoll.getDataValue("value"), null, this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("changeDateToTen")){ /***** 日期格式转为10位 ******/
				results = (Object) SqlClient.queryFirst("changeDateToTen", kcoll.getDataValue("value"), null, this.getConnection());
				kcoll.setDataValue("results", results);
			}else if (submitType.equals("delCurType")){ /***** 外币计算 ******/
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("curType", kcoll.getDataValue("curType").toString());
				paramMap.put("transAmt", kcoll.getDataValue("transAmt").toString());
				results = (Object) SqlClient.queryFirst(submitType, paramMap, null, this.getConnection());
				kcoll.addDataField("results", results);
			}
			/********************************** 1.不良资产end *****************************************/			
			/********************************** 2.法律措施begin *****************************************/
			else if (submitType.equals("ArpLawDebtorInfo")||submitType.equals("ArpLawDefendantInfo")
					||submitType.equals("ArpLawDebtorMana")||submitType.equals("ArpLawDefendantMana")){/***** 被告人、债务人信息、管理校验 ******/				
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("serno", kcoll.getDataValue("serno").toString());
				paramMap.put("cus_id", kcoll.getDataValue("value").toString());
				if(submitType.equals("ArpLawDebtorInfo")||submitType.equals("ArpLawDebtorMana")){
					paramMap.put("member_type","002");
				}else{
					paramMap.put("member_type","001");
				}
				String type = "";
				if(submitType.equals("ArpLawDebtorInfo")||submitType.equals("ArpLawDefendantInfo")){
					type = "ArpLawDebtorInfo";
				}else{
					type = "ArpLawDebtorMana";
				}
				results = (Object) SqlClient.queryFirst(type, paramMap, null, this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("ArpLawLawsuitDetail") || submitType.equals("ArpLawLawsuitDtmana")){ /***** 诉讼明细、管理校验******/
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("serno", kcoll.getDataValue("serno").toString());
				paramMap.put("bill_no", kcoll.getDataValue("value").toString());
				results = (Object) SqlClient.queryFirst(submitType, paramMap, null, this.getConnection());
				kcoll.addDataField("results", results);
			}
			/********************************** 2.法律措施end *****************************************/
			/********************************** 3.债权减免管理begin *****************************************/
			else if (submitType.equals("SumBondReducDetail")){ /***** 统计债权减免明细表合计项 ******/
				kcoll = (KeyedCollection)SqlClient.queryFirst("SumBondReducDetail", kcoll.getDataValue("serno"), null, this.getConnection());
			}else if (submitType.equals("CusidBondReduc") || submitType.equals("BondReducDetail")){ /***** 债权减免客户、明细校验 ******/
				results = (Object) SqlClient.queryFirst(submitType, kcoll.getDataValue("value"), null, this.getConnection());
				kcoll.addDataField("results", results);
			}
			/********************************** 3.债权减免管理end *****************************************/			
			/********************************** 4.以物抵债begin *****************************************/
			else if(submitType.equals("getArpBusiSum") || submitType.equals("SumArpCollDebtRe")){ /***** 动态获取业务抵债信息表中的汇总数据 ******/
				results = (Object) SqlClient.queryFirst(submitType, kcoll.getDataValue("serno"), null, this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("CusidArpCollDebtApp") || submitType.equals("BusiDebtDetail")){
				results = (Object) SqlClient.queryFirst(submitType, kcoll.getDataValue("value"), null, this.getConnection());
				kcoll.addDataField("results", results);
			}
			/********************************** 4.以物抵债end *****************************************/
			/********************************** 5.呆账管理begin *****************************************/
			else if (submitType.equals("CusidDbtCongniz") || submitType.equals("DbtCongnizDetail")
					||submitType.equals("CusidDbtWriteoff") || submitType.equals("DbtWriteoffDetail")){ 
				/***** 呆账认定、核销申请客户校验  呆账认定、核销明细校验 ******/
				results = (Object) SqlClient.queryFirst(submitType, kcoll.getDataValue("value"), null, this.getConnection());
				kcoll.addDataField("results", results);
			}else if (submitType.equals("SumDbtWriteoffDetail")){ /***** 呆账核销申请页面取明细和 ******/
				kcoll = (KeyedCollection)SqlClient.queryFirst("SumDbtWriteoffDetail", kcoll.getDataValue("serno"), null, this.getConnection());
			}
			/********************************** 5.呆账管理end *****************************************/				
		}catch (Exception e) {
			throw new DaoException("调用命名sql出错："+e.getMessage());
		}
		return kcoll;
	}
	
}