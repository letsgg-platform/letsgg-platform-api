package net.letsgg.platform.config;

import net.letsgg.platform.security.CurrentUser
import org.springframework.core.MethodParameter
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest


@Transactional(readOnly = true)
class CurrentUserMethodArgumentResolver : HandlerMethodArgumentResolver {
    
    override fun supportsParameter(methodParam: MethodParameter): Boolean {
        return methodParam.hasParameterAnnotation(CurrentUser::class.java) && methodParam.parameterType == String::class.java
    }
    
    override fun resolveArgument(
        methodParam: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory?
    ): String {
        val request = nativeWebRequest.nativeRequest as HttpServletRequest
        return request.userPrincipal.name as String
    }
    
}