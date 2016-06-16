package ee.openeid.validation.service.pdf.validator.process;

import ee.openeid.validation.service.pdf.validator.process.subprocess.EstonianValidationContextInitialisation;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.XmlDom;
import eu.europa.esig.dss.validation.policy.ProcessParameters;
import eu.europa.esig.dss.validation.policy.XmlNode;
import eu.europa.esig.dss.validation.policy.rules.*;
import eu.europa.esig.dss.validation.process.BasicBuildingBlocks;
import eu.europa.esig.dss.validation.process.subprocess.*;
import eu.europa.esig.dss.validation.report.Conclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is the customized version of {@link eu.europa.esig.dss.validation.process.BasicBuildingBlocks}
 */
public class EstonianBasicBuildingBlocks {

    private static final Logger LOG = LoggerFactory.getLogger(BasicBuildingBlocks.class);

    private XmlDom diagnosticData;

    private void prepareParameters(final ProcessParameters params) {

        diagnosticData = params.getDiagnosticData();
        isInitialised();
    }

    private void isInitialised() {

        if (diagnosticData == null) {
            final String message = String.format(ExceptionMessage.EXCEPTION_TCOPPNTBI, getClass().getSimpleName(), "diagnosticData");
            throw new DSSException(message);
        }
    }

    /**
     * This method lunches the construction process of basic building blocks.
     *
     * @param params validation process parameters
     * @return {@code XmlDom} representing the detailed report of this process.
     */
    public XmlDom run(final XmlNode mainNode, final ProcessParameters params) {

        prepareParameters(params);
        LOG.debug(getClass().getSimpleName() + ": start.");

        params.setContextName(NodeName.SIGNING_CERTIFICATE);

        final XmlNode basicBuildingBlocksNode = mainNode.addChild(NodeName.BASIC_BUILDING_BLOCKS);

        final List<XmlDom> signatures = diagnosticData.getElements("/DiagnosticData/Signature");

        for (final XmlDom signature : signatures) {

            final String type = signature.getValue("./@Type");
            if (AttributeValue.COUNTERSIGNATURE.equals(type)) {
                params.setCurrentValidationPolicy(params.getCountersignatureValidationPolicy());
            } else {
                params.setCurrentValidationPolicy(params.getValidationPolicy());
            }

            final Conclusion conclusion = new Conclusion();

            params.setSignatureContext(signature);
            params.setContextElement(signature);

            final String signatureId = signature.getValue("./@Id");
            final XmlNode signatureNode = basicBuildingBlocksNode.addChild(NodeName.SIGNATURE);
            signatureNode.setAttribute(AttributeName.ID, signatureId);

            final IdentificationOfTheSignersCertificate isc = new IdentificationOfTheSignersCertificate();
            final Conclusion iscConclusion = isc.run(params, NodeName.MAIN_SIGNATURE);
            signatureNode.addChild(iscConclusion.getValidationData());
            if (!iscConclusion.isValid()) {
                signatureNode.addChild(iscConclusion.toXmlNode());
                continue;
            }

            conclusion.addInfo(iscConclusion);
            conclusion.addWarnings(iscConclusion);

            final ValidationContextInitialisation vci = new EstonianValidationContextInitialisation();
            final Conclusion vciConclusion = vci.run(params, signatureNode);
            if (!vciConclusion.isValid()) {
                signatureNode.addChild(vciConclusion.toXmlNode());
                continue;
            }

            conclusion.addInfo(vciConclusion);
            conclusion.addWarnings(vciConclusion);

            final CryptographicVerification cv = new CryptographicVerification();
            final Conclusion cvConclusion = cv.run(params, signatureNode);
            if (!cvConclusion.isValid()) {
                signatureNode.addChild(cvConclusion.toXmlNode());
                continue;
            }

            conclusion.addInfo(cvConclusion);
            conclusion.addWarnings(cvConclusion);

            final SignatureAcceptanceValidation sav = new SignatureAcceptanceValidation();
            final Conclusion savConclusion = sav.run(params, signatureNode);
            if (!savConclusion.isValid()) {

                signatureNode.addChild(savConclusion.toXmlNode());
                continue;
            }
            conclusion.addInfo(savConclusion);
            conclusion.addWarnings(savConclusion);

            final X509CertificateValidation xcv = new X509CertificateValidation();
            final Conclusion xcvConclusion = xcv.run(params, NodeName.MAIN_SIGNATURE);
            signatureNode.addChild(xcvConclusion.getValidationData());
            if (!xcvConclusion.isValid()) {

                signatureNode.addChild(xcvConclusion.toXmlNode());
                continue;
            }
            conclusion.addInfo(xcvConclusion);
            conclusion.addWarnings(xcvConclusion);

            conclusion.setIndication(Indication.VALID);
            final XmlNode conclusionXmlNode = conclusion.toXmlNode();
            signatureNode.addChild(conclusionXmlNode);
        }
        final XmlDom bbbDom = basicBuildingBlocksNode.toXmlDom();
        params.setBBBData(bbbDom);
        return bbbDom;
    }

}