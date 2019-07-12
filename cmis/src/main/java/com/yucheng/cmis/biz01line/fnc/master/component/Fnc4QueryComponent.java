package com.yucheng.cmis.biz01line.fnc.master.component;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfItemsComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.agent.Fnc4QueryAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.Fnc4Query;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.FNCPubConstant;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname com.yucheng.cmis.fnc.master.component.Fnc4QueryComponent.java
 * @author wqgang
 * @Since 2009-4-9 下午03:37:26 
 * @Copyright yuchengtech
 * @version 1.0
 */
public class Fnc4QueryComponent extends CMISComponent {
/**
 * 
 * @param fq
 * @return
 */
	public double getItemValue(Fnc4Query fq){
		double rv=0;
	Fnc4QueryAgent fqa;
	try {
		fqa = (Fnc4QueryAgent) this.getAgentInstance(FNCPubConstant.FNC4QA);
		rv=fqa.QueryItemValue(fq);
	} catch (AgentException e) {
		e.printStackTrace();
	}
	/*fqa=new Fnc4QueryAgent();	
	rv=fqa.QueryItemValue(fq);
	*/	
	return rv;
	}
	
	public double getItemVlaue4BS(Fnc4Query fq){
		return 0;
	}
	
	/**
	 * 根据提供的信息从缓存中获取某科目的对象
	 * 
	 * @param itemId
	 *            科目ID
	 * @return
	 * @throws EMPException
	 */

