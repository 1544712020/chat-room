package edu.hncst.lchat.home.utils.basedao.impl;

import edu.hncst.lchat.home.utils.basedao.BaseDao;
import edu.hncst.lchat.home.utils.basedao.ColumnName;
import edu.hncst.lchat.home.utils.JDBCUtils;
import edu.hncst.lchat.home.utils.basedao.TableName;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class BaseDaoImpl<T> implements BaseDao {

    protected JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    protected QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
    protected Class<T> clazz;
    protected String tabName;

    public BaseDaoImpl() {
        //获取泛型class
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = type.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[0];
            //获取表名
            TableName tableName = clazz.getAnnotation(TableName.class);
            if (tableName == null) {
                throw new RuntimeException("请添加TableName注解，否则无法获取到表名");
            }
            this.tabName = tableName.value();
        }
    }

    /**
     * 查询所有数据
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> queryAll() {
        String sql = "select * from " + tabName;
        return (List<T>) template.query(sql, new BeanPropertyRowMapper<>(this.clazz));
    }

    @Override
    public <T> List<T> queryAll(int startCount, int pageSize) {
        String sql = "select * from " + tabName + "limit ?,?";
        return (List<T>) template.query(sql, new BeanPropertyRowMapper<>(this.clazz),startCount,pageSize);
    }

    /**
     * 根据id查询数据
     *
     * @param id
     * @param <T>
     * @return
     */
    @Override
    public <T> T queryById(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from ")
                .append(tabName)
                .append(" where id =")
                .append(id);
        return (T) template.queryForObject(sql.toString(), new MyRowMapper());
    }

    /**
     * 添加数据
     *
     * @return
     */
    public <T> int addObject(T t) {
        StringBuffer sqlBuffer = new StringBuffer();
        Field[] fields = clazz.getDeclaredFields();
        sqlBuffer.append("insert into ")
                .append(tabName)
                .append(" values(");
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object o = field.get(t);
                ColumnName annotation = field.getAnnotation(ColumnName.class);
                if(annotation == null){
                    throw new RuntimeException("请在属性上添加@ColumnName注解");
                }
                String name = annotation.value();
                if (name.equals("id")) {
                    if (o == null){
                        sqlBuffer.append("NULL");
                    }else {
                        sqlBuffer.append(o);
                    }
                    sqlBuffer.append(",");
                    continue;
                }
                sqlBuffer.append("\"")
                        .append(o)
                        .append("\"");
                sqlBuffer.append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sqlBuffer.replace(sqlBuffer.length() - 1, sqlBuffer.length(), ")");
        return template.update(sqlBuffer.toString());
    }

    /**
     * 更新数据
     * UPDATE Person SET Address = 'Zhongshan 23', City = 'Nanjing'
     * WHERE LastName = 'Wilson'
     *
     * @return
     */
    public <T> int updateObject(T t, Integer whereId) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ")
                .append(tabName)
                .append(" set ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                ColumnName annotation = field.getAnnotation(ColumnName.class);
                if(annotation == null){
                    throw new RuntimeException("请在属性上添加@ColumnName注解");
                }
                String name = annotation.value();
                Object o = field.get(t);
                //是否需要去掉
                if (o == null){
                    continue;
                }
                sql.append(name)
                        .append(" = ")
                        .append("\"")
                        .append(o)
                        .append("\"")
                        .append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sql.replace(sql.length() - 1, sql.length(), " ");
        if (whereId != null) {
            sql.append("where ")
                    .append("id =")
                    .append(whereId);
        }
        System.out.println(sql.toString());
        return template.update(sql.toString());
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ")
                .append(tabName);
        if (id != null) {
            sql.append(" where id =")
                    .append(id);
        }
        return template.update(sql.toString());
    }

    /**
     * 批量删除
     * DELETE FROM TestTable WHERE ID IN (1, 3, 54, 68)
     *
     * @param ids
     * @return
     */
    @Override
    public int[] deleteByIds(String... ids) {
        StringBuffer sql = new StringBuffer();
        String[] sqls = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            sql.append("delete from ")
                    .append(tabName)
                    .append(" where ")
                    .append("id = ")
                    .append(ids[i]);
            sqls[i] = sql.toString();
            sql.delete(0, sql.length());
        }
        return template.batchUpdate(sqls);
    }

    /**
     * 删除整张表
     *
     * @return
     */
    @Override
    public int delete() {
        return deleteById(null);
    }

    /**
     * 返回查询数据
     */
    class MyRowMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Field[] fields = clazz.getDeclaredFields();
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (Field field : fields) {
                String name = field.getName();
                Object rul = resultSet.getObject(name);
                try {
                    field.setAccessible(true);
                    field.set(t, rul);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return t;
        }
    }
}
