package com.smartlife.netty.analysis;

import android.util.Log;

import com.google.protobuf.MessageLite;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Qzy on 2016/1/29.
 */
public class ParseMap {
    private static String TAG = "SmartLife/PacketDec";

    public interface Parsing{
        MessageLite process(byte[] bytes) throws IOException;
    }

    public static HashMap<Integer, Parsing> parseMap = new HashMap<>();
    public static HashMap<Class<?>, Integer> msg2ptoNum = new HashMap<>();

    public static void register(int ptoNum, Parsing parse, Class<?> cla) {
        if (parseMap.get(ptoNum) == null)
            parseMap.put(ptoNum, parse);
        else {
            Log.d(TAG,"pto has been registered in parseMap, ptoNum: {}"+ptoNum);
            return;
        }

        if(msg2ptoNum.get(cla) == null)
            msg2ptoNum.put(cla, ptoNum);
        else {
            Log.d(TAG,"pto has been registered in msg2ptoNum, ptoNum: {}"+ptoNum);
            return;
        }
    }

    public static MessageLite getMessage(int ptoNum, byte[] bytes) throws IOException {
        Parsing parser = parseMap.get(ptoNum);
        if(parser == null) {
            Log.d(TAG,"UnKnown Protocol Num: {}"+ptoNum);
        }
        MessageLite msg = parser.process(bytes);

        return msg;
    }

    public static Integer getPtoNum(MessageLite msg) {
        return getPtoNum(msg.getClass());
    }

    public static Integer getPtoNum(Class<?> clz) {
        return msg2ptoNum.get(clz);
    }

}
