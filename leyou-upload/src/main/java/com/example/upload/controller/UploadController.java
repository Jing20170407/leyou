package com.example.upload.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("upload")
public class UploadController {

    private static final List<String> CONTENT_TYPE_LIST = Arrays.asList("image/png", "image/jpeg", "image/git");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        //验证格式
        if (!CONTENT_TYPE_LIST.contains(file.getContentType())) {
            LOGGER.info("文件格式不正确：{}", file.getOriginalFilename());
            return ResponseEntity.badRequest().build();
        }
        //验证内容
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new Exception("内容有误");
            }
        } catch (Exception e) {
            LOGGER.info("文件内容不正确：{} , {}", file.getOriginalFilename(),e.toString());
            return ResponseEntity.badRequest().build();
        }

        //保存到文件服务器
        StorePath storePath;
        try {
            String sufix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            LOGGER.info("storageClient.uploadFile(*,{},{},*)",file.getSize(),sufix);
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), sufix, null);
        } catch (Exception e) {
            LOGGER.error("文件上传失败：{} , {}", file.getOriginalFilename(),e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        //返回路径
        return ResponseEntity.ok("http://image.leyou.com/"+storePath.getFullPath());
    }
}
