package com.wangqi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientStart {

    public static void main(String[] args) throws InterruptedException {
         String host="localhost";
         int port=8080;
        //创建⼀个 NioEventLoopGroup 对象实例
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try {
            //创建客户端启动引导/辅助类： Bootstrap
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)//指定 IO 模型
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .group(eventLoopGroup)//指定线程组
                    .remoteAddress(host,port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //这⾥可以⾃定义消息的业务处理逻辑
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            //尝试建⽴连接
            ChannelFuture channelFuture=bootstrap.connect(host,port).sync();
            //等待连接关闭（阻塞，直到Channel关闭）
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownNow();
        }
    }
}

class NettyClientHandler extends ChannelHandlerAdapter {

}