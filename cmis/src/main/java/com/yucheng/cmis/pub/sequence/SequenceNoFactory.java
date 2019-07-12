package com.yucheng.cmis.pub.sequence;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.sequence.CMISSequenceService;

public class SequenceNoFactory {

	private static SequenceNoFactory instance = new SequenceNoFactory();

	/**
	 * 单例模式,私有化构造函数
	 */
	private SequenceNoFactory() {

	}

	public static SequenceNoFactory getInstance() {
		if (instance == null) {
			instance = new SequenceNoFactory();
		}
		return instance;
	}

	/**
	 * 获得所有申请业务流水号(新增)
	 * 
	 * @param date
	 * @return
	 * @throws EMPException
	 */
	public String getLmcAppKey(Context context) throws EMPException {
		return getUniqueNoYZ(context, "SQ", "fromDate", 15);

	}

	/**
	 * 获得个体立卷号的序列号
	 * 
	 * @return
	 */
	public String getLmcFilingPvpIndivSerno(Context context, String brId,
			String beforeStr) throws EMPException {
		return getUniqueNo("LJ", brId, beforeStr, "indiv", context);
	}

	/**
	 * 获得团体立卷号的序列号
	 * 
	 * @return
	 */
	public String getLmcFilingPvpComSerno(Context context, String brId,
			String beforeStr) throws EMPException {
		return getUniqueNo("LJ", brId, beforeStr, "com", context);
	}

	/**
	 * 获得发生号的序列号
	 * 
	 * @return
	 */
	public String getLmcFilingHappenSerno(Context context, String filingNo,
			String brId) throws EMPException {
		return getHapUniqueNo("FS", brId, filingNo, context);
	}

	/**
	 * 根据传入的前缀,编号获取立卷号序列号
	 * 
	 * @param preName
	 *            序号类型
	 * @param bh
	 *            ALL
	 * @param beforeString
	 *            需要增加的前置
	 * @param serType
	 *            indiv/com 个人/团体业务标识
	 * @param context
	 * @param connection
	 * @return
	 * @throws EMPException
	 */
	private String getUniqueNo(String preName, String bh, String beforeString,
			String serType, Context context) throws EMPException {
		CMISSequenceService sequenceService = (CMISSequenceService) context
				.getService("sequenceService");
		String serNo = null;// sequenceService.getLmcSequence(preName,
							// bh,beforeString,serType, context);
		return serNo;
	}

	/**
	 * 根据传入的前缀,编号获取发生号序列号
	 * 
	 * @param preName
	 *            序号类型
	 * @param bh
	 *            ALL
	 * @param beforeString
	 *            需要增加的前置
	 * @param serType
	 *            indiv/com 个人/团体业务标识
	 * @param context
	 * @param connection
	 * @return
	 * @throws EMPException
	 */
	private String getHapUniqueNo(String preName, String bh, String filingNo,
			Context context) throws EMPException {
		CMISSequenceService sequenceService = (CMISSequenceService) context
				.getService("sequenceService");
		// String serNo = sequenceService.getLmcHapSequence(preName,
		// bh,filingNo, context);
		String orgId = (String) context.getDataValue("ARTI_ORGANNO").toString()
				.substring(0, 5);
		// 鄞州模式
		String serNo = null;
		return serNo;
	}

	/**
	 * 根据传入的前缀,长度获取标准的序列号
	 * 
	 * @param context
	 * @param connection
	 * @param fromType
	 *            （fromDate：根据日期，fromOrg：根据机构）
	 * @param beforeString
	 *            （前缀）
	 * @param length
	 *            （长度）
	 * @return
	 * @throws EMPException
	 */
	private String getUniqueNoYZ(Context context, String beforeString,
			String fromType, int length) throws EMPException {
		CMISSequenceService sequenceService = (CMISSequenceService) context
				.getService("sequenceService");
		String serNo = null;// sequenceService.getSequence(beforeString,
							// fromType,length, context);
		return serNo;
	}
}
