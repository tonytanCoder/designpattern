package com.tan.agent;

import com.google.common.collect.Lists;
import com.tan.agent.classloader.AgentClassLoader;
import org.springframework.boot.loader.LaunchedURLClassLoader;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.JarFileArchive;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.jar.Attributes;

/**
 * mvn clean package
 */
public class MyAgent {

    private static final String LOGGING_PROPERTY = "Logging-Property";

    private static final String LIB = "lib/"; // "lib/"

    private static final String EASEAGENT_LOG_CONF = "my.log.conf";

    public static final ClassLoader BOOTSTRAP_CLASS_LOADER = null;

    //JVM 首先尝试在代理类上调用以下方法
    public static void premain(String agentArgs, Instrumentation inst) throws Exception {

        System.out.println("基于javaagent链路追踪");
        System.out.println("==========================================================\r\n");

        final JarFileArchive archive = new JarFileArchive(getArchiveFileContains(MyAgent.class));
        final Attributes attributes = archive.getManifest().getMainAttributes();
        final String loggingProperty = attributes.getValue(LOGGING_PROPERTY); //
        final String bootstrap =  "com.tan.agent.StartBootstrap";//attributes.getValue("Bootstrap-Class");
        System.out.println("JarFileArchive:"+archive.getUrl());
        final URL[] urls = nestArchiveUrls(archive);
        Arrays.stream(urls).forEach(url -> System.out.println("url:"+ url.getPath()));
        final ClassLoader loader = new CompoundableClassLoader(urls);

        switchLoggingProperty(loader, loggingProperty, EASEAGENT_LOG_CONF, new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                loader.loadClass(bootstrap)
                        .getMethod("premain", String.class, Instrumentation.class)
                        .invoke(null, agentArgs, inst);
                return null;
            }
        });



    }

    private static void switchLoggingProperty(ClassLoader loader, String hostKey, String agentKey, Callable<Void> callable)
            throws Exception {
        final Thread t = Thread.currentThread();
        final ClassLoader ccl = t.getContextClassLoader();
        t.setContextClassLoader(loader);

        final String host = System.getProperty(hostKey);
        final String agent = System.getProperty(agentKey, "log4j2.xml");
        // Redirect config of host to agent
        System.setProperty(hostKey, agent);
        try {
            callable.call();
        } finally {
            t.setContextClassLoader(ccl);
            // Recovery host configuration
            if (host == null) {
                System.getProperties().remove(hostKey);
            } else {
                System.setProperty(hostKey, host);
            }
        }

    }

    private static File getArchiveFileContains(Class<?> klass) throws URISyntaxException {
        final ProtectionDomain protectionDomain = klass.getProtectionDomain();
        final CodeSource codeSource = protectionDomain.getCodeSource();
        final URI location = (codeSource == null ? null : codeSource.getLocation().toURI());
        final String path = (location == null ? null : location.getSchemeSpecificPart());
        System.out.println("getArchiveFileContains:"+path);
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        final File root = new File(path);
        if (!root.exists() || root.isDirectory()) {
            throw new IllegalStateException("Unable to determine code source archive from " + root);
        }
        return root;
    }



    private static URL[] nestArchiveUrls(Archive archive) throws IOException {
        ArrayList<Archive> archives = Lists.newArrayList(
                archive.getNestedArchives(entry -> !entry.isDirectory() && entry.getName().startsWith(LIB),
                        entry -> true
                ));
        final URL[] urls = new URL[archives.size()];

        for (int i = 0; i < urls.length; i++) {
            urls[i] = archives.get(i).getUrl();
        }

        return urls;
    }


    public static class CompoundableClassLoader extends LaunchedURLClassLoader {
        private final Set<ClassLoader> externals = new CopyOnWriteArraySet<ClassLoader>();

        CompoundableClassLoader(URL[] urls) {
//            super(urls, ClassLoader.getSystemClassLoader());
            super(urls, MyAgent.BOOTSTRAP_CLASS_LOADER);
        }




        public void add(ClassLoader cl) {
            if (cl != null && !Objects.equals(cl, this)) {
                externals.add(cl);
            }
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            try {
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException e) {
                for (ClassLoader external : externals) {
                    try {
                        final Class<?> aClass = external.loadClass(name);
                        if (resolve) resolveClass(aClass);
                        return aClass;
                    } catch (ClassNotFoundException ignore) {
                    }
                }

                throw e;
            }
        }
    }
}
