package com.smartlife.qintin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class CategoryAllRadioModel {

    /**
     * data : [{"auto_play":0,"award_desc":"","award_open":0,"award_text":"","category_id":521,"chatgroup_id":0,"description":"【限时免费，每周三更新五集】作者：清凉如意。这桩婚姻，原本各怀目的。新婚约定，他要的只有相互信任，她要求夫妻亲密一月一次。是夜，他关灯。她问：\u201c干嘛？\u201d他答：\u201c履行亲密约定。\u201d次夜，他揽她入怀。她惊：\u201c干嘛？\u201d他坏坏一笑：\u201c履行亲密约定。\u201d第二天翻出约定，她才发现，\u2018夫妻亲密一月一次\u2019，被改成了\u2018一日一次\u2019。慕早早愤愤咬牙：\u201c苏言之，你这个无良医生！\u201d\r\n","id":211576,"latest_program":"闪爱成婚135","link_id":0,"playcount":"355.4万","program_count":135,"record_enabled":0,"sale_props":"","sale_type":0,"score":10,"star":0,"thumbs":{"200_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!200","400_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!400","800_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!800","large_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!large","medium_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!medium","small_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!small"},"title":"闪爱成婚","type":"channel_ondemand","update_time":"2017-05-10 14:01:01","weburl":"http://m.qingting.fm/vchannels/211576"},{"auto_play":0,"award_desc":"","award_open":0,"award_text":"","category_id":521,"chatgroup_id":0,"description":"【限时免费，每周三更新五集】【蜻蜓FM出品】\r\n作者：24K纯二。演播：一棵坚强树\r\n妹妹差点被......我却无动于衷，只想好好照顾她。\r\n李欣没来让我不能安心，我是生怕她出一点点事的。\r\n于是我赶紧不等了，利索跑进奶茶店去。\r\n夏姐又笑问我干嘛，我直接开口：\u201c李欣还没来？\u201d\r\n她不由苦笑：\u201c我又不是奴隶主，总得给她放放假吧，她最近都挺累的。\u201d\r\n原来是放假了，虚惊一场。\r\n我安下心来，既然她没来我也不必偷偷摸摸","id":213274,"latest_program":"《我和妹子那些事》041集","link_id":0,"playcount":"2.1万","program_count":45,"record_enabled":0,"sale_props":"","sale_type":0,"score":6,"star":0,"thumbs":{"200_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!200","400_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!400","800_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!800","large_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!large","medium_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!medium","small_thumb":"http://pic.qingting.fm/2017/0504/20170504175906407.jpg!small"},"title":"我和妹子那些事","type":"channel_ondemand","update_time":"2017-05-10 12:01:03","weburl":"http://m.qingting.fm/vchannels/213274"}]
     * errormsg : 0
     * errorno : 0
     * total : 3901
     */

    private int errormsg;
    private int errorno;
    private int total;
    private List<DataBean> data;

    public int getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(int errormsg) {
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
         * auto_play : 0
         * award_desc :
         * award_open : 0
         * award_text :
         * category_id : 521
         * chatgroup_id : 0
         * description : 【限时免费，每周三更新五集】作者：清凉如意。这桩婚姻，原本各怀目的。新婚约定，他要的只有相互信任，她要求夫妻亲密一月一次。是夜，他关灯。她问：“干嘛？”他答：“履行亲密约定。”次夜，他揽她入怀。她惊：“干嘛？”他坏坏一笑：“履行亲密约定。”第二天翻出约定，她才发现，‘夫妻亲密一月一次’，被改成了‘一日一次’。慕早早愤愤咬牙：“苏言之，你这个无良医生！”

         * id : 211576
         * latest_program : 闪爱成婚135
         * link_id : 0
         * playcount : 355.4万
         * program_count : 135
         * record_enabled : 0
         * sale_props :
         * sale_type : 0
         * score : 10
         * star : 0
         * thumbs : {"200_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!200","400_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!400","800_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!800","large_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!large","medium_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!medium","small_thumb":"http://pic.qingting.fm/2017/0411/20170411163300318.jpg!small"}
         * title : 闪爱成婚
         * type : channel_ondemand
         * update_time : 2017-05-10 14:01:01
         * weburl : http://m.qingting.fm/vchannels/211576
         */

        private int auto_play;
        private String award_desc;
        private int award_open;
        private String award_text;
        private int category_id;
        private int chatgroup_id;
        private String description;
        private int id;
        private String latest_program;
        private int link_id;
        private String playcount;
        private int program_count;
        private int record_enabled;
        private String sale_props;
        private int sale_type;
        private int score;
        private int star;
        private ThumbsBean thumbs;
        private String title;
        private String type;
        private String update_time;
        private String weburl;

        public int getAuto_play() {
            return auto_play;
        }

        public void setAuto_play(int auto_play) {
            this.auto_play = auto_play;
        }

        public String getAward_desc() {
            return award_desc;
        }

        public void setAward_desc(String award_desc) {
            this.award_desc = award_desc;
        }

        public int getAward_open() {
            return award_open;
        }

        public void setAward_open(int award_open) {
            this.award_open = award_open;
        }

        public String getAward_text() {
            return award_text;
        }

        public void setAward_text(String award_text) {
            this.award_text = award_text;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLatest_program() {
            return latest_program;
        }

        public void setLatest_program(String latest_program) {
            this.latest_program = latest_program;
        }

        public int getLink_id() {
            return link_id;
        }

        public void setLink_id(int link_id) {
            this.link_id = link_id;
        }

        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }

        public int getProgram_count() {
            return program_count;
        }

        public void setProgram_count(int program_count) {
            this.program_count = program_count;
        }

        public int getRecord_enabled() {
            return record_enabled;
        }

        public void setRecord_enabled(int record_enabled) {
            this.record_enabled = record_enabled;
        }

        public String getSale_props() {
            return sale_props;
        }

        public void setSale_props(String sale_props) {
            this.sale_props = sale_props;
        }

        public int getSale_type() {
            return sale_type;
        }

        public void setSale_type(int sale_type) {
            this.sale_type = sale_type;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public ThumbsBean getThumbs() {
            return thumbs;
        }

        public void setThumbs(ThumbsBean thumbs) {
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

        public static class ThumbsBean {
            /**
             * 200_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!200
             * 400_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!400
             * 800_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!800
             * large_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!large
             * medium_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!medium
             * small_thumb : http://pic.qingting.fm/2017/0411/20170411163300318.jpg!small
             */

            @SerializedName("200_thumb")
            private String _$200_thumb;
            @SerializedName("400_thumb")
            private String _$400_thumb;
            @SerializedName("800_thumb")
            private String _$800_thumb;
            private String large_thumb;
            private String medium_thumb;
            private String small_thumb;

            public String get_$200_thumb() {
                return _$200_thumb;
            }

            public void set_$200_thumb(String _$200_thumb) {
                this._$200_thumb = _$200_thumb;
            }

            public String get_$400_thumb() {
                return _$400_thumb;
            }

            public void set_$400_thumb(String _$400_thumb) {
                this._$400_thumb = _$400_thumb;
            }

            public String get_$800_thumb() {
                return _$800_thumb;
            }

            public void set_$800_thumb(String _$800_thumb) {
                this._$800_thumb = _$800_thumb;
            }

            public String getLarge_thumb() {
                return large_thumb;
            }

            public void setLarge_thumb(String large_thumb) {
                this.large_thumb = large_thumb;
            }

            public String getMedium_thumb() {
                return medium_thumb;
            }

            public void setMedium_thumb(String medium_thumb) {
                this.medium_thumb = medium_thumb;
            }

            public String getSmall_thumb() {
                return small_thumb;
            }

            public void setSmall_thumb(String small_thumb) {
                this.small_thumb = small_thumb;
            }
        }
    }
}
