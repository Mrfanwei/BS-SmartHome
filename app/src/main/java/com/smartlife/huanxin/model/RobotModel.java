package com.smartlife.huanxin.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class RobotModel {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : zz
         * online : true
         * robot_serial : fffz
         * controller : 1
         * id : f
         * rname : zzz
         * rid : 1
         */

        private String address;
        private boolean online;
        private String robot_serial;
        private int controller;
        private String id;
        private String rname;
        private int rid;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public String getRobot_serial() {
            return robot_serial;
        }

        public void setRobot_serial(String robot_serial) {
            this.robot_serial = robot_serial;
        }

        public int getController() {
            return controller;
        }

        public void setController(int controller) {
            this.controller = controller;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }
    }
}
