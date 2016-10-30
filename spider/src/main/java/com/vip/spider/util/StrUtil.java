package com.vip.spider.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihuajun on 16-6-28.
 */
public class StrUtil {

    public static String cleanUrl(String url) {
        if (StringUtils.isEmpty(url))
            return null;
        return url.replaceAll("\r|\n|\t| ", "");
    }

    public static String encode(String str) {

        return str.replaceAll("<", "%3C").replaceAll(">", "%3E")
                .replaceAll("/", "%2F").replaceAll(";", "%3B")
                .replaceAll("\\(", "%28").replaceAll("\\)", "%29")
                .replaceAll(",", "%2C").replaceAll("\"", "%22")
                .replaceAll(" ", "%20")
                ;
    }

    /**
     * 将页面上不完整的链接拼接成完整的链接
     *
     * @param pLink
     * @param linkList
     * @return
     */
    public static List<String> handleLink(String pLink, List<String> linkList) {
        if (linkList == null)
            return null;

        List<String> list = new ArrayList<>();
        for (String link : linkList) {
            list.add(handleLink(pLink, link));
        }
        return list;
    }

    /**
     * 将页面上不完整的链接拼接成完整的链接
     *
     * @param pLink
     * @param link
     * @return
     */
    public static String handleLink(String pLink, String link) {
        String childLink = null;
        String protocol = pLink.indexOf("http://") == 0 ? "http" : pLink.indexOf("https://") == 0 ? "https" : null;
        if (link.contains("http://") || link.contains("https://"))
            childLink = link;
        else if (link.contains("../")) {
            String[] strs = link.replaceFirst(protocol + "://", "").split("\\.\\.\\/");
            String[] pStrs = pLink.replaceFirst(protocol + "://", "").split("\\/");
            StringBuffer sb = new StringBuffer(protocol);
            sb.append("://");
            for (int i = 0; i < pStrs.length - strs.length + 1; i++) {
                sb.append(pStrs[i]).append("/");
            }
            sb.append(strs[strs.length - 1]);
            childLink = sb.toString();
        } else if (link.indexOf("/") == 0) {
            String str = pLink.replaceFirst(protocol + "://", "");
            if ("http".equals(protocol)) {
                childLink = protocol + "://" + str.substring(0, str.indexOf("/")) + link;
            } else if ("https".equals(protocol)) {
                childLink = protocol + "://" + str.substring(0, str.indexOf("/")) + link;
            }
        } else {
            String str = pLink.replace(protocol + "://", "");
            int pos = str.lastIndexOf("/");
            if (pos != -1) {
                childLink = protocol + "://" + str.substring(0, pos + 1) + link;
            } else {
                childLink = protocol + "://" + str + "/" + link;
            }
        }
        return childLink;
    }

    public static void showDif(String str1, String str2, String split) throws UnsupportedEncodingException {

        String[] strsSrc1 = str1.split(split);
        String[] strsSrc2 = str2.split(split);
        String[] strs1 = null;
        String[] strs2 = null;
        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();

        if (strsSrc1.length != strsSrc2.length) {
            System.out.println("参数个数不同");
        }

        if (strsSrc1.length >= strsSrc2.length) {
            strs1 = strsSrc1;
            strs2 = strsSrc2;
        } else {
            strs1 = strsSrc2;
            strs2 = strsSrc1;
        }

        for (int i = 0; i < strs1.length; i++) {
            String[] temp = strs1[i].split("=");
            if (temp.length == 1) {
                map1.put(temp[0].trim(), "");
            } else
                map1.put(temp[0].trim(), temp[1].trim());
        }

        for (int i = 0; i < strs2.length; i++) {
            String[] temp = strs2[i].split("=");
            if (temp.length == 1) {
                map2.put(temp[0].trim(), "");
            } else
                map2.put(temp[0].trim(), temp[1].trim());
        }

        for (String key : map1.keySet()) {
            if (map2.get(key) == null) {
                System.out.println("集合2没有[" + key + "]属性");
            }
        }

        for (String key : map2.keySet()) {
            if (map1.get(key) == null) {
                System.out.println("集合1没有[" + key + "]属性");
            }
        }

        List<String> keys = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer diff = new StringBuffer();

        for (String key : map1.keySet()) {
            stringBuffer.append(key).append(",");
            if (map2.get(key) == null) {
                continue;
            }
            if (!map1.get(key).equals(map2.get(key))) {
                System.out.println("map1中[" + key + "]值为：" + URLDecoder.decode(map1.get(key), "utf-8"));
                System.out.println("map2中[" + key + "]值为：" + URLDecoder.decode(map2.get(key), "utf-8"));
                System.out.println("------------------------------------");
                diff.append(key).append(",");
            } else {
                keys.add(key);
            }
        }

        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        Boolean isFirst = true;
        for (String key : keys) {
            if (isFirst) {
                sb.append(key).append("=").append(map1.get(key));
                sb1.append(key);
                isFirst = false;
                continue;
            }
            sb.append(split).append(key).append("=").append(map1.get(key));
            sb1.append(",").append(key);
        }
        System.out.println(sb.toString());
        System.out.println("相同key：" + sb1.toString());
        System.out.println("不同key：" + diff.toString());
        System.out.println("所有key：" + stringBuffer.toString());
    }

