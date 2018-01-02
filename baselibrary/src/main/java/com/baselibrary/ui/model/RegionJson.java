package com.baselibrary.ui.model;

import java.util.List;

/**
 * 创建时间 : 2017/12/22
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：3级联动城市模板
 */
public class RegionJson extends BaseModel{

    /**
     * 省
     */
    public int id;
    public int pid;
    public String name;


    public List<ChildEntity> children;

    /**
     * 市
     */
    public static class ChildEntity {
        public int id;
        public int pid;
        public String name;

        public List<ChildEntity2> children;

        /**
         * 区
         */
        public static class ChildEntity2 {
            public int id;
            public int pid;
            public String name;
        }
    }
}
