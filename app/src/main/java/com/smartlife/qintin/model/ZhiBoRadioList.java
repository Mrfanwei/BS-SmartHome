package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ZhiBoRadioList {

    /**
     * data : [{"detail":{"channel_id":4847,"chatgroup_id":0,"detail":null,"duration":1740,"end_time":"23:59:00","id":1104980,"mediainfo":{"id":4847},"program_id":1891720,"start_time":"23:30:00","title":"名家视点","type":"playbill"},"id":8159,"object_id":1104980,"parent_info":{"parent_extra":{"category_id":5},"parent_id":4847,"parent_type":"channel"},"sequence":159,"sub_title":"广东广播电视台股市广播","thumb":"http:点","update_time":""}]
     * errcode : 0
     * errmsg :
     * errorno : 0
     */

    private int errcode;
    private String errmsg;
    private int errorno;
    private List<DataBean> data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
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
         * detail : {"channel_id":4847,"chatgroup_id":0,"detail":null,"duration":1740,"end_time":"23:59:00","id":1104980,"mediainfo":{"id":4847},"program_id":1891720,"start_time":"23:30:00","title":"名家视点","type":"playbill"}
         * id : 8159
         * object_id : 1104980.0
         * parent_info : {"parent_extra":{"category_id":5},"parent_id":4847,"parent_type":"channel"}
         * sequence : 159
         * sub_title : 广东广播电视台股市广播
         * thumb : http:点
         * update_time :
         */

        private DetailBean detail;
        private int id;
        private double object_id;
        private ParentInfoBean parent_info;
        private int sequence;
        private String sub_title;
        private String thumb;
        private String update_time;

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getObject_id() {
            return object_id;
        }

        public void setObject_id(double object_id) {
            this.object_id = object_id;
        }

        public ParentInfoBean getParent_info() {
            return parent_info;
        }

        public void setParent_info(ParentInfoBean parent_info) {
            this.parent_info = parent_info;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public static class DetailBean {
            /**
             * channel_id : 4847
             * chatgroup_id : 0
             * detail : null
             * duration : 1740
             * end_time : 23:59:00
             * id : 1104980.0
             * mediainfo : {"id":4847}
             * program_id : 1891720.0
             * start_time : 23:30:00
             * title : 名家视点
             * type : playbill
             */

            private int channel_id;
            private int chatgroup_id;
            private Object detail;
            private int duration;
            private String end_time;
            private double id;
            private MediainfoBean mediainfo;
            private double program_id;
            private String start_time;
            private String title;
            private String type;

            public int getChannel_id() {
                return channel_id;
            }

            public void setChannel_id(int channel_id) {
                this.channel_id = channel_id;
            }

            public int getChatgroup_id() {
                return chatgroup_id;
            }

            public void setChatgroup_id(int chatgroup_id) {
                this.chatgroup_id = chatgroup_id;
            }

            public Object getDetail() {
                return detail;
            }

            public void setDetail(Object detail) {
                this.detail = detail;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public double getId() {
                return id;
            }

            public void setId(double id) {
                this.id = id;
            }

            public MediainfoBean getMediainfo() {
                return mediainfo;
            }

            public void setMediainfo(MediainfoBean mediainfo) {
                this.mediainfo = mediainfo;
            }

            public double getProgram_id() {
                return program_id;
            }

            public void setProgram_id(double program_id) {
                this.program_id = program_id;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
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

            public static class MediainfoBean {
                /**
                 * id : 4847
                 */

                private int id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }
            }
        }

        public static class ParentInfoBean {
            /**
             * parent_extra : {"category_id":5}
             * parent_id : 4847
             * parent_type : channel
             */

            private ParentExtraBean parent_extra;
            private int parent_id;
            private String parent_type;

            public ParentExtraBean getParent_extra() {
                return parent_extra;
            }

            public void setParent_extra(ParentExtraBean parent_extra) {
                this.parent_extra = parent_extra;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public String getParent_type() {
                return parent_type;
            }

            public void setParent_type(String parent_type) {
                this.parent_type = parent_type;
            }

            public static class ParentExtraBean {
                /**
                 * category_id : 5
                 */

                private int category_id;

                public int getCategory_id() {
                    return category_id;
                }

                public void setCategory_id(int category_id) {
                    this.category_id = category_id;
                }
            }
        }
    }
}
