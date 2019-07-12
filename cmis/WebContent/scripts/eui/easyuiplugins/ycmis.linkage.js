/**
 * <p>easyui扩展组件：linkage 下拉列表框联动</p>
 * <ul>$fn.linkAge：提供基于easyui的联动下拉列表框,支持下拉选项的分组</ul>
 * <ul>属性说明：
 * 	id: HTML标准属性；在Table表格中充当field属性列字段名称
 * 	labelFORM：布局:文本描述；在Table表格中充当title——列标题文本
 * 	endLabel：加在列表框后面的文本说明
 * 	style：用于自定义style，不推荐使用
 * 	dataUrl：通过URL加载远程列表数据。
 * 	valueField：基础数据值名称绑定到该下拉列表框。
 * 	textField：基础数据字段名称绑定到该下拉列表框。
 * 	groupField：分组字段。
 * 	tabs：下拉选项分组信息，格式：['','','']
 * 	preSelect：上级下拉框ID
 * 	linkedSelect：下级下拉框ID
 * 	callback：回调函数，选择下列选项时触发
 * </ul>
 * @author zangys 2014年6月6日
 * <ul>修改:1、增加全局搜索,callback改在下拉框隐藏后触发。 zangys at 2014-06-29 12:38:55</ul>
 * <ul>修改:2、在IE9及以下全局查询时下拉选项样式不更改及tab不隐藏的问题。 zangys at 2014-07-18 17:19:32</ul>
 * <ul>修改:3、完善linkage在编辑表格中生效。add by zangys at 2014-8-11 14:55:28</ul>
 * <ul>修改:4、修改其余下拉框在手动输入时联动下拉框value值改变的BUG。add by zangys at 2014-8-28 11:25:28</ul>
 * <ul>修改:5、解决在onChange事件中存在alert时，翻译失效的情况。add by zangys at 2014-9-2 11:05:10</ul>
 * <ul>修改:6、combobox空值选项‘---请选择---’样式名修改引起的不能联动加载问题。add by zangys at 2014-11-5 11:14:38</ul>
 * <ul>修改:7、修改在可编辑表格中数据无法回显的问题. add by liwei at 2016-04-08</ul>
 * <ul>修改:8、修改在可编辑表格中第二次编辑js报错，控件无法渲染的问题. add by liwei at 2016-04-08</ul>
 */
