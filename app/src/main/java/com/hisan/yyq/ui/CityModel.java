package com.hisan.yyq.ui;

/**
 * 创建时间 : 2017/12/7
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：无
 */
public class CityModel {


    /**
     * id : 1
     * pid : 0
     * name : 宁夏站
     * sort : 3
     */

    private int id;
    private int pid;
    private String name;
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
