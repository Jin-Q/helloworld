package com.yucheng.cmis.pub;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.StringUtil;


public class ComponentHelper implements CMISDomain {

/**
 * <p>将domain的list集合转换为icol,icol的name为传入的modeId</p>
 * <p>list中的domain必须是同一个CMISDomain的实例</p>
 * @param list  domain的list
 * @param modeId domain对应的表模型ID
 * @return 将每个domian转换为kcol并add到icol中
 * @throws CMISException
 */
	public IndexedCollection domain2icol(List list, String modeId) throws CMISException{
		return this.domain2icol(list, modeId, "");
	}
	
	/**
	 * <p>将domain的list集合转换为icol,icol的name为传入的modeId</p>
	 * <p>list中的domain必须是同一个CMISDomain的实例</p>
	 * @param list  domain的list
	 * @param modeId domain对应的表模型ID
	 * @param listInd 为返回的icol的name属性是否为 modeId+"List"
	 * @return 将每个domian转换为kcol并add到icol中
	 * @throws CMISException
	 */

	public IndexedCollection domain2icol(List list, String modeId,String listInd)
			throws CMISException {

		// 转换类型

		ArrayList arrlist = (ArrayList) list;

		IndexedCollection icol = new IndexedCollection();
		
		if(CMISConstance.CMIS_LIST_IND==listInd){
			icol.setName(modeId+"List");
		}else{
			icol.setName(modeId);
		}
		

		CMISDomain domain = null;
		KeyedCollection kcol = null;
		if (null != arrlist) {
			for (int i = 0; i < arrlist.size(); i++) {
				if (null != arrlist.get(i)
						&& arrlist.get(i) instanceof CMISDomain) {
					domain = (CMISDomain) arrlist.get(i);
					kcol = this.domain2kcol(domain, modeId);
					icol.add(kcol);
				}
			}
		}
		return icol;
	}

	/**
	 * icol ---domain list 转化中间通过 kcol --- domain---------------(未测试，暂时不用)
	 * 
	 * @param domain
	 *            vo对象
	 * @param icol
	 * @return
	 * @throws Exception
	 */
	public List<CMISDomain> icol2domainlist(CMISDomain domain,
			IndexedCollection icol) throws CMISException {
		// 创建对象存储器

		KeyedCollection kcol = new KeyedCollection();
		ArrayList<CMISDomain> arrlist = new ArrayList<CMISDomain>();
		// 取出所有kcol 装入对象中
		if (!(null == icol && null == domain)) {
			for (int i = 0; i < icol.size(); i++) {
				kcol = (KeyedCollection) icol.get(i);

				try {
					domain = this.kcolTOdomain((CMISDomain) (domain.getClass()
							.newInstance()), kcol);
				} catch (InstantiationException e) {

					//e.printStackTrace();
				} catch (IllegalAccessException e) {

					//e.printStackTrace();
				}
				arrlist.add((CMISDomain) domain);
			}
		}
		return arrlist;
	}

	public List<CMISDomain> icol2domainlist(String className,
			IndexedCollection icol) throws CMISException {
		KeyedCollection kcol = new KeyedCollection();
		ArrayList<CMISDomain> arrlist = new ArrayList<CMISDomain>();
		// 取出所有kcol 装入对象中
		Class domainClass;

		if (null != icol) {

			for (int i = 0; i < icol.size(); i++) {
				kcol = (KeyedCollection) icol.get(i);

				try {
					domainClass = Class.forName(className);
					CMISDomain domain = (CMISDomain) domainClass.newInstance();
					domain = this.kcolTOdomain((CMISDomain) (domain.getClass()
							.newInstance()), kcol);
					arrlist.add((CMISDomain) domain);
				} catch (InstantiationException e) {

					//e.printStackTrace();
				} catch (IllegalAccessException e) {

					//e.printStackTrace();
				} catch (ClassNotFoundException e) {
					
					//e.printStackTrace();
				}

			}
		}
		return arrlist;

	}

	/**
	 * <p>
	 * 将domain转换成kCol
	 * </p>
	 * 
	 * @param list
	 * @return Object KeyedCollection
	 * @throws Exception
	 * @todo 将domain转换成kCol
	 */
	/*
	 * public KeyedCollection domain2kcol(CMISDomain domain) throws
	 * CMISException { KeyedCollection kCol = new KeyedCollection(); return
	 * kCol; }
	 */

