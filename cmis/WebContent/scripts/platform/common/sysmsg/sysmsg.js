var lastTime;
$(function() {
	//默认不显示消息列表
	$("#sysmsg_unreadlist_div").hide();
	//默认不显示浮动消息
	$("#sysmsg_newmsg_div").hide();
	//绑定事件
	SysMsg.bindListeners();
	//加载数据
	SysMsg.timeLoad();
});

var SysMsg = function() {
	var msgTimeOut = null;
	lastTime = new Date();
	//消息重复加载时间，默认是10分钟如果系统设置了其更新时间(分钟)
	//则以系统设置时间为准
	var reloadTime = 10 * 60 * 1000;
	if (typeof (_sysmsgInterval) != "undefined" && _sysmsgInterval != "") {
		try {
			reloadTime = parseInt(_sysmsgInterval) * 60 * 1000;
		} catch (e) {
		}
	}
	/**
	 * 加载数据，并更新消息数量 列表 浮动消息等
	 */
	var loadMsgs = function() {
		$.ajax({
			type : "POST",
			dataType : "json",
			data : {},
			url : "queryUsrUnreadSysMsgList.do?EMP_SID=" + empId,
			success : function(data, textStatus, jqXHR) {
				bindMsg(data);
			}
		});
	};
	/**
	 * 根据消息内容绑定消息显示
	 * 更新未读消息数量
	 */
	var bindMsg = function(data) {
		// 更新未读消息数量
		if (data && data.total) {
			$("#msgNum").html(data.total);
		}
		// 更新消息列表
		if (data && data.rows) {
			$("#sysmsg_list_ul li").remove();
			for ( var i = 0; i < data.rows.length; i++) {
				var rowData = data.rows[i];
				$("#sysmsg_list_ul").append(
						'<li class="" targetId="' + rowData.id + '">' + (rowData.msg_type == '1' ? '【公告】' : '【消息】') + rowData.title + '</li>');
			}
		}
		// 更新浮动消息
		if (data && data.rows && data.rows.length > 0) {
			// 更新新消息的显示位置
			// 获取最新的一条
			var newData = data.rows[0];
			// 如果最新一条的发布时间晚于上次消息提醒的时间则添加新消息显示,否则隐藏消息提示
			var lastSendTime = parseSendTime(newData.send_time);

			if (lastSendTime && lastSendTime.getTime() >= lastTime.getTime()) {
				if(layout=='concise'){
					$("#scrollobj").empty();
					
					var rollMsg = '<a href="javascript:void(0)" targetId="' + newData.id + '"><img src="styles/mainpage/concise/default/images/pic_yes.png"/><span style="font-size:19px">'
                    + newData.title+'：'+newData.content + '</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>';
					$("#scrollobj").append(rollMsg);

                    $('#button_close').show();
                    $(".top_m").hide();
                    lastTime = lastSendTime;
				}else{
					if ($("#sysmsg_newmsg_div").is(":hidden")) {
						$("#sysmsg_newmsg_div").fadeIn();
					}
					$("#sysmsg_newmsg_content").html('<a href="javascript:void(0)" targetId="' + newData.id + '">' + newData.title+'：'+ newData.content + '</a>');
					lastTime = lastSendTime;
				}
			} else {
				$('#sysmsg_newmsg_div').fadeOut();
			}

		}
	};

	/**
	 * 日期字符串转为Date对象
	 * 支持的时间格式为yyyy-MM-dd HH:mm:ss.ms 
	 * 其中yyyy-MM-dd为必须有的数据
	 */
	var parseSendTime = function(strTime) {
		if (!strTime) {
			return null;
		}
		// 替换多空格为单个空格
		strTime = strTime.replace(/\s+/g, " ");
		var year = null, month = null, day = null, hours = null, minutes = null, seconds = null, ms = null;
		var dateAndTime = strTime.split(" ");
		// 日期yyyy-MM-dd解析
		var date = dateAndTime[0];
		var dateSpl = date.split("-");
		if (dateSpl.length < 3) {
			return null;
		}
		year = dateSpl[0];
		month = parseInt(dateSpl[1]) - 1;
		day = dateSpl[2];
		// 如果存在HH:mm:ss.ms则解析
		if (dateAndTime.length > 1) {
			var time = dateAndTime[1];
			var timeSpl = time.split(":");
			// 如果存在小时则解析
			if (timeSpl.length >= 1) {
				hours = timeSpl[0];
			}
			// 如果存在分钟则解析
			if (timeSpl.length >= 2) {
				minutes = timeSpl[1];
			}
			// 如果存在秒或者毫秒
			if (timeSpl.length == 3) {
				var ssms = timeSpl[2];
				var ssmsSpl = ssms.split("\.");
				// 秒
				seconds = ssmsSpl[0];
				// 如果存在毫秒则解析
				if (ssmsSpl.length > 1) {
					ms = ssmsSpl[1];
				}
			}
		}
		var newDate = new Date(year, month, day, hours, minutes, seconds, ms);
		return newDate;
	};
	return {
		/**
		 * 获取数据，并设定一段时间后重复执行此方法
		 */
		timeLoad : function() {
			// 存在未执行的定时，清空重新设定
			if (msgTimeOut) {
				clearTimeout(msgTimeOut);
			}
			loadMsgs();
			msgTimeOut = setTimeout("SysMsg.timeLoad()", reloadTime);
		},
		/**
		 * 绑定与消息操作有关的事件
		 */
		bindListeners : function() {
			// 点击消息展开消息内容
			$("#sysmsg_wrap_div").click(function() {
				if ($("#sysmsg_unreadlist_div").is(":hidden")) {
					$("#sysmsg_unreadlist_div").slideDown();
				} else {
					$("#sysmsg_unreadlist_div").slideUp();
				}
			});
			// 点击收缩箭头隐藏消息面板
			$("#sysmsg_shrink_div").click(function() {
				$("#sysmsg_unreadlist_div").slideUp();
			});
			// 点击更多查看所有消息列表
			$("#sysmsg_more_div").click(function() {
				$("#sysmsg_unreadlist_div").slideUp();
				SysMsg.openMorePage();
			});
			// 点击消息提醒中的一条消息，进行阅读
			$("#sysmsg_list_ul").on("click", "li", function() {
				var targetId = $(this).attr("targetId");
				SysMsg.readMessage(targetId);
			});
			// 点击新消息内容，阅读
			$("#sysmsg_newmsg_content").delegate("a", "click", function() {
				var thisFirst = $(this)[0];
				var targetId = $(thisFirst).attr("targetId");
				lastTime = new Date();
				SysMsg.readMessage(targetId);
			});
			// 新消息关闭
			$("#sysmsg_newmsg_close").click(function() {
				$('#sysmsg_newmsg_div').fadeOut();
				lastTime = new Date();
			});
		},
		/**
		 * 阅读消息 1.设置消息为已读 2.打开消息详情页
		 * 
		 * @param msgId 消息的id 不能为空
		 * @param readCallBack 消息设为已读后的回调函数
		 */
		readMessage : function(msgId, readCallBack) {
			// 设置消息公告为已读状态
			$.ajax({
				type : "POST",
				dataType : "json",
				data : {
					"msgId" : msgId
				},
				url : "addUsrSysMsgReadData.do?EMP_SID=" + empId,
				success : function(data, textStatus, jqXHR) {
					loadMsgs();
					if (readCallBack && (typeof (readCallBack) == "function")) {
						readCallBack(data, textStatus, jqXHR);
					}
				}
			});
			// 打开消息公告详情页
			var url = "getSSysMsgDetailPage.do?EMP_SID=" + empId + "&op=view&" + 'id=' + msgId;
			var ob = {
				title : '查看消息公告',
				url : url,
				width : 600,
				height : 300,
				draggable : true,
				modal : true,
				maximized : false
			};
			EMP.createwin(ob);
		},
		/**
		 * 打开更多页面
		 * 
		 * @param msgType 消息类型：0系统消息 1系统公告 不传获取全部
		 */
		openMorePage : function(msgType) {
			var url = "getUsrSSysMsgListPage.do?EMP_SID=" + empId;
			if (msgType) {
				url = "getUsrSSysMsgListPage.do?EMP_SID=" + empId + "&msgType=" + msgType;
			}
			var ob = {
				title : '消息公告',
				url : url,
				width : 800,
				height : 430,
				draggable : true,
				modal : true,
				maximized : false
			};
			EMP.createwin(ob);
		}
	};
}();

/***************concise风格**************************/
/**
 * 查看消息提醒
 */
function viewMsgDetail(obj){
	var _targetId = $(obj).attr("targetId");
	SysMsg.readMessage(_targetId);
	closeTipsa();
}
/**
 * 关闭走马灯效果
 */
function closeTipsa() {
    $('#button_close').fadeOut();
    $(".top_m").show();
    //关闭后不再显示
    lastTime = new Date();
}

/***************concise风格  end************************/