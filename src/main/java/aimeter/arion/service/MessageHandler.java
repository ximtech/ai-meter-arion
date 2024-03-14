package aimeter.arion.service;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.List;
import java.util.Locale;

public record MessageHandler(Locale locale, MessageSource messageSource) implements TemplateMethodModelEx {
    
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (CollectionUtils.isEmpty(arguments)) {
            throw new TemplateModelException("Wrong number of template arguments");
        }
        String code = ((SimpleScalar) arguments.get(0)).getAsString();
        if (StringUtils.isBlank(code)) {
            throw new TemplateModelException("Invalid code value '" + code + "'");
        }
        return messageSource.getMessage(code, null, locale);
    }
}