	/**
	 * <p>
	 * 将domain转换成kCol,具体处理过程：
	 * <p>
	 * 通过domain得到所有成员变量，根据成员变量的名称得到对应表中字段的名称
	 * <p>
	 * 然后更加成员变量得到其get方法,调用该方法得到其值，插入到DataField 封装到KeyCollection中
	 * <p>
	 * 局限性：变量类型要已知，domain要与命名规范一直
	 * 
	 * @param domain
	 *            值对象
	 * @param modeId
	 *            模型ID/表名
	 * @return Object KeyedCollection
	 * @throws Exception
	 * @todo 将domain转换成kCol
	 */
	public KeyedCollection domain2kcol(CMISDomain domain, String modeId)
			throws CMISException {
		if (domain == null || modeId == null) {
			return null;
		}
		KeyedCollection kCol = null;
		try {
			// todo
			Class[] returnType = new Class[1];
			kCol = new KeyedCollection();
			/*
			 * 设置kCol名称
			 */
			kCol.setName(modeId);
			// 得到所有成员变量
			Field[] fields = domain.getClass().getDeclaredFields();
			/*
			 * 把domain中所有的值取出来，存入kCol
			 */
			for (int i = 0; i < fields.length; i++) {
				// 去成员变量名
				String str = fields[i].getName();
				// 在大写字母前加_ 首字母除外
				String formatName = StringUtil.getInstance()
						.AddUnderlineByUppercase(str);

				DataField df = new DataField();
				df.setName(formatName);

				/*
				 * 根据成员变量名找到对应的get方法，并调用方法得到该成员变量在对象中的值
				 */
				String sq = "get" + str.substring(0, 1).toUpperCase()
						+ str.substring(1, str.length());
				Method md = domain.getClass().getDeclaredMethod(sq);
				returnType[0] = md.getReturnType();
				/*
				 * 根据返回值的类型，得到对象中对应成员的值
				 * 并保存到DataField中----------------------------
				 * ----------此功能要独立到一个类中实现
				 */
				if (returnType[0].getName().endsWith("String")) {

					String svalue = (String) md.invoke(domain);

					if (null != svalue) {
						df.setValue(svalue);
						kCol.addDataField(df);
					}

				} else if (returnType[0].getName().endsWith("int")) {

					int ivalue = (Integer) md.invoke(domain);

					df.setValue("" + ivalue);
					kCol.addDataField(df);

				} else if (returnType[0].getName().endsWith("double")) {

					double dvalue = (Double) md.invoke(domain);

					df.setValue("" + dvalue);
					kCol.addDataField(df);

				} else if (returnType[0].getName().endsWith("BigDecimal")) {

					BigDecimal bvalue = (BigDecimal) md.invoke(domain);
					
					if(bvalue!=null){
						//double value=bvalue.doubleValue();
						df.setValue("" + bvalue);
						kCol.addDataField(df);
					}
				}

			}
			/*
			 * Iterator it = (Iterator) kCol.values().iterator();
			 * 
			 * while (it.hasNext()) { DataElement element = (DataElement)
			 * it.next(); if (element instanceof DataField) { DataField aField =
			 * (DataField) element;
			 * 
			 * } }
			 */

		} catch (Exception e) {
			
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			throw new CMISException(e);
		}
		return kCol;
	}

