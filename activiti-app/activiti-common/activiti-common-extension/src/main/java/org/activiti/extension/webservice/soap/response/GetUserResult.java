package org.activiti.extension.webservice.soap.response;

import javax.xml.bind.annotation.XmlType;

import org.activiti.extension.webservice.soap.WsConstants;
import org.activiti.extension.webservice.soap.response.base.WSResult;
import org.activiti.extension.webservice.soap.response.dto.UserDTO;

@XmlType(name = "GetUserResult", namespace = WsConstants.NS)
public class GetUserResult extends WSResult {
	private UserDTO user;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
