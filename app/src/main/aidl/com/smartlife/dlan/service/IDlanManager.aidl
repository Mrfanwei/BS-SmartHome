// IDlanManager.aidl
package com.smartlife.dlan.service;

import com.smartlife.dlan.service.IDlanCallback;

// Declare any non-default types here with import statements

interface IDlanManager {
    void registerINettyCallback(IDlanCallback callback);
    void unregisterINettyCallback(IDlanCallback callback);
    void startSearchDevice();
    void stopSearchDevice();
    boolean play(String uri);
    boolean pause();
     boolean stop();
    boolean goOn(String pausePosition);
    String getTransportState();
    String getVolumeDbRange(String argument);
    int getMinVolumeValue();
    int getMaxVolumeValue();
    boolean seek(String targetPosition);
    String getPositionInfo();
    String getMediaDuration();
    boolean setMute(String targetValue);
    String getMute();
    boolean setVoice(int value);
    int getVoice();
}
