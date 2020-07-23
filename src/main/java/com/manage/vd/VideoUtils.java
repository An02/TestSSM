package com.manage.vd;

import com.manage.controller.UserController;
import io.netty.util.internal.ObjectUtil;
import org.apache.log4j.Logger;
import ws.schild.jave.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/5/1 23:22
 * @Version: 1.0
 */
public class VideoUtils {
    private static Logger logger = Logger.getLogger(VideoUtils.class);
    private static Map<String, Integer> sizeBitRateMap;

    static {
        sizeBitRateMap = new HashMap<>();
        sizeBitRateMap.put("1920*1080", 4992);
        sizeBitRateMap.put("1280*720", 2496);
        sizeBitRateMap.put("1024*576", 1856);
        sizeBitRateMap.put("840*480", 1216);
        sizeBitRateMap.put("768*432", 1088);
        sizeBitRateMap.put("640*360", 896);
        sizeBitRateMap.put("424*240", 576);
    }


    public static void main(String[] args) {
        VideoUtils videoUtils = new VideoUtils();
        videoUtils.convertVideoToMP4(new File("C:\\vd\\靠得住的男人.flv"),"C:\\vd\\靠得住的男人1.mp4");

        //videoUtils.getVideoInfoAndGenerateThumbnail(new File("C:\\\\temp\\\\javae2\\\\0001.哔哩哔哩-颈椎操[流畅版].mp4"), "C:\\\\temp\\\\javae2\\\\0001.哔哩哔哩-颈椎操[流畅版]..jpg");


    }

    /**
     * 截取视频的一针作为封面图
     *
     * @param file          视频文件
     * @param thumbnailPath 截取图片保存路径
     * @return
     */
    public void getVideoInfoAndGenerateThumbnail(File file, String thumbnailPath) {
        MultimediaObject multimediaObject = new MultimediaObject(file);
        try {
            MultimediaInfo info = multimediaObject.getInfo();
            VideoInfo videoInfo = info.getVideo();
            logger.info("获取视频时长：{}"+(info.getDuration() / 1000));
            if (videoInfo != null) {
                VideoSize size = videoInfo.getSize();
                int width = size.getWidth();
                int height = size.getHeight();
                logger.info("视频宽：{} 视频高{}"+width+height);
                logger.info("比特率：{}"+(videoInfo.getBitRate() / 1000));
                ScreenExtractor screenExtractor = new ScreenExtractor();
                File target = new File(thumbnailPath);
                //截取视频作为图片保存
                /*
                 *第一个参数 视频源文件信息类
                 * 第二个参数 截取的宽度
                 * 第三个参数  截取的高度
                 * 第四个参数  截取的是那一帧
                 * 第五个参数是  截取的图片质量 1-31   数字越小质量越高
                 *
                 **/
                //screenExtractor.renderOneImage(multimediaObject, size.getWidth(), size.getHeight(), 3000, target, 31);

            }
        } catch (EncoderException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param source     源文件
     * @param targetPath 转码后的路径
     */
    public void convertVideoToMP4(File source, String targetPath) {
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
            MultimediaInfo info = multimediaObject.getInfo();
            VideoInfo videoInfo = info.getVideo();
            VideoSize size = videoInfo.getSize();
            System.out.println("原视频宽：" + size.getWidth());
            System.out.println("原视频高：" + size.getHeight());
            System.out.println("原视频比特率：" + videoInfo.getBitRate() / 1000);
            System.out.println("原视频编码：" + videoInfo.getDecoder());

            Integer bitRate = sizeBitRateMap.get(size.getWidth() + "*" + size.getHeight());
            VideoAttributes video = new VideoAttributes();
            //设置视频编码
            video.setCodec("h264");

            if (bitRate != null) {
                //设置比特率
                video.setBitRate(bitRate * 1000);
            }
            File target = new File(targetPath);
            AudioAttributes audio = new AudioAttributes();
            //设置编码器名称
            audio.setCodec("aac");
            EncodingAttributes attrs = new EncodingAttributes();
            //设置转换后的格式
            attrs.setFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, target, attrs);
            //花费毫秒数

            MultimediaObject multimediaObjectOfter = new MultimediaObject(Paths.get(targetPath).toFile());
            MultimediaInfo info1 = multimediaObjectOfter.getInfo();
            VideoInfo video1 = info1.getVideo();
            VideoSize size1 = video1.getSize();

            System.out.println("转换后视频宽：" + size1.getWidth());
            System.out.println("转换后视频高：" + size1.getHeight());
            System.out.println("转换后视频比特率：" + video1.getBitRate() / 1000);
            System.out.println("转换后视频编码：" + video1.getDecoder());

        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }
}
