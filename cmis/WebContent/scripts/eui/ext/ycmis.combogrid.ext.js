/**
 * <p>easyui扩展组件：combogridext 数据表格下拉框</p>
 * <ul>扩展于easyui的combogrid组件，选择数据后在valueFieldID指定的文本框显示value值</ul>
 * <ul>属性说明：
 * 	新增属性：
 *  valueFieldID：为文本框的ID，用于显示value值。
 *  注:getValue返回text值。
 * </ul>
 * @author zangys 2014年7月10日
 * <p>修改记录</p>
 * <ul>1、修改在点击下拉表格行时对valueFieldID指定的文本框赋值。add by zangys at 2014-7-31 17:24:21</ul>
 * <ul>2、对文本框的赋值采用setValue方法。add by zangys at 2014-8-7 13:56:12</ul>
 * <ul>3、扩展combogird的setValues方法，通过$.fn.setValue赋值时，反向将存储value的输入框中的值设置到下拉
 * 		  表格框中，针对在修改数据时，只设置text值后，如果再弹出下拉表格不做选择，鼠标点击别的地方时会清空下拉表格框
 *   	  中的内容。add by zangys at 2014-9-18 10:39:48
 * </ul>
 * <ul>4、通过列表的onSelect、onUnselect事件对存储value值的输入框进行赋值，如果采用onClickRow事件则通过回车键
 * 		  选择的时候不能触发该事件。 add by zangys at 2014-9-18 10:41:07</ul>
 * <ul>5、增加getRealValue方法，用于取value值，兼容form，edategrid。add by zangys at 2015-05-29 10:29</ul>
 */
(function($){
	$.fn.combogridext = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.combogridext.methods[options];
			if(fun){
				return fun(this,param);
			}else{//继承combogrid的方法
				return this.combogrid(options,param);
			}
		}
		options=options||{};
		return this.each(function(){
			var state = $.data(this, 'combogridext');
			//继承combogrid对象
			if($.fn.combogrid){
				$(this).combogrid(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'combogridext', {
					options: $.extend({}, $.fn.combogridext.defaults, $.fn.combogridext.parseOptions(this), options)
				});
			}
			defaultsExtend(this);
		});
	};
	
	/**
	 * 扩展combogrid的grid对象的onClickRow事件，点击列表行时将value值设置到valueFieldID指定的文本框
	 * 在编辑表格、表单中均适用
	 */
	function defaultsExtend(target){
		var optsext = $.data(target,"combogridext").options;
		var opts=$.data(target,"combogrid").options;
		var grid=$.data(target,"combogrid").grid;
		
		/**
		 * 设置value值到valueFieldID指定的输入框
		 */
		function setValueToTextBox(rowIndex,rowData){
			if(opts.valueFieldID){
				var values = $(target).combogrid("getValues");
				var valueStr=values.join(opts.separator)
				$("#"+opts.valueFieldID).setValue(valueStr);
				//编辑表格中
				if(opts.parentID){
					EMP.setEDataGridValue(opts.parentID, opts.valueFieldID, valueStr);
				}
				//只显示Text
				setTimeout(function(){
					$(target).combogrid("setText",$(target).combogrid("getText"));
				},0);
			}
		}
		
		/**
		 * 通过列表的onSelect、onUnselect时对存储value值的输入框进行赋值，如果采用onClickRow事件则通过回车键
		 * 选择的时候不能触发该事件。
		 */
		$.extend($.data(target,"combogrid").options,{
			onSelect:function(rowIndex,rowData){
				setValueToTextBox(rowIndex,rowData);
				if(optsext.onSelect)
					optsext.onSelect.call(target,rowIndex,rowData);
			},
			onUnselect:function(rowIndex,rowData){
				setValueToTextBox(rowIndex,rowData);
				if(optsext.onUnselect)
					optsext.onUnselect.call(target,rowIndex,rowData);
			}
		});
		
		//输入框失去焦点时，如果输入框中内容为空则清除存储value值的输入框
		var textbox = $.data(target,"combo").combo.find("input.combo-text");
		$(textbox).focusout(function(){
			if($(target).combo("getValue")== undefined || $(target).combo("getValue") == ""){
				$("#"+opts.valueFieldID).setValue("");
				//编辑表格中
				if(opts.parentID){
					EMP.setEDataGridValue(opts.parentID, opts.valueFieldID, "");
				}
			}
		});
	}
	/**
	 * 默认值配置
	 */
	$.fn.combogridext.defaults = {//默认属性定义
		valueFieldID:''//用于显示text的输入框ID
	};
	
	/**
	 * 扩展方法getValue，返回text值
	 */
	$.fn.combogridext.methods = {
			
		/**
		 * 获取text值或者value值
		 * 如果组件指定了存储value的input框，则该函数返回text值
		 * 否则返回value值,以与combogrid保持一致性。
		 */
		getValue:function(jq){
			var opts = $.data(jq[0],"combogridext").options;
			if(opts.valueFieldID){
				return jq.combo("getText");
			}
		},
		/**
		 * 获取指定的存储value的input框的值，即真实值
		 */
		getRealValue:function(jq){
			var opts = $.data(jq[0],"combogridext").options;
			if(opts.parentID && opts.valueFieldID){
				var ed = EMP.getEDataGridEditor($(opts.parentID)[0],opts.valueFieldID);
				return ed.target.getValue();
			}else if(opts.valueFieldID){
				return $('#'+opts.valueFieldID).getValue();
			}
			
		},
		/**
		 * 扩展setValues方法，通过$.fn.setValue赋值时，反向将存储value的输入框中的值设置到下拉表格框中
		 * 针对在修改数据时，只设置text值后，如果再弹出下拉表格不做选择，鼠标点击别的地方时会清空下拉表格框
		 * 中的内容。
		 */
		setValues:function(jq,_ary){
			jQuery(jq).combogrid('setValues', _ary);
			var opts = $.data(jq[0],"combogridext").options;
			var text = jq.combo("getText");
			if(opts.valueFieldID){
				setTimeout(function(){
					var valueArr = $("#"+opts.valueFieldID).getValue();
					jQuery(jq).combogrid('setValues', [valueArr]);
					//针对value对应的选项不在第一页时，text值与value值相同的情况
					jq.combogrid("setText",text);
				},0);
			}
		}
	};
	/**
	 * 解析data-options中数据
	 */
	$.fn.combogridext.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},$.fn.combogrid.parseOptions(target),$.parser.parseOptions(target,["valueFieldID"]));
	};
	
	//将扩展表格下拉框组件添加到组件数组中，用以easyui进行解析
	$.parser.plugins.push('combogridext');
	
})(jQuery);
