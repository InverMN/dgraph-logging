package me.inver.app;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin(
        name = "DgraphAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class DgraphAppender extends AbstractAppender {
    String text = null;
    InputStream inputStream = null;

    private ConcurrentMap<String, LogEvent> eventMap = new ConcurrentHashMap();

    protected DgraphAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @PluginFactory
    public static DgraphAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new DgraphAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        try {
            String rawData = "{\"loggerName\": \"" + "Root" + "\", \"message\": \"" + event.getMessage() + "\", \"level\": \"" + event.getLevel() + "\", \"timeMilis\": " + event.getTimeMillis() + " }";
            String encodedData = URLEncoder.encode(rawData, "UTF-8");
            var url = new URL("https://en61gr36k3kczz1.m.pipedream.net");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(encodedData.getBytes());
            inputStream = connection.getInputStream();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
        eventMap.put(Instant.now().toString(), event);
    }
}