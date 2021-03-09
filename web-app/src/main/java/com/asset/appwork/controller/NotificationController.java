package com.asset.appwork.controller;

import com.asset.appwork.config.TokenService;
import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Unit;
import com.asset.appwork.platform.soap.Notifications;
import com.asset.appwork.response.AppResponse;
import com.asset.appwork.service.ApprovalHistoryService;
import com.asset.appwork.service.CordysService;
import com.asset.appwork.service.UserService;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/notification")
@RestController
public class NotificationController {
    @Autowired
    TokenService tokenService;
    @Autowired
    CordysService cordysService;
    @Autowired
    ApprovalHistoryService approvalHistoryService;
    @Autowired
    UserService userService;

    @Transactional
    @GetMapping("/get/all")
    public ResponseEntity<AppResponse<JsonNode>> getNotifications(@RequestHeader("X-Auth-Token") String token) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Notifications notificationSoapMessages = new Notifications();
            Account account = tokenService.get(token);
            ObjectMapper objectMapper = new ObjectMapper();
            if (account != null) {
                String response = cordysService.sendRequest(account, notificationSoapMessages.getNotifications());
                Document document = SystemUtil.convertStringToXMLDocument(response);
                NodeList notifications = document.getElementsByTagName("Notification");
                ArrayNode arrayNode = objectMapper.createArrayNode();
                if (notifications.getLength() > 0) {
                    for (int i = 0; i < notifications.getLength(); i++) {
                        JsonNode jsonNode = SystemUtil.convertDocumentNodeToJsonNode(notifications.item(i));

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date deliveryDate = simpleDateFormat.parse(jsonNode.get("DeliveryDate").textValue());
                        simpleDateFormat.applyPattern("yyyy-MM-dd , hh:mm:ss a");
                        ((ObjectNode) jsonNode).put("DeliveryDate", simpleDateFormat.format(deliveryDate));

                        arrayNode.add(jsonNode);
                    }
                }

                respBuilder.data(arrayNode);
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

    @Transactional
    @GetMapping("/read/{id}")
    public ResponseEntity<AppResponse<JsonNode>> getNotificationById(@RequestHeader("X-Auth-Token") String token,@PathVariable String id) {
        AppResponse.ResponseBuilder<JsonNode> respBuilder = AppResponse.builder();
        try {
            Notifications notificationSoapMessages = new Notifications();
            Account account = tokenService.get(token);
            ObjectMapper objectMapper = new ObjectMapper();
            if (account != null) {
                String response = cordysService.sendRequest(account, notificationSoapMessages.readNotification(id));
                Document document = SystemUtil.convertStringToXMLDocument(response);
                NodeList notifications = document.getElementsByTagName("Notification");
                ObjectNode objectNode = objectMapper.createObjectNode();
                if (notifications.getLength() > 0) {
                    objectNode = (ObjectNode)SystemUtil.convertDocumentNodeToJsonNode(notifications.item(0));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date deliveryDate = simpleDateFormat.parse(objectNode.get("DeliveryDate").textValue());
                    simpleDateFormat.applyPattern("yyyy-MM-dd , hh:mm:ss a");
                    objectNode.put("DeliveryDate", simpleDateFormat.format(deliveryDate));
                }

                respBuilder.data(objectNode);
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (AppworkException e) {
            e.printStackTrace();
            respBuilder.status(e.getCode());
        }
        return respBuilder.build().getResponseEntity();
    }

}
