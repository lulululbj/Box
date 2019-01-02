package luyao.parser.classes.bean;

import java.util.List;

/**
 * Created by luyao
 * on 2018/8/27 14:49
 */
public class Clazz {

    private String magic;
    private int minor_version;
    private int major_version;
    private int constant_pool_count;
    private List<Constant> constant_pool_list;
}
