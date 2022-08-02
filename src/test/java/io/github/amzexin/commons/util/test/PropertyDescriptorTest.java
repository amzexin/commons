package io.github.amzexin.commons.util.test;

import io.github.amzexin.commons.util.http.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Description: PropertyDescriptorTest
 *
 * @author Lizexin
 * @date 2022-07-18 14:38
 */
@Slf4j
public class PropertyDescriptorTest {

    @Test
    public void run() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Object obj = new HttpResult();
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("code", obj.getClass());
        Object propertyValue = propertyDescriptor.getReadMethod().invoke(obj);
        System.out.println(propertyValue);
    }

}
