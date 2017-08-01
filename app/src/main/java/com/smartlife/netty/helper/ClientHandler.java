package com.smartlife.netty.helper;

import android.util.Log;

import com.google.protobuf.MessageLite;
import com.smartlife.huanxin.DemoHelper;
import com.smartlife.netty.utils.Utils;
import com.smartlife.utils.ToastUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;

public class ClientHandler extends SimpleChannelInboundHandler<MessageLite> {
    public static ChannelHandlerContext _gateClientConnection;

    private static final String TAG = "SmartLife/ClientHan";
    static String username = "";
    static String destname = "";
    static boolean verify = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        _gateClientConnection = ctx;
        username = DemoHelper.getInstance().getCurrentUsernName();
        destname = DemoHelper.getInstance().getCurrentDestName();
        sendCRegister(ctx, username, username);
        sendCLogin(ctx, username, username);
    }

    void sendCRegister(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid(userid);
        cb.setPasswd(passwd);

        ByteBuf byteBuf = Utils.pack2Client(cb.build());
        ctx.writeAndFlush(byteBuf);
    }

    void sendCLogin(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
        loginInfo.setUserid(userid);
        loginInfo.setPasswd(passwd);
        loginInfo.setPlatform("ios");
        loginInfo.setAppVersion("1.0.0");

        ByteBuf byteBuf = Utils.pack2Client(loginInfo.build());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageLite msg) throws Exception {
        Log.d(TAG,"received message: {}"+msg.getClass());
        if(msg instanceof Auth.SResponse) {
            Auth.SResponse sp = (Auth.SResponse) msg;
            int code = sp.getCode();
            String desc = sp.getDesc();
            switch (code) {
                case Constants.VERYFY_PASSED:
                    Log.d(TAG,"VERYFY_PASSED = "+ desc);
                    verify = true;
                    break;
                case Constants.ACCOUNT_INEXIST:
                    Log.d(TAG,"ACCOUNT_INEXIST = "+desc);
                    break;
                case Constants.VERYFY_ERROR:
                    Log.d(TAG,"VERYFY_ERROR="+desc);
                    break;
                case Constants.ACCOUNT_DUMPLICATED:
                    Log.d(TAG,"ACCOUNT_DUMPLICATED = "+desc);
                    break;
                case Constants.REGISTER_OK:
                    Log.d(TAG,"REGISTER_OK = "+ desc);
                    break;
                case Constants.Msg_SendSuccess:
                    Log.d(TAG,"Msg_SendSuccess ="+desc);
                default:
                    Log.d(TAG,"Unknow code: {}"+code);
            }
        } else if(msg instanceof Chat.SPrivateChat) {
            Chat.SPrivateChat cp = (Chat.SPrivateChat)msg;
            String content = cp.getContent();
            if(content.contains("collectionLike")){
                Log.d(TAG,"collectionLike");
                /******此次异常会导致后台异常****/
                //sendMessage("collectionliked");
            }else{
                Log.d(TAG,"content="+content);
                ToastUtil.showShort(content);
            }
        }
    }

    public static void sendMessage(String command) {
        if(verify){
            Chat.CPrivateChat.Builder cp = Chat.CPrivateChat.newBuilder();
            cp.setContent(command);
            cp.setSelf(username);
            cp.setDest(destname);

            ByteBuf byteBuf = Utils.pack2Client(cp.build());
            _gateClientConnection.writeAndFlush(byteBuf);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
