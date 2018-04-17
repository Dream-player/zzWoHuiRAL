package zz.wohui.wohui365.Bean;

import java.util.List;

/**
 * 说明：二级到三级商品类别列表的Bean
 * 作者：陈杰宇
 * 时间： 2016/2/28 14:38
 * 版本：V1.0
 * 修改历史：
 */
public class CategoryListBean {

    /**
     * BCategory : {"CCategory":[{"name":"银鸽"},{"name":"清风"},{"name":"清风"},{"name":"清风"},{"name":"清风"},{"name":"心相印"}],"name":"卫生纸"}
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * CCategory : [{"name":"银鸽"},{"name":"清风"},{"name":"清风"},{"name":"清风"},{"name":"清风"},{"name":"心相印"}]
         * name : 卫生纸
         */

        private BCategoryEntity BCategory;

        public void setBCategory(BCategoryEntity BCategory) {
            this.BCategory = BCategory;
        }

        public BCategoryEntity getBCategory() {
            return BCategory;
        }

        public static class BCategoryEntity {
            private String name;
            /**
             * name : 银鸽
             */

            private List<CCategoryEntity> CCategory;

            public void setName(String name) {
                this.name = name;
            }

            public void setCCategory(List<CCategoryEntity> CCategory) {
                this.CCategory = CCategory;
            }

            public String getName() {
                return name;
            }

            public List<CCategoryEntity> getCCategory() {
                return CCategory;
            }

            public static class CCategoryEntity {
                private String name;

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }
}
