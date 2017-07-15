package com.smartlife.netty.helper;

import android.util.Log;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * socket网络连接类.
 * 
 * @author Administrator
 * 
 */
public class SocketConnect {
	private static String TAG = "SmartLife/SocketCon";
	public static void  InitSocket(final SocketListener listener,
                                   String address, int port) {

		init(listener, address, port);
	}

	private static void init(final SocketListener listener, String address,
			int port) {
		Log.d(TAG,"address ="+address+" port="+port);
		try {
			ClientBootstrap bootstrap = new ClientBootstrap(
					new NioClientSocketChannelFactory(
							Executors.newCachedThreadPool(),
							Executors.newCachedThreadPool()));

			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() {
					ChannelPipeline pipeline = Channels.pipeline();
					pipeline.addLast("decoder", new Decoder());
					SocketHandler handler = new SocketHandler();
					handler.getInstance(new SocketHandler.SocketHandlerListener() {

						@Override
						public void writeData(ChannelHandlerContext ctx,
								MessageEvent e) {
							Log.d(TAG,"writeData11");
							listener.writeData(ctx, e);
						}

						@Override
						public void receiveSuccess(ChannelHandlerContext ctx,
								MessageEvent e) {
							listener.receiveSuccess(ctx, e);
						}

						@Override
						public void connectSuccess(ChannelHandlerContext ctx,
								ChannelStateEvent e) {
							listener.connectSuccess(ctx, e);
						}

						@Override
						public void connectClose(ChannelHandlerContext ctx,
								ChannelStateEvent e) {
							listener.connectClose(ctx, e);

						}

						@Override
						public void connectFail() {
							listener.connectFail();
						}
					});
					pipeline.addLast("handler", handler);
					return pipeline;
				}

			});
			// "192.168.0.177" 8001
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(
					address, port));
			future.getChannel().getCloseFuture().awaitUninterruptibly();
			bootstrap.releaseExternalResources();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public interface SocketListener {
		public void connectFail();

		public void connectSuccess(ChannelHandlerContext ctx,
                                   ChannelStateEvent e);

		public void writeData(ChannelHandlerContext ctx, MessageEvent e);

		public void receiveSuccess(ChannelHandlerContext ctx, MessageEvent e);

		public void connectClose(ChannelHandlerContext ctx, ChannelStateEvent e);

	}

}
