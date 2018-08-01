package com.nd.hilauncherdev.app;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.net.ThemeHttpCommon;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerService;
/**
 * 软件详情javabean
 * <p>Title: SoftDetialBean</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2014年12月12日
 */
public class SoftDetialBean {
//	private SoftDetialBean(){}//封闭构造方法
	private static final String SOFT_DETIA_REQUEST_URL = "http://ressearch.ifjing.com/service.ashx?act=226&identifier=%s&pact=203&placeid=1000062&iv=7&pos=250101&&osv=4.1";
	
	
	private String key;
	// ID
	public int resId;
	// 名称
	public String resName;
	// 类型
	public String category;
	// 描述
	public String descript;
	// 版本
	public String version;
	// 版本号
	public String versionCode;
	// 标识
	public String identifier;
	// 推荐指数
	public int star;
	// 类别ID
	public int cid;
	// 类别
//	public String cname;
	// 长度
	public String size;
	// 语言
	public String lan;
	// 作者
	public String author;
	// 下载数
	public String downloadNumber;
	// 链接地址
	public String downloadUrl;
	// 图标地址
	public String iconUrl;
	// 价格
	public String price;
	// 浏览次数
//	public int viewNumber;
	// 系统需求
	public String fwRequest;
	// 系统需求
	public boolean fwCompliant;
	// 上传者
	public String sharer;
	// 截图
	public ArrayList<String> snapshots = new ArrayList<String>();
	// 警告信息
	public boolean warn;
	//更新信息
	public String updateInfo;
	//更新时间
	public String updateTime;
	//应用类型
	public String appType;
	// 积分
	public int score ;	
	//安全信息
	public WarnBean safeInfo;
	//软件所属类型
	public int speciesType;
	//智能更新大小
//	public String incSize;
	public int sp = -1;
	
