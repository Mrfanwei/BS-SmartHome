package com.smartlife.qintin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/14.
 */

public class DomainCenterModel {

    /**
     * data : {"radiostations_hls":{"mediacenters":[{"access":"live","backup_ips":"hls","codename":"adts-aac","domain":"hls","name":"stations-hls","protocol":"hls","replay":"cache","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"slide","test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_hls","partition_by":"deviceid","preference_change_cost":null},"radiostations_hls_https":{"mediacenters":[{"access":"live","backup_ips":"","bitrate":["24","64"],"codename":"adts-aac","domain":"ls.qingting.fm","name":"stations-hls","protocol":"hls","replay":"cache","replaydomain":"lcache.qingting.fm","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"","test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_hls_https","partition_by":"deviceid","preference_change_cost":null},"radiostations_http":{"mediacenters":[{"access":"res","backup_ips":"http.open.qingting.fm","codename":"mp3-mp3","domain":"http.open.qingting.fm","name":"stations-http","protocol":"http","replay":null,"resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_http","partition_by":"deviceid","preference_change_cost":null},"storedaudio_m4a":{"mediacenters":[{"access":"file_path","backup_ips":"od.open.qingting.fm","codename":"m4a-aac","domain":"od.open.qingting.fm","name":"od-m4a","protocol":"http","replay":null,"resource_provided":"storedaudio","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"cdn","weight":5}],"name":"storedaudio_m4a","partition_by":"file_path","preference_change_cost":null}}
     * errormsg :
     * errorno : 0
     */

