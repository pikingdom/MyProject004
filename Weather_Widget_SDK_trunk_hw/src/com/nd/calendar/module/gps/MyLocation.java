package com.nd.calendar.module.gps;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.nd.calendar.module.gps.LocManager.Result;

/**
 * 卫星定位位置获取
 * 
 * @author Tsung Wu <tsung.bz@gmail.com>
 */
public class MyLocation {
    private Timer timer1;

    private LocationManager lm;

    private LocationGpsResult locationResult;

    private boolean gps_enabled = false;

    private boolean network_enabled = false;
    
    private void getMyLocation(final Context context, final LocationResult result) {
        try {
            locationResult = new LocationGpsResult() {
                @Override
                public void gotLocation(Location location) {
                    Result r = null;
                    if (location != null) {
                        r = new Result();
                        r.longitude = location.getLongitude();
                        r.latitude = location.getLatitude();
                    }
                    if (result != null) {
                        result.gotLocation(r);
                    }
                }

            };
            if (lm == null) {
                lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }

            // exceptions will be thrown if provider is not permitted.
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            // don't start listeners if no provider is enabled
            if (!gps_enabled && !network_enabled) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    	try {
	                        LocManager loc = new LocManager(context);
	                        Result point = loc.getLocationPoint();
	                        result.gotLocation(point);
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    	}
                    }
                }).start();
            } else {

                if (gps_enabled)
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                            locationListenerGps);
                if (network_enabled)
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationListenerNetwork);
                timer1 = new Timer();
                timer1.schedule(new GetLastLocation(), 20000);
            }
        } catch (Exception e) {
            if (result != null) {
                result.gotLocation(null);
            }
            e.printStackTrace();
        }
    }

    public boolean getLocation(final Context context, final LocationResult result) {
        // I use LocationResult callback class to pass location value from
        // MyLocation to user code.
        // 先调用机站定位
        final Handler h = new Handler(new Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case 0:
                    getMyLocation(context, result);
                    break;
                default:
                    break;
                }
                
                return false;
            }
        });
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocManager loc = new LocManager(context);
                    Result point = loc.getLocationPoint();
                    if ((point != null) && (result != null)) {
                        // 如果定位成功就直接出去
                        result.gotLocation(point);
                    } else {
                        // 定位失败就用另外一种方法(这个不能直接在这里调用)
                        h.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    if (result != null) {
                        result.gotLocation(null);
                    }
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            try {
                lm.removeUpdates(locationListenerGps);
                lm.removeUpdates(locationListenerNetwork);

                Location net_loc = null, gps_loc = null;
                if (gps_enabled)
                    gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (network_enabled)
                    net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if there are both values use the latest one
                if (gps_loc != null && net_loc != null) {
                    if (gps_loc.getTime() > net_loc.getTime())
                        locationResult.gotLocation(gps_loc);
                    else
                        locationResult.gotLocation(net_loc);
                    return;
                }

                if (gps_loc != null) {
                    locationResult.gotLocation(gps_loc);
                    return;
                }
                if (net_loc != null) {
                    locationResult.gotLocation(net_loc);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Result location);
    }

    public static abstract class LocationGpsResult {
        public abstract void gotLocation(Location location);
    }

}
