package edu.hncst.lchat.home.utils.basedao;

import java.util.List;

public interface BaseDao {
    /**
     * 查询所有数据
     * @param <T> 查询的数据类型
     * @return
     */
    <T> List<T> queryAll();

    /**
     * 分页查询
     * @param startCount
     * @param pageSize
     * @param <T>
     * @return
     */
    <T> List<T> queryAll(int startCount, int pageSize);

    /**
     * 根据id查询数据
     * @param id
     * @param <T>
     * @return
     */
    <T> T queryById(Integer id);

    /**
     * 添加数据
     * @param t
     * @param <T>
     * @return
     */
    <T> int addObject(T t);

    /**
     * 根据id更新数据
     * @param t
     * @param id
     * @param <T>
     * @return
     */
    <T> int updateObject(T t, Integer id);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    int[] deleteByIds(String... id);

    /**
     * 删除整张表
     * @return
     */
    int delete();
}
