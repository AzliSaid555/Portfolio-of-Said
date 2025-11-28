package com.example.captchaservice;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class CaptchaController {

    private final DefaultKaptcha captchaProducer;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    // حقن الـ Bean عبر الـ Constructor
    public CaptchaController(DefaultKaptcha captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @GetMapping("/captcha/new")
    public Map<String, String> newCaptcha() throws Exception {
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);
        String id = UUID.randomUUID().toString();
        cache.put(id, text);
        ImageIO.write(image, "png", new File("/tmp/" + id + ".png"));
        return Map.of(
                "captchaId", id,
                "imageUrl", "/captcha/image/" + id
        );
    }

    @GetMapping(value="/captcha/image/{id}", produces= MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable String id) throws Exception {
        return Files.readAllBytes(Paths.get("/tmp/" + id + ".png"));
    }

    @PostMapping("/captcha/verify")
    public Map<String, Boolean> verify(@RequestBody Map<String, String> data) {
        String id = data.get("id");
        String answer = data.get("answer");
        boolean ok = cache.containsKey(id) && cache.get(id).equalsIgnoreCase(answer);
        if (ok) cache.remove(id);
        return Map.of("success", ok);
    }
}
