package com.smartlife.xunfei.model;


/**
 * Created by Administrator on 2017/6/27.
 */

public class AirDigitalModel {

    /**
     * semantic : {"slots":{"attrValue":{"direct":"+","type":"SPOT","ref":"CUR","offset":"2"},"attrType":"Object(digital)","attr":"温度","location":{"type":"LOC_HOUSE","room":"客厅","zone":"楼上"}}}
     * rc : 0
     * operation : SET
     * service : airControl_smartHome
     * text : 把楼上客厅空调温度调高2度
     */

    private SemanticBean semantic;
    private int rc;
    private String operation;
    private String service;
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class SemanticBean {
        /**
         * slots : {"attrValue":{"direct":"+","type":"SPOT","ref":"CUR","offset":"2"},"attrType":"Object(digital)","attr":"温度","location":{"type":"LOC_HOUSE","room":"客厅","zone":"楼上"}}
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
             * attrValue : {"direct":"+","type":"SPOT","ref":"CUR","offset":"2"}
             * attrType : Object(digital)
             * attr : 温度
             * location : {"type":"LOC_HOUSE","room":"客厅","zone":"楼上"}
             */

            private AttrValueBean attrValue;
            private String attrType;
            private String attr;
            private LocationBean location;

            public AttrValueBean getAttrValue() {
                return attrValue;
            }

            public void setAttrValue(AttrValueBean attrValue) {
                this.attrValue = attrValue;
            }

            public String getAttrType() {
                return attrType;
            }

            public void setAttrType(String attrType) {
                this.attrType = attrType;
            }

            public String getAttr() {
                return attr;
            }

            public void setAttr(String attr) {
                this.attr = attr;
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
                 * type : SPOT
                 * ref : CUR
                 * offset : 2
                 */

                private String direct;
                private String type;
                private String ref;
                private String offset;

                public String getDirect() {
                    return direct;
                }

                public void setDirect(String direct) {
                    this.direct = direct;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getRef() {
                    return ref;
                }

                public void setRef(String ref) {
                    this.ref = ref;
                }

                public String getOffset() {
                    return offset;
                }

                public void setOffset(String offset) {
                    this.offset = offset;
                }
            }

            public static class LocationBean {
                /**
                 * type : LOC_HOUSE
                 * room : 客厅
                 * zone : 楼上
                 */

                private String type;
                private String room;
                private String zone;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getRoom() {
                    return room;
                }

                public void setRoom(String room) {
                    this.room = room;
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
}
