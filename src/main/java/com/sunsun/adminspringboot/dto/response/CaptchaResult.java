package com.sunsun.adminspringboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "验证码结果")
public class CaptchaResult {

    @Schema(description = "验证码唯一ID（登录时原样回传）")
    private String captchaId;

    @Schema(description = "验证码图片（base64 data URL）")
    private String img;
}
