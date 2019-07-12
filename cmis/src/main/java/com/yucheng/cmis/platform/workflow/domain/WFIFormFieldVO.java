package com.yucheng.cmis.platform.workflow.domain;

/**
 * 流程表单字段对象（信贷暂不用）
 * @author liuhw 2013-6-17
 *
 */
public class WFIFormFieldVO {

	/**
	 * 编辑权限
	 * <p>"none"不可见，"true"只读，"false"可编辑
	 */
	private String editcontrol;
	
	/**
	 * 字段ID（nodeid_fieldCode）
	 */
	private String fieldId;
	
	/**
	 * 字段名称
	 */
	private String fieldName;
	
	/**
	 * 字段ID
	 */
	private String fieldCode;
	
	/**
	 * 表单ID
	 */
	private String formId;

	/**
	 * @return the editcontrol
	 */
	public String getEditcontrol() {
		return editcontrol;
	}

	/**
	 * @param editcontrol the editcontrol to set
	 */
	public void setEditcontrol(String editcontrol) {
		this.editcontrol = editcontrol;
	}

	/**
	 * @return the fieldId
	 */
	public String getFieldId() {
		return fieldId;
	}

	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldCode
	 */
	public String getFieldCode() {
		return fieldCode;
	}

	/**
	 * @param fieldCode the fieldCode to set
	 */
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
}
