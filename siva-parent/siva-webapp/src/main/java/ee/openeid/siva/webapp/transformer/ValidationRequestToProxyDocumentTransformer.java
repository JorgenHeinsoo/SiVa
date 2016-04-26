package ee.openeid.siva.webapp.transformer;


import ee.openeid.siva.proxy.document.typeresolver.DocumentTypeResolver;
import ee.openeid.siva.proxy.document.typeresolver.ReportTypeResolver;
import ee.openeid.siva.proxy.document.ProxyDocument;
import ee.openeid.siva.proxy.document.RequestProtocol;
import ee.openeid.siva.webapp.request.ValidationRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class ValidationRequestToProxyDocumentTransformer {

    public ProxyDocument transform(ValidationRequest validationRequest) {
        ProxyDocument proxyDocument = new ProxyDocument();
        proxyDocument.setName(validationRequest.getFilename());
        proxyDocument.setDocumentType(DocumentTypeResolver.documentTypeFromString(validationRequest.getDocumentType()));
        proxyDocument.setBytes(Base64.decodeBase64(validationRequest.getDocument()));
        proxyDocument.setReportType(ReportTypeResolver.reportTypeFromString(validationRequest.getReportType()));
        proxyDocument.setRequestProtocol(RequestProtocol.JSON);
        return proxyDocument;
    }

}