	/**
	 * kcoll to domain 注释 在传入domain 和 kCol 时 这个两个数据结构应该是已经存在的
	 * 注：程序调试的时候应该把所有转换时出现的异常都处理掉 直到转换时不出现异常 下面出现的system.out.println();为了调试方便
	 * 
	 * @param domain
	 * @param kCol
	 * @return
	 */
	public CMISDomain kcolTOdomain(CMISDomain domain, KeyedCollection kCol)
			throws CMISException {

		if (kCol.isEmpty() || null == kCol) {
			return null;
		}

		if (domain == null) {
			return null;
		}
		Class[] returnType = new Class[1];
		// 得到所有成员变量
		Field[] fields = domain.getClass().getDeclaredFields();
		/*
		 * 把domain中所有的值取出来，存入kCol
		 */
		for (int i = 0; i < fields.length; i++) {
			// 去成员变量名
			String str = fields[i].getName();
			// 在大写字母前加_ 首字母除外
			String formatName = StringUtil.getInstance()
					.AddUnderlineByUppercase(str);

			try {

				String sq1 = "get" + str.substring(0, 1).toUpperCase()
						+ str.substring(1, str.length());

				Method md1 = domain.getClass().getDeclaredMethod(sq1);

				if (md1 != null) {
					returnType[0] = md1.getReturnType();
					if (returnType[0].getName().endsWith("String")) {
						String sq = "set" + str.substring(0, 1).toUpperCase()
								+ str.substring(1, str.length());
						Method md = domain.getClass().getDeclaredMethod(sq,
								String.class);
						String kValue = (String) kCol.getDataValue(formatName);
						//System.out.println(kValue);
						md.invoke(domain, kValue);
					} else if (returnType[0].getName().endsWith("int")) {
						String sq = "set" + str.substring(0, 1).toUpperCase()
								+ str.substring(1, str.length());
						Method md = domain.getClass().getDeclaredMethod(sq,
								int.class);

						md.invoke(domain, Integer.parseInt((String) kCol
								.getDataValue(formatName)));

					} else if (returnType[0].getName().endsWith("double")) {
						String sq = "set" + str.substring(0, 1).toUpperCase()
								+ str.substring(1, str.length());
						Method md = domain.getClass().getDeclaredMethod(sq,
								double.class);

						md.invoke(domain, Double.parseDouble((String) kCol
								.getDataValue(formatName)));
					} else if (returnType[0].getName().endsWith("BigDecimal")) {
						String sq = "set" + str.substring(0, 1).toUpperCase()
								+ str.substring(1, str.length());
						Method md = domain.getClass().getDeclaredMethod(sq,
								BigDecimal.class);
 
						String _val = (String)kCol.getDataValue(formatName);
						if(_val == null || _val.trim().equals("")){
							_val = "0";
						}
						BigDecimal bv =new BigDecimal((String)kCol.getDataValue(formatName));
						md.invoke(domain, bv);
					}
				}

			} catch (Exception e) {
				 //e.printStackTrace();
			}
		}
		return domain;
	}

	/**
	 * <p>
	 * 将kCol转换成domain
	 * </p>
	 * 
	 * @param classname
	 *            (domain的类名),kCol
	 * @return Object类对象
	 * @throws bjectNotFoundException
	 *             ,InvalidArgumentException,InvocationTargetException,
	 *             IllegalAccessException
	 *             ,ClassNotFoundException,InstantiationException
	 * @todo 将kCol转换成domain
	 */
	public Object kcolTodomain(String className, KeyedCollection kCol)
			throws ObjectNotFoundException, InvalidArgumentException,
			InvocationTargetException, IllegalAccessException,
			ClassNotFoundException, InstantiationException {

		Class domainClass = Class.forName(className);
		Object domain = domainClass.newInstance();

		int j;
		// 保存所有去除下划线的字段名list（全部小写）
		List<String> datanames = new ArrayList<String>();
		// 保存所有方法名list
		List<String> methodnames = new ArrayList<String>();
		// 字段名与属性 名对照，键=小写属性名，值=字段名
		Map<String, String> datanameMap = new HashMap<String, String>();
		// 字段名与set方法对照
		Map<String, Method> methodnameMap = new HashMap<String, Method>();

		// 获取kCol中数据元素(对应表字段名)放入迭代器中
		Iterator it = kCol.keySet().iterator();

		// 将迭代器中数据元素取出，去除其中的'_'封装到datanames列表中，
		// 并将去除'_'前后的数据元素封装成HashMap，以去除后的为Key。
		while (it.hasNext()) {
			String dataname = it.next().toString();
			String tmpstring = null;
			// 将字段名去除下划线
			for (int t = 0; t < dataname.length(); t++) {
				if (dataname.charAt(t) != '_')
					tmpstring = tmpstring + dataname.charAt(t);
			}

			datanames.add(tmpstring.toLowerCase());
			datanameMap.put(tmpstring, dataname);
		}

		// 获取domain对象的所有方法
		Method methods[] = domain.getClass().getDeclaredMethods();
		j = domain.getClass().getDeclaredMethods().length;

		// 从方法数组中获得domain方法的方法名，放到methodnames列表中，
		// 并将方法名和方法对象封装到数组中
		for (int i = 0; i < j; i++) {
			String methodname = methods[i].getName();

			if (methodname.charAt(0) == 's') {
				methodname = methodname.substring(3);
				methodnames.add(methodname.toLowerCase());
				methodnameMap.put(methodname, methods[i]);
			}
		}

		// 将去除'_'后的kCol数据元素与domain对象方法名作比较，若相等则在datanameMap中获取kCol数据元素
		// 在methodnameMap中获取方法对象，调用invoke方法，将这个kCol数据元素对应值set到domain对象中去。
		for (int k = 0; k < methodnames.size(); k++) {
			String s1 = datanames.get(k).toString();
			for (int l = 0; l < methodnames.size(); l++) {
				String s2 = methodnames.get(l).toString();
				if (s1.equals(s2)) {
					String s3 = datanameMap.get(s1).toString();

					Method tmpMethod = (Method) methodnameMap.get(s2);
					tmpMethod.invoke(domain, kCol.getDataValue(s3));
				}
			}
		}
		return domain;
	}



