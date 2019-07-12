package com.yucheng.cmis.biz01line.ind.interfaces.impl;


import java.util.HashMap;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.interfaces.IndNatuComonModelScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 分类模型得分实现类
 * @author Administrator
 *
 */
public class IndNatuComonModelScoreImpl implements IndNatuComonModelScoreIface {


	/**
	 * 取分类模型得分
	 * @param hm 模型下组得分
	 * return 
	 */
	public String getNatuComonModelScore( HashMap hm)
			throws ComponentException { 
		String retVal = "";
		
		/*获取信用等级别*/
		String credit_level = (String) hm.get("credit_rating");
		
		/*获取本金逾期天数*/
		String v_capoverduedays = (String)hm.get("CapOverdueDays");
		int capoverduedays = Integer.parseInt(v_capoverduedays);
		
		/*获取利息逾期天数*/
		String v_intoverduedays = (String)hm.get("IntOverdueDays");
		int intoverduedays = Integer.parseInt(v_intoverduedays);
		
		/*获取担保方式*/
		String guar_type = (String)hm.get("guar_type");
 
		
		if(credit_level.equals("优秀"))
		{
			if(guar_type.equals(IndPubConstant.CREDIT))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_60||intoverduedays<=IndPubConstant.OVERDUEDAYS_60)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ( (capoverduedays>IndPubConstant.OVERDUEDAYS_60&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_60&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.ASSURE))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_60||intoverduedays<=IndPubConstant.OVERDUEDAYS_60)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_60&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_60&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_270)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_270))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_270||intoverduedays>IndPubConstant.OVERDUEDAYS_270)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.GUARANTY))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_90||intoverduedays<=IndPubConstant.OVERDUEDAYS_90)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_180&&capoverduedays<=IndPubConstant.OVERDUEDAYS_270)||(intoverduedays>IndPubConstant.OVERDUEDAYS_180&&intoverduedays<=IndPubConstant.OVERDUEDAYS_270))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_270||intoverduedays>IndPubConstant.OVERDUEDAYS_270)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.IMPAWN))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_90||intoverduedays<=IndPubConstant.OVERDUEDAYS_90)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_180&&capoverduedays<=IndPubConstant.OVERDUEDAYS_360)||(intoverduedays>IndPubConstant.OVERDUEDAYS_180&&intoverduedays<=IndPubConstant.OVERDUEDAYS_360))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_360||intoverduedays>IndPubConstant.OVERDUEDAYS_360)
				{
					retVal = IndPubConstant.DOUBT;
				}
			}
		}else if(credit_level.equals("较好"))
		{
			if(guar_type.equals(IndPubConstant.CREDIT))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_30||intoverduedays<=IndPubConstant.OVERDUEDAYS_30)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_30&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_30&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.ASSURE))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_30||intoverduedays<=IndPubConstant.OVERDUEDAYS_30)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_30&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_30&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.GUARANTY))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_60||intoverduedays<=IndPubConstant.OVERDUEDAYS_60)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_60&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_60&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.IMPAWN))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_90||intoverduedays<=IndPubConstant.OVERDUEDAYS_90)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_180&&capoverduedays<=IndPubConstant.OVERDUEDAYS_270)||(intoverduedays>IndPubConstant.OVERDUEDAYS_180&&intoverduedays<=IndPubConstant.OVERDUEDAYS_270))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_270||intoverduedays>IndPubConstant.OVERDUEDAYS_270)
				{
					retVal = IndPubConstant.DOUBT;
				}
			}
		}else if(credit_level.equals("一般") || credit_level.equals("未评级"))
		{
			if(guar_type.equals(IndPubConstant.CREDIT))
			{
				if(capoverduedays==IndPubConstant.OVERDUEDAYS_0)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_0&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_0&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.ASSURE))
			{
				if(capoverduedays==IndPubConstant.OVERDUEDAYS_0)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_0&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_0&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.GUARANTY))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_30||intoverduedays<=IndPubConstant.OVERDUEDAYS_30)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_30&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_30&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_180)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_180))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_180||intoverduedays>IndPubConstant.OVERDUEDAYS_180)
				{
					retVal = IndPubConstant.DOUBT;
				}
			} else if(guar_type.equals(IndPubConstant.IMPAWN))
			{
				if(capoverduedays<=IndPubConstant.OVERDUEDAYS_60||intoverduedays<=IndPubConstant.OVERDUEDAYS_60)
				{
					retVal = IndPubConstant.NORMAL;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_60&&capoverduedays<=IndPubConstant.OVERDUEDAYS_90)||(intoverduedays>IndPubConstant.OVERDUEDAYS_60&&intoverduedays<=IndPubConstant.OVERDUEDAYS_90))
				{
					retVal = IndPubConstant.ATTENTION;
				} else if ((capoverduedays>IndPubConstant.OVERDUEDAYS_90&&capoverduedays<=IndPubConstant.OVERDUEDAYS_270)||(intoverduedays>IndPubConstant.OVERDUEDAYS_90&&intoverduedays<=IndPubConstant.OVERDUEDAYS_270))
				{
					retVal = IndPubConstant.SUB;
				} else if (capoverduedays>IndPubConstant.OVERDUEDAYS_270||intoverduedays>IndPubConstant.OVERDUEDAYS_270)
				{
					retVal = IndPubConstant.DOUBT;
				}
			}
		}
		return retVal;
	}

}
