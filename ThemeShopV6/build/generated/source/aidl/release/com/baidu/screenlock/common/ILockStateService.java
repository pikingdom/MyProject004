/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\ThemeShopV6\\src\\com\\baidu\\screenlock\\common\\ILockStateService.aidl
 */
package com.baidu.screenlock.common;
public interface ILockStateService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baidu.screenlock.common.ILockStateService
{
private static final java.lang.String DESCRIPTOR = "com.baidu.screenlock.common.ILockStateService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baidu.screenlock.common.ILockStateService interface,
 * generating a proxy if needed.
 */
public static com.baidu.screenlock.common.ILockStateService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baidu.screenlock.common.ILockStateService))) {
return ((com.baidu.screenlock.common.ILockStateService)iin);
}
return new com.baidu.screenlock.common.ILockStateService.Stub.Proxy(obj);
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
case TRANSACTION_isLockOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isLockOpen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getFollowThemeChangeSkin:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getFollowThemeChangeSkin();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setLockState:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setLockState(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_openOrCloseLockScreen:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.openOrCloseLockScreen(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getDxLockSceenPkgName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getDxLockSceenPkgName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCurrLockState:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getCurrLockState();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCurThemeId:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCurThemeId();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.baidu.screenlock.common.ILockStateService
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
@Override public boolean isLockOpen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isLockOpen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//是否处于锁屏状态

@Override public boolean getFollowThemeChangeSkin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFollowThemeChangeSkin, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//跟随主题换肤

@Override public void setLockState(boolean state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((state)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setLockState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//设置锁屏状态

@Override public void openOrCloseLockScreen(java.lang.String pkgName, boolean isOpenThisLockScreen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pkgName);
_data.writeInt(((isOpenThisLockScreen)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_openOrCloseLockScreen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//开起关闭点心锁屏

@Override public java.lang.String getDxLockSceenPkgName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDxLockSceenPkgName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取开启的点心锁屏当前包名

@Override public boolean getCurrLockState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrLockState, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取配置文件中当前锁屏状态

@Override public java.lang.String getCurThemeId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurThemeId, _data, _reply, 0);
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
static final int TRANSACTION_isLockOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getFollowThemeChangeSkin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setLockState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_openOrCloseLockScreen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getDxLockSceenPkgName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getCurrLockState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getCurThemeId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public boolean isLockOpen() throws android.os.RemoteException;
//是否处于锁屏状态

public boolean getFollowThemeChangeSkin() throws android.os.RemoteException;
//跟随主题换肤

public void setLockState(boolean state) throws android.os.RemoteException;
//设置锁屏状态

public void openOrCloseLockScreen(java.lang.String pkgName, boolean isOpenThisLockScreen) throws android.os.RemoteException;
//开起关闭点心锁屏

public java.lang.String getDxLockSceenPkgName() throws android.os.RemoteException;
//获取开启的点心锁屏当前包名

public boolean getCurrLockState() throws android.os.RemoteException;
//获取配置文件中当前锁屏状态

public java.lang.String getCurThemeId() throws android.os.RemoteException;
}
