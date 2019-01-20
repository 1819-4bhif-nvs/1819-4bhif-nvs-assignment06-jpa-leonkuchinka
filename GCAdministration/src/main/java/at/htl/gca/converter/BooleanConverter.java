package at.htl.gca.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean b) {
        return b? "TRUE" : "FALSE";
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        return s.equals("TRUE");
    }
}
