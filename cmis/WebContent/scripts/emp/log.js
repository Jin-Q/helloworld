if (!EMP.util.Logger) {

	EMP.util.Logger = {
	
		levelStr : ['DEBUG','TRACE','INFO','WARNING','ERROR'],
		
		log : function(type, level, msg) {

			firebug.d.console.log(msg);
		/*	var logDiv = document.getElementById('emp_log_div');
			if (logDiv==null) {
				logDiv = EMP.util.Logger.drawWindow(document);
			}
			
			logDiv.innerHTML += type +"&nbsp;"+ EMP.util.Logger.levelStr[level] 
						+"&nbsp;"+ msg +"<br>";*/
		},
		
		drawWindow : function(doc) {
		
			var div = doc.createElement('div');
			div.id = "emp_log_div";
			document.body.appendChild(div);
			return div;
		}
	};
	
}