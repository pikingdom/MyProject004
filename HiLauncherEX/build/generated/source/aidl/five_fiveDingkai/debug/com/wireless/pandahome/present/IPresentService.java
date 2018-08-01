/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\HiLauncherEX\\src\\com\\wireless\\pandahome\\present\\IPresentService.aidl
 */
package com.wireless.pandahome.present;
public interface IPresentService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.wireless.pandahome.present.IPresentService
{
private static final java.lang.String DESCRIPTOR = "com.wireless.pandahome.present.IPresentService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.wireless.pandahome.present.IPresentService interface,
 * generating a proxy if needed.
 */
public static com.wireless.pandahome.present.IPresentService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.wireless.pandahome.present.IPresentService))) {
return ((com.wireless.pandahome.present.IPresentService)iin);
}
return new com.wireless.pandahome.present.IPresentService.Stub.Proxy(obj);
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
case TRANSACTION_isLogined:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isLogined();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getLoginAccessToken:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getLoginAccessToken();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_startThemeShopV2MainActivity:
{
data.enforceInterface(DESCRIPTOR);
this.startThemeShopV2MainActivity();
reply.writeNoException();
return true;
}
case TRANSACTION_startCoinInfoActivity:
{
data.enforceInterface(DESCRIPTOR);
this.startCoinInfoActivity();
reply.writeNoException();
return true;
}
case TRANSACTION_startThemeShopV6TagListActivityAndBackToMain:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.startThemeShopV6TagListActivityAndBackToMain(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_ndComPlatformPersonalCenterLoginWithBroadcast:
{
data.enforceInterface(DESCRIPTOR);
this.ndComPlatformPersonalCenterLoginWithBroadcast();
reply.writeNoException();
return true;
}
case TRANSACTION_setBaiduAssistPresentOverdue:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setBaiduAssistPresentOverdue(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setGetBaiduPresent:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setGetBaiduPresent(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getBaiduPresent:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getBaiduPresent();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getBaiduChannelId:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getBaiduChannelId();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.wireless.pandahome.present.IPresentService
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
@Override public boolean isLogined() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isLogined, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//是否登陆

@Override public java.lang.String getLoginAccessToken() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLoginAccessToken, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startThemeShopV2MainActivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startThemeShopV2MainActivity, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startCoinInfoActivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startCoinInfoActivity, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startThemeShopV6TagListActivityAndBackToMain(java.lang.String tagName, int tagId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tagName);
_data.writeInt(tagId);
mRemote.transact(Stub.TRANSACTION_startThemeShopV6TagListActivityAndBackToMain, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void ndComPlatformPersonalCenterLoginWithBroadcast() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_ndComPlatformPersonalCenterLoginWithBroadcast, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setBaiduAssistPresentOverdue(boolean value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((value)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setBaiduAssistPresentOverdue, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setGetBaiduPresent(boolean value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((value)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setGetBaiduPresent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean getBaiduPresent() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBaiduPresent, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getBaiduChannelId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBaiduChannelId, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_isLogined = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getLoginAccessToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startThemeShopV2MainActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_startCoinInfoActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_startThemeShopV6TagListActivityAndBackToMain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_ndComPlatformPersonalCenterLoginWithBroadcast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setBaiduAssistPresentOverdue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setGetBaiduPresent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getBaiduPresent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getBaiduChannelId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
}
public boolean isLogined() throws android.os.RemoteException;
//是否登陆

public java.lang.String getLoginAccessToken() throws android.os.RemoteException;
public void startThemeShopV2MainActivity() throws android.os.RemoteException;
public void startCoinInfoActivity() throws android.os.RemoteException;
public void startThemeShopV6TagListActivityAndBackToMain(java.lang.String tagName, int tagId) throws android.os.RemoteException;
public void ndComPlatformPersonalCenterLoginWithBroadcast() throws android.os.RemoteException;
public void setBaiduAssistPresentOverdue(boolean value) throws android.os.RemoteException;
public void setGetBaiduPresent(boolean value) throws android.os.RemoteException;
public boolean getBaiduPresent() throws android.os.RemoteException;
public java.lang.String getBaiduChannelId() throws android.os.RemoteException;
}
