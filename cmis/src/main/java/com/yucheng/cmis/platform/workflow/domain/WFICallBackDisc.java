package com.yucheng.cmis.platform.workflow.domain;

/**
 * 打回原因
 * <p>
 * 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求
 * 主要用在获取有效的打回原因，以该对象返回。
 * 
 * @author jwang 2015/7/27
 *
 */
public class WFICallBackDisc {
	private String pkId;
	private String cbEnname;
	private String cbCnname;
	private String cbMemo;
	private String attr1;
	private String attr2;
	private String attr3;
	private String orNo;
	private String isInuse;
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	public String getCbEnname() {
		return cbEnname;
	}
	public void setCbEnname(String cbEnname) {
		this.cbEnname = cbEnname;
	}
	public String getCbCnname() {
		return cbCnname;
	}
	public void setCbCnname(String cbCnname) {
		this.cbCnname = cbCnname;
	}
	public String getCbMemo() {
		return cbMemo;
	}
	public void setCbMemo(String cbMemo) {
		this.cbMemo = cbMemo;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getOrNo() {
		return orNo;
	}
	public void setOrNo(String orNo) {
		this.orNo = orNo;
	}
	public String getIsInuse() {
		return isInuse;
	}
	public void setIsInuse(String isInuse) {
		this.isInuse = isInuse;
	}

}
