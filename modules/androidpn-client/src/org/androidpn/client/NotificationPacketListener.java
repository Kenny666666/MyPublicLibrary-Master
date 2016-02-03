/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.util.Log;

/** 
 * 此类用于获取服务器推送过来的消息  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains(
                    "androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                //                String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                String notificationImageUrl = notification.getImageUrl();
                //将消息保存到数据库
                NotificationHistory history = new NotificationHistory();
                history.setApiKey(notificationApiKey);
                history.setTitle(notificationTitle);
                history.setMessage(notificationMessage);
                history.setUri(notificationUri);
                history.setImageUrl(notificationImageUrl);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time = sdf.format(new Date());
                history.setTime(time);
                history.save();
                
                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY,notificationApiKey);
                intent.putExtra(Constants.NOTIFICATION_TITLE,notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE,notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, notificationImageUrl);
                
                xmppManager.getContext().sendBroadcast(intent);
                //给服务器发送一个消息回执，告诉服务器客户端已经接收到消息了
                DeliverConfirmIQ deliverConfirmIQ = new DeliverConfirmIQ();
                deliverConfirmIQ.setUuid(notificationId);
                deliverConfirmIQ.setType(IQ.Type.SET);
                xmppManager.getConnection().sendPacket(deliverConfirmIQ);
            }
        }

    }

}
