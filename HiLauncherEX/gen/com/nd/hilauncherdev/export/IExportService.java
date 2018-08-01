/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\91Launcher_V6.9.2\\HiLauncherEX\\src\\com\\nd\\hilauncherdev\\export\\IExportService.aidl
 */
package com.nd.hilauncherdev.export;
public interface IExportService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.export.IExportService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.export.IExportService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.export.IExportService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.export.IExportService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.export.IExportService))) {
return ((com.nd.hilauncherdev.export.IExportService)iin);
}
return new com.nd.hilauncherdev.export.IExportService.Stub.Proxy(obj);
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
case TRANSACTION_getIntPreference:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _result = this.getIntPreference(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getFloatPreference:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
float _arg1;
_arg1 = data.readFloat();
float _result = this.getFloatPreference(_arg0, _arg1);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_getLongPreference:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
long _arg1;
_arg1 = data.readLong();
long _result = this.getLongPreference(_arg0, _arg1);
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getBooleanPreference:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
boolean _result = this.getBooleanPreference(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getStringPreference:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.getStringPreference(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getApps:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.app.AppEntity> _result = this.getApps(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getConfigs:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.config.ConfigEntity> _result = this.getConfigs(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getCurTheme:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.theme.CurThemeEntity> _result = this.getCurTheme(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getKeyConfig:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.theme.KeyConfigEntity> _result = this.getKeyConfig(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getModule:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.theme.ModuleEntity> _result = this.getModule(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_getTheme:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.nd.hilauncherdev.export.theme.ThemeEntity> _result = this.getTheme(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.export.IExportService
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
/**
	 * 获取Preference值
	 */
@Override public int getIntPreference(java.lang.String key, int defaultValue) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeInt(defaultValue);
mRemote.transact(Stub.TRANSACTION_getIntPreference, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public float getFloatPreference(java.lang.String key, float defaultValue) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeFloat(defaultValue);
mRemote.transact(Stub.TRANSACTION_getFloatPreference, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getLongPreference(java.lang.String key, long defaultValue) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeLong(defaultValue);
mRemote.transact(Stub.TRANSACTION_getLongPreference, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean getBooleanPreference(java.lang.String key, boolean defaultValue) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeInt(((defaultValue)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_getBooleanPreference, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getStringPreference(java.lang.String key, java.lang.String defaultValue) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeString(defaultValue);
mRemote.transact(Stub.TRANSACTION_getStringPreference, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取匣子数据
	 */
@Override public java.util.List<com.nd.hilauncherdev.export.app.AppEntity> getApps(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.app.AppEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getApps, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.app.AppEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取Config表数据
	 */
@Override public java.util.List<com.nd.hilauncherdev.export.config.ConfigEntity> getConfigs(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.config.ConfigEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getConfigs, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.config.ConfigEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取主题数据
	 */
@Override public java.util.List<com.nd.hilauncherdev.export.theme.CurThemeEntity> getCurTheme(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.theme.CurThemeEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getCurTheme, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.theme.CurThemeEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<com.nd.hilauncherdev.export.theme.KeyConfigEntity> getKeyConfig(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.theme.KeyConfigEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getKeyConfig, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.theme.KeyConfigEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<com.nd.hilauncherdev.export.theme.ModuleEntity> getModule(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.theme.ModuleEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getModule, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.theme.ModuleEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<com.nd.hilauncherdev.export.theme.ThemeEntity> getTheme(java.lang.String auth) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.nd.hilauncherdev.export.theme.ThemeEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(auth);
mRemote.transact(Stub.TRANSACTION_getTheme, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.nd.hilauncherdev.export.theme.ThemeEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getIntPreference = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getFloatPreference = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getLongPreference = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getBooleanPreference = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getStringPreference = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getApps = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getConfigs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getCurTheme = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getKeyConfig = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getModule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getTheme = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
}
/**
	 * 获取Preference值
	 */
public int getIntPreference(java.lang.String key, int defaultValue) throws android.os.RemoteException;
public float getFloatPreference(java.lang.String key, float defaultValue) throws android.os.RemoteException;
public long getLongPreference(java.lang.String key, long defaultValue) throws android.os.RemoteException;
public boolean getBooleanPreference(java.lang.String key, boolean defaultValue) throws android.os.RemoteException;
public java.lang.String getStringPreference(java.lang.String key, java.lang.String defaultValue) throws android.os.RemoteException;
/**
	 * 获取匣子数据
	 */
public java.util.List<com.nd.hilauncherdev.export.app.AppEntity> getApps(java.lang.String auth) throws android.os.RemoteException;
/**
	 * 获取Config表数据
	 */
public java.util.List<com.nd.hilauncherdev.export.config.ConfigEntity> getConfigs(java.lang.String auth) throws android.os.RemoteException;
/**
	 * 获取主题数据
	 */
public java.util.List<com.nd.hilauncherdev.export.theme.CurThemeEntity> getCurTheme(java.lang.String auth) throws android.os.RemoteException;
public java.util.List<com.nd.hilauncherdev.export.theme.KeyConfigEntity> getKeyConfig(java.lang.String auth) throws android.os.RemoteException;
public java.util.List<com.nd.hilauncherdev.export.theme.ModuleEntity> getModule(java.lang.String auth) throws android.os.RemoteException;
public java.util.List<com.nd.hilauncherdev.export.theme.ThemeEntity> getTheme(java.lang.String auth) throws android.os.RemoteException;
}
