package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class SearchHotWordModel {

    /**
     * errorno : 0
     * errormsg :
     * data : [{"name":"浜烘皯鐨勫悕涔","count":96871},{"name":"鏅撹","count":91344},{"name":"鍏ㄨ亴楂樻墜","count":74892},{"name":"浠婃櫄80鍚庤劚鍙","count":68098},{"name":"鍛ㄥ缓榫","count":40345},{"name":"钂嬪媼","count":38780},{"name":"閮痉绾","count":35872},{"name":"鐧介箍鍘","count":29843},{"name":"鑰侀┈楗眬","count":27024},{"name":"闀胯皥","count":24201}]
     * expiredtime : 1501060756
     * title : 鐑棬鎼滅储
     */

    private int errorno;
    private String errormsg;
    private int expiredtime;
    private String title;
    private List<DataBean> data;

    public int getErrorno() {
        return errorno;
    }

    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public int getExpiredtime() {
        return expiredtime;
    }

    public void setExpiredtime(int expiredtime) {
        this.expiredtime = expiredtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 浜烘皯鐨勫悕涔
         * count : 96871
         */

        private String name;
        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
