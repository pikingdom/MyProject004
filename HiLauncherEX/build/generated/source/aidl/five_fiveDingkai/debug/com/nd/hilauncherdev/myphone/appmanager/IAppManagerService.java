/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\new\\newline\\91LauncherToolForDZ\\HiLauncherEX\\src\\com\\nd\\hilauncherdev\\myphone\\appmanager\\IAppManagerService.aidl
 */
package com.nd.hilauncherdev.myphone.appmanager;
public interface IAppManagerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nd.hilauncherdev.myphone.appmanager.IAppManagerService
{
private static final java.lang.String DESCRIPTOR = "com.nd.hilauncherdev.myphone.appmanager.IAppManagerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nd.hilauncherdev.myphone.appmanager.IAppManagerService interface,
 * generating a proxy if needed.
 */
public static com.nd.hilauncherdev.myphone.appmanager.IAppManagerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nd.hilauncherdev.myphone.appmanager.IAppManagerService))) {
return ((com.nd.hilauncherdev.myphone.appmanager.IAppManagerService)iin);
}
return new com.nd.hilauncherdev.myphone.appmanager.IAppManagerService.Stub.Proxy(obj);
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
case TRANSACTION_openDefaultBrowser:
{
data.enforceInterface(DESCRIPTOR);
this.openDefaultBrowser();
reply.writeNoException();
return true;
}
case TRANSACTION_clearDefaulBrowser:
{
data.enforceInterface(DESCRIPTOR);
this.clearDefaulBrowser();
reply.writeNoException();
return true;
}
case TRANSACTION_getDefaultBrowser:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDefaultBrowser();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_openDefaultDial:
{
data.enforceInterface(DESCRIPTOR);
this.openDefaultDial();
reply.writeNoException();
return true;
}
case TRANSACTION_clearDefaulDial:
{
data.enforceInterface(DESCRIPTOR);
this.clearDefaulDial();
reply.writeNoException();
return true;
}
case TRANSACTION_getDefaultDial:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDefaultDial();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_openDefaultContacts:
{
data.enforceInterface(DESCRIPTOR);
this.openDefaultContacts();
reply.writeNoException();
return true;
}
case TRANSACTION_clearDefaulContacts:
{
data.enforceInterface(DESCRIPTOR);
this.clearDefaulContacts();
reply.writeNoException();
return true;
}
case TRANSACTION_getDefaultContacts:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDefaultContacts();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_openDefaultSms:
{
data.enforceInterface(DESCRIPTOR);
this.openDefaultSms();
reply.writeNoException();
return true;
}
case TRANSACTION_clearDefaulSms:
{
data.enforceInterface(DESCRIPTOR);
this.clearDefaulSms();
reply.writeNoException();
return true;
}
case TRANSACTION_getDefaultSms:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDefaultSms();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_openDefaultSoft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.openDefaultSoft(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_clearDefaulSoft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.clearDefaulSoft(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getDefaultSoft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String[] _result = this.getDefaultSoft(_arg0);
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nd.hilauncherdev.myphone.appmanager.IAppManagerService
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
@Override public void openDefaultBrowser() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openDefaultBrowser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//打开设置默认浏览器界面

@Override public void clearDefaulBrowser() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearDefaulBrowser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除默认浏览器

@Override public java.lang.String[] getDefaultBrowser() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultBrowser, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取默认浏览器

@Override public void openDefaultDial() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openDefaultDial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//打开设置默认电话

@Override public void clearDefaulDial() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearDefaulDial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除默认电话

@Override public java.lang.String[] getDefaultDial() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultDial, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取默认电话

@Override public void openDefaultContacts() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openDefaultContacts, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//打开设置默认联系人

@Override public void clearDefaulContacts() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearDefaulContacts, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除默认联系人

@Override public java.lang.String[] getDefaultContacts() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultContacts, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取默认联系人

@Override public void openDefaultSms() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openDefaultSms, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//打开设置默认短信

@Override public void clearDefaulSms() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearDefaulSms, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除默认短信

@Override public java.lang.String[] getDefaultSms() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultSms, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//获取默认短信

@Override public void openDefaultSoft(java.lang.String intent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(intent);
mRemote.transact(Stub.TRANSACTION_openDefaultSoft, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//打开设置默认XX

@Override public void clearDefaulSoft(java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_clearDefaulSoft, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//清除默认XX

@Override public java.lang.String[] getDefaultSoft(java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_getDefaultSoft, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_openDefaultBrowser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_clearDefaulBrowser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getDefaultBrowser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_openDefaultDial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_clearDefaulDial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getDefaultDial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_openDefaultContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_clearDefaulContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getDefaultContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_openDefaultSms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_clearDefaulSms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getDefaultSms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_openDefaultSoft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_clearDefaulSoft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getDefaultSoft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
}
public void openDefaultBrowser() throws android.os.RemoteException;
//打开设置默认浏览器界面

public void clearDefaulBrowser() throws android.os.RemoteException;
//清除默认浏览器

public java.lang.String[] getDefaultBrowser() throws android.os.RemoteException;
//获取默认浏览器

public void openDefaultDial() throws android.os.RemoteException;
//打开设置默认电话

public void clearDefaulDial() throws android.os.RemoteException;
//清除默认电话

public java.lang.String[] getDefaultDial() throws android.os.RemoteException;
//获取默认电话

public void openDefaultContacts() throws android.os.RemoteException;
//打开设置默认联系人

public void clearDefaulContacts() throws android.os.RemoteException;
//清除默认联系人

public java.lang.String[] getDefaultContacts() throws android.os.RemoteException;
//获取默认联系人

public void openDefaultSms() throws android.os.RemoteException;
//打开设置默认短信

public void clearDefaulSms() throws android.os.RemoteException;
//清除默认短信

public java.lang.String[] getDefaultSms() throws android.os.RemoteException;
//获取默认短信

public void openDefaultSoft(java.lang.String intent) throws android.os.RemoteException;
//打开设置默认XX

public void clearDefaulSoft(java.lang.String type) throws android.os.RemoteException;
//清除默认XX

public java.lang.String[] getDefaultSoft(java.lang.String type) throws android.os.RemoteException;
}
