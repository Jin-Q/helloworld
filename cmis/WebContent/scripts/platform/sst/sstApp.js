var SSTApp = function() {
	return {
		// 初始化配置信息
		config : {
			sstInfoUrl : "getSstInfoDetailData.do?EMP_SID=" + empId, // 请求数据的url
			pageFlags : null,// 获取哪个子页面的数据,不设置为取全部，取多个页面使用英文逗号(,)分割
			changedLabelCss : {
				"color" : "red",
				"font-weight" : "bold"
			}, // 发生了改变的表单的label的样式
			sstSeq : "", // 要加载的快照数据的主键
			formRender : "", // readonly|disabled
			eTableEdit : false, // 可编辑表格是否可编辑
			onAfterRender : function() { // 回调方法

			}
		},
		// 记录后台加载的数据
		resData : {},
		// 初始化
		init : function(options) {
			options = options || {};
			this.config = $.extend(this.config, options);
			this.loadData();
		},
		// 数据加载
		loadData : function() {
			var sstAppThis = this;
			var requestData = {
				sstSeq : sstAppThis.config.sstSeq
			};
			if (sstAppThis.config.pageFlags && sstAppThis.config.pageFlags != null && sstAppThis.config.pageFlags != "") {
				requestData.pageFlags = sstAppThis.config.pageFlags;
			}
			$.ajax({
				url : sstAppThis.config.sstInfoUrl,
				data : requestData,
				type : "post",
				dataType : "json",
				success : function(result, status, xhr) {
					sstAppThis.resData = result;
					sstAppThis.renderData();
				},
				error : function(xhr, status, error) {
					alert(error);
				}
			});
		},
		// 数据渲染
		renderData : function() {
			var jsonData = this.resData;
			var options = this.config;
			$.each(jsonData.rows, function(index, row) {
				try {
					var formDatas = eval("(" + row.form_data + ")");
					$.each(formDatas, function(i, formData) {
						var fieldId = formData.fieldId;
						var value = formData.fieldValue;
						var fieldRender = formData.fieldRender;

						// 获取class用于判断表单类型
						var _cssClass = $("#" + fieldId).attr('class');
						if (_cssClass == undefined) {
							_cssClass = "";
						}
						// 普通表格
						if (_cssClass.indexOf('easyui-datagrid') >= 0) {
							if (value && value != null && value != "") {
								var jsonValue = eval("(" + value + ")");
								$("#" + fieldId).datagrid("loadData", jsonValue);
							}
						} else if (_cssClass.indexOf('easyui-edatagrid') >= 0) {
							// 可编辑表格,根据配置禁用编辑
							if (value && value != null && value != "") {
								var jsonValue = eval("(" + value + ")");
								$("#" + fieldId).datagrid("loadData", jsonValue);
								if (!options.tableEdit) {
									$("#" + fieldId).edatagrid("disableEditing");
								}
							}
						} else {
							// 表单
							$("#" + fieldId).setValue(value);
							var fiedlDesc = formData.fieldDesc;
							if (_cssClass.indexOf('easyui-combo') >= 0) {
								if (fiedlDesc && fiedlDesc != null && fiedlDesc != "") {
									$("#" + fieldId).combo("setText", fiedlDesc);
								}
							}
							// 根据配置设置是否只读或者禁用
							if (options.formRender && options.formRender == "readonly") {
								$("#" + fieldId).renderReadonly(true);
							} else if (options.formRender && options.formRender == "disabled") {
								$("#" + fieldId).renderDisabled(true);
							}
							if (fieldRender && fieldRender == "hidden") {
								$("#" + fieldId).renderHidden(true);
							}
							if (fieldRender && fieldRender == "show") {
								$("#" + fieldId).renderHidden(false);
							}
							// 设置发生了改变的表单的label的样式
							var fieldChanged = formData.fieldChanged;
							if (fieldChanged && (fieldChanged == "Y" || fieldChanged == "y") && options.changedLabelCss) {
								$("#label_" + fieldId).css(options.changedLabelCss);
							}
						}
					});
				} catch (e) {
					alert(e);
				}
			});
			// 执行回调
			if (options.onAfterRender) {
				options.onAfterRender.call(this);
			}
		}
	};
};