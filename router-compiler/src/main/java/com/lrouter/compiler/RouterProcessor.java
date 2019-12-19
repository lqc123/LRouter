package com.lrouter.compiler;

import com.google.auto.service.AutoService;
import com.lrouter.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * 注解处理器  路由生成类
 *
 * @Author: lqc
 */
@AutoService(Processor.class)
@SupportedOptions(Consts.KEY_MODULE_NAME)
public class RouterProcessor extends AbstractProcessor {
    private static final boolean DEBUG = true;
    private Messager messager;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Router.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        debug("process apt with " + annotations.toString());

        return handleRouter(roundEnv);
    }


    private boolean handleRouter(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Router.class);
        if (elements == null || elements.isEmpty()) {
            return false;
        }
        String index = processingEnv.getOptions().get(Consts.KEY_MODULE_NAME);
        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        for (Element element : elements) {
            Router router = element.getAnnotation(Router.class);
            if (router == null) {
                continue;
            }
            if (Utils.isEmpty(index)) {
                if(element instanceof Symbol.ClassSymbol){
                    Symbol.ClassSymbol cls = (Symbol.ClassSymbol) element;
                    index = Utils.hash(cls.className());
                }else {
                    index = Utils.hash(element.toString());
                }
            }

            for (String format : router.value()) {
                ClassName className;
                Name methodName = null;
                if (element.getKind() == ElementKind.CLASS) {
                    className = ClassName.get((TypeElement) element);
                } else if (element.getKind() == ElementKind.METHOD) {
                    className = ClassName.get((TypeElement) element.getEnclosingElement());
                    methodName = element.getSimpleName();
                } else {
                    throw new IllegalArgumentException("unknow type");
                }
                if (format.startsWith("/")) {
                    error("Router#value can not start with '/'. at [" + className + "]@Router(\"" + format + "\")");
                    return false;
                }
                if (format.endsWith("/")) {
                    error("Router#value can not end with '/'. at [" + className + "]@Router(\"" + format + "\")");
                    return false;
                }
                if (element.getKind() == ElementKind.CLASS) {
                    mapMethod.addStatement(Consts.PACKAGE_NAME + ".RouterCenter.map($S, $T.class)", format, className);
                } else {
                    mapMethod.addStatement(Consts.PACKAGE_NAME + ".RouterCenter.mapMethod($S, $T.class)", format, className);
                }
            }
            mapMethod.addCode("\n");
        }
        String moduleName = "RouterMapping";
        if (index != null) {
            moduleName = moduleName + "$" + index;
        }
        TypeElement typeIRouteGroup = processingEnv.getElementUtils().getTypeElement(Consts.IROUTER_MAPPER);
        TypeSpec routerMapping = TypeSpec.classBuilder(moduleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mapMethod.build())
                .addSuperinterface(ClassName.get(typeIRouteGroup))
                .build();
        try {
            JavaFile.builder(Consts.GENERATE_PACKAGE_NAME, routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }


    private void error(String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void debug(String msg) {
        if (DEBUG) {
            messager.printMessage(Diagnostic.Kind.NOTE, "RouterProcessor-->>" + msg);
        }
    }
}
