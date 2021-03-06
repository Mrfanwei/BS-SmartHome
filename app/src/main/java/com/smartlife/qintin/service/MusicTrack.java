/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.smartlife.qintin.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


/**
 * This is used by the music playback service to track the music tracks it is playing
 * It has extra meta data to determine where the track came from so that we can show the appropriate
 * song playing indicator
 */
public class MusicTrack implements Parcelable {
    private String TAG = "SmartLifee/MusicTr";

    public static final Creator<MusicTrack> CREATOR = new Creator<MusicTrack>() {
        @Override
        public MusicTrack createFromParcel(Parcel source) {
            return new MusicTrack(source);
        }

        @Override
        public MusicTrack[] newArray(int size) {
            return new MusicTrack[size];
        }
    };
    public String mUrl;
    public long mId;
    public int mSourcePosition;

    public MusicTrack(String url,long id, int sourcePosition) {
        Log.d(TAG,"MusicTrack");
        mUrl = url;
        mId = id;
        mSourcePosition = sourcePosition;
    }

    public MusicTrack(Parcel in) {
        mUrl = in.readString();
        mId = in.readLong();
        mSourcePosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeLong(mId);
        dest.writeInt(mSourcePosition);
    }

}
