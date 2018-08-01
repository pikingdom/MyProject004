/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\HiLauncherEX\\src\\com\\nd\\hilauncherdev\\myphone\\myfont\\IFontService.aidl
 */
package com.nd.hilauncherdev.myphone.myfont;
public interface IFontService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.myphone.myfont.IFontService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.myphone.myfont.IFontService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.myphone.myfont.IFontService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.myphone.myfont.IFontService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.myphone.myfont.IFontService))) {
return ((com.nd.hilauncherdev.myphone.myfont.IFontService)iin);
}
return new com.nd.hilauncherdev.myphone.myfont.IFontService.Stub.Proxy(obj);
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
case TRANSACTION_recharge:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.recharge(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getBuyState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getBuyState(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getOrderInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
int _arg2;
_arg2 = data.readInt();
java.lang.String _result = this.getOrderInfo(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getResourcePayInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getResourcePayInfo(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_buyFont:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.buyFont(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
this.login();
reply.writeNoException();
return true;
}
case TRANSACTION_initNdComPlatform:
{
data.enforceInterface(DESCRIPTOR);
this.initNdComPlatform();
reply.writeNoException();
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
case TRANSACTION_getDownloadUrl:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.getDownloadUrl(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.myphone.myfont.IFontService
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
@Override public void recharge(java.lang.String resId, int chargeType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resId);
_data.writeInt(chargeType);
mRemote.transact(Stub.TRANSACTION_recharge, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//充值

@Override public java.lang.String getBuyState(java.lang.String resId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resId);
mRemote.transact(Stub.TRANSACTION_getBuyState, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取购买状态

@Override public java.lang.String getOrderInfo(java.lang.String resId, boolean bRecharge, int actCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resId);
_data.writeInt(((bRecharge)?(1):(0)));
_data.writeInt(actCode);
mRemote.transact(Stub.TRANSACTION_getOrderInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取订单信息

@Override public java.lang.String getResourcePayInfo(java.lang.String resId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resId);
mRemote.transact(Stub.TRANSACTION_getResourcePayInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//核对字体购买信息

@Override public boolean buyFont(java.lang.String orderId, java.lang.String resId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(orderId);
_data.writeString(resId);
mRemote.transact(Stub.TRANSACTION_buyFont, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//购买

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
@Override public void initNdComPlatform() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_initNdComPlatform, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
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
@Override public java.lang.String getDownloadUrl(java.lang.String resId, java.lang.String orderId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resId);
_data.writeString(orderId);
mRemote.transact(Stub.TRANSACTION_getDownloadUrl, _data, _reply, 0);
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
static final int TRANSACTION_recharge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getBuyState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getOrderInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getResourcePayInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_buyFont = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_initNdComPlatform = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_isLogined = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getDownloadUrl = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
}
public void recharge(java.lang.String resId, int chargeType) throws android.os.RemoteException;
//充值

public java.lang.String getBuyState(java.lang.String resId) throws android.os.RemoteException;
//获取购买状态

public java.lang.String getOrderInfo(java.lang.String resId, boolean bRecharge, int actCode) throws android.os.RemoteException;
//获取订单信息

public java.lang.String getResourcePayInfo(java.lang.String resId) throws android.os.RemoteException;
//核对字体购买信息

public boolean buyFont(java.lang.String orderId, java.lang.String resId) throws android.os.RemoteException;
//购买

public void login() throws android.os.RemoteException;
public void initNdComPlatform() throws android.os.RemoteException;
public boolean isLogined() throws android.os.RemoteException;
public java.lang.String getDownloadUrl(java.lang.String resId, java.lang.String orderId) throws android.os.RemoteException;
}
