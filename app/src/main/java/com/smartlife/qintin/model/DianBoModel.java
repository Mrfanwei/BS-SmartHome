package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class DianBoModel {

    /**
     * data : [{"id":521,"name":"小说","section_id":208,"sequence":1,"type":"category"},{"id":3629,"name":"畅销小说","section_id":1115,"sequence":2,"type":"category"},{"id":3617,"name":"精品内容","section_id":1011,"sequence":3,"type":"category"},{"id":523,"name":"音乐","section_id":139,"sequence":4,"type":"category"},{"id":545,"name":"头条","section_id":199,"sequence":6,"type":"category"},{"id":527,"name":"相声小品","section_id":209,"sequence":7,"type":"category"},{"id":3251,"name":"脱口秀","section_id":200,"sequence":8,"type":"category"},{"id":529,"name":"情感","section_id":82,"sequence":9,"type":"category"},{"id":539,"name":"健康","section_id":107,"sequence":10,"type":"category"},{"id":531,"name":"历史","section_id":213,"sequence":12,"type":"category"},{"id":1599,"name":"儿童","section_id":214,"sequence":13,"type":"category"},{"id":547,"name":"娱乐","section_id":210,"sequence":14,"type":"category"},{"id":3330,"name":"女性","section_id":204,"sequence":15,"type":"category"},{"id":3252,"name":"搞笑","section_id":201,"sequence":16,"type":"category"},{"id":537,"name":"教育","section_id":116,"sequence":17,"type":"category"},{"id":1585,"name":"公开课","section_id":74,"sequence":19,"type":"category"},{"id":3613,"name":"文化","section_id":893,"sequence":20,"type":"category"},{"id":3496,"name":"评书","section_id":515,"sequence":21,"type":"category"},{"id":3276,"name":"戏曲","section_id":217,"sequence":22,"type":"category"},{"id":533,"name":"财经","section_id":215,"sequence":23,"type":"category"},{"id":535,"name":"科技","section_id":216,"sequence":24,"type":"category"},{"id":3385,"name":"汽车","section_id":207,"sequence":25,"type":"category"},{"id":3238,"name":"体育","section_id":203,"sequence":26,"type":"category"},{"id":1737,"name":"校园","section_id":166,"sequence":27,"type":"category"},{"id":3427,"name":"游戏动漫","section_id":205,"sequence":28,"type":"category"},{"id":3442,"name":"广播剧","section_id":206,"sequence":29,"type":"category"},{"id":3588,"name":"影视","section_id":569,"sequence":31,"type":"category"},{"id":3597,"name":"旅游","section_id":674,"sequence":35,"type":"category"},{"id":3599,"name":"自媒体","section_id":732,"sequence":37,"type":"category"},{"id":3600,"name":"品牌电台","section_id":751,"sequence":38,"type":"category"},{"id":3605,"name":"时尚","section_id":818,"sequence":40,"type":"category"},{"id":3608,"name":"中国之声","section_id":837,"sequence":42,"type":"category"},{"id":3631,"name":"会员专区","section_id":1142,"sequence":53,"type":"category"}]
     * errormsg :
     * errorno : 0
     */

    private String errormsg;
    private int errorno;
    private List<DataBean> data;

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public int getErrorno() {
        return errorno;
    }

    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 521
         * name : 小说
         * section_id : 208
         * sequence : 1
         * type : category
         */

        private int id;
        private String name;
        private int section_id;
        private int sequence;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSection_id() {
            return section_id;
        }

        public void setSection_id(int section_id) {
            this.section_id = section_id;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
