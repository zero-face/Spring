package com.zero.controller;

import com.zero.error.BusinessException;
import com.zero.error.EmBustinessError;
import com.zero.response.CommonReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/4/11 12:30
 * @Since 1.8
 **/
@RestController
@RequestMapping("/file")
/**
 * 文件上传,由于springboot默认的文件上传会限制文件上传，所以需要修改配置
 */
@Slf4j
public class FileUploadController extends BaseController{

    @PostMapping("/upload")
    public CommonReturnType upload(@RequestParam(value = "username" ,required = false)String username,
                                   @RequestParam(value = "email",required = false)String email,
                                   @RequestPart(value = "file",required = false)MultipartFile file
//                                   @RequestPart(value = "files",required = false)MultipartFile[] files
                                   ) throws IOException, BusinessException {
        log.info(username);
        log.info(email);
//            if(files != null && files.length > 0) {
//                for(int i = 0; i < files.length; i++) {
//                    if(!files[i].isEmpty()) {
//                        String originalFilename = files[i].getOriginalFilename();
//                        files[i].transferTo(new File("F://" + originalFilename));
//                        System.out.println("单文件保存成功");
//                        return CommonReturnType.create(null);
//                    }
//                }
//            }

        if( file != null && !file.isEmpty()) {    //如果长传的文件不是空
            //保存到文件服务器，OSS服务器
            String originalFilename = file.getOriginalFilename();
            System.out.println("多文件保存成功");
            file.transferTo(new File("F://" + originalFilename) );
            return CommonReturnType.create(null);
        }

        throw new BusinessException(EmBustinessError.PARAMETER_VALIDATION_ERROR, "上传的文件为空");

    }



}
