package channel;

import http.Request;
import http.Response;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by sergey on 11.10.17.
 */
@ChannelHandler.Sharable
public class ServerChanel extends ChannelInboundHandlerAdapter {
    private String root;

    private Request request;
    private Response response;


    public ServerChanel(String root) {
        this.root = root;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            request = new Request((String) msg);
            response = new Response(request);
        } finally {
            ReferenceCountUtil.release(msg);
        }
        final ChannelFuture channelfuture = ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer(response.getResponseAnswer().getBytes()));
        //if (!response.isTextFile()) {
        if (response.needWriteFile()) {
            //final RandomAccessFile file;
            try (final RandomAccessFile file = new RandomAccessFile(response.getFullPath(), "r")) {
                //file = new RandomAccessFile(response.getFullPath(), "r");
                long length = 0;
                try{
                    length = file.length();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(new DefaultFileRegion(file.getChannel(), 0, length));
            } catch (FileNotFoundException ignored) {}
        }
        channelfuture.addListener(ChannelFutureListener.CLOSE);
    }
        //super.channelRead(ctx, msg);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //super.exceptionCaught(ctx, cause);
    }
}