	public String getDescript()	{
		if (null == descript) {
			return "";
		}
		return descript;
	}
	/**
	 * 获取下载标示，与appmarketitem中一致
	 * <p>Title: getKey</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	public String getKey() {
		if(!TextUtils.isEmpty(key))
			return key;
		
		key=DownloadServerService.RECOMMEND_PREFIX + identifier;
		if(TextUtils.isEmpty(key))
			key=downloadUrl;
		
		return key;
	}
	
	public void initDataFormJSONObject(JSONObject object) throws JSONException {
		resId = object.getInt("resId");
		resName = object.getString("resName");
		descript = object.getString("desc");
		identifier = object.getString("identifier");
		author = object.getString("author");
		version = object.getString("version");
		versionCode = object.getString("versionCode");
		category = object.getString("category");
		cid = object.getInt("cateId");
		lan = object.getString("lan");
		size = object.getString("size");
		star = object.getInt("star");
		iconUrl = object.getString("icon");
		downloadUrl = object.getString("downloadUrl");
		fwRequest = object.getString("fw_Requirement");
		fwCompliant = object.getBoolean("fw_Compliant");
		price = object.getString("price");
		downloadNumber = object.getString("downloadNumber");
		sharer = object.getString("sharer");
		warn  = object.optBoolean("warn");
		updateInfo = object.optString("updateInfo");
		updateTime = object.optString("updateTime");
		appType = object.optString("markType");
		score = object.optInt("score");
		speciesType = object.optInt("speciesType");
		JSONArray array = object.getJSONArray("snapshots");
		if (array != null) {
			int count = array.length();
			for (int index = 0; index < count; index++) {
				String content = array.getString(index);
				snapshots.add(content);
			}
		}
		
		JSONObject safeInfoObj= object.optJSONObject("safeInfo");
		if(safeInfoObj != null) {
			safeInfo = new WarnBean();
			safeInfo.initDataFormJSONObject(safeInfoObj);
		}
	}
	
	/**
	 * 通过包名，获取详情信息
	 * <p>Title: getSoftDetialBean</p>
	 * <p>Description: </p>
	 * @param packageName
	 * @return
	 * @author maolinnan_350804
	 */
	public static SoftDetialBean getSoftDetialBean(Context context,String packageName){
		if(StringUtil.isEmpty(packageName)){
			return null;
		}
		SoftDetialBean softDetialBean = new SoftDetialBean();
		//请求详情数据
		StringBuffer url = new StringBuffer();
		url.append(String.format(SOFT_DETIA_REQUEST_URL,packageName));
		addGlobalRequestValue(context,url,null);//添加全局参数
		//发起网络请求
		HttpCommon httpCommon = new HttpCommon(url.toString());
		String respondContent = httpCommon.getResponseAsStringGET(new HashMap<String, String>(0));
		if (respondContent == null) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(respondContent);
			int code = jsonObject.getInt("Code");
			if (code != 0){
				return null;
			}
			softDetialBean.initDataFormJSONObject(jsonObject.getJSONObject("Result"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return softDetialBean;
	}
	
	
	/**
	 * MobileType 1代表IPHONE 2代表WINDOWSMOBILE 3代表 SYMBIAN S60 4代表ANDROID 5代表JAVA
	 * 6代表MTK
	 */
	public static final String MT = "4";
	/** 产品id 82是助手给的 */
	public static final String PID = "82";
	/** 熊猫桌面项目编号 */
	public static final String PROJECT_OPTION = "1900";
	/**
	 * 添加全局参数
	 * Title: addGlobalRequestValue
	 * Description:
	 * @param context
	 * @param sb
	 * @param sessionID
	 *            能获取到就传，获取不到就传null
	 * @author maolinnan_350804
	 */
	public static void addGlobalRequestValue(Context context, StringBuffer sb, String sessionID) {
		if (sb == null || context == null)
			return;

		String imsiNumber = TelephoneUtil.getIMSI(context);
		if (null == imsiNumber) {
			imsiNumber = "";
		}
		String imeiNumber = TelephoneUtil.getIMEI(context);
		if (null == imeiNumber) {
			imeiNumber = "";
		}
		appendAttrValue(sb, "mt", MT);
		appendAttrValue(sb, "tfv", "40000");
		appendAttrValue(sb, "pid", PID);
		appendAttrValue(sb, "imei", imeiNumber);
		appendAttrValue(sb, "imsi", imsiNumber);
		appendAttrValue(sb, "projectoption", PROJECT_OPTION);
		appendAttrValue(sb, "DivideVersion", TelephoneUtil.getVersionName(context, context.getPackageName()));
		appendAttrValue(sb, "SupPhone", encodeAttrValue(TelephoneUtil.getMachineName())); // 型号
		appendAttrValue(sb, "supfirm", TelephoneUtil.getFirmWareVersion()); // Android版本号
		appendAttrValue(sb, "company", TelephoneUtil.getManufacturer()); // 制造商
		appendAttrValue(sb, "nt", TelephoneUtil.getNT(context)); // 网络类型
		appendAttrValue(sb, "chl", ChannelUtil.getChannel(context)); // 渠道ID
        appendAttrValue(sb, "CUID", encodeAttrValue(CUIDUtil.getCUID())); //加入CUID
		String isRoot = TelephoneUtil.hasRootPermission() ? "1" : "0"; // 是否root
		appendAttrValue(sb, "JailBroken", isRoot); // 渠道ID
		if (sessionID != null && !"".equals(sessionID)) {
			appendAttrValue(sb, "sessionid", sessionID);
			appendAttrValue(sb, "ProtocolVersion", ThemeHttpCommon.ProtocolVersion);
		}
	}
	/**
	 * 拼接参数
	 * <p>
	 * Title: appendAttrValue
	 * Description:
	 * @param sb
	 * @param key
	 * @param values
	 * @author maolinnan_350804
	 */
	private static void appendAttrValue(StringBuffer sb, String key, String... values) {
		if (sb.indexOf("?" + key + "=") != -1 || sb.indexOf("&" + key + "=") != -1) {
			return;
		}
		for (String value : values) {
			if (sb.indexOf("?") == -1) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
	}
	/**
	 * 对参数进行转码
	 * <p>Title: encodeAttrValue</p>
	 * <p>Description: </p>
	 * @param value
	 * @return
	 * @author maolinnan_350804
	 */
	private static String encodeAttrValue(String value){
		String returnValue = "";
		try {
			value = URLEncoder.encode(value+"", "UTF-8");
			returnValue = value.replaceAll("\\+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnValue;
	}
}
