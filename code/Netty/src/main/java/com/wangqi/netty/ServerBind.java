package com.wangqi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ServerBind {
    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        //创建BoosGroup和WorkGroup
        NioEventLoopGroup boosGroup=new NioEventLoopGroup(1);
        //配置NioEventLoopGroup
        NioEventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)//设置Channel类型为Nio
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        //自定义编解码器
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast("decoder",new MessageDecoder());
                            nioSocketChannel.pipeline().addLast("encoder",new MessageEncoder());
                        }
                    });
            //绑定端⼝,调⽤ sync ⽅法阻塞知道绑定完成
            ChannelFuture f=serverBootstrap.bind(port).sync();
            //阻塞等待直到服务器Channel关闭(closeFuture()⽅法获取Channel的CloseFuture对象,然后调⽤sync()⽅法)
            f.channel().closeFuture().sync();
        } finally {
            //优雅关闭相关线程组资源
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

class MessageDecoder extends ChannelHandlerAdapter {
    //具体业务
}

class MessageEncoder extends ChannelHandlerAdapter {
    //具体业务
}
