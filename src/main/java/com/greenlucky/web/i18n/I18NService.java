package com.greenlucky.web.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Mr Lam on 3/11/2016.
 */
@Service
public class I18NService {

    @Autowired
    private MessageSource messageSource;

    /**
     * Returns a message for the given message id and the default locale in the session context
     * @param messageId The key to the message resource file
     **/
    public String getMessage(String messageId) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(messageId,locale);
    }

    /**
     * Returns a message for the given the message id and locate
     * @param messageId The key  to the message resource file
     * @param locale The locate
     * @return
     */
    public String getMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId,null, locale);
    }
}
