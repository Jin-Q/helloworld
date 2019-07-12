/**
 * <p>easyui扩展组件：datapanel 数据面板</p>
 * <ul>$fn.datapanel：提供基于easyui的数据面板</ul>
 * <p>属性说明</p>
 * <ul>
 * 	url:返回json数据远程地址。
 *  data:要加载的数据，json格式
 *  columns:数据列信息[{},{}]
 * </ul>
 * <p>列配置说明</p>
 *<ul>
 * <emp:text label="title" id="value" 
 * 		dataOptions="position:'top',titlestyle:'',fieldstyle:'',floatstyle:'left'">
 * </emp:text>
 * label:字段标题，为空则不显示。
 * id:要显示的字段，通过此id取json中的数据。必须。
 * position:数据显示位置，enum position{top,middle,bottom}，必须。
 * titlestyle:对应label显示样式。
 * fieldstyle：对应id显示内容样式。
 * floatstyle：数据显示居左or居右，enum floatstyle{left,right}.
 * </ul>
 * @author zangys 2014年6月9日
 * <p>
 * 修改记录:
 * <ul>1、增加字段对字典项的支持,以及列为数字类型的支持。zangys at 2014-07-02 10:39:26</ul>
 * </p>
 */

(function($){
	$.fn.datapanel = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.datapanel.methods[options];
			if(fun){
				return fun(this,param);
			}else{//继承panel的方法
				return this.panel(options,param);
			}
		}
		options=options||{};
		return this.each(function(){
			var state = $.data(this, 'datapanel');
			//继承panel对象
			if($.fn.panel){
				$(this).panel(options);
			}
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'datapanel', {
					options: $.extend({}, $.fn.datapanel.defaults, $.fn.datapanel.parseOptions(this), options)
				});
			}
			loadDataByUrl(this);
		});
	};
	
	/**
	 * 根据Url通过ajax获取后台数据
	 * @param target this对象
	 */
	function loadDataByUrl(target){
		var opts = $.data(target, 'datapanel').options;
		opts.initdata = null;
		var url = opts.url;
		if(url){
			$.ajax( {
				type : "POST",
				url : url,
				data : null,
				success : function(rsp) {
					opts.data = rsp;
					initData(target);
				},
				error : function(rsp) {
				    $.messager.alert(CusLang.EUIExt.datapanel.errorTitle,
                            CusLang.EUIExt.datapanel.errorMsg);
				},
				dataType : "json"
			});
		}
	}
	
	/**
	 * 加载数据
	 * @param target this对象
	 */
	function initData(target){
		var opts = $.data(target, 'datapanel').options;
		var columns = null;
		if(opts.columns)
			columns = opts.columns;
		var Origin_x = opts.width/2;
		//datapanel宽度设置过小时以防不能完全显示数据单元
		if(Origin_x < 330)
			Origin_x = 330;
		//添加开始DIV
		var titlePos = {left:Origin_x,top:20};
		createDiv("titleDiv",titlePos,38).appendTo($(target));
		//数据单元在panel中高度
		var dataItemHeight = 231;
		//数据单元实际高度
		var dataItemTrueHeight = 212;
		//添加Y轴
		var Ypos = {left:Origin_x,top:80};
		var ylineDiv = createDiv("yline",Ypos,6);
		var yHeight = "100%";
		//Y轴高度计算
		if(opts.data)
			yHeight =  parseInt(opts.data.length/2)*dataItemHeight + opts.data.length%2*dataItemTrueHeight;
		ylineDiv.css({height:yHeight});
		ylineDiv.appendTo($(target));
		
		if(opts.data){
			var data = null;
			var lorr = "left";
			var leftPos = {left:Origin_x-320,top:-150};
			var rightPos = {left:Origin_x+5,top:-130};
			for(var i=0; i<opts.data.length;i++){
				data = opts.data[i];
				if(!data)
					continue;
				var pos;
				if(i%2==0){
					lorr = "left";
					leftPos.top += dataItemHeight;
					pos = leftPos;
				}else{
					lorr = "right";
					rightPos.top += dataItemHeight;
					pos = rightPos;
				}
				createDataItem(target,"dataItem_"+i,lorr,pos,data).appendTo($(target));
			}
		}
	}
	
	/**
	 * 创建特定位置及样式的DIV
	 * @param className div样式名称
	 * @param pos 位置：{left:'',top:''}
	 * @param correctionWidth 修正left
	 * @return div
	 */
	function createDiv(className,pos,correctionLeft){
		var div = null;
		if(!correctionLeft)
			correctionLeft = 0;
		div = $('<div class="'+className+'"></div>');
		div.css({top:pos.top,left:pos.left-correctionLeft});
		return div;
	}
	/**
	 * 数据单元
	 * @id 
	 * @param lorr  left or right
	 * @param pos {left:'',top:''}
	 * @param data 数据对象
	 * @return 数据单元
	 */
	function createDataItem(target,id,lorr,pos,data){
		var dataItem = null;
		dataItem = $('<div id='+id+' class="leftItem" style="position:absolute;display:block;">');
		var topDiv = $('<div id=top_'+id+' class="top"> </div>');
		dataItem.append(topDiv);
		var middleDiv = $('<div id=middler_'+id+' class="middle"> </div>');
		dataItem.append(middleDiv);
		var bottomDiv = $('<div id=bottom_'+id+' class="bottom"> </div>');
		dataItem.append(bottomDiv);
		if(lorr == "left")
			dataItem.addClass("leftItem");
		else
			dataItem.addClass("rightItem");
		
		dataItem.css({left:pos.left,top:pos.top});
		//加载数据
		//topDiv单独处理，内容设置四块:左上、左下、右上、右下;根据floatstyle顺序填充。
		var fourDiv = $("<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"height:18px;\"><td></td><td></td></tr><tr style=\"height:18px;\"><td></td><td></td></tr></table>")
		topDiv.append(fourDiv);
		var opts = $.data(target, 'datapanel').options;
		var columns = null;
		if(opts.columns)
			columns = opts.columns;
		for(var i=0;i<columns.length;i++){
			var text = data[columns[i].field];
			//内容格式化
			if( columns[i].formatter){
				text = columns[i].formatter.call(columns[i],text,columns[i]);
			}
			
			if(text == null){
				if(typeof text == "undefined"){
					text = CusLang.EUIExt.datapanel.fieldConfigErrorMsg;
				}else{
					text = "";
				}
			}
			if(columns[i].position == "top"){
				addDataToTopDiv(topDiv,columns[i],text)
			}else if(columns[i].position == "middle"){
				addDaTaToDiv(middleDiv,columns[i],text)
			}else{
				addDaTaToDiv(bottomDiv,columns[i],text)
			}
		}
		
		return dataItem;
	}
	
	/**
	 * 数据加载到DIV：以行形式添加数据
	 * @param div 目标div
	 * @param column 列配置信息
	 * @param text 显示内容
	 */
	function addDaTaToDiv(div,column,text){
		var title = "";
		if(column.title){
			title = column.title+"：";
		}
		var float = "left";
		if(column.floatstyle && (column.floatstyle == "left" || column.floatstyle == "right"))
			float = column.floatstyle;
		var p = $("<p><span class='spanCls' style='"+column.titlestyle+"'>"+title+"</span><span class='spanCls' style='"+column.fieldstyle+"'>"+text+"</span></p>");
		div.append(p);
		p.css({textAlign:float,width:'100%'});
	}
	/**
	 * 添加数据到topDIv:以单元格的形式添加数据
	 * 内容设置四块:左上、左下、右上、右下;根据floatstyle顺序填充
	 * @param div 目标div
	 * @param column 列配置信息
	 * @param text 显示的内容
	 */
	function addDataToTopDiv(div,column,text){
		var title = "";
		if(column.title){
			title = column.title + ":";
		}
		var float = "left";
		if(column.floatstyle && (column.floatstyle == "left" || column.floatstyle == "right"))
			float = column.floatstyle;
		var p = "<p><span class='spanCls' style='" + column.titlestyle+"'>" + title + "</span><span class='spanCls' style='" + column.fieldstyle + "'>" + text + "</span></p>"
		div.find("td").each(function(i,td){
			if(float == "left"){
				if($(td).html() == "" && i%2 == 0){
					$(td).css({textAlign:float});
					$(td).html(p);
					return false;
				}
			}else{
				if($(td).html() == "" && i%2 == 1){
					$(td).css({textAlign:float});
					$(td).html(p);
					return false;
				}
			}
		});
	}
	/**
	 * 默认值配置
	 */
	$.fn.datapanel.defaults = {//默认属性定义
	    url : '',//
	    columns : '',//列信息
	    data : ''//json格式[{field1:'',field2:''}] field对应columns的field字段
	};

	/**
	 * 扩展方法
	 */
	$.fn.datapanel.methods = {
			reload : function(jq,url){
			return jq.each(function(){
				$.data(this,'datapanel').options.url = url;
				loadDataByUrl(this);
			});
		}
	};

	/**
	 * 解析data-options中数据
	 */
	$.fn.datapanel.parseOptions = function(target) {
		var t = $(target);
		return $.extend({},$.fn.panel.parseOptions(target),$.parser.parseOptions(target,["url","columns","data"]));//解析 data-options 中的初始化参数
	};
	
	$.parser.plugins.push('datapanel');
})(jQuery);