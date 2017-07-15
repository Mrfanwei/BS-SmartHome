package com.smartlife.xunfei.model;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SmartHomeAirObjModel {

    /**
     * operation : SET
     * rc : 0
     * semantic : {"slots":{"attr":"温度","attrType":"Object(digital)","attrValue":{"direct":"+","offset":"2","ref":"CUR","type":"SPOT"},"location":{"room":"客厅","type":"LOC_HOUSE","zone":"楼上"}}}
     * service : airControl_smartHome
     * text : 将楼上客厅空调温度调高两度
     * uuid : atn00261175@un1c230cad91e76f2a01
     * used_state : {"state_key":"fg::airControl_smartHome::default::default","state":"default"}
     * answer : {"text":"已为您将空调温度升高\"两\"度"}
     * dialog_stat : dataInvalid
     * sid : atn00261175@un1c230cad91e76f2a01
     */

    private String operation;
    private int rc;
    private SemanticBean semantic;
    private String service;
    private String text;
    private String uuid;
    private UsedStateBean used_state;
    private AnswerBean answer;
    private String dialog_stat;
    private String sid;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public SemanticBean getSemantic() {
        return semantic;
    }

    public void setSemantic(SemanticBean semantic) {
        this.semantic = semantic;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UsedStateBean getUsed_state() {
        return used_state;
    }

    public void setUsed_state(UsedStateBean used_state) {
        this.used_state = used_state;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getDialog_stat() {
        return dialog_stat;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static class SemanticBean {
        /**
         * slots : {"attr":"温度","attrType":"Object(digital)","attrValue":{"direct":"+","offset":"2","ref":"CUR","type":"SPOT"},"location":{"room":"客厅","type":"LOC_HOUSE","zone":"楼上"}}
         */

        private SlotsBean slots;

        public SlotsBean getSlots() {
            return slots;
        }

        public void setSlots(SlotsBean slots) {
            this.slots = slots;
        }

        public static class SlotsBean {
            /**
             * attr : 温度
             * attrType : Object(digital)
             * attrValue : {"direct":"+","offset":"2","ref":"CUR","type":"SPOT"}
             * location : {"room":"客厅","type":"LOC_HOUSE","zone":"楼上"}
             */

            private String attr;
            private String attrType;
            private AttrValueBean attrValue;
            private LocationBean location;

            public String getAttr() {
                return attr;
            }

            public void setAttr(String attr) {
                this.attr = attr;
            }

            public String getAttrType() {
                return attrType;
            }

            public void setAttrType(String attrType) {
                this.attrType = attrType;
            }

            public AttrValueBean getAttrValue() {
                return attrValue;
            }

            public void setAttrValue(AttrValueBean attrValue) {
                this.attrValue = attrValue;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public static class AttrValueBean {
                /**
                 * direct : +
                 * offset : 2
                 * ref : CUR
                 * type : SPOT
                 */

                private String direct;
                private String offset;
                private String ref;
                private String type;

                public String getDirect() {
                    return direct;
                }

                public void setDirect(String direct) {
                    this.direct = direct;
                }

                public String getOffset() {
                    return offset;
                }

                public void setOffset(String offset) {
                    this.offset = offset;
                }

                public String getRef() {
                    return ref;
                }

                public void setRef(String ref) {
                    this.ref = ref;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class LocationBean {
                /**
                 * room : 客厅
                 * type : LOC_HOUSE
                 * zone : 楼上
                 */

                private String room;
                private String type;
                private String zone;

                public String getRoom() {
                    return room;
                }

                public void setRoom(String room) {
                    this.room = room;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getZone() {
                    return zone;
                }

                public void setZone(String zone) {
                    this.zone = zone;
                }
            }
        }
    }

    public static class UsedStateBean {
        /**
         * state_key : fg::airControl_smartHome::default::default
         * state : default
         */

        private String state_key;
        private String state;

        public String getState_key() {
            return state_key;
        }

        public void setState_key(String state_key) {
            this.state_key = state_key;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class AnswerBean {
        /**
         * text : 已为您将空调温度升高"两"度
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
