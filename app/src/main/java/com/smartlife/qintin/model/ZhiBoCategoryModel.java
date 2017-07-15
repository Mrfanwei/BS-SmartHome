package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class ZhiBoCategoryModel {

    /**
     * data : [{"id":20,"name":"地区","values":[{"id":1209,"name":"北京","sequence":14},{"id":1208,"name":"上海","sequence":15},{"id":1216,"name":"天津","sequence":16},{"id":1219,"name":"重庆","sequence":17},{"id":1215,"name":"广东","sequence":18},{"id":1212,"name":"浙江","sequence":19},{"id":1210,"name":"江苏","sequence":20},{"id":1213,"name":"湖南","sequence":21},{"id":1211,"name":"四川","sequence":22},{"id":1217,"name":"山西","sequence":24},{"id":1218,"name":"河南","sequence":25},{"id":1220,"name":"湖北","sequence":27},{"id":1221,"name":"黑龙江","sequence":28},{"id":1214,"name":"辽宁","sequence":29},{"id":1222,"name":"河北","sequence":30},{"id":1223,"name":"山东","sequence":31},{"id":1224,"name":"安徽","sequence":32},{"id":1226,"name":"福建","sequence":33},{"id":1227,"name":"广西","sequence":34},{"id":1228,"name":"贵州","sequence":35},{"id":1237,"name":"云南","sequence":36},{"id":1229,"name":"江西","sequence":37},{"id":1230,"name":"吉林","sequence":38},{"id":1241,"name":"陕西","sequence":39},{"id":1225,"name":"甘肃","sequence":39},{"id":1231,"name":"宁夏","sequence":40},{"id":1235,"name":"内蒙古","sequence":41},{"id":1234,"name":"海南","sequence":42},{"id":1233,"name":"西藏","sequence":43},{"id":1232,"name":"青海","sequence":44},{"id":1236,"name":"新疆","sequence":45}]},{"id":91,"name":"类型","values":[{"id":1335,"name":"新闻","sequence":1},{"id":1336,"name":"音乐","sequence":3},{"id":1337,"name":"经济","sequence":5},{"id":1338,"name":"交通","sequence":7},{"id":1520,"name":"校园","sequence":8},{"id":1339,"name":"都市","sequence":9},{"id":1340,"name":"曲艺","sequence":11},{"id":1341,"name":"体育","sequence":13},{"id":1342,"name":"综合","sequence":15},{"id":1343,"name":"生活","sequence":17},{"id":1344,"name":"文艺","sequence":19},{"id":1345,"name":"旅游","sequence":21},{"id":1347,"name":"方言","sequence":23},{"id":1774,"name":"外语","sequence":24}]}]
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
         * id : 20
         * name : 地区
         * values : [{"id":1209,"name":"北京","sequence":14},{"id":1208,"name":"上海","sequence":15},{"id":1216,"name":"天津","sequence":16},{"id":1219,"name":"重庆","sequence":17},{"id":1215,"name":"广东","sequence":18},{"id":1212,"name":"浙江","sequence":19},{"id":1210,"name":"江苏","sequence":20},{"id":1213,"name":"湖南","sequence":21},{"id":1211,"name":"四川","sequence":22},{"id":1217,"name":"山西","sequence":24},{"id":1218,"name":"河南","sequence":25},{"id":1220,"name":"湖北","sequence":27},{"id":1221,"name":"黑龙江","sequence":28},{"id":1214,"name":"辽宁","sequence":29},{"id":1222,"name":"河北","sequence":30},{"id":1223,"name":"山东","sequence":31},{"id":1224,"name":"安徽","sequence":32},{"id":1226,"name":"福建","sequence":33},{"id":1227,"name":"广西","sequence":34},{"id":1228,"name":"贵州","sequence":35},{"id":1237,"name":"云南","sequence":36},{"id":1229,"name":"江西","sequence":37},{"id":1230,"name":"吉林","sequence":38},{"id":1241,"name":"陕西","sequence":39},{"id":1225,"name":"甘肃","sequence":39},{"id":1231,"name":"宁夏","sequence":40},{"id":1235,"name":"内蒙古","sequence":41},{"id":1234,"name":"海南","sequence":42},{"id":1233,"name":"西藏","sequence":43},{"id":1232,"name":"青海","sequence":44},{"id":1236,"name":"新疆","sequence":45}]
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
             * id : 1209
             * name : 北京
             * sequence : 14
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
