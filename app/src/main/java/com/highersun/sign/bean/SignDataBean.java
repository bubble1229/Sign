package com.highersun.sign.bean;

/**
 * Created by admin on 2016/8/16.
 */

public class SignDataBean extends BaseDataBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        Sign signIn;
        Sign signOut;

        public Sign getSignIn() {
            return signIn;
        }

        public void setSignIn(Sign signIn) {
            this.signIn = signIn;
        }

        public Sign getSignOut() {
            return signOut;
        }

        public void setSignOut(Sign signOut) {
            this.signOut = signOut;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "signIn=" + signIn +
                    ", signOut=" + signOut +
                    '}';
        }
    }

    public class Sign {

        /**
         * type : 0
         * ibeaconId : 1
         * addTime : 2016-08-01 10:12:11
         * signTime : 2016-08-01 10:11:57
         * recordId : 4
         * userId : 1
         * attendanceId : 1
         * signEquipment : 1
         */

        private int type;
        private String ibeaconId;
        private String addTime;
        private String signTime;
        private String recordId;
        private String userId;
        private String attendanceId;
        private String signEquipment;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIbeaconId() {
            return ibeaconId;
        }

        public void setIbeaconId(String ibeaconId) {
            this.ibeaconId = ibeaconId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getSignTime() {
            return signTime;
        }

        public void setSignTime(String signTime) {
            this.signTime = signTime;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAttendanceId() {
            return attendanceId;
        }

        public void setAttendanceId(String attendanceId) {
            this.attendanceId = attendanceId;
        }

        public String getSignEquipment() {
            return signEquipment;
        }

        public void setSignEquipment(String signEquipment) {
            this.signEquipment = signEquipment;
        }

        @Override
        public String toString() {
            return "Sign{" +
                    "type=" + type +
                    ", ibeaconId='" + ibeaconId + '\'' +
                    ", addTime='" + addTime + '\'' +
                    ", signTime='" + signTime + '\'' +
                    ", recordId='" + recordId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", attendanceId='" + attendanceId + '\'' +
                    ", signEquipment='" + signEquipment + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SignDataBean{" +
                "data=" + data +
                '}';
    }
}
