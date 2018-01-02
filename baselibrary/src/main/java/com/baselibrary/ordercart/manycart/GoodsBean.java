package com.baselibrary.ordercart.manycart;

import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class GoodsBean {
    /**
     * storename : 陶瓷店铺
     * list : [{"id":15,"goods_id":2,"store_id":2,"goods_name":"孙大娘豆腐","goods_thumbnail":null,"goods_attr":"1000克","goods_price":2,"number":11,"goodsstatus":1,"storename":"陶瓷店铺","cursprice":0.01},{"id":12,"goods_id":2,"store_id":2,"goods_name":"孙大娘豆腐","goods_thumbnail":null,"goods_attr":"500克","goods_price":2,"number":11,"goodsstatus":1,"storename":"陶瓷店铺","cursprice":0.01}]
     */

    private String storename;


    private boolean isChoosed;
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    private List<ListBean> list;

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 383
         * goods_id : 91
         * store_id : 14
         * goods_name : 平菇
         * goods_thumbnail : http://static.shehuizhue.com/95e4372b5c4131c21045d1196bc858f3.jpg
         * goods_attr : 20时前
         * goods_price : 4.98
         * number : 1
         * goodsstatus : 1
         * storename : 鲜百惠生活配送
         * cursprice : 4.98
         */

        private int id;
        private int goods_id;
        private int store_id;
        private String goods_name;
        private String goods_thumbnail;
        private String goods_attr;
        private double goods_price;
        private int number;
        private int goodsstatus;
        private String storename;
        private double cursprice;

                private boolean is_choose;//是否选中

        public double getGoodsTotalPrice() {
            return number * cursprice;
        }

        public boolean getChecked() {
            return this.is_choose;
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

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_thumbnail() {
            return goods_thumbnail;
        }

        public void setGoods_thumbnail(String goods_thumbnail) {
            this.goods_thumbnail = goods_thumbnail;
        }

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }

        public double getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(double goods_price) {
            this.goods_price = goods_price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getGoodsstatus() {
            return goodsstatus;
        }

        public void setGoodsstatus(int goodsstatus) {
            this.goodsstatus = goodsstatus;
        }

        public String getStorename() {
            return storename;
        }

        public void setStorename(String storename) {
            this.storename = storename;
        }

        public double getCursprice() {
            return cursprice;
        }

        public void setCursprice(double cursprice) {
            this.cursprice = cursprice;
        }
    }
}
