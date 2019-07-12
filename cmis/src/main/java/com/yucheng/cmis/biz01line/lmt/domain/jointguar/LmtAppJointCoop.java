package com.yucheng.cmis.biz01line.lmt.domain.jointguar;

import com.yucheng.cmis.pub.CMISDomain;

public class LmtAppJointCoop implements CMISDomain{
	private String serno;
	private String cus_id;
	private String coop_type;
	private String share_range;
	private String belg_org;
	
	private String 	app_date;
	private String over_date;
	private String is_biz_area_joint;
	private String biz_area_no;
	private String cur_type;
	private String lmt_totl_amt;
	private String single_max_amt;
	private String term_type;
	private String term;
	
	private String manager_id;
	private String manager_br_id;
	private String input_id;
	private String input_br_id;
	private String input_date;
	private String approve_status;
	
	public String getSerno() {
		return serno;
	}


	public void setSerno(String serno) {
		this.serno = serno;
	}


	public String getCus_id() {
		return cus_id;
	}


	public void setCus_id(String cusId) {
		cus_id = cusId;
	}


	public String getCoop_type() {
		return coop_type;
	}


	public void setCoop_type(String coopType) {
		coop_type = coopType;
	}


	public String getShare_range() {
		return share_range;
	}


	public void setShare_range(String shareRange) {
		share_range = shareRange;
	}


	public String getBelg_org() {
		return belg_org;
	}


	public void setBelg_org(String belgOrg) {
		belg_org = belgOrg;
	}


	public String getApp_date() {
		return app_date;
	}


	public void setApp_date(String appDate) {
		app_date = appDate;
	}


	public String getOver_date() {
		return over_date;
	}


	public void setOver_date(String overDate) {
		over_date = overDate;
	}


	public String getIs_biz_area_joint() {
		return is_biz_area_joint;
	}


	public void setIs_biz_area_joint(String isBizAreaJoint) {
		is_biz_area_joint = isBizAreaJoint;
	}


	public String getBiz_area_no() {
		return biz_area_no;
	}


	public void setBiz_area_no(String bizAreaNo) {
		biz_area_no = bizAreaNo;
	}


	public String getCur_type() {
		return cur_type;
	}


	public void setCur_type(String curType) {
		cur_type = curType;
	}


	public String getLmt_totl_amt() {
		return lmt_totl_amt;
	}


	public void setLmt_totl_amt(String lmtTotlAmt) {
		lmt_totl_amt = lmtTotlAmt;
	}


	public String getsingle_max_amt() {
		return single_max_amt;
	}


	public void setsingle_max_amt(String singleMaxAmt) {
		single_max_amt = singleMaxAmt;
	}


	public String getManager_id() {
		return manager_id;
	}


	public void setManager_id(String managerId) {
		manager_id = managerId;
	}


	public String getManager_br_id() {
		return manager_br_id;
	}


	public void setManager_br_id(String managerBrId) {
		manager_br_id = managerBrId;
	}


	public String getInput_id() {
		return input_id;
	}


	public void setInput_id(String inputId) {
		input_id = inputId;
	}


	public String getInput_br_id() {
		return input_br_id;
	}


	public void setInput_br_id(String inputBrId) {
		input_br_id = inputBrId;
	}


	public String getInput_date() {
		return input_date;
	}


	public void setInput_date(String inputDate) {
		input_date = inputDate;
	}


	public String getApprove_status() {
		return approve_status;
	}


	public void setApprove_status(String approveStatus) {
		approve_status = approveStatus;
	}


	public String getSingle_max_amt() {
		return single_max_amt;
	}


	public void setSingle_max_amt(String singleMaxAmt) {
		single_max_amt = singleMaxAmt;
	}


	public String getTerm_type() {
		return term_type;
	}


	public void setTerm_type(String termType) {
		term_type = termType;
	}


	public String getTerm() {
		return term;
	}


	public void setTerm(String term) {
		this.term = term;
	}


	public Object clone() throws CloneNotSupportedException {
        Object result = super.clone();
        return result;
    } 
}
