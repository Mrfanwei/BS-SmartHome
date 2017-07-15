package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class DianBoRecommendModel {

    /**
     * data : [{"brief_name":"","name":"banner","recommends":[{"detail":{"description":"","id":1231,"network":"","thumb":"","title":"民国\u201c渣男\u201d徐志摩的四个女人","type":"activity","url":"http://sss.qingting.fm/newTopic/index.html?newTopicId=59102e212941f8284d9456ba"},"id":2001,"object_id":1231,"parent_info":null,"sequence":1,"sub_title":"","thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg","thumbs":{"large_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!large","medium_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!medium","small_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!small"},"title":"民国\u201c渣男\u201d徐志摩的四个女人","update_time":"2017-05-11 11:58:21"},{"detail":{"channel_star":10,"chatgroup_id":0,"description":"","detail":{},"duration":1500,"id":3069143,"mediainfo":{"bitrates_url":[{"bitrate":24,"file_path":"vod/00/00/0000000000000000000026023940_24.m4a?u=678&channelId=111996&programId=3069143","qetag":"lmh715qqQpSLy1fNkvHMudhjwd5s"},{"bitrate":64,"file_path":"vod/00/00/0000000000000000000026023940_64.m4a?u=678&channelId=111996&programId=3069143","qetag":"lkogRL-ufRPbPoTMPN3rLh0NVM57"}],"duration":1500,"id":3902905},"original_fee":0,"price":0,"redirect_url":"","sale_status":"","sequence":0,"thumbs":null,"title":"三侠剑 第1回","type":"program_ondemand","update_time":"2017-05-10 18:05:43"},"id":837003,"object_id":7104063,"parent_info":{"parent_extra":{"category_id":3608,"tag":""},"parent_id":138504,"parent_name":"央广评论","parent_type":"channel_ondemand"},"sequence":3,"sub_title":"","thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg","thumbs":{"200_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!200","400_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!400","800_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!800"},"title":"民办学校面试，何以离题万里","update_time":"2017-05-11 06:24:39"}],"redirect":{"redirect_type":"section","section_id":837},"section_id":837,"sequence":34}]
     * errormsg :
     * errorno : 0
     */

    private String errormsg;
    private int errorno;
    private List<DataBean> data;

    /**
     * parent_info : {"parent_extra":{"category_id":3251,"tag":""},"parent_id":212192,"parent_name":"晓说 2017","parent_type":"channel_ondemand"}
     */

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
         * brief_name :
         * name : banner
         * recommends : [{"detail":{"description":"","id":1231,"network":"","thumb":"","title":"民国\u201c渣男\u201d徐志摩的四个女人","type":"activity","url":"http://sss.qingting.fm/newTopic/index.html?newTopicId=59102e212941f8284d9456ba"},"id":2001,"object_id":1231,"parent_info":null,"sequence":1,"sub_title":"","thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg","thumbs":{"large_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!large","medium_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!medium","small_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!small"},"title":"民国\u201c渣男\u201d徐志摩的四个女人","update_time":"2017-05-11 11:58:21"},{"detail":{"channel_star":10,"chatgroup_id":0,"description":"","detail":{},"duration":1500,"id":3069143,"mediainfo":{"bitrates_url":[{"bitrate":24,"file_path":"vod/00/00/0000000000000000000026023940_24.m4a?u=678&channelId=111996&programId=3069143","qetag":"lmh715qqQpSLy1fNkvHMudhjwd5s"},{"bitrate":64,"file_path":"vod/00/00/0000000000000000000026023940_64.m4a?u=678&channelId=111996&programId=3069143","qetag":"lkogRL-ufRPbPoTMPN3rLh0NVM57"}],"duration":1500,"id":3902905},"original_fee":0,"price":0,"redirect_url":"","sale_status":"","sequence":0,"thumbs":null,"title":"三侠剑 第1回","type":"program_ondemand","update_time":"2017-05-10 18:05:43"},"id":837003,"object_id":7104063,"parent_info":{"parent_extra":{"category_id":3608,"tag":""},"parent_id":138504,"parent_name":"央广评论","parent_type":"channel_ondemand"},"sequence":3,"sub_title":"","thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg","thumbs":{"200_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!200","400_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!400","800_thumb":"http://pic.qingting.fm/2015/1224/20151224154559738.jpg!800"},"title":"民办学校面试，何以离题万里","update_time":"2017-05-11 06:24:39"}]
         * redirect : {"redirect_type":"section","section_id":837}
         * section_id : 837
         * sequence : 34
         */

        private String brief_name;
        private String name;
        private RedirectBean redirect;
        private int section_id;
        private int sequence;
        private List<RecommendsBean> recommends;

        public String getBrief_name() {
            return brief_name;
        }

        public void setBrief_name(String brief_name) {
            this.brief_name = brief_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public RedirectBean getRedirect() {
            return redirect;
        }

        public void setRedirect(RedirectBean redirect) {
            this.redirect = redirect;
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

        public List<RecommendsBean> getRecommends() {
            return recommends;
        }

        public void setRecommends(List<RecommendsBean> recommends) {
            this.recommends = recommends;
        }

        public static class RedirectBean {
            /**
             * redirect_type : section
             * section_id : 837
             */

            private String redirect_type;
            private int section_id;

            public String getRedirect_type() {
                return redirect_type;
            }

            public void setRedirect_type(String redirect_type) {
                this.redirect_type = redirect_type;
            }

            public int getSection_id() {
                return section_id;
            }

            public void setSection_id(int section_id) {
                this.section_id = section_id;
            }
        }

        public static class RecommendsBean {
            /**
             * detail : {"description":"","id":1231,"network":"","thumb":"","title":"民国\u201c渣男\u201d徐志摩的四个女人","type":"activity","url":"http://sss.qingting.fm/newTopic/index.html?newTopicId=59102e212941f8284d9456ba"}
             * id : 2001
             * object_id : 1231
             * parent_info : null
             * sequence : 1
             * sub_title :
             * thumb : http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg
             * thumbs : {"large_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!large","medium_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!medium","small_thumb":"http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!small"}
             * title : 民国“渣男”徐志摩的四个女人
             * update_time : 2017-05-11 11:58:21
             */

            private DetailBean detail;
            private int id;
            private int object_id;
            private ParentInfoBean parent_info;
            private int sequence;
            private String sub_title;
            private String thumb;
            private ThumbsBean thumbs;
            private String title;
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

            public int getObject_id() {
                return object_id;
            }

            public void setObject_id(int object_id) {
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

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public static class DetailBean {
                /**
                 * description :
                 * id : 1231
                 * network :
                 * thumb :
                 * title : 民国“渣男”徐志摩的四个女人
                 * type : activity
                 * url : http://sss.qingting.fm/newTopic/index.html?newTopicId=59102e212941f8284d9456ba
                 */

                private String description;
                private int id;
                private String network;
                private String thumb;
                private String title;
                private String type;
                private String url;
                private int duration;
                private int channel_star;
                private int original_fee;
                private int price;
                private int chatgroup_id;
                private String redirect_url;
                private String sale_status;
                private int sequence;
                private String update_time;
                private MediainfoBean mediainfo;

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

                public String getNetwork() {
                    return network;
                }

                public void setNetwork(String network) {
                    this.network = network;
                }

                public String getThumb() {
                    return thumb;
                }

                public void setThumb(String thumb) {
                    this.thumb = thumb;
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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public int getChannel_star(){
                    return channel_star;
                }

                public void setChannel_star(int channel_star){
                    this.channel_star=channel_star;
                }

                public int getOriginal_fee(){
                    return original_fee;
                }

                public void setOriginal_fee(int original_fee){
                    this.original_fee=original_fee;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public int getChatgroup_id(){
                    return chatgroup_id;
                }

                public void setChatgroup_id(int chatgroup_id){
                    this.chatgroup_id=chatgroup_id;
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

                public String getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(String update_time) {
                    this.update_time = update_time;
                }

                public MediainfoBean getMediainfo() {
                    return mediainfo;
                }

                public void setMediainfo(MediainfoBean mediainfo) {
                    this.mediainfo = mediainfo;
                }


                public static class MediainfoBean {
                    /**
                     * bitrates_url : [{"bitrate":24,"file_path":"vod/00/00/0000000000000000000026023940_24.m4a?u=678&channelId=111996&programId=3069143","qetag":"lmh715qqQpSLy1fNkvHMudhjwd5s"},{"bitrate":64,"file_path":"vod/00/00/0000000000000000000026023940_64.m4a?u=678&channelId=111996&programId=3069143","qetag":"lkogRL-ufRPbPoTMPN3rLh0NVM57"}]
                     * duration : 1500
                     * id : 3902905
                     */

                    private int duration;
                    private int id;
                    private List<BitratesUrlBean> bitrates_url;

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

                    public List<BitratesUrlBean> getBitrates_url() {
                        return bitrates_url;
                    }

                    public void setBitrates_url(List<BitratesUrlBean> bitrates_url) {
                        this.bitrates_url = bitrates_url;
                    }

                    public static class BitratesUrlBean {
                        /**
                         * bitrate : 24
                         * file_path : vod/00/00/0000000000000000000026023940_24.m4a?u=678&channelId=111996&programId=3069143
                         * qetag : lmh715qqQpSLy1fNkvHMudhjwd5s
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

            public static class ParentInfoBean {
                /**
                 * parent_extra : {"category_id":3251,"tag":""}
                 * parent_id : 212192
                 * parent_name : 晓说 2017
                 * parent_type : channel_ondemand
                 */

                private ParentExtraBean parent_extra;
                private int parent_id;
                private String parent_name;
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

                public static class ParentExtraBean {
                    /**
                     * category_id : 3251
                     * tag :
                     */

                    private int category_id;
                    private String tag;

                    public int getCategory_id() {
                        return category_id;
                    }

                    public void setCategory_id(int category_id) {
                        this.category_id = category_id;
                    }

                    public String getTag() {
                        return tag;
                    }

                    public void setTag(String tag) {
                        this.tag = tag;
                    }
                }
            }

            public static class ThumbsBean {
                /**
                 * large_thumb : http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!large
                 * medium_thumb : http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!medium
                 * small_thumb : http://pic.qingting.fm/goods/2017/05/10/299085f3ba119c3d62d01ef278728af7.jpg!small
                 */

                private String large_thumb;
                private String medium_thumb;
                private String small_thumb;

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
}