	public FncConfItems4Query getFncConfItemsFormCashe(String itemId)
			throws EMPException {
		FncConfItems4Query rv = null;
		FncConfItemsComponent fcic = (FncConfItemsComponent) this
				.getComponent(PUBConstant.FNCCONFITEMS);
		rv = fcic.findFncConfItemsFromCashe(itemId);
		return rv;

	}
	
	
	/**
	 * 根据提供的信息从缓存中获取科目的对象列表
	 * 
	 * @param list
	 * @return
	 * @throws EMPException
	 */
	public List<FncConfItems4Query> getFncConfItemsListFormCashe(List<String> list)
			throws EMPException {
		List<FncConfItems4Query> rv = new ArrayList<FncConfItems4Query>();
		FncConfItems4Query fci = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != list.get(i)) {
				fci = this.getFncConfItemsFormCashe(list.get(i));
				if (null != fci) {
					rv.add(fci);
				}
			}
		}
		return rv;
	}

	/**
	 * 根据提供的信息获取某科目的对象
	 * 
	 * @param itemId
	 *            科目ID
	 * @return
	 * @throws ComponentException
	 *             组件异常
	 */

	public FncConfItems4Query getFncConfItems(String itemId)
			throws ComponentException {
		FncConfItems4Query rv = null;
		FncConfItemsComponent fcic = (FncConfItemsComponent) this
				.getComponent(PUBConstant.FNCCONFITEMS);
		rv = fcic.findFncConfItems(itemId);
		return rv;
	}

	/**
	 * 根据提供的信息获取科目的对象列表
	 * 
	 * @param list
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList(List<String> list)
			throws ComponentException {
		List<FncConfItems4Query> rv = new ArrayList<FncConfItems4Query>();
		FncConfItems4Query fci = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != list.get(i)) {
				fci = this.getFncConfItems(list.get(i));
				if (null != fci) {
					rv.add(fci);
				}
			}
		}
		return rv;
	}

	/**
	 * 根据提供的信息获取某科目的值
	 * 
	 * @param itemId
	 *            科目ID
	 * @param vData
	 *            数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param fncType
	 *            财报类型 01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表
	 * @param termType
	 *            报表周期类型 1:月报 2:季报 3:半年报 4:年报
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException
	 *             组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType) throws ComponentException {
		double rv = 0;

		Fnc4Query fq = new Fnc4Query();
		fq.setCusId(cusId);
		fq.setFncType(fncType);
		fq.setItemId(itemId);
		fq.setTermType(termType);
		fq.setVDate(vDate);

		if (this != null) {
			rv = this.getItemValue(fq);
		}
		return rv;
	}
	
	/**
	 * 根据提供的信息获取某科目的值(报表口径)
	 * 
	 * @param itemId
	 *            科目ID
	 * @param vData
	 *            数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param fncType
	 *            财报类型 01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表
	 * @param termType
	 *            报表周期类型 1:月报 2:季报 3:半年报 4:年报
	 * @return 对应可还没的值，若没有返回0
	 * @throws ComponentException
	 *             组件异常
	 */
	public double getItemValue(String cusId, String itemId, String vDate,
			String fncType, String termType,String stat_style) throws ComponentException {
		double rv = 0;

		Fnc4Query fq = new Fnc4Query();
		fq.setCusId(cusId);
		fq.setFncType(fncType);
		fq.setItemId(itemId);
		fq.setTermType(termType);
		fq.setVDate(vDate);
		fq.setStatStyle(stat_style);
		
		if (this != null) {
			rv = this.getItemValue(fq);
		}
		return rv;
	}

	/**
	 * 根据提供的信息获取某科目的值
	 * 
	 * @param itemIdList
	 *            科目ID列表
	 * @param vData
	 *            数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param fncType
	 *            财报类型 01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表
	 * @param termType
	 *            报表周期类型 1:月报 2:季报 3:半年报 4:年报
	 * @return 对应可还没的值，即使没有只也返回‘0’
	 * @throws ComponentException
	 *             组件异常
	 */
	public List<String> getItemValueList(String cusId, List<String> list,
			String vData, String fncType, String termType)
			throws ComponentException {
		List<String> rv = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			double value = 0;
			if (null != list.get(i)) {
				value = this.getItemValue(cusId, list.get(i), vData, fncType,
						termType);
			}
			rv.add(Double.toString(value));
		}
		return rv;
	}
	
	
	/**
	 * 获取带财报数据的FncConfItems4Query
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItems4QueryList(String cusId,
			List<FncConfItems4Query> list, String vData, String termType)
			throws ComponentException {
		List<FncConfItems4Query> rv = list;

		for (int i = 0; i < rv.size(); i++) {
			double value1 = 0;
			double value2 = 0;
			double value[]=new double[1];
			if (null != rv.get(i) && null != rv.get(i).getItemId()) {			
				value1 = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType);
				value[0]=this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType);
				rv.get(i).setData1(value1);
				rv.get(i).setData(value);
			}
		}
		return rv;
	}
	
	/**
	 * 获取带财报数据的FncConfItems4Query(报表口径)
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItems4QueryList(String cusId,
			List<FncConfItems4Query> list, String vData, String termType,String stat_style)
			throws ComponentException {
		List<FncConfItems4Query> rv = list;

		for (int i = 0; i < rv.size(); i++) {
			double value1 = 0;
			double value2 = 0;
			double value[]=new double[1];
			if (null != rv.get(i) && null != rv.get(i).getItemId()) {			
				value1 = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType,stat_style);
				value[0]=this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType,stat_style);
				rv.get(i).setData1(value1);
				rv.get(i).setData(value);
			}
		}
		return rv;
	}
	
	/**
	 * 获取带财报数据的FncConfItems4Query
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws EMPException 
	 */
	public List<FncConfItems4Query> getFncConfItemsByItemList(String cusId,
			List<String> list, String vData, String termType)
			throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=this.getFncConfItems4QueryList(cusId, rv, vData, termType);
		return rv;
	}
	
	
	/**
	 * 获取年初数
	 * @param cusId
	 * @param list
	 * @param vData    yyyy0101
	 * @param fncType  年报  
	 * @param termType 资产负债表, 损益表
	 * @param stat_style 报表口径
	 * @return
	 * @throws EMPException 
	 */
	public List<FncConfItems4Query> getConfItems4BOP(String cusId,
			List<String> list, String vData)
			throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=this.getFncConfItems4QueryList(cusId, rv, vData, "4");
		return rv;
	}
	
	/**
	 * 获取年初数(报表口径)
	 * @param cusId
	 * @param list
	 * @param vData    yyyy0101
	 * @param fncType  年报  
	 * @param termType 资产负债表, 损益表
	 * @param stat_style 报表口径
	 * @return
	 * @throws EMPException 
	 */
	public List<FncConfItems4Query> getConfItems4BOP(String cusId,
			List<String> list, String vData,String stat_style)
			throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=this.getFncConfItems4QueryList(cusId, rv, vData, "4",stat_style);
		return rv;
	}
	
	/** add by tangzf
	 * 获取年初数(报表口径)
	 * @param cusId
	 * @param list
	 * @param vData    yyyy0101
	 * @param fncType  年报  
	 * @param termType 资产负债表, 损益表
	 * @param stat_style 报表口径
	 * @return
	 * @throws EMPException 
	 */
	public List<FncConfItems4Query> getConfItems4BOP(String cusId,String statPrdStyle,
			List<String> list, String vData,String stat_style)throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=this.getFncConfItems4QueryList(cusId, rv, vData, statPrdStyle,stat_style);
		return rv;
	}
	
	/**
	 * 查询是否存在财报
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws EMPException 
	 */
	public boolean isExistStatBase(String cusId,String statPrdStyle,String statPrd,String statStyle,String fncType) throws EMPException{
		boolean rv=false;
		FncStatBase tempFB = new FncStatBase();
		tempFB.setCusId(cusId);
		tempFB.setStatPrdStyle(statPrdStyle);
		tempFB.setStatPrd(statPrd);
		tempFB.setStatStyle(statStyle);
		tempFB.setFncType(fncType);
		
		FncStatCommonComponent fCommonComponent =(FncStatCommonComponent) this.getComponent(PUBConstant.FNCSTATCOMMON);
		FncStatBase pfncStatBase = null; 
		pfncStatBase = fCommonComponent.findOneFncStatBase(tempFB);
		if(pfncStatBase == null){
			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "得到的财报对象为空");
		}else{
			String statFlag = pfncStatBase.getStateFlg();//财报状态为完成才取数据
			if("2".equals(statFlag.substring(8))){
				rv=true;
			}
		}
		return rv;
	}
	/**
	 * 根据客户编号到对公客户基表看该客户是集团客户，还是非集团客户（1是集团客户；2，3是非集团客户）
	 * @param cus_id
	 * @return comGrpMode
	 * @throws ComponentException
	 * @throws EMPException
	 */
	public String getComGrpModeFromCusCom(String cus_id) throws ComponentException,EMPException{
		String comGrpMode = "";
		CusComComponent cusComComponent = (CusComComponent)this.getComponent(PUBConstant.CUSCOM);
		CusCom cusCom = cusComComponent.getCusCom(cus_id);
		if(cusCom!=null){
//			comGrpMode = cusCom.getComGrpFlag();
			String grpModeTmp = cusCom.getComGrpMode();
			if("9".equals(grpModeTmp)){//非集团
				comGrpMode = "2";
			}else {
				comGrpMode = "1";
			}
		}
		return comGrpMode;
	}
	
	public FncIndexRpt getIndexValue(String cusId,String itemId,String statStyle)throws ComponentException{
		FncIndexRpt fncIndexRpt = null;
		FncStatCommonComponent fCommonComponent =(FncStatCommonComponent) this.getComponent(PUBConstant.FNCSTATCOMMON);
		fncIndexRpt = fCommonComponent.getIndexValue(cusId,itemId,statStyle);
		return fncIndexRpt;
	}
	
	/**
	 * 根据客户的财务报表类型，和 源财报列表 获取到目标列表
	 * @param temp
	 * @param srcList
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<String> getItemIdFromDef(FncConfTemplate fncTemp ,ArrayList<String> srcList) throws ComponentException{
		Fnc4QueryAgent fqa = (Fnc4QueryAgent) this.getAgentInstance(FNCPubConstant.FNC4QA);
		
		return fqa.getItemIdFromDef(fncTemp, srcList);
	}
	/**
	 * 统计财报数目
	 * @param cusId
	 * @param statPrdStyle
	 * @param statStyle
	 * @return
	 * @throws EMPException
	 */
	public  HashMap<String, String> queryCountFncStatBase(String cusId,String statPrdStyle,String statStyle)throws EMPException{
		 HashMap<String, String> rq=new HashMap<String, String>();
		FncStatCommonComponent fCommonComponent =(FncStatCommonComponent) this.getComponent(PUBConstant.FNCSTATCOMMON);
		rq = fCommonComponent.queryCountFncStatBase(cusId,statPrdStyle,statStyle);
		return rq;
	}
}
