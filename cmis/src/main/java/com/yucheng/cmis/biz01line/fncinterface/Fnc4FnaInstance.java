package com.yucheng.cmis.biz01line.fncinterface;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.component.FncConfItemsComponent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.biz01line.fnc.master.component.Fnc4QueryComponent;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatBaseComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.Fnc4Query;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.FNCPubConstant;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 * 为财务分析提供财务接口
 * 
 * @Classname com.yucheng.cmis.fncinterface.Fnc4FnaInterface.java
 * @author wqgang
 * @Since 2009-4-15 下午02:56:58
 * @Copyright yuchengtech
 * @version 1.0
 */
public class Fnc4FnaInstance extends CMISComponent implements Fnc4FnaInterface {

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
	 * @param list ItemID
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
	 * @param list itemID
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
	/*	double rv = 0;

		Fnc4Query fq = new Fnc4Query();
		fq.setCusId(cusId);
		fq.setFncType(fncType);
		fq.setItemId(itemId);
		fq.setTermType(termType);
		fq.setVDate(vDate);
		

		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this
				.getComponent(FNCPubConstant.FNC4QC);

		if (fqc != null) {
			rv = fqc.getItemValue(fq);
		}*/
		String statStyle="1";
		String Com_Grp_Mode = this.getComGrpModeFromCusCom(cusId);
		if("1".equals(Com_Grp_Mode)){
			statStyle="2";//合并报表
		}else{
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
		List<String> rv = new ArrayList<String>();
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
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return 对应可还没的值，即使没有只也返回‘0’
	 * 
	 * @throws ComponentException
	 *             组件异常
	 */
	public List<String> getItemValueList(String cusId, List<String> list,
			String vData, String fncType, String termType,String statStyle)
			throws ComponentException {
		List<String> rv = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			double value = 0;
			if (null != list.get(i)) {
				value = this.getItemValue(cusId, list.get(i), vData, fncType,
						termType,statStyle);
			}
			rv.add(Double.toString(value));
		}
		return rv;
	}

	/**
	 * 获取经过整理item数据value2存放前期数据value1存放本期数据valuedill存放差值，如果小于0为减少，否则为增加
	 * @param cusId
	 * @param list
	 * @param vData 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4datax(String cusId,
			List<FncConfItems4Query> list, String vData, String termType)
			throws ComponentException {
		List<FncConfItems4Query> rv = list;
		String dataValue = "";
		DecimalFormat df = new DecimalFormat("#########0.0000");
		for (int i = 0; i < rv.size(); i++) {
			double value1 = 0;
			double value2 = 0;
			double valuedill = 0;
			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				value2 = this.getItemValue(cusId, rv.get(i).getItemId(),
						TimeUtil.getPeryyMMdd(vData, termType), rv.get(i)
								.getFncConfTyp(), termType);
				value1 = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType);
				valuedill = value1 - value2;
				rv.get(i).setData1(value1);
				rv.get(i).setData2(value2);
				dataValue = String.valueOf(df.format(new BigDecimal(valuedill)));
				rv.get(i).setDatadill1(Double.parseDouble(dataValue));

			}
		}
		return rv;
	}
	
	/**
	 * 获取经过整理item数据value2存放前期数据value1存放本期数据valuedill存放差值，如果小于0为减少，否则为增加
	 * @param cusId
	 * @param list
	 * @param vData 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param fncType
	 * @param termType
	  * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4datax(String cusId,
			List<FncConfItems4Query> list, String vData, String termType,String statStyle)
			throws ComponentException {
		List<FncConfItems4Query> rv = list;

		String dataValue = "";
		DecimalFormat df = new DecimalFormat("#########0.0000");
		
		
		for (int i = 0; i < rv.size(); i++) {
			double value1 = 0;
			double value2 = 0;
			double valuedill = 0;
			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				value2 = this.getItemValue(cusId, rv.get(i).getItemId(),
						TimeUtil.getPeryyMMdd(vData, termType), rv.get(i)
								.getFncConfTyp(), termType,statStyle);
				value1 = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType,statStyle);
				valuedill = value1 - value2;
				rv.get(i).setData1(value1);
				rv.get(i).setData2(value2);
				dataValue = String.valueOf(df.format(new BigDecimal(valuedill)));
				rv.get(i).setDatadill1(Double.parseDouble(dataValue));

			}
		}
		return rv;
	}

	/**
	 * 获取经过整理item数据value2，value1存放相同的值
	 * @param cusId
	 * @param list
	 * @param vData
	 * @param fncType
	 * @param termType
	 * @return
	 * @throws ComponentException
	 */

