package com.smartlife.netty.utils;

import com.google.protobuf.MessageLite;
import com.smartlife.netty.analysis.ParseMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by win7 on 2016/3/5.
 */
public class Utils {

    public static ByteBuf pack2Client(MessageLite msg) {
        byte[] bytes = msg.toByteArray();
        int length =bytes.length;
        int ptoNum = ParseMap.getPtoNum(msg);

        ByteBuf buf = Unpooled.buffer(8 + length);
        buf.writeInt(length);
        buf.writeInt(ptoNum);
        buf.writeBytes(bytes);

        return buf;
    }
}
