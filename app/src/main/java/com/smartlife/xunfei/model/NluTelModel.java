package com.smartlife.xunfei.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class NluTelModel {

    /**
     * dialog_stat : dataInvalid
     * rc : 0
     * semantic : [{"intent":"DIAL","slots":[{"name":"name","value":"张三"}]}]
     * service : telephone
     * uuid : atn00290b13@un39d00cae71f96f2601
     * text : 呼叫张三
     * used_state : {"name":"1","operation":"1","state":"default","state_key":"fg::telephone::default::default"}
     * answer : {"text":"没有为您找到张三.请确认要拨打的联系人"}
     * sid : atn00290b13@un39d00cae71f96f2601
     */

    private String dialog_stat;
    private int rc;
    private String service;
    private String uuid;
    private String text;
    private UsedStateBean used_state;
    private AnswerBean answer;
    private String sid;
    private List<SemanticBean> semantic;

    public String getDialog_stat() {
        return dialog_stat;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<SemanticBean> getSemantic() {
        return semantic;
    }

    public void setSemantic(List<SemanticBean> semantic) {
        this.semantic = semantic;
    }

    public static class UsedStateBean {
        /**
         * name : 1
         * operation : 1
         * state : default
         * state_key : fg::telephone::default::default
         */

        private String name;
        private String operation;
        private String state;
        private String state_key;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState_key() {
            return state_key;
        }

        public void setState_key(String state_key) {
            this.state_key = state_key;
        }
    }

    public static class AnswerBean {
        /**
         * text : 没有为您找到张三.请确认要拨打的联系人
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class SemanticBean {
        /**
         * intent : DIAL
         * slots : [{"name":"name","value":"张三"}]
         */

        private String intent;
        private List<SlotsBean> slots;

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public List<SlotsBean> getSlots() {
            return slots;
        }

        public void setSlots(List<SlotsBean> slots) {
            this.slots = slots;
        }

        public static class SlotsBean {
            /**
             * name : name
             * value : 张三
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
