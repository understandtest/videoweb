
package com.videoweb.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public final class ImageUtil {
	private ImageUtil() {
	}

	/**
	 * * 转换图片大小，不变形
	 * 
	 * @param img 图片文件
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public static final void changeImge(File img, int width, int height) {
		try {
			Thumbnails.of(img).size(width, height).keepAspectRatio(false).toFile(img);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("图片转换出错！", e);
		}
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param orgImgFile 源图片路径
	 * @param scale 比例
	 * @param targetFile 缩放后的图片存放路径
	 * @throws IOException
	 */
	public static final void scale(BufferedImage orgImg, double scale, String targetFile) throws IOException {
		Thumbnails.of(orgImg).scale(scale).toFile(targetFile);
	}

	public static final void scale(String orgImgFile, double scale, String targetFile) throws IOException {
		Thumbnails.of(orgImgFile).scale(scale).toFile(targetFile);
	}

	/**
	 * 图片格式转换
	 * 
	 * @param orgImgFile
	 * @param width
	 * @param height
	 * @param suffixName
	 * @param targetFile
	 * @throws IOException
	 */
	public static final void format(String orgImgFile, int width, int height, String suffixName, String targetFile)
			throws IOException {
		Thumbnails.of(orgImgFile).size(width, height).outputFormat(suffixName).toFile(targetFile);
	}

	/**
	 * 根据宽度同比缩放
	 * 
	 * @param orgImg 源图片
	 * @param orgWidth 原始宽度
	 * @param targetWidth 缩放后的宽度
	 * @param targetFile 缩放后的图片存放路径
	 * @throws IOException
	 */
	public static final double scaleWidth(BufferedImage orgImg, int targetWidth, String targetFile) throws IOException {
		int orgWidth = orgImg.getWidth();
		// 计算宽度的缩放比例
		double scale = targetWidth * 1.00 / orgWidth;
		// 裁剪
		scale(orgImg, scale, targetFile);

		return scale;
	}

	public static final void scaleWidth(String orgImgFile, int targetWidth, String targetFile) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File(orgImgFile));
		scaleWidth(bufferedImage, targetWidth, targetFile);
	}

	/**
	 * 根据高度同比缩放
	 * 
	 * @param orgImgFile //源图片
	 * @param orgHeight //原始高度
	 * @param targetHeight //缩放后的高度
	 * @param targetFile //缩放后的图片存放地址
	 * @throws IOException
	 */
	public static final double scaleHeight(BufferedImage orgImg, int targetHeight, String targetFile) throws IOException {
		int orgHeight = orgImg.getHeight();
		double scale = targetHeight * 1.00 / orgHeight;
		scale(orgImg, scale, targetFile);
		return scale;
	}

	public static final void scaleHeight(String orgImgFile, int targetHeight, String targetFile) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File(orgImgFile));
		// int height = bufferedImage.getHeight();
		scaleHeight(bufferedImage, targetHeight, targetFile);
	}

	// 原始比例缩放
	public static final void scaleWidth(File file, Integer width) throws IOException {
		String fileName = file.getName();
		String filePath = file.getAbsolutePath();
		String postFix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		// 缩放
		BufferedImage bufferedImg = ImageIO.read(file);
		String targetFile = filePath + "_s" + postFix;
		scaleWidth(bufferedImg, width, targetFile);
		String targetFile2 = filePath + "@" + width;
		new File(targetFile).renameTo(new File(targetFile2));
	}
	
	
	//对图片文件进行Base64编码  
    public static String getImagebase64(String imgFileName) {  
        byte[] data = null;  
        try {  
            InputStream in = new FileInputStream(imgFileName);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);  
    }  
	
    /**
     * Base64解码并保存图片文件  
     * Implement: <br>
     *
     * @param base64
     * @param imgFileName 
     * @see
     */
    public void saveImage(String base64, String imgFileName) {  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {  
            byte[] bytes = decoder.decodeBuffer(base64);  
            for (int i = 0; i < bytes.length; ++i) {  
                if (bytes[i] < 0) {  
                    bytes[i] += 256;  
                }  
            }  
            OutputStream out = new FileOutputStream(imgFileName);  
            out.write(bytes);  
            out.flush();  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    /**
     * 
     * Description: 根据图片字节流获取图片后缀
     *
     * @param photoByte
     * @return 
     * @see
     */
    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = ".jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70)
                && (photoByte[3] == 56) && ((photoByte[4] == 55) || (photoByte[4] == 57))
                && (photoByte[5] == 97)) {
            strFileExtendName = ".gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73)
                && (photoByte[9] == 70)) {
            strFileExtendName = ".jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = ".bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = ".png";
        }
        return strFileExtendName;
    }
	
	
}
