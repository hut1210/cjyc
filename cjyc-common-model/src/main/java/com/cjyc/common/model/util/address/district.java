package com.cjyc.common.model.util.address;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class district {
    /**
     * 省级后缀
     */
    private static String[] suffixs = {"省","市","自治区","特别行政区"};
    /**
     * 中国34个省，直辖市，自治区，特别行政区
     */
    @SuppressWarnings("serial")
    private static Map<Integer,String[]> maps = new HashMap<Integer,String[]>(){{
        /**
         * 中国的23个省
         */
        put(1, new String[]{"河北","山西","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","海南","四川","贵州","云南","陕西","甘肃","青海"});
        /**
         * 中国的4个直辖市
         */
        put(2, new String[]{"北京","天津","上海","重庆"});
        /**
         * 中国的5个自治区
         */
        put(3, new String[]{"广西","内蒙古","西藏","宁夏","新疆"});
        /**
         * 中国的2个特别行政区
         */
        put(4, new String[]{"香港","澳门"});
        /**
         * 中国的市
         */
        put(5, new String[]{"北京",
                "天津",
                "石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水",
                "太原","大同","阳泉","长治","晋城","朔州","晋中","运城","忻州","临汾","吕梁",
                "呼和浩特","包头","乌海","赤峰","通辽","鄂尔多斯","呼伦贝尔","巴彦淖尔","乌兰察布","兴安盟","锡林郭勒盟","阿拉善盟",
                "沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新","辽阳","盘锦","铁岭","朝阳","葫芦岛",
                "长春","吉林","四平","辽源","通化","白山","松原","白城","延边",
                "哈尔滨","齐齐哈尔","鸡西","鹤岗","双鸭山","大庆","伊春","佳木斯","七台河","牡丹江","黑河","绥化","大兴安岭",
                "上海",
                "南京","无锡","徐州","常州","苏州","南通","连云港","淮安","盐城","扬州","镇江","泰州","宿迁",
                "杭州","宁波","温州","嘉兴","湖州","绍兴","金华","衢州","舟山","台州","丽水",
                "合肥","芜湖","蚌埠","淮南","马鞍山","淮北","铜陵","安庆","黄山","滁州","阜阳","宿州","六安","亳州","池州","宣城",
                "福州","厦门","莆田","三明","泉州","漳州","南平","龙岩","宁德",
                "南昌","景德镇","萍乡","九江","新余","鹰潭","赣州","吉安","宜春","抚州","上饶",
                "济南","青岛","淄博","枣庄","东营","烟台","潍坊","济宁","泰安","威海","日照","临沂","德州","聊城","滨州","菏泽",
                "郑州","开封","洛阳","平顶山","安阳","鹤壁","新乡","焦作","濮阳","许昌","漯河","三门峡","南阳","商丘","信阳","周口","驻马店",
                "武汉","黄石","十堰","宜昌","襄阳","鄂州","荆门","孝感","荆州","黄冈","咸宁","随州","恩施",
                "长沙","株洲","湘潭","衡阳","邵阳","岳阳","常德","张家界","益阳","郴州","永州","怀化","娄底","湘西",
                "广州","韶关","深圳","珠海","汕头","佛山","江门","湛江","茂名","肇庆","惠州","梅州","汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮",
                "南宁","柳州","桂林","梧州","北海","防城港","钦州","贵港","玉林","百色","贺州","河池","来宾","崇左",
                "海口","三亚","三沙","儋州",
                "重庆",
                "成都","自贡","攀枝花","泸州","德阳","绵阳","广元","遂宁","内江","乐山","南充","眉山","宜宾","广安","达州","雅安","巴中","资阳","阿坝","甘孜","凉山",
                "贵阳","六盘水","遵义","安顺","毕节","铜仁","黔西南","黔东南","黔南",
                "昆明","曲靖","玉溪","保山","昭通","丽江","普洱","临沧","楚雄","红河","文山","西双版纳","大理","德宏","怒江","迪庆",
                "拉萨","日喀则","昌都","林芝","山南","那曲","阿里",
                "西安","铜川","宝鸡","咸阳","渭南","延安","汉中","榆林","安康","商洛",
                "兰州","嘉峪关","金昌","白银","天水","武威","张掖","平凉","酒泉","庆阳","定西","陇南","临夏","甘南",
                "西宁","海东","海北","黄南","海南","果洛","玉树","海西",
                "银川","石嘴山","吴忠","固原","中卫",
                "乌鲁木齐","克拉玛依","吐鲁番","哈密","昌吉","博尔塔拉","巴音郭楞","阿克苏","克孜勒苏柯尔克孜","喀什","和田","伊犁哈萨克","塔城","阿勒泰"});
    }};
    /**
     * 拼接“省”，“市”后缀
     * @param address
     * @param $suffix
     * @param s
     * @return
     */
    private static String restructure(String address,String $suffix,String s){
        if(address.indexOf($suffix) == 0){
            //拼接“省”，“市”关键字
            return address;
        }else if(address.indexOf(s) == 0){
            //拼接“省”，“市”关键字
            return address.replaceFirst(s, $suffix);
        }
        return null;
    }


    /**
     * 判断是否包含省级地区
     * @param address
     * @return -1（不包含省份 ）， 1（包含23个省份中的一个），2（包含直辖市），3（包含自治区），4（包含特别行政区），5（包含市级）
     */
    public static Integer isExistProvince(String address){
        Integer isExit = -1;
        for(Map.Entry<Integer, String[]> entry : maps.entrySet()){
            //过滤掉市级地区
            if(entry.getKey() == 5) {
                break;
            }
            //判断是否存在中国34个省级地址（23个普通省份，4个直辖市，5个自治区，2个特别行政区）
            for(String provin : entry.getValue()){
                if(address.indexOf(provin) == 0){
                    return entry.getKey();
                }
            }
        }
        return isExit;
    }


    /**
     * 处理没有指定省市后缀的地址
     * 检查出来之后自动拼接
     * 例如：四川成都高新区
     * 结果：四川省成都市高新区
     * @param suffix
     * @param address
     * @param array
     * @return
     */
    private static String restructure(String address,int k){
        //省级
        String provinces = "";
        //省
        String[] arrays = maps.get(k);
        //后缀
        String suffix = suffixs[k-1];
        //把全部的"市辖区","市辖县"替换成""
        for(String s : cityKeyWords.get(1)){
            address = address.replace(s, "");
        }
        for(String s : arrays){
            //自治区
            String $suffix = "";
            if(k == 3){
                switch (s) {
                    case "广西":
                        $suffix = s + "壮族" + suffix;
                        break;
                    case "宁夏":
                        $suffix = s + "回族" + suffix;
                        break;
                    case "新疆":
                        $suffix = s + "维吾尔" + suffix;
                        break;
                    default:
                        $suffix = s + suffix;
                        break;
                }
            }
            else{
                $suffix = s + suffix;
            }

            provinces = $suffix;

            //拼接“省”，“市”后缀
            String $address = restructure(address, $suffix, s);
            if($address != null){
                address = $address;
                break;
            }
        }
        return restructure(address,provinces);
    }

    /**
     * 出现下列关键词的将不作处理
     */
    @SuppressWarnings({"serial" })
    private static Map<Integer,String[]> cityKeyWords = new LinkedHashMap<Integer,String[]>(){{
        put(0,new String[]{"县"});
        put(1,new String[]{"市辖区","市辖县"});
        put(2,new String[]{"盟","州","地区","自治州","回族自治州","土家族苗族自治州","藏族自治州","藏族羌族自治州","蒙古族藏族自治州","壮族苗族自治州","傣族自治州","彝族自治州","朝鲜族自治州","布依族苗族自治州","苗族侗族自治州","傣族景颇族自治州","傈僳族自治州","白族自治州","哈尼族彝族自治州"});
    }};

    /**
     * 处理没有填写市级地区的地址，一旦检查出来，则添加上后缀
     * 例如：成都高新区
     * 结果：成都市高新区
     * @param address
     * @return
     */
    private static String restructure(String address,String provinces){
        address = address.replace(provinces, "");
        //如果第一位是"县"，替换为""
        if(cityKeyWords.get(0)[0].equals(address.substring(0, 1))){
            address = address.substring(1);
        }
        //如果省份是直辖市，自动插入"市辖区"用于区分
        for(String s : maps.get(2)){
            if(provinces.equals(s + suffixs[1])){
                address = cityKeyWords.get(1)[0] + address;
            }
        }
        //后缀
        String suffix = suffixs[1];
        big:for(String s : maps.get(5)){
            for(String key : cityKeyWords.get(2)){
                if(address.indexOf(s + key) == 0){
                    continue big;
                }
            }
            //拼接“省”，“市”后缀
            String $address = restructure(address, s + suffix, s);
            if($address != null){
                address = $address;
                break;
            }
        }
        return provinces + address;
    }
    /**
     * 解析地址
     * @param address
     * @return
     */
    public static Map<String,String> addressResolution(String address){
        String regex="(?<province>[^特别行政区]+特别行政区|[^自治区]+自治区|[^省]+省|[^市]+市)(?<city>省直辖行政单位|省属虚拟市|市辖县|市辖区|县|自治州|[^地区]+地区|[^州]+州|[^盟]+盟|[^市]+市|[^区]+区|)?(?<county>[^旗]+旗|[^市]+市|[^区]+区|[^县]+县)?(?<town>[^县]+县|[^区]+区|[^乡]+乡|[^村]+村|[^镇]+镇|[^街道]+街道)?(?<village>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(address);
        Map<String,String> rmap = null;
        while(matcher.find()){
            rmap = new LinkedHashMap<String,String>();
            rmap.put("province", matcher.group("province") == null ? "" : matcher.group("province").trim());
            rmap.put("city", matcher.group("city") == null ? "" : matcher.group("city").trim());
            rmap.put("county", matcher.group("county") == null ? "" : matcher.group("county").trim());
            rmap.put("town", matcher.group("town") == null ? "" : matcher.group("town").trim());
            rmap.put("village", matcher.group("village") == null ? "" : matcher.group("village").trim());
        }
        //重构一次地址
        return addressResolution(rmap);
    }
    /**
     * 重构一次地址，将直辖市所在区域进行特殊处理
     * 注：如果在地址中出现未明确省市区的将无法去重，由于详细地址中可能出现于省市同名的情况，所有对于这类情况将
     * 	保留，即使从肉眼能看出是重复的，也不会处理
     * 例如：四川省成都市高新区四川成都高新xxxx大道xxx号
     * @param rmap
     * @return
     */
    private static Map<String,String> addressResolution(Map<String,String> rmap){
        if(rmap == null) return rmap;
        //针对直辖市，进行特殊处理
        String city = rmap.get("city");
        //将直辖市-市级全部替换为区级内容，并将区级内容全部替换为""
        for(String s : cityKeyWords.get(1)){
            if(s.equals(city)){
                rmap.put("city", rmap.get("county"));
                rmap.put("county", "");
                break;
            }
        }
        //市
        city = rmap.get("city");
        //区（县）
        String county = rmap.get("county");
        //省
        String province = rmap.get("province");
        //街道，乡村，镇
        rmap.put("town", rmap.get("town").replace(city, "").replace(county, "").replace(province, ""));
        //详细地址
        rmap.put("village", rmap.get("village").replace(city, "").replace(county, "").replace(province, ""));

        return rmap;
    }


    /**
     * 格式化省市县/区信息
     * @param address
     * @return
     */
    public static Map<String, String> addressFormat(String address) {
        if(address == null) return null;
        address = address.replaceAll("\\s+","");
        //判断是否存在省级地区
        int k = isExistProvince(address);
        if(k == -1){
            return null;
        }else{
            //重构地区格式（拼接省级，市级地区后缀名）
            address = restructure(address, k);
        }
        //格式化地址
        Map<String,String> addresss = addressResolution(address);
        if(addresss == null){
            return null;
        }
        //省份
        String province = addresss.get("province");
        //市
        String city = addresss.get("city");
        //区县
        String county = addresss.get("county");

        //详细地址
        String town = addresss.get("town");
        String village = addresss.get("village");

        //完整地址 ： 省 + 市 + 区 + 详细地址
        StringBuilder detailAddress = new StringBuilder();
        detailAddress.append(province).append(" ").append(city).append(" ").append(county).append(" ").append(town).append(village);

        Map<String,String> rmap = new LinkedHashMap<String,String>();
        rmap.put("province",province);
        rmap.put("city",city);
        rmap.put("county",county);
        rmap.put("town",town + village);
        rmap.put("detail", detailAddress.toString().replaceAll("\\s+",""));
        rmap.put("detail_format", detailAddress.toString());

        return rmap;
    }
}
