package com.highersun.sign.bean;

import java.util.List;

/**
 * Author: ShenDanLai on 2016/9/8.
 * Email: 17721129316@163.com
 */

public class MonthDataBean extends BaseDataBean {

    private List<List<Data>> data;

    public List<List<Data>>  getData() {
        return data;
    }

    public void setData(List<List<Data>>  data) {
        this.data = data;
    }

    public class Data {
        /**
         * status : 0
         * currentTime : 2016-05-01
         * signInTime : null
         * signOutTime : null
         */
        private int status;
        private String currentTime;
        private String signInTime;
        private String signOutTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getSignInTime() {
            return signInTime;
        }

        public void setSignInTime(String signInTime) {
            this.signInTime = signInTime;
        }

        public String getSignOutTime() {
            return signOutTime;
        }

        public void setSignOutTime(String signOutTime) {
            this.signOutTime = signOutTime;
        }
    }
}
