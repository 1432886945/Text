package com.baselibrary.ordercart.singlecart;

import com.baselibrary.ui.model.BaseModel;

/**
 * 单商品模板
 */
public class CartBean extends BaseModel{
    /**
     * id : 3
     * goods_id : 3
     * name : 象鼻子 XONE3 防雾霾智能口罩
     * parameter : XL
     * thumbnail : http://freeride.oss-cn-qingdao.aliyuncs.com/0b5f42e84ef6cd2fb5bee2ed5a448da5.jpg
     * sprice : 699
     * number : 3
     * issell : false
     */

    private int id;
    private int goods_id;
    private String name;
    private String parameter;
    private String thumbnail;
    private double sprice;
    private int number;
    //产品失败判断
    private boolean issell;
    private boolean is_choose;//是否选中

    public double getGoodsTotalPrice() {
        return number * sprice;
    }

    public boolean getChecked() {
        return is_choose;
    }


    public void setIs_choose(boolean is_choose) {
        this.is_choose = is_choose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getSprice() {
        return sprice;
    }

    public void setSprice(double sprice) {
        this.sprice = sprice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean issell() {
        return issell;
    }

    public void setIssell(boolean issell) {
        this.issell = issell;
    }

    public boolean is_choose() {
        return is_choose;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", goods_id=" + goods_id +
                ", name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", sprice=" + sprice +
                ", number=" + number +
                ", issell=" + issell +
                ", is_choose=" + is_choose +
                '}';
    }
}
