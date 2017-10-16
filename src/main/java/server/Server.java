package server;

import channel.ServerChanel;
import config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * Created by sergey on 10.10.17.
 */
public class Server {
    private static String root;
    private int port;
    private int cpuLimit;

    public Server (ServerConfig serverConfig) {
        this.root = serverConfig.getRoot();
        this.cpuLimit = serverConfig.getCpuLimit();
        this.port = serverConfig.getPort();
    }

    public void start() throws InterruptedException {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup(cpuLimit);
        try {
            final ServerBootstrap server = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new ServerChanel(root));
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        }
                    });
            System.out.println("Port: " + port);
            System.out.println("Root dir: " + root);
            System.out.println("Cpu limit: " + cpuLimit);
            final ChannelFuture f = server.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static String getRoot() {
        return root;
    }
}