	public List<FncConfItems4Query> getFncConfItemsList4dataAll(String cusId,
			List<FncConfItems4Query> list, String vData, String termType)
			throws ComponentException {
		List<FncConfItems4Query> rv = list;
		for (int i = 0; i < rv.size(); i++) {
			double value = 0;
			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				value = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
						rv.get(i).getFncConfTyp(), termType);
				rv.get(i).setData2(value);
				rv.get(i).setData1(value);
			}
		}
		return rv;
	}

	 	
		/**
		 * 获取经过整理item数据value2，value1存放相同的值
		 * @param cusId
		 * @param list
		 * @param vData
		 * @param fncType
		 * @param termType
		 * @param statStyle  报表口径  1 本部 2合并 3未知 
		 * @return
		 * @throws ComponentException
		 */

		public List<FncConfItems4Query> getFncConfItemsList4dataAll(String cusId,
				List<FncConfItems4Query> list, String vData, String termType,String statStyle)
				throws ComponentException {
			List<FncConfItems4Query> rv = list;
			for (int i = 0; i < rv.size(); i++) {
				double value = 0;
				if (null != rv.get(i) && null != rv.get(i).getItemId()) {
					value = this.getItemValue(cusId, rv.get(i).getItemId(), vData,
							rv.get(i).getFncConfTyp(), termType,statStyle);
					rv.get(i).setData2(value);
					rv.get(i).setData1(value);
				}
			}
			return rv;
		}
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4Query(String cusId,
			List<FncConfItems4Query> list, String vDate, String termType,
			int term) throws ComponentException {
		List<FncConfItems4Query> rv = list;

		for (int i = 0; i < rv.size(); i++) {
			//double value1 = 0;

			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				String[] date = TimeUtil.getPerNyyMMdd(vDate, termType,term);
						
				rv.get(i).setData(new double[term]);
				
				double[] dataTmp=rv.get(i).getData();
				
				for (int j = 0; j < term; j++) {
					dataTmp[j] = this.getItemValue(cusId, rv.get(i)
							.getItemId(), date[j], rv.get(i).getFncConfTyp(), termType);
				}

				rv.get(i).setData(dataTmp);

			}
		}
		return rv;
	}
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中 用于BSB财务分析
	 * *在这里不同于以上取值，每次取到每一个报表项目的N期数据
	 * 封装于一个IndexedCollection，其中每一个报表项目用一个KeyedCollection封装，
	 * KeyedCollection的Id为其期数，如，1代表本期，2代表上年同期，3代表上两年同期
	 * 循环每个报表项目,把所有
	 * 报表项目的IndexCollection封装成一个List<IndexedCollection>
	 * 这里主要利于财务分析图形的显示
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @return
	 * @throws EMPException 
	 */
	public List<IndexedCollection> getFncConfItemsList4QueryForBSB(String cusId,
			List<FncConfItems4Query> list, String vDate, String termType,
			int term) throws EMPException {
		List<FncConfItems4Query> rv = list;
		List<IndexedCollection> iCollList = new ArrayList<IndexedCollection>();
		try{
		for (int i = 0; i < rv.size(); i++) {
			//double value1 = 0;

			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				String[] date = TimeUtil.getPerNyyMMdd(vDate, termType,term);
		
				rv.get(i).setData(new double[term]);
				
				IndexedCollection termIColl = new IndexedCollection(rv.get(i).getItemName());
				
				double[] dataTmp=rv.get(i).getData();
				for (int j = 0; j < term; j++) {
					dataTmp[j] = this.getItemValue(cusId, rv.get(i)
							.getItemId(), date[j], rv.get(i).getFncConfTyp(), termType);
					KeyedCollection kColl = new KeyedCollection(j+1+"");
					kColl.addDataField("seq_id", j+1);
					kColl.addDataField("item_id", rv.get(i).getItemId());
					kColl.addDataField("item_name", rv.get(i).getItemName());
					kColl.addDataField("value", dataTmp[j]);
					kColl.addDataField("date", date[j]);
					termIColl.addDataElement(kColl);
				}
				iCollList.add(termIColl);

			}
		}
		}catch(EMPException e){
			throw new EMPException(e);
		}
		return iCollList;
	}
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中 用于BSB财务分析
	 * 在这里不同于以上取值，每次取到所有个报表项目的一期数据
	 * 封装于一个IndexedCollection，其中每一个报表项目用一个KeyedCollection封装，
	 * KeyedCollection的Id为其期数，如，1代表本期，2代表上年同期，3代表上两年同期
	 * 循环每个报表项目,把所有
	 * 报表项目的N期财务数据IndexCollection封装成一个List<IndexedCollection>
	 * 这里主要利于财务分析图形的显示
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @return
	 * @throws EMPException 
	 */
	public List<IndexedCollection> getFncConfItemsList4QueryForBSB1(String cusId,
			List<FncConfItems4Query> list, String vDate, String termType,
			int term) throws EMPException {
		List<FncConfItems4Query> rv = list;
		List<IndexedCollection> iCollList = new ArrayList<IndexedCollection>();
		try{
			for (int j = 0; j < term; j++) {
				IndexedCollection termIColl = new IndexedCollection();
				String[] date = TimeUtil.getPerNyyMMdd(vDate, termType,term);
				for (int i = 0; i < rv.size(); i++) {
					//double value1 = 0;
		
					if (null != rv.get(i) && null != rv.get(i).getItemId()) {
						
						rv.get(i).setData(new double[term]);
						
						double[] dataTmp=rv.get(i).getData();
						
						dataTmp[j] = this.getItemValue(cusId, rv.get(i).getItemId(), date[j], rv.get(i).getFncConfTyp(), termType);
						KeyedCollection kColl = new KeyedCollection(j+1+"");
						kColl.addDataField("seq_id", j+1);
						kColl.addDataField("item_id", rv.get(i).getItemId());
						kColl.addDataField("item_name", rv.get(i).getItemName());
						kColl.addDataField("value", dataTmp[j]);
						kColl.addDataField("date", date[j]);
						termIColl.addDataElement(kColl);
						
					}
				}
				iCollList.add(termIColl);
			}
		}catch(EMPException e){
			throw new EMPException(e);
		}
		return iCollList;
	}
	
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4Query(String cusId,
			List<FncConfItems4Query> list, String vDate, String termType,
			int term,String statStyle) throws ComponentException {
		List<FncConfItems4Query> rv = list;

		for (int i = 0; i < rv.size(); i++) {
			//double value1 = 0;

			if (null != rv.get(i) && null != rv.get(i).getItemId()) {
				String[] date = TimeUtil.getPerNyyMMdd(vDate, termType,term);
						
				rv.get(i).setData(new double[term]);
				
				double[] dataTmp=rv.get(i).getData();
				
				for (int j = 0; j < term; j++) {
					dataTmp[j] = this.getItemValue(cusId, rv.get(i)
							.getItemId(), date[j], rv.get(i).getFncConfTyp(), termType,statStyle);
				}

				rv.get(i).setData(dataTmp);

			}
		}
		return rv;
	}
	
	
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list  item list
	 * @param vDate 
	 * @param termType
	 * @param term
	 * 
	  * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws EMPException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4QueryWithItmeId(String cusId,
			List<String> list, String vDate, String termType,
			int term,String statStyle) throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=getFncConfItemsList4Query(cusId,rv, vDate,  termType,term,statStyle);
		return rv;
	}
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list  item list
	 * @param vDate  归属日期 数据归属日期 格式为 2012-09-11
	 * @param termType 报表类型   STD_ZB_FNC_STAT  年报（常用的比较是年报）
	 * @param term 期数（返回几期财报）
	 * @return
	 * @throws EMPException
	 */
	public List<FncConfItems4Query> getFncConfItemsList4QueryWithItmeId(String cusId,
			List<String> list, String vDate, String termType,
			int term) throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		rv=getFncConfItemsList4Query(cusId,rv, vDate,  termType,term);
		return rv;
	}
	
	
	/**
	 * 根据客户号，要转换的 财报条目列表
	 * @param cusId
	 * @param srcList
	 * @return 返回对应转换后的财务报表条目
	 * @throws ComponentException
	 */
	public ArrayList<String> converFncConfItem(String cusId ,ArrayList<String> srcList)throws EMPException{
		
		if(srcList == null || srcList.size() == 0){
			return new ArrayList<String>();
		}
		
		
		if(StringUtils.isBlank(cusId)){
			throw new IllegalArgumentException("客户号不能为空");
		}
		
        /*
         * 获取对应客户的财务报表 模板
         */
		FncStatBaseComponent fncStatComponent = (FncStatBaseComponent)this.getOtherComponentInstance(PUBConstant.FNCSTATBASE);
		
		String repType = fncStatComponent.findCusRepType(cusId);
	
		
		/*
		 * 校验客户的财务报表模板
		 */
		if(StringUtils.isBlank(repType)){
			
			EMPLog.log(this.getClass().getName(), EMPLog.WARNING, 0,"客户号["+cusId+"]未配置对应的财务报表模板");
			return new ArrayList<String>();
		}
		
		
		
		FncConfTemplate fncTemp = fncStatComponent.findFncConfTemplate(repType);
		
		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this.getComponent(FNCPubConstant.FNC4QC);
		
		return fqc.getItemIdFromDef(fncTemp,srcList);
	}
    
	
	
	
	
	/**
	 * 返回N期财务数据值封装在FncConfItems4Query对象中BSB
	 * 在这里不同于以上取值，每次取到每一个报表项目的N期数据
	 * 封装于一个IndexedCollection，循环每个报表项目,把所有
	 * 报表项目的IndexCollection封装成一个List<IndexedCollection>
	 * 这里主要利于财务分析图形的显示
	 * @param cusId 客户号
	 * @param list  报表项目列表
	 * @param vDate 
	 * @param termType
	 * @param term
	 * @return
	 * @throws EMPException
	 */
	public List<IndexedCollection> getFncConfItemsList4QueryWithItmeIdForBSB(String cusId,
			List<String> list, String vDate, String termType,
			int term) throws EMPException {
		List<FncConfItems4Query> rv = this.getFncConfItemsListFormCashe(list);
		List<IndexedCollection> iCollList = null;
		iCollList = getFncConfItemsList4QueryForBSB(cusId,rv, vDate,  termType,term);
		return iCollList;
	}
	

