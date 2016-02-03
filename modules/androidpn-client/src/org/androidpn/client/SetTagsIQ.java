package org.androidpn.client;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
/**
 * 设置别名的IQ
 * @author hugs
 */
public class SetTagsIQ extends IQ {

	private String username;
	//此用户关注的一组标签的集合
	private List<String> tagList = new ArrayList<String>();
	
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("settags").append(" xmlns=\"").append(
                "androidpn:iq:settags").append("\">");
        if (username != null) {
            buf.append("<username>").append(username).append("</username>");
        }
        if (tagList!=null && !tagList.isEmpty()) {
			buf.append("<tags>");
			//表示是否需要一个分隔符,主要用于解决拼接字符串后最后一个字符为,号的问题
			//这是一个巧妙的算法
			boolean needSeperate = false;
			for (String tag : tagList) {
				if (needSeperate) {
					buf.append(",");	
				}
				buf.append(tag);
				needSeperate = true;
			}
			buf.append("</tags>");
		}
        buf.append("</").append("settags").append("> ");
        return buf.toString();
	}

}
