package com.yucheng.cmis.biz01line.fncinterface;


import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.master.component.Fnc4QueryComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.Fnc4Query;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.FNCPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class Fnc4RscInstance extends CMISComponent implements Fnc4RscInterface {

	/**
	 * 根据提供的信息获取某科目的值
	 * @param cusId 	客户ID
	 * @param itemId 	科目ID
	 * @param vDate  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd 
	 *                  如果dd=01 为期初或上月本年累计，如果dd=30为期末或本年累计
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException 组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType) throws ComponentException {
		
	/*	double rv=0;
		
		Fnc4Query fq=new Fnc4Query();
		fq.setCusId(cusId);
		fq.setFncType(fncType);
		fq.setItemId(itemId);
		fq.setTermType(termType);
		fq.setVDate(vDate);
		
		Fnc4QueryComponent fqc=(Fnc4QueryComponent)this.getComponent(FNCPubConstant.FNC4QC);
		
		//Fnc4QueryComponent fqc=new Fnc4QueryComponent();
		if(fqc!=null){
			rv=fqc.getItemValue(fq);
		}*/
		String statStyle="1";
		String Com_Grp_Mode = this.getComGrpModeFromCusCom(cusId);
		if("1".equals(Com_Grp_Mode)){//集团客户
			statStyle="2";//合并报表
		}else{							//非集团客户
			statStyle="1";//本部报表
		}
		return this.getItemValue(cusId, itemId, vDate, fncType, termType, statStyle);
	}

	/**
	 * 根据提供的信息获取某科目的值
	 * @param cusId 	客户ID
	 * @param itemId 	科目ID
	 * @param vDate  	数据归属日期
	 *                  格式为
	 *                  yyyyMMdd 
	 *                  如果dd=01 为期初或上月本年累计，如果dd=30为期末或本年累计
	 * @param fncType	财报类型
	 * 					01:资产负债表
	 *					02:损益表
	 *					03:现金流量
	 *					04:财务指标
	 *					05.所有者权益变动表
	 *					06财务简表
	 * @param termType  报表周期类型
	 *                  1:月报
	 *					2:季报
	 *					3:半年报
	 *					4:年报
	 *@param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException 组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType,String statStyle) throws ComponentException {
		
		double rv=0;
		
		Fnc4Query fq=new Fnc4Query();
		fq.setCusId(cusId);
		fq.setFncType(fncType);
		fq.setItemId(itemId);
		fq.setTermType(termType);
		fq.setVDate(vDate);
		fq.setStatStyle(statStyle);
		
		Fnc4QueryComponent fqc=(Fnc4QueryComponent)this.getComponent(FNCPubConstant.FNC4QC);
		
		//Fnc4QueryComponent fqc=new Fnc4QueryComponent();
		if(fqc!=null){
			rv=fqc.getItemValue(fq);
		}
		
		return rv;
	}
	/**
	 * 根据客户编号到对公客户基表看该客户是集团客户，还是非集团客户（1，2是集团客户；3是非集团客户）
	 * @param cus_id
	 * @return
	 */
	public String getComGrpModeFromCusCom(String cus_id)throws ComponentException{
		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this
		.getComponent(FNCPubConstant.FNC4QC);
		String comGrpMode = "";
		try {
			comGrpMode = fqc.getComGrpModeFromCusCom(cus_id);
			if(comGrpMode==null){
				comGrpMode = "";
			}
		} catch (EMPException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
		}
		return comGrpMode;
	}
	
	/*public static void main(String[] args) {
		 Fnc4RscInstance f4r=new  Fnc4RscInstance();
		 
		 try {
		 Fnc4RscInterface f4r=(Fnc4RscInterface)this.getComponentInterface(FNCPubConstant.FNC4RSCINTF);
		 double a=f4r.getItemValue("8152125798", "L01000000", "20151231",
						FNCPubConstant.IS, "1");
			System.out.println("***a="+a);
		} catch (ComponentException e) {

			e.printStackTrace();
		}
	}*/
	
	/**
	 * 获取到客户的最近年的资产负债率数值
	 */
	public FncIndexRpt getIndexValue(String cusId,String itemId)throws ComponentException{
		FncIndexRpt fncIndexRpt = null;
		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this
		.getComponent(FNCPubConstant.FNC4QC);
		
		String statStyle="1";
		String Com_Grp_Mode = this.getComGrpModeFromCusCom(cusId);
		if("1".equals(Com_Grp_Mode)){//集团客户
			statStyle="2";//合并报表
		}else{							//非集团客户
			statStyle="1";//本部报表
		}
		fncIndexRpt = fqc.getIndexValue(cusId,itemId,statStyle);
		return fncIndexRpt;
	}
}
