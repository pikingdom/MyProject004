package com.nd.hilauncherdev.analysis.integral;
/**
 * 积分服务端反馈信息bean
 * <p>Title: IntegralInfoBean</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2016年11月22日
 */
public class IntegralInfoBean {
	public int getIntegrals;//获得的积分数
	public int getGrowups;//获得的成长值
	public int taskType;//任务名称类型
	//组成红包元素
	public int themeTickets;//获得的红包主题券数量
	public int ticketValidDays;//红包主题券有效天数
	public String redPackageName;//红包名称
}
