package net.avdw;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import net.avdw.color.AnsiColor;
import net.avdw.color.ColorConverter;
import org.tinylog.Logger;

import java.lang.reflect.Method;

public final class ProfilingModule extends AbstractModule {
    private final Matcher<? super Class<?>> classMatcher;
    private final Matcher<? super Method> methodMatcher;

    public ProfilingModule(final Matcher<? super Class<?>> classMatcher, final Matcher<? super Method> methodMatcher) {
        this.classMatcher = classMatcher;
        this.methodMatcher = methodMatcher;
    }

    @Override
    protected void configure() {
        ColorConverter colorConverter = new ColorConverter();
        bindInterceptor(classMatcher, methodMatcher, (methodInvocation) -> {
            long start = System.currentTimeMillis();
            try {
                Logger.trace(String.format("%s%s.%s -> %s",
                        colorConverter.hexToAnsiFg(0x666666, false),
                        methodInvocation.getMethod().getDeclaringClass().getSimpleName(),
                        methodInvocation.getMethod().getName(),
                        AnsiColor.RESET
                ));
                return methodInvocation.proceed();
            } finally {
                Logger.trace(String.format("%s%s.%s <- %,d ms%s",
                        colorConverter.hexToAnsiFg(0x666666, false),
                        methodInvocation.getMethod().getDeclaringClass().getSimpleName(),
                        methodInvocation.getMethod().getName(),
                        System.currentTimeMillis() - start,
                        AnsiColor.RESET
                ));
            }
        });
    }
}
