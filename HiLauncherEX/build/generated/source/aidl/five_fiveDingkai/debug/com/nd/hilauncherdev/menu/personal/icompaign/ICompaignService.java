/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\HiLauncherEX\\src\\com\\nd\\hilauncherdev\\menu\\personal\\icompaign\\ICompaignService.aidl
 */
package com.nd.hilauncherdev.menu.personal.icompaign;
public interface ICompaignService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService))) {
return ((com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService)iin);
}
return new com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_hasLogin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.hasLogin();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
this.login();
reply.writeNoException();
return true;
}
case TRANSACTION_recharge:
{
data.enforceInterface(DESCRIPTOR);
this.recharge();
reply.writeNoException();
return true;
}
case TRANSACTION_showShop:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.showShop(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_shareWeibo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.shareWeibo(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_showPlanShop:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.showPlanShop(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_showThemeDetail:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.showThemeDetail(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_downLoadTheme:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.downLoadTheme(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_downLoadThemeById:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.downLoadThemeById(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getThemeInfoForServer:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getThemeInfoForServer();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_bindPhone:
{
data.enforceInterface(DESCRIPTOR);
this.bindPhone();
reply.writeNoException();
return true;
}
case TRANSACTION_hasbindPhone:
{
data.enforceInterface(DESCRIPTOR);
this.hasbindPhone();
reply.writeNoException();
return true;
}
case TRANSACTION_downLoadSoft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
this.downLoadSoft(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_creatShortCut:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.creatShortCut(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_showPlanShopMain:
{
data.enforceInterface(DESCRIPTOR);
this.showPlanShopMain();
reply.writeNoException();
return true;
}
case TRANSACTION_loginWithNotify:
{
data.enforceInterface(DESCRIPTOR);
this.loginWithNotify();
reply.writeNoException();
return true;
}
case TRANSACTION_upgrade:
{
data.enforceInterface(DESCRIPTOR);
this.upgrade();
reply.writeNoException();
return true;
}
case TRANSACTION_showSpecialPlan:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.showSpecialPlan(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_HdDownApp:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.HdDownApp(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_HdDownThemeid:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.HdDownThemeid(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_HdBuy:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.HdBuy(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_HdGetAppStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.HdGetAppStatus(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_HdSecKill:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.HdSecKill(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_HdRecharge:
{
data.enforceInterface(DESCRIPTOR);
this.HdRecharge();
reply.writeNoException();
return true;
}
case TRANSACTION_HdLauncherProtocol:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.HdLauncherProtocol(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendFeedEvent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.Bundle _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.sendFeedEvent(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_clearFeedNotifyNum:
{
data.enforceInterface(DESCRIPTOR);
this.clearFeedNotifyNum();
reply.writeNoException();
return true;
}
case TRANSACTION_getFeedNotifyNum:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getFeedNotifyNum();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.menu.personal.icompaign.ICompaignService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String hasLogin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hasLogin, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//是否登陆过

@Override public void login() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//登陆

@Override public void recharge() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_recharge, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//充值

@Override public void showShop(boolean isFromPlan) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isFromPlan)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_showShop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//跳转到主题商城

@Override public void shareWeibo(java.lang.String text, java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(text);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_shareWeibo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//微博分享

@Override public void showPlanShop(java.lang.String url, java.lang.String activityName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
_data.writeString(activityName);
mRemote.transact(Stub.TRANSACTION_showPlanShop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//跳转到指定主题

@Override public void showThemeDetail(java.lang.String themeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
mRemote.transact(Stub.TRANSACTION_showThemeDetail, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//跳转到指定主题

@Override public void downLoadTheme(java.lang.String activityId, java.lang.String themeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(activityId);
_data.writeString(themeId);
mRemote.transact(Stub.TRANSACTION_downLoadTheme, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//下载主题

@Override public void downLoadThemeById(java.lang.String themeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
mRemote.transact(Stub.TRANSACTION_downLoadThemeById, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//下载主题

@Override public java.lang.String getThemeInfoForServer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getThemeInfoForServer, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取主题

@Override public void bindPhone() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_bindPhone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//绑定手机

@Override public void hasbindPhone() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hasbindPhone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//是否绑定手机

@Override public void downLoadSoft(java.lang.String resid, java.lang.String identifier, java.lang.String activityid, java.lang.String resname, java.lang.String iconUrl) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resid);
_data.writeString(identifier);
_data.writeString(activityid);
_data.writeString(resname);
_data.writeString(iconUrl);
mRemote.transact(Stub.TRANSACTION_downLoadSoft, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//软件下载

@Override public void creatShortCut(java.lang.String postUrl, java.lang.String postComParam, java.lang.String title) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(postUrl);
_data.writeString(postComParam);
_data.writeString(title);
mRemote.transact(Stub.TRANSACTION_creatShortCut, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//创建快捷

@Override public void showPlanShopMain() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_showPlanShopMain, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 跳转到主题活动商城主页

@Override public void loginWithNotify() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loginWithNotify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//登录通知

@Override public void upgrade() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_upgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//检测更新桌面

@Override public void showSpecialPlan(int tagId, java.lang.String tagName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(tagId);
_data.writeString(tagName);
mRemote.transact(Stub.TRANSACTION_showSpecialPlan, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//跳转到指定专辑
//V6.3.3改版活动

@Override public void HdDownApp(java.lang.String packageName, java.lang.String appName, java.lang.String iconUrl, java.lang.String placeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(appName);
_data.writeString(iconUrl);
_data.writeString(placeId);
mRemote.transact(Stub.TRANSACTION_HdDownApp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//下载应用

@Override public void HdDownThemeid(java.lang.String themeId, java.lang.String compaignId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
_data.writeString(compaignId);
mRemote.transact(Stub.TRANSACTION_HdDownThemeid, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//主题下载(主题ID，活动ID)

@Override public void HdBuy(java.lang.String themeId, java.lang.String compaignPrice, java.lang.String compaignId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
_data.writeString(compaignPrice);
_data.writeString(compaignId);
mRemote.transact(Stub.TRANSACTION_HdBuy, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//主题购买(主题ID,活动价格,活动ID)

@Override public int HdGetAppStatus(java.lang.String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_HdGetAppStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取某应用当前状态（应用包名）

@Override public void HdSecKill(java.lang.String themeId, java.lang.String compaignPrice, java.lang.String compaignId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
_data.writeString(compaignPrice);
_data.writeString(compaignId);
mRemote.transact(Stub.TRANSACTION_HdSecKill, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//主题秒杀

@Override public void HdRecharge() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_HdRecharge, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//充值

@Override public void HdLauncherProtocol(java.lang.String content) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(content);
mRemote.transact(Stub.TRANSACTION_HdLauncherProtocol, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//调用桌面内容协议
//v7.0.3美图

@Override public void sendFeedEvent(java.lang.String key, android.os.Bundle bundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
if ((bundle!=null)) {
_data.writeInt(1);
bundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_sendFeedEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//美图事件传递

@Override public void clearFeedNotifyNum() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearFeedNotifyNum, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除美图消息数目

@Override public int getFeedNotifyNum() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFeedNotifyNum, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_hasLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_recharge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_showShop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_shareWeibo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_showPlanShop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_showThemeDetail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_downLoadTheme = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_downLoadThemeById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getThemeInfoForServer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_bindPhone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_hasbindPhone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_downLoadSoft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_creatShortCut = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_showPlanShopMain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_loginWithNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_upgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_showSpecialPlan = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_HdDownApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_HdDownThemeid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_HdBuy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_HdGetAppStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_HdSecKill = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_HdRecharge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_HdLauncherProtocol = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_sendFeedEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_clearFeedNotifyNum = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_getFeedNotifyNum = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
}
public java.lang.String hasLogin() throws android.os.RemoteException;
//是否登陆过

public void login() throws android.os.RemoteException;
//登陆

public void recharge() throws android.os.RemoteException;
//充值

public void showShop(boolean isFromPlan) throws android.os.RemoteException;
//跳转到主题商城

public void shareWeibo(java.lang.String text, java.lang.String path) throws android.os.RemoteException;
//微博分享

public void showPlanShop(java.lang.String url, java.lang.String activityName) throws android.os.RemoteException;
//跳转到指定主题

public void showThemeDetail(java.lang.String themeId) throws android.os.RemoteException;
//跳转到指定主题

public void downLoadTheme(java.lang.String activityId, java.lang.String themeId) throws android.os.RemoteException;
//下载主题

public void downLoadThemeById(java.lang.String themeId) throws android.os.RemoteException;
//下载主题

public java.lang.String getThemeInfoForServer() throws android.os.RemoteException;
//获取主题

public void bindPhone() throws android.os.RemoteException;
//绑定手机

public void hasbindPhone() throws android.os.RemoteException;
//是否绑定手机

public void downLoadSoft(java.lang.String resid, java.lang.String identifier, java.lang.String activityid, java.lang.String resname, java.lang.String iconUrl) throws android.os.RemoteException;
//软件下载

public void creatShortCut(java.lang.String postUrl, java.lang.String postComParam, java.lang.String title) throws android.os.RemoteException;
//创建快捷

public void showPlanShopMain() throws android.os.RemoteException;
// 跳转到主题活动商城主页

public void loginWithNotify() throws android.os.RemoteException;
//登录通知

public void upgrade() throws android.os.RemoteException;
//检测更新桌面

public void showSpecialPlan(int tagId, java.lang.String tagName) throws android.os.RemoteException;
//跳转到指定专辑
//V6.3.3改版活动

public void HdDownApp(java.lang.String packageName, java.lang.String appName, java.lang.String iconUrl, java.lang.String placeId) throws android.os.RemoteException;
//下载应用

public void HdDownThemeid(java.lang.String themeId, java.lang.String compaignId) throws android.os.RemoteException;
//主题下载(主题ID，活动ID)

public void HdBuy(java.lang.String themeId, java.lang.String compaignPrice, java.lang.String compaignId) throws android.os.RemoteException;
//主题购买(主题ID,活动价格,活动ID)

public int HdGetAppStatus(java.lang.String packageName) throws android.os.RemoteException;
//获取某应用当前状态（应用包名）

public void HdSecKill(java.lang.String themeId, java.lang.String compaignPrice, java.lang.String compaignId) throws android.os.RemoteException;
//主题秒杀

public void HdRecharge() throws android.os.RemoteException;
//充值

public void HdLauncherProtocol(java.lang.String content) throws android.os.RemoteException;
//调用桌面内容协议
//v7.0.3美图

public void sendFeedEvent(java.lang.String key, android.os.Bundle bundle) throws android.os.RemoteException;
//美图事件传递

public void clearFeedNotifyNum() throws android.os.RemoteException;
//清除美图消息数目

public int getFeedNotifyNum() throws android.os.RemoteException;
}
