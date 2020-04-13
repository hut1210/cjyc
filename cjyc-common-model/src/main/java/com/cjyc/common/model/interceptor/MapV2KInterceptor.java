package com.cjyc.common.model.interceptor;

import com.cjyc.common.model.annotations.MapV2K;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.dao.DuplicateKeyException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * MapV2K的拦截器
 */
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}))
public class MapV2KInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MetaObject metaStatementHandler = ReflectUtil.getRealTarget(invocation);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("mappedStatement");

        // 当前类名
        String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");
        // 当前方法名
        String currentMethodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");
        // 获取当前方法
        Method currentMethod = findMethod(className, currentMethodName);
        /** 如果当前Method没有注解MapV2K */
        if (currentMethod == null || currentMethod.getAnnotation(MapV2K.class) == null) {
            return invocation.proceed();
        }

        /** 如果有MapV2K注解，则这里对结果进行拦截并转换 **/
        MapV2K mapV2KAnnotation = currentMethod.getAnnotation(MapV2K.class);
        Statement statement = (Statement) invocation.getArgs()[0];
        // 获取返回Map里key-value的类型
        Pair<Class<?>, Class<?>> kvTypePair = getKVTypeOfReturnMap(currentMethod);
        // 获取各种TypeHander的注册器
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        return result2Map(statement, typeHandlerRegistry, kvTypePair, mapV2KAnnotation);

    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 找到与指定函数名匹配的Method。
     *
     * @param className
     * @param targetMethodName
     * @throws Throwable
     */
    private Method findMethod(String className, String targetMethodName) throws Throwable {
        // 该类所有声明的方法
        Method[] methods = Class.forName(className).getDeclaredMethods();
        if (methods.length <= 0) {
            return null;
        }

        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), targetMethodName)) {
                return method;
            }
        }

        return null;
    }

    /**
     * 获取函数返回Map中key-value的类型
     *
     * @param mapV2KMethod
     * @return left为key的类型，right为value的类型
     */
    private Pair<Class<?>, Class<?>> getKVTypeOfReturnMap(Method mapV2KMethod) {
        Type returnType = mapV2KMethod.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (!Map.class.equals(parameterizedType.getRawType())) {
                throw new RuntimeException(
                        "[ERROR-MapV2K-return-map-type]使用MapV2K,返回类型必须是java.util.Map类型！！！method=" + mapV2KMethod);
            }

            return new Pair<>((Class<?>) parameterizedType.getActualTypeArguments()[0],
                    (Class<?>) parameterizedType.getActualTypeArguments()[1]);
        }

        return new Pair<>(null, null);
    }

    /**
     * 将查询结果映射成Map，其中第一个字段作为key，第二个字段作为value.
     *
     * @param statement
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param kvTypePair          函数指定返回Map key-value的类型
     * @param mapV2KAnnotation
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement, TypeHandlerRegistry typeHandlerRegistry,
                              Pair<Class<?>, Class<?>> kvTypePair, MapV2K mapV2KAnnotation) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        List<Object> res = new ArrayList<>();
        Map<Object, Object> map = new HashMap<>();

        while (resultSet.next()) {
            Object key = this.getObject(resultSet, 1, typeHandlerRegistry, kvTypePair.getKey());
            Object value = this.getObject(resultSet, 2, typeHandlerRegistry, kvTypePair.getValue());
            // 该key已存在
            if (map.containsKey(key)) {
                // 判断是否允许key重复
                if (!mapV2KAnnotation.isAllowKeyRepeat()) {
                    throw new DuplicateKeyException("MapV2K duplicated key! key=" + key);
                }

                Object preValue = map.get(key);
                // 判断是否允许value不同
                if (!mapV2KAnnotation.isAllowValueDifferentWithSameKey() && !Objects.equals(value, preValue)) {
                    throw new DuplicateKeyException("MapV2K different value with same key!key=" + key + ",value1="
                            + preValue + ",value2=" + value);
                }
            }
            // 第一列作为key,第二列作为value
            map.put(key, value);
        }

        res.add(map);
        return res;
    }

    /**
     * 结果类型转换。
     * <p>
     * 这里借用注册在MyBatis的typeHander（包括自定义的），方便进行类型转换。
     *
     * @param resultSet
     * @param columnIndex         字段下标，从1开始
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param javaType            要转换的Java类型
     * @return
     * @throws SQLException
     */
    private Object getObject(ResultSet resultSet, int columnIndex, TypeHandlerRegistry typeHandlerRegistry,
                             Class<?> javaType) throws SQLException {
        final TypeHandler<?> typeHandler = typeHandlerRegistry.hasTypeHandler(javaType)
                ? typeHandlerRegistry.getTypeHandler(javaType) : typeHandlerRegistry.getUnknownTypeHandler();

        return typeHandler.getResult(resultSet, columnIndex);

    }

}