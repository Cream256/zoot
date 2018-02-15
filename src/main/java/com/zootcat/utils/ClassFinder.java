package com.zootcat.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {

    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    public static List<Class<?>> find(String scannedPackage, boolean includeSubDirs) 
    {
        String scannedPath = scannedPackage.replace(DOT, SLASH);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null)
        {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        
        String url = scannedUrl.getFile();
        if(url == null)
        {
            throw new IllegalArgumentException("Url is null for " + scannedPackage);
        }
        
        url = url.replace("%20", " ");  //escape spaces
                
        List<Class<?>> classes = new ArrayList<Class<?>>();
        
        File scannedItem = new File(url);
        if(url.contains(".jar!") || url.contains(".exe!"))
        {
            return findInJar(scannedPackage, url);
        }        
        else if(scannedItem.isDirectory())
        {
            for (File fileInDir : scannedItem.listFiles()) 
            {
                classes.addAll(find(fileInDir, scannedPackage, includeSubDirs));
            }    
        }
        else
        {
            throw new IllegalArgumentException("Unable to find classes for item: " + scannedItem);
        }        
        return classes;
    }

    private static List<Class<?>> findInJar(String scannedPackage, String jarUrl)
    {
        List<Class<?>> classes = new ArrayList<Class<?>>();        
        try
        {
            String jarPath = jarUrl.split("\\!")[0].substring(6);
            
            JarFile jar = new JarFile(jarPath);
            Enumeration<JarEntry> e = jar.entries();
            
            URL[] urls = { new URL("jar:file:" + jarPath + "!/") };
            URLClassLoader contextClassLoader = URLClassLoader.newInstance(urls);
                
            while (e.hasMoreElements()) 
            {
                JarEntry je = (JarEntry) e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class"))
                {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace(SLASH, DOT);
                
                if(!className.startsWith(scannedPackage))
                {
                    continue;
                }
                                
                try
                {
                    classes.add(contextClassLoader.loadClass(className));
                }
                catch (ClassNotFoundException ignore)
                {
                    //ignore
                }
            }
            jar.close();
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException(e);
        }
        return classes;
    }
    
    private static List<Class<?>> find(File file, String scannedPackage, boolean includeSubDirs) 
    {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory() && includeSubDirs) 
        {
            for (File child : file.listFiles()) 
            {
                classes.addAll(find(child, resource, includeSubDirs));
            }
        } 
        else if (resource.endsWith(CLASS_SUFFIX)) 
        {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try 
            {
                classes.add(Class.forName(className));
            } 
            catch (ClassNotFoundException ignore) 
            {
                //ignore
            }
        }
        return classes;
    }

}
