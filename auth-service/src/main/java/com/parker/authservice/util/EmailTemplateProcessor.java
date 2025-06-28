package com.parker.authservice.util;

import com.parker.authservice.exception.UnableToReadResourceException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
@Component
public class EmailTemplateProcessor {
    public String processTemplate(String templateName, Map<String, String> values) {
        ClassPathResource resource = new ClassPathResource("templates" + File.separator + templateName);
        try {
            byte[] bytes = resource.getInputStream().readAllBytes();
            String template = new String(bytes, StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : values.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return template;
        } catch (IOException e) {
            throw new UnableToReadResourceException("Failed to load email template: " + templateName, e);
        }
    }
}
