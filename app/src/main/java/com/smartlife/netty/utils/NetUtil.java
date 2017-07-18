package com.smartlife.netty.utils;

import android.util.Log;

import com.smartlife.netty.ParseRegistryMap;
import com.smartlife.netty.code.PacketDecoder;
import com.smartlife.netty.code.PacketEncoder;
import com.smartlife.netty.helper.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NetUtil {

	private static String TAG = "SmartLife/NetUtil";
	private static NetUtil mNetUtil = null;
	static final String HOST = "112.74.175.96";
	static final int PORT = 9090;

	public NetUtil() {
		super();
	}

	public static NetUtil getInstance(){
		if (mNetUtil == null) {
			synchronized (NetUtil.class) {
				if (mNetUtil == null) {
					mNetUtil = new NetUtil();
				}
			}

		}
		return mNetUtil;
	}

	public static void socketConnect(){
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();

						p.addLast("MessageDecoder", new PacketDecoder());
						p.addLast("MessageEncoder", new PacketEncoder());
						p.addLast(new ClientHandler());
					}
				});

		startConnection(b,1);
	}

	private static void startConnection(Bootstrap b, int index) {
		b.connect(HOST, PORT)
				.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						if (future.isSuccess()) {
							//init registry
							ParseRegistryMap.initRegistry();
							Log.d(TAG,"success");
						} else {
							Log.d(TAG,"failed");
						}
					}
				});
	}

	public static void sendCommand(String command){
		Log.d(TAG,"sendCommand");
		ClientHandler.sendMessage(command);
	}
}
