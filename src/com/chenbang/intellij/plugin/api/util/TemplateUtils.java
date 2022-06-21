package com.chenbang.intellij.plugin.api.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class TemplateUtils {
    public static void generateFile(String pathname, String templateName, Configuration configuration, Map<Object, Object> context) throws Exception {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname), StandardCharsets.UTF_8));
        Template template = configuration.getTemplate(templateName);
        template.process(context, out);
        out.close();
    }
}
