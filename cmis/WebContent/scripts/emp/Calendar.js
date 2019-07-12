

	function Calendar(objName) {
		this.style = {
			borderColor       		: "#909eff", //边框颜色
			headerBackColor    		: "#909EFF", //表头背景颜色
			headerFontColor    		: "#ffffff", //表头字体颜色
			bodyBarBackColor  		: "#f4f4f4", //日历标题背景色
			bodyBarFontColor  		: "#000000", //日历标题字体色
			bodyBackColor     		: "#ffffff", //日历背景色
			bodyFontColor            : "#000000", //日历字体色 
			bodyHolidayFontColor     : "#ff0000", //假日字体色
			watermarkColor 		    : "#d4d4d4",  //背景水印色
			moreDayColor             : "#cccccc" 
		};
		this.showMoreDay = false; //是否显示上月和下月的日期
		this.Obj = objName;		
		this.date = null;
		this.mouseOffset = null;
		this.dateInput = null;
		this.timer = null;	
	};

	Calendar.prototype.toString = function() {   
		var str = '<iframe id="Calendar_iframe" style="background:#c00; color:#FFFFFF; border:none; position:absolute; z-index:9998;" ></iframe>'
		str += '<div Author="alin" class="calendar" style="display:none;" onselectstart="return false" oncontextmenu="return false" id="Calendar">\n';
		str += '<div Author="alin" class="cdrWatermark" id="cdrWatermark"></div><div id="cdrBody" style="position:absolute;left:0px;top:0px;z-index:2;width:140px;">';
		str += this.getHeader();
		str += this.getBody();   
		str += '</div><div Author="alin" id="cdrMenu" style="position:absolute;left:0px;top:0px;z-index:3;display:none;"  onmouseover="' + this.Obj + '.showMenu(null);" onmouseout="' + this.Obj + '.hideMenu();"></div></div>';
		return str;
	};

	Calendar.prototype.getHeader = function() {
		var str = '<table Author="alin" class="cdrHeader" cellSpacing="2" cellPadding="0"><tr Author="alin" align="center">\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" onmouseout="this.className=\'\'" id="previousYear" title="上一年份" style="cursor:pointer;width:10px;" onclick="'+this.Obj+'.onChangeYear(false);"><<</td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" onmouseout="this.className=\'\'" id="previousMonth" title="上一月份" style="cursor:pointer;width:10px;" onclick="'+this.Obj+'.onChangeMonth(false);"><</td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" id="currentYear" style="width:50px;" onclick="' + this.Obj + '.showMenu(true);" onmouseout="' + this.Obj + '.hideMenu();this.className=\'\';">0</td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" id="currentMonth" onclick="' + this.Obj + '.showMenu(false);" onmouseout="' + this.Obj + '.hideMenu();this.className=\'\';">0</td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" onmouseout="this.className=\'\'" id="nextMonth" title="下一月份" style="cursor:pointer;width:10px;" onclick="'+this.Obj+'.onChangeMonth(true);">></td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'headerOver\'" onmouseout="this.className=\'\'" id="nextYear" title="下一年份" style="cursor:pointer;width:10px;" onclick="'+this.Obj+'.onChangeYear(true);">>></td></tr>\n';
		str += '</table>\n';
		return str;
	};

	Calendar.prototype.getBody = function() {
		var n = 0;
		var str = this.getBodyBar();
		str += '<table Author="alin" class="cdrBody" cellSpacing="2" cellPadding="0">\n';   
		for(i = 0; i < 6; i++) {	  
			str += '<tr Author="alin" align="center">';
			for(j = 0; j < 7; j++) {
				str += '<td Author="alin" class="dayOut" id="cdrDay'+(n++)+'" width="13%"></td>\n';
			}
			str += '</tr>';
		}
		str += '</table>\n';
		str += '<table Author="alin" class="cdrBodyBar" cellSpacing="2" cellPadding="0"><tr align="center" Author="alin"><td Author="alin" style="cursor:pointer;" onclick="'+this.Obj+'.getToday();">今天：'+new Date().toFormatString("yyyy年MM月dd日")+'</td></tr></table>\n';
		return str;
	};
	
	Calendar.prototype.getBodyBar = function() {
		var str = '<table Author="alin_bar" id="cdrBodyBar" class="cdrBodyBar" cellSpacing="2" cellPadding="0"><tr Author="alin_bar" align="center">\n';
		var day = new Array('日','一','二','三','四','五','六');
		for(i = 0; i < 7; i++) {
			str += '<td Author="alin_bar">' + day[i] + '</td>\n';     
		}
		str += '</tr></table>';
		return str;  
	};
	
	Calendar.prototype.getYearMenu = function(year) {
		var str = '<table Author="alin" cellSpacing="0" class="cdrMenu" cellPadding="0">\n';
		for(i = 0; i < 10; i++) {	  
			var _year = year + i;
			var _date = new Date(_year,this.date.getMonth(),this.date.getDate());
			str += '<tr Author="alin" align="center"><td Author="alin" width="13%" height="16" ';
			if(this.date.getFullYear() != _year) {
				str += 'onmouseover="this.className=\'menuOver\'" onmouseout="this.className=\'\'" ';
			}
			else {
				str += 'class="menuOver"';
			}
			str += 'onclick="' + this.Obj + '.bindDate(\'' + _date.toFormatString("-") + '\')">' + _year + '年</td>\n';		
			str += '</tr>';
		}
		str += '<tr Author="alin" align="center"><td Author="alin"><table Author="alin" style="font-size:12px;width:100%;" cellSpacing="0" cellPadding="0">\n';
		str += '<tr Author="alin" align="center"><td Author="alin" onmouseover="this.className=\'menuOver\'" onmouseout="this.className=\'\'" onclick="'+this.Obj+'.getYearMenu('+ (year - 10) + ')"><<</td>\n';
		str += '<td Author="alin" onmouseover="this.className=\'menuOver\'" onmouseout="this.className=\'\'" onclick="'+this.Obj+'.getYearMenu('+ (year + 10) +')">>></td><tr>\n';
		str += '</table></td></tr>\n';
		str += '</table>';
		var _menu = document.getElementById("cdrMenu");
		_menu.innerHTML = str;
	};
		
	Calendar.prototype.getMonthMenu = function() {
		var str = '<table Author="alin" cellSpacing="0" class="cdrMenu" cellPadding="0">\n';
		for(i = 1; i <= 12; i++) {   
			var _date = new Date(this.date.getFullYear(),i-1,this.date.getDate());		
			str += '</tr><tr Author="alin" align="center"><td Author="alin" height="16" ';
			if(this.date.getMonth() + 1 != i) {
				str += 'onmouseover="this.className=\'menuOver\'" onmouseout="this.className=\'\'" ';
			}
			else {
				str += 'class="menuOver"';
			}
			str += 'onclick="' + this.Obj + '.bindDate(\'' + _date.toFormatString("-") + '\')">'+i+'月</td></tr>\n';
		}
		str += '</table>';
		var _menu = document.getElementById("cdrMenu");
		_menu.innerHTML = str;   
	};
	
	Calendar.prototype.show = function(){
		if (arguments.length >  3  || arguments.length == 0){
			alert("对不起！传入参数不对！" );
			return;
		}   
		var _date = null;
		var _evObj = null;
		var _initValue = null  
		for(i = 0; i < arguments.length; i++) {
			if(typeof(arguments[i]) == "object" && arguments[i].type == "text") {
				_date = arguments[i];
			} else if(typeof(arguments[i]) == "object") {	
				_evObj = arguments[i];
			} else if(typeof(arguments[i]) == "string") {
				_initValue = arguments[i];
			}  
		}
		_evObj = _evObj || _date;
		inputObj = _date;
		targetObj = _evObj
		if(!_date) {
			alert("传入参数错误!"); 
			return;
		}
		this.dateInput = _date;
		_date = _date.value;
		if(_date == "" && _initValue) 
			_date = _initValue;   
		this.bindDate(_date);        
		var _target = getPosition(_evObj);   
		var _obj = document.getElementById("Calendar");
		var _obj_iframe = document.getElementById("Calendar_iframe"); 
		_obj.style.display = ""; 
		_obj_iframe.style.display = "";
		_obj.style.left = _target.x + 'px';
		_obj_iframe.style.left = _target.x + 'px';
		if((document.body.clientHeight - (_target.y + _evObj.clientHeight)) >= _obj.clientHeight) {        
			_obj.style.top = (_target.y + _evObj.clientHeight) + 'px';
			_obj_iframe.style.top = (_target.y + _evObj.clientHeight) + 'px';
		}
		else {	  
			_obj.style.top = (_target.y - _obj.clientHeight) + 'px';
			_obj_iframe.style.top = (_target.y - _obj.clientHeight) + 'px';
		}
	};
		
	Calendar.prototype.hide = function() {
		var obj = document.getElementById("Calendar");
		obj.style.display = "none"; 
		var ci = document.getElementById("Calendar_iframe");
		ci.style.display = "none";  
	};
		
	Calendar.prototype.bindDate = function(date) {
		var _monthDays = new Array(31,30,31,30,31,30,31,31,30,31,30,31);	
		var _arr = date.split('-');		
		var _date = new Date(_arr[0],_arr[1]-1,_arr[2]);	
		if(isNaN(_date)) 
			_date = new Date();	
		this.date = _date;
		this.bindHeader();	
		var _year = _date.getFullYear();
		var _month = _date.getMonth();
		var _day = 1;	
		var _startDay = new Date(_year,_month,1).getDay();
		var _previYear = _month == 0 ? _year - 1 : _year;
		var _previMonth = _month == 0 ? 11 : _month - 1;
		var _previDay = _monthDays[_previMonth];
		if (_previMonth == 1) 
			_previDay =((_previYear%4==0)&&(_previYear%100!=0)||(_previYear%400==0))?29:28;	
		_previDay -= _startDay - 1;
		var _nextDay = 1;
		_monthDays[1] = ((_year%4==0)&&(_year%100!=0)||(_year%400==0))?29:28;
		for(i = 0; i < 40; i++) {	
			var _dayElement = document.getElementById("cdrDay" + i);
			_dayElement.onmouseover = Function(this.Obj + ".onMouseOver(this)");
			_dayElement.onmouseout = Function(this.Obj + ".onMouseOut(this)");
			_dayElement.onclick = Function(this.Obj + ".onClick(this)");
			this.onMouseOut(_dayElement);	 		
			_dayElement.style.color = "";
			if(i < _startDay) {
				//获取上一个月的日期
				if(this.showMoreDay) {
					var _previDate = new Date(_year,_month - 1,_previDay);
					_dayElement.innerHTML = _previDay;
					_dayElement.title = _previDate.toFormatString("yyyy年MM月dd日");
					_dayElement.value = _previDate.toFormatString("-");	
					_dayElement.style.color = this.style.moreDayColor;	
					_previDay++;
				}else {
					_dayElement.innerHTML = "";
					_dayElement.title = "";
				}
			}
			else if(_day > _monthDays[_month]) {
				//获取下个月的日期
				if(this.showMoreDay) {
					var _nextDate = new Date(_year,_month + 1,_nextDay);
					_dayElement.innerHTML = _nextDay;
					_dayElement.title = _nextDate.toFormatString("yyyy年MM月dd日");
					_dayElement.value = _nextDate.toFormatString("-");
					_dayElement.style.color = this.style.moreDayColor;	
					_nextDay++;			   
				}else {
					_dayElement.innerHTML = "";
					_dayElement.title = "";
				}
			}
			else if(i >= new Date(_year,_month,1).getDay() && _day <= _monthDays[_month]) {
				//获取本月日期
				_dayElement.innerHTML = _day;
				if(_day == _date.getDate()) {
					this.onMouseOver(_dayElement);
					_dayElement.onmouseover = Function("");   
					_dayElement.onmouseout = Function(""); 					  			    
				}
				if(this.isHoliday(_year,_month,_day)) {
					_dayElement.style.color = this.style.bodyHolidayFontColor;			  
				}
				var _curDate = new Date(_year, _month, _day);
				_dayElement.title =  _curDate.toFormatString("yyyy年MM月dd日");
				_dayElement.value = _curDate.toFormatString("-");
				_day++;
			}
			else {
				_dayElement.innerHTML = "";
				_dayElement.title = "";
			}	
		}
		var _menu = document.getElementById("cdrMenu");
		_menu.style.display = "none";	
	};
	
	Calendar.prototype.bindHeader = function() {
		var _curYear = document.getElementById("currentYear");
		var _curMonth = document.getElementById("currentMonth");
		var _watermark = document.getElementById("cdrWatermark");
		_curYear.innerHTML = this.date.toFormatString("yyyy年");
		_curMonth.innerHTML =  this.date.toFormatString("MM月");
		_watermark.innerHTML = this.date.getFullYear();     
	};	
		
	Calendar.prototype.getToday = function() {
		var _date = new Date();
		this.bindDate(_date.toFormatString("-"));
	};	
	
	Calendar.prototype.isHoliday = function(year,month,date) {
		var _date = new Date(year,month,date);
		return (_date.getDay() == 6 || _date.getDay() == 0);
	};
	
	Calendar.prototype.onMouseOver = function(obj) {
		obj.className = "dayOver";
	};
	
	Calendar.prototype.onMouseOut = function(obj) {
		obj.className = "dayOut";
	};	
	
	Calendar.prototype.onClick = function(obj) {  
		if(obj.innerHTML != "")  this.dateInput.value = obj.value;
		this.hide();
		this.dateInput.focus();
	};
		
	Calendar.prototype.onChangeYear = function(isnext) {
		var _year = this.date.getFullYear();
		var _month = this.date.getMonth() + 1;
		var _date = this.date.getDate();
		if(_year > 999 && _year <10000) {
			if(isnext){_year++;}else{ _year --;}
		}
		else {
			alert("年份超出范围（1000-9999）!");
		}
		this.bindDate(_year + '-' + _month + '-' + _date);
	};
	
	Calendar.prototype.onChangeMonth = function(isnext) {
		var _year = this.date.getFullYear();
		var _month = this.date.getMonth() + 1;
		var _date = this.date.getDate();
		if(isnext) { 
			_month ++;
		} else {
			_month--;
		}
		if(_year > 999 && _year <10000) { 
			if(_month < 1) {_month = 12; _year--;}
			if(_month > 12) {_month = 1; _year++;}
		} else {
			alert("年份超出范围（1000-9999）!");
		}  
		this.bindDate(_year + '-' + _month + '-' + _date);
	};
	
	Calendar.prototype.showMenu = function(isyear) {
		var _menu = document.getElementById("cdrMenu");
		if(isyear != null) {    
			var _obj = (isyear)? document.getElementById("currentYear") : document.getElementById("currentMonth");
			if(isyear) {
				this.getYearMenu(this.date.getFullYear() - 5);	   
			} else {
				this.getMonthMenu();	   
			}
			_menu.style.top = (_obj.offsetTop + _obj.offsetHeight) + 'px';
			_menu.style.left = _obj.offsetLeft + 'px';	
			_menu.style.width = _obj.offsetWidth + 'px';
		}
		if (this.timer != null) 
			clearTimeout(this.timer);
		_menu.style.display="";
	};
	
	Calendar.prototype.hideMenu = function() {
		var _obj = document.getElementById("cdrMenu");
		this.timer = window.setTimeout(function(){_obj.style.display='none';},500);	
	};
	
	Number.prototype.NaN0 = function() {
		return isNaN(this) ? 0 : this;
	};
			
	Date.prototype.toFormatString = function(fs){
		
		var yyyy = ""+this.getFullYear();
		var month = this.getMonth()+1;
		var M = ""+month;
		var MM = month<10?"0"+M:M;
		var date = this.getDate();
		var d = ""+date;
		var dd = date<10?"0"+d:d;
		
		if(fs.length == 1) { 
			return yyyy + fs + MM + fs + dd; 
		}
		
		fs = fs.replace("yyyy",yyyy);
		fs = fs.replace("MM",MM);
		fs = fs.replace("M",M);
		fs = fs.replace("dd",dd);
		fs = fs.replace("d",d);
		return fs;
	};
	