(function($){
	
	/**
	 * 修改linkage继承自combobox的默认功能
	 */
	function changeDefaults(target){
		var l=$.data(target,"linkage");
		var opts=l.options;
		
		$(target).combobox($.extend({},opts,{filter:function(q,row){
			var opts=$(this).combobox("options");
			//模糊查询
			return row[opts.textField].indexOf(q)>-1;
		}}));
	}
	
	$.fn.linkage = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.linkage.methods[options];
			if(fun){
				return fun(this,param);
			}else{//继承combobox的方法
				return this.combobox(options,param);
			}
		}
		options=options||{};
		return this.each(function(){
			var state = $.data(this, 'linkage');
			//继承combobox对象
			if($.fn.combobox){
				$(this).combobox(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'linkage', {
					options: $.extend({}, $.fn.linkage.defaults, $.fn.linkage.parseOptions(this), options)
				});
			}
			changeDefaults(this);
			init(this);
			loader(this);
			$(this).combobox({
				onHidePanel:function(){
					var opts = $.data(this,'linkage').options;
					var linkSel = opts.linkedSelect;
					var newvalue = {};
					var text = $(this).combobox("getText");
					var value = $(this).combobox("getValue");
					newvalue[opts.textField] = text;
					newvalue[opts.valueField] = value;
					var target = null;
					if(opts.tableID&&linkSel){//编辑表格中
						var ed=EMP.getEDataGridEditor($("#"+opts.tableID), linkSel);
						if(ed)
							target = ed.target;
					}else if(linkSel){//表单中
						target = $("#"+linkSel);
					}
					if(!target)
						return;
					setTimeout(function(){
						$(target).linkage("clearItems");
						$(target).linkage("loadDataByUrl",value);
						if(opts.callback)
							opts.callback.call(target,newvalue);
					},0);
				}
			});
		});
	};
	/**
	 * 初始化：增加分组信息,绑定事件
	 */
	function init(target){
		var opts = $.data(target, 'linkage').options;
		if(opts.endLabel)
			$(target).after("<label> " + opts.endLabel + " </label>");
		//获取焦点时，显示下拉框		
		$(target).combobox("textbox").bind("focus",function(){
			var obj = this;
            setTimeout(function(){
            	obj.select();
            },0);
			$(target).combobox("showPanel");
			$("#tabs_"+$(target).attr("id")).css({display:''});
			//根据当前值决定显示哪个分组
			var curValue = $(target).combobox("getValue");
			var curTab = null;
			if(curValue&&opts.initdata){
				for(var i=0;i<opts.initdata.length;i++){
					if(curValue==opts.initdata[i][opts.valueField]){
						curTab = opts.initdata[i][opts.groupField];
						break;
					}
				}
				if(curTab && opts.tabs){
					for(var k=0;k<opts.tabs.length;k++){
						if(opts.tabs[k]==curTab){
							opts.curTabIndex = k;
							break;
						}
					}
				}
			}
			//上次最后点击分组
			$("#tabs_"+$(target).attr("id")).find("a").eq(opts.curTabIndex).each(function(){
			    $("#tabs_"+$(target).attr("id")).find(".selected").removeClass("selected");
			    initData(target,$(this).attr("tabindex"));
			    $(this).addClass("selected");
			    opts.curTabIndex = $(this).attr("tabindex");
			});
		});
		
		$(target).combobox("panel").css({paddingBottom:'12'});
		$(target).combobox("panel").empty();
		addTabs(target,0);
	}
	
	/**
	 * ajax加载数据, 如果存在上级下拉框则不允许手动加载数据
	 */
	function loader(target,parent){
		var opts = $.data(target, 'linkage').options;
		var url = opts.dataUrl;
		if(opts.preSelect&&parent){
			//根据parent值过滤数据
			if(url.indexOf("?")>-1)
				url += "&parent=" + parent;
			else
				url += "?parent=" + parent;
		}
		//存在上级下拉框,则需要根据上级下拉框加载数据,即初始化时不加载数据
		if(opts.preSelect&&(parent == undefined || parent == ""))
		{
			opts.initdata = null;
			return;
		}
		
		if(url){//返回的数据格式json:[{textField:'',valueField:'',groupField:''}]
			$.ajax( {
				type : "POST",
				url : url,
				data : null,
				success : function(rsp) {
					//如果存在tabs信息，则删除不在tabs下的选项--begin--
					var initdata = [];
					if(opts.tabs && rsp){
						for(var k=0;k<opts.tabs.length;k++){
							var tv = opts.tabs[k];
							for(var j=0;j<rsp.length;j++){
								if(tv==rsp[j][opts.groupField])
									initdata.push(rsp[j]);
							}
						}
					}else{
						initdata = rsp;
					}
					//--end--
					opts.initdata=initdata;
					initData(target,0);
					//在view模式下显示值
					var _id = $(target).attr("id");
					if(_id)
						$('#view_'+_id.replace('.', '\\.')).text($(target).combo("getText"));
				},
				error : function(rsp) {
				    $.messager.alert(CusLang.EUIExt.linkage.msgTypeInfo,
                            CusLang.EUIExt.linkage.msgBodyPre + rsp.responseText,
                            'error');
					opts.initdata = null;
					initData(target,0);
				},
				dataType : "json"
			});
		}
	}

	/**
	 * 初始化数据
	 */
	function initData(target,tabindex)
	{
		var opts = $.data(target, 'linkage').options;
		$(target).combobox("panel").empty();
		var index = 0;
		if(opts.initdata)
			$(target).combobox("loadData",opts.initdata);
		if(tabindex)
			index = tabindex;
		if(opts.initdata && opts.tabs && opts.tabs[index]){
			var tabValue = opts.tabs[index];
			var item;
			for(var i=0;i<opts.initdata.length;i++){
				item = opts.initdata[i];
				var divItem = $(target).combobox("panel").find(".combobox-item[value="+item[opts.valueField]+"]");
				if(item[opts.groupField] == tabValue){
					divItem.css({display:''});
				}else{
					divItem.css({display:'none'});
				}
			}
		}
		addTabs(target,index);
		initItemStyle(target);
	}
	/**
	 * 增加tab
	 */
	function addTabs(target,curTabIndex){
		var opts = $.data(target, 'linkage').options;
		var panel = $(target).combobox("panel");
		if(opts.tabs){
			//标签时opts.tabs为字符串，需转换成数组
			if(typeof(opts.tabs) == "string")
				opts.tabs = eval(opts.tabs);
			var ul = $('<ul id="tabs_' + $(target).attr("id") + '" class="tab"></ul>');
			for(var i = 0;i < opts.tabs.length;i++){
				if(curTabIndex != undefined && curTabIndex == i)
					ul.append($('<li ><a href="javascript:void(0)" class="selected" tabindex="'+i+'"><em>'+opts.tabs[i]+'</em></a></li>'));
				else
					ul.append($('<li ><a href="javascript:void(0)" class="" tabindex="'+i+'"><em>'+opts.tabs[i]+'</em></a></li>'));
			}
			panel.prepend(ul);
			
			ul.find('a').bind('click',function(jq){
				$("#tabs_"+$(target).attr("id")).find(".selected").removeClass("selected");
				initData(target,$(this).attr("tabindex"));
				$(this).addClass("selected");
				opts.curTabIndex = $(this).attr("tabindex");
			});
		}
		$(target).combobox("panel").prepend($("<div class='headTitle'>"+CusLang.EUIExt.linkage.headTitle+"</div>"));
		$(target).combobox("panel").css({paddingBottom:10});
	}
	
	/**
	 * 更改样式
	 */
	function initItemStyle(target){
			$(target).combobox("panel").find(".combobox-item-psel").remove();
			$(target).combobox("panel").find(".combobox-item").each(function(i,value){
				$(value).addClass("boxItem");
		});
	}
	/**
	 * 联动清除下拉选项内容
	 */
	function clearItems(target){
		$(target).combobox("clear");
		$(target).combobox("panel").find(".combobox-item").remove();
		$.data(target,"linkage").options.initdata = null;
		var opts = $.data(target, 'linkage').options;
		var linkselectID = opts.linkedSelect;
		var jq = null;
		
		
		if(opts.tableID&&linkselectID){//编辑表格
			var ed = EMP.getEDataGridEditor($("#"+opts.tableID), linkselectID);
			jq = ed.target;
		}else if(linkselectID && $("#"+linkselectID)){//表单
			jq = $("#"+linkselectID);
		}
		if(jq){
			jq.linkage("clearItems");
		}
	}
	/**
	 * 解析data-options中数据
	 */
	$.fn.linkage.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.fn.combobox.parseOptions(target),$.parser.parseOptions(target,["groupField","linkedSelect","endLabel","panelWidth","preSelect","dataUrl","callback"]));
	};
	
	/**
	 * linkage扩展的方法
	 */
	$.fn.linkage.methods = {
			/**
			 * 过滤加载数据
			 */
			loadDataByUrl:function(jq,filter){
				return jq.each(function(){
					$.data(this,'linkage').options.initdata=null;
					loader(this,filter);
				});
			},
			/**
			 * 通过linkselect递归清除下拉选项
			 */
			clearItems:function(jq){
				return jq.each(function(){
					clearItems(this);
				});
			},
			/**
			 * 重写setValue
			 * 目的：翻译value值用,下拉框设置时，过滤加载联动的下拉框选项
			 */
			setValue:function(jq,val){
				return jq.each(function(){
					var opts = $.data(this,'linkage').options;
					$(this).combobox("setValue",val);
					var linkSel = opts.linkedSelect;
					//解决在onChange事件中存在alert时，显示真实值的情况
					setTimeout(function(){
					    var target = null;
					    //编辑表格中,不能通过id找到对应的组件
					    if(opts.tableID&&linkSel){
					        var ed=EMP.getEDataGridEditor($("#"+opts.tableID), linkSel);
					        if(ed){
					            target = ed.target;
					        }
					    }else if(linkSel){//表单中
					        target = $("#"+linkSel);
					    }
					    if(target){
					        $(target).linkage("loadDataByUrl",val);
					    }
					},0);
					
				});
			}
	};
	
	/**
	 * 默认值配置
	 */
	$.fn.linkage.defaults = $.extend({}, $.fn.combobox.defaults, {
		tabs : null,//分组信息
		linkSelect : null,
		endLabel : '',
		initdata : null,
		dataUrl : '',
		preSelect : '',
		groupField : '',
		panelWidth : '',
		callback : '',
		curTabIndex : 0,
		keyHandler:{
			query:function(q){
				$.fn.combobox.defaults.keyHandler.query.call(this,q);
				format(this)
			},
			enter:$.fn.combobox.defaults.keyHandler.enter,
			up:$.fn.combobox.defaults.keyHandler.up,
			down:$.fn.combobox.defaults.keyHandler.down
		}
	});

	/**
	 * 搜索模式下格式化下拉面板内容
	 */
	function format(target){
		$("#tabs_"+$(target).attr("id")).css({display:'none'});
		var t = null;
		if(t){
			clearTimeout(t);
		}
		t = setTimeout(function(){
			var searchText = $(target).combobox("textbox").val();
			var firstItem = null;
			if(searchText!= ""){
				$(target).combobox("panel").find(".combobox-item").each(function(i,value){
					if($(value).html().indexOf(searchText)== 0){
						if(firstItem == null){
							firstItem = $(value);
						}else{
							firstItem.after($(value));
						}
					}
				});
			}
		},0);
		
		$(target).combobox("panel").find(".combobox-item").each(function(i,value){
			$(value).removeClass("boxItem");
		});
	}
	
})(jQuery);