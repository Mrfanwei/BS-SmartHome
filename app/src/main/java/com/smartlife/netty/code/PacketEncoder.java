package com.smartlife.netty.code;

import android.util.Log;

import com.google.protobuf.MessageLite;
import com.smartlife.netty.analysis.ParseMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Administrator on 2016/1/29.
 */
public class PacketEncoder extends MessageToByteEncoder<MessageLite> {
    private static final String TAG = "SmartLife/PacketEnc";

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageLite msg, ByteBuf out)
            throws Exception {

        byte[] bytes = msg.toByteArray();// 将对象转换为byte
        int ptoNum = ParseMap.msg2ptoNum.get(msg);
        int length = bytes.length;

        /* 加密消息体
        ThreeDES des = ctx.channel().attr(ClientAttr.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);
        int length = encryptByte.length;*/

        ByteBuf buf = Unpooled.buffer(8 + length);
        buf.writeInt(length);
        buf.writeInt(ptoNum);
        buf.writeBytes(bytes);
        out.writeBytes(buf);

        Log.d(TAG,"GateServer Send Message, remoteAddress: {},"+ctx.channel().remoteAddress() + " content length {},"+length + " ptoNum: {}"+ptoNum);

    }
}
