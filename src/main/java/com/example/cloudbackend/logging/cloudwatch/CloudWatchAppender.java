package com.example.cloudbackend.logging.cloudwatch;


import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.Serializable;

@Plugin(name = "CloudWatch", category = "Core", elementType = "appender")
public class CloudWatchAppender extends AbstractAppender {

    protected CloudWatchAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static CloudWatchAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Property") Property[] properties
    ) {
        return new CloudWatchAppender(
                name,
                filter,
                layout != null ? layout : PatternLayout.createDefaultLayout(),
                true,
                properties != null ? properties : new Property[0]
        );
    }

    @Override
    public void append(LogEvent event) {
        String message = new String(getLayout().toByteArray(event));
        CloudWatchBuffer.push(message, event.getTimeMillis());
    }




}