//	/**
//	 * 是否存在相应的报表信息
//	 * @param cusId 客户号
//	 * @param statPrdStyle 报表周期类型
//	 * @param statPrd 报表所属期间
//	 * @return
//	 * @throws EMPException
//	 */
//	public boolean isExistStatBase(String cusId, String statPrdStyle,
//			String statPrd) throws ComponentException {
//		boolean rv=false;
//		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this.getComponent(FNCPubConstant.FNC4QC);
//		
//		String statStyle="1";
//		String Com_Grp_Mode = this.getComGrpModeFromCusCom(cusId);
//		if("1".equals(Com_Grp_Mode)){
//			statStyle="2";
//		}else{
//			statStyle="1";
//		}
//		
//		try {
//			rv=fqc.isExistStatBase(cusId, statPrdStyle, statPrd,statStyle);
//		} catch (EMPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return rv;
//	}
//	
//	
//	/**
//	 * 是否存在相应的报表信息
//	 * @param cusId 客户号
//	 * @param statPrdStyle 报表周期类型
//	 * @param statPrd 报表所属期间
//	 * @param statStyle  报表口径  1 本部 2合并 3未知 
//	 * @return
//	 * @throws EMPException
//	 */
//	public boolean isExistStatBase(String cusId, String statPrdStyle,
//			String statPrd,String statStyle) throws ComponentException {
//		boolean rv=false;
//		Fnc4QueryComponent fqc = (Fnc4QueryComponent) this
//		.getComponent(FNCPubConstant.FNC4QC);
//		try {
//			rv=fqc.isExistStatBase(cusId, statPrdStyle, statPrd,statStyle);
//		} catch (EMPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return rv;
//	}
	
	/**
	 * 根据客户编号到对公客户基表看该客户是集团客户，还是非集团客户（1是集团客户；2，3是非集团客户）
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
	
	
	/**
	 * 返回一个项目的N期财务数据值封装在FncConfItems4Query对象中
	 * @param cusId 客户号
	 * @param list item查询对象列表
	 * @param vDate 归属日期 数据归属日期 格式为 yyyyMMdd 如果dd=01 为期初，如果dd=30为期末
	 * @param termType 
	 * @param term 期数（返回几期财报）
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 */
	public double[] getFncConfItemValues4Query(String cusId,
			String item_id, String vDate, String termType,
			String[] date,String statStyle,String fncConfTyp) throws ComponentException {
		    double[] values = new double[date.length];
			for (int j = 0; j < date.length; j++) {
				values[j] = this.getItemValue(cusId, item_id, date[j], fncConfTyp, termType,statStyle);
			}

			return values;

	}
	public FncStatBase findOneFncStatBase(String cusId,String statPrdStyle,String statPrd,String stat_style)throws ComponentException{
		FncStatBaseComponent fsic=(FncStatBaseComponent)this.getComponent(FNCPubConstant.FNCSTATBASE);
		return fsic.findFncStatBase(cusId, statPrdStyle, statPrd, stat_style);
	}
	
	/**
	 * 统计财报数目
	 * @param cusId
	 * @param statPrdStyle 报表周期类型
	 * @param statStyle  报表口径  1 本部 2合并 3未知 
	 * @return
	 * @throws ComponentException
	 * @throws EMPException 
	 */
	public HashMap<String, String>  getNumStatBase(String cusId,String statPrdStyle,String statStyle) throws EMPException{
		 HashMap<String, String> rq=new HashMap<String, String>();
			Fnc4QueryComponent fqc = (Fnc4QueryComponent) this.getComponent(FNCPubConstant.FNC4QC);
			rq=fqc.queryCountFncStatBase(cusId, statPrdStyle,statStyle);
			return rq;
	}

	
}
