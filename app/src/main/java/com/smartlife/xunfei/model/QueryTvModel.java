package com.smartlife.xunfei.model;

/**
 * Created by Administrator on 2017/6/27.
 */

public class QueryTvModel {

    /**
     * webPage : {"header":"为您找到下面的结果","url":"http://kcbj.openspeech.cn/service/iss?wuuid=f299d5d450962f2b86757b9d961dddf7&ver=2.0&method=webPage&uuid=3d1a44bc0e14c6f49145ba559c09d1cfquery"}
     * semantic : {"slots":{"tvName":"浙江卫视","startTime":{"date":"2017-06-28","type":"DT_BASIC","dateOrig":"明天"}}}
     * rc : 0
     * operation : QUERY_TV
     * service : tv
     * data : {}
     * text : 浙江卫视明天的节目
     */

    private WebPageBean webPage;
    private SemanticBean semantic;
    private int rc;
    private String operation;
    private String service;
    private DataBean data;
    private String text;

    public WebPageBean getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPageBean webPage) {
        this.webPage = webPage;
    }

    public SemanticBean getSemantic() {
        return semantic;
    }

    public void setSemantic(SemanticBean semantic) {
        this.semantic = semantic;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class WebPageBean {
        /**
         * header : 为您找到下面的结果
         * url : http://kcbj.openspeech.cn/service/iss?wuuid=f299d5d450962f2b86757b9d961dddf7&ver=2.0&method=webPage&uuid=3d1a44bc0e14c6f49145ba559c09d1cfquery
         */

        private String header;
        private String url;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SemanticBean {
        /**
         * slots : {"tvName":"浙江卫视","startTime":{"date":"2017-06-28","type":"DT_BASIC","dateOrig":"明天"}}
         */

        private SlotsBean slots;

        public SlotsBean getSlots() {
            return slots;
        }

        public void setSlots(SlotsBean slots) {
            this.slots = slots;
        }

        public static class SlotsBean {
            public static class StartTimeBean {
            }
        }
    }

    public static class DataBean {
    }
}
