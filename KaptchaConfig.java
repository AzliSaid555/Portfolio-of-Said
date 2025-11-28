package com.example.captchaservice;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha k = new DefaultKaptcha();
        Properties p = new Properties();
        p.put("kaptcha.border", "no");
        p.put("kaptcha.textproducer.char.length", "5");
        p.put("kaptcha.textproducer.font.color", "black");
        k.setConfig(new Config(p));
        return k;
    }
}
