package com.smartlife.xunfei.model;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SmartHomeModel {

    /**
     * semantic : {"slots":{"attrValue":"开","modifier":"海尔","location":{"type":"LOC_HOUSE","zone":"楼上"},"attrType":"String","attr":"开关"}}
     * rc : 0
     * operation : SET
     * service : tv_smartHome
     * text : 打开楼上的海尔电视
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
         * slots : {"attrValue":"开","modifier":"海尔","location":{"type":"LOC_HOUSE","zone":"楼上"},"attrType":"String","attr":"开关"}
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
             * attrValue : 开
             * modifier : 海尔
             * location : {"type":"LOC_HOUSE","zone":"楼上"}
             * attrType : String
             * attr : 开关
             */

            private String attrValue;
            private String modifier;
            private LocationBean location;
            private String attrType;
            private String attr;

            public String getAttrValue() {
                return attrValue;
            }

            public void setAttrValue(String attrValue) {
                this.attrValue = attrValue;
            }

            public String getModifier() {
                return modifier;
            }

            public void setModifier(String modifier) {
                this.modifier = modifier;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
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

            public static class LocationBean {
                /**
                 * type : LOC_HOUSE
                 * zone : 楼上
                 */

                private String type;
                private String zone;

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
}
