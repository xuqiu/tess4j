package xuqiu.github.io;

import net.sourceforge.tess4j.Tesseract1;
import com.alibaba.fastjson.JSON;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchOperator {
    private static Tesseract1 instance = new Tesseract1();
    private static final Rectangle CODE_RECT_RAW = new Rectangle(745, 782, 470, 32);
    private static final Rectangle CODE_RECT = new Rectangle(82, 245, 245, 17);
    private static final Rectangle CODE_BPM = new Rectangle(266, 270, 48, 21);
    private static final Rectangle CODE_BPM_RAW = new Rectangle(1105, 835, 68, 35);
    String output = "C:\\Users\\yinzhennan\\Pictures\\e52\\";
    public static void main(String[] args) throws Exception {
        //1批量截取压缩
//        List<String> fileList = getFileList("C:\\Users\\yinzhennan\\Pictures\\e5\\");
//        System.out.println(fileList.size());
//        for (String s : fileList) {
//            cutImages(s);
//        }
        //2,获取歌曲信息
        List<String> fileList = getFileList("C:\\Users\\yinzhennan\\Pictures\\e5\\");
        System.out.println(fileList.size());
        List<Song> songList = new ArrayList<>(fileList.size());
        int i=0;
        for (String file : fileList) {
            Song song = getInfo(file);
            songList.add(song);
            System.out.println(i++);
            System.out.println(JSON.toJSONString(song));
        }
        System.out.println(JSON.toJSONString(songList));

        //////

//        List<String> fileList = getFileList("C:\\Users\\yinzhennan\\Pictures\\e5\\");
//        System.out.println(fileList.size());
//        for (String s : fileList) {
//            getImg(s);
//        }
    }
    
    private static void getImg(String srcPath){
        OperateImage imageObj = new OperateImage();
        String fileName = srcPath.substring(srcPath.lastIndexOf("\\"));
        String toPath = "C:\\Users\\yinzhennan\\Pictures\\e55\\"+fileName+"testCode.jpg";
        String writeImageFormat = "jpg";
        try {
            BufferedImage tag = imageObj.readBufferedImage(srcPath, CODE_RECT_RAW);
            //保存新图片
            ImageIO.write(tag, writeImageFormat, new File(toPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Song getInfo(String srcPath) throws Exception {
        OperateImage imageObj = new OperateImage();
        Song song = new Song();
        File file = new File(srcPath);
        String fileName = srcPath.substring(srcPath.lastIndexOf("\\")+1);
        song.setCode(fileName);
        BufferedImage image = ImageIO.read(file); // require jai-imageio lib to read TIFF
        //获取code
        BufferedImage codeBI = imageObj.readBufferedImage(srcPath, CODE_RECT_RAW);
        String code = OCRUtil.getString(codeBI,"eng").trim();
        song.setE5code(code);
        //获取bpm
        BufferedImage codeBpm = imageObj.readBufferedImage(srcPath, CODE_BPM_RAW);
        String bpm = OCRUtil.getString(codeBpm,"eng").trim();
        song.setBpm(bpm);
        return song;
    }

    private static void cutImages(String srcPath){
        OperateImage imageObj = new OperateImage();
        String fileName = srcPath.substring(srcPath.lastIndexOf("MWSnap")+6);
        String toPath = "C:\\Users\\yinzhennan\\Pictures\\e52\\21_"+fileName;
        int x = 567;
        int y = 291;
        int width = 1402-x;
        int height = 913-y ;
        String readImageFormat = "jpg";
        String writeImageFormat = "jpg";
        try {
            imageObj.cropAndReduceImage(srcPath, toPath, x, y, width, height,2,readImageFormat,writeImageFormat);//剪切图片
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static List<String> getFileList(String path) {
        List<String> result = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("文件夹是空的!");
                return result;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        List<String> fileList = getFileList(file2.getAbsolutePath());
                        result.addAll(fileList);
                    } else {
                        String filePath = file2.getAbsolutePath();
                        result.add(filePath);
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return result;
    }
}
