package com.dingtalk.util;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Note：
 *
 * @author ：mengjw
 * @description：钉钉群发送消息工具类
 * @date ：Created in 2021-04-27
 */
@Slf4j
public class MsgSender {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String TEXT_MSG = "text";
    private static final String MARKDOWN_MSG = "markdown";
    private static final String ACTION_CARD_MSG = "actionCard";

    /**
     * 发送文本给钉钉群机器人
     *
     * @param title       消息的标题
     * @param msg         消息
     * @param atUserPhone 需要@的人的手机
     * @param webhook     接收消息的接口地址
     * @param robotSecret 机器人验签码
     * @return 响应true，失败false
     */
    @SneakyThrows
    public static void sendTextToRobot(String title, List<String> msg, List<String> atUserPhone,
                                       String webhook, String robotSecret) {
        sendToRobot(title, msg, atUserPhone, TEXT_MSG, webhook, robotSecret);
    }

    /**
     * 发送Markdown给钉钉群机器人(样式较好，但是是伪@，不是真实的@)
     *
     * @param title       消息的标题
     * @param msg         消息
     * @param atUserPhone 需要@的人的手机
     * @param webhook     接收消息的接口地址
     * @param robotSecret 机器人验签码
     * @return 响应true，失败false
     */
    @SneakyThrows
    public static void sendMarkdownToRobot(String title, List<String> msg, List<String> atUserPhone,
                                           String webhook, String robotSecret) {
        sendToRobot(title, msg, atUserPhone, MARKDOWN_MSG, webhook, robotSecret);
    }

    /**
     * 发送消息给钉钉群机器人
     *
     * @param title       消息的标题
     * @param msg         消息
     * @param atUserPhone 需要@的人的手机
     * @param msgType     消息类型
     * @param webhook     接收消息的接口地址
     * @param robotSecret 机器人验签码
     * @return 响应true，失败false
     */
    @SneakyThrows
    private static void sendToRobot(String title, List<String> msg, List<String> atUserPhone, String msgType,
                                   String webhook, String robotSecret) {
        if (msg.size() == 0) {
            return;
        }
        // @人的名单会有重复，所以要去重
        atUserPhone = atUserPhone.stream().distinct().collect(Collectors.toList());
        // 获取请求
        OapiRobotSendRequest request;
        if (MARKDOWN_MSG.equals(msgType)) {
            request = getMarkdownRequest(msg, atUserPhone, title);
        } else {
            request = getTextRequest(msg, title);
        }
        // 设置@人
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(atUserPhone);
        request.setAt(at);
        // 获取完整url
        long timestamp = System.currentTimeMillis();
        String sign = sign(robotSecret, timestamp);
        String webhookWithSigned = webhook + "&timestamp=" + timestamp + "&sign=" + sign;
        DingTalkClient client = new DefaultDingTalkClient(webhookWithSigned);
        // 执行
        OapiRobotSendResponse response = client.execute(request);
        log.info("响应状态码：{}，消息：{}", response.getErrorCode(), response.getErrmsg());
    }

    /**
     * 获取Markdown类型的请求
     *
     * @param msg
     * @param atUserPhone
     * @param title
     * @return
     */
    private static OapiRobotSendRequest getMarkdownRequest(List<String> msg, List<String> atUserPhone, String title) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(MARKDOWN_MSG);
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        StringBuilder textContent = new StringBuilder();
        textContent.append("### **").append(title).append(":**\n");
        for (int i = 1; i <= msg.size(); i++) {
            textContent.append(font(msg.get(i - 1).replaceFirst("-\\W+", ""))).append("\n");
        }
        if (atUserPhone.size() > 0) {
            textContent.append("### ");
            for (String s : atUserPhone) {
                textContent.append("@").append(s);
            }
        }
        markdown.setText(textContent.toString());
        request.setMarkdown(markdown);
        log.info("发送内容：" + textContent.toString());
        return request;
    }

    /**
     * 获取Text类型的请求
     *
     * @param msg
     * @param title
     * @return
     */
    private static OapiRobotSendRequest getTextRequest(List<String> msg, String title) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(TEXT_MSG);
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        StringBuilder textContent = new StringBuilder();
        textContent.append(title).append("：\n");
        for (String s : msg) {
            textContent.append(s).append("\n");
        }
        text.setContent(textContent.toString());
        request.setText(text);
        log.info("发送内容：" + textContent.toString());
        return request;
    }

    /**
     * 发送事件给钉钉群机器人
     *
     * @param title   消息的标题
     * @param msg     消息
     * @param url     消息的连接
     * @param webhook 接收消息的接口地址
     * @return 响应true，失败false
     */
    @SneakyThrows
    public static void sendActionCardToRobot(String title, List<String> msg, String url, String webhook) {
        if (msg.size() == 0) {
            return;
        }
        DingTalkClient client = new DefaultDingTalkClient(webhook);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(ACTION_CARD_MSG);
        OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
        actionCard.setTitle(title);
        StringBuilder text = new StringBuilder();
        for (String s : msg) {
            text.append("#### ").append(s).append("\n");
        }
        actionCard.setText(text.toString());
        actionCard.setSingleURL(url);
        request.setActionCard(actionCard);
        log.info("发送内容：" + text.toString());
        OapiRobotSendResponse response = client.execute(request);
        log.info("响应状态码：{}，消息：{}", response.getErrorCode(), response.getErrmsg());
    }

    /**
     * 加签
     *
     * @param secret    秘钥
     * @param timestamp 时间戳
     * @return
     */
    @SneakyThrows
    private static String sign(String secret, Long timestamp) {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(DEFAULT_CHARSET), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(DEFAULT_CHARSET));
        return URLEncoder.encode(Base64.getEncoder().encodeToString(signData), "UTF-8");
    }

    /**
     * 设置字体
     *
     * @param s 内容
     * @return 加了字体的内容
     */
    private static String font(String s) {
        return "<font color=\"#3296FA\">" + s + "</font>";
    }
}