package cn.lingjiatong.re.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;

import java.util.List;
import java.util.function.BiFunction;

/**
 * mybatis批量处理工具类
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 17:49
 */
@Slf4j
public class MybatisBatchUtils {

    /**
     * 每次处理1000条
     */
    private static final int BATCH_SIZE = 1000;

    /**
     * 批量处理修改或者插入
     *
     * 注意：必须通过 SqlSessionUtils 获取 Spring 管理的会话。
     * 1. 旧实现使用 sqlSessionFactory.openSession(BATCH) 自行开启会话，
     *    在 @Transactional 方法中不会随外层事务提交（commit被跳过），
     *    语句虽执行但连接关闭时被回滚，导致数据静默丢失；
     * 2. 也不能指定 ExecutorType.BATCH，同一事务中已有 SIMPLE 执行器会话时
     *    Spring 会抛出 "Cannot change the ExecutorType" 异常。
     * 这里使用默认执行器获取（有事务则复用事务会话，无事务则由 close 自动提交），
     * 标签等小规模批量场景下循环插入完全够用。
     *
     * @param data        需要被处理的数据
     * @param mapperClass Mybatis的Mapper类
     * @param function    自定义处理逻辑
     * @return int 处理的总条数
     */
    public static <T, U, R> int batchUpdateOrInsert(SqlSessionFactory sqlSessionFactory, List<T> data, Class<U> mapperClass, BiFunction<T, U, R> function) {
        int i = 1;
        // 通过 SqlSessionUtils 获取/注册 Spring 管理的会话：
        // 处于 Spring 事务中时复用事务连接，提交/回滚由外层事务统一控制；
        // 无事务时由 SqlSessionUtils 在 close 时自动提交
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        try {
            U mapper = sqlSession.getMapper(mapperClass);
            int size = data.size();
            for (T element : data) {
                function.apply(element, mapper);
                if ((i % BATCH_SIZE == 0) || i == size) {
                    sqlSession.flushStatements();
                }
                i++;
            }
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
        }
        return i - 1;
    }

}