	/**
	 * kcol到map的转换
	 * 
	 * <pre>
	 * &lt;cusbase text='客户信息'&gt;&lt;br&gt;
	 * 		&lt;!----&gt;&lt;br&gt;
	 * 		&lt;item id='comclltyp' text='所属行业' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='comcrdgrade' text='年度信用等级' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='comreglchkdt' text='执照最近年审日期' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='comwprmaddr' text='注册登记地址' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='commainprd' text='主营业务及产品' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='accmng' text='主办客户经理' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='crpname' text='法定代表人' value=''/&gt;&lt;br&gt;
	 * 		&lt;item id='regcpt' text='注册资本' value=''/&gt;&lt;br&gt;
	 * 		&lt;/cusbase&gt;&lt;br&gt;
	 * 		
	 * 		将以下kcol结构转换为map&lt;br&gt;
	 * 		kcol结构&lt;br&gt;
	 * 		kcol name=cusBase&lt;br&gt;
	 * 			dataField name=	comclltyp  		value=&quot;***&quot;&lt;br&gt; 	
	 * 			dataField name=	comcrdgrade  	value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	comreglchkdt	value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	comwprmaddr		value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	commainprd		value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	accmng			value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	crpname			value=&quot;***&quot;&lt;br&gt;
	 * 			dataField name=	regcpt			value=&quot;***&quot;&lt;br&gt;
	 * 		kcol end&lt;br&gt;
	 * 		map结构&lt;br&gt;
	 * 		map&lt;br&gt;
	 * 			key=comclltyp  		value=&quot;***&quot;&lt;br&gt; 	
	 * 			key=comcrdgrade  	value=&quot;***&quot;&lt;br&gt;
	 * 			key=comreglchkdt	value=&quot;***&quot;&lt;br&gt;
	 * 			key=comwprmaddr		value=&quot;***&quot;&lt;br&gt;
	 * 			key=commainprd		value=&quot;***&quot;&lt;br&gt;
	 * 			key=accmng			value=&quot;***&quot;&lt;br&gt;
	 * 			key=crpname			value=&quot;***&quot;&lt;br&gt;
	 * 			key=regcpt			value=&quot;***&quot;&lt;br&gt;
	 * 		map end&lt;br&gt;
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	private  Map<String, String> koc2Map(KeyedCollection kcol) {
		Map<String, String> toMap = new HashMap<String, String>();// 要转换的Map
		String key = null;
		String value = null;

		// 循环得到kcol的name和value放入map中
		for (int i = 0; i < kcol.size(); i++) {
			// kcol.getName();
			try {
				key = ((DataField) kcol.getDataElement(i)).getName();
				value = (String) ((DataField) kcol.getDataElement(i))
						.getValue();
			} catch (InvalidArgumentException e) {
				
				//e.printStackTrace();
			}
			toMap.put(key, value);
		}
		return toMap;
	}

	/**
	 * kcol到map 的转换
	 * 
	 * <pre>
	 * &lt;cusrels text='其他关联企业'&gt;&lt;br&gt;
	 * 		&lt;!-- 多条记录--&gt;&lt;br&gt;
	 * 			&lt;cusrel&gt;&lt;br&gt;
	 * 				&lt;item id='seq' text='序号' value=''/&gt;&lt;br&gt;
	 * 				&lt;item id='relcusname' text='关联企业名称' value=''/&gt;&lt;br&gt;
	 * 			&lt;/cusrel&gt;&lt;br&gt;
	 * 			&lt;cusrel&gt;&lt;br&gt;
	 * 				&lt;item id='seq' text='序号' value=''/&gt;&lt;br&gt;
	 * 				&lt;item id='relcusname' text='关联企业名称' value=''/&gt;&lt;br&gt;
	 * 			&lt;/cusrel&gt;&lt;br&gt;
	 * 		&lt;/cusrels&gt;&lt;br&gt;
	 * 		将以下icol结构转换为list&lt;br&gt;
	 * &lt;kColl id=&quot;cusapts&quot; append=&quot;false&quot;&gt;&lt;br&gt;                   --------------Map
	 * 			&lt;iColl id=&quot;cusapt0&quot; append=&quot;false&quot;&gt;&lt;br&gt;              --------------List
	 * 				&lt;kColl id=&quot;cominvamt&quot; append=&quot;false&quot;&gt;&lt;br&gt;        --------------Map
	 * 					&lt;field id=&quot;id&quot; value=&quot;cominvamt&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;text&quot; value=&quot;投资金额&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;value&quot; value=&quot;aa4&quot;/&gt;&lt;br&gt;
	 * 				&lt;/kColl&gt;&lt;br&gt;
	 * 				&lt;kColl id=&quot;cominvtperc&quot; append=&quot;false&quot;&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;id&quot; value=&quot;cominvtperc&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;text&quot; value=&quot;投资比例&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;value&quot; value=&quot;aa5&quot;/&gt;&lt;br&gt;
	 * 				&lt;/kColl&gt;&lt;br&gt;
	 * 			&lt;/iColl&gt;&lt;br&gt;
	 * 			&lt;iColl id=&quot;cusapt1&quot; append=&quot;false&quot;&gt;&lt;br&gt;
	 * 				&lt;kColl id=&quot;cominvamt&quot; append=&quot;false&quot;&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;id&quot; value=&quot;cominvamt&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;text&quot; value=&quot;投资金额&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;value&quot; value=&quot;bb4&quot;/&gt;&lt;br&gt;
	 * 				&lt;/kColl&gt;&lt;br&gt;
	 * 				&lt;kColl id=&quot;cominvtperc&quot; append=&quot;false&quot;&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;id&quot; value=&quot;cominvtperc&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;text&quot; value=&quot;投资比例&quot;/&gt;&lt;br&gt;
	 * 					&lt;field id=&quot;value&quot; value=&quot;bb5&quot;/&gt;&lt;br&gt;
	 * 				&lt;/kColl&gt;&lt;br&gt;
	 * 			&lt;/iColl&gt;&lt;br&gt;
	 * 		&lt;/kColl&gt;&lt;br&gt;
	 * </pre>
	 * 
	 * @param kcol
	 * @return
	 */
	private Map<String, List<Map<String, String>>> kol2Map(
			KeyedCollection kcol) {
		Map<String, List<Map<String, String>>> toMap = new HashMap<String, List<Map<String, String>>>();
		IndexedCollection icol = null;
		KeyedCollection kCol = null;
		String key = null;
		String value = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String kName = kcol.getName();
		for (int m = 0; m < kcol.size(); m++) {
			String subName = kName.substring(0, kName.length() - 1) + m;
			icol = (IndexedCollection) kcol.get(subName);
			for (int i = 0; i < icol.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				kCol = (KeyedCollection) icol.get(i);

				for (int j = 0; j < kCol.size(); j++) {
					try {
						key = ((DataField) kCol.getDataElement(j)).getName();
						value = (String) ((DataField) kCol.getDataElement(j))
								.getValue();
					} catch (InvalidArgumentException e) {
						
						//e.printStackTrace();
					}
					map.put(key, value);
				}
				list.add(map);
			}
			toMap.put(subName, list);
		}

		return toMap;
	}

