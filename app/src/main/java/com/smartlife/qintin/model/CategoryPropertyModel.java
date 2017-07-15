package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CategoryPropertyModel {

    /**
     * data : [{"id":32,"name":"分类","values":[{"id":2743,"name":"青春校园","sequence":1},{"id":2742,"name":"穿越架空","sequence":1},{"id":3182,"name":"推理刑侦","sequence":1},{"id":2762,"name":"广播剧","sequence":1},{"id":2745,"name":"社科经管","sequence":1},{"id":2744,"name":"武侠仙侠","sequence":1},{"id":2761,"name":"评书名家","sequence":1},{"id":517,"name":"悬疑探险","sequence":2},{"id":511,"name":"惊悚灵异","sequence":3},{"id":508,"name":"玄幻超能","sequence":4},{"id":518,"name":"古风言情","sequence":6},{"id":509,"name":"现代言情","sequence":8},{"id":510,"name":"都市现代","sequence":10},{"id":520,"name":"官商职场","sequence":11},{"id":516,"name":"历史军事","sequence":12},{"id":513,"name":"文学小说","sequence":13}]},{"id":171,"name":"标签","values":[{"id":2045,"name":"总裁","sequence":1},{"id":2741,"name":"盗墓","sequence":1},{"id":2127,"name":"影视原著","sequence":1},{"id":2386,"name":"热门排行","sequence":1},{"id":3291,"name":"主妇","sequence":1}]},{"id":187,"name":"属性","values":[{"id":2079,"name":"蜻蜓FM出品","sequence":1},{"id":2126,"name":"原创","sequence":1},{"id":2174,"name":"火爆完本","sequence":1},{"id":2125,"name":"出版","sequence":1},{"id":3258,"name":"限时免费","sequence":1},{"id":2135,"name":"合集","sequence":1}]},{"id":265,"name":"性别","values":[{"id":3289,"name":"男频","sequence":1},{"id":3290,"name":"女频","sequence":1}]}]
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
         * id : 32
         * name : 分类
         * values : [{"id":2743,"name":"青春校园","sequence":1},{"id":2742,"name":"穿越架空","sequence":1},{"id":3182,"name":"推理刑侦","sequence":1},{"id":2762,"name":"广播剧","sequence":1},{"id":2745,"name":"社科经管","sequence":1},{"id":2744,"name":"武侠仙侠","sequence":1},{"id":2761,"name":"评书名家","sequence":1},{"id":517,"name":"悬疑探险","sequence":2},{"id":511,"name":"惊悚灵异","sequence":3},{"id":508,"name":"玄幻超能","sequence":4},{"id":518,"name":"古风言情","sequence":6},{"id":509,"name":"现代言情","sequence":8},{"id":510,"name":"都市现代","sequence":10},{"id":520,"name":"官商职场","sequence":11},{"id":516,"name":"历史军事","sequence":12},{"id":513,"name":"文学小说","sequence":13}]
         */

        private int id;
        private String name;
        private List<ValuesBean> values;

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

        public List<ValuesBean> getValues() {
            return values;
        }

        public void setValues(List<ValuesBean> values) {
            this.values = values;
        }

        public static class ValuesBean {
            /**
             * id : 2743
             * name : 青春校园
             * sequence : 1
             */

            private int id;
            private String name;
            private int sequence;

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

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }
        }
    }
}
