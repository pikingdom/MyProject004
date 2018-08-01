/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : WidgetSkinInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2011-10-24 下午02:28:38 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget.skin;

import java.util.Date;

import org.json.JSONObject;

import android.graphics.Bitmap;

public class WidgetSkinInfo
{
	private String mSkinId;
	private int mSkinIdServer;
	
	private String mSkinName = ""; 		// 名字
	private String mPreviewUrl = "";	// 图例 URL
	private String mSkinUrl = ""; 		// 文件 URL
	private String mDescribe = "";		// 描述
	private String mDimension = ""; 	// 皮肤尺寸
	private Date mPublishDate = null;	// 发布日期
	private String mOtherDescribe = "";
	private float mRating; 				// 星级
	private int mFlag;					// 类别标志
	private int mSkinSize;				// 皮肤大小
	private int mSort;					// 排序
	private String mVersion;
	private boolean mIsNewVersion;
	

	private Bitmap m_bmpAuthorIco;		//

	private boolean mbSetuped = false;

	private boolean mIsOutside = false; // 是否是外置皮肤
	private String mPreviewName; 		// 预览图文件名
	private String mSkinPath;			// 皮肤路径

	private String mAuthor = "";
	private String m_detailInfo = "";
	private int m_downloadCount = 0;

	public String getSkinId() {
		return mSkinId;
	}

	public void setSkinId(String sSkinId) {
		this.mSkinId = sSkinId;
	}
	
	public boolean isNewVersion() {
		return mIsNewVersion;
	}

	public void setNewVersion(boolean bNewVersion) {
		this.mIsNewVersion = bNewVersion;
	}
	public boolean isOutside() {
		return mIsOutside;
	}

	public void setOutsideSkin(boolean bInside) {
		mIsOutside = bInside;
	}

	public String getPreviewFileName() {
		return mPreviewName;
	}

	public void setPreviewFileName(String sPreview) {
		mPreviewName = sPreview;
	}

	public String getSkinPath() {
		return mSkinPath;
	}

	public void setSkinPath(String mStrPath) {
		mSkinPath = mStrPath;
	}

	public String getSkinName() {
		return mSkinName;
	}

	public void setSkinName(String skinName) {
		mSkinName = skinName;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getDetailInfo() {
		return m_detailInfo;
	}

	public void setDetailInfo(String info) {
		m_detailInfo = info;
	}

	public int getDownloadCount() {
		return m_downloadCount;
	}

	public void setDownloadCount(int count) {
		m_downloadCount = count;
	}

	public int getSkinIdServer() {
		return mSkinIdServer;
	}

	public String getPreviewUrl() {
		return mPreviewUrl;
	}

	public String getSkinUrl() {
		return mSkinUrl;
	}

	public String getDimension() {
		return mDimension;
	}

	public Date getPublishDate() {
		return mPublishDate;
	}

	public String getOtherDescribe() {
		return mOtherDescribe;
	}

	public float getRating() {
		return mRating;
	}

	public int getFlag() {
		return mFlag;
	}

	public int getSkinSize() {
		return mSkinSize;
	}

	public long getSort() {
		return mSort;
	}

	public void setSkinIdServer(int skinId) {
		this.mSkinIdServer = skinId;
	}

	public void setPreviewUrl(String sPreviewUrl) {
		this.mPreviewUrl = sPreviewUrl;
	}

	public void setSkinUrl(String sSkinUrl) {
		this.mSkinUrl = sSkinUrl;
	}

	public void setDimension(String sDimension) {
		this.mDimension = sDimension;
	}

	public void setPublishDate(long nPublishDate) {
		this.mPublishDate = new Date(nPublishDate);
	}

	public void setOtherDescribe(String sOtherDescribe) {
		this.mOtherDescribe = sOtherDescribe;
	}

	public void setRating(float rating) {
		this.mRating = rating;
	}

	public void setFlag(int nFlag) {
		this.mFlag = nFlag;
	}

	public void setSkinSize(int nSkinSize) {
		this.mSkinSize = nSkinSize;
	}

	public void setSort(int nSort) {
		this.mSort = nSort;
	}

	public String getDescribe() {
		return mDescribe;
	}

	public void setDescribe(String sDescribe) {
		this.mDescribe = sDescribe;
	}

	public void setAuthorIcoBitmap(Bitmap bmpIco) {
		this.m_bmpAuthorIco = bmpIco;
	}

	public Bitmap getAuthorIcoBitmap() {
		return m_bmpAuthorIco;
	}

	public boolean isSetuped() {
		return mbSetuped;
	}

	public void setSetuped(boolean bSetuped) {
		this.mbSetuped = bSetuped;
	}

	public String getVersion() {
		return mVersion;
	}

	public void setVersion(String sVersion) {
		this.mVersion = sVersion;
	}
	
	public JSONObject getVersinJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("nPluginSkinInfoId", mSkinIdServer);
			jsonObject.put("vercode", mVersion);
		} catch (Exception e) {
		}
		
		return jsonObject;
	}
	
	public boolean setJson(JSONObject jsonObj) {
		try {
			setSkinIdServer(jsonObj.getInt("nPluginSkinInfoId"));
			setSkinName(jsonObj.optString("sSkinTitle"));
			setPreviewUrl(jsonObj.getString("sLogUrl"));
			setSkinUrl(jsonObj.getString("sSkinDownUrl"));
			setAuthor(jsonObj.optString("sUserName"));
			setDescribe(jsonObj.optString("sSkinInfodes"));
			setDimension(jsonObj.optString("sAndrSizeInfo"));
			setOtherDescribe(jsonObj.optString("sSkinOtherDes"));
			setPublishDate(jsonObj.optLong("nSkinDate")*1000);
			setRating((float)jsonObj.getDouble("nStartCount"));
			setFlag(jsonObj.optInt("nFlag"));
			setSkinSize(jsonObj.optInt("nSkinSize"));
			setSort(jsonObj.optInt("nSortFlag"));
			setDownloadCount(jsonObj.optInt("nDownTime"));
			setVersion(jsonObj.optString("vercode"));
			
			return true;
		} catch (Exception e) {
		}
		
		return false;
	}
}