	/**
	 * icol到list的转换
	 * 
	 * <pre>
	 * &lt;cusrels text='其他关联企业'&gt;&lt;br&gt;
	 * 		&lt;!-- 多条记录--&gt;&lt;br&gt;
	 * 			&lt;cusrel&gt;&lt;br&gt;
	 * 				&lt;item id='seq' text='序号' value=''/&gt;&lt;br&gt;
	 * 				&lt;item id='relcusname' text='关联企业名称' value=''/&gt;&lt;br&gt;
	 * 			&lt;/cusrel&gt;&lt;br&gt;
	 * 			&lt;cusrel&gt;&lt;br&gt;
	 * 				&lt;item id='seq' text='序号' value=''/&gt;&lt;br&gt;
	 * 				&lt;item id='relcusname' text='关联企业名称' value=''/&gt;&lt;br&gt;
	 * 			&lt;/cusrel&gt;&lt;br&gt;
	 * 		&lt;/cusrels&gt;&lt;br&gt;
	 * 		将以下icol结构转换为list&lt;br&gt;
	 * 		icol结构&lt;br&gt;
	 * 		icol name=cusrels
	 * 			kcol name=cusrel1&lt;br&gt;
	 * 				dataField name=	seq  		value=&quot;***&quot;&lt;br&gt; 	
	 * 				dataField name=	relcusname 	value=&quot;***&quot;&lt;br&gt;
	 * 			kcol end&lt;br&gt;
	 * 			kcol name=cusrel2&lt;br&gt;
	 * 				dataField name=	seq  		value=&quot;***&quot;&lt;br&gt; 	
	 * 				dataField name=	relcusname 	value=&quot;***&quot;&lt;br&gt;
	 * 			kcol end&lt;br&gt;
	 * 			......&lt;br&gt;
	 * 		icol end&lt;br&gt;
	 * 		
	 * 		List结构&lt;br&gt;
	 * 		List 
	 * 			map&lt;br&gt;
	 * 				key=comclltyp  		value=&quot;***&quot;&lt;br&gt; 	
	 * 				key=comcrdgrade  	value=&quot;***&quot;&lt;br&gt;
	 * 			map end&lt;br&gt;
	 * 			map&lt;br&gt;
	 * 				key=comclltyp  		value=&quot;***&quot;&lt;br&gt; 	
	 * 				key=comcrdgrade  	value=&quot;***&quot;&lt;br&gt;
	 * 			map end&lt;br&gt;
	 * 			......&lt;br&gt;
	 * 		List end&lt;br&gt;
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws
	 */