    private DataBean data;
    private String errormsg;
    private int errorno;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * radiostations_hls : {"mediacenters":[{"access":"live","backup_ips":"hls","codename":"adts-aac","domain":"hls","name":"stations-hls","protocol":"hls","replay":"cache","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"slide","test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_hls","partition_by":"deviceid","preference_change_cost":null}
         * radiostations_hls_https : {"mediacenters":[{"access":"live","backup_ips":"","bitrate":["24","64"],"codename":"adts-aac","domain":"ls.qingting.fm","name":"stations-hls","protocol":"hls","replay":"cache","replaydomain":"lcache.qingting.fm","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"","test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_hls_https","partition_by":"deviceid","preference_change_cost":null}
         * radiostations_http : {"mediacenters":[{"access":"res","backup_ips":"http.open.qingting.fm","codename":"mp3-mp3","domain":"http.open.qingting.fm","name":"stations-http","protocol":"http","replay":null,"resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"idc","weight":5}],"name":"radiostations_http","partition_by":"deviceid","preference_change_cost":null}
         * storedaudio_m4a : {"mediacenters":[{"access":"file_path","backup_ips":"od.open.qingting.fm","codename":"m4a-aac","domain":"od.open.qingting.fm","name":"od-m4a","protocol":"http","replay":null,"resource_provided":"storedaudio","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"cdn","weight":5}],"name":"storedaudio_m4a","partition_by":"file_path","preference_change_cost":null}
         */

        private RadiostationsHlsBean radiostations_hls;
        private RadiostationsHlsHttpsBean radiostations_hls_https;
        private RadiostationsHttpBean radiostations_http;
        private StoredaudioM4aBean storedaudio_m4a;

        public RadiostationsHlsBean getRadiostations_hls() {
            return radiostations_hls;
        }

        public void setRadiostations_hls(RadiostationsHlsBean radiostations_hls) {
            this.radiostations_hls = radiostations_hls;
        }

        public RadiostationsHlsHttpsBean getRadiostations_hls_https() {
            return radiostations_hls_https;
        }

        public void setRadiostations_hls_https(RadiostationsHlsHttpsBean radiostations_hls_https) {
            this.radiostations_hls_https = radiostations_hls_https;
        }

        public RadiostationsHttpBean getRadiostations_http() {
            return radiostations_http;
        }

        public void setRadiostations_http(RadiostationsHttpBean radiostations_http) {
            this.radiostations_http = radiostations_http;
        }

        public StoredaudioM4aBean getStoredaudio_m4a() {
            return storedaudio_m4a;
        }

        public void setStoredaudio_m4a(StoredaudioM4aBean storedaudio_m4a) {
            this.storedaudio_m4a = storedaudio_m4a;
        }

        public static class RadiostationsHlsBean {
            /**
             * mediacenters : [{"access":"live","backup_ips":"hls","codename":"adts-aac","domain":"hls","name":"stations-hls","protocol":"hls","replay":"cache","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"slide","test_path":"f_ck.dns","type":"idc","weight":5}]
             * name : radiostations_hls
             * partition_by : deviceid
             * preference_change_cost : null
             */

            private String name;
            private String partition_by;
            private Object preference_change_cost;
            private List<MediacentersBean> mediacenters;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPartition_by() {
                return partition_by;
            }

            public void setPartition_by(String partition_by) {
                this.partition_by = partition_by;
            }

            public Object getPreference_change_cost() {
                return preference_change_cost;
            }

            public void setPreference_change_cost(Object preference_change_cost) {
                this.preference_change_cost = preference_change_cost;
            }

            public List<MediacentersBean> getMediacenters() {
                return mediacenters;
            }

            public void setMediacenters(List<MediacentersBean> mediacenters) {
                this.mediacenters = mediacenters;
            }

            public static class MediacentersBean {
                /**
                 * access : live
                 * backup_ips : hls
                 * codename : adts-aac
                 * domain : hls
                 * name : stations-hls
                 * protocol : hls
                 * replay : cache
                 * resource_provided : radiostations
                 * result : Hello Qingting!
                 * slidereplay : slide
                 * test_path : f_ck.dns
                 * type : idc
                 * weight : 5
                 */

                private String access;
                private String backup_ips;
                private String codename;
                private String domain;
                private String name;
                private String protocol;
                private String replay;
                private String resource_provided;
                private String result;
                private String slidereplay;
                private String test_path;
                private String type;
                private int weight;

                public String getAccess() {
                    return access;
                }

                public void setAccess(String access) {
                    this.access = access;
                }

                public String getBackup_ips() {
                    return backup_ips;
                }

                public void setBackup_ips(String backup_ips) {
                    this.backup_ips = backup_ips;
                }

                public String getCodename() {
                    return codename;
                }

                public void setCodename(String codename) {
                    this.codename = codename;
                }

                public String getDomain() {
                    return domain;
                }

                public void setDomain(String domain) {
                    this.domain = domain;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public String getReplay() {
                    return replay;
                }

                public void setReplay(String replay) {
                    this.replay = replay;
                }

                public String getResource_provided() {
                    return resource_provided;
                }

                public void setResource_provided(String resource_provided) {
                    this.resource_provided = resource_provided;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public String getSlidereplay() {
                    return slidereplay;
                }

                public void setSlidereplay(String slidereplay) {
                    this.slidereplay = slidereplay;
                }

                public String getTest_path() {
                    return test_path;
                }

                public void setTest_path(String test_path) {
                    this.test_path = test_path;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }
            }
        }

        public static class RadiostationsHlsHttpsBean {
            /**
             * mediacenters : [{"access":"live","backup_ips":"","bitrate":["24","64"],"codename":"adts-aac","domain":"ls.qingting.fm","name":"stations-hls","protocol":"hls","replay":"cache","replaydomain":"lcache.qingting.fm","resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":"","test_path":"f_ck.dns","type":"idc","weight":5}]
             * name : radiostations_hls_https
             * partition_by : deviceid
             * preference_change_cost : null
             */

            private String name;
            private String partition_by;
            private Object preference_change_cost;
            private List<MediacentersBeanX> mediacenters;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPartition_by() {
                return partition_by;
            }

            public void setPartition_by(String partition_by) {
                this.partition_by = partition_by;
            }

            public Object getPreference_change_cost() {
                return preference_change_cost;
            }

            public void setPreference_change_cost(Object preference_change_cost) {
                this.preference_change_cost = preference_change_cost;
            }

            public List<MediacentersBeanX> getMediacenters() {
                return mediacenters;
            }

            public void setMediacenters(List<MediacentersBeanX> mediacenters) {
                this.mediacenters = mediacenters;
            }

            public static class MediacentersBeanX {
                /**
                 * access : live
                 * backup_ips :
                 * bitrate : ["24","64"]
                 * codename : adts-aac
                 * domain : ls.qingting.fm
                 * name : stations-hls
                 * protocol : hls
                 * replay : cache
                 * replaydomain : lcache.qingting.fm
                 * resource_provided : radiostations
                 * result : Hello Qingting!
                 * slidereplay :
                 * test_path : f_ck.dns
                 * type : idc
                 * weight : 5
                 */

                private String access;
                private String backup_ips;
                private String codename;
                private String domain;
                private String name;
                private String protocol;
                private String replay;
                private String replaydomain;
                private String resource_provided;
                private String result;
                private String slidereplay;
                private String test_path;
                private String type;
                private int weight;
                private List<String> bitrate;

                public String getAccess() {
                    return access;
                }

                public void setAccess(String access) {
                    this.access = access;
                }

                public String getBackup_ips() {
                    return backup_ips;
                }

                public void setBackup_ips(String backup_ips) {
                    this.backup_ips = backup_ips;
                }

                public String getCodename() {
                    return codename;
                }

                public void setCodename(String codename) {
                    this.codename = codename;
                }

                public String getDomain() {
                    return domain;
                }

                public void setDomain(String domain) {
                    this.domain = domain;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public String getReplay() {
                    return replay;
                }

                public void setReplay(String replay) {
                    this.replay = replay;
                }

                public String getReplaydomain() {
                    return replaydomain;
                }

                public void setReplaydomain(String replaydomain) {
                    this.replaydomain = replaydomain;
                }

                public String getResource_provided() {
                    return resource_provided;
                }

                public void setResource_provided(String resource_provided) {
                    this.resource_provided = resource_provided;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public String getSlidereplay() {
                    return slidereplay;
                }

                public void setSlidereplay(String slidereplay) {
                    this.slidereplay = slidereplay;
                }

                public String getTest_path() {
                    return test_path;
                }

                public void setTest_path(String test_path) {
                    this.test_path = test_path;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }

                public List<String> getBitrate() {
                    return bitrate;
                }

                public void setBitrate(List<String> bitrate) {
                    this.bitrate = bitrate;
                }
            }
        }

        public static class RadiostationsHttpBean {
            /**
             * mediacenters : [{"access":"res","backup_ips":"http.open.qingting.fm","codename":"mp3-mp3","domain":"http.open.qingting.fm","name":"stations-http","protocol":"http","replay":null,"resource_provided":"radiostations","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"idc","weight":5}]
             * name : radiostations_http
             * partition_by : deviceid
             * preference_change_cost : null
             */

            private String name;
            private String partition_by;
            private Object preference_change_cost;
            private List<MediacentersBeanXX> mediacenters;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPartition_by() {
                return partition_by;
            }

            public void setPartition_by(String partition_by) {
                this.partition_by = partition_by;
            }

            public Object getPreference_change_cost() {
                return preference_change_cost;
            }

            public void setPreference_change_cost(Object preference_change_cost) {
                this.preference_change_cost = preference_change_cost;
            }

            public List<MediacentersBeanXX> getMediacenters() {
                return mediacenters;
            }

            public void setMediacenters(List<MediacentersBeanXX> mediacenters) {
                this.mediacenters = mediacenters;
            }

            public static class MediacentersBeanXX {
                /**
                 * access : res
                 * backup_ips : http.open.qingting.fm
                 * codename : mp3-mp3
                 * domain : http.open.qingting.fm
                 * name : stations-http
                 * protocol : http
                 * replay : null
                 * resource_provided : radiostations
                 * result : Hello Qingting!
                 * slidereplay : null
                 * test_path : f_ck.dns
                 * type : idc
                 * weight : 5
                 */

                private String access;
                private String backup_ips;
                private String codename;
                private String domain;
                private String name;
                private String protocol;
                private Object replay;
                private String resource_provided;
                private String result;
                private Object slidereplay;
                private String test_path;
                private String type;
                private int weight;

                public String getAccess() {
                    return access;
                }

                public void setAccess(String access) {
                    this.access = access;
                }

                public String getBackup_ips() {
                    return backup_ips;
                }

                public void setBackup_ips(String backup_ips) {
                    this.backup_ips = backup_ips;
                }

                public String getCodename() {
                    return codename;
                }

                public void setCodename(String codename) {
                    this.codename = codename;
                }

                public String getDomain() {
                    return domain;
                }

                public void setDomain(String domain) {
                    this.domain = domain;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public Object getReplay() {
                    return replay;
                }

                public void setReplay(Object replay) {
                    this.replay = replay;
                }

                public String getResource_provided() {
                    return resource_provided;
                }

                public void setResource_provided(String resource_provided) {
                    this.resource_provided = resource_provided;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public Object getSlidereplay() {
                    return slidereplay;
                }

                public void setSlidereplay(Object slidereplay) {
                    this.slidereplay = slidereplay;
                }

                public String getTest_path() {
                    return test_path;
                }

                public void setTest_path(String test_path) {
                    this.test_path = test_path;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }
            }
        }

        public static class StoredaudioM4aBean {
            /**
             * mediacenters : [{"access":"file_path","backup_ips":"od.open.qingting.fm","codename":"m4a-aac","domain":"od.open.qingting.fm","name":"od-m4a","protocol":"http","replay":null,"resource_provided":"storedaudio","result":"Hello Qingting!","slidereplay":null,"test_path":"f_ck.dns","type":"cdn","weight":5}]
             * name : storedaudio_m4a
             * partition_by : file_path
             * preference_change_cost : null
             */

            private String name;
            private String partition_by;
            private Object preference_change_cost;
            private List<MediacentersBeanXXX> mediacenters;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPartition_by() {
                return partition_by;
            }

            public void setPartition_by(String partition_by) {
                this.partition_by = partition_by;
            }

            public Object getPreference_change_cost() {
                return preference_change_cost;
            }

            public void setPreference_change_cost(Object preference_change_cost) {
                this.preference_change_cost = preference_change_cost;
            }

            public List<MediacentersBeanXXX> getMediacenters() {
                return mediacenters;
            }

            public void setMediacenters(List<MediacentersBeanXXX> mediacenters) {
                this.mediacenters = mediacenters;
            }

            public static class MediacentersBeanXXX {
                /**
                 * access : file_path
                 * backup_ips : od.open.qingting.fm
                 * codename : m4a-aac
                 * domain : od.open.qingting.fm
                 * name : od-m4a
                 * protocol : http
                 * replay : null
                 * resource_provided : storedaudio
                 * result : Hello Qingting!
                 * slidereplay : null
                 * test_path : f_ck.dns
                 * type : cdn
                 * weight : 5
                 */

                private String access;
                private String backup_ips;
                private String codename;
                private String domain;
                private String name;
                private String protocol;
                private Object replay;
                private String resource_provided;
                private String result;
                private Object slidereplay;
                private String test_path;
                private String type;
                private int weight;

                public String getAccess() {
                    return access;
                }

                public void setAccess(String access) {
                    this.access = access;
                }

                public String getBackup_ips() {
                    return backup_ips;
                }

                public void setBackup_ips(String backup_ips) {
                    this.backup_ips = backup_ips;
                }

                public String getCodename() {
                    return codename;
                }

                public void setCodename(String codename) {
                    this.codename = codename;
                }

                public String getDomain() {
                    return domain;
                }

                public void setDomain(String domain) {
                    this.domain = domain;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public Object getReplay() {
                    return replay;
                }

                public void setReplay(Object replay) {
                    this.replay = replay;
                }

                public String getResource_provided() {
                    return resource_provided;
                }

                public void setResource_provided(String resource_provided) {
                    this.resource_provided = resource_provided;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public Object getSlidereplay() {
                    return slidereplay;
                }

                public void setSlidereplay(Object slidereplay) {
                    this.slidereplay = slidereplay;
                }

                public String getTest_path() {
                    return test_path;
                }

                public void setTest_path(String test_path) {
                    this.test_path = test_path;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }
            }
        }
    }
}
