/**
 * <p>checkbox、radio扩展组件</p>
 * <p>$fn.checkbox:提供基于easyui的checkbox/radio，可以通过url、data和dictname指定选项内容</p>
 * @author wangbin 2014-3-10 17:04:34
 * <ul>添加reload方法、loadFliter用于数据过滤 add by wangbin 2014-3-25 16:27:36</ul>
 * <ul>添加getTextValue方法，用于取文本显示值 add by yuhq at 2014-5-27 10:07:56</ul>
 * <ul>添加loadData方法，用于取文本显示值 add by yuhq at 2014-5-27 11:10:31</ul>
 * <ul>修改必输项检查、值初始化工作在数据加载完成后进行,针对当异步加载数据时初始化无效的情况。 add by zangys at 2014-7-30 14:24:12</ul>
 * <ul>修改getTextValue方法。add by zangys at 2014-10-24 14:30:05</ul>
 * <ul>修改setValue方法参数为'',赋值失效的问题。add by zangys at 2015-05-07 17:52</ul>
 * <ul>checkbox增加singleSelect属性，支持单选。add by zangys at 2015-07-09 17:25</ul>
 */
(function($){
	$.fn.checkbox = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.checkbox.methods[options];
			if(fun){
			return fun(this,param);
			}else{//继承validatebox的方法
			return this.validatebox(options,param);
			}
			}
			options=options||{};
		return this.each(function(){
			var state = $.data(this, 'checkbox');
			//继承validatebox对象
			if($.fn.validatebox){
				$(this).validatebox(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				state=$.data(this, 'checkbox', {
					options: $.extend({}, $.fn.checkbox.defaults, $.fn.checkbox.parseOptions(this), options),field:init(this)
				});
			}
			initData(this);
		});
		//初始化存放选择项的span
		function init(target){
			$(target).data().validatebox.options.required=false;
			var span=$("<span class=\"checkbox\" ></span>").insertAfter(target);
			return span;
		}
		
	};
	//初始化选择项内容
	function initData(target){
		var opts = $.data(target, 'checkbox').options;
		var field= $.data(target, 'checkbox').field;
		var id=$(target).attr("id");

		//清除field
		field.empty();
		var onchange=opts.onchange?opts.onchange:"";
		var onclick=opts.onclick?opts.onclick:"";
		//当on/off赋值时为单一选择项
		if(opts.on){
			var item=$("<input type=\""+opts.dataType+"\" onchange=\""+onchange+"\" onclick=\""+onclick+"\"  name=\""+id+"\" value=\""+opts.on+"\"></input>").appendTo(field)
			if(opts.disabled){
				item.attr('disabled',true);
			}
			initAfterLoadData(target);
		}//当url赋值时远程加载数据
		else if(opts.url){
			loader(target,opts.url);
		}else{
			initContent(target);
			initAfterLoadData(target);
		}
	}
	
	/**
	 * 需要在数据加载完成后进行的初始化工作(主要针对异步加载数据的情况)
	 * 1、必输项检查;
	 * 2、初始化值。
	 */
	function initAfterLoadData(target){
		var opts = $.data(target, 'checkbox').options;
		var field= $.data(target, 'checkbox').field;
		var id=$(target).attr("id");
		//必输项检查
		if(opts.required){
			var last=field.find('input').last();
			$(last).validatebox({validType: opts.dataType+"['"+id+"']"});
			$(last).addClass("validatebox-text");
			
		}
		//初始化值
		var value=$(target).val();
		if(value){
			field.find('input').removeAttr("checked");
			var _ary = value.split(opts.separator);
			$.each(_ary, function(key, value) {
				field.find("input[value='" + value + "']")
						.attr("checked", 'checked');
			});
		}
	}
	/**
	 * 生成多选框内容
	 * @param target
	 * @return
	 */
	function initContent(target){
		var field= $.data(target, 'checkbox').field;
		var opts = $.data(target, 'checkbox').options;
		var id=$(target).attr("id");
		var values=opts.value;
		var data=opts.data;

		var onchange=opts.onchange?opts.onchange:"";
		var onclick=opts.onclick?opts.onclick:"";
		//清空数据
		field.empty();
		//是否table布局
		var table;
		if(opts.layout){
			table=$("<table class=\"emp_field_radio_table\"></table>").appendTo(field);
		}
		
		//加载数据
		if(!data){
			return false;
		}
		for(var i=0;i<data.length;){
			if(opts.layout){
	    		var tr=$("<tr></tr>").appendTo(table);
	    		for(var j=0;j<opts.layoutCols&&i<data.length;j++,i++){
	    			var td=$("<td></td>").appendTo(tr);
	    			var v=data[i][opts.valueField];
	    			var s=data[i][opts.textField];
	    			if(v){
		    			var item=$("<input type=\""+opts.dataType+"\" onchange=\""+onchange+"\" name=\""+id+"\" value=\""+v+"\"></input><span>"+s+"</span>").appendTo(td);
		    			if(opts.disabled){
		    				item.attr('disabled',true);
		    			}
		    			
		    			//checkbox 支持单选-注意在配置onclick事件时需要带括号
		    			item.click(function(){
		    				var val = $(this).val();
		    				var isChecked = $(this).prop('checked');
		    				
		    				if(isChecked && opts.singleSelect && opts.dataType == "checkbox"){
		    					$(target).setValue(val);
		    				}
		    				if(onclick){
		    					eval(onclick);
		    				}
		    			});
	    			}
	    		}
			}else{
				var v=data[i][opts.valueField];
				var s=data[i][opts.textField];
				if(v){
					var item=$("<input type=\""+opts.dataType+"\" onchange=\""+onchange+"\" onclick=\""+onclick+"\"  name=\""+id+"\" value=\""+v+"\"></input><span>"+s+"</span>").appendTo(field);
					if(opts.disabled){
						item.attr('disabled',true);
					}
					
					//checkbox 支持单选-注意在配置onclick事件时需要带括号
					item.click(function(){
	    				var val = $(this).val();
	    				var isChecked = $(this).prop('checked');
	    				
	    				if(isChecked && opts.singleSelect && opts.dataType == "checkbox"){
	    					$(target).setValue(val);
	    				}
	    				if(onclick){
	    					eval(onclick);
	    				}
	    			});
					
				}
				i++;
			}
		}
		
	}
	/**
	 * 加载远程数据,数据加载完成后进行必输项检查、值初始化工作
	 * @param url
	 */
	function loader(target,url){
		var opts = $.data(target, 'checkbox').options;
		$.post(url,null,function(data){
			opts.data=opts.loadFilter.call(target,data);
			initContent(target);
			initAfterLoadData(target);
		},"json");
	}

	/**
	 * checkbox继承和扩展的方法
	 */
	$.fn.checkbox.methods = {
		options:function(jq){
			return $.data(jq[0],"checkbox").options;
		},destroy:function(jq){
			return jq.each(function(){
				$.data(this,"checkbox").field.remove();
				$(this).validatebox("destroy");
				$(this).remove();
			});
		},
		enable:function(jq){
			return jq.each(function(){
				var span=$.data(this,"checkbox").field;
				if(span)
					$(span).removeAttr("disabled");
			});
		},disable:function(jq){
			return jq.each(function(){
				var span=$.data(this,"checkbox").field;
				if(span)
					$(span).attr("disabled",true);
			});
		},getValue:function(jq){
			return getvalue(jq[0]);
			function getvalue(target){
				var _v = '';
				var opts=$.data(target,"checkbox").options;
				var objchecked=$.data(target,"checkbox").field.find('input:checked');
				jQuery.each(objchecked, function(key, value) {
					_v += opts.separator+ $(value).val();
				});
				if (_v.length >= 1) {
					_v = _v.substring(1);
				}else{
					_v=opts.off;
				}
				return _v;
			}
		},
		//取文本显示值
		getTextValue:function(jq){
			return getvalue(jq[0]);
			function getvalue(target){
				var _v = '';
				var opts=$.data(target,"checkbox").options;
				var objchecked=$.data(target,"checkbox").field.find('input:checked + span');
				jQuery.each(objchecked, function(key, value) {
					_v += opts.separator + $(value).html();
				});
				if (_v.length >= 1) {
					_v = _v.substring(1);
				}else{
					_v=opts.off;
				}
				return _v;
			}
		},setValue:function(jq,value){
			return jq.each(function(){
				if(value != undefined){
					var opts=$.data(this,"checkbox").options;
					var span=$.data(this,"checkbox").field;
					span.find('input').removeAttr("checked");
					var _ary = value.split(opts.separator);
					$.each(_ary, function(key, value) {
						span.find("input[value='" + value + "']")
								.attr("checked", 'checked');
					});
				}
			});
		},
		//重新加载url
		reload:function(jq,url){
			return jq.each(function(){
				var opts=$.data(this,"checkbox").options;
				if(url){
					opts.url=url;
				}
				initData(this);
			});
		},
		//根据本地data重新加载
		loadData:function(jq,data){
			return jq.each(function(){
				var opts=$.data(this,"checkbox").options;
				if(data){
					opts.data=data;
				}
				initData(this);
			});
		}
	};


	$.fn.checkbox.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.fn.validatebox.parseOptions(target),$.parser.parseOptions(target,["valueField","textField","data","method","url","layout","on","off",{layoutCols:"number"}]),{required:(t.attr("required")?true:undefined),disabled:(t.attr("disabled")?true:undefined)});
	};
	/**
	 * checkbox继承自validatebox
	 */
	$.fn.checkbox.defaults = $.extend({}, $.fn.validatebox.defaults, {
		valueField:'enname',
		textField:'cnname',
		dataType:'checkbox',
		url:null,
		data:null,//[{id:value,text:value},{id:value,text:value}...]
		layout:false,
		layoutCols:1,
		on:null,
		off:'',
		method:'POST',
		disabled:false,
		separator:',',
		value:null,
		onchange:null,
		onclick:null,
		loadFilter:function(data){
			if(data.rows)
				return data.rows;
			return data;
		},
		singleSelect:false
	});
})(jQuery);