package io.github.amzexin.commons.test.all.other.abstractclass;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description: AbstractClassTest
 *
 * @author Lizexin
 * @date 2021-09-24 20:29
 */
@Slf4j
public class AbstractClassTest {

    public String toJSONString(AbstractClazz baseClazz) {
        String jsonString = JSON.toJSONString(baseClazz);
        log.info("baseClazz = {}", jsonString);
        return jsonString;
    }

    public AbstractClazz parseObject(String jsonString) {
        AbstractClazz baseClazz = JSON.parseObject(jsonString, ChildClazz.class);
        log.info("baseClazz = {}", JSON.toJSONString(baseClazz));
        return baseClazz;
    }

    @Test
    public void test() {
        ChildClazz childClazz = new ChildClazz();
        childClazz.setName("Vincent");

        String jsonString = toJSONString(childClazz);
        parseObject(jsonString);
    }
}
