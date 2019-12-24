package com.cjyc.web.api.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 * @Author: Hut
 * @Date: 2019/12/19 11:34
 */
public class QRcodeUtil {
    public static String creatRrCode(String contents, int width, int height) {
        Hashtable hints = new Hashtable();
        //设置容错级别最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置字符编码为utf-8
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //二维码空白区域,最小为0也有白边,只是很小,最小是6像素左右
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            // 1、读取文件转换为字节数组
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            //转为图片对象格式
            BufferedImage image = toBufferedImage(bitMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //转换成png格式的IO流
            ImageIO.write(image, "png", outputStream);

            return Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * image流数据处理
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
