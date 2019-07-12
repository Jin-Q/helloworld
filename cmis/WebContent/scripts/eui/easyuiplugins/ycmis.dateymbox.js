/**
 * <p>easyui扩展组件：dateymbox（年月输入框）</p>
 * <p>dateymbox扩展自combo用于年月的录入</p>
 * @author wangbin 2014-3-10 17:08:49
 * <p>修改记录：</p>
 * <p>1.在初始化时进行提前赋值，避免在初始化时触发onchange事件。add by zangys at 2014-7-28 11:48:56</p>
 * <p>2.增加options方法。add by zangys at 2014-11-4 16:53:32</p>
 * <p>3.增加setValue方法，修改手动输入时设置年月值。add by zangys at 2015-04-23</p>
 * <p>4.增加onSelect事件，在年月选择之后执行。add by liwei at 2016-07-15</p>
 */
(function(){
	$.fn.dateymbox = function(options, param){
		if(typeof options=="string"){
			var fun=$.fn.dateymbox.methods[options];
			if(fun){
			return fun(this,param);
			}
			}
			options=options||{};
		return this.each(function(){
			options = options || {};
			var state = $.data(this, 'dateymbox');
			
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'dateymbox', {
					options: $.extend({}, $.fn.dateymbox.defaults, $.fn.dateymbox.parseOptions(this), options)
				});
			}
			createdateymbox(this);
		});
		
	};

	/**
	 * 由于dateymbox扩展自combo，这里用于创建combo面板（用于存放后面创建的年月信息），同时进行默认值的设置
	 */
	function createdateymbox(target){
		var state = $.data(target, 'dateymbox');
		var opts = state.options;

		var val = $(target).val();
		var date=opts.parser(val);
		opts.year=date.getFullYear();
		opts.month=date.getMonth()+1;
		
		$(target).combo($.extend({}, opts, {
			onShowPanel:function(){
				opts.onShowPanel.call(target);
			}
		}));
		$(target).combo('textbox').parent().addClass('datebox');
		
		if (!state.calendar){
			createCalendar();
		}
		
		var month=opts.month<10?'0'+opts.month:opts.month;
		var value=opts.formatter(opts.year,month);
		//采用提前赋值的方式，避免在初始化时触发onchange事件
		$.data(target,'combo').combo.find('.combo-value').val(value);
		
		moveTo(target);

		/**
		 * 创建年月面板，用于展示月份信息
		 * 
		 */
		function createCalendar(){
			
			var panel = $(target).combo('panel');
			panel.empty();
			var inner = $('<div class="calendar-noborder"></div>').appendTo(panel).wrap('<div class="datebox-calendar-inner"></div>');
			$('<div class="calendar-header">' +
					'<div class="calendar-prevyear"></div>' +
					'<div class="calendar-nextyear"></div>' +
					'<div class="calendar-title">' +
						'<span>'+opts.year+'-'+opts.month+'</span>' +
					'</div>' +
				'</div>' +
				'<div class="calendar-body">' +
					'<div class="calendar-menu" style=\"width:150px;\">' +
						'<div class="calendar-menu-month-inner">xx' +
						'</div>' +
					'</div>' +
				'</div>'
			).appendTo(inner);

			
			$(inner).find('.calendar-menu').show();
			var menu=$(inner).find(".calendar-menu-month-inner");
			menu.empty();
			var t = $('<table></table>').appendTo($(inner).find('.calendar-menu-month-inner'));
			var idx = 0;
			for(var i=0; i<4; i++){
				var tr = $('<tr></tr>').appendTo(t);
				for(var j=0; j<3; j++){
					$('<td class="calendar-menu-month"></td>').html(opts.months[idx++]).attr('abbr',idx).appendTo(tr);
				}
			}
			
			menu.find("td.calendar-selected").removeClass("calendar-selected");
			menu.find("td:eq("+(opts.month-1)+")").addClass("calendar-selected");

			var button = $('<div class="datebox-button"></div>').appendTo(panel);
			
			$('<a href="javascript:void(0)" class="datebox-close"></a>').html(opts.closeText).appendTo(button);
			button.find('.datebox-close').hover(
					function(){$(this).addClass('datebox-button-hover');},
					function(){$(this).removeClass('datebox-button-hover');}
			);

			button.find('.datebox-close').click(function(){
				$(target).combo('hidePanel');
			});

			$(inner).find('.calendar-nextyear').click(function(){
				var title = $(this).parent().find('.calendar-title');
				opts.year= opts.year*1+1;
				$(title).empty().append('<span>'+opts.year+'-'+opts.month+'</span>');
			});
			$(inner).find('.calendar-prevyear').click(function(){
				var title = $(this).parent().find('.calendar-title');
				opts.year= opts.year*1-1;
				$(title).empty().append('<span>'+opts.year+'-'+opts.month+'</span>');
			});
			$(inner).find('.calendar-menu-month').hover(
					function(){$(this).addClass('calendar-menu-hover');},
					function(){$(this).removeClass('calendar-menu-hover');}
			).click(function(){
				var menu = $(this).parent().parent();
				var month = $(this).attr("abbr");
				opts.month = month;
				moveTo(target,false,"select");
			});
			
		}
	}
	//选中后逻辑处理：添加选中样式、title修改等。
	function moveTo(target,type,from){
		var inner = $(target).combo('panel');
		var menu=$(inner).find('.calendar-menu');
		var opts = $.data(target, 'dateymbox').options;
		menu.find('.calendar-selected').removeClass('calendar-selected');
		menu.find("td:eq("+(opts.month-1)+")").addClass("calendar-selected");
		var month=opts.month<10?'0'+opts.month:opts.month;
		var value=opts.formatter(opts.year,month);
		if(type == undefined || type == false){
			$(target).combo('setValue', value).combo('setText', value);
			$(target).combo('hidePanel');
		}
		var title = $(inner).find('.calendar-title');
		$(title).empty().append('<span>'+opts.year+'-'+month+'</span>');
		if("select"==from){
		    if(opts.onSelect){
		        opts.onSelect.call(this,value);
		    }
		}
	}
	//格式化输入值
	function parseValue(target,type){
		var opts = $.data(target, 'dateymbox').options;
		var val=$(target).combo('getText');
		var arr = val.split('-');
		var s = "";
		if(arr.length>1){
			s = arr[0];
			var intmonth = parseInt(arr[1]);
			var smonth = intmonth<10?'0'+ intmonth: intmonth;
			s += "-" + smonth;
		}
		
		var date=opts.parser(s);
		opts.year=date.getFullYear();
		opts.month=date.getMonth()+1;
		moveTo(target,type);
	}
	
	function _a(_b,q){
		_c(_b,q);
	};
	
	function _c(_11,_12){
		var _13=$.data(_11,"dateymbox");
		var _14=_13.options;
		$(_11).combo("setValue",_12).combo("setText",_12);
		parseValue(_11,true);
	};

	$.fn.dateymbox.methods = {
		options:function(jq){
			return $.data(jq[0],"dateymbox").options;
		},
		setValue:function(jq,_1a){
			return jq.each(function(){
				_c(this,_1a);
			});
		}
	};

	$.fn.dateymbox.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.fn.combo.parseOptions(target), {
		});
	};
	/**
	 * dateymbox默认属性扩展自combo
	 */
	$.fn.dateymbox.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth:133,
		panelHeight:'auto',
		closeText:'关闭',
		months:$.fn.calendar.defaults.months,
		year:new Date().getFullYear(),
		month:new Date().getMonth()+1,
		keyHandler:{up:function(){
		},down:function(){
		},enter:function(){
			//parseValue(this);
		},query: function(q){
			_a(this,q);
		}
		},
		formatter:function(y,m){
			return y+"-"+m;
		},
		parser:function(s){
			var t=Date.parse(s);
			if(!isNaN(t)){
			return new Date(t);
			}else{
			return new Date();
			}			
			/*
			if (!s) return new Date();
			var y,m;
			if(s.length>=7){
				y=parseInt(s.substring(0, 4),10);
				m = parseInt(s.substring(5),10);
			}else if(s.length==6){
				y=parseInt(s.substring(0, 4),10);
				m = parseInt(s.substring(4),10);
			}else if(s.length==5){
				y=parseInt(s.substring(0, 4),10);
				m = parseInt(s.substring(4),10);
			}
			
			if (!isNaN(y) && !isNaN(m) ){
				return new Date(y,m-1,1);
			} else {
				return new Date();
			}
			*/
		},
		onSelect:function(val){
		    
		}
	});
})(jQuery);