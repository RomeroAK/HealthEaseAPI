package com.ayanda.HealthEaseApi.additional;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
@Documented
public @interface CurrentUser {
}
