package com.asset.appwork.orgchart;

import com.asset.appwork.dto.Account;
import com.asset.appwork.otds.Otds;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.util.Http;
import com.asset.appwork.util.SystemUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by karim on 10/26/20.
 */
@Data
@Service
public class UserManagement {

    @Autowired
    Environment environment;

    public void create(Account account) throws IOException, AppworkException {
        String ticket = Otds.login(environment, "{" +
                "    \"userName\": \"" + account.getUsername() + "\"," +
                "    \"password\": \"" + account.getPassword() + "\"" +
                "}");
        if (ticket == null) throw new AppworkException("INVALID_CREDENTIALS", ResponseCode.INVALID_AUTH);
        String SAMLart = getSAMLart(ticket);
        account.setSAMLart(SAMLart);
        account.setTicket(ticket);
    }

    public Account create(String username, String password) throws IOException, AppworkException {
        String ticket = Otds.login(environment, "{" +
                "    \"userName\": \"" + username + "\"," +
                "    \"password\": \"" + password + "\"" +
                "}");
        if (ticket == null) throw new AppworkException("INVALID_CREDENTIALS", ResponseCode.INVALID_AUTH);
        String SAMLart = getSAMLart(ticket);

        Account account = new Account();
        account.setSAMLart(SAMLart);
        account.setTicket(ticket);
        return account;
    }

    private String getSAMLart(String ticket) throws IOException {
        String gatewayUrl =  environment.getProperty("server.request")+"://"+environment.getProperty("appwork.domain")+":"+environment.getProperty("appwork.port")+"/home/"+environment.getProperty("appwork.organization")+"/"+environment.getProperty("appwork.gateway");

        Http http = new Http().setContentType(Http.ContentType.XML_REQUEST)
                .setData(samlartRequest(ticket))
                .post(gatewayUrl);
        return SystemUtil.getJsonByPtrExpr(SystemUtil.convertXMLtoJSON(http.getResponse()), "/Body/Response/AssertionArtifact");
    }

    private String samlartRequest(String ticket){
        return "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "    <SOAP:Header>" +
                "        <OTAuthentication xmlns=\"urn:api.bpm.opentext.com\">" +
                "            <AuthenticationToken>" +
                ticket +
                "            </AuthenticationToken>" +
                "        </OTAuthentication>" +
                "    </SOAP:Header>" +
                "    <SOAP:Body>" +
                "        <samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" IssueInstant=\"2018-09-07T16:47:13.359Z\" RequestID=\"a5470c392e-264e-jopl-56ac-4397b1b416d\">" +
                "            <samlp:AuthenticationQuery>" +
                "                <saml:Subject xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\">" +
                "                    <saml:NameIdentifier Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\"/>" +
                "                </saml:Subject>" +
                "            </samlp:AuthenticationQuery>" +
                "        </samlp:Request>" +
                "    </SOAP:Body>" +
                "</SOAP:Envelope>";
    }

}
