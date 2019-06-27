package service;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TaxService {
    private String soapUrl      = "http://npchk.nalog.ru/FNSNDSCAWS_2?wsdl";
    private String requestUrl = "http://ws.unisoft/FNSNDSCAWS2/Request";
    private String action = "NdsRequest2";
    private String namespace  = "xs";

    public String getState(String[] data) throws SOAPException {

        SOAPConnection soapConnect = SOAPConnectionFactory.newInstance().createConnection();
        SOAPMessage soapMessage = postMessage(data);
        SOAPMessage soapResponse = soapConnect.call(soapMessage, soapUrl);
        SOAPElement soapElement = (SOAPElement) soapResponse.getSOAPBody().getChildElements().next();
        soapElement = (SOAPElement) soapElement.getChildElements().next();
        return soapElement.getAttributeValue(new QName("State"));
        }

        private SOAPMessage postMessage(String[] dataToCheck) throws SOAPException {

            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(namespace, requestUrl);
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem  = soapBody.addChildElement(action, namespace);
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("NP", namespace);
            for (String s : dataToCheck) {
                String qname = s.substring(0, s.indexOf("="));
                String value = s.substring(s.indexOf("=")+1);
                soapBodyElem1.addAttribute(new QName(qname), value);
            }

            return soapMessage;

        }

        public String getDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
            LocalDate localDate = LocalDate.now();
            return localDate.format(formatter);
        }

    }