	private List<Map<String, String>> icol2List(IndexedCollection icol) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String key = null;
		String value = null;
		KeyedCollection kCol = new KeyedCollection();

		// 循环得到Kcol，将转换出的数据放入list中
		for (int i = 0; i < icol.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			kCol = (KeyedCollection) icol.get(i);
			// 循环得到一个DataField，将得到的数据放入map中
			for (int j = 0; j < kCol.size(); j++) {
				try {
					key = ((DataField) kCol.getDataElement(j)).getName();
					value = (String) ((DataField) kCol.getDataElement(j))
							.getValue();
				} catch (InvalidArgumentException e) {
					//e.printStackTrace();
				}
				map.put(key, value);
			}
			list.add(map);
		}

		return list;
	}

	/**
	 * icol到map的转换
	 * 
	 * <pre>
	 * &lt;cfs  flag='' text='现金流量表'&gt;&lt;br&gt;	
	 * 				&lt;item id='bsctgntcsflw' text='经营活动产生的现金流量净额' init='' cur='' prd='' diff=''/&gt;&lt;br&gt;
	 * 				&lt;item id='invctgntcsflw' text='投资活动产生的现金流量净额' init='' cur='' prd='' diff=''/&gt;&lt;br&gt;
	 * 				&lt;item id='fndctgntcsflw' text='筹资活动产生的现金流量净额' init='' cur='' prd='' diff=''/&gt;&lt;br&gt;	
	 * 	 &lt;/cfs&gt;&lt;br&gt; 
	 * 	 icol结构&lt;br&gt;
	 * 	 icol name=csf&lt;br&gt;
	 *   			kcol name=bsctgntcsflw&lt;br&gt;
	 *   				dateField name=init value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=cur value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=prd value=&quot;***&quot;&lt;br&gt;
	 *   			kcol end&lt;br&gt;
	 *   			
	 *   			kcol name=invctgntcsflw&lt;br&gt;
	 *   				dateField name=init value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=cur value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=prd value=&quot;***&quot;&lt;br&gt;
	 *   			kcol end&lt;br&gt;
	 *   			
	 *   			kcol name=fndctgntcsflw&lt;br&gt;
	 *   				dateField name=init value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=cur value=&quot;***&quot;&lt;br&gt;
	 *   				dateField name=prd value=&quot;***&quot;&lt;br&gt;
	 *   			kcol end&lt;br&gt;
	 * 	 icol end&lt;br&gt;
	 * 	 map结构&lt;br&gt;
	 * 	 map&lt;br&gt;
	 * 	 	key=bsctgntcsflw value=map&lt;br&gt;
	 * 	 	 				map&lt;br&gt;
	 * 	 	 					key=init value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=cur value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=prd value=&quot;****&quot;&lt;br&gt;
	 * 	 	 				map end&lt;br&gt;
	 * 	 	key=invctgntcsflw value=map&lt;br&gt;
	 * 	 	 				map&lt;br&gt;
	 * 	 	 					key=init value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=cur value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=prd value=&quot;****&quot;&lt;br&gt;
	 * 	 	 				map end&lt;br&gt;
	 * 	 	key=fndctgntcsflw value=map&lt;br&gt;
	 * 	 	 				map&lt;br&gt;
	 * 	 	 					key=init value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=cur value=&quot;****&quot;&lt;br&gt;
	 * 	 	 					key=prd value=&quot;****&quot;&lt;br&gt;
	 * 	 	 				map end &lt;br&gt;				 				
	 * 	 map end&lt;br&gt;
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	private Map<String, Map<String, String>> icol2Map(
			IndexedCollection icol) {

		Map<String, Map<String, String>> toMap = new HashMap<String, Map<String, String>>();
		String key = null;
		String value = null;
		String name = null;
		KeyedCollection kCol = null;

		// 循环得到kcol
		for (int i = 0; i < icol.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			kCol = (KeyedCollection) icol.get(i);
			// 得到kcol的name
			name = kCol.getName();
			// 循环得到kcol里每个DataElement的name、value放入map中
			for (int j = 0; j < kCol.size(); j++) {
				try {
					key = ((DataField) kCol.getDataElement(j)).getName();
					value = (String) ((DataField) kCol.getDataElement(j))
							.getValue();
				} catch (InvalidArgumentException e) {
					
					//e.printStackTrace();
				}

				map.put(key, value);
			}
			toMap.put(name, map);
		}
		return toMap;
	}

	/**
	 * list到icol的转换
	 * 
	 * @param list
	 *            <List<Map<String,String>>><br>
	 * 
	 *            <pre>
	 * 说明:第一层list存放的是符合条件的记录&lt;br&gt;
	 *     第二层list存放的是一条记录查询出来的字段&lt;br&gt;
	 *     第三层map存放的是具体字段的键值对(以字段名为键key)&lt;br&gt;
	 * </pre>
	 * @return IndexedCollection<br>
	 *         icol的格式：<br>
	 * 
	 *         <pre>
	 * iCol&lt;br&gt;
	 *     kCol&lt;br&gt;
	 *       field id=&quot;EDT_FLG&quot; value=&quot;1&quot;&lt;br&gt;
	 *       field id=&quot;CUS_ID&quot; value=&quot;A0000001&quot;&lt;br&gt;
	 *       ...&lt;br&gt;
	 *     /kCol&lt;br&gt;
	 *     kCol&lt;br&gt;
	 *       field id=&quot;EDT_FLG&quot; value=&quot;0&quot;&lt;br&gt;
	 *       field id=&quot;CUS_ID&quot; value=&quot;A0000001&quot;&lt;br&gt;
	 *       ...&lt;br&gt;
	 *     /kCol&lt;br&gt;
	 *     ...&lt;br&gt;
	 *  /iCol&lt;br&gt;
	 * </pre>
	 */
	private IndexedCollection list2Icol(List list) {
		IndexedCollection icol = new IndexedCollection();
		KeyedCollection kc = null;
		DataField df = null;
		List subList = null;
		Map subMap = null;
		try {
			/**
			 * 循环一次得到一条记录,放入kc中，然后将kc放入icol中
			 */
			for (int i = 0; i < list.size(); i++) {
				subList = (List) list.get(i);
				kc = new KeyedCollection();
				kc.setName("" + i);
				/**
				 * 循环一次得到一条记录中的一个字段
				 */
				for (int j = 0; j < subList.size(); j++) {
					subMap = (Map) subList.get(j);
					/**
					 * 循环从map容器中取键值对，keyName表示键，map.get(keyName)得到值
					 * 此中循环方式jdk1.5支持
					 */
					for (Object k : subMap.keySet()) {
						/**
						 * 具体一个字段形成一个DataField
						 */
						df = new DataField();
						df.setName((String) k);
						df.setValue(subMap.get(k));
						/**
						 * 将DataField放入kc中
						 */
						kc.addDataField(df);
					}
				}
				/**
				 * 将kc放入icol中
				 */
				icol.add(kc);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return icol;
	}

	public static int searchArr(String[] rec, String key) {
		for (int i = 0; i < rec.length; i++) {
			if (rec[i].equalsIgnoreCase(key))
				return i;
		}
		return -1;
	}

	public boolean isBaseType(String type) {
		boolean bool = false;
		String[] baseTypeStr = { "boolean", "char", "byte", "short", "int",
				"long", "float", "double", "void", "[" };

		for (int i = 0; i < baseTypeStr.length; i++) {
			if (type.indexOf(baseTypeStr[i]) != -1) {
				bool = true;
			}
		}
		return bool;

	}

	public Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
	return result;

	}

	/**
	 * 适用于icol只包含kcol（单表查询返回的icol）
	 * 
	 * @param
	 * @return
	 * @exception
	 * @param domain
	 * @param iCol
	 * @return
	 */
	public List<CMISDomain> icol2domainList4One(CMISDomain domain,
			IndexedCollection iCol) {
		List<CMISDomain> rtList = null;
		KeyedCollection tempKcol = null;
		CMISDomain tempDomain;
		try {
			tempDomain = (CMISDomain) domain.clone();
		} catch (CloneNotSupportedException e) {
			
			//e.printStackTrace();
		}
		for (int i = 0; i < iCol.size(); i++) {
			if (null != iCol.get(i) && iCol.get(i) instanceof KeyedCollection) {
				tempKcol = (KeyedCollection) iCol.get(i);
				try {
					tempDomain = this.kcolTOdomain((CMISDomain) domain.clone(),
							tempKcol);
					if (null != tempDomain) {
						rtList.add(tempDomain);
					}
				} catch (CMISException e) {
					
					//e.printStackTrace();
				} catch (CloneNotSupportedException e) {
					
					//e.printStackTrace();
				}

			}

		}

		return iCol;

	}
	
	/**
	 * 将父类所有的属性COPY到子类中。  
     * 类定义中child一定要extends father；  
     * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate
	 * @param father
	 * @param child
	 * @throws Exception
	 */
	
    private void fatherToChild (Object father,Object child)throws Exception{   
        if(!(child.getClass().getSuperclass()==father.getClass())){   
            throw new Exception("child不是father的子类");   
        }   
        Class fatherClass= father.getClass();   
        Field ff[]= fatherClass.getDeclaredFields();   
        for(int i=0;i<ff.length;i++){   
            Field f=ff[i];//取出每一个属性，如deleteDate   
            Class type=f.getType();   
            Method m=fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate   
            Object obj=m.invoke(father);//取出属性值                
            f.set(child,obj);   
        }   
    }   
    /**  
     * 首字母大写，in:deleteDate，out:DeleteDate  
     */  
    private String upperHeadChar(String in){   
        String head=in.substring(0,1);   
        String out=head.toUpperCase()+in.substring(1,in.length());   
        return out;   
    }  
	
    /**
     * 将表名转换为表模型名
     * @param tabName
     * @return
     */
    public String tabName2modId(String tabName) {
        String modId = "";
        String[] temp = NewStringUtils.split(tabName, "_");
        for (int t = 0; t < temp.length; t++) {
                modId = modId + temp[t].charAt(0) + temp[t].substring(1);

        }

        return modId;
}
    
    public String modId2tabName(String modId) {
        

        return StringUtil.AddUnderlineByUppercase(modId);
}
}


/*

*/