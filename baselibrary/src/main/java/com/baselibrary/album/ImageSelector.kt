package com.hisan.yyq.album

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.baselibrary.album.SelectImageActivity
import java.util.*

/**
 * 图片选择器
 */
class ImageSelector private constructor() {
    //选择图片张数
    private var mMaxCount = 9
    //选择图片的模式
    private var mMode = SelectImageActivity.MODE_MULTI
    //是否显示拍照的相机
    private var mShowCamera = true
    //原始的图片
    private var mOriginData: ArrayList<String>? = null

    /**
     * 单选模式
     *
     * @return
     */
    fun single(): ImageSelector {
        mMode = SelectImageActivity.MODE_SINGLE
        return this
    }

    /**
     * 多选模式
     *
     * @return
     */
    fun multi(): ImageSelector {
        mMode = SelectImageActivity.MODE_MULTI
        return this
    }

    /**
     * 设置可以选择的图片张数
     *
     * @param count
     * @return
     */
    fun count(count: Int): ImageSelector {
        mMaxCount = count
        return this
    }

    /**
     * 是否显示相机
     *
     * @param showCamera
     * @return
     */
    fun showCamera(showCamera: Boolean): ImageSelector {
        mShowCamera = showCamera
        return this
    }

    /**
     * 选择好的图片
     *
     * @param originList
     * @return
     */
    fun origin(originList: ArrayList<String>): ImageSelector {
        this.mOriginData = originList
        return this

    }

    /**
     * 启动requestCode
     *
     * @param activity
     * @param requestCode
     */
    fun start(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, SelectImageActivity::class.java)
        addParmasByIntent(intent)
        activity.startActivityForResult(intent, requestCode)
    }


    /**
     * 启动requestCode
     *
     * @param fragment
     * @param requestCode
     */
    fun start(fragment: Fragment, requestCode: Int) {
        val intent = Intent(fragment.context, SelectImageActivity::class.java)
        addParmasByIntent(intent)
        fragment.startActivityForResult(intent, requestCode)
    }

    /**
     * 设置参数
     *
     * @param intent
     */
    private fun addParmasByIntent(intent: Intent) {
        val bundle = Bundle()
        bundle.putInt(SelectImageActivity.EXTRA_SELECT_COUNT, mMaxCount)
        bundle.putInt(SelectImageActivity.EXTRA_SELECT_MODE, mMode)
        bundle.putBoolean(SelectImageActivity.EXTRA_SHOW_CAMERA, mShowCamera)
        if (mOriginData != null && mMode == SelectImageActivity.MODE_MULTI) {
            bundle.putSerializable(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData)
        }
        intent.putExtras(bundle)
    }
    companion object {

        fun create(): ImageSelector {
            return ImageSelector()
        }
    }
}
