package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.response.CaptchaResult;

public interface CaptchaService {

    /** 生成验证码图片（base64）并返回 captchaId */
    CaptchaResult generate();

    /**
     * 校验验证码（校验后立即作废，一次性使用）
     * @return true 通过；false 未通过/已过期/不存在
     */
    boolean verify(String captchaId, String captchaCode);
}
