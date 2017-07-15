package com.smartlife.xunfei.model;

/**
 * Created by Administrator on 2016/11/6.
 */

public class NluModel {

    /**
     * slots : {"attrValue":"开","attrType":"String","attr":"开关"}
     */

    private SemanticBean semantic;
    /**
     * semantic : {"slots":{"attrValue":"开","attrType":"String","attr":"开关"}}
     * rc : 0
     * operation : SET
     * service : freezer_smartHome
     * text : 打开~。
     */

    private int rc;
    private String operation;
    private String service;
    private String text;
    /**
     * text : 孙悟空从耳朵里掏出金箍棒在地上画了一个避魔圈，他又怕唐僧乱走，于是就对唐生说：“师父，我化缘期间，无论发生什么事你都不要出这个圈子。出了这个范围，就没WiFi了。” 至此之后。唐僧再也没移动过半步。
     * type : T
     */

    private AnswerBean answer;
    /**
     * direct : +
     * type : SPOT
     * ref : CUR
     * offset : 1
     */

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

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public static class SemanticBean {
        /**
         * attrValue : 开
         * attrType : String
         * attr : 开关
         */

        private SlotsBean slots;

        public SlotsBean getSlots() {
            return slots;
        }

        public void setSlots(SlotsBean slots) {
            this.slots = slots;
        }

        public static class SlotsBean {
            private String attrValue;
            private String attrType;
            private String attr;
            private String modifier;
            private AttrValueBean attrValueBean;
            private String artist;
            private String song;

            public String getArtist() {
                return artist;
            }

            public void setArtist(String artist) {
                this.artist = artist;
            }

            public String getSong() {
                return song;
            }

            public void setSong(String song) {
                this.song = song;
            }

            public String getModifier() {
                return modifier;
            }

            public void setModifier(String modifier) {
                this.modifier = modifier;
            }

            public String getAttrValue() {
                return attrValue;
            }

            public void setAttrValue(String attrValue) {
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

            public AttrValueBean getAttrValueBean() {
                return attrValueBean;
            }

            public void setAttrValueBean(AttrValueBean attrValue) {
                this.attrValueBean = attrValue;
            }

            public static class AttrValueBean {
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
        }
    }

    public static class AnswerBean {
        private String text;
        private String type;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
