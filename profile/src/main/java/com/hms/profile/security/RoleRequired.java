package com.hms.profile.security;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoleRequired {
    String value(); // Required role
}
