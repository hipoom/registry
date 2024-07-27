package com.hipoom.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ZhengHaiPeng
 * @since 4/25/24 11:44 PM
 */
public class Registry {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private static boolean isInitialed;

    /**
     * Store the mapping from the interface to all implementation classes of that interface.
     */
    private static final Map<Class<?>, Set<Class<?>>> annotation2Classes = new HashMap<>();

    /**
     * Store the mapping from the annotation to all annotated classes of that annotation.
     */
    private static final Map<Class<?>, Set<Class<?>>> interface2Classes = new HashMap<>();



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * Get all annotated classes of that annotation.
     */
    public static synchronized Set<Class<?>> getClassesAnnotatedWith(Class<?> annotation) {
        prepare();
        Set<Class<?>> res = annotation2Classes.get(annotation);
        if (res == null) {
            return new HashSet<>();
        }
        return res;
    }

    /**
     * Get all annotated classes of that annotation.
     */
    public static synchronized Set<Class<?>> getClassesImplements(Class<?> i) {
        prepare();
        Set<Class<?>> res = interface2Classes.get(i);
        if (res == null) {
            return new HashSet<>();
        }
        return res;
    }

    /**
     * Register a mapping relationship.
     */
    public static synchronized void addAnnotation(Class<?> annotation, Class<?> annotatedClass) {
        Set<Class<?>> res = annotation2Classes.get(annotation);
        //noinspection Java8MapApi
        if (res == null) {
            res = new HashSet<>();
            annotation2Classes.put(annotation, res);
        }
        res.add(annotatedClass);
    }

    /**
     * Register a mapping relationship.
     */
    public static synchronized void addInterface(Class<?> interFace, Class<?> klass) {
        Set<Class<?>> res = interface2Classes.get(interFace);
        //noinspection Java8MapApi
        if (res == null) {
            res = new HashSet<>();
            interface2Classes.put(interFace, res);
        }
        res.add(klass);
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * The Gradle plugin will insert code into this method during compilation.
     * like: addAnnotation(SomeAnnotation.class, Annotated.class);
     * or: addInterface(SomeInterface.class, ImplementationClass.class);
     */
    private static void init() {

    }

    /**
     * Before calling {@link #getClassesAnnotatedWith(Class)} or {@link #getClassesImplements(Class)},
     * this method will be called.
     */
    private synchronized static void prepare() {
        if (isInitialed) {
            return;
        }
        init();
        isInitialed = true;
    }

}