/************公用方法及变量**************/
	var inputObj = null; 
	var targetObj = null;	
	
	function getPosition(e) {
		var left = 0;
		var top  = 0;
		while (e.offsetParent){
			left += e.offsetLeft + (e.currentStyle?(parseInt(e.currentStyle.borderLeftWidth)).NaN0():0) - e.scrollLeft;
			top  += e.offsetTop  + (e.currentStyle?(parseInt(e.currentStyle.borderTopWidth)).NaN0():0) - e.scrollTop;
			e     = e.offsetParent;
		}
		left += e.offsetLeft + (e.currentStyle?(parseInt(e.currentStyle.borderLeftWidth)).NaN0():0) - e.scrollLeft;
		top  += e.offsetTop  + (e.currentStyle?(parseInt(e.currentStyle.borderTopWidth)).NaN0():0);// - e.scrollTop;
		return {x:left, y:top};
	};
	
	
	function closeCalendar(evt){
		var c = document.getElementById("Calendar");
		if (!c) return;
		evt = evt || window.event; 
		var _target= evt.target || evt.srcElement; 
		if(!_target.getAttribute("Author") &&  _target != inputObj && _target != targetObj)	{
			c.style.display = "none"; 	  
			var ci = document.getElementById("Calendar_iframe");
			ci.style.display = "none";
		}
	};

/***********End 公用方法*********/
	document.onclick = closeCalendar;

/*********结束**********/
