package priv.eric.starter.entity.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceExpose {
}
