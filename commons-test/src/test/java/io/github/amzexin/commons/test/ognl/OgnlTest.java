package io.github.amzexin.commons.test.ognl;

import io.github.amzexin.commons.test.bean.Address;
import io.github.amzexin.commons.test.bean.User;
import ognl.Ognl;
import ognl.OgnlException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * OgnlTest
 *
 * @author zexin
 */
public class OgnlTest {

    /**
     * 对 Root 对象的访问
     *
     * @throws OgnlException
     */
    @Test
    public void test1() throws OgnlException {
        User user = new User("test", null);
        Address address = new Address("330108", "杭州市滨江区");
        user.setAddress(address);
        System.out.println(Ognl.getValue("name", user));    // test
        System.out.println(Ognl.getValue("age != null", user));    // test
        System.out.println(Ognl.getValue("name.length", user));        // 4
        System.out.println(Ognl.getValue("address", user));        // Address(port=330108, address=杭州市滨江区)
        System.out.println(Ognl.getValue("address.port", user));    // 110003
    }

    /**
     * 对上下文对象的访问
     *
     * @return
     * @throws OgnlException
     */
    @Test
    public void test2() throws OgnlException {
        User user = new User("test", 23);
        Address address = new Address("330108", "杭州市滨江区");
        user.setAddress(address);
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("init", "hello");
        context.put("user", user);
        System.out.println(Ognl.getValue("#init", context, user));    // hello
        System.out.println(Ognl.getValue("#user.name", context, user));    // test
        System.out.println(Ognl.getValue("name", context, user));    // test
    }
}
