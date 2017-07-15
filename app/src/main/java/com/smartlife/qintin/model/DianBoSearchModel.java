package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class DianBoSearchModel {

    /**
     * code : 0
     * data : [{"doclist":{"docs":[{"category_id":547,"category_name":"娱乐","cover":"http://pic.qingting.fm/2015/0925/2015092519571864.jpg!200","duration":3296.3,"file_path":"m4a/57ad51537cb8911fa0cf409c_5671001_64.m4a?u=678&channelId=125114&programId=5063432","id":5063432,"idx_id":"radio_program_5063432","mediainfo_id":5671001,"parent_id":125114,"parent_name":"倾听文化汇","parent_type":"channel","playcount":"7.3万","title":"逻辑思维：182 我们到底该信谁","totalscore":8.849806,"type":"program_ondemand","updatetime":1.474329814E9},{"category_id":3251,"category_name":"脱口秀","cover":"http://pic.qingting.fm/2017/02/11/partner_9f36e48ca14e950c1b572b969a023d5f.jpg!200","duration":168.484,"file_path":"m4a/589f17227cb8913977b2778b_6780641_64.m4a?u=678&channelId=206971&programId=6496916","id":6496916,"idx_id":"radio_program_6496916","mediainfo_id":6780641,"parent_id":206971,"parent_name":"大宋竹林寺","parent_type":"channel","playcount":"771","title":"竹林墨客：如何评价逻辑思维罗胖？","totalscore":0.32712504,"type":"program_ondemand","updatetime":1.48682119E9}],"maxScore":8.849806,"numFound":29212,"start":0},"groupValue":"program_ondemand"}]
     * errormsg :
     * errorno : 0
     */

    private int code;
    private String errormsg;
    private int errorno;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
         * doclist : {"docs":[{"category_id":547,"category_name":"娱乐","cover":"http://pic.qingting.fm/2015/0925/2015092519571864.jpg!200","duration":3296.3,"file_path":"m4a/57ad51537cb8911fa0cf409c_5671001_64.m4a?u=678&channelId=125114&programId=5063432","id":5063432,"idx_id":"radio_program_5063432","mediainfo_id":5671001,"parent_id":125114,"parent_name":"倾听文化汇","parent_type":"channel","playcount":"7.3万","title":"逻辑思维：182 我们到底该信谁","totalscore":8.849806,"type":"program_ondemand","updatetime":1.474329814E9},{"category_id":3251,"category_name":"脱口秀","cover":"http://pic.qingting.fm/2017/02/11/partner_9f36e48ca14e950c1b572b969a023d5f.jpg!200","duration":168.484,"file_path":"m4a/589f17227cb8913977b2778b_6780641_64.m4a?u=678&channelId=206971&programId=6496916","id":6496916,"idx_id":"radio_program_6496916","mediainfo_id":6780641,"parent_id":206971,"parent_name":"大宋竹林寺","parent_type":"channel","playcount":"771","title":"竹林墨客：如何评价逻辑思维罗胖？","totalscore":0.32712504,"type":"program_ondemand","updatetime":1.48682119E9}],"maxScore":8.849806,"numFound":29212,"start":0}
         * groupValue : program_ondemand
         */

        private DoclistBean doclist;
        private String groupValue;

        public DoclistBean getDoclist() {
            return doclist;
        }

        public void setDoclist(DoclistBean doclist) {
            this.doclist = doclist;
        }

        public String getGroupValue() {
            return groupValue;
        }

        public void setGroupValue(String groupValue) {
            this.groupValue = groupValue;
        }

        public static class DoclistBean {
            /**
             * docs : [{"category_id":547,"category_name":"娱乐","cover":"http://pic.qingting.fm/2015/0925/2015092519571864.jpg!200","duration":3296.3,"file_path":"m4a/57ad51537cb8911fa0cf409c_5671001_64.m4a?u=678&channelId=125114&programId=5063432","id":5063432,"idx_id":"radio_program_5063432","mediainfo_id":5671001,"parent_id":125114,"parent_name":"倾听文化汇","parent_type":"channel","playcount":"7.3万","title":"逻辑思维：182 我们到底该信谁","totalscore":8.849806,"type":"program_ondemand","updatetime":1.474329814E9},{"category_id":3251,"category_name":"脱口秀","cover":"http://pic.qingting.fm/2017/02/11/partner_9f36e48ca14e950c1b572b969a023d5f.jpg!200","duration":168.484,"file_path":"m4a/589f17227cb8913977b2778b_6780641_64.m4a?u=678&channelId=206971&programId=6496916","id":6496916,"idx_id":"radio_program_6496916","mediainfo_id":6780641,"parent_id":206971,"parent_name":"大宋竹林寺","parent_type":"channel","playcount":"771","title":"竹林墨客：如何评价逻辑思维罗胖？","totalscore":0.32712504,"type":"program_ondemand","updatetime":1.48682119E9}]
             * maxScore : 8.849806
             * numFound : 29212
             * start : 0
             */

            private double maxScore;
            private int numFound;
            private int start;
            private List<DocsBean> docs;

            public double getMaxScore() {
                return maxScore;
            }

            public void setMaxScore(double maxScore) {
                this.maxScore = maxScore;
            }

            public int getNumFound() {
                return numFound;
            }

            public void setNumFound(int numFound) {
                this.numFound = numFound;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public List<DocsBean> getDocs() {
                return docs;
            }

            public void setDocs(List<DocsBean> docs) {
                this.docs = docs;
            }

            public static class DocsBean {
                /**
                 * category_id : 547
                 * category_name : 娱乐
                 * cover : http://pic.qingting.fm/2015/0925/2015092519571864.jpg!200
                 * duration : 3296.3
                 * file_path : m4a/57ad51537cb8911fa0cf409c_5671001_64.m4a?u=678&channelId=125114&programId=5063432
                 * id : 5063432.0
                 * idx_id : radio_program_5063432
                 * mediainfo_id : 5671001.0
                 * parent_id : 125114
                 * parent_name : 倾听文化汇
                 * parent_type : channel
                 * playcount : 7.3万
                 * title : 逻辑思维：182 我们到底该信谁
                 * totalscore : 8.849806
                 * type : program_ondemand
                 * updatetime : 1.474329814E9
                 */

                private int category_id;
                private String category_name;
                private String cover;
                private double duration;
                private String file_path;
                private double id;
                private String idx_id;
                private double mediainfo_id;
                private int parent_id;
                private String parent_name;
                private String parent_type;
                private String playcount;
                private String title;
                private double totalscore;
                private String type;
                private double updatetime;

                public int getCategory_id() {
                    return category_id;
                }

                public void setCategory_id(int category_id) {
                    this.category_id = category_id;
                }

                public String getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(String category_name) {
                    this.category_name = category_name;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public double getDuration() {
                    return duration;
                }

                public void setDuration(double duration) {
                    this.duration = duration;
                }

                public String getFile_path() {
                    return file_path;
                }

                public void setFile_path(String file_path) {
                    this.file_path = file_path;
                }

                public double getId() {
                    return id;
                }

                public void setId(double id) {
                    this.id = id;
                }

                public String getIdx_id() {
                    return idx_id;
                }

                public void setIdx_id(String idx_id) {
                    this.idx_id = idx_id;
                }

                public double getMediainfo_id() {
                    return mediainfo_id;
                }

                public void setMediainfo_id(double mediainfo_id) {
                    this.mediainfo_id = mediainfo_id;
                }

                public int getParent_id() {
                    return parent_id;
                }

                public void setParent_id(int parent_id) {
                    this.parent_id = parent_id;
                }

                public String getParent_name() {
                    return parent_name;
                }

                public void setParent_name(String parent_name) {
                    this.parent_name = parent_name;
                }

                public String getParent_type() {
                    return parent_type;
                }

                public void setParent_type(String parent_type) {
                    this.parent_type = parent_type;
                }

                public String getPlaycount() {
                    return playcount;
                }

                public void setPlaycount(String playcount) {
                    this.playcount = playcount;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public double getTotalscore() {
                    return totalscore;
                }

                public void setTotalscore(double totalscore) {
                    this.totalscore = totalscore;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public double getUpdatetime() {
                    return updatetime;
                }

                public void setUpdatetime(double updatetime) {
                    this.updatetime = updatetime;
                }
            }
        }
    }
}
