package com.nd.hilauncherdev.kitset.uconfig;

/**
 * Description: </br>
 * Author: cxy
 * Date: 2017/2/20.
 */

public interface ContentConverter<T> {

    T convert(String src);
}
