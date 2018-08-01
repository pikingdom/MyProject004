/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\ThemeShopV6\\src\\com\\nd\\hilauncherdev\\theme\\ILocalThemeService.aidl
 */
package com.nd.hilauncherdev.theme;
public interface ILocalThemeService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.theme.ILocalThemeService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.theme.ILocalThemeService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.theme.ILocalThemeService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.theme.ILocalThemeService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.theme.ILocalThemeService))) {
return ((com.nd.hilauncherdev.theme.ILocalThemeService)iin);
}
return new com.nd.hilauncherdev.theme.ILocalThemeService.Stub.Proxy(obj);
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
case TRANSACTION_applyTheme:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.applyTheme(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_applyModule:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.applyModule(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_applyThemeSeries:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
boolean _arg2;
_arg2 = (0!=data.readInt());
this.applyThemeSeries(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_importLocalExistTheme:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.importLocalExistTheme(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_requestLauncherUpdate:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.requestLauncherUpdate(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_installApt:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.installApt(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isThemeExist:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isThemeExist(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_applyStyle:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.applyStyle(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.theme.ILocalThemeService
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
@Override public void applyTheme(java.lang.String themeId, boolean autoDirect) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
_data.writeInt(((autoDirect)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_applyTheme, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//应用主题

@Override public void applyModule(java.lang.String moduleId, java.lang.String moduleKey) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(moduleId);
_data.writeString(moduleKey);
mRemote.transact(Stub.TRANSACTION_applyModule, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//应用模块

@Override public void applyThemeSeries(java.lang.String applyThemeId, boolean autoDirect, boolean showSuccessHint) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(applyThemeId);
_data.writeInt(((autoDirect)?(1):(0)));
_data.writeInt(((showSuccessHint)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_applyThemeSeries, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//应用主题系列

@Override public void importLocalExistTheme(boolean showDialog) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((showDialog)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_importLocalExistTheme, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//导入本地已存在的主题

@Override public void requestLauncherUpdate(boolean showDialog) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((showDialog)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_requestLauncherUpdate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//请求桌面版本升级

@Override public boolean installApt(java.lang.String aptPath) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(aptPath);
mRemote.transact(Stub.TRANSACTION_installApt, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//安装apt主题

@Override public boolean isThemeExist(java.lang.String themeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(themeId);
mRemote.transact(Stub.TRANSACTION_isThemeExist, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//主题是否已安装

@Override public void applyStyle(java.lang.String sceneId, java.lang.String themeId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sceneId);
_data.writeString(themeId);
mRemote.transact(Stub.TRANSACTION_applyStyle, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_applyTheme = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_applyModule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_applyThemeSeries = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_importLocalExistTheme = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_requestLauncherUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_installApt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isThemeExist = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_applyStyle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
public void applyTheme(java.lang.String themeId, boolean autoDirect) throws android.os.RemoteException;
//应用主题

public void applyModule(java.lang.String moduleId, java.lang.String moduleKey) throws android.os.RemoteException;
//应用模块

public void applyThemeSeries(java.lang.String applyThemeId, boolean autoDirect, boolean showSuccessHint) throws android.os.RemoteException;
//应用主题系列

public void importLocalExistTheme(boolean showDialog) throws android.os.RemoteException;
//导入本地已存在的主题

public void requestLauncherUpdate(boolean showDialog) throws android.os.RemoteException;
//请求桌面版本升级

public boolean installApt(java.lang.String aptPath) throws android.os.RemoteException;
//安装apt主题

public boolean isThemeExist(java.lang.String themeId) throws android.os.RemoteException;
//主题是否已安装

public void applyStyle(java.lang.String sceneId, java.lang.String themeId) throws android.os.RemoteException;
}
