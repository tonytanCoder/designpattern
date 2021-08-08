package com.tan.agent.plugin;

import com.tan.agent.plugin.impl.jvm.JvmPlugin;
import com.tan.agent.plugin.impl.link.LinkPlugin;

import java.util.ArrayList;
import java.util.List;

public class PluginFactory {

    public static List<IPlugin> pluginGroup = new ArrayList<>();

    static {
        //链路监控
        pluginGroup.add(new LinkPlugin());
        //Jvm监控
        pluginGroup.add(new JvmPlugin());
    }

}
