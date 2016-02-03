package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;
/**
 * 自定义IQ
 * @author hugs
 */
public class DeliverConfirmIQ extends IQ {

	private String uuid;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("deliverconfirm").append(" xmlns=\"").append(
                "androidpn:iq:deliverconfirm").append("\">");
        if (uuid != null) {
            buf.append("<uuid>").append(uuid).append("</uuid>");
        }
        buf.append("</").append("deliverconfirm").append("> ");
        return buf.toString();
	}

}
