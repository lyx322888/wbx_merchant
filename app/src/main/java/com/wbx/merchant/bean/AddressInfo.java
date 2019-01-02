package com.wbx.merchant.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by wushenghui on 2017/6/23.
 */

public class AddressInfo  implements IPickerViewData {

    /**
     * city_id : 386
     * name : 重庆
     * first_letter : C
     * area : [{"area_id":230,"area_name":" 江北","business":[{"business_id":226,"business_name":"江北商圈"}]},{"area_id":231,"area_name":" 万州","business":[{"business_id":227,"business_name":"万州商圈"}]},{"area_id":232,"area_name":" 渝中","business":[{"business_id":228,"business_name":"渝中商圈"}]},{"area_id":233,"area_name":" 九龙坡","business":[{"business_id":229,"business_name":"九龙坡商圈"}]},{"area_id":234,"area_name":"涪陵","business":[{"business_id":230,"business_name":"涪陵商圈"}]},{"area_id":235,"area_name":"长寿","business":[{"business_id":231,"business_name":"长寿商圈"}]},{"area_id":236,"area_name":" 沙坪坝","business":[{"business_id":232,"business_name":"沙坪坝商圈"}]},{"area_id":237,"area_name":" 合川","business":[{"business_id":233,"business_name":"合川商圈"}]},{"area_id":238,"area_name":" 南岸","business":[{"business_id":234,"business_name":"南岸商圈"}]},{"area_id":239,"area_name":" 渝北","business":[{"business_id":235,"business_name":"渝北商圈"}]},{"area_id":240,"area_name":"巴南","business":[{"business_id":236,"business_name":"巴南商圈"}]},{"area_id":241,"area_name":" 北碚","business":[{"business_id":237,"business_name":"北碚商圈"}]},{"area_id":242,"area_name":" 大渡口","business":[{"business_id":238,"business_name":"大渡口商圈"}]},{"area_id":243,"area_name":" 永川","business":[{"business_id":239,"business_name":"永川商圈"}]},{"area_id":244,"area_name":" 两江新区","business":[{"business_id":240,"business_name":"两江新区商圈"}]},{"area_id":245,"area_name":" 璧山","business":[{"business_id":241,"business_name":"璧山商圈"}]},{"area_id":246,"area_name":" 重庆周边","business":[{"business_id":242,"business_name":"重庆周边商圈"}]},{"area_id":247,"area_name":" 石柱","business":[{"business_id":243,"business_name":"石柱商圈"}]}]
     */

    private int city_id;
    private String name;
    private String first_letter;
    private List<AreaBean> area;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public List<AreaBean> getArea() {
        return area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class AreaBean implements IPickerViewData {
        /**
         * area_id : 230
         * area_name :  江北
         * business : [{"business_id":226,"business_name":"江北商圈"}]
         */

        private int area_id;
        private String area_name;
        private List<BusinessBean> business;

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public List<BusinessBean> getBusiness() {
            return business;
        }

        public void setBusiness(List<BusinessBean> business) {
            this.business = business;
        }

        @Override
        public String getPickerViewText() {
            return area_name;
        }

        public static class BusinessBean implements IPickerViewData {
            /**
             * business_id : 226
             * business_name : 江北商圈
             */

            private int business_id;
            private String business_name;

            public int getBusiness_id() {
                return business_id;
            }

            public void setBusiness_id(int business_id) {
                this.business_id = business_id;
            }

            public String getBusiness_name() {
                return business_name;
            }

            public void setBusiness_name(String business_name) {
                this.business_name = business_name;
            }

            @Override
            public String getPickerViewText() {
                return business_name;
            }
        }
    }
}
