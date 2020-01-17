package com.mvc.view.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(UserTags.class)
public @interface UserTag {

    String id();
}
