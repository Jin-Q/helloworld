echarts.js : 这是包含AMD加载器的echarts主文件，需要通过script最先引入

chart（文件夹） : echarts-optimizer通过依赖关系分析同时去除与echarts.js的重复模块后为echarts的每一个图表类型单独打包生成一个独立文件，根据应用需求可实现图表类型按需加载
	line.js : 折线图（如需折柱动态类型切换，require时还需要echarts/chart/bar）
	bar.js : 柱形图（如需折柱动态类型切换，require时还需要echarts/chart/line）
	scatter.js : 散点图
	k.js : K线图
	pie.js : 饼图（如需饼漏斗图动态类型切换，require时还需要echarts/chart/funnel）
	radar.js : 雷达图
	map.js : 地图
	force.js : 力导向布局图（如需力导和弦动态类型切换，require时还需要echarts/chart/chord）
	chord.js : 和弦图（如需力导和弦动态类型切换，require时还需要echarts/chart/force）
	funnel.js : 漏斗图（如需饼漏斗图动态类型切换，require时还需要echarts/chart/pie）
	gauge.js : 仪表盘
	eventRiver.js : 事件河流图
	treemap.js : 矩阵树图
	venn.js : 韦恩图