    //设置表单
    private static void setForm(String str, String decode) throws UnsupportedEncodingException {
        String[] params = str.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] kv = params[i].split("=");
            if (kv.length == 1) {
                System.out.println("params.add(new BasicNameValuePair(\"" + kv[0] + "\", \"\"));");
            } else {
                System.out.println("params.add(new BasicNameValuePair(\"" + kv[0] + "\", \"" + URLDecoder.decode(kv[1], decode) + "\"));");
            }
        }
    }

    public static void main(String[] args) {

        //评论
        String str1 = "d_c0=AHAA2J2WngqPTg29F5RB7iMYNGFmFNxNgVg=|1475221375; _za=30e82170-dc0a-434a-9d6d-a2165077f167; _zap=ddab3390-9acf-4493-91f5-2a806ffd605c; _xsrf=52c5a1f783fb75e458f254e918d488ea; q_c1=1abe3fda341a43dd9292ee116dcd9561|1477384487000|1477384487000; l_cap_id=MDgwYmJiOWI0ODQxNDdiODk3NmNjM2U3ZGRhZWFlY2I=|1477384487|5736949b8a5bb1fb0d467b082ef19889f14b7d12; cap_id=ZWVmZTIyZjU3M2NmNDQwMGIzMDBlY2I5YzcwZjY0ZDM=|1477384487|897e2d6d3b11ce7b96b96122b1d850456f5895e9; a_t=2.0AOCAgY8N3gcXAAAAKHw9WADggIGPDd4HAHAA2J2WngoXAAAAYQJVTV2nNlgAKaQdqU3Ruz77NC-AymDOnlLt_gbkeTUOCN2ZZ-hfnLsUVSeUbR9PPQ==; z_c0=Mi4wQU9DQWdZOE4zZ2NBY0FEWW5aYWVDaGNBQUFCaEFsVk5YYWMyV0FBcHBCMnBUZEc3UHZzMEw0REtZTTZlVXUzLUJn|1477832488|16be806acb16f6badf91a09c382ce6531b01aef4; __utma=51854390.1355232947.1477649019.1477820877.1477831686.5; __utmb=51854390.11.9.1477832488123; __utmc=51854390; __utmz=51854390.1477820877.4.3.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/li-hua-jun-43; __utmv=51854390.100--|2=registration_date=20150402=1^3=entry_date=20150402=1";
        String str2 = "d_c0=AHAA2J2WngqPTg29F5RB7iMYNGFmFNxNgVg=|1475221375; _za=30e82170-dc0a-434a-9d6d-a2165077f167; _zap=ddab3390-9acf-4493-91f5-2a806ffd605c; _xsrf=52c5a1f783fb75e458f254e918d488ea; q_c1=1abe3fda341a43dd9292ee116dcd9561|1477384487000|1477384487000; l_cap_id=MDgwYmJiOWI0ODQxNDdiODk3NmNjM2U3ZGRhZWFlY2I=|1477384487|5736949b8a5bb1fb0d467b082ef19889f14b7d12; cap_id=ZWVmZTIyZjU3M2NmNDQwMGIzMDBlY2I5YzcwZjY0ZDM=|1477384487|897e2d6d3b11ce7b96b96122b1d850456f5895e9; a_t=2.0AOCAgY8N3gcXAAAAvX09WADggIGPDd4HAHAA2J2WngoXAAAAYQJVTV2nNlgAKaQdqU3Ruz77NC-AymDOnlLt_gZAYMm3iRWDinmxHMVT6UKDm3NcTA==; z_c0=Mi4wQU9DQWdZOE4zZ2NBY0FEWW5aYWVDaGNBQUFCaEFsVk5YYWMyV0FBcHBCMnBUZEc3UHZzMEw0REtZTTZlVXUzLUJn|1477832893|9f5f5e395a15af6d3972ff668911648611cab5a0; __utma=51854390.1355232947.1477649019.1477820877.1477831686.5; __utmb=51854390.12.9.1477832893314; __utmc=51854390; __utmz=51854390.1477820877.4.3.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/li-hua-jun-43; __utmv=51854390.100--|2=registration_date=20150402=1^3=entry_date=20150402=1";

        try {
            showDif(str1, str2, ";");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设置表单
        /*String str = "targetid=1500957283&type=1&format=SCRIPT&callback=parent.popCallback&content=%D4%DE%B8%F6&_method=put&g_tk=1203714245&code=1&source=1&subsource=0&picture=";
        try {
            setForm(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

}
