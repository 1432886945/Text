package com.baselibrary.multilist;

import java.util.List;

/**
 * Created by Xiangb on 2017/12/4.
 * 功能：
 */

public class ExpandBean {

    private int index;
    private List<ExpandBean> childList;
    private boolean showChild = true;
    private boolean selected = false;
    private String id;
    private String itemText;
    private Object customData;

    public ExpandBean(Object customData, String itemText) {
        setCustomData(customData);
        setItemText(itemText);
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowChild() {
        return showChild;
    }

    public void setShowChild(boolean showChild) {
        this.showChild = showChild;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<ExpandBean> getChildList() {
        return childList;
    }

    public void setChildList(List<ExpandBean> childList) {
        this.childList = childList;
    }
}
