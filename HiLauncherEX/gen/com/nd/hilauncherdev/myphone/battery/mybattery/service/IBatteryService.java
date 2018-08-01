/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\91Launcher_V6.9.2\\HiLauncherEX\\src\\com\\nd\\hilauncherdev\\myphone\\battery\\mybattery\\service\\IBatteryService.aidl
 */
package com.nd.hilauncherdev.myphone.battery.mybattery.service;
public interface IBatteryService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService))) {
return ((com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService)iin);
}
return new com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService.Stub.Proxy(obj);
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
case TRANSACTION_getIsTrikle:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getIsTrikle();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getShowTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getShowTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.myphone.battery.mybattery.service.IBatteryService
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
	 * 是否在涓流充电<br>
	 * true 是
	 * false 否
	 */
@Override public boolean getIsTrikle() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getIsTrikle, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 得到显示的时间
	 * 
	 */
@Override public long getShowTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getShowTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getIsTrikle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getShowTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
	 * 是否在涓流充电<br>
	 * true 是
	 * false 否
	 */
public boolean getIsTrikle() throws android.os.RemoteException;
/**
	 * 得到显示的时间
	 * 
	 */
public long getShowTime() throws android.os.RemoteException;
}
