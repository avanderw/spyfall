package net.avdw.spyfall.game;

import com.google.gson.Gson;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import net.avdw.property.AbstractPropertyModule;
import net.avdw.repository.Repository;
import net.avdw.spyfall.SpyfallProperty;
import net.avdw.spyfall.game.memory.MemorySpecification;
import net.avdw.spyfall.game.memory.SpyfallPlayer;
import net.avdw.spyfall.game.memory.SpyfallPlayerMemoryRepository;
import org.tinylog.Logger;

import java.lang.reflect.Method;
import java.util.Properties;

public class GameModule extends AbstractPropertyModule {
    @Override
    protected void configure() {
        Properties properties = configureProperties();
        Names.bindProperties(binder(), properties);

        String messagesFileName = String.format("messages_%s.properties", properties.getProperty(SpyfallProperty.LANGUAGE));
        Properties defaultMessages = new Properties();
        Properties localMessages = new Properties();

        bind(new TypeLiteral<Repository<SpyfallPlayer>>() {
        }).to(SpyfallPlayerMemoryRepository.class);
        Gson gson = new Gson();
        bindInterceptor(Matchers.subclassesOf(Action.class), new AbstractMatcher<Method>() {
            @Override
            public boolean matches(final Method method) {
                return !method.isSynthetic();
            }
        }, invocation -> {
            Logger.trace("->{}.{}({})", invocation.getThis().getClass().getSimpleName(), invocation.getMethod().getName(), gson.toJson(invocation.getArguments()));
            Object object = invocation.proceed();
            Logger.trace("<-{}", gson.toJson(object));
            return object;
        });
    }

    @Override
    protected String globalDirectory() {
        String globalDirectory = this.getClass().getPackage().getName();
        globalDirectory = globalDirectory.replace("net.avdw.", "");
        globalDirectory = globalDirectory.substring(0, globalDirectory.lastIndexOf("."));
        globalDirectory = globalDirectory.replace('.', '/');
        if (globalDirectory.length() == 0) {
            globalDirectory = ".";
        }
        return globalDirectory;
    }

    @Override
    protected String propertyFileName() {
        String propertyFileName = this.getClass().getPackage().getName();
        propertyFileName = propertyFileName.substring(propertyFileName.lastIndexOf(".") + 1);
        propertyFileName = String.format("%s.properties", propertyFileName);
        return propertyFileName;
    }

    @Override
    protected Properties defaultProperties() {
        Properties properties = new Properties();
        return properties;
    }
}
