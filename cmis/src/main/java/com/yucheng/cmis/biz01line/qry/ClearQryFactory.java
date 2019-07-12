package com.yucheng.cmis.biz01line.qry;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.helpers.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.yucheng.cmis.biz01line.qry.job.ClearFileJob;

@SuppressWarnings("deprecation")
/**
 * 初始化调度清理综合查询缓存文件任务
 */
public class ClearQryFactory {
	public static void init() {
		try {
			ClearQryFactory.getInstance().startScheduler();
		} catch (SchedulerException e) {
			// TODO: handle exception
		}

	}

	private ClearQryFactory() {
	}

	private static ClearQryFactory instance;

	public static ClearQryFactory getInstance() {
		if (instance == null) {
			instance = new ClearQryFactory();
		}
		return instance;
	}

	public void startScheduler() throws SchedulerException {
		try {

			Scheduler scheduler = this.createScheduler();
			scheduler.start();
			this.createClearScheduler(scheduler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopScheduler() throws SchedulerException {
		try {

			Scheduler scheduler = this.createScheduler();
			scheduler.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void createClearScheduler(Scheduler scheduler)
			throws SchedulerException {
		JobDetail jobDetail = new JobDetail("ClearFileJob",
				Scheduler.DEFAULT_GROUP, ClearFileJob.class);
		// 每天上午5点 删除综合查询记录
		 Trigger trigger = TriggerUtils.makeDailyTrigger(5, 0);
		// Trigger trigger=TriggerUtils.makeMinutelyTrigger();
	//	Trigger trigger = TriggerUtils.makeSecondlyTrigger(5);
		trigger.setName("ClearFileJob");
		trigger.setStartTime(new Date());
		scheduler.scheduleJob(jobDetail, trigger);
	}

	public Scheduler createScheduler() throws SchedulerException {
		return StdSchedulerFactory.getDefaultScheduler();
	}
}
