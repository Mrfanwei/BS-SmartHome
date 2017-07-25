package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class DianBoProgram {

    /**
     * data : [{"chatgroup_id":0,"description":"","detail":{},"duration":1180,"id":7111104,"mediainfo":{"bitrates_url":[{"bitrate":62,"file_path":"m4a/591411907cb8913c4a3df933_7298995_64.m4a?u=678&channelId=177796&programId=7111104","qetag":"lk_gAp5kvyAPF5uVj4goBcuskAfb"},{"bitrate":22,"file_path":"m4a/591411907cb8913c4a3df933_7298995_24.m4a?u=678&channelId=177796&programId=7111104","qetag":"FrXBDfNuF4jCYBs5z3T9JguvyHwO"}],"duration":1180,"id":7298995},"original_fee":0,"price":0,"redirect_url":"","sale_status":"","sequence":-231,"thumbs":null,"title":"0512期：\u201c撞车\u201d的梦想","type":"program_ondemand","update_time":"2017-05-12 11:15:00","weburl":"http://m.qingting.fm/vchannels/177796/programs/7111104"},{"chatgroup_id":0,"description":"","detail":{},"duration":1464,"id":7110486,"mediainfo":{"bitrates_url":[{"bitrate":62,"file_path":"m4a/5913ef1a7cb8913c4ea194f0_7298417_64.m4a?u=678&channelId=177796&programId=7110486","qetag":"lqit94UmUsf7fy22Qwh9Lh8z36ab"},{"bitrate":22,"file_path":"m4a/5913ef1a7cb8913c4ea194f0_7298417_24.m4a?u=678&channelId=177796&programId=7110486","qetag":"ljRVW5tw2B0MfG5NSvyZxIBnIq4u"}],"duration":1464,"id":7298417},"original_fee":0,"price":0,"redirect_url":"","sale_status":"","sequence":-230,"thumbs":null,"title":"0511期：\u201c微信达人\u201d之死","type":"program_ondemand","update_time":"2017-05-11 13:45:58","weburl":"http://m.qingting.fm/vchannels/177796/programs/7110486"}]
     * errormsg :
     * errorno : 0
     * total : 236
     */

    private String errormsg;
    private int errorno;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * chatgroup_id : 0
         * description :
         * detail : {}
         * duration : 1180
         * id : 7111104.0
         * mediainfo : {"bitrates_url":[{"bitrate":62,"file_path":"m4a/591411907cb8913c4a3df933_7298995_64.m4a?u=678&channelId=177796&programId=7111104","qetag":"lk_gAp5kvyAPF5uVj4goBcuskAfb"},{"bitrate":22,"file_path":"m4a/591411907cb8913c4a3df933_7298995_24.m4a?u=678&channelId=177796&programId=7111104","qetag":"FrXBDfNuF4jCYBs5z3T9JguvyHwO"}],"duration":1180,"id":7298995}
         * original_fee : 0
         * price : 0
         * redirect_url :
         * sale_status :
         * sequence : -231
         * thumbs : null
         * title : 0512期：“撞车”的梦想
         * type : program_ondemand
         * update_time : 2017-05-12 11:15:00
         * weburl : http://m.qingting.fm/vchannels/177796/programs/7111104
         */

        private int chatgroup_id;
        private String description;
        private DetailBean detail;
        private int duration;
        private int id;
        private MediainfoBean mediainfo;
        private int original_fee;
        private int price;
        private String redirect_url;
        private String sale_status;
        private int sequence;
        private Object thumbs;
        private String title;
        private String type;
        private String update_time;
        private String weburl;
        /**
         * playcount : 40.8万
         */

        private String playcount;

        public int getChatgroup_id() {
            return chatgroup_id;
        }

        public void setChatgroup_id(int chatgroup_id) {
            this.chatgroup_id = chatgroup_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MediainfoBean getMediainfo() {
            return mediainfo;
        }

        public void setMediainfo(MediainfoBean mediainfo) {
            this.mediainfo = mediainfo;
        }

        public int getOriginal_fee() {
            return original_fee;
        }

        public void setOriginal_fee(int original_fee) {
            this.original_fee = original_fee;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getRedirect_url() {
            return redirect_url;
        }

        public void setRedirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
        }

        public String getSale_status() {
            return sale_status;
        }

        public void setSale_status(String sale_status) {
            this.sale_status = sale_status;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }

        public Object getThumbs() {
            return thumbs;
        }

        public void setThumbs(Object thumbs) {
            this.thumbs = thumbs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public static class DetailBean {
        }

        public static class MediainfoBean {
            /**
             * bitrates_url : [{"bitrate":62,"file_path":"m4a/591411907cb8913c4a3df933_7298995_64.m4a?u=678&channelId=177796&programId=7111104","qetag":"lk_gAp5kvyAPF5uVj4goBcuskAfb"},{"bitrate":22,"file_path":"m4a/591411907cb8913c4a3df933_7298995_24.m4a?u=678&channelId=177796&programId=7111104","qetag":"FrXBDfNuF4jCYBs5z3T9JguvyHwO"}]
             * duration : 1180
             * id : 7298995.0
             */

            private int duration;
            private double id;
            private List<BitratesUrlBean> bitrates_url;

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public double getId() {
                return id;
            }

            public void setId(double id) {
                this.id = id;
            }

            public List<BitratesUrlBean> getBitrates_url() {
                return bitrates_url;
            }

            public void setBitrates_url(List<BitratesUrlBean> bitrates_url) {
                this.bitrates_url = bitrates_url;
            }

            public static class BitratesUrlBean {
                /**
                 * bitrate : 62
                 * file_path : m4a/591411907cb8913c4a3df933_7298995_64.m4a?u=678&channelId=177796&programId=7111104
                 * qetag : lk_gAp5kvyAPF5uVj4goBcuskAfb
                 */

                private int bitrate;
                private String file_path;
                private String qetag;

                public int getBitrate() {
                    return bitrate;
                }

                public void setBitrate(int bitrate) {
                    this.bitrate = bitrate;
                }

                public String getFile_path() {
                    return file_path;
                }

                public void setFile_path(String file_path) {
                    this.file_path = file_path;
                }

                public String getQetag() {
                    return qetag;
                }

                public void setQetag(String qetag) {
                    this.qetag = qetag;
                }
            }
        }
    }
}
