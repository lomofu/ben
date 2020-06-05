package com.ben.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.ben.bot.dto.mail.JobDto;
import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.sms.config.AlibabaSmsConfiguration;
import com.ben.sms.config.JDSmsConfiguration;
import com.ben.sms.dto.SendNotificationDto;
import com.ben.sms.dto.SendScheduleDto;
import com.ben.sms.service.SmsService;
import com.google.gson.Gson;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResponse;
import com.jdcloud.sdk.service.sms.model.StatusReportRequest;
import com.jdcloud.sdk.service.sms.model.StatusReportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @date 2020/3/8 22:43
 */
@Service
@Slf4j
public class SmeServiceImpl implements SmsService {
  public static final String PHONE_CODE = "phoneCode";

  public static final String LOGIN_TO_MUCH = "loginToMuch";

  public static final String NOTIFICATION = "notification";

  public static final String SCHEDULE = "schedule";

  @Resource private JDSmsConfiguration jdSmsConfiguration;

  @Resource private AlibabaSmsConfiguration alibabaSmsConfiguration;

  @Resource private SmsClient smsClient;

  @Resource private IAcsClient acsClient;

  @Override
  public void sendPhoneCode(String phoneNumber, String code) {
    String templateId = getJDTemplateId(PHONE_CODE);
    List<String> phoneList = new ArrayList<>(1);
    List<String> params = new ArrayList<>(1);

    phoneList.add(phoneNumber);
    params.add(code);

    sendSMSWithJD(phoneList, params, templateId);
  }

  @Override
  public void sendLoginToMuch(String phoneNumber) {
    String templateId = getJDTemplateId(LOGIN_TO_MUCH);
    List<String> phoneList = new ArrayList<>(1);
    List<String> params = new ArrayList<>(1);

    phoneList.add(phoneNumber);
    params.add(phoneNumber);

    sendSMSWithJD(phoneList, params, templateId);
  }

  @Override
  public void sendNotification(List<String> phoneNumber, String templateParam) {
    SendNotificationDto sendNotificationDto =
        SendNotificationDto.builder().title(templateParam).build();
    String templateId = getALTemplateId(NOTIFICATION);
    String jsonString = JSON.toJSONString(sendNotificationDto);
    String phone = String.join(",", phoneNumber);
    sendSMSWithAL(phone, jsonString, templateId);
  }

  @Override
  public void sendSchedule(ScheduleDto scheduleDto) {
    SendScheduleDto sendScheduleDto =
        SendScheduleDto.builder().shcedule(this.getScheduleToJson(scheduleDto)).build();
    String templateId = getALTemplateId(SCHEDULE);
    String jsonString = JSON.toJSONString(sendScheduleDto);
    String phone = String.join(",", scheduleDto.getTo());
    sendSMSWithAL(phone, jsonString, templateId);
  }

  protected String getScheduleToJson(ScheduleDto scheduleDto) {
    List<JobDto> jobDtoList =
        scheduleDto.getList().stream()
            .map(e -> JSON.parseObject(e, JobDto.class))
            .collect(Collectors.toList());
    StringBuffer buffer = new StringBuffer();
    for (JobDto job : jobDtoList) {
      buffer.append(job.getName()).append(" ");
    }
    String string = buffer.toString();
    if (string.length() < 20) {
      return string;
    }
    return buffer.toString().substring(0, 15).concat("...");
  }

  protected void sendSMSWithAL(String phone, String templateParam, String templateId) {
    CommonRequest request = new CommonRequest();
    request.setDomain(alibabaSmsConfiguration.getDomain());
    request.setVersion(alibabaSmsConfiguration.getVersion());
    request.setMethod(MethodType.POST);
    request.setAction("SendSms");
    request.putQueryParameter("RegionId", alibabaSmsConfiguration.getRegion());
    request.putQueryParameter("PhoneNumbers", phone);
    request.putQueryParameter("SignName", alibabaSmsConfiguration.getSignId());
    request.putQueryParameter("TemplateCode", templateId);
    request.putQueryParameter("TemplateParam", templateParam);
    try {
      CommonResponse response = acsClient.getCommonResponse(request);
      log.info("阿里结果: {}", response.getData());
    } catch (ClientException e) {
      log.error("阿里发送失败: {}", e.getErrMsg());
    }
  }

  protected void sendSMSWithJD(List<String> phoneList, List<String> params, String templateId) {
    BatchSendRequest request = new BatchSendRequest();
    StatusReportRequest statusReportRequest = new StatusReportRequest();
    String region = jdSmsConfiguration.getRegion();
    String signId = jdSmsConfiguration.getSignId();

    request.setRegionId(region);
    request.setTemplateId(templateId);
    request.setSignId(signId);
    request.setPhoneList(phoneList);
    request.setParams(params);

    BatchSendResponse response = smsClient.batchSend(request);
    statusReportRequest.setRegionId(region);
    statusReportRequest.setSequenceNumber(response.getResult().getData().getSequenceNumber());
    statusReportRequest.setPhoneList(phoneList);
    StatusReportResponse reportResponse = smsClient.statusReport(statusReportRequest);
    log.info("京东结果: {}", new Gson().toJson(reportResponse));
  }

  protected String getJDTemplateId(String key) {
    return jdSmsConfiguration.getTemplate().get(key);
  }

  protected String getALTemplateId(String aKey) {
    return alibabaSmsConfiguration.getTemplate().get(aKey);
  }
}
