package com.highersun.sign.bean;

import java.util.List;

/**
 * Author: ShenDanLai on 2016/9/14.
 * Email: 17721129316@163.com
 */

public class IbeaconListBean extends BaseDataBean{
    private IbeaconData data;

    public IbeaconData getData() {
        return data;
    }

    public void setData(IbeaconData data) {
        this.data = data;
    }

    public class IbeaconData{
          List<Ibeacon> list;
          Pagination pagination;

          public List<Ibeacon> getList() {
              return list;
          }

          public void setList(List<Ibeacon> list) {
              this.list = list;
          }

          public Pagination getPagination() {
              return pagination;
          }

          public void setPagination(Pagination pagination) {
              this.pagination = pagination;
          }
      }
    public class Ibeacon{

        /**
         * ibeaconId : fda50693-a4e2-4fb1-afcf-c6eb07647825
         * name : 海洱森科技
         * id : 1
         */

        private String ibeaconId;
        private String name;
        private String id;

        public String getIbeaconId() {
            return ibeaconId;
        }

        public void setIbeaconId(String ibeaconId) {
            this.ibeaconId = ibeaconId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public class Pagination {

        /**
         * pageSize : 10
         * pageNo : 1
         * totalCount : 1
         * start : 0
         */

        private int pageSize;
        private int pageNo;
        private int totalCount;
        private int start;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }
    }
}
