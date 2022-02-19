package org.csp.rpc.core.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ExtensionLoader<T> {

    private static final String [] EXTENSION_FILE_PATH = {
            "META-INF/rpc/internal/",
            "META-INF/rpc/"
    };

    private static final Map<String, ExtensionLoader> cacheMap = new HashMap<>();

    private Class<T> cls;

    private T defaultExtension;

    private Map<String, T> implMap = new HashMap<>();

    public T getDefault() {
        return defaultExtension;
    }

    public T get(String name) {
        return implMap.get(name);
    }

    public static <T> ExtensionLoader<T> loadExtensionInfo(Class<T> cls) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (String filePath : EXTENSION_FILE_PATH) {
            String file = filePath + cls.getName();

            if (cacheMap.containsKey(cls.getName())) {
                continue;
            }
            Enumeration<URL> resources = null;

            try {
                resources = loader.getResources(file);
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    loadExtension(cls, url);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return cacheMap.get(cls.getName());
    }

    private static <T> void loadExtension(Class<T> cls, URL resourceURL) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
            String line = null;

            ExtensionLoader<?> extensionLoader = new ExtensionLoader<>(cls);

            while ((line = reader.readLine()) != null) {
                String[] contents = line.split("=");
                String key = contents[0];
                String value = contents[1];
                extensionLoader.addImpl(key, Class.forName(value));
            }

            extensionLoader.setDefaultExtension(cls);

            cacheMap.put(cls.getName(), extensionLoader);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> void setDefaultExtension(Class<T> cls) {
        SPI spi = cls.getAnnotation(SPI.class);
        String key = spi.value();
        this.defaultExtension = implMap.get(key);

        if (this.defaultExtension == null) {
            throw new RuntimeException(cls.getName() +" not found default extension");
        }
    }

    private ExtensionLoader(Class<T> cls) {
        this.cls = cls;
    }

    private void addImpl(String name, Class<?> cls) {
        try {
            Object o = cls.newInstance();
            implMap.put(name, (T) o);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
