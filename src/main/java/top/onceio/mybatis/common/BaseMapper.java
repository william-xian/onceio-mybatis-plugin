package top.onceio.mybatis.common;

import java.util.List;


public interface BaseMapper <E,T,ID>{


    /**
     */
    int countByExample(T example);

    /**
     */
    int deleteByExample(T example);

    /**
     */
    int deleteByPrimaryKey(ID id);

    /**
     */
    int insert(E record);

    /**
     */
    int insertSelective(E record);

    /**
     */
    List<E> selectByExample(T example);

    /**
     */
    E selectByPrimaryKey(ID id);

    /**
     */
    int updateByExampleSelective(E record, T example);

    /**
     */
    int updateByExample(E record, T example);

    /**
     */
    int updateByPrimaryKeySelective(E record);

    /**
     */
    int updateByPrimaryKey(E record);
}
