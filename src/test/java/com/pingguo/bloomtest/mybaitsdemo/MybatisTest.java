//package com.pingguo.bloomtest.mybaitsdemo;
//
//import com.pingguo.bloomtest.mapper.UserDemoMapperPlus;
//import com.pingguo.bloomtest.entity.User;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MybatisTest {
//
//    public SqlSessionFactory getSqlSessionFactory() throws IOException {
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        return new SqlSessionFactoryBuilder().build(inputStream);
//    }
//
//    @Test
//    void test() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//        System.out.println(userMapper.getUserById(3));
//    }
//
//    @Test
//    void test2params() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//        System.out.println(userMapper.getUserByIdAndUsername(3, "大周"));
//    }
//
//    @Test
//    void testLike() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//        System.out.println(userMapper.getUserByUsernameLike("%新%"));
//    }
//
//    @Test
//    void testByMap() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", 3);
//        map.put("username", "大周");
//        System.out.println(userMapper.getUserByMap(map));
//    }
//
//    @Test
//    void testReturnMap() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//
////        System.out.println(userMapper.getUserByIdReturnMap(3));
//
//        System.out.println(userMapper.getUserByUsernameReturnMap("%新%"));
//    }
//
//    @Test
//    void test2() throws IOException {
//        // 根据配置文件，创建一个 SqlSessionFactory 对象
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        // 获取 SqlSession 实例，可以执行已经映射的 sql 语句
//        SqlSession session = sqlSessionFactory.openSession();
//        // 获取接口的实现类对象
//        UserMapper userMapper = session.getMapper(UserMapper.class);
//
//        User user = userMapper.getUserById(4);
//        System.out.println(user);
//        System.out.println(userMapper.getClass());
//        session.close();
//    }
//
//    @Test
//    void test3() throws IOException{
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        // 获取到的 SqlSession 不会自动提交数据
//        SqlSession session = sqlSessionFactory.openSession();
//        User newUser = new User();
//        newUser.setUsername("新用户2");
//        newUser.setPassword("22222");
//        newUser.setCreateTime(new Date());
//        newUser.setUpdateTime(new Date());
//        try {
//            UserMapper userMapper = session.getMapper(UserMapper.class);
//            boolean result = userMapper.addUser(newUser);
//            System.out.println(result);
//            System.out.println(newUser.getId());
//            // 需要这里手动提交
//            session.commit();
//        } finally {
//            session.close();
//        }
//    }
//
//    @Test
//    void test5() throws IOException {
//        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
//        SqlSession session = sqlSessionFactory.openSession();
//        UserDemoMapperPlus userDemoMapperPlus = session.getMapper(UserDemoMapperPlus.class);
//        System.out.println(userDemoMapperPlus.getUserById(3));
//    }
//}
