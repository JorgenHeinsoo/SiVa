package ee.openeid.validation.service.timemark.report;

import ee.openeid.siva.validation.document.ValidationDocument;
import ee.openeid.siva.validation.document.report.SignatureScope;
import ee.openeid.siva.validation.document.report.ValidationWarning;
import ee.openeid.siva.validation.service.signature.policy.properties.ValidationPolicy;
import org.apache.commons.lang3.StringUtils;
import org.digidoc4j.Container;
import org.digidoc4j.DigestDataFile;
import org.digidoc4j.Signature;
import org.digidoc4j.SignatureProfile;
import org.digidoc4j.exceptions.DigiDoc4JException;
import org.digidoc4j.impl.ddoc.DDocContainer;
import org.digidoc4j.impl.ddoc.DDocFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DDOCContainerValidationReportBuilder extends TimemarkContainerValidationReportBuilder {
    public DDOCContainerValidationReportBuilder(Container container, ValidationDocument validationDocument, ValidationPolicy validationPolicy, List<DigiDoc4JException> containerErrors, boolean isReportSignatureEnabled) {
        super(container, validationDocument, validationPolicy, containerErrors, isReportSignatureEnabled);
    }

    @Override
    void addExtraValidationWarnings(List<ValidationWarning> validationWarnings) {
        ValidationWarning timestampValidationWarning = new ValidationWarning();
        timestampValidationWarning.setContent(DDOC_TIMESTAMP_WARNING);
        validationWarnings.add(timestampValidationWarning);
    }

    @Override
    List<ValidationWarning> getValidationWarningsForUnsignedDataFiles() {
        return new ArrayList<>();
    }

    @Override
    List<SignatureScope> getSignatureScopes(Signature signature, List<String> dataFilenames) {
        return dataFilenames
                .stream()
                .map(this::mapDataFile)
                .collect(Collectors.toList());
    }

    @Override
    String getSignatureForm() {
        return getDigidocXmlSignatureForm();
    }

    @Override
    String getSignatureFormat(SignatureProfile profile) {
        DDocFacade dDocFacade = ((DDocContainer) container).getDDoc4JFacade();
        return dDocFacade.getFormat().replaceAll("-", "_") + "_" + dDocFacade.getVersion();
    }

    private SignatureScope mapDataFile(String filename) {
        SignatureScope signatureScope = new SignatureScope();
        signatureScope.setName(filename);
        signatureScope.setContent(FULL_DOCUMENT);
        signatureScope.setScope(FULL_SIGNATURE_SCOPE);
        return signatureScope;
    }

    private String getDigidocXmlSignatureForm() {
        return DDOC_SIGNATURE_FORM_PREFIX + ((DDocContainer) container).getDDoc4JFacade().getVersion() + getSignatureFormSuffix();
    }

    private String getSignatureFormSuffix() {
        return isHashcodeForm() ? DDOC_HASHCODE_SIGNATURE_FORM_SUFFIX : StringUtils.EMPTY;
    }

    private boolean isHashcodeForm() {
        DDocFacade ddocFacade = ((DDocContainer) container).getDDoc4JFacade();
        return ddocFacade.getDataFiles().stream().anyMatch(dataFile -> {
                    if (dataFile instanceof DigestDataFile) {
                        return HASHCODE_CONTENT_TYPE.equals(((DigestDataFile) dataFile).getContentType());
                    }
                    return false;
                }
        );
    }
}
