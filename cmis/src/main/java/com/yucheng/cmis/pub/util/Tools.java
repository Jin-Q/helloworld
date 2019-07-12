package com.yucheng.cmis.pub.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.PUBConstant;

public class Tools {


	/**
     * 将context内的键值进行改名
     * 
     * @param context
     * @param oldKey
     * @param newKey
     * @throws DuplicatedDataNameException
     * @throws EMPException
     */
    public static void renameKey(Context context, String oldKey, String newKey)
            throws EMPException {
        Iterator it = context.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object o = context.get(key);
            // 非集合
            if (!(o instanceof IndexedCollection)) {
                if (key.indexOf(oldKey) != -1) {
                    key = replaceStr(key, oldKey, newKey);
                    try {
                        context.setDataValue(key, o);
                    } catch (Exception e) {
                        context.addDataField(key, o);
                    }
                }
            }
            // 集合
            else {
                // 先检查改名后的集合是否存在，如果存在先把旧的删掉
                // String icollKey = replaceStr(
                // ((IndexedCollection)o).getName(), oldKey, newKey);
                // if(context.containsKey(icollKey)) {
                // context.remove(icollKey);
                // }
                renameKey((IndexedCollection) o, oldKey, newKey);

            }
        }
    }

    // public static void renameKey(Context context, String oldKey, String
    // newKey) {
    //		
    // Iterator it = context.keySet().iterator();
    // while(it.hasNext()) {
    // String key = (String) it.next();
    // Object o = context.get(key);
    // if(o instanceof IndexedCollection) {
    // renameKey((IndexedCollection)o, oldKey, newKey);
    // } else {
    // //如果包含oldkey就行进改名
    // if(key.indexOf(oldKey)!=-1) {
    // //kcoll.remove(key);
    // it.remove();
    // key = replaceStr(key,oldKey, newKey);
    //					
    // }
    // }
    // }
    // }
    /**
     * 为KeyedCollection对象的键值改名 注意：
     * icoll里可以套kcoll,kcoll里可以套icoll，但是不能icoll套icoll,kcoll套kcoll 不会报错，只接忽略
     * 
     * @param kcoll
     * @param oldKey
     * @param newKey
     */
    public static void renameKey(KeyedCollection kcoll, String oldKey,
            String newKey) {

        // String a = TableModel.DATAMAP_SEPARATOR;
        Iterator it = kcoll.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object val = kcoll.get(key);
            // 如果是一个IndexedCollection接口对象
            if (val instanceof IndexedCollection) {
                renameKey((IndexedCollection) val, oldKey, newKey);
            } else {
                // 如果包含oldkey就行进改名
                if (key.indexOf(oldKey) != -1) {
                    // kcoll.remove(key);
                    it.remove();
                    key = replaceStr(key, oldKey, newKey);
                    kcoll.put(key, val);
                }
            }
        }
    }

    /**
     * 为List对象内的各对象键值改名 注意： List里可以套Map,Map里可以套List，但是不能List套List,Map套Map
     * 不会报错，只接忽略
     * 
     * @param icoll
     * @param oldKey
     * @param newKey
     */
    public static void renameKey(IndexedCollection icoll, String oldKey,
            String newKey) {

        if (icoll == null)
            return;

        String key = icoll.getName();
        if (key.indexOf(oldKey) != -1) {
            key = replaceStr(key, oldKey, newKey);
            // key.replace(oldKey, newKey);
        }
        icoll.setName(key);

        for (int i = 0; i < icoll.size(); i++) {
            Object val = icoll.get(i);
            if (val instanceof KeyedCollection) {
                renameKey((KeyedCollection) val, oldKey, newKey);
            }
        }
    }
    
	/**
	 * 动态修改context中dictIColl中的值
	 * @param optTyp    操作类型
	 * @param context
	 * @param type  	dictIColl的key
	 * @param enname  	每条字典项的key
	 * @param cnname  	每条字典项的value
	 * @throws InvalidArgumentException
	 * @throws ObjectNotFoundException 
	 */
	public static void modifyDictIColl(String optTyp, Context context, String type, String enname, String cnname) throws InvalidArgumentException, ObjectNotFoundException {
		KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME) ;
		if(dictColl.containsKey(type)){
			IndexedCollection iColl = (IndexedCollection)dictColl.getDataElement(type) ;
			KeyedCollection kColl = new KeyedCollection() ;
			if(optTyp.equals(PUBConstant.MODIFY)){
				for(int i=0;i<iColl.size();i++){
					kColl = (KeyedCollection)iColl.get(i) ;
					if(kColl.getName().equals(enname)){
						kColl.put("cnname", cnname) ;
						return ;
					}
				}
			}else if(optTyp.equals(PUBConstant.ADD)){
				kColl.setName(enname) ;
				kColl.put("enname", enname) ;
				kColl.put("cnname", cnname) ;
				
				iColl.addDataElement(kColl) ;
			}else if(optTyp.equals(PUBConstant.DELETE)){
				for(int i=0; i<iColl.size(); i++){
					KeyedCollection subKColl = (KeyedCollection)iColl.get(i) ;
					
					if(subKColl.getName().equals(enname))
						iColl.remove(i) ;
				}
			}
		}
	}

    /**
     * 取系统时间
     */
    public static Date getCurrDateTime() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 取得当前系统时间,并格式化
     * 
     * @return
     */
    public static String getCurrDate(String fm) {
        Date date_time = getCurrDateTime();
        return FormatDate(date_time, fm);
    }

    /**
     * 对日期进行格式化
     * 
     * @param date
     *            日期
     * @param sf
     *            日期格式
     * @return 字符串
     */
    public static String FormatDate(Date date, String sf) {
        SimpleDateFormat dateformat = new SimpleDateFormat(sf);
        return dateformat.format(date);
    }

    public static String replaceStr(String str, String str1, String str2) {

        StringBuffer sb = new StringBuffer("");
        String[] strArry = str.split(str1);
        for (int i = 0; i < strArry.length; i++) {
            if (i == strArry.length - 1) {
                sb.append(strArry[i]);
            } else {
                sb.append(strArry[i] + str2);
            }
        }
        return sb.toString();
    }

    /**
     * 向context中设置值或增加值
     * 
     * @param key
     * @param o
     * @throws DuplicatedDataNameException
     * @throws InvalidArgumentException
     */
    public static void setContextValue(Context context, String key, Object o)
            throws EMPException {

        /*
         * 如果是DataElement的子类，说明是一个集合，
         * context.setDataElement方法是把这个大集合整体替换context里的内容， 这种功能估计我用的这个层面是使用不上。。。
         * 先把原来的值删除，再添加进去
         */
        if (o instanceof DataElement) {
            context.removeDataElement(key);
            context.addDataElement((DataElement) o);
        }

        /*
         * 非集合对象
         */
        else {
            try {
                context.setDataValue(key, o);
            } catch (Exception e) {
                context.addDataField(key, o);
            }
        }
    }

    /**
     * 根据传入的日期计算相差月份数
     * @param start
     * @param end
     * @return
     */
    public static int getMonthDifference(Date start, Date end) {
        int monthDifference = 0;
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DATE, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        if (startCalendar.get(Calendar.DATE) == endCalendar.get(Calendar.DATE)) {
            monthDifference = (year * 12 + month) == 0 ? 1
                    : (year * 12 + month);
        } else if (startCalendar.get(Calendar.DATE) > endCalendar
                .get(Calendar.DATE)) {
            monthDifference = year * 12 + month;
        } else {
            monthDifference = year * 12 + month + 1;
        }
        return monthDifference;
    }

    public static void main(String[] args) {
        String str = getCurrDate("-");
    }
    
    
	/**
	 * 通过ENNAME获得系统COM_CDE对应的字典值
	 * @param context
	 * @param dicName
	 * @param enName
	 * @return
	 */
	public static String getComCdeCnName(Context context, String dicName, String enName) {
		
		String cnName = null;
		try {
			KeyedCollection dicKcoll = (KeyedCollection) context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
			IndexedCollection dicIcoll = (IndexedCollection) dicKcoll.getDataElement(dicName);
			for(int i=0;i<dicIcoll.size();i++) {
				KeyedCollection kcoll = (KeyedCollection) dicIcoll.get(i);
				if(enName.equals(kcoll.get("enname"))) {
					cnName = (String) kcoll.get("cnname");
					break;
				}
					
			}
			
		} catch (Exception e) {
			System.out.println("转换字典项["+enName+"]时发生异常，异常信息为："+e.getMessage());
			cnName = "";
		} finally {
			return cnName;
		}
	}
	
	
	/**
	 * 简化从通过SqlOperator返回的结果kcoll中取得某字段的值
	 * 
	 * @param kcoll
	 * @param key
	 * @return
	 */
	public static String getResultSetValue(KeyedCollection kcoll ,String key) {
		
		IndexedCollection icoll = (IndexedCollection) kcoll.get(PUBConstant.RESULT_SET);
		KeyedCollection tempKcoll = (KeyedCollection) icoll.get(0);		//取第一条数据
		if(tempKcoll == null || !tempKcoll.containsKey(key)) {		//如果
			return null;
		} else {
			return (String) tempKcoll.get(key);
		}
	}
	
}
