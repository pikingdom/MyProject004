package com.nd.hilauncherdev.analysis.integral;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nd.hilauncherdev.net.ResultCodeMap;
import com.nd.hilauncherdev.net.ServerResultHeader;
import com.nd.hilauncherdev.net.ThemeHttpCommon;
import com.nd.hilauncherdev.shop.api6.net.ServerDetailResult;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 会员积分访问网路的基础接口
 * <p>
 * Title: MemberintegralNetOptApiBase
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: ND
 * </p>
 * 
 * @author MaoLinnan
 * @date 2015年10月21日
 */
public class MemberintegralNetOptApiBase {
	public final static String Panda_Space_Inland_Server = "http://mri.ifjing.com/";

	/**
	 * 基础接口(1-1000)
	 * 
	 * @param actionCode
	 * @return
	 */
	public static String getCommonActionUrl(int actionCode) {
		return Panda_Space_Inland_Server + "action/commonaction/" + actionCode;
	}

	/**
	 * 2、接收完成任务请求
	 * 
	 * @param ctx
	 * @return
	 */
	public static ServerDetailResult<IntegralInfoBean> getTaskInformationList_2(Context ctx, String taskGuid) {
		if (TextUtils.isEmpty(taskGuid)){
			return null;
		}
		int acitonCode = 2;
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		String jsonParams = "";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("TaskGuid", taskGuid);
			String pandaId = getUserpandaUid(ctx);
			if (!TextUtils.isEmpty(pandaId)){
				jsonObject.put("PandaUid", pandaId);
			}
			jsonParams = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThemeHttpCommon.addMemberIntegralGlobalRequestValue(paramsMap, ctx.getApplicationContext(), jsonParams);
		ThemeHttpCommon httpCommon = new ThemeHttpCommon(getCommonActionUrl(acitonCode));
		ServerResultHeader csResult = httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
		ServerDetailResult<IntegralInfoBean> sr = new ServerDetailResult<IntegralInfoBean>();
		if (csResult != null) {
			sr.setCsResult(csResult);
			if (sr.getCsResult().isRequestOK()) {
				String responseJson = sr.getCsResult().getResponseJson();
				try {
					JSONObject responseObj = new JSONObject(responseJson);
					IntegralInfoBean bean = new IntegralInfoBean();
					bean.getIntegrals = responseObj.optInt("GetIntegrals");
					bean.taskType = responseObj.optInt("TaskType");
					bean.getGrowups = responseObj.optInt("GetGrowups");
					bean.themeTickets = responseObj.optInt("ThemeTickets");
					bean.ticketValidDays = responseObj.optInt("TicketValidDays");
					bean.redPackageName = responseObj.optString("RedPackageName");
					sr.detailItem = bean;
				} catch (Exception ex) {
					sr.getCsResult().setResultCode(ResultCodeMap.SERVER_RESPONSE_CODE_8800);
					ex.printStackTrace();
				}
			}
		}
		return sr;
	}
	
	/**
	 * 保存用户id
	 * <p>Title: saveUserId</p>
	 * <p>Description: </p>
	 * @author maolinnan_350804
	 */
	public static void saveUserId(Context context,String userId){
		if (context == null || userId == null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences("misp", Context.MODE_PRIVATE | 4);
		sp.edit().putString("miid", userId).commit();
	}
	/**
	 * 获取用户ID
	 * <p>Title: getUserId</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	public static String getUserId(Context context){
		SharedPreferences sp = context.getSharedPreferences("misp", Context.MODE_PRIVATE | 4);
		return sp.getString("miid", "");
	}
	
	/**
	 * 保存用户pandaUid
	 * <p>Title: saveUserId</p>
	 * <p>Description: </p>
	 * @author maolinnan_350804
	 */
	public static void saveUserpandaUid(Context context,String pandaUid){
		if (context == null || pandaUid == null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences("misp", Context.MODE_PRIVATE | 4);
		sp.edit().putString("mipid", pandaUid).commit();
	}
	/**
	 * 获取用户pandaId
	 * <p>Title: getUserId</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	public static String getUserpandaUid(Context context){
		SharedPreferences sp = context.getSharedPreferences("misp", Context.MODE_PRIVATE | 4);
		return sp.getString("mipid", "");
	}
}
