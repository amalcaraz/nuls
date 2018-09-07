package io.nuls.contract.vm.code;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.nuls.contract.vm.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassCodeLoader {

    public static final Map<String, ClassCode> RESOURCE_CLASS_CODES;

    private static final LoadingCache<ClassCodeCacheKey, Map<String, ClassCode>> CACHE;

    static {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterAccess(10 * 60, TimeUnit.SECONDS)
                .build(new CacheLoader<ClassCodeCacheKey, Map<String, ClassCode>>() {
                    @Override
                    public Map<String, ClassCode> load(@Nonnull final ClassCodeCacheKey cacheKey) {
                        return ClassCodeLoader.loadJar(cacheKey.getBytes());
                    }
                });
        RESOURCE_CLASS_CODES = loadFromResource();
    }

    public static void init() {

    }

    public static ClassCode load(String className) {
        try {
            ClassReader classReader = new ClassReader(className);
            return load(classReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassCode loadFromResource(String className) {
        ClassCode classCode = RESOURCE_CLASS_CODES.get(className);
        if (classCode == null) {
            throw new RuntimeException("can't load class " + className);
        } else {
            return classCode;
        }
    }

    public static Map<String, ClassCode> loadFromResource() {
        Map<String, ClassCode> map = new HashMap<>();
        InputStream inputStream = ClassCodeLoader.class.getResourceAsStream("/used_classes");
        if (inputStream == null) {
            return map;
        } else {
            return loadJar(inputStream);
        }
    }

    public static ClassCode loadFromResourceOrTmp(String className) {
        ClassCode classCode = RESOURCE_CLASS_CODES.get(className);
        if (classCode == null) {
            try {
                File file = new File("/tmp/classes/" + className + ".class");
                if (file.exists()) {
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    return load(bytes);
                } else {
                    throw new RuntimeException("can't load class " + className);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return classCode;
        }
    }

    public static ClassCode load(byte[] bytes) {
        return load(new ClassReader(bytes));
    }

    public static ClassCode load(ClassReader classReader) {
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        ClassCode classCode = ClassCodeConver.toClassCode(classNode);
        return classCode;
    }

    public static void load(Map<String, ClassCode> classCodes, String className, Function<String, ClassCode> loader) {
        className = Utils.classNameReplace(className);
        if (!classCodes.containsKey(className)) {
            ClassCode classCode = loader.apply(className);
            classCodes.put(className, classCode);
            if (StringUtils.isNotEmpty(classCode.getSuperName())) {
                load(classCodes, classCode.getSuperName(), loader);
            }
            for (String interfaceName : classCode.getInterfaces()) {
                load(classCodes, interfaceName, loader);
            }
            for (MethodCode methodCode : classCode.getMethods()) {
                if (isSupport(methodCode.getReturnVariableType())) {
                    load(classCodes, methodCode.getReturnVariableType().getType(), loader);
                }
                for (VariableType variableType : methodCode.getArgsVariableType()) {
                    if (isSupport(variableType)) {
                        load(classCodes, variableType.getType(), loader);
                    }
                }
            }
        }
    }

    private static boolean isSupport(VariableType variableType) {
        if (variableType.isPrimitiveType()) {
            return false;
        } else if (variableType.isVoid()) {
            return false;
        } else {
            return true;
        }
    }


    public static Map<String, ClassCode> loadJar(byte[] bytes) {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return loadJar(inputStream);
    }

    public static Map<String, ClassCode> loadJarCache(byte[] bytes) {
        try {
            return CACHE.get(new ClassCodeCacheKey(bytes));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, ClassCode> loadJar(InputStream inputStream) {
        try {
            JarInputStream jarInputStream = new JarInputStream(inputStream);
            return loadJar(jarInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, ClassCode> loadJar(JarInputStream jarInputStream) {
        Map<String, ClassCode> map = new HashMap<>();
        try {
            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    IOUtils.copy(jarInputStream, outputStream);
                    byte[] bytes = outputStream.toByteArray();
                    ClassCode classCode = load(bytes);
                    map.put(classCode.getName(), classCode);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

}