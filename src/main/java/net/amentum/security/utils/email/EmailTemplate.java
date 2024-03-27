package net.amentum.security.utils.email;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class EmailTemplate {

    private String templateId;

    private String template;

    private Map<String, String> replacementParams;

    public EmailTemplate(String templateId) {
        this.templateId = templateId;
        try {
            if(!templateId.isEmpty())
                this.template = loadTemplate(templateId);
        } catch (Exception e) {
            this.template = "";
        }
    }

    private String loadTemplate(String templateId) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("email-templates/" + templateId).getFile());
        String content = "";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new Exception("Error al leer template = " + templateId);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = this.getTemplate();

        if (!cTemplate.isEmpty()) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        return cTemplate;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, String> getReplacementParams() {
        return replacementParams;
    }

    public void setReplacementParams(Map<String, String> replacementParams) {
        this.replacementParams = replacementParams;
    }
}
