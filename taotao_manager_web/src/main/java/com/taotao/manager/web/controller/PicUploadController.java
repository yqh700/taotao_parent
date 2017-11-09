package com.taotao.manager.web.controller;

import com.taotao.common.PicUploadResult;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by 杨清华.
 * on 2017/11/8.
 */
@Controller
@RequestMapping("/pic/upload")
public class PicUploadController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    private static String[] TYPES = {".jpg", ".jpeg", ".png", ".gif", "bmp"};

    /**
     * 图片上传
     * @param uploadFile
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PicUploadResult upload(MultipartFile uploadFile) {
        //声明标志位
        boolean flag = false;

        //构建返回数据，并将上传状态设置为失败
        PicUploadResult result = new PicUploadResult();
        result.setError(1);

        //校验上传文件的后缀
        for(String type: TYPES ) {
            String filename = uploadFile.getOriginalFilename();
            if(StringUtils.endsWithIgnoreCase(filename, type)) {
                flag = true;
                break;
            }
        }

        //如果校验失败，直接返回
        if(!flag) {
            return result;
        }

        //重置标志位
        flag = false;

        //图片内容校验
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            result.setHeight(String.valueOf(bufferedImage.getHeight()));
            result.setWidth(String.valueOf(bufferedImage.getWidth()));
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //校验成功，上传图片
        if(flag) {
            try {
                ClientGlobal.init(System.getProperty("user.dir") + "/src/main/resources/tracker.conf");
                TrackerClient trackerClient = new TrackerClient();
                TrackerServer trackerServer = trackerClient.getConnection();
                StorageServer storageServer = null;
                StorageClient storageClient = new StorageClient(trackerServer, storageServer);

                //获得文件扩展名
                String extName = org.apache.commons.lang3.StringUtils.substringAfterLast(uploadFile.getOriginalFilename(), ".");
                //上传
                String[] paths = storageClient.upload_file(uploadFile.getBytes(), extName, null);
                String picUrl = IMAGE_SERVER_URL + "/" + paths[0] + "/" + paths[1];
                result.setUrl(picUrl);
                result.setError(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MyException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
